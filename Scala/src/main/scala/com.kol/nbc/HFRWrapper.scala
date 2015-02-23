package com.kol.nbc

class HFRWrapper {
  def learn(image: Array[Double], clazz: String): NaiveBayesClassifier[Array[Double]] = {
    val nbla: NaiveBayesLearningAlgorithm[Array[Double]] = NBLA.doubleArray(5)
    nbla.addExample(image, clazz)
    nbla.classifier
  }

}
