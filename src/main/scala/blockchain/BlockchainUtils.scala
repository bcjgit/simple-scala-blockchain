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
      Option.apply(
        MessageDigest
          .getInstance(SHA_256)
          .digest(SerializationUtils.serialise(block))
      )
    } catch {
      case e: NoSuchAlgorithmException => {
        logger.error("Failed to load SHA-256 hash function")
        Option.empty
      }
      case e: UnsupportedEncodingException => {
        logger.error("UnsupportedEncodingException when hashing block")
        Option.empty
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
}
