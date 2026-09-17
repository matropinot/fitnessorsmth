package com.homefit.app.domain

data class WorkoutExercise(
    val exerciseId: String,
    val workSeconds: Int? = null,
    val repetitions: Int? = null,
    val restSecondsAfter: Int = 20,
)

data class Workout(
    val id: String,
    val title: String,
    val subtitle: String,
    val durationMinutes: Int,
    val level: Difficulty,
    val exercises: List<WorkoutExercise>,
    val tags: List<String>,
)

data class WorkoutStats(
    val completedSessions: Int = 0,
    val totalMinutes: Int = 0,
    val recentWorkoutIds: List<String> = emptyList(),
)
