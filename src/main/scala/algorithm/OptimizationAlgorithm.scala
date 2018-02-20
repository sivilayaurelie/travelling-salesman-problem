package algorithm
import tsp.models.Solution

trait OptimizationAlgorithm extends Algorithm {

  protected var solutionIsImproving: Boolean = true

  protected val solution: Solution

  def hasNextIteration(): Boolean =
    solutionIsImproving

}
