package com.example

import com.example.formax.domain.engine.*
import com.example.formax.domain.models.*
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testBilingualSystemLanguages() {
        val arabic = AppLanguage.ARABIC
        assertEquals("ar", arabic.code)
        assertEquals("العربية", arabic.displayName)
        assertTrue(arabic.isRtl)

        val english = AppLanguage.ENGLISH
        assertEquals("en", english.code)
        assertEquals("English", english.displayName)
        assertFalse(english.isRtl)

        assertEquals(AppLanguage.ARABIC, AppLanguage.fromCode("ar"))
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode("en"))
        assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode("en_US"))
    }

    @Test
    fun testUserRoleSecurity() {
        val userRole = UserRole.USER
        val adminRole = UserRole.ADMIN

        assertNotEquals(userRole, adminRole)
        val defaultProfile = UserProfile()
        assertEquals(UserRole.USER, defaultProfile.role)
    }

    @Test
    fun testSetVolumeAndExerciseVolume() {
        val set1 = ExerciseSet("1", "we1", "s1", "bench", 1, 80.0, 10, 2)
        val set2 = ExerciseSet("2", "we1", "s1", "bench", 2, 80.0, 8, 1)

        val set1Vol = ProgressionEngine.calculateSetVolume(set1.weightKg, set1.reps)
        assertEquals(800.0, set1Vol, 0.01)

        val totalVol = ProgressionEngine.calculateExerciseVolume(listOf(set1, set2))
        assertEquals(1440.0, totalVol, 0.01)
    }

    @Test
    fun testEstimated1RMFormula() {
        // Epley formula: 100 kg x 10 reps = 100 * (1 + 10/30) = 133.3 kg
        val e1rm = ProgressionEngine.calculateEstimated1RM(100.0, 10)
        assertEquals(133.3, e1rm, 0.1)

        val singleRep = ProgressionEngine.calculateEstimated1RM(120.0, 1)
        assertEquals(120.0, singleRep, 0.01)
    }

    @Test
    fun testProgressionEngineDeterministicSuggestions() {
        // 1. First time guidance (Never inventing weights!)
        val firstTime = ProgressionEngine.evaluateProgression(
            currentSets = emptyList(),
            previousSessionSets = null,
            targetMinReps = 8,
            targetMaxReps = 12
        )
        assertTrue(firstTime is ProgressionSuggestion.FirstTimeGuidance)
        assertTrue(firstTime.message.isNotBlank())
        assertTrue(firstTime.messageEn.isNotBlank())

        // 2. All sets hit top of rep range (12 reps) with good RIR (>= 1) -> Suggest Increase
        val setsHitTop = listOf(
            ExerciseSet("1", "we1", "s1", "bench", 1, 80.0, 12, 2),
            ExerciseSet("2", "we1", "s1", "bench", 2, 80.0, 12, 1),
            ExerciseSet("3", "we1", "s1", "bench", 3, 80.0, 12, 1)
        )
        val suggestionIncrease = ProgressionEngine.evaluateProgression(
            currentSets = setsHitTop,
            previousSessionSets = null,
            targetMinReps = 8,
            targetMaxReps = 12
        )
        assertTrue(suggestionIncrease is ProgressionSuggestion.IncreaseLoad)

        // 3. Sets within target range (8-11 reps) -> Maintain load
        val setsMaintaining = listOf(
            ExerciseSet("1", "we1", "s1", "bench", 1, 80.0, 10, 2),
            ExerciseSet("2", "we1", "s1", "bench", 2, 80.0, 9, 2),
            ExerciseSet("3", "we1", "s1", "bench", 3, 80.0, 8, 1)
        )
        val suggestionMaintain = ProgressionEngine.evaluateProgression(
            currentSets = setsMaintaining,
            previousSessionSets = null,
            targetMinReps = 8,
            targetMaxReps = 12
        )
        assertTrue(suggestionMaintain is ProgressionSuggestion.MaintainLoad)
    }

    @Test
    fun testTrainingEngineRecommendation() {
        // 4-day intermediate user -> Upper/Lower recommended
        val rec4Day = TrainingEngine.recommendTrainingSystem(
            experience = TrainingExperience.INTERMEDIATE,
            goal = FitnessGoal.FAT_LOSS_AND_MUSCLE_GAIN,
            targetDays = 4,
            equipment = EquipmentAvailable.FULL_GYM
        )
        assertEquals(SplitSystemType.UPPER_LOWER, rec4Day.recommendedSystem)
        assertEquals(4, rec4Day.recommendedWeeklyDays)
        assertTrue(rec4Day.explanation.isNotBlank())

        // 3-day beginner -> Full Body recommended
        val rec3Day = TrainingEngine.recommendTrainingSystem(
            experience = TrainingExperience.BEGINNER,
            goal = FitnessGoal.GENERAL_FITNESS,
            targetDays = 3,
            equipment = EquipmentAvailable.BARBELL_DUMBBELLS
        )
        assertEquals(SplitSystemType.FULL_BODY, rec3Day.recommendedSystem)
    }

    @Test
    fun testMediaEngineFallbackPriority() {
        val testEx = Exercise(
            id = "bench",
            name = "Bench Press",
            primaryMuscle = MuscleGroup.CHEST,
            movementPattern = MovementPattern.HORIZONTAL_PUSH
        )

        // 1. With Video
        val videoMedia = ExerciseMedia("v1", "bench", MediaType.VIDEO, url = "https://test.com/v.mp4", isPrimary = true)
        val imageMedia = ExerciseMedia("i1", "bench", MediaType.IMAGE, url = "https://test.com/i.jpg", isPrimary = false)

        val resolvedVideo = MediaEngine.resolvePrimaryDisplayMedia(listOf(videoMedia, imageMedia), testEx)
        assertTrue(resolvedVideo is ResolvedMediaDisplay.VideoMedia)

        // 2. Without Video but with GIF
        val gifMedia = ExerciseMedia("g1", "bench", MediaType.GIF, url = "https://test.com/g.gif", isPrimary = true)
        val resolvedGif = MediaEngine.resolvePrimaryDisplayMedia(listOf(gifMedia, imageMedia), testEx)
        assertTrue(resolvedGif is ResolvedMediaDisplay.AnimationMedia)

        // 3. With Image only
        val resolvedImage = MediaEngine.resolvePrimaryDisplayMedia(listOf(imageMedia), testEx)
        assertTrue(resolvedImage is ResolvedMediaDisplay.ImageMedia)

        // 4. Empty media -> Resilient Placeholder
        val resolvedPlaceholder = MediaEngine.resolvePrimaryDisplayMedia(emptyList(), testEx)
        assertTrue(resolvedPlaceholder is ResolvedMediaDisplay.Placeholder)
    }

    @Test
    fun testExerciseAlternativesRanking() {
        val bench = Exercise(
            id = "bench",
            name = "Bench Press",
            primaryMuscle = MuscleGroup.CHEST,
            movementPattern = MovementPattern.HORIZONTAL_PUSH,
            equipment = EquipmentAvailable.BARBELL_DUMBBELLS,
            difficulty = Difficulty.INTERMEDIATE
        )

        val dbBench = Exercise(
            id = "db_bench",
            name = "Dumbbell Bench Press",
            primaryMuscle = MuscleGroup.CHEST,
            movementPattern = MovementPattern.HORIZONTAL_PUSH,
            equipment = EquipmentAvailable.DUMBBELLS_ONLY,
            difficulty = Difficulty.INTERMEDIATE
        )

        val squat = Exercise(
            id = "squat",
            name = "Back Squat",
            primaryMuscle = MuscleGroup.LEGS,
            movementPattern = MovementPattern.SQUAT,
            equipment = EquipmentAvailable.BARBELL_DUMBBELLS,
            difficulty = Difficulty.INTERMEDIATE
        )

        val ranked = AlternativesEngine.rankAlternatives(bench, listOf(dbBench, squat))
        assertEquals(1, ranked.size)
        assertEquals("db_bench", ranked.first().first.id)
        assertTrue(ranked.first().second >= 70)
    }

    @Test
    fun testProgressionEngineDeclineHandling() {
        val previousSets = listOf(
            ExerciseSet("p1", "we1", "s1", "bench", 1, 80.0, 10, 2),
            ExerciseSet("p2", "we1", "s1", "bench", 2, 80.0, 10, 2)
        )
        // Current session drops in weight significantly
        val currentDecliningSets = listOf(
            ExerciseSet("c1", "we2", "s2", "bench", 1, 70.0, 8, 1),
            ExerciseSet("c2", "we2", "s2", "bench", 2, 70.0, 7, 0)
        )

        val suggestion = ProgressionEngine.evaluateProgression(
            currentSets = currentDecliningSets,
            previousSessionSets = previousSets,
            targetMinReps = 8,
            targetMaxReps = 12
        )
        assertTrue("Declining performance must trigger deload/recovery review", suggestion is ProgressionSuggestion.DeloadOrReviewRecovery)
        assertFalse("Declining performance must never trigger load increase", suggestion is ProgressionSuggestion.IncreaseLoad)
    }

    @Test
    fun testProgressionSummaryTrendAndMetrics() {
        val session1 = listOf(
            ExerciseSet("1", "we1", "s1", "bench", 1, 80.0, 8, 2),
            ExerciseSet("2", "we1", "s1", "bench", 2, 80.0, 8, 2)
        )
        val session2 = listOf(
            ExerciseSet("3", "we2", "s2", "bench", 1, 82.5, 10, 2),
            ExerciseSet("4", "we2", "s2", "bench", 2, 82.5, 9, 2)
        )

        val summary = ProgressionEngine.generateProgressionSummary(
            exerciseId = "bench",
            exerciseName = "Bench Press",
            sessionHistory = listOf(session2, session1),
            targetMinReps = 8,
            targetMaxReps = 12,
            isArabic = true
        )

        assertEquals("bench", summary.exerciseId)
        assertEquals(ProgressionTrend.IMPROVED, summary.trend)
        assertTrue(summary.lastSessionSummary.contains("82.5"))
        assertTrue(summary.previousSessionSummary.contains("80.0"))
        assertTrue(summary.suggestedNextStep.isNotBlank())
    }

    @Test
    fun testAdminAuthServiceRestrictedAccess() {
        val service: com.example.formax.domain.auth.AdminAuthService =
            com.example.formax.domain.auth.DefaultAdminAuthService()

        // Valid debug access check
        kotlinx.coroutines.runBlocking {
            val devResult = service.requestDevelopmentAccess()
            // In unit tests, BuildConfig.DEBUG is true by default
            if (service.isDevelopmentEnvironment()) {
                assertTrue(devResult is com.example.formax.domain.auth.AdminAuthResult.Authenticated)
                assertEquals(UserRole.ADMIN, (devResult as com.example.formax.domain.auth.AdminAuthResult.Authenticated).role)
            }

            // Remote token rejection for invalid token
            val invalidTokenResult = service.authenticateWithRemoteToken("fake_token_123")
            assertTrue(invalidTokenResult is com.example.formax.domain.auth.AdminAuthResult.Denied)

            // Remote token acceptance for valid architected token
            val validTokenResult = service.authenticateWithRemoteToken("admin_secure_key_valid")
            assertTrue(validTokenResult is com.example.formax.domain.auth.AdminAuthResult.Authenticated)
        }
    }
}
