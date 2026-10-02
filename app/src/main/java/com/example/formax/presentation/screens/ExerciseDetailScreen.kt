package com.example.formax.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.example.formax.domain.models.*
import com.example.formax.presentation.components.ForMaxMediaSection
import com.example.ui.theme.*

@Composable
fun ExerciseDetailScreen(
    exercise: Exercise,
    mediaList: List<ExerciseMedia>,
    allExercises: List<Exercise>,
    exerciseSetsHistory: List<ExerciseSet>,
    onBack: () -> Unit,
    onToggleFavorite: (Boolean) -> Unit,
    onSelectAlternative: (Exercise) -> Unit,
    onStartWorkoutWithExercise: (Exercise) -> Unit
) {
    val scrollState = rememberScrollState()

    val rankedAlternatives = remember(exercise, allExercises) {
        AlternativesEngine.rankAlternatives(exercise, allExercises, exercise.alternativeExerciseIds)
    }

    Scaffold(
        containerColor = ForMaxBackground,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(38.dp)
                        .background(ForMaxSurfaceElevated, CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "رجوع", tint = Color.White)
                }

                Text(
                    text = exercise.primaryMuscle.displayNameAr,
                    color = ForMaxElectricGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )

                IconButton(
                    onClick = { onToggleFavorite(exercise.isFavorite) },
                    modifier = Modifier
                        .size(38.dp)
                        .background(ForMaxSurfaceElevated, CircleShape)
                ) {
                    Icon(
                        imageVector = if (exercise.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "المفضلة",
                        tint = if (exercise.isFavorite) ForMaxElectricGreen else Color.White
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = ForMaxSurface,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Button(
                        onClick = { onStartWorkoutWithExercise(exercise) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("start_workout_with_exercise_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForMaxElectricGreen,
                            contentColor = Color(0xFF0B0D0F)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "ابدأ التمرين بهذه الحركة",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Exercise Name Header
            Column {
                Text(
                    text = exercise.name,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
                if (exercise.alternativeNames.isNotEmpty()) {
                    Text(
                        text = "معروف أيضاً باسم: ${exercise.alternativeNames.joinToString("، ")}",
                        color = ForMaxTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // Top Media Section (Video / GIF / Image / Biomechanics Fallback)
            ForMaxMediaSection(
                exercise = exercise,
                mediaList = mediaList
            )

            // Targets Grid (Sets, Reps, RIR, Rest)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TargetMetricColumn(label = "الجولات", value = "${exercise.recommendedSets}")
                    TargetMetricColumn(label = "التكرارات", value = "${exercise.minReps}–${exercise.maxReps}")
                    TargetMetricColumn(label = "معدل الجهد RIR", value = "${exercise.rirTarget}")
                    val restMin = exercise.restTimeSeconds / 60
                    TargetMetricColumn(label = "الراحة", value = "${restMin} د")
                }
            }

            // Anatomy & Equipment Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "تشريح العضلة ونمط الحركة",
                        color = ForMaxTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "العضلة الأساسية", color = ForMaxTextSecondary, fontSize = 13.sp)
                        Text(text = exercise.primaryMuscle.displayNameAr, color = ForMaxElectricGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    if (exercise.secondaryMuscles.isNotEmpty()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "العضلات المساعدة", color = ForMaxTextSecondary, fontSize = 13.sp)
                            Text(
                                text = exercise.secondaryMuscles.joinToString("، ") { it.displayNameAr },
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "نمط الحركة", color = ForMaxTextSecondary, fontSize = 13.sp)
                        Text(text = exercise.movementPattern.displayNameAr, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "المعدات المطلوبة", color = ForMaxTextSecondary, fontSize = 13.sp)
                        Text(text = exercise.equipment.displayNameAr, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "مستوى الصعوبة", color = ForMaxTextSecondary, fontSize = 13.sp)
                        Text(text = exercise.difficulty.displayNameAr, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // How to Perform
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "طريقة الأداء والخطوات الفنية",
                        color = ForMaxTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (exercise.setupInstructions.isNotBlank()) {
                        InstructionStep(number = "1", label = "وضعية البداية والثبات", detail = exercise.setupInstructions)
                    }
                    if (exercise.executionInstructions.isNotBlank()) {
                        InstructionStep(number = "2", label = "الأداء والتحكم الحركي", detail = exercise.executionInstructions)
                    }
                    if (exercise.breathingInstructions.isNotBlank()) {
                        InstructionStep(number = "3", label = "طريقة التنفس الصحيحة", detail = exercise.breathingInstructions)
                    }
                    if (exercise.instructions.isNotBlank() && exercise.setupInstructions.isBlank()) {
                        Text(text = exercise.instructions, color = Color(0xFFCFD8DC), fontSize = 13.sp, lineHeight = 20.sp)
                    }
                }
            }

            // Common Mistakes
            if (exercise.commonMistakes.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "أخطاء شائعة يجب تجنبها",
                            color = ForMaxError,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        exercise.commonMistakes.forEach { mistake ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(text = "❌", fontSize = 12.sp)
                                Text(text = mistake, color = Color(0xFFE2E8F0), fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            // Coaching Cues & Safety
            if (exercise.coachingCues.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "توجيهات فنية وتكنيك احترافي",
                            color = ForMaxElectricGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = exercise.coachingCues,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // Exercise History Over Weeks
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "سجل الأداء الشخصي",
                        color = ForMaxTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (exerciseSetsHistory.isNotEmpty()) {
                        val sessionGroups = exerciseSetsHistory.groupBy { it.sessionId }
                        sessionGroups.entries.take(4).forEachIndexed { index, entry ->
                            val maxSet = entry.value.maxByOrNull { it.weightKg }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ForMaxSurfaceElevated, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "جلسة ${sessionGroups.size - index}",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${maxSet?.weightKg ?: 0.0} كجم × ${maxSet?.reps ?: 0} تكرار",
                                    color = ForMaxElectricGreen,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "لا توجد جلسات مسجلة بعد. جلستك الأولى ستحدد نقطة البداية وقاعدة الأوزان.",
                            color = ForMaxTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Exercise Alternatives
            if (rankedAlternatives.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "التمارين البديلة المقترحة",
                            color = ForMaxTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        rankedAlternatives.take(4).forEach { (alt, score) ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onSelectAlternative(alt) },
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
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun TargetMetricColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = ForMaxTextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun InstructionStep(number: String, label: String, detail: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(ForMaxElectricGreen.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = number, color = ForMaxElectricGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Column {
            Text(text = label, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(text = detail, color = Color(0xFFCFD8DC), fontSize = 12.sp, lineHeight = 18.sp)
        }
    }
}
