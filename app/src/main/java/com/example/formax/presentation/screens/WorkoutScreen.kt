package com.example.formax.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formax.domain.engine.AlternativesEngine
import com.example.formax.domain.engine.ProgressionEngine
import com.example.formax.domain.engine.ProgressionSuggestion
import com.example.formax.domain.models.*
import com.example.formax.presentation.components.RecoveryCheckDialog
import com.example.formax.presentation.components.RestTimerBanner
import com.example.formax.presentation.components.SetInputRow
import com.example.ui.theme.*
import java.util.UUID

@Composable
fun WorkoutScreen(
    activeSession: WorkoutSession?,
    workoutExercises: List<WorkoutExercise>,
    sessionSets: List<ExerciseSet>,
    allExercises: List<Exercise>,
    onSaveSet: (ExerciseSet) -> Unit,
    onReplaceExercise: (WorkoutExercise, Exercise) -> Unit,
    onFinishWorkout: (sessionId: String, notes: String) -> Unit,
    onStartNewSession: (TrainingDay, RecoveryCheck?) -> Unit,
    availableProgramDays: List<TrainingDay>,
    onOpenExerciseDetail: (String) -> Unit
) {
    var showRecoveryDialog by remember { mutableStateOf(false) }
    var selectedDayForNewWorkout by remember { mutableStateOf<TrainingDay?>(null) }
    var exerciseToReplace by remember { mutableStateOf<WorkoutExercise?>(null) }
    var activeRestSeconds by remember { mutableIntStateOf(120) }
    var isRestTimerActive by remember { mutableStateOf(false) }
    var workoutNotes by remember { mutableStateOf("") }

    if (showRecoveryDialog && selectedDayForNewWorkout != null) {
        RecoveryCheckDialog(
            onDismiss = {
                onStartNewSession(selectedDayForNewWorkout!!, null)
                showRecoveryDialog = false
            },
            onSubmit = { recovery ->
                onStartNewSession(selectedDayForNewWorkout!!, recovery)
                showRecoveryDialog = false
            }
        )
    }

    if (exerciseToReplace != null) {
        val baseEx = allExercises.find { it.id == exerciseToReplace!!.exerciseId }
        val rankedAlternatives = remember(baseEx, allExercises) {
            if (baseEx != null) {
                AlternativesEngine.rankAlternatives(baseEx, allExercises, baseEx.alternativeExerciseIds)
            } else emptyList()
        }

        AlertDialog(
            onDismissRequest = { exerciseToReplace = null },
            title = {
                Column {
                    Text(
                        text = "استبدال التمرين",
                        color = ForMaxElectricGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "اختر تمريناً بديلاً",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                LazyColumn(modifier = Modifier.fillMaxWidth().heightIn(max = 340.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        Text(
                            text = "يظل السجل التدريبي متصلاً تلقائياً مع حسابات التطور والحمل التراكمي.",
                            color = ForMaxTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    items(rankedAlternatives) { (alt, score) ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, ForMaxCardBorder, RoundedCornerShape(10.dp))
                                .clickable {
                                    onReplaceExercise(exerciseToReplace!!, alt)
                                    exerciseToReplace = null
                                },
                            color = ForMaxSurfaceElevated
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = alt.name,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "${alt.equipment.displayNameAr} • ${alt.exerciseType.displayNameAr}",
                                        color = ForMaxTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                                Surface(
                                    color = ForMaxElectricGreen.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "تطابق $score%",
                                        color = ForMaxElectricGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { exerciseToReplace = null }) {
                    Text("إلغاء", color = ForMaxTextSecondary)
                }
            },
            containerColor = ForMaxSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ForMaxBackground)
    ) {
        if (activeSession == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "جلسات التمرين",
                    color = ForMaxElectricGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "اختر يوم التمرين",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(availableProgramDays) { day ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp))
                                .clickable {
                                    if (!day.isRestDay) {
                                        selectedDayForNewWorkout = day
                                        showRecoveryDialog = true
                                    }
                                },
                            colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "اليوم ${day.dayNumber}",
                                        color = ForMaxElectricGreen,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (day.isRestDay) {
                                        Surface(
                                            color = ForMaxSurfaceElevated,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "راحة",
                                                color = ForMaxTextSecondary,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = day.dayName,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )

                                Text(
                                    text = day.focus,
                                    color = ForMaxTextSecondary,
                                    fontSize = 12.sp
                                )

                                if (!day.isRestDay) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${day.exercises.size} تمارين مبرمجة",
                                            color = Color(0xFFCFD8DC),
                                            fontSize = 11.sp
                                        )
                                        Text(
                                            text = "ابدأ الجلسة ←",
                                            color = ForMaxElectricGreen,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // Header with Finish Workout CTA
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(ForMaxElectricGreen)
                            )
                            Text(
                                text = "تمرين مباشر",
                                color = ForMaxElectricGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = activeSession.dayName,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Button(
                        onClick = { onFinishWorkout(activeSession.id, workoutNotes) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForMaxElectricGreen,
                            contentColor = Color(0xFF0B0D0F)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("finish_workout_btn")
                    ) {
                        Text("إنهاء التمرين", fontWeight = FontWeight.Black, fontSize = 13.sp)
                    }
                }

                RestTimerBanner(
                    initialSeconds = activeRestSeconds,
                    isActive = isRestTimerActive,
                    onDismiss = { isRestTimerActive = false }
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(workoutExercises) { we ->
                        val ex = allExercises.find { it.id == we.exerciseId }
                        val exSets = sessionSets.filter { it.workoutExerciseId == we.id }
                        val isCompound = ex?.exerciseType == ExerciseType.COMPOUND

                        val suggestion = remember(exSets, ex) {
                            if (ex != null) {
                                ProgressionEngine.evaluateProgression(
                                    currentSets = exSets,
                                    previousSessionSets = null,
                                    targetMinReps = we.minReps,
                                    targetMaxReps = we.maxReps,
                                    targetRir = ex.rirTarget
                                )
                            } else ProgressionSuggestion.FirstTimeGuidance()
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, ForMaxCardBorder, RoundedCornerShape(18.dp)),
                            colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { onOpenExerciseDetail(we.exerciseId) }
                                    ) {
                                        Text(
                                            text = we.exerciseName,
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "المستهدف: ${we.targetSets} جولات × ${we.minReps}–${we.maxReps} تكرار • RIR: ${ex?.rirTarget ?: 2}",
                                            color = ForMaxTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        IconButton(
                                            onClick = { exerciseToReplace = we },
                                            modifier = Modifier.size(34.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.SwapHoriz,
                                                contentDescription = "استبدال التمرين",
                                                tint = ForMaxElectricGreen,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }

                                        IconButton(
                                            onClick = { onOpenExerciseDetail(we.exerciseId) },
                                            modifier = Modifier.size(34.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = "دليل التمرين",
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }

                                // Last Workout Reference
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(ForMaxSurfaceElevated, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "التمرين السابق:",
                                        color = ForMaxTextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (ex?.lastPerformedWeightKg != null && ex.lastPerformedWeightKg > 0) {
                                        Text(
                                            text = "${ex.lastPerformedWeightKg} كجم × ${ex.lastPerformedReps ?: 10} تكرار",
                                            color = ForMaxElectricGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    } else {
                                        Text(
                                            text = "اختر وزناً مريحاً يتيح لك أداء الحركة بتحكم كامل.",
                                            color = ForMaxTextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                // Progression Advice in Arabic
                                when (suggestion) {
                                    is ProgressionSuggestion.IncreaseLoad -> {
                                        Surface(
                                            color = ForMaxElectricGreen.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = ForMaxElectricGreen, modifier = Modifier.size(14.dp))
                                                Text(text = suggestion.message, color = ForMaxElectricGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    is ProgressionSuggestion.DeloadOrReviewRecovery -> {
                                        Surface(
                                            color = ForMaxWarning.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(Icons.Default.Warning, contentDescription = null, tint = ForMaxWarning, modifier = Modifier.size(14.dp))
                                                Text(text = suggestion.message, color = ForMaxWarning, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                    else -> {}
                                }

                                val setsCount = maxOf(we.targetSets, exSets.size)
                                (1..setsCount).forEach { setNum ->
                                    val existingSet = exSets.find { it.setNumber == setNum }
                                    SetInputRow(
                                        setNumber = setNum,
                                        initialWeight = existingSet?.weightKg ?: (ex?.lastPerformedWeightKg ?: 0.0),
                                        initialReps = existingSet?.reps ?: we.minReps,
                                        initialRir = existingSet?.rir ?: (ex?.rirTarget ?: 2),
                                        lastSessionSet = if (ex?.lastPerformedWeightKg != null) {
                                            ExerciseSet(
                                                id = "last",
                                                workoutExerciseId = we.id,
                                                sessionId = "prev",
                                                exerciseId = we.exerciseId,
                                                setNumber = setNum,
                                                weightKg = ex.lastPerformedWeightKg,
                                                reps = ex.lastPerformedReps ?: 10
                                            )
                                        } else null,
                                        isCompleted = existingSet?.isCompleted == true,
                                        onSaveSet = { w, r, rir ->
                                            val set = ExerciseSet(
                                                id = existingSet?.id ?: UUID.randomUUID().toString(),
                                                workoutExerciseId = we.id,
                                                sessionId = activeSession.id,
                                                exerciseId = we.exerciseId,
                                                setNumber = setNum,
                                                weightKg = w,
                                                reps = r,
                                                rir = rir,
                                                isCompleted = true
                                            )
                                            onSaveSet(set)
                                            activeRestSeconds = if (isCompound) 150 else 75
                                            isRestTimerActive = true
                                        }
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        val newSetNum = setsCount + 1
                                        val set = ExerciseSet(
                                            id = UUID.randomUUID().toString(),
                                            workoutExerciseId = we.id,
                                            sessionId = activeSession.id,
                                            exerciseId = we.exerciseId,
                                            setNumber = newSetNum,
                                            weightKg = ex?.lastPerformedWeightKg ?: 0.0,
                                            reps = we.minReps,
                                            rir = ex?.rirTarget ?: 2,
                                            isCompleted = false
                                        )
                                        onSaveSet(set)
                                    },
                                    modifier = Modifier.fillMaxWidth().height(36.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = androidx.compose.ui.graphics.SolidColor(ForMaxCardBorder)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("+ إضافة جولة", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}
