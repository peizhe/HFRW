package com.kol

class Complex(val real: Double, val imaginary: Double) {

  private val eps: Double = 1e-4

  override def toString: String =
    if(math.abs(imaginary) < eps) "%.3f".format(real)
    else if(math.abs(real) < eps) "%.3f".format(imaginary)
    else if(imaginary < 0) "%.3f".format(real) + " - " + "%.3f".format(math.abs(imaginary)) + "i"
    else "%.3f".format(real) + " + " + "%.3f".format(imaginary) + "i"

  /**
   * Absolute value of this complex number
   * @return double value (Math.sqrt(real*real + imaginary*imaginary))
   */
  def abs: Double = Math.sqrt(real * real + imaginary * imaginary)

  /**
   * Return angle/phase/argument (between -pi and pi)
   */
  def phase: Double = Math.atan2(imaginary, real)

  /**
   * Return a new Complex object whose value is (this + b)
   * @param b - complex number
   * @return this + b
   */
  def +(b: Complex): Complex = new Complex(this.real + b.real, this.imaginary + b.imaginary)

  /**
   * Return a new Complex object whose value is (this - b)
   * @param b - complex number
   * @return this - b
   */
  def -(b: Complex): Complex = new Complex(this.real - b.real, this.imaginary - b.imaginary)

  /**
   * Return a new Complex object whose value is (this * b)
   * @param b - complex number
   * @return this * b
   */
  def *(b: Complex): Complex = new Complex(this.real * b.real - this.imaginary * b.imaginary, this.real * b.imaginary + this.imaginary * b.real)

  /**
   * Scalar multiplication
   * Return a new object whose value is (this * number)
   * @param number - complex number
   * @return this * number
   */
  def *(number: Double): Complex = new Complex(number * real, number * imaginary)

  /**
   * @return a new Complex object whose value is the reciprocal of this
   */
  def reciprocal: Complex = {
    val tmp: Double = real * real + imaginary * imaginary
    new Complex(real / tmp, -imaginary / tmp)
  }

  /**
   * @return a new Complex object whose value is the conjugate of this
   */
  def conjugate: Complex = new Complex(real, -imaginary)

  /**
   * Return a new Complex object whose value is (this / b)
   * @param b - complex number
   * @return this / b
   */
  def /(b: Complex): Complex = this * b.reciprocal

  /**
   * @return a new Complex object whose value is the complex exponential of this
   */
  def exp: Complex = new Complex(Math.exp(real) * Math.cos(imaginary), Math.exp(real) * Math.sin(imaginary))

  /**
   * @return a new Complex object whose value is the complex sine of this
   */
  def sin: Complex = new Complex(Math.sin(real) * Math.cosh(imaginary), Math.cos(real) * Math.sinh(imaginary))

  /**
   * @return a new Complex object whose value is the complex cosine of this
   */
  def cos: Complex = new Complex(Math.cos(real) * Math.cosh(imaginary), Math.sin(real) * Math.sinh(imaginary))

  /**
   * @return a new Complex object whose value is the complex tangent of this
   */
  def tan: Complex = sin / cos
}

object C {
  def apply(real: Double, imaginary: Double): Complex = new Complex(real, imaginary)
  def apply(real: Double): Complex = new Complex(real, 0)
}