package lunkova.pomodoro

data class SavedSession(
    val userChooseTimer: Int,
    val remainingOnSaveMillis: Long,
    val finishSession: Long,
    val stateSession: StateSession)
