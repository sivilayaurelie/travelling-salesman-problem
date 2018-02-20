package tsp

import algorithm.{ConstructiveAlgorithm, OptimizationAlgorithm}
import algorithm.constructive.heuristic.NearestNeighbour
import algorithm.optimization.heuristic.PairwiseOptimization
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

  val executionContext: ExecutionContextExecutor = ExecutionContext.global

  var timelimit: Long = TSPConfig.TimeLimit

  logInfo(s"$instanceName instance loading ...")

  val instance: Instance = TSPLIBInstanceParser.parseInstance(instanceName)

  logInfo(s"$instanceName instance loading succeeded")

  logInfo(s"$instanceName instance solving ...")

  logInfo(s"Building initial solution ...")

  val constructor: ConstructiveAlgorithm = new NearestNeighbour(instance)

  val startTime: Long = System.currentTimeMillis()
  var buildingTime: Long = 0l
  val futureSolution: Future[Solution] = Future.apply(constructor.solve())(executionContext)
  var solution: Solution = Try(Await.result(futureSolution, timelimit.millis)) match {
    case Success(s: Solution) =>
      buildingTime = System.currentTimeMillis() - startTime
      timelimit -= buildingTime
      s
    case Failure(_) =>
      logError(s"Failed to build initial solution", new RuntimeException)
      throw new RuntimeException
  }

  val initialObjective: Double = solution.tourObjective()

  logInfo(s"Building initial solution terminated after $buildingTime millis")

  logInfo(s"Improving solution ...")

  val optimization: OptimizationAlgorithm = new PairwiseOptimization(solution)

  var nIterations: Int = 0
  var improvingTime: Long = 0l
  while (optimization.hasNextIteration() && timelimit > 0l) {
    var startTime: Long = System.currentTimeMillis()
    val futureSolution: Future[Solution] = Future.apply(optimization.solve())(executionContext)
    solution = Try(Await.result(futureSolution, timelimit.millis)) match {
      case Success(s: Solution) =>
        nIterations += 1
        improvingTime += System.currentTimeMillis() - startTime
        timelimit -= improvingTime
        s
      case Failure(_) =>
        solution
    }
  }

  val objective: Double = solution.tourObjective()

  logInfo(s"Improving solution terminated after $improvingTime millis")

  logInfo(s"$instanceName instance solving succeeded")

  val objectiveVariation: Double = (initialObjective - objective) / initialObjective * 100
  logInfo(s"Solution improved by $objectiveVariation% after $nIterations iterations")

}
