package com.example.formax.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 'default_user' LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 'default_user' LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET isOnboarded = 0, name = 'Athlete', age = 26, sex = 'MALE', heightCm = 178.0, weightKg = 78.5, experience = 'INTERMEDIATE', mainGoal = 'FAT_LOSS_AND_MUSCLE_GAIN', equipment = 'FULL_GYM', targetDaysPerWeek = 4, selectedProgramId = 'upper_lower_4day', isAdminMode = 0 WHERE id = 'default_user'")
    suspend fun resetProfileToDefault()
}

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises WHERE status = 'PUBLISHED' ORDER BY name ASC")
    fun getPublishedExercises(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises ORDER BY name ASC")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE id = :id LIMIT 1")
    fun getExerciseById(id: String): Flow<ExerciseEntity?>

    @Query("SELECT * FROM exercises WHERE id = :id LIMIT 1")
    suspend fun getExerciseByIdOnce(id: String): ExerciseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Update
    suspend fun updateExercise(exercise: ExerciseEntity)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

    @Query("DELETE FROM exercises WHERE id = :id")
    suspend fun deleteExerciseById(id: String)

    @Query("UPDATE exercises SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: String, isFavorite: Boolean)

    @Query("UPDATE exercises SET viewsCount = viewsCount + 1 WHERE id = :id")
    suspend fun incrementViewCount(id: String)

    @Query("UPDATE exercises SET completionCount = completionCount + 1, lastPerformedWeightKg = :weight, lastPerformedReps = :reps, lastPerformedDateMillis = :timestamp WHERE id = :id")
    suspend fun recordCompletion(id: String, weight: Double, reps: Int, timestamp: Long)

    @Query("UPDATE exercises SET replacementCount = replacementCount + 1 WHERE id = :id")
    suspend fun incrementReplacementCount(id: String)

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getTotalExerciseCount(): Int

    @Query("SELECT COUNT(*) FROM exercises WHERE status = 'PUBLISHED'")
    suspend fun getPublishedExerciseCount(): Int

    @Query("SELECT COUNT(*) FROM exercises WHERE status = 'DRAFT'")
    suspend fun getDraftExerciseCount(): Int

    @Query("SELECT COUNT(*) FROM exercises WHERE status = 'ARCHIVED'")
    suspend fun getArchivedExerciseCount(): Int

    @Query("UPDATE exercises SET lastPerformedWeightKg = NULL, lastPerformedReps = NULL, lastPerformedDateMillis = NULL, completionCount = 0, isFavorite = 0")
    suspend fun resetExerciseUserStats()
}

@Dao
interface MediaDao {
    @Query("SELECT * FROM exercise_media WHERE exerciseId = :exerciseId ORDER BY sortOrder ASC, isPrimary DESC")
    fun getMediaForExercise(exerciseId: String): Flow<List<ExerciseMediaEntity>>

    @Query("SELECT * FROM exercise_media WHERE exerciseId = :exerciseId ORDER BY sortOrder ASC, isPrimary DESC")
    suspend fun getMediaForExerciseOnce(exerciseId: String): List<ExerciseMediaEntity>

    @Query("SELECT * FROM exercise_media")
    fun getAllMedia(): Flow<List<ExerciseMediaEntity>>

    @Query("SELECT * FROM exercise_media WHERE mediaId = :mediaId LIMIT 1")
    suspend fun getMediaById(mediaId: String): ExerciseMediaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(media: ExerciseMediaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMedia(mediaList: List<ExerciseMediaEntity>)

    @Update
    suspend fun updateMedia(media: ExerciseMediaEntity)

    @Query("DELETE FROM exercise_media WHERE mediaId = :mediaId")
    suspend fun deleteMediaById(mediaId: String)

    @Query("UPDATE exercise_media SET isPrimary = 0 WHERE exerciseId = :exerciseId AND type = :mediaType")
    suspend fun clearPrimaryForExerciseType(exerciseId: String, mediaType: String)

    @Query("SELECT COUNT(*) FROM exercise_media")
    suspend fun getTotalMediaCount(): Int
}

@Dao
interface AlternativeDao {
    @Query("SELECT * FROM exercise_alternatives WHERE exerciseId = :exerciseId ORDER BY priority ASC, similarityScore DESC")
    fun getAlternativesForExercise(exerciseId: String): Flow<List<ExerciseAlternativeEntity>>

    @Query("SELECT * FROM exercise_alternatives WHERE exerciseId = :exerciseId ORDER BY priority ASC, similarityScore DESC")
    suspend fun getAlternativesForExerciseOnce(exerciseId: String): List<ExerciseAlternativeEntity>

    @Query("SELECT * FROM exercise_alternatives")
    fun getAllAlternatives(): Flow<List<ExerciseAlternativeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlternative(alternative: ExerciseAlternativeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlternatives(alternatives: List<ExerciseAlternativeEntity>)

    @Query("DELETE FROM exercise_alternatives WHERE id = :id")
    suspend fun deleteAlternativeById(id: String)
}

@Dao
interface ProgramDao {
    @Query("SELECT * FROM training_programs")
    fun getAllPrograms(): Flow<List<TrainingProgramEntity>>

    @Query("SELECT * FROM training_programs WHERE id = :id LIMIT 1")
    suspend fun getProgramById(id: String): TrainingProgramEntity?

    @Query("SELECT * FROM training_days WHERE programId = :programId ORDER BY dayNumber ASC")
    fun getDaysForProgram(programId: String): Flow<List<TrainingDayEntity>>

    @Query("SELECT * FROM training_days WHERE programId = :programId ORDER BY dayNumber ASC")
    suspend fun getDaysForProgramOnce(programId: String): List<TrainingDayEntity>

    @Query("SELECT * FROM training_days WHERE id = :dayId LIMIT 1")
    suspend fun getDayById(dayId: String): TrainingDayEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: TrainingProgramEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrograms(programs: List<TrainingProgramEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDay(day: TrainingDayEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDays(days: List<TrainingDayEntity>)

    @Query("SELECT COUNT(*) FROM training_programs")
    suspend fun getTotalProgramCount(): Int

    @Query("DELETE FROM training_programs WHERE id = :id")
    suspend fun deleteProgramById(id: String)

    @Query("UPDATE training_programs SET status = :status WHERE id = :id")
    suspend fun updateProgramStatus(id: String, status: String)
}

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 0 ORDER BY startTimeMillis DESC LIMIT 1")
    fun getActiveSession(): Flow<WorkoutSessionEntity?>

    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 1 ORDER BY startTimeMillis DESC")
    fun getCompletedSessions(): Flow<List<WorkoutSessionEntity>>

    @Query("SELECT * FROM workout_sessions WHERE id = :sessionId LIMIT 1")
    suspend fun getSessionById(sessionId: String): WorkoutSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkoutSessionEntity)

    @Update
    suspend fun updateSession(session: WorkoutSessionEntity)

    @Query("DELETE FROM workout_sessions WHERE id = :sessionId")
    suspend fun deleteSessionById(sessionId: String)

    @Query("SELECT * FROM workout_exercises WHERE sessionId = :sessionId ORDER BY orderIndex ASC")
    fun getExercisesForSession(sessionId: String): Flow<List<WorkoutExerciseEntity>>

    @Query("SELECT * FROM workout_exercises WHERE sessionId = :sessionId ORDER BY orderIndex ASC")
    suspend fun getExercisesForSessionOnce(sessionId: String): List<WorkoutExerciseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercises(exercises: List<WorkoutExerciseEntity>)

    @Update
    suspend fun updateWorkoutExercise(exercise: WorkoutExerciseEntity)

    @Query("SELECT * FROM exercise_sets WHERE sessionId = :sessionId ORDER BY setNumber ASC")
    fun getSetsForSession(sessionId: String): Flow<List<ExerciseSetEntity>>

    @Query("SELECT * FROM exercise_sets WHERE workoutExerciseId = :workoutExerciseId ORDER BY setNumber ASC")
    fun getSetsForWorkoutExercise(workoutExerciseId: String): Flow<List<ExerciseSetEntity>>

    @Query("SELECT * FROM exercise_sets WHERE exerciseId = :exerciseId AND isCompleted = 1 ORDER BY timestampMillis DESC")
    fun getAllSetsForExercise(exerciseId: String): Flow<List<ExerciseSetEntity>>

    @Query("SELECT * FROM exercise_sets WHERE exerciseId = :exerciseId AND isCompleted = 1 ORDER BY timestampMillis DESC")
    suspend fun getAllSetsForExerciseOnce(exerciseId: String): List<ExerciseSetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: ExerciseSetEntity)

    @Delete
    suspend fun deleteSet(set: ExerciseSetEntity)

    @Query("DELETE FROM exercise_sets WHERE id = :id")
    suspend fun deleteSetById(id: String)

    @Query("DELETE FROM workout_sessions")
    suspend fun clearAllSessions()

    @Query("DELETE FROM workout_exercises")
    suspend fun clearAllWorkoutExercises()

    @Query("DELETE FROM exercise_sets")
    suspend fun clearAllSets()
}

@Dao
interface ProgressDao {
    @Query("SELECT * FROM body_measurements ORDER BY timestampMillis DESC")
    fun getAllBodyMeasurements(): Flow<List<BodyMeasurementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodyMeasurement(measurement: BodyMeasurementEntity)

    @Query("DELETE FROM body_measurements WHERE id = :id")
    suspend fun deleteMeasurementById(id: String)

    @Query("DELETE FROM body_measurements")
    suspend fun clearAllBodyMeasurements()

    @Query("SELECT * FROM progress_photos ORDER BY timestampMillis DESC")
    fun getAllProgressPhotos(): Flow<List<ProgressPhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgressPhoto(photo: ProgressPhotoEntity)

    @Query("DELETE FROM progress_photos WHERE id = :id")
    suspend fun deletePhotoById(id: String)

    @Query("DELETE FROM progress_photos")
    suspend fun clearAllProgressPhotos()

    @Query("SELECT * FROM personal_records ORDER BY achievedAtMillis DESC")
    fun getAllPersonalRecords(): Flow<List<PersonalRecordEntity>>

    @Query("SELECT * FROM personal_records WHERE exerciseId = :exerciseId ORDER BY estimated1RM DESC LIMIT 1")
    suspend fun getBestPersonalRecordForExercise(exerciseId: String): PersonalRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalRecord(pr: PersonalRecordEntity)

    @Query("DELETE FROM personal_records")
    suspend fun clearAllPersonalRecords()
}

@Dao
interface CardioDao {
    @Query("SELECT * FROM cardio_sessions ORDER BY timestampMillis DESC")
    fun getAllCardioSessions(): Flow<List<CardioSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCardioSession(session: CardioSessionEntity)

    @Query("DELETE FROM cardio_sessions WHERE id = :id")
    suspend fun deleteCardioSessionById(id: String)

    @Query("DELETE FROM cardio_sessions")
    suspend fun clearAllCardioSessions()
}
