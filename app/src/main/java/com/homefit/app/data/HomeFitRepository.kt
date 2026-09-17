package com.homefit.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.homefit.app.domain.WorkoutStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.homeFitDataStore by preferencesDataStore(name = "homefit")

class HomeFitRepository(private val context: Context) {
    private object Keys {
        val completedSessions = intPreferencesKey("completed_sessions")
        val totalMinutes = intPreferencesKey("total_minutes")
        val history = stringSetPreferencesKey("history")
    }

    val stats: Flow<WorkoutStats> = context.homeFitDataStore.data.map { prefs ->
        WorkoutStats(
            completedSessions = prefs[Keys.completedSessions] ?: 0,
            totalMinutes = prefs[Keys.totalMinutes] ?: 0,
            recentWorkoutIds = prefs[Keys.history].orEmpty().toList().sortedDescending().mapNotNull {
                it.substringAfter('|', missingDelimiterValue = "").ifBlank { null }
            }.take(12),
        )
    }

    suspend fun recordCompletedWorkout(workoutId: String, minutes: Int) {
        context.homeFitDataStore.edit { prefs ->
            val sessions = (prefs[Keys.completedSessions] ?: 0) + 1
            val total = (prefs[Keys.totalMinutes] ?: 0) + minutes
            val history = (prefs[Keys.history] ?: emptySet()).toMutableSet()
            history.add("${System.currentTimeMillis()}|$workoutId|$minutes")
            prefs[Keys.completedSessions] = sessions
            prefs[Keys.totalMinutes] = total
            prefs[Keys.history] = history
        }
    }
}
