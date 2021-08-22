import blockchain.{Block, BlockchainUtils}
import com.typesafe.scalalogging.Logger

/**
  * Runner for our simple blockchain. Wraps our main method.
  */
object SimpleBlockchainRunner {

  val logger: Logger = Logger(SimpleBlockchainRunner.getClass)

  def main(args: Array[String]): Unit = {
    val block = Block(
      Array.empty,
      Option.empty,
      "Hello here",
      0L
    )

    val nonce = BlockchainUtils.mineBlock(2, block)
    if (nonce.isEmpty) {
      logger.error("Block mining failed")
    } else {
      logger.info("Block mined successfully. Nonce value: {}", nonce.get)
    }
  }
}
