package com.kol

object Utils {
  def int2doubleArray(in: Array[Array[Int]]): Array[Array[Double]] = in.map(row => row.map(_ * 1.0))

  def sum(in: Array[Array[Double]]): Double = in.map(_.sum).sum

  def mean(in: Array[Array[Double]]): Double = sum(in)/in.count(_ => true)

  def toInt(array: Array[Array[Double]]): Array[Array[Int]] = array.map(v => v.map(math.round(_).toInt))

  def toDouble(array: Array[Array[Int]]): Array[Array[Double]] = array.map(v => v.map(_.toDouble))

  def toDouble(array: Array[Int]): Array[Double] = array.map(_.toDouble)

  def sum(f: Array[Int], s: Array[Int]): Array[Int] = f.zip(s).map(v => v._1 + v._2)

  def diff(f: Array[Int], s: Array[Int]): Array[Int] = f.zip(s).map(v => v._1 - v._2)

  def transpose(in: Array[Array[Int]]): Array[Array[Int]] = in.transpose
}
