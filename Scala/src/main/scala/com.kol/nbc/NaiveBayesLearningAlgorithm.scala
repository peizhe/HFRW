package com.kol.nbc

/**
 * Обучающий алгоритм классификации
 */
class NaiveBayesLearningAlgorithm[T] {
  private var examples: List[(T, String)] = List()

  private val tokenize = (v: String) => v.split(' ')
  private val tokenizeTuple = (v: (String, String)) => tokenize(v._1)
  private val calculateWords = (l: List[(String, String)]) => l.map(tokenizeTuple(_).length).sum

  def addExample(example: T, clazz: String) = examples = (example, clazz) :: examples

  /*def dictionary = examples.map(tokenizeTuple).flatten.toSet

  def model = {
    val docsByClass = examples.groupBy(_._2)
    val lengths = docsByClass.mapValues(calculateWords)
    val docCounts = docsByClass.mapValues(_.length)
    val wordsCount = docsByClass.mapValues(_.map(tokenizeTuple).flatten.groupBy(x => x).mapValues(_.length))

    new NaiveBayesModel(lengths, docCounts, wordsCount, dictionary.size)
  }

  def classifier = new NaiveBayesClassifier(model)*/
}