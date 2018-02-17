package tsp.utils

import tsp.models.Instance

trait InstanceParser {

  def parseInstance(instanceName: String): Instance

}
