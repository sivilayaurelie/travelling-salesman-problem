package tsp.models

import tsp.utils.Logger

object Solution {

  def apply(instance: Instance) = new Solution(
    instance,
    Array.ofDim[Vertex](instance.nVertices + 1),
    0d
  )

}

final class Solution (
  private val instance: Instance,
  val path: Array[Vertex],
  private var objective: Double
) extends Logger {

  def setVertexPositionInPath(vertex: Vertex, position: Int): Unit = {
    if (position < 0 || position >= path.length)
      logError(
        s"Failed to set position $position in path",
        new IllegalArgumentException
      )

    path(position) = vertex
  }

  def evaluate(): Double = {
    objective = 0
    (0 until path.length - 1).foreach { position: Int =>
      objective += instance.distance(path(position), path(position + 1))
    }
    objective
  }

  def isFeasible(): Boolean = {
    var feasible: Boolean = path(0) == path(path.length - 1)
    val occurrences: Array[Int] = Array.ofDim[Int](instance.nVertices)
    var position: Int = 0
    while (position < path.length - 1 && feasible) {
      occurrences(path(position).index) += 1
      feasible &= occurrences(path(position).index) == 1
      position += 1
    }
    feasible
  }

  def tourObjective(): Double = {
    logInfo(
      s"Tour objective found: $objective"
    )

    objective
  }

}
