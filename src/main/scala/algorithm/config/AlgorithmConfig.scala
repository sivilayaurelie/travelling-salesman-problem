package algorithm.config

import com.typesafe.config.{Config, ConfigFactory}

object AlgorithmConfig {

  private val Config: Config = ConfigFactory.load()

  val InitialVertexIndex: Int = Config.getInt("algorithm.initialvertexindex")

}
