package algorithm.optimization.heuristic

import algorithm.OptimizationAlgorithm
import tsp.models.Solution
import tsp.utils.Logger

final class PairwiseOptimization(
  override val solution: Solution
) extends OptimizationAlgorithm with Logger {

  def iterate(): Solution = {
    solutionIsImproving = false
    (0 until solution.lastPosition() - 2).foreach { i: Int =>
      (i + 1 until solution.lastPosition() - 1).foreach { j: Int =>
        val from = solution.getVertexAtPosition(i)
        val to = solution.getVertexAtPosition(i + 1)
        val from2 = solution.getVertexAtPosition(j)
        val to2 = solution.getVertexAtPosition(j + 1)
        if (instance.distance(from, to) + instance.distance(from2, to2) > instance.distance(from, from2) + instance.distance(to, to2)) {
          solution.reversePath(i + 1, j)
          solutionIsImproving = true
        }
      }
    }

    solution
  }

  override def solve(): Solution = {
    iterate()
  }

}
