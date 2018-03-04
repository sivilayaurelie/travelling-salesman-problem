package algorithm

import tsp.models.{Instance, Solution}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

trait Algorithm {

  protected val executionContext: ExecutionContextExecutor = ExecutionContext.global

  var runningTime: Long = 0l

  protected val instance: Instance

  def solve(timelimit: Long): Solution

}
