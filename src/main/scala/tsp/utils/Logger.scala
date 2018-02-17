package tsp.utils

import org.slf4j.LoggerFactory

trait Logger {

  private val logger = LoggerFactory.getLogger(getClass)

  def logInfo(message: String): Unit =
    if (logger.isInfoEnabled) logger.info(message)

  def logError(message: String, throwable: Throwable): Unit =
    if (logger.isErrorEnabled) logger.error(message, throwable)

}
