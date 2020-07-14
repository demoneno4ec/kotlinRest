package myFirstRestApi.test

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import java.lang.NumberFormatException

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val counter = Counter()
    counter.reset();

    val client = HttpClient(Apache) {}

    routing {
        get("/") {
            val currentCounter: Int = counter.getValue();

            call.respondText(currentCounter.toString(), contentType = ContentType.Text.Plain)
        }

        get("/plus/{value}") {
            val incrementedValue: Int = try {
                call.parameters["value"]?.toInt()!!
            }catch (e: NumberFormatException) {
                0
            }

            call.respondText(
                    counter.increment(incrementedValue),
                    contentType = ContentType.Text.Plain
            )
        }

        get("/minus/{value}") {
            val incrementedValue: Int = try {
                call.parameters["value"]?.toInt()!!
            }catch (e: NumberFormatException) {
                0
            }

            call.respondText(
                    counter.decrement(incrementedValue),
                    contentType = ContentType.Text.Plain
            )
        }
    }
}