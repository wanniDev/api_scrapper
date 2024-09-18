package org.http.api

class ApiEndPointUri(
    val schema: String,
    val host: String,
) {
    private var path: String = ""
    private var parameters: List<ApiParameter> = listOf()
    private var anchor: String = ""
    private var headers: List<ApiHeader> = listOf()

    fun appendPath(path: String) {
        val sb = StringBuilder()
        if (path[0] != '/') {
            sb.append('/')
        }
        this.path = StringBuilder(this.path).append(path).toString()
    }

    fun appendParameter(parameter: ApiParameter) {
        val mutableParameters = this.parameters.toMutableList()
        mutableParameters.add(parameter)
        this.parameters = mutableParameters.toList()
    }

    fun appendHeader(header: ApiHeader) {
        val mutableHeaders = this.headers.toMutableList()
        mutableHeaders.add(header)
        this.headers = mutableHeaders.toList()
    }

    fun endPoint(): String {
        val sb = StringBuilder()
        sb
            .append(schema)
            .append("://")
            .append(host)
            .append(path)
        if (parameters.isNotEmpty()) {
            sb.append("?")
            parameters.forEachIndexed { index, parameter ->
                sb.append(parameter.name).append("=").append(parameter.value)
                if (index < parameters.size - 1) {
                    sb.append("&")
                }
            }
        }
        if (anchor.isNotEmpty()) {
            sb.append("#").append(anchor)
        }
        return sb.toString()
    }

    fun headers(): List<ApiHeader> = headers

    fun parameters(): List<ApiParameter> = parameters
}
