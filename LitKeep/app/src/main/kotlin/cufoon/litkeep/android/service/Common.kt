package cufoon.litkeep.android.service

import com.squareup.moshi.JsonClass
import retrofit2.Response

@JsonClass(generateAdapter = true)
data class HttpResponse<T>(
    val code: Int,
    val info: String,
    val data: T?
)

data class Err(val code: Int, val info: String)

suspend fun <T> request(run: suspend () -> Response<HttpResponse<T>>): Pair<Err?, T?> {
    try {
        val r = run()
        if (r.code() == 401) {
            return Pair(Err(ErrorNoAuthorization, "未登录"), null)
        }
        r.body()?.let {
            if (it.code != 0) {
                return Pair(Err(it.code, it.info), null)
            }
            return Pair(null, it.data)
        }
        return Pair(null, null)
    } catch (e: Exception) {
        return Pair(Err(ErrorFromRetrofit, e.message ?: "未知错误"), null)
    }
}
