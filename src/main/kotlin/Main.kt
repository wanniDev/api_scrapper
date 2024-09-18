package org.http.api

import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ParseException
import java.io.IOException
import java.nio.file.Paths
import java.util.Properties

fun main() {
    val path = "${System.getProperty("user.dir")}/src/main/resources/application.properties"
    var properties = mapOf<String, Any>()
    Properties()
        .apply {
            load(Paths.get(path).toFile().reader())
        }.forEach { (key, value) ->
            val splitKey = key.toString().split(".")
            val mutableProperties = properties.toMutableMap()
            if (splitKey.size > 1) {
                val depth1Key = splitKey[0]
                mutableProperties.putIfAbsent(depth1Key, mapOf<String, String>())

                val depth2Properties = mutableProperties[depth1Key] as Map<*, *>
                val mutableDepth2Properties = depth2Properties.toMutableMap()
                mutableDepth2Properties.putIfAbsent(splitKey[1], value.toString())

                mutableProperties[depth1Key] = mutableDepth2Properties.toMap()
            } else {
                mutableProperties.putIfAbsent(key.toString(), value.toString())
            }
            properties = mutableProperties.toMap()
        }

    val apiEndPointUri =
        ApiEndPointUri(
            schema = properties["schema"].toString(),
            host = properties["host"].toString(),
        )
    apiEndPointUri.appendPath(properties["path"].toString())

    properties["parameters"]?.let {
        val parameters = it as Map<*, *>
        parameters.forEach { (key, value) ->
            apiEndPointUri.appendParameter(ApiParameter(key.toString(), value.toString()))
        }
    }

    properties["headers"]?.let {
        val headers = it as Map<*, *>
        headers.forEach { (key, value) ->
            apiEndPointUri.appendHeader(ApiHeader(key.toString(), value.toString()))
        }
    }

    val endPoint = apiEndPointUri.endPoint()
    println(endPoint)
    val headers = apiEndPointUri.headers()
    println(headers)
    val parameters = apiEndPointUri.parameters()
    println(parameters)

    try {
        val httpGet = HttpGet(endPoint)
        for (header in headers) {
            httpGet.addHeader(header.name, header.value)
        }
        val httpClient = HttpClients.createDefault()
        httpClient.execute(httpGet) { response ->
            val version = response.version
            val status = response.code
            val message = response.reasonPhrase
            val entities =
                response.entity.content
                    .readAllBytes()
                    .decodeToString()
            println("version: $version")
            println("status: $status")
            println("message: $message")
            println("entities: $entities")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: ParseException) {
        e.printStackTrace()
    }
}
