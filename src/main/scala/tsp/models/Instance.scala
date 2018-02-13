package tsp.solver

object Instance {
  def apply() = new Instance(
    3,
    new Array[Array[Double]](3)
  )
}

final class Instance(
  val nVertices: Int,
  val distances: Array[Array[Double]]
) {

}
