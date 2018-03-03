package algorithm.config

import com.typesafe.config.{Config, ConfigFactory}

object AlgorithmConfig {

  private val Config: Config = ConfigFactory.load()

  val InitialVertexIndex: Int = Config.getInt("algorithm.initialvertexindex")

  val NAnts: Int = Config.getInt("algorithm.aoc.nants")

  val Alpha: Int = Config.getInt("algorithm.aoc.alpha")

  val Beta: Int = Config.getInt("algorithm.aoc.beta")

  val Epsilon: Double = Config.getDouble("algorithm.aoc.epsilon")

  val Rho: Double = Config.getDouble("algorithm.aoc.rho")

  val Q0: Double = Config.getDouble("algorithm.aoc.q0")

}
