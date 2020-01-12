package net.frozendevelopment.bridgeio

import net.frozendevelopment.bridgeio.extensions.applyBridgeHeaders
import okhttp3.Request
import org.junit.Assert
import org.junit.Test

class RequestBuilderExtensionsUnitTest {

    @Test
    fun testApplyBridgeHeaders() {
        val request = Request.Builder()
            .url("http://localhost")
            .applyBridgeHeaders()
            .build()

        Assert.assertNotNull(request.headers.get("Accept"))
        Assert.assertNotNull(request.headers.get("Content-Type"))
        Assert.assertNotNull(request.headers.get("User-Agent"))
        Assert.assertEquals("application/json", request.headers["Accept"])
        Assert.assertEquals("text/json", request.headers["Content-Type"])
        Assert.assertEquals("FrozenHue", request.headers["User-Agent"])
    }
}