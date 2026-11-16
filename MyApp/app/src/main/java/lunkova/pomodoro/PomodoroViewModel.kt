package lunkova.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import lunkova.pomodoro.Timer.TimerProgress

class PomodoroViewModel: ViewModel() {
    private val _timer = MutableStateFlow(TimerProgress())
    val timer = _timer.asStateFlow()
    private val _userProgress = MutableStateFlow(
        UserProgress(
            totalXp = 0
        )
    )

    val userProgress = _userProgress.asStateFlow()
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

    private fun  completedSession() {
        if(_timer.value.stateSession == StateSession.RUNNING && _timer.value.remainingTimer == 0) {
//            timerJob?.cancel()
            addXp(rewardXp(_timer.value.userTimer))
            _timer.value = _timer.value.copy(
                stateSession = StateSession.COMPLETED)

        }
    }
    private fun launchTimer() {
        timerJob = viewModelScope.launch {
            while (_timer.value.remainingTimer > 0) {
                delay(1000)

                _timer.value = _timer.value.copy(
                    remainingTimer = _timer.value.remainingTimer - 1
                )
            }
           completedSession()
        }
    }

    fun resetSession(minutes: Int) {
        timerJob?.cancel()
        timerJob = null

        _timer.value = TimerProgress(userTimer = minutes)
    }
    private fun addXp(amount: Int) {
        _userProgress.value = _userProgress.value.copy(
            totalXp = _userProgress.value.totalXp + amount
        )
    }
}
