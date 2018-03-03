package algorithm
import tsp.models.{Instance, Solution}

trait OptimizationAlgorithm extends Algorithm {

  protected val solution: Solution

  protected val instance: Instance = solution.instance

  protected var solutionIsImproving: Boolean = true

  def hasNextIteration(): Boolean =
    solutionIsImproving

}
