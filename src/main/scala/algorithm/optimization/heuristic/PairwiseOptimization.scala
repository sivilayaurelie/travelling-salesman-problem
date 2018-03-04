package algorithm.optimization.heuristic

import algorithm.OptimizationAlgorithm
import tsp.models.{Solution, Vertex}
import tsp.utils.Logger

final class PairwiseOptimization(
  override val solution: Solution
) extends OptimizationAlgorithm with Logger {

  private def exchangePairwise(): Unit = {
    solutionIsImproving = false
    (0 until solution.lastPosition() - 2).foreach { i: Int =>
      (i + 1 until solution.lastPosition() - 1).foreach { j: Int =>
        val from1: Vertex = solution.getVertexAtPosition(i)
        val to1: Vertex = solution.getVertexAtPosition(i + 1)
        val from2: Vertex = solution.getVertexAtPosition(j)
        val to2: Vertex = solution.getVertexAtPosition(j + 1)
        if (instance.distance(from1, to1) + instance.distance(from2, to2) > instance.distance(from1, from2) + instance.distance(to1, to2)) {
          solution.reversePath(i + 1, j)
          solutionIsImproving = true
        }
      }
    }
  }

  override def improve(): Solution = {
    exchangePairwise()

    solution
  }

}
