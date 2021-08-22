import blockchain.{Block, BlockchainUtils}

/**
  * Runner for our simple blockchain. Wraps our main method.
  */
object SimpleBlockchainRunner {

  def main(args: Array[String]): Unit = {
    val block = Block(
      "",
      "",
      "Hello here",
      0L
    )

    val nonce = BlockchainUtils.mineBlock(2, block)
    println("I found the nonce: ")
    println(nonce)

    println("Bye now :)")
  }
}
