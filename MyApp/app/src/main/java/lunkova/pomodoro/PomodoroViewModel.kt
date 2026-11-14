package lunkova.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PomodoroViewModel: ViewModel() {
    private val _timer = MutableStateFlow(TimerProgress())
    val timer = _timer.asStateFlow()
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

    private fun launchTimer() {
        timerJob = viewModelScope.launch {
            while (_timer.value.remainingTimer > 0) {
                delay(1000)
                _timer.value = _timer.value.copy(
                    remainingTimer = _timer.value.remainingTimer - 1
                )
            }
        }
    }
}
