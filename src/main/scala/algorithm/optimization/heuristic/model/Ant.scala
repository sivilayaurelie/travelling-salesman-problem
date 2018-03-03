package algorithm.optimization.heuristic.model

import tsp.models.{Instance, Solution, Vertex}

object Ant {

  def apply(instance: Instance) = new Ant(
    Solution(instance),
    Array.ofDim[Int](instance.nVertices)
  )

  def apply(solution: Solution) = new Ant(
    solution,
    Array.ofDim[Int](solution.instance.nVertices)
  )

}

final class Ant(
  val solution: Solution,
  private val visitedVertices: Array[Int]
) {

  def getPath(): Array[Vertex] =
    solution.getPath()

  def getVertexAtPosition(position: Int): Vertex =
    solution.getVertexAtPosition(position)

  def setVertexPositionInPath(vertex: Vertex, position: Int): Unit =
    solution.setVertexPosition(vertex, position)

  def tourObjective(): Double =
    solution.tourObjective()

  def clearVisitedVertices(): Unit = {
    visitedVertices.indices.foreach { index =>
      visitedVertices(index) = 0
    }
  }

  def visitedVertex(vertex: Vertex): Unit =
    visitedVertices(vertex.index) += 1

  def depositePheromoneTrail(network: Network, from: Vertex, to: Vertex, lambda: Double, mu: Double): Unit = {
    network.updatePheronomeTrail(from, to, lambda, mu)
    network.updatePathAttractiveness(from, to)
  }

  def depositePheromones(network: Network, lambda: Double, mu: Double): Unit =
    (0 until solution.lastPosition()).foreach { position: Int =>
      val from: Vertex = solution.getVertexAtPosition(position)
      val to: Vertex = solution.getVertexAtPosition(position + 1)
      depositePheromoneTrail(network, from, to, lambda, mu)
    }

  def decidePath(network: Network, currentVertex: Vertex, position: Int): Vertex = {
    val q: Double = math.random
    if (q < network.networkParams.q0)
      decideBestPath(network, currentVertex, position)
    else
      decideRandomPath(network, currentVertex, position)
  }

  private def decideBestPath(network: Network, currentVertex: Vertex, position: Int): Vertex = {
    val nextVertex: Vertex = solution.instance.vertices.foldLeft(currentVertex) { (current: Vertex, next: Vertex) =>
      if (visitedVertices(next.index) == 0 &&
        network.pathAttractiveness(currentVertex, next) > network.pathAttractiveness(currentVertex, current))
        next
      else
        current
    }
    setVertexPositionInPath(nextVertex, position)
    visitedVertex(nextVertex)
    nextVertex
  }

  private def decideRandomPath(network: Network, currentVertex: Vertex, position: Int): Vertex = {
    val selectionProbability: Array[Double] = Array.tabulate[Double](solution.instance.nVertices) { vertexIndex: Int =>
      if (visitedVertices(vertexIndex) != 0)
        0d
      else
        network.pathAttractiveness(currentVertex, solution.instance.vertex(vertexIndex))
    }
    val selectionProbabilitySum: Double = selectionProbability.sum

    val randomProbability: Double = math.random * selectionProbabilitySum
    var nextVertexIndex: Int = 0
    var probability: Double = selectionProbability(nextVertexIndex)
    while (probability < randomProbability) {
      nextVertexIndex += 1
      probability += selectionProbability(nextVertexIndex)
    }

    val nextVertex: Vertex = solution.instance.vertex(nextVertexIndex)
    setVertexPositionInPath(nextVertex, position)
    visitedVertex(nextVertex)
    nextVertex
  }

}
