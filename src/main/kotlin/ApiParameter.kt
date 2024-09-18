package org.http.api

data class ApiParameter(
    val name: String,
    val value: String,
    val description: String = ""
)
