package lunkova.pomodoro

import kotlinx.coroutines.CompletableDeferred

sealed interface SessionCommand {

    data class Save(
        val session: SavedSession
    ) : SessionCommand

    data class Complete(
        val earnedXp: Int,
        val result: CompletableDeferred<Int?>
    ) : SessionCommand

    data class ClearCompleted(
        val result: CompletableDeferred<Unit>
    ) : SessionCommand
}