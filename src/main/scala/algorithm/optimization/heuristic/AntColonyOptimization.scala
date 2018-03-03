package algorithm.optimization.heuristic

import algorithm.OptimizationAlgorithm
import algorithm.config.AlgorithmConfig
import algorithm.optimization.heuristic.model.{Ant, Network, NetworkParams}
import tsp.models.Solution
import tsp.utils.Logger

final class AntColonyOptimization(
  override val solution: Solution
) extends OptimizationAlgorithm with Logger {

  val nAnts: Int = AlgorithmConfig.NAnts

  val referenceObjective: Double = solution.tourObjective()
  val tau: Double = 1d / (referenceObjective * instance.nVertices)
  val networkParams = NetworkParams(
    AlgorithmConfig.Alpha,
    AlgorithmConfig.Beta,
    AlgorithmConfig.Epsilon,
    AlgorithmConfig.Rho,
    AlgorithmConfig.Q0,
    tau
  )

  val network: Network = Network.initialize(
    instance,
    nAnts,
    networkParams
  )

  var bestAnt: Ant = Ant(solution)

  val currentSolution: Solution = Solution(instance)
  currentSolution.setPath(solution.getPath())

  def updateCurrentSolution(): Unit = {
    network.colony.ants.foreach { ant: Ant =>
      if (ant.tourObjective() < currentSolution.tourObjective())
        currentSolution.setPath(ant.getPath())
    }
  }

  def updateSolution(): Unit = {
    if (currentSolution.tourObjective() < solution.tourObjective()) {
      solution.setPath(currentSolution.getPath())
      bestAnt = Ant(currentSolution)
    }
  }

  def buildSolutions(): Unit = {
    network.memorizePheromoneTrails()
    network.memorizeAttrativePaths()

    var position = 0
    network.colony.ants.foreach { ant: Ant =>
      val randomVertexIndex: Int = (math.random * instance.nVertices).toInt
      val randomInitialVertex = instance.vertex(randomVertexIndex)
      ant.clearVisitedVertices()
      ant.setVertexPositionInPath(randomInitialVertex, position)
      ant.visitedVertex(randomInitialVertex)
    }
    position += 1

    while (position < solution.lastPosition()) {
      network.colony.ants.foreach { ant: Ant =>
        val currentVertex = ant.getVertexAtPosition(position - 1)
        val nextVertex = ant.decidePath(network, currentVertex, position)
        ant.depositePheromoneTrail(network, currentVertex, nextVertex, networkParams.epsilon, networkParams.tau)
      }
      position += 1
    }
  }

  def updatePheromoneTrails(): Unit = {
    network.reinitializePheromoneTrails()
    network.reinitializeAttrativePaths()

    network.colony.ants.foreach { ant: Ant =>
      ant.depositePheromones(network, networkParams.epsilon, networkParams.tau)
    }

    bestAnt.depositePheromones(network, networkParams.rho, 1d / solution.tourObjective())
  }

  def iterate(): Solution = {
    buildSolutions()
    updateCurrentSolution()
    updateSolution()
    updatePheromoneTrails()
    solution
  }

  override def solve(): Solution = {
    iterate()
  }

}
