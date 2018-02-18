package tsp.config

import com.typesafe.config.{Config, ConfigFactory}

object TSPConfig {

  private val Config: Config = ConfigFactory.load()

  val FileDirectory: String = Config.getString("runtime.filedirectory")

  val TimeLimit: Long = Config.getLong("runtime.timelimit")

}
