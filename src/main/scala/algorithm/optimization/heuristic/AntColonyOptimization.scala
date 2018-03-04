package algorithm.optimization.heuristic

import algorithm.OptimizationAlgorithm
import algorithm.config.AlgorithmConfig
import algorithm.optimization.heuristic.model.{Ant, Network, NetworkParams}
import tsp.models.Solution
import tsp.utils.Logger

final class AntColonyOptimization(
  override val solution: Solution
) extends OptimizationAlgorithm with Logger {

  private val nAnts: Int = AlgorithmConfig.NAnts

  private val referenceObjective: Double = solution.tourObjective()
  private val tau: Double = 1d / (referenceObjective * instance.nVertices)
  private val networkParams = NetworkParams(
    AlgorithmConfig.Alpha,
    AlgorithmConfig.Beta,
    AlgorithmConfig.Epsilon,
    AlgorithmConfig.Rho,
    AlgorithmConfig.Q0,
    tau
  )

  private val network: Network = Network.initialize(
    instance,
    nAnts,
    networkParams
  )

  private var bestAnt: Ant = Ant(solution)

  private val iterationSolution: Solution = Solution(instance)
  iterationSolution.setPath(solution.getPath())

  private def updateIterationSolution(): Unit = {
    network.colony.ants.foreach { ant: Ant =>
      if (ant.solution.tourObjective() < iterationSolution.tourObjective())
        iterationSolution.setPath(ant.solution.getPath())
    }
  }

  private def updateSolution(): Unit = {
    if (iterationSolution.tourObjective() < solution.tourObjective()) {
      solution.setPath(iterationSolution.getPath())
    }
  }

  private def buildSolutions(): Unit = {
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

  private def updatePheromoneTrails(): Unit = {
    network.reinitializePheromoneTrails()
    network.reinitializeAttrativePaths()

    network.colony.ants.foreach { ant: Ant =>
      ant.depositePheromones(network, networkParams.epsilon, networkParams.tau)
    }

    bestAnt.depositePheromones(network, networkParams.rho, 1d / solution.tourObjective())
  }

  override def improve(): Solution = {
    buildSolutions()
    updateIterationSolution()
    updateSolution()
    updatePheromoneTrails()
    solution
  }

}
