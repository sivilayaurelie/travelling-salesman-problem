package algorithm.constructive.heuristic

import algorithm.Algorithm
import algorithm.config.AlgorithmConfig
import tsp.models.{Instance, Solution, Vertex}
import tsp.utils.Logger

class NearestNeighbour(override val instance: Instance) extends Algorithm with Logger {

  val visitedVertices: Array[Int] = Array.ofDim[Int](instance.nVertices)

  private def findNearestUnvisitedVertex(vertex: Vertex): Vertex = {
    var minDistance: Double = Double.MaxValue
    var nearestUnvisitedVertex: Vertex = null
    instance.vertices.foreach { candidateVertex: Vertex =>
      val distance: Double = instance.distance(vertex, candidateVertex)
      if (distance < minDistance && distance != 0d && visitedVertices(candidateVertex.index) == 0) {
        minDistance = distance
        nearestUnvisitedVertex = candidateVertex
      }
    }

    nearestUnvisitedVertex
  }

  private def solve(initialVertex: Vertex): Solution = {
    val solution = Solution(instance)

    var vertex: Vertex = initialVertex
    (0 until solution.path.length - 1).foreach { position: Int =>
      solution.setVertexPositionInPath(vertex, position)
      visitedVertices(vertex.index) += 1
      vertex = findNearestUnvisitedVertex(vertex)
    }
    solution.setVertexPositionInPath(initialVertex, solution.path.length - 1)
    solution.evaluate()
    solution
  }

  override def solve(): Solution = {
    val initialVertexIndex: Int = AlgorithmConfig.InitialVertexIndex

    if (initialVertexIndex < 0 || initialVertexIndex >= instance.nVertices)
      logError(
        s"Failed to solve instance with initial vertex index $initialVertexIndex",
        new IllegalArgumentException
      )

    val initialVertex: Vertex = instance.vertices(initialVertexIndex)
    solve(initialVertex)
  }

}
