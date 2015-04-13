package com.kol

object Utils {
  def int2doubleArray(in: Array[Array[Int]]): Array[Array[Double]] = in.map(row => row.map(_ * 1.0))

  def sum(in: Array[Array[Double]]): Double = in.map(_.sum).sum

  def mean(in: Array[Array[Double]]): Double = sum(in)/in.count(_ => true)

  def toInt(array: Array[Array[Double]]): Array[Array[Int]] = array.map(v => v.map(math.round(_).toInt))

  def toDouble(array: Array[Array[Int]]): Array[Array[Double]] = array.map(v => v.map(_.toDouble))
}
