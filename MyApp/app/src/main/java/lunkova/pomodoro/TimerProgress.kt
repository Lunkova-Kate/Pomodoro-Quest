package lunkova.pomodoro

data class TimerProgress (
    val remindTimer: Int = userTimer * 60,
    val userTimer: Int = 25,
    val stateSession: StateSession = StateSession.SLEEPING
)