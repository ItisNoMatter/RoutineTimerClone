package jp.itIsNoMatter.routineTimerClone.data.remote.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import jp.itIsNoMatter.routineTimerClone.data.remote.RoutineResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface RoutineRemoteDataSource {
    suspend fun getRoutines(): List<RoutineResponse>

    suspend fun addRoutine(routine: RoutineResponse)
}

class RoutineRemoteDataSourceImpl(
    private val client: HttpClient,
) : RoutineRemoteDataSource {
    private val baseUrl = "http://10.0.2.2:8080"

    override suspend fun getRoutines(): List<RoutineResponse> {
        return withContext(Dispatchers.IO) {
            client.get("$baseUrl/routines").body()
        }
    }

    override suspend fun addRoutine(routine: RoutineResponse) {
        withContext(Dispatchers.IO) {
            client.post("$baseUrl/routines") {
                // 「今からJSONを送るよ！」とサーバーにお知らせする
                contentType(ContentType.Application.Json)

                // モック認証：DBeaverで確認した「ItIsNoMatter予備校のテナントID」を名札として貼る！
                // ※↓のIDは、DBeaverで見た実際の tenants テーブルのIDに書き換えてくださいね！
                header("X-Tenant-Id", "2a77c348-4b18-48e3-99e0-341e88f83211")

                // KotlinのオブジェクトをJSONに変換してボディに詰める（Ktorが全自動でやってくれます）
                setBody(routine)
            }
        }
    }
}
