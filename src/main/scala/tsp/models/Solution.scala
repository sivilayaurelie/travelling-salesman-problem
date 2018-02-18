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

  def distance(fromPosition: Int, toPosition: Int): Double = {
    if (fromPosition < 0 || fromPosition >= path.length || toPosition < 0 || toPosition >= path.length)
      logError(
        s"Failed to get distance between vertices at positions $fromPosition and $toPosition",
        new IllegalArgumentException
      )

    instance.distance(path(fromPosition), path(toPosition))
  }

  private def tour(fromPosition: Int, toPosition: Int): Double = {
    var tour: Double = 0d
    (fromPosition until toPosition).foreach { position: Int =>
      tour += instance.distance(path(position), path(position + 1))
    }
    tour
  }

  def tourObjective(): Double = {
    objective = tour(0, path.length - 1)
    logInfo(
      s"Tour objective found: $objective"
    )

    objective
  }

  def reversePath(fromPosition: Int, toPosition: Int): Unit = {
    if (fromPosition < 0 || fromPosition >= path.length || toPosition < 0 || toPosition >= path.length)
      logError(
        s"Failed to reverse between positions $fromPosition and $toPosition",
        new IllegalArgumentException
      )

    var i: Int = fromPosition
    var j: Int = toPosition
    while (i < j) {
      val vertex: Vertex = path(i)
      path(i) = path(j)
      path(j) = vertex
      i += 1
      j -= 1
    }
  }

}
