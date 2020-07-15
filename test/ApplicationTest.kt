package myFirstRestApi.test

import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*

class ApplicationTest {
    @Test
    fun testShow() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testIncrement() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/plus/30").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testDecrement() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/minus/45").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}
