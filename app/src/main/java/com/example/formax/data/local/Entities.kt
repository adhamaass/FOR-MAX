package com.example.formax.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.formax.domain.models.*
import org.json.JSONArray
import org.json.JSONObject

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach { array.put(it) }
        return array.toString()
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        val list = mutableListOf<String>()
        val array = JSONArray(value)
        for (i in 0 until array.length()) {
            list.add(array.getString(i))
        }
        return list
    }

    @TypeConverter
    fun fromMuscleGroupList(value: List<MuscleGroup>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach { array.put(it.name) }
        return array.toString()
    }

    @TypeConverter
    fun toMuscleGroupList(value: String?): List<MuscleGroup> {
        if (value.isNullOrBlank()) return emptyList()
        val list = mutableListOf<MuscleGroup>()
        val array = JSONArray(value)
        for (i in 0 until array.length()) {
            try {
                list.add(MuscleGroup.valueOf(array.getString(i)))
            } catch (e: Exception) {
                // Ignore unknown
            }
        }
        return list
    }

    @TypeConverter
    fun fromProgramExerciseList(value: List<ProgramExercise>?): String {
        if (value == null) return "[]"
        val array = JSONArray()
        value.forEach {
            val obj = JSONObject().apply {
                put("exerciseId", it.exerciseId)
                put("orderIndex", it.orderIndex)
                put("targetSets", it.targetSets)
                put("minReps", it.minReps)
                put("maxReps", it.maxReps)
                put("rirTarget", it.rirTarget)
                put("restTimeSeconds", it.restTimeSeconds)
                put("notes", it.notes)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toProgramExerciseList(value: String?): List<ProgramExercise> {
        if (value.isNullOrBlank()) return emptyList()
        val list = mutableListOf<ProgramExercise>()
        val array = JSONArray(value)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add(
                ProgramExercise(
                    exerciseId = obj.getString("exerciseId"),
                    orderIndex = obj.getInt("orderIndex"),
                    targetSets = obj.optInt("targetSets", 3),
                    minReps = obj.optInt("minReps", 8),
                    maxReps = obj.optInt("maxReps", 12),
                    rirTarget = obj.optInt("rirTarget", 2),
                    restTimeSeconds = obj.optInt("restTimeSeconds", 120),
                    notes = obj.optString("notes", "")
                )
            )
        }
        return list
    }
}

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "default_user",
    val name: String,
    val age: Int,
    val sex: String,
    val heightCm: Double,
    val weightKg: Double,
    val experience: String,
    val mainGoal: String,
    val equipment: String,
    val targetDaysPerWeek: Int,
    val selectedProgramId: String,
    val isOnboarded: Boolean,
    val isAdminMode: Boolean,
    val createdAtMillis: Long
)

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val alternativeNames: List<String>,
    val primaryMuscle: String,
    val secondaryMuscles: List<MuscleGroup>,
    val movementPattern: String,
    val equipment: String,
    val difficulty: String,
    val exerciseType: String,
    val recommendedSets: Int,
    val minReps: Int,
    val maxReps: Int,
    val rirTarget: Int,
    val restTimeSeconds: Int,
    val instructions: String,
    val setupInstructions: String,
    val executionInstructions: String,
    val breathingInstructions: String,
    val commonMistakes: List<String>,
    val safetyNotes: String,
    val coachingCues: String,
    val progressionNotes: String,
    val alternativeExerciseIds: List<String>,
    val status: String,
    val isFavorite: Boolean,
    val viewsCount: Int,
    val completionCount: Int,
    val replacementCount: Int,
    val lastPerformedWeightKg: Double?,
    val lastPerformedReps: Int?,
    val lastPerformedDateMillis: Long?
)

@Entity(tableName = "exercise_media")
data class ExerciseMediaEntity(
    @PrimaryKey val mediaId: String,
    val exerciseId: String,
    val type: String,
    val url: String,
    val localPath: String,
    val thumbnail: String,
    val title: String,
    val description: String,
    val durationSeconds: Int,
    val source: String,
    val isPrimary: Boolean,
    val sortOrder: Int,
    val language: String,
    val createdAtMillis: Long,
    val updatedAtMillis: Long
)

@Entity(tableName = "exercise_alternatives")
data class ExerciseAlternativeEntity(
    @PrimaryKey val id: String,
    val exerciseId: String,
    val alternativeExerciseId: String,
    val similarityScore: Int,
    val priority: Int,
    val notes: String
)

@Entity(tableName = "training_programs")
data class TrainingProgramEntity(
    @PrimaryKey val id: String,
    val name: String,
    val systemType: String,
    val description: String,
    val goal: String,
    val experienceLevel: String,
    val weeklyDays: Int,
    val isRecommended: Boolean,
    val recommendationReason: String,
    val status: String
)

@Entity(tableName = "training_days")
data class TrainingDayEntity(
    @PrimaryKey val id: String,
    val programId: String,
    val dayNumber: Int,
    val dayName: String,
    val focus: String,
    val isRestDay: Boolean,
    val isActiveRecovery: Boolean,
    val exercises: List<ProgramExercise>
)

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey val id: String,
    val programId: String,
    val programDayId: String,
    val programName: String,
    val dayName: String,
    val startTimeMillis: Long,
    val endTimeMillis: Long?,
    val isCompleted: Boolean,
    val totalVolumeKg: Double,
    val totalSetsCompleted: Int,
    val totalRepsCompleted: Int,
    val durationSeconds: Long,
    val notes: String,
    val recoveryEnergy: Int?,
    val recoverySleep: Int?,
    val recoverySoreness: Int?,
    val recoveryStress: Int?,
    val recoveryNotes: String?
)

@Entity(tableName = "workout_exercises")
data class WorkoutExerciseEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val exerciseId: String,
    val exerciseName: String,
    val orderIndex: Int,
    val targetSets: Int,
    val minReps: Int,
    val maxReps: Int,
    val originalExerciseId: String?,
    val isCompleted: Boolean,
    val notes: String
)

@Entity(tableName = "exercise_sets")
data class ExerciseSetEntity(
    @PrimaryKey val id: String,
    val workoutExerciseId: String,
    val sessionId: String,
    val exerciseId: String,
    val setNumber: Int,
    val weightKg: Double,
    val reps: Int,
    val rir: Int,
    val isCompleted: Boolean,
    val timestampMillis: Long
)

@Entity(tableName = "personal_records")
data class PersonalRecordEntity(
    @PrimaryKey val id: String,
    val exerciseId: String,
    val exerciseName: String,
    val weightKg: Double,
    val reps: Int,
    val estimated1RM: Double,
    val achievedAtMillis: Long,
    val previousBestWeightKg: Double
)

@Entity(tableName = "body_measurements")
data class BodyMeasurementEntity(
    @PrimaryKey val id: String,
    val timestampMillis: Long,
    val weightKg: Double,
    val waistCm: Double?,
    val chestCm: Double?,
    val armCm: Double?,
    val thighCm: Double?,
    val notes: String
)

@Entity(tableName = "progress_photos")
data class ProgressPhotoEntity(
    @PrimaryKey val id: String,
    val timestampMillis: Long,
    val weekLabel: String,
    val pose: String,
    val imagePathOrUri: String,
    val notes: String
)

@Entity(tableName = "cardio_sessions")
data class CardioSessionEntity(
    @PrimaryKey val id: String,
    val type: String,
    val durationMinutes: Int,
    val distanceKm: Double?,
    val caloriesBurned: Int?,
    val intensity: String,
    val timestampMillis: Long,
    val notes: String
)
