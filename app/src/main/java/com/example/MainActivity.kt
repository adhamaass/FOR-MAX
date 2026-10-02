package com.example

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.example.formax.domain.models.*
import com.example.formax.presentation.MainViewModel
import com.example.formax.presentation.screens.*
import com.example.ui.theme.*
import java.util.Locale

enum class ForMaxBottomTab(val labelResId: Int, val icon: ImageVector) {
    HOME(R.string.nav_home, Icons.Default.Home),
    WORKOUT(R.string.nav_workout, Icons.Default.FitnessCenter),
    PROGRESS(R.string.nav_progress, Icons.Default.TrendingUp),
    EXERCISES(R.string.nav_exercises, Icons.Default.FormatListBulleted),
    PROFILE(R.string.nav_profile, Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
            val layoutDirection = if (currentLanguage.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

            val context = LocalContext.current
            val localizedContext = remember(currentLanguage) {
                val locale = Locale(currentLanguage.code)
                Locale.setDefault(locale)
                val config = Configuration(context.resources.configuration)
                config.setLocale(locale)
                config.setLayoutDirection(locale)
                context.createConfigurationContext(config)
            }

            CompositionLocalProvider(
                LocalContext provides localizedContext,
                LocalLayoutDirection provides layoutDirection
            ) {
                MyApplicationTheme {
                    ForMaxApp(viewModel = viewModel, currentLanguage = currentLanguage)
                }
            }
        }
    }
}

@Composable
fun ForMaxApp(
    viewModel: MainViewModel,
    currentLanguage: AppLanguage
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val currentUserRole by viewModel.currentUserRole.collectAsStateWithLifecycle()
    val activeSession by viewModel.activeSession.collectAsStateWithLifecycle()
    val sessionExercises by viewModel.sessionExercises.collectAsStateWithLifecycle()
    val sessionSets by viewModel.sessionSets.collectAsStateWithLifecycle()
    val publishedExercises by viewModel.publishedExercises.collectAsStateWithLifecycle()
    val allExercisesForAdmin by viewModel.allExercisesForAdmin.collectAsStateWithLifecycle()
    val allMedia by viewModel.allMedia.collectAsStateWithLifecycle()
    val allPrograms by viewModel.allPrograms.collectAsStateWithLifecycle()
    val currentProgramDays by viewModel.currentProgramDays.collectAsStateWithLifecycle()
    val completedSessions by viewModel.completedSessions.collectAsStateWithLifecycle()
    val personalRecords by viewModel.personalRecords.collectAsStateWithLifecycle()
    val bodyMeasurements by viewModel.bodyMeasurements.collectAsStateWithLifecycle()
    val progressPhotos by viewModel.progressPhotos.collectAsStateWithLifecycle()
    val cardioSessions by viewModel.cardioSessions.collectAsStateWithLifecycle()
    val weeklyReport by viewModel.weeklyReport.collectAsStateWithLifecycle()
    val adminMetrics by viewModel.adminMetrics.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(ForMaxBottomTab.HOME) }
    var selectedExerciseIdForDetail by remember { mutableStateOf<String?>(null) }
    var isInAdminPanel by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // First Launch / Onboarding Check
    if (!userProfile.isOnboarded) {
        OnboardingScreen(
            onCompleteOnboarding = { newProfile ->
                viewModel.completeOnboarding(newProfile)
            }
        )
        return
    }

    // Detail Screen Navigation Handling
    if (selectedExerciseIdForDetail != null) {
        val exercise = allExercisesForAdmin.find { it.id == selectedExerciseIdForDetail }
        if (exercise != null) {
            val exMedia = allMedia.filter { it.exerciseId == exercise.id }
            val exSets = sessionSets.filter { it.exerciseId == exercise.id }

            BackHandler {
                selectedExerciseIdForDetail = null
            }

            ExerciseDetailScreen(
                exercise = exercise,
                mediaList = exMedia,
                allExercises = publishedExercises,
                exerciseSetsHistory = exSets,
                onBack = { selectedExerciseIdForDetail = null },
                onToggleFavorite = { viewModel.toggleFavorite(exercise) },
                onSelectAlternative = { alt ->
                    selectedExerciseIdForDetail = alt.id
                },
                onStartWorkoutWithExercise = { ex ->
                    selectedExerciseIdForDetail = null
                    currentTab = ForMaxBottomTab.WORKOUT
                    viewModel.startWorkoutWithSpecificExercise(ex)
                }
            )
            return
        }
    }

    // Admin Panel Navigation Handling
    if (isInAdminPanel && currentUserRole == UserRole.ADMIN) {
        BackHandler {
            isInAdminPanel = false
        }

        AdminPanelScreen(
            exercises = allExercisesForAdmin,
            allMedia = allMedia,
            programs = allPrograms,
            adminMetrics = adminMetrics,
            onSaveExercise = { viewModel.adminSaveExercise(it) },
            onDeleteExercise = { viewModel.adminDeleteExercise(it) },
            onSaveMedia = { viewModel.adminSaveMedia(it) },
            onDeleteMedia = { viewModel.adminDeleteMedia(it) },
            onExitAdmin = { isInAdminPanel = false }
        )
        return
    }

    // Main App with Bottom Navigation Bar
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ForMaxBackground,
        bottomBar = {
            NavigationBar(
                containerColor = ForMaxSurface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .border(1.dp, ForMaxCardBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .testTag("bottom_nav_bar")
            ) {
                ForMaxBottomTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    val tabTitle = stringResource(tab.labelResId)
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tabTitle,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tabTitle,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Black else androidx.compose.ui.text.font.FontWeight.Medium
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF0B0D0F),
                            selectedTextColor = ForMaxElectricGreen,
                            indicatorColor = ForMaxElectricGreen,
                            unselectedIconColor = ForMaxTextSecondary,
                            unselectedTextColor = ForMaxTextSecondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                ForMaxBottomTab.HOME -> {
                    val todayDay = currentProgramDays.firstOrNull { !it.isRestDay } ?: currentProgramDays.firstOrNull()
                    DashboardScreen(
                        userProfile = userProfile,
                        activeWorkout = activeSession,
                        completedWorkouts = completedSessions,
                        todayProgramDay = todayDay,
                        personalRecords = personalRecords,
                        onStartWorkout = { day ->
                            currentTab = ForMaxBottomTab.WORKOUT
                            viewModel.startWorkout(day)
                        },
                        onResumeWorkout = {
                            currentTab = ForMaxBottomTab.WORKOUT
                        },
                        onViewExercise = { id ->
                            selectedExerciseIdForDetail = id
                        },
                        onNavigateToProgress = {
                            currentTab = ForMaxBottomTab.PROGRESS
                        }
                    )
                }
                ForMaxBottomTab.WORKOUT -> {
                    WorkoutScreen(
                        activeSession = activeSession,
                        workoutExercises = sessionExercises,
                        sessionSets = sessionSets,
                        allExercises = publishedExercises,
                        onSaveSet = { set -> viewModel.logSet(set) },
                        onReplaceExercise = { we, newEx -> viewModel.replaceExercise(we, newEx) },
                        onFinishWorkout = { sid, notes -> viewModel.finishWorkout(sid, notes) },
                        onStartNewSession = { day, recovery -> viewModel.startWorkout(day, recovery) },
                        availableProgramDays = currentProgramDays,
                        onOpenExerciseDetail = { id -> selectedExerciseIdForDetail = id }
                    )
                }
                ForMaxBottomTab.PROGRESS -> {
                    ProgressScreen(
                        weeklyReport = weeklyReport,
                        measurements = bodyMeasurements,
                        progressPhotos = progressPhotos,
                        cardioSessions = cardioSessions,
                        onAddMeasurement = { viewModel.saveMeasurement(it) },
                        onAddProgressPhoto = { viewModel.saveProgressPhoto(it) },
                        onAddCardioSession = { viewModel.saveCardioSession(it) }
                    )
                }
                ForMaxBottomTab.EXERCISES -> {
                    ExerciseLibraryScreen(
                        exercises = publishedExercises,
                        onSelectExercise = { ex ->
                            selectedExerciseIdForDetail = ex.id
                        },
                        onToggleFavorite = { ex ->
                            viewModel.toggleFavorite(ex)
                        }
                    )
                }
                ForMaxBottomTab.PROFILE -> {
                    ProfileScreen(
                        userProfile = userProfile,
                        allPrograms = allPrograms,
                        currentLanguage = currentLanguage,
                        currentUserRole = currentUserRole,
                        isDevelopmentEnvironment = viewModel.isDevelopmentEnvironment(),
                        onSelectLanguage = { viewModel.setLanguage(it) },
                        onResetApp = { viewModel.resetApp() },
                        onResetDemoData = { viewModel.resetDemoData() },
                        onRequestDevAdmin = {
                            coroutineScope.launch {
                                val result = viewModel.requestDevelopmentAdminAccess()
                                if (result is com.example.formax.domain.auth.AdminAuthResult.Authenticated) {
                                    isInAdminPanel = true
                                }
                            }
                        },
                        onAuthenticateRemoteToken = { token ->
                            coroutineScope.launch {
                                val result = viewModel.authenticateRemoteAdmin(token)
                                if (result is com.example.formax.domain.auth.AdminAuthResult.Authenticated) {
                                    isInAdminPanel = true
                                }
                            }
                        },
                        onOpenAdminPanel = { isInAdminPanel = true },
                        onExitAdmin = { viewModel.exitAdminMode() },
                        onSelectProgram = { viewModel.selectProgram(it) }
                    )
                }
            }
        }
    }
}
