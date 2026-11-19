package lunkova.pomodoro

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.userDataStore by preferencesDataStore(
    name = "user_progress"
)

class UserProgressRepository(
    private val context: Context
) {
    companion object {
        private val TOTAL_XP = intPreferencesKey("total_xp")

        private val SESSION_MINUTES = intPreferencesKey("session_minutes")
        private val SESSION_REMAINING = longPreferencesKey("session_remaining")
        private val SESSION_FINISH = longPreferencesKey("session_finish")
        private val SESSION_STATE = stringPreferencesKey("session_state")
    }

    val totalXp: Flow<Int> = context.userDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[TOTAL_XP] ?: 0
        }

    suspend fun addXp(amount: Int): Int {
        val updatedPreferences = context.userDataStore.edit { preferences ->
            val currentXp = preferences[TOTAL_XP] ?: 0

            preferences[TOTAL_XP] = currentXp + amount
        }

        return updatedPreferences[TOTAL_XP] ?: 0
    }

    suspend fun saveSession(session: SavedSession) {
        context.userDataStore.edit { preferences ->
            preferences[SESSION_MINUTES] = session.userChooseTimer
            preferences[SESSION_REMAINING] = session.remainingOnSaveMillis
            preferences[SESSION_FINISH] = session.finishSession
            preferences[SESSION_STATE] = session.stateSession.name
        }
    }

    suspend fun loadSession(): SavedSession? {
        val preferences = context.userDataStore.data.first()

        val minutes = preferences[SESSION_MINUTES] ?: return null
        val remaining = preferences[SESSION_REMAINING] ?: return null
        val finish = preferences[SESSION_FINISH] ?: return null
        val stateName = preferences[SESSION_STATE] ?: return null

        val state = StateSession.entries
            .firstOrNull { it.name == stateName }
            ?: return null

        return SavedSession(
            userChooseTimer = minutes,
            remainingOnSaveMillis = remaining,
            finishSession = finish,
            stateSession = state
        )
    }
}