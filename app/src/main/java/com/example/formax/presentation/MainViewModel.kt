package com.example.formax.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.formax.data.local.ForMaxDatabase
import com.example.formax.data.repository.AdminMetrics
import com.example.formax.data.repository.ForMaxRepository
import com.example.formax.domain.models.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = ForMaxDatabase.getInstance(application)
    private val repository = ForMaxRepository(database, viewModelScope)

    private val prefs = application.getSharedPreferences("formax_prefs", android.content.Context.MODE_PRIVATE)

    private val _currentLanguage = MutableStateFlow(
        AppLanguage.fromCode(prefs.getString("pref_language", "ar") ?: "ar")
    )
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val adminAuthService: com.example.formax.domain.auth.AdminAuthService =
        com.example.formax.domain.auth.DefaultAdminAuthService()

    private val _currentUserRole = MutableStateFlow(UserRole.USER)
    val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

    fun isDevelopmentEnvironment(): Boolean = adminAuthService.isDevelopmentEnvironment()

    suspend fun requestDevelopmentAdminAccess(): com.example.formax.domain.auth.AdminAuthResult {
        val result = adminAuthService.requestDevelopmentAccess()
        if (result is com.example.formax.domain.auth.AdminAuthResult.Authenticated) {
            _currentUserRole.value = result.role
        }
        return result
    }

    suspend fun authenticateRemoteAdmin(bearerToken: String): com.example.formax.domain.auth.AdminAuthResult {
        val result = adminAuthService.authenticateWithRemoteToken(bearerToken)
        if (result is com.example.formax.domain.auth.AdminAuthResult.Authenticated) {
            _currentUserRole.value = result.role
        }
        return result
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        prefs.edit().putString("pref_language", language.code).apply()
    }

    fun exitAdminMode() {
        _currentUserRole.value = UserRole.USER
    }

    val userProfile: StateFlow<UserProfile> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    val publishedExercises: StateFlow<List<Exercise>> = repository.getPublishedExercises()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allExercisesForAdmin: StateFlow<List<Exercise>> = repository.getAllExercisesForAdmin()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMedia: StateFlow<List<ExerciseMedia>> = repository.getAllMedia()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPrograms: StateFlow<List<TrainingProgram>> = repository.getAllPrograms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAlternatives: StateFlow<List<ExerciseAlternative>> = repository.getAllAlternatives()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeSession: StateFlow<WorkoutSession?> = repository.getActiveSession()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _currentSessionId = MutableStateFlow<String?>(null)

    val sessionExercises: StateFlow<List<WorkoutExercise>> = activeSession.flatMapLatest { session ->
        if (session != null) {
            repository.getExercisesForSession(session.id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sessionSets: StateFlow<List<ExerciseSet>> = activeSession.flatMapLatest { session ->
        if (session != null) {
            repository.getSetsForSession(session.id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedSessions: StateFlow<List<WorkoutSession>> = repository.getCompletedSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val personalRecords: StateFlow<List<PersonalRecord>> = repository.getAllPersonalRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bodyMeasurements: StateFlow<List<BodyMeasurement>> = repository.getBodyMeasurements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progressPhotos: StateFlow<List<ProgressPhoto>> = repository.getProgressPhotos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cardioSessions: StateFlow<List<CardioSession>> = repository.getCardioSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _weeklyReport = MutableStateFlow(
        WeeklyReport(
            weekStartDateMillis = System.currentTimeMillis() - 7 * 86400000L,
            weekEndDateMillis = System.currentTimeMillis(),
            workoutsCompleted = 0,
            targetWorkouts = 4,
            consistencyPercentage = 0.0,
            totalVolumeKg = 0.0,
            muscleVolumeBreakdown = emptyMap(),
            prsAchieved = 0,
            improvedExercises = emptyList(),
            stableExercises = emptyList(),
            decliningExercises = emptyList()
        )
    )
    val weeklyReport: StateFlow<WeeklyReport> = _weeklyReport.asStateFlow()

    private val _adminMetrics = MutableStateFlow(
        AdminMetrics(0, 0, 0, 0, 0, 0, 0, 0)
    )
    val adminMetrics: StateFlow<AdminMetrics> = _adminMetrics.asStateFlow()

    val currentProgramDays: StateFlow<List<TrainingDay>> = userProfile.flatMapLatest { profile ->
        repository.getDaysForProgram(profile.selectedProgramId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refreshWeeklyReport()
        refreshAdminMetrics()
    }

    fun completeOnboarding(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveUserProfile(profile)
            refreshWeeklyReport()
        }
    }

    fun selectProgram(program: TrainingProgram) {
        viewModelScope.launch {
            val current = userProfile.value
            repository.saveUserProfile(current.copy(selectedProgramId = program.id))
        }
    }

    fun toggleFavorite(exercise: Exercise) {
        viewModelScope.launch {
            repository.toggleFavorite(exercise.id, exercise.isFavorite)
        }
    }

    fun startWorkout(day: TrainingDay, recovery: RecoveryCheck? = null) {
        viewModelScope.launch {
            val profile = userProfile.value
            val allEx = publishedExercises.value
            val prog = allPrograms.value.find { it.id == profile.selectedProgramId }
            repository.startWorkout(
                programId = profile.selectedProgramId,
                dayId = day.id,
                programName = prog?.name ?: "FOR MAX Program",
                dayName = day.dayName,
                programExercises = day.exercises,
                allExercises = allEx,
                recoveryCheck = recovery
            )
        }
    }

    fun startWorkoutWithSpecificExercise(exercise: Exercise) {
        viewModelScope.launch {
            val profile = userProfile.value
            val prog = allPrograms.value.find { it.id == profile.selectedProgramId }
            val singlePE = ProgramExercise(
                exerciseId = exercise.id,
                orderIndex = 0,
                targetSets = exercise.recommendedSets,
                minReps = exercise.minReps,
                maxReps = exercise.maxReps,
                rirTarget = exercise.rirTarget,
                restTimeSeconds = exercise.restTimeSeconds
            )
            repository.startWorkout(
                programId = profile.selectedProgramId,
                dayId = "custom_session",
                programName = prog?.name ?: "FOR MAX Program",
                dayName = "Target: ${exercise.name}",
                programExercises = listOf(singlePE),
                allExercises = publishedExercises.value
            )
        }
    }

    fun logSet(set: ExerciseSet) {
        viewModelScope.launch {
            repository.logSet(set)
        }
    }

    fun replaceExercise(workoutExercise: WorkoutExercise, newExercise: Exercise) {
        viewModelScope.launch {
            repository.replaceExerciseInSession(workoutExercise, newExercise)
        }
    }

    fun finishWorkout(sessionId: String, notes: String = "") {
        viewModelScope.launch {
            repository.finishWorkout(sessionId, notes)
            refreshWeeklyReport()
            refreshAdminMetrics()
        }
    }

    fun saveMeasurement(measurement: BodyMeasurement) {
        viewModelScope.launch {
            repository.saveBodyMeasurement(measurement)
        }
    }

    fun saveProgressPhoto(photo: ProgressPhoto) {
        viewModelScope.launch {
            repository.saveProgressPhoto(photo)
        }
    }

    fun saveCardioSession(session: CardioSession) {
        viewModelScope.launch {
            repository.saveCardioSession(session)
        }
    }

    // Reset App (Production Reset - wipes all user generated training data and returns to onboarding)
    fun resetApp(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.resetAppUserData()
            refreshWeeklyReport()
            refreshAdminMetrics()
            onComplete()
        }
    }

    // Development Reset (Recreates demo workouts and PRs for testing)
    fun resetDemoData(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            repository.seedDemoWorkoutData()
            refreshWeeklyReport()
            refreshAdminMetrics()
            onComplete()
        }
    }

    // Admin Operations
    fun adminSaveExercise(exercise: Exercise) {
        viewModelScope.launch {
            repository.saveExercise(exercise)
            refreshAdminMetrics()
        }
    }

    fun adminDuplicateExercise(source: Exercise) {
        viewModelScope.launch {
            val duplicate = source.copy(
                id = "ex_${java.util.UUID.randomUUID().toString().take(8)}",
                name = "${source.name} (Copy)",
                status = ContentStatus.DRAFT,
                isFavorite = false,
                viewsCount = 0,
                completionCount = 0,
                replacementCount = 0,
                lastPerformedWeightKg = null,
                lastPerformedReps = null,
                lastPerformedDateMillis = null
            )
            repository.saveExercise(duplicate)
            refreshAdminMetrics()
        }
    }

    fun adminDeleteExercise(exerciseId: String) {
        viewModelScope.launch {
            repository.deleteExercise(exerciseId)
            refreshAdminMetrics()
        }
    }

    fun adminSaveMedia(media: ExerciseMedia) {
        viewModelScope.launch {
            repository.saveMedia(media)
            refreshAdminMetrics()
        }
    }

    fun adminDeleteMedia(mediaId: String) {
        viewModelScope.launch {
            repository.deleteMedia(mediaId)
            refreshAdminMetrics()
        }
    }

    fun adminUpdateExerciseStatus(exercise: Exercise, newStatus: ContentStatus) {
        viewModelScope.launch {
            repository.saveExercise(exercise.copy(status = newStatus))
            refreshAdminMetrics()
        }
    }

    fun adminSaveProgram(program: TrainingProgram) {
        viewModelScope.launch {
            repository.saveProgram(program)
            refreshAdminMetrics()
        }
    }

    fun adminDeleteProgram(programId: String) {
        viewModelScope.launch {
            repository.deleteProgram(programId)
            refreshAdminMetrics()
        }
    }

    fun adminSaveAlternative(alternative: ExerciseAlternative) {
        viewModelScope.launch {
            repository.saveAlternative(alternative)
            refreshAdminMetrics()
        }
    }

    fun adminDeleteAlternative(alternativeId: String) {
        viewModelScope.launch {
            repository.deleteAlternative(alternativeId)
            refreshAdminMetrics()
        }
    }

    fun refreshWeeklyReport() {
        viewModelScope.launch {
            val report = repository.generateWeeklyReport(userProfile.value.targetDaysPerWeek)
            _weeklyReport.value = report
        }
    }

    fun refreshAdminMetrics() {
        viewModelScope.launch {
            val metrics = repository.getAdminMetrics()
            _adminMetrics.value = metrics
        }
    }
}
