package algorithm.optimization.heuristic.model

import tsp.models.{Instance, Vertex}

object Network {

  def initialize(instance: Instance, nAnts: Int, networkParams: NetworkParams): Network = {
    val pheromoneTrails: Array[Array[Double]] = Array.tabulate[Array[Double]](instance.nVertices) { _ =>
      Array.ofDim[Double](instance.nVertices)
    }
    val pheromoneTrailsMemory: Array[Array[Double]] = Array.tabulate[Array[Double]](instance.nVertices) { _ =>
      Array.ofDim[Double](instance.nVertices)
    }

    val attractivePaths: Array[Array[Double]] = Array.tabulate[Array[Double]](instance.nVertices) { _ =>
      Array.ofDim[Double](instance.nVertices)
    }
    val attractivePathsMemory: Array[Array[Double]] = Array.tabulate[Array[Double]](instance.nVertices) { _ =>
      Array.ofDim[Double](instance.nVertices)
    }

    val network = new Network(
      instance,
      Colony(instance, nAnts),
      networkParams,
      pheromoneTrails,
      pheromoneTrailsMemory,
      attractivePaths,
      attractivePathsMemory
    )

    pheromoneTrails.indices.foreach { fromIndex: Int =>
      (fromIndex until pheromoneTrails(fromIndex).length).foreach { toIndex: Int =>
        val from = instance.vertex(fromIndex)
        val to = instance.vertex(toIndex)
        network.updatePheronomeTrail(from, to, 1d, networkParams.tau)
        network.updatePathAttractiveness(from, to)
      }
    }

    network
  }

}

final class Network(
  val instance: Instance,
  val colony: Colony,
  val networkParams: NetworkParams,
  private val pheromoneTrails: Array[Array[Double]],
  private val pheromoneTrailsMemory: Array[Array[Double]],
  private val pathAttractiveness: Array[Array[Double]],
  private val pathAttractivenessMemory: Array[Array[Double]]
) {

  def memorizePheromoneTrails(): Unit = {
    pheromoneTrailsMemory.indices.foreach { i: Int =>
      (i until pheromoneTrailsMemory(i).length).foreach { j: Int =>
        pheromoneTrailsMemory(i)(j) = pheromoneTrails(i)(j)
        pheromoneTrailsMemory(j)(i) = pheromoneTrails(j)(i)
      }
    }
  }

  def memorizeAttrativePaths(): Unit = {
    pathAttractivenessMemory.indices.foreach { i: Int =>
      (i until pathAttractivenessMemory(i).length).foreach { j: Int =>
        pathAttractivenessMemory(i)(j) = pathAttractiveness(i)(j)
        pathAttractivenessMemory(j)(i) = pathAttractiveness(j)(i)
      }
    }
  }

  def reinitializePheromoneTrails(): Unit = {
    pheromoneTrails.indices.foreach { i: Int =>
      (i until pheromoneTrails(i).length).foreach { j: Int =>
        pheromoneTrails(i)(j) = pheromoneTrailsMemory(i)(j)
        pheromoneTrails(j)(i) = pheromoneTrailsMemory(j)(i)
      }
    }
  }

  def reinitializeAttrativePaths(): Unit = {
    pathAttractiveness.indices.foreach { i: Int =>
      (i until pathAttractiveness(i).length).foreach { j: Int =>
        pathAttractiveness(i)(j) = pathAttractivenessMemory(i)(j)
        pathAttractiveness(j)(i) = pathAttractivenessMemory(j)(i)
      }
    }
  }

  def updatePheronomeTrail(from: Vertex, to: Vertex, lambda: Double, mu: Double): Unit = {
    val fromIndex = from.index
    val toIndex = to.index
    pheromoneTrails(fromIndex)(toIndex) = updatedPheromoneTrail(fromIndex, toIndex, lambda, mu)
    pheromoneTrails(toIndex)(fromIndex) = pheromoneTrails(fromIndex)(toIndex)
  }

  private def updatedPheromoneTrail(fromIndex: Int, toIndex: Int, lambda: Double, mu: Double): Double =
    (1d - lambda) * pheromoneTrails(fromIndex)(toIndex) + lambda * mu

  def updatePathAttractiveness(from: Vertex, to: Vertex): Unit = {
    val fromIndex = from.index
    val toIndex = to.index
    pathAttractiveness(fromIndex)(toIndex) = updatedPathAttractiveness(fromIndex, toIndex)
    pathAttractiveness(toIndex)(fromIndex) = pathAttractiveness(fromIndex)(toIndex)
  }

  private def updatedPathAttractiveness(fromIndex: Int, toIndex: Int): Double = {
    val distance: Double = instance.distance(fromIndex, toIndex)
    val a: Double = math.pow(pheromoneTrails(fromIndex)(toIndex), networkParams.alpha)
    val b: Double = math.pow(if (distance != 0d) 1d / distance else 0d, networkParams.beta)
    a * b
  }

  def pathAttractiveness(from: Vertex, to: Vertex): Double = {
    pathAttractiveness(from.index)(to.index)
  }

}
