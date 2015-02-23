package com.kol.nbc

object ClassifierSpec {
  def main(args: Array[String]) {
    val c: NaiveBayesLearningAlgorithm[String] = NBLA.string
    c.addExample("предоставляю услуги бухгалтера", "SPAM")
    c.addExample("спешите купить виагру", "SPAM")
    c.addExample("надо купить молоко", "HAM")

    val bestClass = c.classifier.classify("")
    println(bestClass)
  }
}
