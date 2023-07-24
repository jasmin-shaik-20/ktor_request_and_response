package com.example.plugins

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }


    install(StatusPages){
        exception<Throwable>{call,cause->
            call.respond(status = HttpStatusCode.BadRequest,"Details not found")
        }
    }




}
