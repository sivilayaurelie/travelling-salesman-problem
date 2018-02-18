package algorithm.optimization.heuristic

import algorithm.OptimizationAlgorithm
import tsp.models.Solution
import tsp.utils.Logger

final class PairwiseOptimization(override val solution: Solution) extends OptimizationAlgorithm with Logger {

  def iterate(): Boolean = {
    var solutionIsImproving: Boolean = false

    (0 until solution.path.length - 2).foreach { i: Int =>
      (i + 1 until solution.path.length - 1).foreach { j: Int =>
        if (solution.distance(i, i + 1) + solution.distance(j, j + 1) > solution.distance(i, j) + solution.distance(i + 1, j + 1)) {
          solution.reversePath(i + 1, j)
          solutionIsImproving = true
        }
      }
    }

    solutionIsImproving
  }

  override def solve(): Solution = {
    var solutionIsImproving: Boolean = true
    while (solutionIsImproving) {
      nIterations += 1
      solutionIsImproving = iterate()
    }

    solution
  }
}
