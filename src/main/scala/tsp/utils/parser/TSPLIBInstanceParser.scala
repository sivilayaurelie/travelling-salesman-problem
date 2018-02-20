package tsp.utils.parser

import java.io._
import java.util.zip.GZIPInputStream

import tsp.config.TSPConfig
import tsp.models.{Instance, Vertex}
import tsp.utils.{InstanceParser, Logger}

object TSPLIBInstanceParser extends InstanceParser with Logger {

  override def parseInstance(instanceName: String): Instance = {
    val fileDirectory: String = TSPConfig.FileDirectory
    val filePath: String = s"$fileDirectory$instanceName.tsp.gz"
    val file: File = new File(filePath)
    if (!file.exists())
      logError(
        s"Failed to find file at $filePath",
        new IllegalArgumentException
      )

    parseInstance(file)
  }

  private def parseInstance(file: File): Instance = {
    val inputStream: FileInputStream = new FileInputStream(file)
    val gzInputStream: GZIPInputStream = new GZIPInputStream(inputStream)
    val inputStreamReader: InputStreamReader = new InputStreamReader(gzInputStream)
    val bufferedReader: BufferedReader = new BufferedReader(inputStreamReader)

    var line: String = bufferedReader.readLine()
    while (line != null && !line.startsWith("NAME")) {
      line = bufferedReader.readLine()
    }
    val name: String = line.split(" ").last
    while (line != null && !line.startsWith("DIMENSION")) {
      line = bufferedReader.readLine()
    }
    val nVertices: Int = line.split(" ").last.toInt
    while (line != null && !line.startsWith("NODE_COORD_SECTION")) {
      line = bufferedReader.readLine()
    }

    val vertices: Array[Vertex] = Array.ofDim[Vertex](nVertices)
    var index: Int = 0
    line = bufferedReader.readLine()
    while (index < vertices.length && line != null && !line.startsWith("EOF")) {
      val coordinates: Array[String] = line.trim().split("\\s+")
      vertices(index) = Vertex(index, coordinates(0).toInt, coordinates(1).toDouble, coordinates(2).toDouble)
      index += 1
      line = bufferedReader.readLine()
    }

    val distances: Array[Array[Double]] = Array.ofDim[Array[Double]](nVertices)
    distances.indices.foreach { row: Int =>
      distances(row) = Array.ofDim[Double](nVertices)
    }

    (0 until nVertices).foreach { from: Int =>
      distances(from)(from) = 0
      (from + 1 until nVertices).foreach { to: Int =>
        val distance: Double = computeDistance(vertices(from), vertices(to))
        distances(from)(to) = distance
        distances(to)(from) = distance
      }
    }

    bufferedReader.close()

    Instance(name, nVertices, vertices, distances)
  }

  private def computeDistance(from: Vertex, to: Vertex): Double = {
    val dx: Double = from.x - to.x
    val dy: Double = from.y - to.y
    Math.hypot(dx, dy)
  }

}
