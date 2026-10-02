package com.example.formax.data.repository

import androidx.room.withTransaction
import com.example.formax.data.local.*
import com.example.formax.domain.engine.ProgressionEngine
import com.example.formax.domain.models.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ForMaxRepository(
    private val database: ForMaxDatabase,
    private val scope: CoroutineScope? = null
) {
    private val userDao = database.userDao()
    private val exerciseDao = database.exerciseDao()
    private val mediaDao = database.mediaDao()
    private val alternativeDao = database.alternativeDao()
    private val programDao = database.programDao()
    private val workoutDao = database.workoutDao()
    private val progressDao = database.progressDao()
    private val cardioDao = database.cardioDao()

    init {
        scope?.launch {
            ensureSeeded()
        }
    }

    suspend fun ensureSeeded() = withContext(Dispatchers.IO) {
        val totalExercises = exerciseDao.getTotalExerciseCount()
        if (totalExercises == 0) {
            val initialExercises = ExerciseSeedData.getInitialExercises()
            exerciseDao.insertExercises(initialExercises)

            val initialMedia = ExerciseSeedData.getInitialMedia()
            mediaDao.insertAllMedia(initialMedia)

            val initialPrograms = ProgramSeedData.getInitialPrograms()
            programDao.insertPrograms(initialPrograms)

            val initialDays = ProgramSeedData.getInitialDays()
            programDao.insertDays(initialDays)

            // Seed user profile if missing
            val existingUser = userDao.getUserProfileOnce()
            if (existingUser == null) {
                userDao.insertOrUpdateProfile(
                    UserProfileEntity(
                        id = "default_user",
                        name = "Athlete",
                        age = 26,
                        sex = Sex.MALE.name,
                        heightCm = 178.0,
                        weightKg = 78.5,
                        experience = TrainingExperience.INTERMEDIATE.name,
                        mainGoal = FitnessGoal.FAT_LOSS_AND_MUSCLE_GAIN.name,
                        equipment = EquipmentAvailable.FULL_GYM.name,
                        targetDaysPerWeek = 4,
                        selectedProgramId = "upper_lower_4day",
                        isOnboarded = false,
                        isAdminMode = false,
                        createdAtMillis = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    // ==========================================
    // USER PROFILE
    // ==========================================
    fun getUserProfile(): Flow<UserProfile> {
        return userDao.getUserProfile().map { entity ->
            entity?.toDomain() ?: UserProfile()
        }
    }

    suspend fun saveUserProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        userDao.insertOrUpdateProfile(profile.toEntity())
    }

    // ==========================================
    // EXERCISES
    // ==========================================
    fun getPublishedExercises(): Flow<List<Exercise>> {
        return exerciseDao.getPublishedExercises().map { list -> list.map { it.toDomain() } }
    }

    fun getAllExercisesForAdmin(): Flow<List<Exercise>> {
        return exerciseDao.getAllExercises().map { list -> list.map { it.toDomain() } }
    }

    fun getExerciseById(id: String): Flow<Exercise?> {
        return exerciseDao.getExerciseById(id).map { it?.toDomain() }
    }

    suspend fun getExerciseByIdOnce(id: String): Exercise? = withContext(Dispatchers.IO) {
        exerciseDao.getExerciseByIdOnce(id)?.toDomain()
    }

    suspend fun saveExercise(exercise: Exercise) = withContext(Dispatchers.IO) {
        exerciseDao.insertExercise(exercise.toEntity())
    }

    suspend fun deleteExercise(id: String) = withContext(Dispatchers.IO) {
        exerciseDao.deleteExerciseById(id)
    }

    suspend fun toggleFavorite(exerciseId: String, current: Boolean) = withContext(Dispatchers.IO) {
        exerciseDao.setFavorite(exerciseId, !current)
    }

    suspend fun recordExerciseView(exerciseId: String) = withContext(Dispatchers.IO) {
        exerciseDao.incrementViewCount(exerciseId)
    }

    // ==========================================
    // MEDIA
    // ==========================================
    fun getMediaForExercise(exerciseId: String): Flow<List<ExerciseMedia>> {
        return mediaDao.getMediaForExercise(exerciseId).map { list -> list.map { it.toDomain() } }
    }

    suspend fun saveMedia(media: ExerciseMedia) = withContext(Dispatchers.IO) {
        if (media.isPrimary) {
            mediaDao.clearPrimaryForExerciseType(media.exerciseId, media.type.name)
        }
        mediaDao.insertMedia(media.toEntity())
    }

    suspend fun deleteMedia(mediaId: String) = withContext(Dispatchers.IO) {
        mediaDao.deleteMediaById(mediaId)
    }

    fun getAllMedia(): Flow<List<ExerciseMedia>> {
        return mediaDao.getAllMedia().map { list -> list.map { it.toDomain() } }
    }

    // ==========================================
    // ALTERNATIVES
    // ==========================================
    fun getAlternativesForExercise(exerciseId: String): Flow<List<ExerciseAlternative>> {
        return alternativeDao.getAlternativesForExercise(exerciseId).map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getAllAlternatives(): Flow<List<ExerciseAlternative>> {
        return alternativeDao.getAllAlternatives().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun saveAlternative(alternative: ExerciseAlternative) = withContext(Dispatchers.IO) {
        alternativeDao.insertAlternative(alternative.toEntity())
    }

    suspend fun deleteAlternative(id: String) = withContext(Dispatchers.IO) {
        alternativeDao.deleteAlternativeById(id)
    }

    // ==========================================
    // TRAINING PROGRAMS
    // ==========================================
    fun getAllPrograms(): Flow<List<TrainingProgram>> {
        return programDao.getAllPrograms().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getProgramDays(programId: String): List<TrainingDay> = withContext(Dispatchers.IO) {
        programDao.getDaysForProgramOnce(programId).map { it.toDomain() }
    }

    fun getDaysForProgram(programId: String): Flow<List<TrainingDay>> {
        return programDao.getDaysForProgram(programId).map { list -> list.map { it.toDomain() } }
    }

    suspend fun saveProgram(program: TrainingProgram) = withContext(Dispatchers.IO) {
        programDao.insertProgram(program.toEntity())
    }

    suspend fun deleteProgram(programId: String) = withContext(Dispatchers.IO) {
        programDao.deleteProgramById(programId)
    }

    suspend fun updateProgramStatus(programId: String, status: ContentStatus) = withContext(Dispatchers.IO) {
        programDao.updateProgramStatus(programId, status.name)
    }

    // ==========================================
    // WORKOUT LOGGER
    // ==========================================
    fun getActiveSession(): Flow<WorkoutSession?> {
        return workoutDao.getActiveSession().map { it?.toDomain() }
    }

    fun getExercisesForSession(sessionId: String): Flow<List<WorkoutExercise>> {
        return workoutDao.getExercisesForSession(sessionId).map { list -> list.map { it.toDomain() } }
    }

    fun getSetsForSession(sessionId: String): Flow<List<ExerciseSet>> {
        return workoutDao.getSetsForSession(sessionId).map { list -> list.map { it.toDomain() } }
    }

    fun getSetsForExercise(exerciseId: String): Flow<List<ExerciseSet>> {
        return workoutDao.getAllSetsForExercise(exerciseId).map { list -> list.map { it.toDomain() } }
    }

    suspend fun getPreviousSetsForExercise(exerciseId: String): List<ExerciseSet> = withContext(Dispatchers.IO) {
        workoutDao.getAllSetsForExerciseOnce(exerciseId).map { it.toDomain() }
    }

    suspend fun startWorkout(
        programId: String,
        dayId: String,
        programName: String,
        dayName: String,
        programExercises: List<ProgramExercise>,
        allExercises: List<Exercise>,
        recoveryCheck: RecoveryCheck? = null
    ): String = withContext(Dispatchers.IO) {
        val sessionId = UUID.randomUUID().toString()
        val session = WorkoutSessionEntity(
            id = sessionId,
            programId = programId,
            programDayId = dayId,
            programName = programName,
            dayName = dayName,
            startTimeMillis = System.currentTimeMillis(),
            endTimeMillis = null,
            isCompleted = false,
            totalVolumeKg = 0.0,
            totalSetsCompleted = 0,
            totalRepsCompleted = 0,
            durationSeconds = 0,
            notes = "",
            recoveryEnergy = recoveryCheck?.energy,
            recoverySleep = recoveryCheck?.sleep,
            recoverySoreness = recoveryCheck?.soreness,
            recoveryStress = recoveryCheck?.stress,
            recoveryNotes = recoveryCheck?.notes
        )
        workoutDao.insertSession(session)

        val workoutExercises = programExercises.mapIndexed { index, pe ->
            val ex = allExercises.find { it.id == pe.exerciseId }
            WorkoutExerciseEntity(
                id = UUID.randomUUID().toString(),
                sessionId = sessionId,
                exerciseId = pe.exerciseId,
                exerciseName = ex?.name ?: "Exercise",
                orderIndex = index,
                targetSets = pe.targetSets,
                minReps = pe.minReps,
                maxReps = pe.maxReps,
                originalExerciseId = null,
                isCompleted = false,
                notes = pe.notes
            )
        }
        workoutDao.insertWorkoutExercises(workoutExercises)
        sessionId
    }

    suspend fun logSet(set: ExerciseSet) = withContext(Dispatchers.IO) {
        workoutDao.insertSet(set.toEntity())
    }

    suspend fun deleteSet(setId: String) = withContext(Dispatchers.IO) {
        workoutDao.deleteSetById(setId)
    }

    suspend fun replaceWorkoutExercise(
        workoutExerciseId: String,
        newExercise: Exercise
    ) = withContext(Dispatchers.IO) {
        val currentList = workoutDao.getExercisesForSessionOnce(workoutExerciseId)
        // Find existing
        // Search across all exercises in session
    }

    suspend fun replaceExerciseInSession(
        workoutExercise: WorkoutExercise,
        newExercise: Exercise
    ) = withContext(Dispatchers.IO) {
        val updated = workoutExercise.copy(
            exerciseId = newExercise.id,
            exerciseName = newExercise.name,
            originalExerciseId = workoutExercise.originalExerciseId ?: workoutExercise.exerciseId,
            targetSets = newExercise.recommendedSets,
            minReps = newExercise.minReps,
            maxReps = newExercise.maxReps
        )
        workoutDao.updateWorkoutExercise(updated.toEntity())
        exerciseDao.incrementReplacementCount(workoutExercise.exerciseId)
    }

    suspend fun finishWorkout(
        sessionId: String,
        notes: String = ""
    ) = withContext(Dispatchers.IO) {
        val session = workoutDao.getSessionById(sessionId) ?: return@withContext
        val sets = workoutDao.getSetsForSession(sessionId).firstOrNull() ?: emptyList()
        val completedSets = sets.filter { it.isCompleted }

        val totalVolume = completedSets.sumOf { it.weightKg * it.reps }
        val totalReps = completedSets.sumOf { it.reps }
        val durationSeconds = (System.currentTimeMillis() - session.startTimeMillis) / 1000

        val updatedSession = session.copy(
            isCompleted = true,
            endTimeMillis = System.currentTimeMillis(),
            totalVolumeKg = totalVolume,
            totalSetsCompleted = completedSets.size,
            totalRepsCompleted = totalReps,
            durationSeconds = durationSeconds,
            notes = notes
        )
        workoutDao.updateSession(updatedSession)

        // Check and record PRs and update last performed on exercises
        val setsByExercise = completedSets.groupBy { it.exerciseId }
        for ((exId, exSets) in setsByExercise) {
            val maxWeightSet = exSets.maxByOrNull { it.weightKg }
            if (maxWeightSet != null) {
                exerciseDao.recordCompletion(
                    id = exId,
                    weight = maxWeightSet.weightKg,
                    reps = maxWeightSet.reps,
                    timestamp = System.currentTimeMillis()
                )

                val bestPr = progressDao.getBestPersonalRecordForExercise(exId)
                val e1rm = ProgressionEngine.calculateEstimated1RM(maxWeightSet.weightKg, maxWeightSet.reps)

                if (bestPr == null || e1rm > bestPr.estimated1RM) {
                    val exEntity = exerciseDao.getExerciseByIdOnce(exId)
                    progressDao.insertPersonalRecord(
                        PersonalRecordEntity(
                            id = UUID.randomUUID().toString(),
                            exerciseId = exId,
                            exerciseName = exEntity?.name ?: "Exercise",
                            weightKg = maxWeightSet.weightKg,
                            reps = maxWeightSet.reps,
                            estimated1RM = e1rm,
                            achievedAtMillis = System.currentTimeMillis(),
                            previousBestWeightKg = bestPr?.weightKg ?: 0.0
                        )
                    )
                }
            }
        }
    }

    // ==========================================
    // HISTORY & WEEKLY REPORT
    // ==========================================
    fun getCompletedSessions(): Flow<List<WorkoutSession>> {
        return workoutDao.getCompletedSessions().map { list -> list.map { it.toDomain() } }
    }

    fun getAllPersonalRecords(): Flow<List<PersonalRecord>> {
        return progressDao.getAllPersonalRecords().map { list -> list.map { it.toDomain() } }
    }

    fun getBodyMeasurements(): Flow<List<BodyMeasurement>> {
        return progressDao.getAllBodyMeasurements().map { list -> list.map { it.toDomain() } }
    }

    suspend fun saveBodyMeasurement(measurement: BodyMeasurement) = withContext(Dispatchers.IO) {
        progressDao.insertBodyMeasurement(measurement.toEntity())
    }

    fun getProgressPhotos(): Flow<List<ProgressPhoto>> {
        return progressDao.getAllProgressPhotos().map { list -> list.map { it.toDomain() } }
    }

    suspend fun saveProgressPhoto(photo: ProgressPhoto) = withContext(Dispatchers.IO) {
        progressDao.insertProgressPhoto(photo.toEntity())
    }

    fun getCardioSessions(): Flow<List<CardioSession>> {
        return cardioDao.getAllCardioSessions().map { list -> list.map { it.toDomain() } }
    }

    suspend fun saveCardioSession(session: CardioSession) = withContext(Dispatchers.IO) {
        cardioDao.insertCardioSession(session.toEntity())
    }

    // Weekly analytics report generator
    suspend fun generateWeeklyReport(targetDays: Int = 4): WeeklyReport = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val oneWeekAgo = now - (7 * 24 * 60 * 60 * 1000L)

        val completedSessions = workoutDao.getCompletedSessions().firstOrNull() ?: emptyList()
        val recentSessions = completedSessions.filter { it.startTimeMillis >= oneWeekAgo }

        val workoutsCount = recentSessions.size
        val consistency = if (targetDays > 0) {
            ((workoutsCount.toDouble() / targetDays.toDouble()) * 100.0).coerceAtMost(100.0)
        } else 100.0

        val totalVolume = recentSessions.sumOf { it.totalVolumeKg }

        // Muscle breakdown & exercise progress
        val allExercises = exerciseDao.getAllExercises().firstOrNull() ?: emptyList()
        val exerciseMap = allExercises.associateBy { it.id }

        val muscleBreakdown = mutableMapOf<MuscleGroup, Double>()
        for (m in MuscleGroup.values()) {
            muscleBreakdown[m] = 0.0
        }

        val improved = mutableListOf<String>()
        val stable = mutableListOf<String>()
        val declining = mutableListOf<String>()

        for (session in recentSessions) {
            val sets = workoutDao.getSetsForSession(session.id).firstOrNull() ?: emptyList()
            for (set in sets) {
                val ex = exerciseMap[set.exerciseId]
                if (ex != null) {
                    val primaryMuscle = try {
                        MuscleGroup.valueOf(ex.primaryMuscle)
                    } catch (e: Exception) {
                        MuscleGroup.CHEST
                    }
                    val currentVol = muscleBreakdown[primaryMuscle] ?: 0.0
                    muscleBreakdown[primaryMuscle] = currentVol + (set.weightKg * set.reps)
                }
            }
        }

        // Categorize exercises with history (processing ALL exercises with user history, not just 10)
        for (ex in allExercises) {
            val sets = workoutDao.getAllSetsForExerciseOnce(ex.id)
            if (sets.size >= 4) {
                val recentSets = sets.take(sets.size / 2)
                val olderSets = sets.drop(sets.size / 2)
                val recentAvg = recentSets.map { it.weightKg }.average()
                val olderAvg = olderSets.map { it.weightKg }.average()
                if (recentAvg > olderAvg) {
                    improved.add(ex.name)
                } else if (recentAvg < olderAvg) {
                    declining.add(ex.name)
                } else {
                    stable.add(ex.name)
                }
            }
        }

        val allPrs = progressDao.getAllPersonalRecords().firstOrNull() ?: emptyList()
        val recentPrs = allPrs.filter { it.achievedAtMillis >= oneWeekAgo }.size

        WeeklyReport(
            weekStartDateMillis = oneWeekAgo,
            weekEndDateMillis = now,
            workoutsCompleted = workoutsCount,
            targetWorkouts = targetDays,
            consistencyPercentage = consistency,
            totalVolumeKg = totalVolume,
            muscleVolumeBreakdown = muscleBreakdown,
            prsAchieved = recentPrs,
            improvedExercises = improved,
            stableExercises = stable,
            decliningExercises = declining
        )
    }

    // Admin analytics
    suspend fun getAdminMetrics(): AdminMetrics = withContext(Dispatchers.IO) {
        val totalEx = exerciseDao.getTotalExerciseCount()
        val publishedEx = exerciseDao.getPublishedExerciseCount()
        val draftEx = exerciseDao.getDraftExerciseCount()
        val archivedEx = exerciseDao.getArchivedExerciseCount()
        val totalMedia = mediaDao.getTotalMediaCount()
        val totalPrograms = programDao.getTotalProgramCount()

        val allExercises = exerciseDao.getAllExercises().firstOrNull() ?: emptyList()
        val allMedia = mediaDao.getAllMedia().firstOrNull() ?: emptyList()

        val mediaByExercise = allMedia.groupBy { it.exerciseId }
        val missingMedia = allExercises.filter { (mediaByExercise[it.id]?.size ?: 0) == 0 }.size
        val missingAlternatives = allExercises.filter { it.alternativeExerciseIds.isEmpty() }.size

        AdminMetrics(
            totalExercises = totalEx,
            publishedExercises = publishedEx,
            draftExercises = draftEx,
            archivedExercises = archivedEx,
            totalMediaFiles = totalMedia,
            totalPrograms = totalPrograms,
            exercisesMissingMedia = missingMedia,
            exercisesMissingAlternatives = missingAlternatives
        )
    }

    // ==========================================
    // APP RESET & DEVELOPMENT DEMO DATA
    // ==========================================
    suspend fun resetAppUserData() = withContext(Dispatchers.IO) {
        database.withTransaction {
            workoutDao.clearAllSets()
            workoutDao.clearAllWorkoutExercises()
            workoutDao.clearAllSessions()
            progressDao.clearAllBodyMeasurements()
            progressDao.clearAllProgressPhotos()
            progressDao.clearAllPersonalRecords()
            cardioDao.clearAllCardioSessions()
            exerciseDao.resetExerciseUserStats()
            userDao.resetProfileToDefault()
        }
    }

    suspend fun seedDemoWorkoutData() = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val oneDay = 86400000L

        // Clear existing user data first
        resetAppUserData()

        // Create sample completed workouts
        val session1Id = UUID.randomUUID().toString()
        val session1 = WorkoutSessionEntity(
            id = session1Id,
            programId = "upper_lower_4day",
            programDayId = "ul_day1",
            programName = "Upper / Lower (4-Day)",
            dayName = "Upper A - Chest & Back",
            startTimeMillis = now - 5 * oneDay,
            endTimeMillis = now - 5 * oneDay + 3600000L,
            isCompleted = true,
            totalVolumeKg = 4250.0,
            totalSetsCompleted = 12,
            totalRepsCompleted = 114,
            durationSeconds = 3600,
            notes = "Strong session, felt great on bench.",
            recoveryEnergy = 4,
            recoverySleep = 4,
            recoverySoreness = 2,
            recoveryStress = 2,
            recoveryNotes = null
        )
        workoutDao.insertSession(session1)

        val we1 = WorkoutExerciseEntity(
            id = UUID.randomUUID().toString(),
            sessionId = session1Id,
            exerciseId = "bench_press",
            exerciseName = "Barbell Bench Press",
            orderIndex = 0,
            targetSets = 3,
            minReps = 6,
            maxReps = 10,
            originalExerciseId = null,
            isCompleted = true,
            notes = ""
        )
        workoutDao.insertWorkoutExercises(listOf(we1))

        val set1 = ExerciseSetEntity(UUID.randomUUID().toString(), we1.id, session1Id, "bench_press", 1, 75.0, 10, 2, true, now - 5 * oneDay)
        val set2 = ExerciseSetEntity(UUID.randomUUID().toString(), we1.id, session1Id, "bench_press", 2, 75.0, 10, 2, true, now - 5 * oneDay)
        val set3 = ExerciseSetEntity(UUID.randomUUID().toString(), we1.id, session1Id, "bench_press", 3, 75.0, 9, 1, true, now - 5 * oneDay)
        workoutDao.insertSet(set1)
        workoutDao.insertSet(set2)
        workoutDao.insertSet(set3)

        // Second workout
        val session2Id = UUID.randomUUID().toString()
        val session2 = WorkoutSessionEntity(
            id = session2Id,
            programId = "upper_lower_4day",
            programDayId = "ul_day2",
            programName = "Upper / Lower (4-Day)",
            dayName = "Lower A - Quads & Hamstrings",
            startTimeMillis = now - 3 * oneDay,
            endTimeMillis = now - 3 * oneDay + 3900000L,
            isCompleted = true,
            totalVolumeKg = 5800.0,
            totalSetsCompleted = 12,
            totalRepsCompleted = 108,
            durationSeconds = 3900,
            notes = "Squats felt rock solid.",
            recoveryEnergy = 5,
            recoverySleep = 4,
            recoverySoreness = 1,
            recoveryStress = 1,
            recoveryNotes = null
        )
        workoutDao.insertSession(session2)

        val we2 = WorkoutExerciseEntity(
            id = UUID.randomUUID().toString(),
            sessionId = session2Id,
            exerciseId = "back_squat",
            exerciseName = "Barbell Back Squat",
            orderIndex = 0,
            targetSets = 3,
            minReps = 6,
            maxReps = 10,
            originalExerciseId = null,
            isCompleted = true,
            notes = ""
        )
        workoutDao.insertWorkoutExercises(listOf(we2))

        val s2Set1 = ExerciseSetEntity(UUID.randomUUID().toString(), we2.id, session2Id, "back_squat", 1, 100.0, 8, 2, true, now - 3 * oneDay)
        val s2Set2 = ExerciseSetEntity(UUID.randomUUID().toString(), we2.id, session2Id, "back_squat", 2, 100.0, 8, 2, true, now - 3 * oneDay)
        val s2Set3 = ExerciseSetEntity(UUID.randomUUID().toString(), we2.id, session2Id, "back_squat", 3, 100.0, 8, 2, true, now - 3 * oneDay)
        workoutDao.insertSet(s2Set1)
        workoutDao.insertSet(s2Set2)
        workoutDao.insertSet(s2Set3)

        // Seed PRs
        progressDao.insertPersonalRecord(
            PersonalRecordEntity(
                id = UUID.randomUUID().toString(),
                exerciseId = "bench_press",
                exerciseName = "Barbell Bench Press",
                weightKg = 75.0,
                reps = 10,
                estimated1RM = 100.0,
                achievedAtMillis = now - 5 * oneDay,
                previousBestWeightKg = 70.0
            )
        )
        progressDao.insertPersonalRecord(
            PersonalRecordEntity(
                id = UUID.randomUUID().toString(),
                exerciseId = "back_squat",
                exerciseName = "Barbell Back Squat",
                weightKg = 100.0,
                reps = 8,
                estimated1RM = 126.6,
                achievedAtMillis = now - 3 * oneDay,
                previousBestWeightKg = 90.0
            )
        )

        // Seed measurements
        progressDao.insertBodyMeasurement(
            BodyMeasurementEntity(
                id = UUID.randomUUID().toString(),
                timestampMillis = now - 7 * oneDay,
                weightKg = 79.2,
                waistCm = 84.0,
                chestCm = 103.0,
                armCm = 37.0,
                thighCm = 58.0,
                notes = "Starting check-in"
            )
        )
        progressDao.insertBodyMeasurement(
            BodyMeasurementEntity(
                id = UUID.randomUUID().toString(),
                timestampMillis = now,
                weightKg = 78.5,
                waistCm = 82.5,
                chestCm = 104.0,
                armCm = 37.5,
                thighCm = 58.5,
                notes = "Leaner waist, arm fullness preserved"
            )
        )

        // Seed cardio
        cardioDao.insertCardioSession(
            CardioSessionEntity(
                id = UUID.randomUUID().toString(),
                type = CardioType.WALKING.name,
                durationMinutes = 40,
                distanceKm = 3.5,
                caloriesBurned = 210,
                intensity = CardioIntensity.LOW.name,
                timestampMillis = now - 2 * oneDay,
                notes = "Incline walk for active recovery"
            )
        )

        // Update user profile to onboarded so user can see dashboard
        userDao.insertOrUpdateProfile(
            UserProfileEntity(
                id = "default_user",
                name = "Captain Max",
                age = 26,
                sex = Sex.MALE.name,
                heightCm = 178.0,
                weightKg = 78.5,
                experience = TrainingExperience.INTERMEDIATE.name,
                mainGoal = FitnessGoal.FAT_LOSS_AND_MUSCLE_GAIN.name,
                equipment = EquipmentAvailable.FULL_GYM.name,
                targetDaysPerWeek = 4,
                selectedProgramId = "upper_lower_4day",
                isOnboarded = true,
                isAdminMode = false,
                createdAtMillis = now - 14 * oneDay
            )
        )
    }
}

data class AdminMetrics(
    val totalExercises: Int,
    val publishedExercises: Int,
    val draftExercises: Int,
    val archivedExercises: Int,
    val totalMediaFiles: Int,
    val totalPrograms: Int,
    val exercisesMissingMedia: Int,
    val exercisesMissingAlternatives: Int
)

// Extension converters
fun UserProfileEntity.toDomain() = UserProfile(
    id = id,
    name = name,
    age = age,
    sex = try { Sex.valueOf(sex) } catch (e: Exception) { Sex.MALE },
    heightCm = heightCm,
    weightKg = weightKg,
    experience = try { TrainingExperience.valueOf(experience) } catch (e: Exception) { TrainingExperience.INTERMEDIATE },
    mainGoal = try { FitnessGoal.valueOf(mainGoal) } catch (e: Exception) { FitnessGoal.FAT_LOSS_AND_MUSCLE_GAIN },
    equipment = try { EquipmentAvailable.valueOf(equipment) } catch (e: Exception) { EquipmentAvailable.FULL_GYM },
    targetDaysPerWeek = targetDaysPerWeek,
    selectedProgramId = selectedProgramId,
    isOnboarded = isOnboarded,
    isAdminMode = isAdminMode,
    createdAtMillis = createdAtMillis
)

fun UserProfile.toEntity() = UserProfileEntity(
    id = id,
    name = name,
    age = age,
    sex = sex.name,
    heightCm = heightCm,
    weightKg = weightKg,
    experience = experience.name,
    mainGoal = mainGoal.name,
    equipment = equipment.name,
    targetDaysPerWeek = targetDaysPerWeek,
    selectedProgramId = selectedProgramId,
    isOnboarded = isOnboarded,
    isAdminMode = isAdminMode,
    createdAtMillis = createdAtMillis
)

fun ExerciseEntity.toDomain() = Exercise(
    id = id,
    name = name,
    alternativeNames = alternativeNames,
    primaryMuscle = try { MuscleGroup.valueOf(primaryMuscle) } catch (e: Exception) { MuscleGroup.CHEST },
    secondaryMuscles = secondaryMuscles,
    movementPattern = try { MovementPattern.valueOf(movementPattern) } catch (e: Exception) { MovementPattern.HORIZONTAL_PUSH },
    equipment = try { EquipmentAvailable.valueOf(equipment) } catch (e: Exception) { EquipmentAvailable.FULL_GYM },
    difficulty = try { Difficulty.valueOf(difficulty) } catch (e: Exception) { Difficulty.INTERMEDIATE },
    exerciseType = try { ExerciseType.valueOf(exerciseType) } catch (e: Exception) { ExerciseType.COMPOUND },
    recommendedSets = recommendedSets,
    minReps = minReps,
    maxReps = maxReps,
    rirTarget = rirTarget,
    restTimeSeconds = restTimeSeconds,
    instructions = instructions,
    setupInstructions = setupInstructions,
    executionInstructions = executionInstructions,
    breathingInstructions = breathingInstructions,
    commonMistakes = commonMistakes,
    safetyNotes = safetyNotes,
    coachingCues = coachingCues,
    progressionNotes = progressionNotes,
    alternativeExerciseIds = alternativeExerciseIds,
    status = try { ContentStatus.valueOf(status) } catch (e: Exception) { ContentStatus.PUBLISHED },
    isFavorite = isFavorite,
    viewsCount = viewsCount,
    completionCount = completionCount,
    replacementCount = replacementCount,
    lastPerformedWeightKg = lastPerformedWeightKg,
    lastPerformedReps = lastPerformedReps,
    lastPerformedDateMillis = lastPerformedDateMillis
)

fun Exercise.toEntity() = ExerciseEntity(
    id = id,
    name = name,
    alternativeNames = alternativeNames,
    primaryMuscle = primaryMuscle.name,
    secondaryMuscles = secondaryMuscles,
    movementPattern = movementPattern.name,
    equipment = equipment.name,
    difficulty = difficulty.name,
    exerciseType = exerciseType.name,
    recommendedSets = recommendedSets,
    minReps = minReps,
    maxReps = maxReps,
    rirTarget = rirTarget,
    restTimeSeconds = restTimeSeconds,
    instructions = instructions,
    setupInstructions = setupInstructions,
    executionInstructions = executionInstructions,
    breathingInstructions = breathingInstructions,
    commonMistakes = commonMistakes,
    safetyNotes = safetyNotes,
    coachingCues = coachingCues,
    progressionNotes = progressionNotes,
    alternativeExerciseIds = alternativeExerciseIds,
    status = status.name,
    isFavorite = isFavorite,
    viewsCount = viewsCount,
    completionCount = completionCount,
    replacementCount = replacementCount,
    lastPerformedWeightKg = lastPerformedWeightKg,
    lastPerformedReps = lastPerformedReps,
    lastPerformedDateMillis = lastPerformedDateMillis
)

fun ExerciseMediaEntity.toDomain() = ExerciseMedia(
    mediaId = mediaId,
    exerciseId = exerciseId,
    type = try { MediaType.valueOf(type) } catch (e: Exception) { MediaType.IMAGE },
    url = url,
    localPath = localPath,
    thumbnail = thumbnail,
    title = title,
    description = description,
    durationSeconds = durationSeconds,
    source = source,
    isPrimary = isPrimary,
    sortOrder = sortOrder,
    language = language,
    createdAtMillis = createdAtMillis,
    updatedAtMillis = updatedAtMillis
)

fun ExerciseMedia.toEntity() = ExerciseMediaEntity(
    mediaId = mediaId,
    exerciseId = exerciseId,
    type = type.name,
    url = url,
    localPath = localPath,
    thumbnail = thumbnail,
    title = title,
    description = description,
    durationSeconds = durationSeconds,
    source = source,
    isPrimary = isPrimary,
    sortOrder = sortOrder,
    language = language,
    createdAtMillis = createdAtMillis,
    updatedAtMillis = updatedAtMillis
)

fun ExerciseAlternativeEntity.toDomain() = ExerciseAlternative(
    id = id,
    exerciseId = exerciseId,
    alternativeExerciseId = alternativeExerciseId,
    similarityScore = similarityScore,
    priority = priority,
    notes = notes
)

fun ExerciseAlternative.toEntity() = ExerciseAlternativeEntity(
    id = id,
    exerciseId = exerciseId,
    alternativeExerciseId = alternativeExerciseId,
    similarityScore = similarityScore,
    priority = priority,
    notes = notes
)

fun TrainingProgramEntity.toDomain() = TrainingProgram(
    id = id,
    name = name,
    systemType = try { SplitSystemType.valueOf(systemType) } catch (e: Exception) { SplitSystemType.UPPER_LOWER },
    description = description,
    goal = try { FitnessGoal.valueOf(goal) } catch (e: Exception) { FitnessGoal.FAT_LOSS_AND_MUSCLE_GAIN },
    experienceLevel = try { TrainingExperience.valueOf(experienceLevel) } catch (e: Exception) { TrainingExperience.INTERMEDIATE },
    weeklyDays = weeklyDays,
    isRecommended = isRecommended,
    recommendationReason = recommendationReason,
    status = try { ContentStatus.valueOf(status) } catch (e: Exception) { ContentStatus.PUBLISHED }
)

fun TrainingDayEntity.toDomain() = TrainingDay(
    id = id,
    programId = programId,
    dayNumber = dayNumber,
    dayName = dayName,
    focus = focus,
    isRestDay = isRestDay,
    isActiveRecovery = isActiveRecovery,
    exercises = exercises
)

fun TrainingProgram.toEntity() = TrainingProgramEntity(
    id = id,
    name = name,
    systemType = systemType.name,
    description = description,
    goal = goal.name,
    experienceLevel = experienceLevel.name,
    weeklyDays = weeklyDays,
    isRecommended = isRecommended,
    recommendationReason = recommendationReason,
    status = status.name
)

fun TrainingDay.toEntity() = TrainingDayEntity(
    id = id,
    programId = programId,
    dayNumber = dayNumber,
    dayName = dayName,
    focus = focus,
    isRestDay = isRestDay,
    isActiveRecovery = isActiveRecovery,
    exercises = exercises
)

fun WorkoutSessionEntity.toDomain() = WorkoutSession(
    id = id,
    programId = programId,
    programDayId = programDayId,
    programName = programName,
    dayName = dayName,
    startTimeMillis = startTimeMillis,
    endTimeMillis = endTimeMillis,
    isCompleted = isCompleted,
    totalVolumeKg = totalVolumeKg,
    totalSetsCompleted = totalSetsCompleted,
    totalRepsCompleted = totalRepsCompleted,
    durationSeconds = durationSeconds,
    notes = notes,
    recoveryCheck = if (recoveryEnergy != null) {
        RecoveryCheck(
            energy = recoveryEnergy,
            sleep = recoverySleep ?: 3,
            soreness = recoverySoreness ?: 3,
            stress = recoveryStress ?: 3,
            notes = recoveryNotes ?: ""
        )
    } else null
)

fun WorkoutExerciseEntity.toDomain() = WorkoutExercise(
    id = id,
    sessionId = sessionId,
    exerciseId = exerciseId,
    exerciseName = exerciseName,
    orderIndex = orderIndex,
    targetSets = targetSets,
    minReps = minReps,
    maxReps = maxReps,
    originalExerciseId = originalExerciseId,
    isCompleted = isCompleted,
    notes = notes
)

fun WorkoutExercise.toEntity() = WorkoutExerciseEntity(
    id = id,
    sessionId = sessionId,
    exerciseId = exerciseId,
    exerciseName = exerciseName,
    orderIndex = orderIndex,
    targetSets = targetSets,
    minReps = minReps,
    maxReps = maxReps,
    originalExerciseId = originalExerciseId,
    isCompleted = isCompleted,
    notes = notes
)

fun ExerciseSetEntity.toDomain() = ExerciseSet(
    id = id,
    workoutExerciseId = workoutExerciseId,
    sessionId = sessionId,
    exerciseId = exerciseId,
    setNumber = setNumber,
    weightKg = weightKg,
    reps = reps,
    rir = rir,
    isCompleted = isCompleted,
    timestampMillis = timestampMillis
)

fun ExerciseSet.toEntity() = ExerciseSetEntity(
    id = id,
    workoutExerciseId = workoutExerciseId,
    sessionId = sessionId,
    exerciseId = exerciseId,
    setNumber = setNumber,
    weightKg = weightKg,
    reps = reps,
    rir = rir,
    isCompleted = isCompleted,
    timestampMillis = timestampMillis
)

fun PersonalRecordEntity.toDomain() = PersonalRecord(
    id = id,
    exerciseId = exerciseId,
    exerciseName = exerciseName,
    weightKg = weightKg,
    reps = reps,
    estimated1RM = estimated1RM,
    achievedAtMillis = achievedAtMillis,
    previousBestWeightKg = previousBestWeightKg
)

fun BodyMeasurementEntity.toDomain() = BodyMeasurement(
    id = id,
    timestampMillis = timestampMillis,
    weightKg = weightKg,
    waistCm = waistCm,
    chestCm = chestCm,
    armCm = armCm,
    thighCm = thighCm,
    notes = notes
)

fun BodyMeasurement.toEntity() = BodyMeasurementEntity(
    id = id,
    timestampMillis = timestampMillis,
    weightKg = weightKg,
    waistCm = waistCm,
    chestCm = chestCm,
    armCm = armCm,
    thighCm = thighCm,
    notes = notes
)

fun ProgressPhotoEntity.toDomain() = ProgressPhoto(
    id = id,
    timestampMillis = timestampMillis,
    weekLabel = weekLabel,
    pose = try { PhotoPose.valueOf(pose) } catch (e: Exception) { PhotoPose.FRONT },
    imagePathOrUri = imagePathOrUri,
    notes = notes
)

fun ProgressPhoto.toEntity() = ProgressPhotoEntity(
    id = id,
    timestampMillis = timestampMillis,
    weekLabel = weekLabel,
    pose = pose.name,
    imagePathOrUri = imagePathOrUri,
    notes = notes
)

fun CardioSessionEntity.toDomain() = CardioSession(
    id = id,
    type = try { CardioType.valueOf(type) } catch (e: Exception) { CardioType.RUNNING },
    durationMinutes = durationMinutes,
    distanceKm = distanceKm,
    caloriesBurned = caloriesBurned,
    intensity = try { CardioIntensity.valueOf(intensity) } catch (e: Exception) { CardioIntensity.MODERATE },
    timestampMillis = timestampMillis,
    notes = notes
)

fun CardioSession.toEntity() = CardioSessionEntity(
    id = id,
    type = type.name,
    durationMinutes = durationMinutes,
    distanceKm = distanceKm,
    caloriesBurned = caloriesBurned,
    intensity = intensity.name,
    timestampMillis = timestampMillis,
    notes = notes
)
