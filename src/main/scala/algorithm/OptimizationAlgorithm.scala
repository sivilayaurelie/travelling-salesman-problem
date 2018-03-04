package algorithm
import tsp.models.{Instance, Solution}
import tsp.utils.Logger

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

trait OptimizationAlgorithm extends Algorithm with Logger {

  var nIterations: Int = 0

  protected var solutionIsImproving: Boolean = true

  protected val solution: Solution

  protected def improve(): Solution

  override val instance: Instance = solution.instance

  override def solve(timelimit: Long): Solution = {
    var iterationTimelimit: Long = timelimit
    while (solutionIsImproving && iterationTimelimit > 0l) {
      val startTime: Long = System.currentTimeMillis()
      val futureSolution: Future[Solution] = Future.apply(improve())(executionContext)
      Try(Await.result(futureSolution, iterationTimelimit.millis)) match {
        case Success(_) =>
          nIterations += 1
          runningTime += System.currentTimeMillis() - startTime
        case Failure(_) =>
          ()
      }
      iterationTimelimit -= System.currentTimeMillis() - startTime
    }
    solution
  }

}
