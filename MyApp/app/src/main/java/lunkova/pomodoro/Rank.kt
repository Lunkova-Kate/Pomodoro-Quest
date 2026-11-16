package lunkova.pomodoro

object Rank {

    fun getTitle(level: Int): String {
        return when (level) {
            in 0..4 -> "Новичок"
            in 5..15 -> "Ученик"
            in 16..25 -> "Исследователь"
            in 26..42 -> "Капитан"
            in 43..50 -> "Командор"
            else -> "Адмирал"
        }
    }
}