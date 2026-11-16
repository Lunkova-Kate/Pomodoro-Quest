package lunkova.pomodoro.Timer

import lunkova.pomodoro.StateSession

data class TimerProgress(
    val userTimer: Int = FocusSettings.defaultMinutes,
    val remainingTimer: Int = userTimer * 60,
    val stateSession: StateSession = StateSession.SLEEPING
)