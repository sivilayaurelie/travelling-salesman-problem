package algorithm

import tsp.models.{Instance, Solution}

trait Algorithm {

  val instance: Instance

  def solve(): Solution

}
