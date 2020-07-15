package myFirstRestApi.test

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory

class Logging {
    fun getLogger(): Logger{
        return LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    }
}