package com.homefit.app.domain

object WorkoutScreenCatalog {
    fun find(id: String?): Workout? = when (id) {
        "full-body-15" -> WorkoutEngine.quickWorkouts().first { it.id == "full-body-15" }
        "full-body-20" -> WorkoutEngine.quickWorkouts().first { it.id == "full-body-20" }
        "full-body-30" -> WorkoutEngine.recommended(Difficulty.BEGINNER, 30, true).copy(id = "full-body-30", title = "30-Minute Home Flow")
        "low-impact-20" -> WorkoutEngine.recommended(Difficulty.INTERMEDIATE, 20, true).copy(id = "low-impact-20", title = "Low-Impact Conditioning")
        "recommended-10-beginner-low" -> WorkoutEngine.recommended(Difficulty.BEGINNER, 10, true)
        "recommended-15-beginner-low" -> WorkoutEngine.recommended(Difficulty.BEGINNER, 15, true)
        "recommended-20-intermediate-full" -> WorkoutEngine.recommended(Difficulty.INTERMEDIATE, 20, false)
        else -> null
    }
}
