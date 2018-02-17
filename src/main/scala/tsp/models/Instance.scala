package tsp.models

import tsp.utils.Logger

object Instance {

  def apply(name: String, nVertices: Int, vertices: Array[Vertex], distances: Array[Array[Double]]) = new Instance(
    name,
    nVertices,
    vertices,
    distances
  )

}

final class Instance(
  val name: String,
  val nVertices: Int,
  val vertices: Array[Vertex],
  private val distances: Array[Array[Double]]
) extends Logger {

  def distance(from: Vertex, to: Vertex): Double = {
    if (from.index < 0 || from.index >= distances.length || to.index < 0 || to.index >= distances.length)
      logError(
        s"Failed to get distance between $from and $to",
        new IllegalArgumentException
      )

    distances(from.index)(to.index)
  }

}
