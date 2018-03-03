package algorithm.optimization.heuristic.model

import tsp.models.Instance

object Colony {

  def apply(instance: Instance, nAnts: Int) = new Colony(
    instance,
    nAnts,
    Array.tabulate[Ant](nAnts) { _ =>
      Ant(instance)
    }
  )

}

final class Colony(
  val instance: Instance,
  val nAnts: Int,
  val ants: Array[Ant]
) {

  def ant(antIndex: Int): Ant =
    ants(antIndex)

}
