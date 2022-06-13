package cufoon.ddns

import com.jsoniter.output.JsonStream
import cufoon.ddns.util.SignV3
import cufoon.ddns.util.SignObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testSign() {
        val dataTreeMap = TreeMap<String, Any>()
        dataTreeMap["Domain"] = "xxxx.yyyy"
        dataTreeMap["DomainId"] = 12345678
        dataTreeMap["SubDomain"] = "a"
        dataTreeMap["RecordType"] = "A"
        dataTreeMap["RecordLine"] = "默认"
        dataTreeMap["Value"] = "1.1.1.1"
        dataTreeMap["RecordId"] = 5555555555
        val data = JsonStream.serialize(dataTreeMap)
        val headers = SignV3.signPostIt(SignObject("dnspod", "ModifyRecord", "2021-03-23", data))
        val client = OkHttpClient()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url("https://dnspod.tencentcloudapi.com")
            .post(data.toRequestBody(mediaType))
        headers.forEach { (key, value) ->
            request.addHeader(key, value)
        }
        client.newCall(request.build()).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            print(response)
        }
    }

    @Test
    fun changeARecord(){

    }
}