package com.kol.dct

object DCT {
  private val blockSize: Int = 8
  private val denominator: Double = 2.0 * blockSize

  def dct(x: Array[Array[Double]]): Array[Array[Double]] = {
    val (height, width) = (x.length, x(0).length)
    val (blocksHeight, blocksWidth) = (height / blockSize, width / blockSize)

    val result: Array[Array[Double]] = Array.ofDim(height, width)
    for(b1: Int <- 0 until blocksHeight; b2: Int <- 0 until blocksWidth) {
      for (p: Int <- 0 until blockSize; q: Int <- 0 until blockSize) {
        var sum = 0.0
        for (m: Int <- 0 until blockSize; n: Int <- 0 until blockSize) {
          sum += x(b1 * blockSize + m)(b2 * blockSize + n) *
            Math.cos((Math.PI * (2 * m + 1) * p) / denominator) *
            Math.cos((Math.PI * (2 * n + 1) * q) / denominator)
        }
        result(blockSize * b1 + p)(blockSize * b2 + q) = 2.0 / blockSize * c(q) * c(p) * sum
      }
    }
    result
  }

  def idct(in: Array[Array[Double]]): Array[Array[Double]] = {
    val (height, width) = (in.length, in(0).length)
    val (blocksHeight, blocksWidth) = (height / blockSize, width / blockSize)

    val result: Array[Array[Double]] = Array.ofDim(height, width)
    for(b1: Int <- 0 until blocksHeight; b2: Int <- 0 until blocksWidth) {
      val (p, q) = (b1 * blockSize, b2 * blockSize)
      for (x <- p until p + blockSize; y <- q until q + blockSize) {
        var sum = 0.0
        for (i: Int <- 0 until blockSize; j: Int <- 0 until blockSize) {
          sum += c(i) * c(j) * in(p + i)(q + j) *
            Math.cos((Math.PI * (2 * x + 1) * i) / denominator) *
            Math.cos((Math.PI * (2 * y + 1) * j) / denominator)
        }
        result(idx(b1, x))(idx(b2, y)) = 1.0 / math.sqrt(2.0 * blockSize) * sum
      }
    }
    result
  }

  private def c(i: Int): Double = if(i == 0) 1.0/math.sqrt(2) else 1.0

  private def idx(b: Int, i: Int): Int = if(b % 2 == 0) i else b * blockSize + ((b + 1) * blockSize) - i - 1
}