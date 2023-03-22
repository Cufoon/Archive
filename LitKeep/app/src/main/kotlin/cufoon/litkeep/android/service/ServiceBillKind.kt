package cufoon.litkeep.android.service

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.POST


@JsonClass(generateAdapter = true)
data class BillKind(
    val UserID: String, val KindID: String, val Name: String, val Description: String
)

@JsonClass(generateAdapter = true)
data class BillKindParent(
    val UserID: String,
    val KindID: String,
    val Name: String,
    val Description: String,
    val Children: List<BillKind>?
)

@JsonClass(generateAdapter = true)
data class ResBillKindQuery(var kind: List<BillKindParent>)

interface DefBillKindService {
    @POST("kind")
    suspend fun query(): Response<HttpResponse<ResBillKindQuery>>
}

val billKindRetrofit: DefBillKindService =
    retrofitWithTokenJSON.create(DefBillKindService::class.java)

object BillKindService {
    suspend fun query(): Pair<Err?, ResBillKindQuery?> {
        return request {
            billKindRetrofit.query()
        }
    }
}
