package tsp.models

import tsp.utils.Logger

object Solution {

  def apply(instance: Instance) = new Solution(
    instance,
    Array.ofDim[Vertex](instance.nVertices + 1),
    Double.MaxValue
  )

}

final class Solution (
  val instance: Instance,
  private val path: Array[Vertex],
  private var objective: Double
) extends Logger {

  def lastPosition(): Int =
    path.length - 1

  def getVertexAtPosition(position: Int): Vertex =
    path(position)

  def setVertexAtPosition(vertex: Vertex, position: Int): Unit = {
    if (position == 0)
      path(path.length - 1) = vertex
    path(position) = vertex
  }

  def getPath(): Array[Vertex] = {
    path.clone()
  }

  def setPath(updatedPath: Array[Vertex]): Unit = {
    (0 until path.length).foreach { position =>
      path(position) = updatedPath(position)
    }
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

  private def tour(fromPosition: Int, toPosition: Int): Double = {
    var tour: Double = 0d
    (fromPosition until toPosition).foreach { position: Int =>
      tour += instance.distance(path(position), path(position + 1))
    }
    tour
  }

  def tourObjective(): Double = {
    objective = tour(0, path.length - 1)
    objective
  }

  def reversePath(fromPosition: Int, toPosition: Int): Unit = {
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
