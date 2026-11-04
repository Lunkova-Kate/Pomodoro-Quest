package lunkova.pomodoro

data class TimerProgress (
    val userTimer: Int = 25,
    val remainingTimer: Int = userTimer * 60,
    val stateSession: StateSession = StateSession.SLEEPING
)