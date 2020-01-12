package net.frozendevelopment.bridgeio.extensions

import okhttp3.Request

internal fun Request.Builder.applyBridgeHeaders(): Request.Builder {
    return this
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "text/json")
        .addHeader("User-Agent", "FrozenHue")
}
