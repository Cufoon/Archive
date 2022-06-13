package cufoon.ddns.util

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

data class SignObject(
    val service: String,
    val action: String,
    val version: String,
    val payload: String,
    val region: String? = null
)

object SignV3 {
    private val UTF8 = StandardCharsets.UTF_8
    private const val SECRET_ID = "hdbavbfsbvfhsbhfsbfsdbfds"
    private const val SECRET_KEY = "fsdbrsevfdssssssssssgresgresgfds"
    private const val ContentType_JSON = "application/json; charset=utf-8"

    private fun hmac256(key: ByteArray?, msg: String): ByteArray? {
        return try {
            val mac = Mac.getInstance("HmacSHA256")
            val secretKeySpec = SecretKeySpec(key, mac.algorithm)
            mac.init(secretKeySpec)
            mac.doFinal(msg.toByteArray(UTF8))
        } catch (e: Exception) {
            null
        }
    }

    private fun sha256Hex(s: String): String? {
        return try {
            val md = MessageDigest.getInstance("SHA-256")
            val d = md.digest(s.toByteArray(UTF8))
            byteToHexString(d).lowercase()
        } catch (e: Exception) {
            null
        }
    }

    private fun byteToHexString(payload: ByteArray?): String {
        if (payload == null) return "<you-passed-an-empty-byte-array>"
        val stringBuilder = StringBuilder(payload.size)
        for (byteChar in payload) stringBuilder.append(String.format("%02X", byteChar))
        return stringBuilder.toString()
    }

    fun signPostIt(postData: SignObject): TreeMap<String, String> {
        val service = postData.service
        val host = "$service.tencentcloudapi.com"
        val region = postData.region ?: ""
        val action = postData.action
        val version = postData.version
        val algorithm = "TC3-HMAC-SHA256"
        val timestamp = Instant.now().epochSecond.toString()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.format(Date((timestamp + "000").toLong()))

        // ************* 步骤 1：拼接规范请求串 *************
        val httpRequestMethod = "POST"
        val canonicalUri = "/"
        val canonicalQueryString = ""
        val canonicalHeaders = "content-type:application/json; charset=utf-8\nhost:$host\n"
        val signedHeaders = "content-type;host"
        val payload = postData.payload
        val hashedRequestPayload = sha256Hex(payload)
        val canonicalRequest = """
               |$httpRequestMethod
               |$canonicalUri
               |$canonicalQueryString
               |$canonicalHeaders
               |$signedHeaders
               |$hashedRequestPayload
               """.trimMargin()

        // ************* 步骤 2：拼接待签名字符串 *************
        val credentialScope = "$date/$service/tc3_request"
        val hashedCanonicalRequest = sha256Hex(canonicalRequest)
        val stringToSign = """
               |$algorithm
               |$timestamp
               |$credentialScope
               |$hashedCanonicalRequest
               """.trimMargin()

        // ************* 步骤 3：计算签名 *************
        val secretDate = hmac256(("TC3$SECRET_KEY").toByteArray(UTF8), date)
        val secretService = hmac256(secretDate, service)
        val secretSigning = hmac256(secretService, "tc3_request")
        val signature = byteToHexString(hmac256(secretSigning, stringToSign)).lowercase()
        println(signature)

        // ************* 步骤 4：拼接 Authorization *************
        val authorization =
            """$algorithm Credential=$SECRET_ID/$credentialScope, SignedHeaders=$signedHeaders, Signature=$signature"""
        val headers = TreeMap<String, String>()
        headers["Authorization"] = authorization
        headers["Content-Type"] = ContentType_JSON
        headers["Host"] = host
        headers["X-TC-Action"] = action
        headers["X-TC-Timestamp"] = timestamp
        headers["X-TC-Version"] = version
        if (region != "") {
            headers["X-TC-Region"] = region
        }
        return headers
    }
}