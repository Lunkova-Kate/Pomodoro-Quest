package lunkova.pomodoro

object Level {
    fun fromXp(totalXp: Int): Int {
        var level = 1
        var xpLeft = totalXp

        while (xpLeft >= xpToNextLevel(level)) {
            xpLeft -= xpToNextLevel(level)
            level++
        }

        return level
    }
    fun xpToNextLevel(level: Int): Int {
        return 100 + 50 * (level - 1)
    }
    fun xpInCurrentLevel(totalXp: Int): Int {
        var xpLeft = totalXp
        var level = 1

        while (xpLeft >= xpToNextLevel(level)) {
            xpLeft -= xpToNextLevel(level)
            level++
        }

        return xpLeft
    }
}