package blockchain

import com.typesafe.scalalogging.Logger
import util.SerializationUtils

import java.io.UnsupportedEncodingException
import java.security.{MessageDigest, NoSuchAlgorithmException}
import scala.annotation.tailrec

/**
  * Set of util functions for common chain operations
  */
object BlockchainUtils {

  val logger: Logger = Logger(BlockchainUtils.getClass)
  val SHA_256 = "SHA-256"

  /**
    * Calculates the SHA-256 hash of given block
    * @param block Block in our blockchain
    * @return SHA-256 hash of given block or none if error occurs during encoding
    */
  def calculateBlockHash(block: Block): Option[Array[Byte]] = {
    try {
      Some(
        MessageDigest
          .getInstance(SHA_256)
          .digest(SerializationUtils.serialise(block))
      )
    } catch {
      case e: NoSuchAlgorithmException => {
        logger.error("Failed to load SHA-256 hash function")
        None
      }
      case e: UnsupportedEncodingException => {
        logger.error("UnsupportedEncodingException when hashing block")
        None
      }
    }
  }

  /**
    * Checks if hash starts with prefix number of zeros
    * @param prefix number of zeros we want the hash to start with
    * @param hash the hash
    * @return boolean true iff hash contains prefix number of zeros at start
    */
  def checkHashContainsDesiredZeroPrefixLength(
      prefix: Int,
      hash: Array[Byte]
  ): Boolean = {
    hash.startsWith(Array.fill[Byte](prefix)(0))
  }

  /**
    * Mine a new block (i.e. search for nonce value given a block and a prefix)
    * @param prefix number of zeros we need to see in the start of the hash
    * @param block Block object containing a starter nonce
    * @return Optional either empty or nonce value
    */
  @tailrec
  def mineBlock(prefix: Int, block: Block): Option[Int] = {
    val hash = calculateBlockHash(block)

    // This only occurs if there was an error ... honestly not clear we even want to do this
    if (hash.isEmpty) {
      return Option.empty
    }

    // If we've found the nonce, return it
    if (checkHashContainsDesiredZeroPrefixLength(prefix, hash.get)) {
      Option.apply(block.nonce)
    } else {
      // Otherwise, iterate the nonce and recurse
      mineBlock(
        prefix,
        Block(
          block.hash,
          block.previousHash,
          block.data,
          block.timeStamp,
          block.nonce + 1
        )
      )
    }

  }

  /**
    * Check that the hash value included in the block is indeed the block hash
    * @param block [[Block]] who's hash we want to check
    * @return boolean true iff block hash matches hash value included in block
    */
  def blockHashMatchesContainedHash(block: Block): Boolean = {
    calculateBlockHash(block) match {
      case Some(hashArr) => block.hash.equals(hashArr)
      case None          => false
    }
  }

  /**
    * Validate blockchain on non genisis blocks
    * @param blockchain List of [[Block]]s
    * @param prefix int prefix length of 0s in our hash
    * @param prevBlockHash hash of previous block
    * @return boolean true iff head block in chain is valid
    */
  @tailrec
  def validateChainTail(
      blockchain: List[Block],
      prefix: Int,
      prevBlockHash: Array[Byte]
  ): Boolean = {
    if (blockchain.isEmpty) {
      return true
    }
    val block = blockchain.head
    if (
      !(prevBlockHash.sameElements(
        block.previousHash.get
      ) && blockHashMatchesContainedHash(
        block
      ) && checkHashContainsDesiredZeroPrefixLength(prefix, block.hash))
    ) {
      false
    } else {
      validateChainTail(blockchain.tail, prefix, block.previousHash.get)
    }
  }

  def chainIsValid(blockchain: List[Block], prefix: Int): Boolean = {
    if (blockchain.isEmpty) {
      return true
    }
    // Verify first block as a special case (it has no previous hash)
    val genesisBlock = blockchain.head
    if (
      !(blockHashMatchesContainedHash(
        genesisBlock
      ) && checkHashContainsDesiredZeroPrefixLength(prefix, genesisBlock.hash))
    ) {
      false
    } else {
      // TODO do we need this?
      validateChainTail(blockchain.tail, prefix, genesisBlock.hash)
    }
  }
}
