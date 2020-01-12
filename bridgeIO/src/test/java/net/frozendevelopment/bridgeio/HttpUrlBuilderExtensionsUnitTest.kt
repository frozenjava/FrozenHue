package net.frozendevelopment.bridgeio

import net.frozendevelopment.bridgeio.extensions.applyBridgeHost
import net.frozendevelopment.bridgeio.extensions.applyBridgePath
import okhttp3.Request
import org.junit.Assert
import org.junit.Test

class HttpUrlBuilderExtensionsUnitTest {

    @Test
    fun testApplyBridgeHost() {
        val mockRequest = Request.Builder()
            .url("http://example.com/testing/?pass=true")
            .build()

        val newUrl = mockRequest.url.newBuilder()
            .applyBridgeHost("localhost")
            .build()

        Assert.assertEquals("http://localhost/testing/?pass=true", newUrl.toString())
    }

    @Test
    fun testApplyBridgeHostWithNull() {
        val mockRequest = Request.Builder()
            .url("http://example.com/testing/?pass=true")
            .build()

        val newUrl = mockRequest.url.newBuilder()
            .applyBridgeHost(null)
            .build()

        Assert.assertEquals("http://example.com/testing/?pass=true", newUrl.toString())
    }

    @Test
    fun testApplyBridgePath() {
        val token = "e41a2a8a-70fe-44f9-b795-b0355ce3d092"

        val mockRequest = Request.Builder()
            .url("http://localhost/lights/1?on=true")
            .build()

        val newUrl = mockRequest.url.newBuilder()
            .applyBridgePath(token, mockRequest.url.pathSegments)
            .build()

        Assert.assertEquals("http://localhost/api/$token/lights/1?on=true", newUrl.toString())
    }

    @Test
    fun testApplyBridgePathWithNull() {
        val mockRequest = Request.Builder()
            .url("http://localhost/lights/1?on=true")
            .build()

        val newUrl = mockRequest.url.newBuilder()
            .applyBridgePath(null, mockRequest.url.pathSegments)
            .build()

        Assert.assertEquals("http://localhost/api/lights/1?on=true", newUrl.toString())
    }
}