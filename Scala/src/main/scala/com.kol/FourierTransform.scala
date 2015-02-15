package com.kol

/** Cormen, Leiserson, Rivest, Stein. Introduction to Algorithms, 2nd Ed.
  * Chapter 30.2-3 Polynomials and the FFT
  * The DFT an FFT. Efficient FFT implementations
  */
object FourierTransform {

  /**============================================ Fast Fourier Transform ===========================================**/
  /**
   * Count fft for vector
   * Length of vector must be power of 2
   * @param x - input vector for counts fft
   * @return vector after fft
   */
  def fft(x: Array[Complex]): Array[Complex] = {
    val length: Int = x.length
    if (length == 1) {
      return Array[Complex](x(0))
    }
    val size: Int = length / 2
    val q: Array[Complex] = fft((for {i <- 0 until size} yield x(i * 2)).toArray)
    val r: Array[Complex] = fft((for {i <- 0 until size} yield x(i * 2 + 1)).toArray)

    val result: Array[Complex] = Array.ofDim(length)
    for (i <- 0 until size) {
      val w: Double = -2 * i * Math.PI / length
      val wk: Complex = C(Math.cos(w), Math.sin(w))
      result(i) = q(i) + (wk * r(i))
      result(i + size) = q(i) - (wk * r(i))
    }
    result
  }

  def fft(x: Array[Double]): Array[Complex] = fft(x.map(C(_, 0)))
  /**============================================ Fast Fourier Transform ===========================================**/

  /**======================================== Inverse Fast Fourier Transform ========================================**/
  /**
   * Count invert fft for vector
   * Length of vector must be power of 2
   * @param x - input vector for counts invert fft
   * @return vector after invert fft
   */
  def ifft(x: Array[Complex]): Array[Complex] = fft(x.map(_.conjugate)).map(_.conjugate).map(_ * (1.0 / x.length))

  def ifft(x: Array[Double]): Array[Complex] = ifft(x.map(C(_, 0)))
  /**======================================== Inverse Fast Fourier Transform ========================================**/

  /**=================================== Discrete Fourier Transform first variant ==================================**/
  /**
   * Count dftOneElement for vector
   * @param x input vector for counts dftOneElement
   * @return vector after dftOneElement
   */
  def dft(x: Array[Double]): Array[Complex] =
    (for {i <- 0 until x.length} yield dftOneElement(x, x.length, i)).toArray.map(_.conjugate)

  /**
   * Counts one complex element for dftOneElement vector
   * @param x - input vector for dftOneElement
   * @param idx - current index of element for counts
   * @return complex number
   */
  private def dftOneElement(x: Array[Double], length: Int, idx: Int): Complex =
    (for {k <- 0 until length} yield ((C(0, -1) * (-2 * Math.PI * k * idx)) / C(length, 0)).exp * x(k)).reduce(_ + _)
  /**=================================== Discrete Fourier Transform first variant ==================================**/

  /**=================================== Discrete Fourier Transform second variant ==================================**/
  def dftC(x: Array[Complex]): Array[Complex] = {
    val length: Int = x.length
    val res: Array[Complex] = Array.ofDim(length)
    for {k <- 0 until length} {
      val value: IndexedSeq[(Double, Double)] = for {t <- 0 until length; angle: Double = 2 * Math.PI * t * k / length}
      yield (x(t).real * Math.cos(angle) + x(t).imaginary * Math.sin(angle), -x(t).real * Math.sin(angle) + x(t).imaginary * Math.cos(angle))
      res(k) = C(value.map(_._1).sum, value.map(_._2).sum)
    }
    res
  }

  def dftC(x: Array[Double]): Array[Complex] = dftC(x.map(C(_, 0)))
  /**=================================== Discrete Fourier Transform second variant ==================================**/

  /**====================================== Inverse Discrete Fourier Transform ======================================**/
  def idftC(x: Array[Complex]): Array[Complex] = dftC(x.map(_.conjugate)).map(_.conjugate).map(_ * (1.0 / x.length))

  def idftC(x: Array[Double]): Array[Complex] = idftC(x.map(C(_, 0)))
  /**====================================== Inverse Discrete Fourier Transform ======================================**/

  /**========================================= Iterative Fast Fourier Transform =====================================**/
  private def bitReverseCopy(x: Array[Complex]): Array[Complex] = {
    val res: Array[Complex] = Array.ofDim(x.length)
    val size: Int = Integer.toBinaryString(x.length).size
    for {k <- 0 until x.length} res(reverseBits(k, size)) = x(k)
    res
  }

  private def reverseBits(x: Int, size: Int): Int = {
    val string: String = Integer.toBinaryString(x)
    Integer.parseInt((Array.fill(size - string.size - 1)(0).mkString("") + string).reverse, 2)
  }

  def iterativeFFT(x: Array[Complex]): Array[Complex] = {
    val copy: Array[Complex] = bitReverseCopy(x)
    for(s <- 1 to (math.log(x.length)/math.log(2)).round.toInt) {
      val m: Int = Math.pow(2, s).toInt
      val wm: Complex = (C(0, -1) * (-2 * Math.PI / m)).exp
      for(k <- 0 to x.length - 1 by m) {
        var w: Complex = C(1, 0)
        for(j <- 0 to m/2 - 1) {
          val t: Complex = w * copy(k + j + m/2)
          val u: Complex = copy(k + j)
          copy(k + j) = u + t
          copy(k + j + m/2) = u - t
          w = w * wm
        }
      }
    }
    copy.map(_.conjugate)
  }

  def iterativeFFT(x: Array[Double]): Array[Complex] = iterativeFFT(x.map(C(_, 0)))
  /**========================================= Iterative Fast Fourier Transform =====================================**/
}