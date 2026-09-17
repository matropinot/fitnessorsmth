package com.homefit.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homefit.app.data.HomeFitRepository
import com.homefit.app.domain.Workout
import com.homefit.app.domain.WorkoutEngine
import com.homefit.app.domain.WorkoutStats
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeFitViewModel(private val repository: HomeFitRepository) : ViewModel() {
    val stats: StateFlow<WorkoutStats> = repository.stats.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        WorkoutStats(),
    )

    val recommended: Workout = WorkoutEngine.recommended()

    fun markWorkoutComplete(workout: Workout) {
        viewModelScope.launch {
            repository.recordCompletedWorkout(workout.id, workout.durationMinutes)
        }
    }
}
