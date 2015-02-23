package com.kol.nbc

import java.math

import scala.math.BigDecimal.RoundingMode

/**
 * Learning algorithm of classification
 */
class NaiveBayesLearningAlgorithm[T](private val map: (T) => String) {
  private var examples: List[(String, String)] = List()

  private val tokenize = (v: String) => v.split(' ')
  private val tokenizeTuple = (v: (String, String)) => tokenize(v._1)
  private val calculateWords = (l: List[(String, String)]) => l.map(tokenizeTuple(_).length).sum

  def addExample(example: T, clazz: String) = examples = (map(example), clazz) :: examples

  def dictionary = examples.map(tokenizeTuple).flatten.toSet

  def model = {
    val docsByClass = examples.groupBy(_._2)
    val lengths = docsByClass.mapValues(calculateWords)
    val docCounts = docsByClass.mapValues(_.length)
    val wordsCount = docsByClass.mapValues(_.map(tokenizeTuple).flatten.groupBy(x => x).mapValues(_.length))

    new NaiveBayesModel(lengths, docCounts, wordsCount, dictionary.size)
  }

  def classifier = new NaiveBayesClassifier(model, map)
}

object NBLA {
  def string = new NaiveBayesLearningAlgorithm[String](v => v)

  def intArray = new NaiveBayesLearningAlgorithm[Array[Int]](v => v.mkString(" "))

  def doubleArray(decimalSymbols: Int) =
    new NaiveBayesLearningAlgorithm[Array[Double]](v => v.map(f => round(f, decimalSymbols)).mkString(" "))

  private def round(value: Double, places: Int): Double =
      new math.BigDecimal(value).setScale(places, RoundingMode.HALF_UP).doubleValue()
}