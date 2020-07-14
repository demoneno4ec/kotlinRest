package myFirstRestApi.test

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import java.sql.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val dbConnection = initDB()
    val tableName = "counter"
    resetValue(dbConnection, tableName)

    val client = HttpClient(Apache) {}

    routing {
        get("/") {
            val currentCounter: Int = getCurrentCounter(dbConnection, tableName)

            call.respondText(currentCounter.toString(), contentType = ContentType.Text.Plain)
        }

        get("/plus/{value}") {
            val incrementedValue: Int = try {
                call.parameters["value"]?.toInt()!!
            }catch (e: java.lang.NumberFormatException) {
                0
            }

            call.respondText(
                    incrementValue(dbConnection, tableName, incrementedValue),
                    contentType = ContentType.Text.Plain
            )
        }

        get("/minus/{value}") {
            val incrementedValue: Int = try {
                call.parameters["value"]?.toInt()!!
            }catch (e: java.lang.NumberFormatException) {
                0
            }

            call.respondText(
                    decrementValue(dbConnection, tableName, incrementedValue),
                    contentType = ContentType.Text.Plain
            )
        }
    }
}

fun getCurrentCounter(dbConnection: Connection, tableName: String): Int {

    val statement: Statement = dbConnection.createStatement()
    val sqlString = "select * from $tableName"
    val rs: ResultSet = statement.executeQuery(sqlString)
    var currentCounter = 0
    if (rs.next()) {
        currentCounter = rs.getInt("value")
    }

    return currentCounter
}

fun resetValue(dbConnection: Connection, tableName: String) {
    truncateTable(dbConnection, tableName)
    try {
        val statement: Statement = dbConnection.createStatement()
        val sqlString = "insert into $tableName(value) values(0);"
        statement.execute(sqlString)
        println("reset table $tableName!")
    } catch (e: SQLException) {
        println(e.message)
    }
}

fun truncateTable(dbConnection: Connection, tableName: String) {
    try {
        val statement: Statement = dbConnection.createStatement()
        val sqlString = "truncate $tableName restart identity;"
        statement.execute(sqlString)
        println("truncate table $tableName!")
    } catch (e: SQLException) {
        println(e.message)
    }
}

fun initDB(): Connection {
    val dbUrl = "jdbc:postgresql://localhost:5432/firstrestapi"
    val dbUser = "ladmin"
    val dbPassword = "password"

    return DriverManager.getConnection(dbUrl, dbUser, dbPassword)
}

fun updateValue(dbConnection: Connection, tableName: String, value: Int, currentCounter: Int): Int {
    try {
        val statement: Statement = dbConnection.createStatement()
        val sqlString = "UPDATE $tableName SET value = $value WHERE value = $currentCounter;"
        statement.execute(sqlString)
        println("Value update $tableName, $value, $currentCounter!")
    } catch (e: SQLException) {
        println(e.message)
    }

    return value
}

fun incrementValue(dbConnection: Connection, tableName: String, value: Int = 0): String {
    val currentCounter: Int = getCurrentCounter(dbConnection, tableName)

    val newValue: Int = updateValue(dbConnection, tableName, currentCounter + value, currentCounter)

    return "New value = $newValue, Old value $currentCounter"
}

fun decrementValue(dbConnection: Connection, tableName: String, value: Int = 0): String {
    val currentCounter: Int = getCurrentCounter(dbConnection, tableName)

    val newValue: Int = updateValue(dbConnection, tableName, currentCounter - value, currentCounter)

    return "New value = $newValue, Old value $currentCounter"
}