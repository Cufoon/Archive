package cufoon.litkeep.android.service

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.time.OffsetDateTime

@JsonClass(generateAdapter = true)
data class ReqBillRecordQuery(
    var userID: String,
    var kindID: String,
    var startTime: OffsetDateTime,
    var endTime: OffsetDateTime
)

@JsonClass(generateAdapter = true)
data class BillRecord(
    var ID: Int? = null,
    var CreatedAt: String? = null,
    var UpdatedAt: String? = null,
    var DeletedAt: String? = null,
    var UserID: String? = null,
    var Type: Int? = null,
    var Kind: String? = null,
    var Value: Double? = null,
    var Time: OffsetDateTime? = null,
    var Mark: String? = null
)

@JsonClass(generateAdapter = true)
data class ResBillRecordQuery(var record: List<BillRecord>)

interface DefBillRecordService {
    @POST("bill")
    suspend fun query(@Body data: ReqBillRecordQuery): Response<HttpResponse<ResBillRecordQuery>>
}

val billRecordRetrofit: DefBillRecordService =
    retrofitWithTokenJSON.create(DefBillRecordService::class.java)

object BillRecordService {
    suspend fun query(data: ReqBillRecordQuery): Pair<Err?, ResBillRecordQuery?> {
        return request {
            billRecordRetrofit.query(data)
        }
    }
}
