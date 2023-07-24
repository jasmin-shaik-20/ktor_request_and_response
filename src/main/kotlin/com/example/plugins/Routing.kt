package com.example.plugins

import com.example.models.Customer
import com.example.models.customerStorage
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/customer") {
            get {
                if (customerStorage.isNotEmpty()) {
                    call.respond(customerStorage)
                } else {
                    throw Throwable()
                }
            }

            get("/{id?}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respondText("Missing id")
                } else {
                    val customer = customerStorage.find { it.id == id }
                    if (customer == null) {
                        call.respondText("No customer with id $id")
                    } else {
                        call.respond(customer)
                    }
                }
            }

            post {
                val customer = call.receive<Customer>()
                customerStorage.add(customer)
                call.respondText("Customer stored correctly")
            }

            delete("/{id?}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respondText("Missing id")
                } else {
                    if (customerStorage.removeIf { it.id == id }) {
                        call.respondText("Customer removed correctly")
                    } else {
                        call.respondText("No customer to delete with given id $id")
                    }
                }
            }

            put("/{id?}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respondText("Missing id")
                } else {
                    val existingCustomer = customerStorage.find { it.id == id }
                    if (existingCustomer == null) {
                        call.respondText("No customer with id $id")
                    } else {
                        val updatedCustomer = call.receive<Customer>()
                        customerStorage.remove(existingCustomer)
                        customerStorage.add(updatedCustomer)
                        call.respondText("Customer updated correctly")
                    }
                }
            }
            patch("/{id?}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respondText("Missing id ")
                } else {
                    val name = call.receive<Map<String, String>>()["name"]
                    val find = customerStorage.find {
                        it.id == id
                    }
                    if (find != null) {
                        find.name = name.toString()
                        call.respond(find)
                    } else {
                        call.respondText("customer not found with the given id")
                    }
                }
            }


        }
    }
}