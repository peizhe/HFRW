package com.kol.nbc

/**
 * Naive Bayes Classifier Algorithm
 * @param m - Statistical Model of Classifier
 */
class NaiveBayesClassifier[T](m: NaiveBayesModel, private val map: (T) => String) {

  def classify(s: T) = m.classes.toList.map(c => (c, calculateProbability(c, map(s)))).sortBy(_._2).last._1

  def tokenize(s: String) = s.split(' ')

  /**
   * Calculates the estimate of the probability of the document within the class
   * @param clazz - class name
   * @param src - text (object of type T) for classification
   * @return point mark <code>P(c|d)</code>
   */
  def calculateProbability(clazz: String, src: String) = {
    tokenize(src).map(m.wordLogProbability(clazz, _)).sum + m.classLogProbability(clazz)
  }
}