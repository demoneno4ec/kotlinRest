package myFirstRestApi.test

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    val counter = Counter()
    val logging = Logging()
    val logger = logging.getLogger()
    val client = HttpClient(Apache) {}

    counter.reset()

    routing {
        get("/") {
            logger.debug("В тз не было написано, что тут писать, но напем, что вот мы такие красивые пришли, увидели и победили")

            val currentCounter: Int = counter.getValue()

            call.respondText(
                    currentCounter.toString(),
                    contentType = ContentType.Text.Plain
            )
        }

        post("/plus/{value}") {
            logger.debug("Тоже не описано, но знайте, мы тут короче инкрементим")

            val incrementedValue: Int = Utils.convertToInt(call.parameters["value"])

            call.respondText(
                    counter.increment(incrementedValue),
                    contentType = ContentType.Text.Plain
            )
        }

        post("/minus/{value}") {
            logger.debug("F nen ltrhtvtynbv")

            val incrementedValue: Int = Utils.convertToInt(call.parameters["value"])

            call.respondText(
                    counter.decrement(incrementedValue),
                    contentType = ContentType.Text.Plain
            )
        }
    }
}