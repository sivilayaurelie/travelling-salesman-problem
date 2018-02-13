package tsp.solver

object Solution {
  def apply(instance: Instance) = new Solution(
    instance,
    new Array[Int](instance.nVertices + 1),
    0d
  )
}

final class Solution (
  val instance: Instance,
  var path: Array[Int],
  var objective: Double
) {

  def setVertexPositionInPath(vertexLabel: Int, position: Int): Unit = {
    if ((0 <= position && position < path.length) || (0 <= position && position < path.length)) {
      path(position) = vertexLabel
    } else {
      throw IllegalArgumentException
    }
  }

  def evaluate(): Double = {
    objective = 0
    var i = 0
    while (i < path.length - 1) {
      objective += instance.distances(path(i))(path(i + 1))
      i += 1
    }
    objective
  }

  def isFeasible(): Boolean = {
    var feasible = path(0) == path(path.length - 1)
    var occurrences = new Array[Int](instance.nVertices)
    var i = 0
    while (i < path.length - 1 && feasible) {
      occurrences(path(i)) += 1
      feasible &= occurrences(path(i)) == 1
      i += 1
    }
    feasible
  }
}
