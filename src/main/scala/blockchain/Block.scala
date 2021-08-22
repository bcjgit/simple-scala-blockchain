package blockchain

/**
  * Block for blockchain
  * @param hash String hash (SHA-256) of this block
  * @param previousHash String hash (SHA-265) of previous block in chain
  * @param data String data of this block
  * @param timeStamp Long timestamp when this block was mined
  * @param nonce Int nonce value
  */
case class Block(
    hash: String,
    previousHash: String,
    data: String,
    timeStamp: Long,
    // Default to 0 nonce and then iterate
    nonce: Int = 0
)
