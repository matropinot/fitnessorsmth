package com.homefit.app

import com.homefit.app.domain.Difficulty
import com.homefit.app.domain.Equipment
import com.homefit.app.domain.ExerciseCatalog
import com.homefit.app.domain.Impact
import com.homefit.app.domain.WorkoutEngine
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WorkoutEngineTest {
    @Test
    fun recommendedBeginnerLowImpactHasNoHighImpactExercise() {
        val workout = WorkoutEngine.recommended(Difficulty.BEGINNER, 15, lowImpact = true)
        val exercises = workout.exercises.mapNotNull { ExerciseCatalog.find(it.exerciseId) }

        assertTrue(exercises.isNotEmpty())
        assertTrue(exercises.all { it.impact != Impact.HIGH })
        assertTrue(exercises.all { it.equipment.all { eq -> eq == Equipment.NONE || eq == Equipment.MAT } })
    }

    @Test
    fun recommendedWorkoutIsDeterministic() {
        val first = WorkoutEngine.recommended(Difficulty.INTERMEDIATE, 20, false)
        val second = WorkoutEngine.recommended(Difficulty.INTERMEDIATE, 20, false)

        assertEquals(first, second)
    }
}
