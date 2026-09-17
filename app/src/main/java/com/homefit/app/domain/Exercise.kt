package com.homefit.app.domain

enum class Difficulty { BEGINNER, INTERMEDIATE, ADVANCED }
enum class Equipment { NONE, CHAIR, BAND, DUMBBELL, KETTLEBELL, PULL_UP_BAR, MAT }
enum class Impact { LOW, MODERATE, HIGH }
enum class MovementPattern { SQUAT, HINGE, LUNGE, PUSH, PULL, CORE, LOCOMOTION, JUMP, MOBILITY, BALANCE, CARDIO }

data class Exercise(
    val id: String,
    val name: String,
    val category: String,
    val pattern: MovementPattern,
    val difficulty: Difficulty,
    val equipment: Set<Equipment>,
    val impact: Impact,
    val primaryMuscles: List<String>,
    val instructions: String,
    val easierId: String? = null,
    val harderId: String? = null,
    val durationSeconds: Int? = null,
    val repetitions: Int? = null,
)
