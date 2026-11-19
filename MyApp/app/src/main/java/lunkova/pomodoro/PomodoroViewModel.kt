package lunkova.pomodoro

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import lunkova.pomodoro.Timer.TimerProgress
import kotlinx.coroutines.channels.Channel

class PomodoroViewModel(app: Application): AndroidViewModel(app) {
    private val _timer = MutableStateFlow(TimerProgress())
    val timer = _timer.asStateFlow()
    private val _restored = MutableStateFlow(false)
    val restored = _restored.asStateFlow()

    private val repository = UserProgressRepository(app)
    private val _userProgress = MutableStateFlow(
        UserProgress(
            totalXp = 0
        )
    )

    val userProgress = _userProgress.asStateFlow()
    init {
        viewModelScope.launch {
            for (session in saveQueue) {
                repository.saveSession(session)
            }
        }


        viewModelScope.launch {
            try {
                restoreSession()
            } finally {
                _restored.value = true
            }

            repository.totalXp.collect { savedXp ->
                _userProgress.value = _userProgress.value.copy(
                    totalXp = savedXp
                )
            }
        }
    }
    private var timerJob: Job? = null
    private val saveQueue = Channel<SavedSession>(Channel.UNLIMITED)

    private fun enqueueSave(session: SavedSession) {
        saveQueue.trySend(session)
    }
    fun startSession() {
        if (_timer.value.stateSession != StateSession.SLEEPING) {
            return
        }
        val remaining = _timer.value.remainingMillis
        finishElapsedMillisInApp = SystemClock.elapsedRealtime() + remaining

        val finishRestoreTime = System.currentTimeMillis() + remaining
        _timer.value = _timer.value.copy(stateSession = StateSession.RUNNING)
        timerJob = viewModelScope.launch {
            enqueueSave(
                SavedSession(
                    userChooseTimer = _timer.value.userTimer,
                    remainingOnSaveMillis = remaining,
                    finishSession = finishRestoreTime,
                    stateSession = StateSession.RUNNING
                )
            )
        }
            timerJob = viewModelScope.launch {
                runTimerLoop()
            }

    }

    private suspend fun runTimerLoop() {
        while (_timer.value.stateSession == StateSession.RUNNING) {

            val remaining = (
                    finishElapsedMillisInApp - SystemClock.elapsedRealtime()
                    ).coerceAtLeast(0L)

            _timer.value = _timer.value.copy(
                remainingMillis = remaining
            )

            if (remaining == 0L) {
                completedSession()
                break
            }

            delay(100)
        }
    }

    private suspend fun restoreSession() {
        val saved = repository.loadSession() ?: return

        when (saved.stateSession) {

            StateSession.RUNNING -> {
                val remaining = (
                        saved.finishSession - System.currentTimeMillis()
                        ).coerceAtLeast(0L)

                _timer.value = TimerProgress(
                    userTimer = saved.userChooseTimer,
                    remainingMillis = remaining,
                    stateSession = StateSession.RUNNING
                )

                if (remaining == 0L) {
                    completedSession()
                } else {
                    finishElapsedMillisInApp =
                        SystemClock.elapsedRealtime() + remaining

                    timerJob = viewModelScope.launch {
                        runTimerLoop()
                    }
                }
            }

            StateSession.PAUSED -> {
                _timer.value = TimerProgress(
                    userTimer = saved.userChooseTimer,
                    remainingMillis = saved.remainingOnSaveMillis,
                    stateSession = StateSession.PAUSED
                )
            }

            StateSession.COMPLETED -> {
                _timer.value = TimerProgress(
                    userTimer = saved.userChooseTimer,
                    remainingMillis = 0L,
                    stateSession = StateSession.COMPLETED
                )
            }

            else -> Unit
        }
    }
    fun pauseSession() {
        if (_timer.value.stateSession == StateSession.RUNNING) {
            val remaining = (
                    finishElapsedMillisInApp - SystemClock.elapsedRealtime()
                    ).coerceAtLeast(0L)
            timerJob?.cancel()
            _timer.value = _timer.value.copy(remainingMillis = remaining,stateSession = StateSession.PAUSED)

                enqueueSave(
                    SavedSession(
                        userChooseTimer = _timer.value.userTimer,
                        remainingOnSaveMillis = remaining,
                        finishSession = 0L,
                        stateSession = StateSession.PAUSED
                    )
                )

        }

 }
//    val userChooseTimer: Int,
//    val remainingOnSaveMillis: Long,
//    val finishSession: Long,
//    val finishSession: StateSession)

    fun resumeSession() {
        if (_timer.value.stateSession != StateSession.PAUSED) {
            return
        }

        val remaining = _timer.value.remainingMillis

        finishElapsedMillisInApp =
            SystemClock.elapsedRealtime() + remaining

        val finishRestoreTime =
            System.currentTimeMillis() + remaining

        _timer.value = _timer.value.copy(
            stateSession = StateSession.RUNNING
        )

        timerJob = viewModelScope.launch {
            enqueueSave(
                SavedSession(
                    userChooseTimer = _timer.value.userTimer,
                    remainingOnSaveMillis = remaining,
                    finishSession = finishRestoreTime,
                    stateSession = StateSession.RUNNING
                )
            )
        }
            timerJob = viewModelScope.launch {
                runTimerLoop()
            }

    }

    fun cancelSession() {
        if (_timer.value.stateSession == StateSession.RUNNING ||
            _timer.value.stateSession == StateSession.PAUSED
        ) {
            timerJob?.cancel()

            _timer.value = _timer.value.copy(
                stateSession = StateSession.CANCELLED
            )

            val session = SavedSession(
                userChooseTimer = _timer.value.userTimer,
                remainingOnSaveMillis = _timer.value.remainingMillis,
                finishSession = 0L,
                stateSession = StateSession.CANCELLED
            )

            enqueueSave(session)
        }
    }

    private suspend fun completedSession() {
        if (_timer.value.stateSession != StateSession.RUNNING ||
            _timer.value.remainingMillis != 0L
        ) {
            return
        }

        val earnedXp = rewardXp(_timer.value.userTimer)

        val newTotalXp = repository.completeSessionIfRunning(earnedXp)

        if (newTotalXp != null) {
            _userProgress.value = _userProgress.value.copy(
                totalXp = newTotalXp
            )
        }

        _timer.value = _timer.value.copy(
            stateSession = StateSession.COMPLETED
        )
    }

    fun closeCompletedSession() {
        if (_timer.value.stateSession != StateSession.COMPLETED) {
            return
        }

        viewModelScope.launch {
            repository.clearCompletedSession()

            if (_timer.value.stateSession == StateSession.COMPLETED) {
                _timer.value = TimerProgress()
            }
        }
    }
    private var finishElapsedMillisInApp: Long = 0L

    fun resetSession(minutes: Int) {
        timerJob?.cancel()
        timerJob = null

        _timer.value = TimerProgress(userTimer = minutes)
    }

}
