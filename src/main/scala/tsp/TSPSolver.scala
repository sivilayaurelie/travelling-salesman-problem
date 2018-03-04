package tsp

import algorithm.{ConstructiveAlgorithm, OptimizationAlgorithm}
import algorithm.constructive.heuristic.NearestNeighbour
import algorithm.optimization.heuristic.{AntColonyOptimization, PairwiseOptimization}
import tsp.config.TSPConfig
import tsp.models.{Instance, Solution}
import tsp.utils.Logger
import tsp.utils.parser.TSPLIBInstanceParser

import scala.concurrent.{Await, ExecutionContext, ExecutionContextExecutor, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object TSPSolver extends App with Logger {

  if (args.length != 1)
    throw new IllegalArgumentException

  val instanceName: String = args(0)

  var timelimit: Long = TSPConfig.TimeLimit

  logInfo(s"$instanceName instance loading ...")

  val instance: Instance = TSPLIBInstanceParser.parseInstance(instanceName)

  logInfo(s"$instanceName instance loading succeeded")

  logInfo(s"$instanceName instance solving ...")

  logInfo(s"Building initial solution ...")

  val constructor: ConstructiveAlgorithm = new NearestNeighbour(instance)
  var solution: Solution = constructor.solve(timelimit)

  val initialObjective: Double = solution.tourObjective()
  logInfo(s"Tour objective found: $initialObjective")

  logInfo(s"Building initial solution terminated after ${constructor.runningTime} millis")

  logInfo(s"Improving solution ...")

  val optimization: OptimizationAlgorithm = new AntColonyOptimization(solution)
  solution = optimization.solve(timelimit - constructor.runningTime)

  val objective: Double = solution.tourObjective()
  logInfo(s"Tour objective found: $objective")

  logInfo(s"Improving solution terminated after ${optimization.runningTime} millis")

  logInfo(s"$instanceName instance solving succeeded")

  val objectiveVariation: Double = (initialObjective - objective) / initialObjective * 100
  logInfo(s"Solution improved by $objectiveVariation% after ${optimization.nIterations} iterations")

}
