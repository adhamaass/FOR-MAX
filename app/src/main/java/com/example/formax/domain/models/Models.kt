package com.example.formax.domain.models

enum class AppLanguage(val code: String, val displayName: String, val isRtl: Boolean) {
    ARABIC("ar", "العربية", true),
    ENGLISH("en", "English", false);

    companion object {
        fun fromCode(code: String): AppLanguage =
            if (code.startsWith("en", ignoreCase = true)) ENGLISH else ARABIC
    }
}

enum class UserRole {
    USER,
    ADMIN
}

enum class Sex(val displayNameAr: String) {
    MALE("ذكر"),
    FEMALE("أنثى"),
    OTHER("آخر")
}

enum class TrainingExperience(val displayNameAr: String) {
    BEGINNER("مبتدئ"),
    INTERMEDIATE("متوسط"),
    ADVANCED("متقدم")
}

enum class FitnessGoal(val displayName: String, val displayNameAr: String) {
    FAT_LOSS_AND_MUSCLE_GAIN("Fat Loss + Muscle Gain", "خسارة دهون + بناء عضلات"),
    MUSCLE_GAIN("Muscle Gain", "بناء عضلات (تضخيم)"),
    FAT_LOSS("Fat Loss", "خسارة دهون (تنشيف)"),
    STRENGTH("Strength", "زيادة القوة البدنية"),
    GENERAL_FITNESS("General Fitness", "لياقة بدنية عامة")
}

enum class EquipmentAvailable(val displayName: String, val displayNameAr: String) {
    FULL_GYM("Full Gym", "نادي رياضي متكامل"),
    BARBELL_DUMBBELLS("Barbells & Dumbbells", "باربل ودامبلز"),
    DUMBBELLS_ONLY("Dumbbells Only", "دامبلز فقط"),
    BODYWEIGHT_HOME("Bodyweight & Bands", "وزن الجسم وأحزمة مقاومة")
}

enum class SplitSystemType(val displayName: String, val displayNameAr: String, val shortDescAr: String) {
    FULL_BODY(
        "Full Body",
        "الجسم بالكامل",
        "تدريب كل مجموعة عضلية رئيسية في نفس الجلسة. ممتاز ومناسب عند التدريب أياماً أقل في الأسبوع."
    ),
    UPPER_LOWER(
        "Upper / Lower",
        "علوي / سفلي",
        "توزيع جلسات الجزء العلوي والسفلي على مدار الأسبوع لتحقيق أفضل توازن بين حجم التدريب والاستشفاء."
    ),
    PUSH_PULL_LEGS(
        "Push / Pull / Legs",
        "دفع / سحب / أرجل",
        "تنظيم الجلسات حسب نوع الحركة وسلاسل العضلات المتوافقة لتركيز موضعي عالي."
    ),
    FIVE_DAY_HYBRID(
        "5-Day Hybrid",
        "هجين 5 أيام",
        "يدمج بين القوة التراكمية في التمارين المركبة مع كثافة تضخيم عالية للرياضيين ذوي الخبرة."
    ),
    CUSTOM_ADVANCED(
        "Custom / Advanced",
        "مخصص / متقدم",
        "توزيع مرن لحجم التدريب وتكرار الجلسات حسب الاحتياجات الخاصة للرياضي."
    )
}

data class UserProfile(
    val id: String = "default_user",
    val name: String = "Athlete",
    val age: Int = 26,
    val sex: Sex = Sex.MALE,
    val heightCm: Double = 178.0,
    val weightKg: Double = 78.5,
    val experience: TrainingExperience = TrainingExperience.INTERMEDIATE,
    val mainGoal: FitnessGoal = FitnessGoal.FAT_LOSS_AND_MUSCLE_GAIN,
    val equipment: EquipmentAvailable = EquipmentAvailable.FULL_GYM,
    val targetDaysPerWeek: Int = 4,
    val selectedProgramId: String = "upper_lower_4day",
    val isOnboarded: Boolean = false,
    val isAdminMode: Boolean = false,
    val role: UserRole = UserRole.USER,
    val preferredLanguage: String = "ar",
    val createdAtMillis: Long = System.currentTimeMillis()
)

enum class ProgressionTrend(val displayName: String, val displayNameAr: String) {
    IMPROVED("Improved", "تطور مستمر"),
    STABLE("Stable", "ثبات"),
    DECLINING("Declining", "تراجع")
}

data class ExerciseProgressionSummary(
    val exerciseId: String,
    val exerciseName: String,
    val lastSessionSummary: String,
    val previousSessionSummary: String,
    val bestPerformanceSummary: String,
    val currentPerformanceSummary: String,
    val trend: ProgressionTrend,
    val suggestedNextStep: String
)

enum class MuscleGroup(val displayName: String, val displayNameAr: String) {
    CHEST("Chest", "الصدر"),
    BACK("Back", "الظهر"),
    SHOULDERS("Shoulders", "الأكتاف"),
    BICEPS("Biceps", "البايسبس"),
    TRICEPS("Triceps", "الترايسبس"),
    LEGS("Legs", "الأرجل"),
    CORE("Core", "عضلات البطن والوسط"),
    FULL_BODY("Full Body", "الجسم بالكامل")
}

enum class MovementPattern(val displayName: String, val displayNameAr: String) {
    HORIZONTAL_PUSH("Horizontal Push", "دفع أفقي"),
    VERTICAL_PUSH("Vertical Push", "دفع رأسي"),
    HORIZONTAL_PULL("Horizontal Pull", "سحب أفقي"),
    VERTICAL_PULL("Vertical Pull", "سحب رأسي"),
    SQUAT("Squat", "سكوات (القرفصاء)"),
    HINGE("Hinge", "مفصل الورك (Hinge)"),
    LUNGE("Lunge", "طعن (Lunge)"),
    ISOLATION("Isolation", "عزل عضلي"),
    CARRY("Carry", "حمل أوزان")
}

enum class Difficulty(val displayName: String, val displayNameAr: String) {
    BEGINNER("Beginner", "مبتدئ"),
    INTERMEDIATE("Intermediate", "متوسط"),
    ADVANCED("Advanced", "متقدم")
}

enum class ExerciseType(val displayName: String, val displayNameAr: String) {
    COMPOUND("Compound", "مركّب"),
    ISOLATION("Isolation", "عزل"),
    BODYWEIGHT("Bodyweight", "وزن الجسم"),
    MACHINE("Machine", "جهاز"),
    CABLE("Cable", "كيبل")
}

enum class ContentStatus {
    DRAFT,
    PUBLISHED,
    ARCHIVED
}

data class Exercise(
    val id: String,
    val name: String,
    val alternativeNames: List<String> = emptyList(),
    val primaryMuscle: MuscleGroup,
    val secondaryMuscles: List<MuscleGroup> = emptyList(),
    val movementPattern: MovementPattern,
    val equipment: EquipmentAvailable = EquipmentAvailable.FULL_GYM,
    val difficulty: Difficulty = Difficulty.INTERMEDIATE,
    val exerciseType: ExerciseType = ExerciseType.COMPOUND,
    val recommendedSets: Int = 3,
    val minReps: Int = 8,
    val maxReps: Int = 12,
    val rirTarget: Int = 2,
    val restTimeSeconds: Int = 120,
    val instructions: String = "",
    val setupInstructions: String = "",
    val executionInstructions: String = "",
    val breathingInstructions: String = "",
    val commonMistakes: List<String> = emptyList(),
    val safetyNotes: String = "",
    val coachingCues: String = "",
    val progressionNotes: String = "",
    val alternativeExerciseIds: List<String> = emptyList(),
    val status: ContentStatus = ContentStatus.PUBLISHED,
    val isFavorite: Boolean = false,
    val viewsCount: Int = 0,
    val completionCount: Int = 0,
    val replacementCount: Int = 0,
    val lastPerformedWeightKg: Double? = null,
    val lastPerformedReps: Int? = null,
    val lastPerformedDateMillis: Long? = null
)

enum class MediaType {
    VIDEO,
    GIF,
    IMAGE,
    THUMBNAIL,
    URL
}

data class ExerciseMedia(
    val mediaId: String,
    val exerciseId: String,
    val type: MediaType,
    val url: String = "",
    val localPath: String = "",
    val thumbnail: String = "",
    val title: String = "",
    val description: String = "",
    val durationSeconds: Int = 0,
    val source: String = "local_asset",
    val isPrimary: Boolean = false,
    val sortOrder: Int = 0,
    val language: String = "ar",
    val createdAtMillis: Long = System.currentTimeMillis(),
    val updatedAtMillis: Long = System.currentTimeMillis()
)

data class ExerciseAlternative(
    val id: String,
    val exerciseId: String,
    val alternativeExerciseId: String,
    val similarityScore: Int = 90,
    val priority: Int = 1,
    val notes: String = ""
)

data class ProgramExercise(
    val exerciseId: String,
    val orderIndex: Int,
    val targetSets: Int = 3,
    val minReps: Int = 8,
    val maxReps: Int = 12,
    val rirTarget: Int = 2,
    val restTimeSeconds: Int = 120,
    val notes: String = ""
)

data class TrainingDay(
    val id: String,
    val programId: String,
    val dayNumber: Int,
    val dayName: String,
    val focus: String,
    val isRestDay: Boolean = false,
    val isActiveRecovery: Boolean = false,
    val exercises: List<ProgramExercise> = emptyList()
)

data class TrainingProgram(
    val id: String,
    val name: String,
    val systemType: SplitSystemType,
    val description: String,
    val goal: FitnessGoal,
    val experienceLevel: TrainingExperience,
    val weeklyDays: Int,
    val isRecommended: Boolean = false,
    val recommendationReason: String = "",
    val days: List<TrainingDay> = emptyList(),
    val status: ContentStatus = ContentStatus.PUBLISHED
)

data class RecoveryCheck(
    val energy: Int = 3,
    val sleep: Int = 3,
    val soreness: Int = 3,
    val stress: Int = 3,
    val notes: String = ""
)

data class WorkoutSession(
    val id: String,
    val programId: String,
    val programDayId: String,
    val programName: String,
    val dayName: String,
    val startTimeMillis: Long,
    val endTimeMillis: Long? = null,
    val isCompleted: Boolean = false,
    val totalVolumeKg: Double = 0.0,
    val totalSetsCompleted: Int = 0,
    val totalRepsCompleted: Int = 0,
    val durationSeconds: Long = 0,
    val notes: String = "",
    val recoveryCheck: RecoveryCheck? = null
)

data class WorkoutExercise(
    val id: String,
    val sessionId: String,
    val exerciseId: String,
    val exerciseName: String,
    val orderIndex: Int,
    val targetSets: Int = 3,
    val minReps: Int = 8,
    val maxReps: Int = 12,
    val originalExerciseId: String? = null,
    val isCompleted: Boolean = false,
    val notes: String = ""
)

data class ExerciseSet(
    val id: String,
    val workoutExerciseId: String,
    val sessionId: String,
    val exerciseId: String,
    val setNumber: Int,
    val weightKg: Double,
    val reps: Int,
    val rir: Int = 2,
    val isCompleted: Boolean = true,
    val timestampMillis: Long = System.currentTimeMillis()
) {
    val volumeKg: Double get() = weightKg * reps
    val estimated1RM: Double get() = if (reps > 0) weightKg * (1.0 + reps / 30.0) else weightKg
}

data class PersonalRecord(
    val id: String,
    val exerciseId: String,
    val exerciseName: String,
    val weightKg: Double,
    val reps: Int,
    val estimated1RM: Double,
    val achievedAtMillis: Long = System.currentTimeMillis(),
    val previousBestWeightKg: Double = 0.0
)

data class WeeklyReport(
    val weekStartDateMillis: Long,
    val weekEndDateMillis: Long,
    val workoutsCompleted: Int,
    val targetWorkouts: Int,
    val consistencyPercentage: Double,
    val totalVolumeKg: Double,
    val muscleVolumeBreakdown: Map<MuscleGroup, Double>,
    val prsAchieved: Int,
    val improvedExercises: List<String>,
    val stableExercises: List<String>,
    val decliningExercises: List<String>
)

data class BodyMeasurement(
    val id: String,
    val timestampMillis: Long = System.currentTimeMillis(),
    val weightKg: Double,
    val waistCm: Double? = null,
    val chestCm: Double? = null,
    val armCm: Double? = null,
    val thighCm: Double? = null,
    val notes: String = ""
)

enum class PhotoPose(val displayNameAr: String) {
    FRONT("أمامي"),
    SIDE("جانبي"),
    BACK("خلفي")
}

data class ProgressPhoto(
    val id: String,
    val timestampMillis: Long = System.currentTimeMillis(),
    val weekLabel: String = "الأسبوع 1",
    val pose: PhotoPose = PhotoPose.FRONT,
    val imagePathOrUri: String,
    val notes: String = ""
)

enum class CardioType(val displayName: String, val displayNameAr: String) {
    WALKING("Walking", "المشي"),
    CYCLING("Cycling", "ركوب الدراجة"),
    TREADMILL("Treadmill", "جهاز المشي"),
    RUNNING("Running", "الجري"),
    ROWING("Rowing", "جهاز التجديف"),
    ELLIPTICAL("Elliptical", "جهاز التزلج (إليبتيكال)"),
    INTERVALS("Interval Training", "تدريب الفترات (HIIT)")
}

enum class CardioIntensity(val displayName: String, val displayNameAr: String) {
    LOW("Low", "منخفضة"),
    MODERATE("Moderate", "متوسطة"),
    HIGH("High", "عالية"),
    INTERVAL("HIIT", "فترات عالية الشدة")
}

data class CardioSession(
    val id: String,
    val type: CardioType,
    val durationMinutes: Int,
    val distanceKm: Double? = null,
    val caloriesBurned: Int? = null,
    val intensity: CardioIntensity = CardioIntensity.MODERATE,
    val timestampMillis: Long = System.currentTimeMillis(),
    val notes: String = ""
)
