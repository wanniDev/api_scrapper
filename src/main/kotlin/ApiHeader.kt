package org.http.api

data class ApiHeader(
    val name: String,
    val value: String,
    val description: String = ""
)