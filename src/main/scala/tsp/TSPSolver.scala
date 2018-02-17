package tsp

import algorithm.constructive.heuristic.NearestNeighbour
import tsp.config.TSPConfig
import tsp.models.Solution
import tsp.utils.Logger
import tsp.utils.parser.TSPLIBInstanceParser

object TSPSolver extends App with Logger {

  if (args.length != 1)
    throw new IllegalArgumentException

  val instanceName: String = args(0)

  val timelimit: Int = TSPConfig.TimeLimit

  logInfo(s"$instanceName instance loading ...")
  val instance = TSPLIBInstanceParser.parseInstance(instanceName)
  logInfo(s"$instanceName instance loading succeeded")

  logInfo(s"$instanceName instance solving ...")
  val solution: Solution = new NearestNeighbour(instance).solve()
  logInfo(s"$instanceName instance solving succeeded")

  val objective: Double = solution.tourObjective()

}
