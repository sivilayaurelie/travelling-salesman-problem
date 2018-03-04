package algorithm

import tsp.models.Solution
import tsp.utils.Logger

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

trait ConstructiveAlgorithm extends Algorithm with Logger {

  protected def build(): Solution

  override def solve(timelimit: Long): Solution = {
    val startTime: Long = System.currentTimeMillis()
    val futureSolution: Future[Solution] = Future.apply(build())(executionContext)
    Try(Await.result(futureSolution, timelimit.millis)) match {
      case Success(s: Solution) =>
        runningTime = System.currentTimeMillis() - startTime
        s
      case Failure(_) =>
        logError(s"Failed to build initial solution", new RuntimeException)
        throw new RuntimeException
    }
  }

}
