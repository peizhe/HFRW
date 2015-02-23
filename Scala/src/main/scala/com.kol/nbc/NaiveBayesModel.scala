package com.kol.nbc

import scala.math.log

/**
 * Classifier Model. Includes all necessary to classify statistics
 *
 * @param lengths - The total length of the documents in the words by grade
 * @param docCount - the number of documents by class
 * @param wordCount - statistics quoted words within classes
 * @param dictionarySize - dictionary size of training sample
 */
class NaiveBayesModel(lengths: Map[String, Int],
                      docCount: Map[String, Int],
                      wordCount: Map[String, Map[String, Int]],
                      dictionarySize: Int) {

  /**
   * @param clazz - class name
   * @param word  - word
   * @return logarithm of the evaluation <code> P (w | c) </ code> - the probability of speech within the class
   */
  def wordLogProbability(clazz: String, word: String) =
    log((wordCount(clazz).getOrElse(word, 0) + 1.0) / (lengths(clazz).toDouble + dictionarySize))

  /**
   * @param clazz - class name
   * @return logarithm of the apriori probability of class <code>P(c)</code>
   */
  def classLogProbability(clazz: String) = log(docCount(clazz).toDouble / docCount.values.sum)

  /**
   * @return the set of all classes
   */
  def classes = docCount.keySet
}