package lunkova.pomodoro.Timer

import lunkova.pomodoro.StateSession

data class TimerProgress(
    val userTimer: Int = FocusSettings.defaultMinutes,
    val remainingMillis: Long = userTimer * 60_000L,
    val stateSession: StateSession = StateSession.SLEEPING
)