package com.kol

import java.awt.Color
import java.awt.image.BufferedImage

class RGBImage(val width: Int, val height: Int) {
  val red: Array[Array[Int]] = Array.ofDim(width, height)
  val blue: Array[Array[Int]] = Array.ofDim(width, height)
  val green: Array[Array[Int]] = Array.ofDim(width, height)

  def this(width: Int, height: Int, red: Array[Array[Int]], blue: Array[Array[Int]], green: Array[Array[Int]]){
    this(width, height)
    for {
      i <- 0 until width
      j <- 0 until height
    } {
      this.red(i)(j) = red(i)(j)
      this.blue(i)(j) = blue(i)(j)
      this.green(i)(j) = green(i)(j)
    }
  }

  /**
   * creates an image from arrays with the RGB pixels
   * @return the BufferedImage represented by this object
   */
  def toBufferedImage: BufferedImage = {
    val rgb: Array[Int] = (for {
      i <- 0 until width
      j <- 0 until height
    } yield (red(i)(j), green(i)(j), blue(i)(j))).map(c => getIntRGB(c._1, c._2, c._3)).toArray
    val img: BufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    img.setRGB(0, 0, width, height, rgb, 0, width)
    img
  }

  /**
   * creates an image from red RGB channel
   * @return the BufferedImage represented by this object
   */
  def toRedBufferedImage: BufferedImage = toOneColorBufferedImage(red)
  def toRed: Array[Int] = toOneColor(red)

  /**
   * creates an image from blue RGB channel
   * @return the BufferedImage represented by this object
   */
  def toBlueBufferedImage: BufferedImage = toOneColorBufferedImage(blue)
  def toBlue: Array[Int] = toOneColor(blue)

  /**
   * creates an image from green RGB channel
   * @return the BufferedImage represented by this object
   */
  def toGreenBufferedImage: BufferedImage = toOneColorBufferedImage(green)
  def toGreen: Array[Int] = toOneColor(green)

  def meanRed: Double = toRed.sum / (width * height * 1.0)
  def meanBlue: Double = toBlue.sum / (width * height * 1.0)
  def meanGreen: Double = toGreen.sum / (width * height * 1.0)

  private def toOneColorBufferedImage(color: Array[Array[Int]]): BufferedImage = {
    val c: Array[Int] = (for {
      i <- 0 until width
      j <- 0 until height
    } yield color(i)(j)).toArray
    val img: BufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    img.setRGB(0, 0, width, height, c, 0, width)
    img
  }

  private def toOneColor(color: Array[Array[Int]]): Array[Int] = {
    (for {
      i <- 0 until width
      j <- 0 until height
    } yield color(i)(j)).toArray
  }

  /**
   * checks to make sure the RGB value is valid. Makes it less than 256 and non-negative
   * @param value the value of the pixel to be check
   * @return a pixel that is 0-255
   */
  private def check(value: Int): Int = if (value > 255) 255 else if (value < 0) 0 else value

  private def getIntRGB(red: Int, green: Int, blue: Int): Int = new Color(check(red), check(green), check(blue)).getRGB
}

object RGBImage {
  def apply(width: Int, height: Int): RGBImage = new RGBImage(width, height)

  /**
   * reads the pixels from a BufferedImage
   * @param image the BufferedImage to be getting the pixels from
   * @return a object RGBImage that has all of the arrays with red, green, and blue pixels in it
   */
  def fromBufferedImage(image: BufferedImage): RGBImage = {
    val width = image.getWidth
    val height = image.getHeight
    val red: Array[Array[Int]] = Array.ofDim(width, height)
    val blue: Array[Array[Int]] = Array.ofDim(width, height)
    val green: Array[Array[Int]] = Array.ofDim(width, height)
    for {
      i <- 0 until width
      j <- 0 until height
    } {
      val rgb: Int = image.getRGB(i, j)
      red(i)(j) = toRed(rgb)
      blue(i)(j) = toBlue(rgb)
      green(i)(j) = toGreen(rgb)
    }
    new RGBImage(width, height, red, blue, green)
  }

  def toBlue(pixel: Int): Int = pixel & 0x000000FF
  def toRed(pixel: Int): Int = (pixel >> 16) & 0x000000FF
  def toGreen(pixel: Int): Int = (pixel >> 8) & 0x000000FF
}