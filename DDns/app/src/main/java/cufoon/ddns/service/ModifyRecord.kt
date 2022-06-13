package cufoon.ddns.service

import com.jsoniter.output.JsonStream
import cufoon.ddns.util.SignObject
import cufoon.ddns.util.SignV3
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

object ModifyRecord {
    fun changeARecord(ip: String, subDomain: String = "a"): Boolean {
        val dataTreeMap = TreeMap<String, Any>()
        dataTreeMap["Domain"] = "xxxxx.yyyyy"
        dataTreeMap["DomainId"] = 12345678
        dataTreeMap["SubDomain"] = subDomain
        dataTreeMap["RecordType"] = "A"
        dataTreeMap["RecordLine"] = "默认"
        dataTreeMap["Value"] = ip
        dataTreeMap["RecordId"] = 55555555
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
        return client.newCall(request.build()).execute().use { response ->
            print(response)
            response.isSuccessful
        }
    }
}