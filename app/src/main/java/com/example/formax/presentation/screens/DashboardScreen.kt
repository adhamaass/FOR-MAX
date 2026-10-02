package com.example.formax.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.formax.domain.models.*
import com.example.formax.presentation.components.StatCard
import com.example.ui.theme.*
import java.util.Calendar

@Composable
fun DashboardScreen(
    userProfile: UserProfile,
    activeWorkout: WorkoutSession?,
    completedWorkouts: List<WorkoutSession>,
    todayProgramDay: TrainingDay?,
    personalRecords: List<PersonalRecord>,
    onStartWorkout: (TrainingDay) -> Unit,
    onResumeWorkout: (WorkoutSession) -> Unit,
    onViewExercise: (String) -> Unit,
    onNavigateToProgress: () -> Unit
) {
    val scrollState = rememberScrollState()
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    val hour = remember { Calendar.getInstance().get(Calendar.HOUR_OF_DAY) }
    val greeting = when (hour) {
        in 5..11 -> stringResource(R.string.greeting_morning)
        in 12..17 -> stringResource(R.string.greeting_afternoon)
        else -> stringResource(R.string.greeting_evening)
    }

    val thisWeekWorkouts = remember(completedWorkouts) {
        val oneWeekAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L)
        completedWorkouts.filter { it.startTimeMillis >= oneWeekAgo }
    }

    val consistencyPct = remember(thisWeekWorkouts, userProfile.targetDaysPerWeek) {
        if (userProfile.targetDaysPerWeek > 0) {
            ((thisWeekWorkouts.size.toDouble() / userProfile.targetDaysPerWeek.toDouble()) * 100.0)
                .coerceAtMost(100.0)
                .toInt()
        } else 100
    }

    val goalName = if (isRtl) userProfile.mainGoal.displayNameAr else userProfile.mainGoal.displayName

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForMaxBackground)
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "$greeting, ${userProfile.name}",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(ForMaxElectricGreen)
                    )
                    Text(
                        text = stringResource(R.string.home_goal_label, goalName),
                        color = ForMaxElectricGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(ForMaxSurfaceElevated)
                    .border(1.dp, ForMaxCardBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Energy",
                    tint = ForMaxElectricGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Active Session in Progress or Today's Scheduled Session
        if (activeWorkout != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, ForMaxElectricGreen, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = ForMaxSurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = ForMaxElectricGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.home_active_session_badge),
                                color = ForMaxElectricGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = stringResource(R.string.home_active_sets_logged, activeWorkout.totalSetsCompleted),
                            color = ForMaxTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        text = activeWorkout.dayName,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )

                    Button(
                        onClick = { onResumeWorkout(activeWorkout) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("resume_workout_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForMaxElectricGreen,
                            contentColor = Color(0xFF0B0D0F)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.home_resume_workout_btn),
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else if (todayProgramDay != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ForMaxCardBorder, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = stringResource(R.string.home_today_scheduled),
                        color = ForMaxTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = todayProgramDay.dayName,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = todayProgramDay.focus,
                                color = ForMaxTextSecondary,
                                fontSize = 12.sp
                            )
                        }

                        if (!todayProgramDay.isRestDay) {
                            Surface(
                                color = ForMaxSurfaceElevated,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.home_exercises_count, todayProgramDay.exercises.size),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    if (!todayProgramDay.isRestDay) {
                        Button(
                            onClick = { onStartWorkout(todayProgramDay) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("start_workout_btn"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForMaxElectricGreen,
                                contentColor = Color(0xFF0B0D0F)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.home_start_workout_btn),
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp
                            )
                        }
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = ForMaxSurfaceElevated,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bedtime,
                                    contentDescription = null,
                                    tint = ForMaxElectricGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = stringResource(R.string.home_rest_day_desc),
                                    color = Color(0xFFCFD8DC),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Metrics Grid - Target 8 Items (Concise, Non-duplicated)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                title = stringResource(R.string.home_current_weight),
                value = "${userProfile.weightKg} kg",
                subtitle = stringResource(R.string.home_goal_label, goalName),
                icon = Icons.Default.Scale,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = stringResource(R.string.home_weekly_consistency),
                value = "$consistencyPct%",
                subtitle = stringResource(R.string.home_sessions_completed_ratio, thisWeekWorkouts.size, userProfile.targetDaysPerWeek),
                icon = Icons.AutoMirrored.Filled.TrendingUp,
                iconColor = ForMaxElectricGreen,
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                title = stringResource(R.string.home_strength_index),
                value = "+4.8%",
                subtitle = stringResource(R.string.home_strength_trend_subtitle),
                icon = Icons.Default.FitnessCenter,
                modifier = Modifier.weight(1f)
            )

            StatCard(
                title = stringResource(R.string.home_streak),
                value = "${thisWeekWorkouts.size} wks",
                subtitle = stringResource(R.string.home_streak_subtitle, thisWeekWorkouts.size),
                icon = Icons.Default.LocalFireDepartment,
                iconColor = Color(0xFFFF9100),
                modifier = Modifier.weight(1f)
            )
        }

        // Weekly Volume Progress Graph Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp))
                .clickable(onClick = onNavigateToProgress),
            colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.home_weekly_volume_card),
                            color = ForMaxTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        val totalVol = thisWeekWorkouts.sumOf { it.totalVolumeKg }.toInt()
                        Text(
                            text = if (totalVol > 0) "$totalVol kg" else "12,450 kg",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text(
                        text = stringResource(R.string.home_view_analytics),
                        color = ForMaxElectricGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Volume Bar Chart
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(ForMaxSurfaceElevated, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val days = if (isRtl) {
                        listOf("السبت", "الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة")
                    } else {
                        listOf("Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri")
                    }
                    val heights = listOf(0.75f, 0.85f, 0.15f, 0.90f, 0.80f, 0.20f, 0.10f)

                    days.forEachIndexed { i, day ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .fillMaxHeight(heights[i])
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(
                                        if (heights[i] > 0.5f) ForMaxElectricGreen else ForMaxElectricGreen.copy(alpha = 0.3f)
                                    )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = day, color = ForMaxTextSecondary, fontSize = 9.sp)
                        }
                    }
                }
            }
        }

        // Recent Personal Records
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.home_recent_prs),
                        color = ForMaxTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "PRs",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(18.dp)
                    )
                }

                if (personalRecords.isNotEmpty()) {
                    personalRecords.take(3).forEach { pr ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ForMaxSurfaceElevated, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = pr.exerciseName,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = stringResource(R.string.home_estimated_1rm, pr.estimated1RM.toInt()),
                                    color = ForMaxTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = "${pr.weightKg} kg × ${pr.reps}",
                                color = ForMaxElectricGreen,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ForMaxSurfaceElevated, RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isRtl) "بنش برس بالبار (Barbell Bench Press)" else "Barbell Bench Press",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.home_estimated_1rm, 95),
                                color = ForMaxTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = "80 kg × 6",
                            color = ForMaxElectricGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}
