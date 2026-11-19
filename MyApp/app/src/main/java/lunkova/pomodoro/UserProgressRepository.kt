package lunkova.pomodoro

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
}