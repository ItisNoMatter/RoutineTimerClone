package jp.itIsNoMatter.routineTimerClone.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import jp.itIsNoMatter.routineTimerClone.data.remote.datasource.RoutineRemoteDataSource
import jp.itIsNoMatter.routineTimerClone.data.remote.datasource.RoutineRemoteDataSourceImpl
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // ① Ktor Client（通信機本体）の設定
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            // JSONの翻訳係をセットアップ
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true // サーバーから知らないデータが来てもアプリをクラッシュさせない優しさ設定
                        prettyPrint = true
                    },
                )
            }
            // 接続先に到達できない場合でも無期限に待たされないようにするタイムアウト設定
            install(HttpTimeout) {
                connectTimeoutMillis = 5000
                requestTimeoutMillis = 8000
                socketTimeoutMillis = 8000
            }
        }
    }

    // ② 通信係（DataSource）の設定
    @Provides
    @Singleton
    fun provideRoutineRemoteDataSource(httpClient: HttpClient): RoutineRemoteDataSource {
        return RoutineRemoteDataSourceImpl(httpClient)
    }
}
