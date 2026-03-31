package jp.itIsNoMatter.routineTimerClone.ui.routineList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.itIsNoMatter.routineTimerClone.data.remote.RoutineResponse
import jp.itIsNoMatter.routineTimerClone.data.repository.FakeRoutineRepository
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RoutineListViewModel
    @Inject
    constructor(
        private val routineRepository: RoutineRepository,
    ) : ViewModel() {
        val routines = routineRepository.getAllRoutines()

        init {
            viewModelScope.launch {
                routineRepository.syncRoutines()
            }
        }

        companion object {
            val Noop =
                RoutineListViewModel(
                    routineRepository = FakeRoutineRepository,
                )
        }

        fun deleteRoutine(routineId: String) {
            viewModelScope.launch {
                routineRepository.deleteRoutineById(routineId)
            }
        }

        fun addDummyRoutine() {
            Log.d("TestPOST", "👆 ボタンが押されました！Coroutineを開始します。")
            viewModelScope.launch {
                // 1. 送信用のダミーデータを作成
                val dummyRoutine =
                    RoutineResponse(
                        id = UUID.randomUUID().toString(),
                        name = "🤖 サーバー送信テスト！",
                        tasks = emptyList(),
                    )

                try {
                    // 2. サーバーに向かってPOST！（ここでさっき作ったモック認証も走ります）
                    routineRepository.addRoutine(dummyRoutine)

                    // 3. 成功したら、すかさずサーバーの最新状態をGETしてRoomに同期！
                    routineRepository.syncRoutines()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
