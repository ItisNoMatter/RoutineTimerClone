package jp.itIsNoMatter.routineTimerClone.data.remote.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import jp.itIsNoMatter.routineTimerClone.data.remote.RoutineResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface RoutineRemoteDataSource {
    suspend fun getRoutines(): List<RoutineResponse>
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
}
