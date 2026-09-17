package com.homefit.app.domain

object WorkoutEngine {
    fun recommended(level: Difficulty = Difficulty.BEGINNER, durationMinutes: Int = 15, lowImpact: Boolean = true): Workout {
        val eligible = ExerciseCatalog.all.filter { exercise ->
            exercise.difficulty.ordinal <= level.ordinal &&
                (!lowImpact || exercise.impact != Impact.HIGH) &&
                exercise.equipment.all { it == Equipment.NONE || it == Equipment.MAT }
        }

        val preferredPatterns = listOf(
            MovementPattern.SQUAT,
            MovementPattern.PUSH,
            MovementPattern.HINGE,
            MovementPattern.CORE,
            MovementPattern.CARDIO,
        )

        val picked = preferredPatterns.mapNotNull { pattern -> eligible.firstOrNull { it.pattern == pattern } }
            .distinctBy { it.id }
            .take(5)

        val targetCount = when {
            durationMinutes <= 10 -> 3
            durationMinutes <= 20 -> 5
            else -> 7
        }

        val remaining = eligible.filter { it !in picked }
            .sortedWith(compareBy<Exercise> { it.impact.ordinal }.thenBy { it.difficulty.ordinal })

        val final = (picked + remaining).distinctBy { it.id }.take(targetCount.coerceAtMost(7))

        return Workout(
            id = "recommended-${durationMinutes}-${level.name.lowercase()}-${if (lowImpact) "low" else "full"}",
            title = if (lowImpact) "Balanced Low-Impact" else "Full-Body Starter",
            subtitle = "A practical home session with strength, core and cardio",
            durationMinutes = durationMinutes,
            level = level,
            exercises = final.mapIndexed { index, exercise ->
                WorkoutExercise(
                    exerciseId = exercise.id,
                    workSeconds = exercise.durationSeconds ?: 35,
                    repetitions = exercise.repetitions,
                    restSecondsAfter = if (index == final.lastIndex) 0 else 20,
                )
            },
            tags = listOf("Full Body", if (lowImpact) "Low Impact" else "Conditioning", "No Equipment"),
        )
    }

    fun quickWorkouts(): List<Workout> = listOf(
        recommended(Difficulty.BEGINNER, 10, true),
        recommended(Difficulty.BEGINNER, 15, true).copy(id = "full-body-15"),
        recommended(Difficulty.INTERMEDIATE, 20, false).copy(id = "full-body-20"),
    )
}
