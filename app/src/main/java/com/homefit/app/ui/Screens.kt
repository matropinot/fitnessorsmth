package com.homefit.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.homefit.app.domain.Difficulty
import com.homefit.app.domain.Equipment
import com.homefit.app.domain.Exercise
import com.homefit.app.domain.ExerciseCatalog
import com.homefit.app.domain.Impact
import com.homefit.app.domain.Workout
import com.homefit.app.domain.WorkoutEngine
import com.homefit.app.domain.WorkoutStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(
    viewModel: HomeFitViewModel,
    onStartWorkout: (Workout) -> Unit,
) {
    val stats by viewModel.stats.collectAsState()
    val recommended = viewModel.recommended
    val quick = remember { WorkoutEngine.quickWorkouts() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("HomeFit", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text("Ready for a good session?", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Choose something realistic for today. Consistency beats perfection.", style = MaterialTheme.typography.bodyLarge)
        }
        item {
            Card {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Recommended today", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    Text(recommended.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(recommended.subtitle, style = MaterialTheme.typography.bodyMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        recommended.tags.forEach { AssistChip(onClick = {}, label = { Text(it) }) }
                    }
                    Button(onClick = { onStartWorkout(recommended) }, modifier = Modifier.fillMaxWidth()) {
                        Text("Start ${recommended.durationMinutes}-min workout")
                    }
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                StatCard("Sessions", stats.completedSessions.toString(), Modifier.weight(1f))
                StatCard("Minutes", stats.totalMinutes.toString(), Modifier.weight(1f))
            }
        }
        item {
            Text("Quick starts", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        items(quick) { workout ->
            WorkoutListCard(workout, onStartWorkout)
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun WorkoutsScreen(onStartWorkout: (Workout) -> Unit) {
    val workouts = remember { WorkoutEngine.quickWorkouts() + listOf(
        WorkoutEngine.recommended(Difficulty.BEGINNER, 30, true).copy(id = "full-body-30", title = "30-Minute Home Flow"),
        WorkoutEngine.recommended(Difficulty.INTERMEDIATE, 20, true).copy(id = "low-impact-20", title = "Low-Impact Conditioning"),
    ) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Text("Workouts", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Structured sessions with a clear purpose, level and duration.", style = MaterialTheme.typography.bodyLarge)
        }
        item { SectionTitle("Browse by goal") }
        item {
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Full Body", "Strength", "Cardio", "Mobility", "Low Impact").forEach { AssistChip(onClick = {}, label = { Text(it) }) }
            }
        }
        items(workouts) { workout -> WorkoutListCard(workout, onStartWorkout) }
    }
}

@Composable
private fun WorkoutListCard(workout: Workout, onStartWorkout: (Workout) -> Unit) {
    Card {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(workout.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Text(workout.subtitle, style = MaterialTheme.typography.bodyMedium)
                }
                Text("${workout.durationMinutes}m", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(workout.level.name.lowercase().replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.labelMedium)
                Text("•", color = MaterialTheme.colorScheme.outline)
                Text("${workout.exercises.size} moves", style = MaterialTheme.typography.labelMedium)
            }
            Button(onClick = { onStartWorkout(workout) }, modifier = Modifier.fillMaxWidth()) { Text("Start") }
        }
    }
}

@Composable
fun ExercisesScreen() {
    var query by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("All") }
    val categories = remember { listOf("All") + ExerciseCatalog.all.map { it.category }.distinct().sorted() }
    val filtered = remember(query, category) {
        ExerciseCatalog.all.filter { exercise ->
            (category == "All" || exercise.category == category) &&
                (query.isBlank() || exercise.name.contains(query, ignoreCase = true) || exercise.primaryMuscles.any { it.contains(query, true) })
        }
    }

    Column(Modifier.fillMaxSize()) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Exercises", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Search exercises or muscles") },
            )
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.forEach { item ->
                    FilterChip(selected = category == item, onClick = { category = item }, label = { Text(item) })
                }
            }
        }
        LazyColumn(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (filtered.isEmpty()) {
                item { EmptyState("No matching exercises", "Try a different name or reset the filter.") }
            }
            items(filtered, key = { it.id }) { exercise -> ExerciseCard(exercise) }
        }
    }
}

@Composable
private fun ExerciseCard(exercise: Exercise) {
    Card {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(exercise.name, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(exercise.difficulty.name.lowercase().replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.labelSmall)
            }
            Text(exercise.instructions, style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(exercise.category, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Text("•", color = MaterialTheme.colorScheme.outline)
                Text(exercise.impact.name.lowercase().replaceFirstChar { it.uppercase() } + " impact", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun ProgressScreen(statsFlow: StateFlow<WorkoutStats>) {
    val stats by statsFlow.collectAsState()
    val weeklyGoalMinutes = 150
    val ratio = (stats.totalMinutes.toFloat() / weeklyGoalMinutes).coerceIn(0f, 1f)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text("Progress", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Useful signals about your training—not a score for your body.", style = MaterialTheme.typography.bodyLarge)
        }
        item {
            Card {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Training minutes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("${stats.totalMinutes} min", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator({ ratio }, Modifier.fillMaxWidth())
                    Text("${weeklyGoalMinutes - stats.totalMinutes.coerceAtMost(weeklyGoalMinutes)} min to the example weekly reference point", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                StatCard("Completed", stats.completedSessions.toString(), Modifier.weight(1f))
                StatCard("Saved history", stats.recentWorkoutIds.size.toString(), Modifier.weight(1f))
            }
        }
        item { SectionTitle("Training principles") }
        item { PrincipleCard("Consistency", "Regular sessions matter more than trying to do everything in one day.") }
        item { PrincipleCard("Progression", "Use easier variations when needed, then gradually increase control, duration or resistance.") }
        item { PrincipleCard("Recovery", "Rest and lighter movement are part of a sustainable training routine.") }
    }
}

@Composable
private fun PrincipleCard(title: String, body: String) {
    Card { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(body, style = MaterialTheme.typography.bodyMedium)
    } }
}

@Composable
fun SettingsScreen() {
    var sound by rememberSaveable { mutableStateOf(true) }
    var haptics by rememberSaveable { mutableStateOf(true) }
    var reminders by rememberSaveable { mutableStateOf(false) }
    var lowImpact by rememberSaveable { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Keep control of how HomeFit behaves on your device.", style = MaterialTheme.typography.bodyLarge)
        }
        item { SettingToggle("Workout sounds", "Countdown and transition cues", sound) { sound = it } }
        item { SettingToggle("Haptics", "Optional tactile feedback", haptics) { haptics = it } }
        item { SettingToggle("Reminders", "Gentle reminders you control", reminders) { reminders = it } }
        item { SettingToggle("Prefer low impact", "Prioritize low-impact alternatives in recommendations", lowImpact) { lowImpact = it } }
        item { HorizontalDivider(Modifier.padding(vertical = 8.dp)) }
        item {
            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Safety", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("HomeFit is general fitness software, not a medical diagnostic tool. Choose a comfortable level and use stable equipment.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        item {
            TextButton(onClick = {}) { Text("Reset local progress") }
        }
    }
}

@Composable
private fun SettingToggle(title: String, subtitle: String, value: Boolean, onValueChange: (Boolean) -> Unit) {
    Surface(tonalElevation = 1.dp, shape = MaterialTheme.shapes.medium) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
            Switch(checked = value, onCheckedChange = onValueChange)
        }
    }
}

@Composable
fun WorkoutPlayerScreen(
    workout: Workout,
    onFinished: () -> Unit,
    onClose: () -> Unit,
) {
    var exerciseIndex by rememberSaveable { mutableIntStateOf(0) }
    var remaining by rememberSaveable {
        mutableIntStateOf(workout.exercises.firstOrNull()?.let { it.workSeconds ?: 30 } ?: 0)
    }
    var paused by rememberSaveable { mutableStateOf(false) }
    var completed by rememberSaveable { mutableStateOf(false) }
    val current = workout.exercises.getOrNull(exerciseIndex)
    val exercise = current?.let { ExerciseCatalog.find(it.exerciseId) }

    LaunchedEffect(exerciseIndex, paused, completed) {
        if (!paused && !completed && current != null && remaining > 0) {
            while (remaining > 0 && !paused && !completed) {
                delay(1000)
                if (!paused && !completed) remaining -= 1
            }
        }
        if (!paused && !completed && current != null && remaining <= 0) {
            if (exerciseIndex == workout.exercises.lastIndex) {
                completed = true
            } else {
                val nextIndex = exerciseIndex + 1
                exerciseIndex = nextIndex
                remaining = workout.exercises[nextIndex].workSeconds ?: 30
            }
        }
    }

    if (completed) {
        WorkoutCompleteScreen(workout, onFinished)
        return
    }

    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onClose) { Text("Close") }
                Spacer(Modifier.weight(1f))
                Text("${exerciseIndex + 1}/${workout.exercises.size}", style = MaterialTheme.typography.labelLarge)
            }
            LinearProgressIndicator(
                progress = { (exerciseIndex + 1).toFloat() / workout.exercises.size },
                modifier = Modifier.fillMaxWidth(),
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier.size(170.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(remaining.toString(), style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Spacer(Modifier.height(24.dp))
                Text(exercise?.name ?: "Exercise", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text(exercise?.instructions ?: "Follow the movement at a comfortable pace.", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    exercise?.primaryMuscles?.forEach { AssistChip(onClick = {}, label = { Text(it) }) }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { paused = !paused }, modifier = Modifier.weight(1f)) {
                    Text(if (paused) "Resume" else "Pause")
                }
                Button(onClick = {
                    if (exerciseIndex == workout.exercises.lastIndex) completed = true
                    else {
                        exerciseIndex += 1
                        remaining = workout.exercises[exerciseIndex].workSeconds ?: 30
                    }
                }, modifier = Modifier.weight(1f)) { Text("Next") }
            }
        }
    }
}

@Composable
private fun WorkoutCompleteScreen(workout: Workout, onFinished: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.size(100.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
            Text("✓", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Spacer(Modifier.height(20.dp))
        Text("Session complete", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("You finished ${workout.title}.", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(20.dp))
        Button(onClick = onFinished, modifier = Modifier.fillMaxWidth()) { Text("Save and return home") }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
}

@Composable
private fun EmptyState(title: String, body: String) {
    Card {
        Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text(body, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }
}
