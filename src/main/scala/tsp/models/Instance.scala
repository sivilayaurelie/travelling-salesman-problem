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

  def vertex(vertexIndex: Int): Vertex =
    vertices(vertexIndex)

  def distance(fromIndex: Int, toIndex: Int): Double =
    distances(fromIndex)(toIndex)

  def distance(from: Vertex, to: Vertex): Double =
    distances(from.index)(to.index)

}
