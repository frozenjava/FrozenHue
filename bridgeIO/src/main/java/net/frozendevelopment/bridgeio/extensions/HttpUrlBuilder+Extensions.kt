package net.frozendevelopment.bridgeio.extensions

import okhttp3.HttpUrl

internal fun HttpUrl.Builder.applyBridgeHost(host: String?): HttpUrl.Builder {
    host ?: return this
    return this.host(host)
}

internal fun HttpUrl.Builder.applyBridgePath(token: String?, currentSegments: List<String>): HttpUrl.Builder {
    val newSegments = mutableListOf("api")

    if (!token.isNullOrEmpty()) {
        newSegments.add(token)
    }

    newSegments.addAll(currentSegments)

    return this.encodedPath(newSegments.joinToString(separator = "/", prefix = "/"))
}
