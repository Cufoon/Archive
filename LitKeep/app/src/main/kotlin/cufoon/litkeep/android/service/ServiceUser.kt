package cufoon.litkeep.android.service

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

@JsonClass(generateAdapter = true)
data class ReqUserLogin(@Json(name = "email") val username: String, val password: String)

@JsonClass(generateAdapter = true)
data class ResUserLoginRes(val logined: Boolean, val token: String)

interface DefUserService {
    @POST("user/login")
    suspend fun login(@Body data: ReqUserLogin): Response<HttpResponse<ResUserLoginRes>>
}

val userRetrofit: DefUserService = retrofitJSON.create(DefUserService::class.java)

object UserService {
    suspend fun login(username: String, password: String): Pair<Err?, ResUserLoginRes?> {
        return request {
            userRetrofit.login(ReqUserLogin(username, password))
        }
    }
}
