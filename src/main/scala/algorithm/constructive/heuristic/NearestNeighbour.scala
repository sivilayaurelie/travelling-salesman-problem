package algorithm.constructive.heuristic

import algorithm.ConstructiveAlgorithm
import algorithm.config.AlgorithmConfig
import tsp.models.{Instance, Solution, Vertex}
import tsp.utils.Logger

final class NearestNeighbour(
  override val instance: Instance
) extends ConstructiveAlgorithm with Logger {

  private val visitedVertices: Array[Int] = Array.ofDim[Int](instance.nVertices)

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

  private def build(initialVertex: Vertex): Solution = {
    val solution = Solution(instance)

    var vertex: Vertex = initialVertex
    (0 until solution.lastPosition()).foreach { position: Int =>
      solution.setVertexAtPosition(vertex, position)
      visitedVertices(vertex.index) += 1
      vertex = findNearestUnvisitedVertex(vertex)
    }

    solution
  }

  override def build(): Solution = {
    val initialVertexIndex: Int = AlgorithmConfig.InitialVertexIndex

    if (initialVertexIndex < 0 || initialVertexIndex >= instance.nVertices)
      logError(
        s"Failed to solve instance with initial vertex index $initialVertexIndex",
        new IllegalArgumentException
      )

    val initialVertex: Vertex = instance.vertex(initialVertexIndex)
    build(initialVertex)
  }

}
