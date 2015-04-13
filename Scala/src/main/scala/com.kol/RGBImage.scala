package com.kol

import java.awt.image.BufferedImage

class RGBImage(val content: Array[Array[Int]], val width: Int, val height: Int) {
  /**
   * creates an image from arrays with the RGB pixels
   * @return the BufferedImage represented by this object
   */
  def toBufferedImage: BufferedImage = {
    val img: BufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    for(i <- 0 until width; j <- 0 until height) img.setRGB(i, j, content(i)(j))
    img
  }

  lazy val vectorContent: Array[Int] = content.flatten

  def mean: Double = vectorContent.sum / (width * height * 1.0)
}

object RGBImage {
  def apply(content: Array[Array[Int]], width: Int, height: Int): RGBImage = new RGBImage(content, width, height)
  /**
   * reads the pixels from a BufferedImage
   * @param image the BufferedImage to be getting the pixels from
   * @return a object RGBImage
   */
  def fromBufferedImage(image: BufferedImage): RGBImage = {
    val (width, height) = (image.getWidth, image.getHeight)
    val rgb: Array[Array[Int]] = Array.ofDim(width, height)
    for {i <- 0 until width; j <- 0 until height} {
      rgb(i)(j) = image.getRGB(i, j)
    }
    new RGBImage(rgb, width, height)
  }

  def toBlue(pixel: Int): Int = pixel & 0x000000FF

  def toRed(pixel: Int): Int = (pixel >> 16) & 0x000000FF

  def toGreen(pixel: Int): Int = (pixel >> 8) & 0x000000FF
}