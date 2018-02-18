package algorithm
import tsp.models.Solution

trait OptimizationAlgorithm extends Algorithm {

  var nIterations: Int = 0

  val solution: Solution

}
