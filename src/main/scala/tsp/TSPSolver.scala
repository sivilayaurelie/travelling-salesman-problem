package tsp

import algorithm.OptimizationAlgorithm
import algorithm.constructive.heuristic.NearestNeighbour
import algorithm.optimization.heuristic.PairwiseOptimization
import tsp.config.TSPConfig
import tsp.models.Solution
import tsp.utils.Logger
import tsp.utils.parser.TSPLIBInstanceParser

object TSPSolver extends App with Logger {

  if (args.length != 1)
    throw new IllegalArgumentException

  val instanceName: String = args(0)

  val timelimit: Long = TSPConfig.TimeLimit

  logInfo(s"$instanceName instance loading ...")

  val instance = TSPLIBInstanceParser.parseInstance(instanceName)

  logInfo(s"$instanceName instance loading succeeded")

  logInfo(s"$instanceName instance solving ...")

  val startTime: Long = System.currentTimeMillis()

  val initialSolution: Solution = new NearestNeighbour(instance).solve()
  val initialObjective: Double = initialSolution.tourObjective()

  val optimization: OptimizationAlgorithm = new PairwiseOptimization(initialSolution)
  var solution: Solution = optimization.solve()
  val objective: Double = solution.tourObjective()

  logInfo(s"$instanceName instance solving succeeded")

  val objectiveVariation: Double = (initialObjective - objective) / initialObjective * 100
  logInfo(s"Solution improved by $objectiveVariation% after ${optimization.nIterations} iterations")

}
