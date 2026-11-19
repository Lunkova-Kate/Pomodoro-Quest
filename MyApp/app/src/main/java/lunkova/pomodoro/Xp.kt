package lunkova.pomodoro


    fun rewardXp(minutes: Int): Int {
    return when (minutes) {
        1-> 500
        15 -> 50
        25 -> 100
        45 -> 180
        60 -> 250
        else -> 0
    }
    }
