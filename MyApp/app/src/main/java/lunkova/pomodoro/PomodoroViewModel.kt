package lunkova.pomodoro

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import lunkova.pomodoro.Timer.TimerProgress

class PomodoroViewModel(app: Application): AndroidViewModel(app) {
    private val _timer = MutableStateFlow(TimerProgress())
    val timer = _timer.asStateFlow()
    private val repository = UserProgressRepository(app)
    private val _userProgress = MutableStateFlow(
        UserProgress(
            totalXp = 0
        )
    )

    val userProgress = _userProgress.asStateFlow()
    init {
        viewModelScope.launch {
            repository.totalXp.collect { savedXp ->
                _userProgress.value = _userProgress.value.copy (totalXp = savedXp)
            }
        }
    }
    private var timerJob: Job? = null


    fun startSession() {
        if (_timer.value.stateSession != StateSession.SLEEPING) {
            return
        }

        _timer.value = _timer.value.copy(stateSession = StateSession.RUNNING)
        launchTimer()
        }


    fun pauseSession() {
        if (_timer.value.stateSession == StateSession.RUNNING) {
            timerJob?.cancel()
            _timer.value = _timer.value.copy(stateSession = StateSession.PAUSED)
        }

    }

    fun resumeSession() {
        if (_timer.value.stateSession == StateSession.PAUSED) {
            _timer.value = _timer.value.copy(
                stateSession = StateSession.RUNNING
            )

            launchTimer()
        }
    }

    fun cancelSession() {
        if((_timer.value.stateSession == StateSession.PAUSED) || (_timer.value.stateSession == StateSession.RUNNING)) {
            timerJob?.cancel()
            _timer.value = _timer.value.copy(
                stateSession = StateSession.CANCELLED)
        }
    }

    private suspend fun  completedSession() {
        if(_timer.value.stateSession == StateSession.RUNNING && _timer.value.remainingMillis == 0L) {
            val earnedXp = rewardXp(_timer.value.userTimer)
            val newTotalXp = repository.addXp(earnedXp)

            _userProgress.value = _userProgress.value.copy(
                totalXp = newTotalXp
            )
            _timer.value = _timer.value.copy(
                stateSession = StateSession.COMPLETED)

        }
    }
    private fun launchTimer() {
        timerJob = viewModelScope.launch {

            val finishTime = SystemClock.elapsedRealtime() +
                    _timer.value.remainingMillis

            while (true) {
                val millisLeft = (
                        finishTime - SystemClock.elapsedRealtime()
                        ).coerceAtLeast(0L)

                _timer.value = _timer.value.copy(
                    remainingMillis = millisLeft
                )

                if (millisLeft == 0L) {
                    break
                }

                delay(100)
            }

            completedSession()
        }
    }

    fun resetSession(minutes: Int) {
        timerJob?.cancel()
        timerJob = null

        _timer.value = TimerProgress(userTimer = minutes)
    }
//    private fun addXp(amount: Int) {
//        _userProgress.value = _userProgress.value.copy(
//            totalXp = _userProgress.value.totalXp + amount
//        )
//    }
}
