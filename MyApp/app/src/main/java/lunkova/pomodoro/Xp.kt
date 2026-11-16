package lunkova.pomodoro


    fun rewardXp(minutes: Int): Int {
    return when (minutes) {
        15 -> 50
        25 -> 100
        45 -> 180
        60 -> 250
        else -> 0
    }
    }
