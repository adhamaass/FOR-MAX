package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.formax.data.local.ExerciseSeedData
import com.example.formax.data.local.ForMaxDatabase
import com.example.formax.data.repository.ForMaxRepository
import com.example.formax.domain.models.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var database: ForMaxDatabase
    private lateinit var repository: ForMaxRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ForMaxDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ForMaxRepository(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("FOR MAX", appName)
    }

    @Test
    fun `test initial seed and database persistence`() = runBlocking {
        repository.ensureSeeded()

        val exercises = repository.getPublishedExercises().first()
        assertTrue("Database should have 50+ seeded exercises", exercises.size >= 50)

        val bench = repository.getExerciseByIdOnce("bench_press")
        assertNotNull(bench)
        assertEquals("Barbell Bench Press", bench?.name)
        assertEquals(MuscleGroup.CHEST, bench?.primaryMuscle)
        assertEquals(EquipmentAvailable.BARBELL_DUMBBELLS, bench?.equipment)

        // Verify media persistence
        val benchMedia = repository.getMediaForExercise("bench_press").first()
        assertTrue("Media should be attached", benchMedia.isNotEmpty())
    }

    @Test
    fun `test workout logging set by set and weekly report`() = runBlocking {
        repository.ensureSeeded()
        val allEx = repository.getPublishedExercises().first()

        val programExercise = ProgramExercise("bench_press", 1, targetSets = 3, minReps = 8, maxReps = 12)
        val sessionId = repository.startWorkout(
            programId = "upper_lower_4day",
            dayId = "ul_day1",
            programName = "Upper Body A",
            dayName = "Upper Body A",
            programExercises = listOf(programExercise),
            allExercises = allEx
        )

        assertNotNull(sessionId)

        // Log sets
        val set1 = ExerciseSet("s1", "we1", sessionId, "bench_press", 1, 80.0, 10, 2)
        val set2 = ExerciseSet("s2", "we1", sessionId, "bench_press", 2, 80.0, 10, 2)
        repository.logSet(set1)
        repository.logSet(set2)

        // Finish workout
        repository.finishWorkout(sessionId, "Great first session")

        val completed = repository.getCompletedSessions().first()
        assertEquals(1, completed.size)
        assertEquals(1600.0, completed.first().totalVolumeKg, 0.01)

        val weeklyReport = repository.generateWeeklyReport(4)
        assertEquals(1, weeklyReport.workoutsCompleted)
        assertEquals(25.0, weeklyReport.consistencyPercentage, 0.01)
    }

    @Test
    fun `test app reset clears user history but preserves master catalog`() = runBlocking {
        repository.ensureSeeded()
        val initialExCount = repository.getPublishedExercises().first().size
        assertTrue(initialExCount >= 50)

        // Seed demo user data
        repository.seedDemoWorkoutData()
        val demoSessions = repository.getCompletedSessions().first()
        assertTrue(demoSessions.isNotEmpty())

        // Production Reset
        repository.resetAppUserData()
        val postResetSessions = repository.getCompletedSessions().first()
        assertTrue("User sessions should be cleared", postResetSessions.isEmpty())

        val postResetEx = repository.getPublishedExercises().first()
        assertEquals("Master exercises must be preserved", initialExCount, postResetEx.size)

        val profile = repository.getUserProfile().first()
        assertFalse("Profile should be reset to not onboarded", profile.isOnboarded)
    }

    @Test
    fun `test published exercise visibility strictly excludes draft and archived`() = runBlocking {
        repository.ensureSeeded()

        val draftEx = Exercise(
            id = "test_draft_ex",
            name = "Draft Movement",
            primaryMuscle = MuscleGroup.CHEST,
            movementPattern = MovementPattern.HORIZONTAL_PUSH,
            status = ContentStatus.DRAFT
        )
        val archivedEx = Exercise(
            id = "test_archived_ex",
            name = "Archived Movement",
            primaryMuscle = MuscleGroup.BACK,
            movementPattern = MovementPattern.VERTICAL_PULL,
            status = ContentStatus.ARCHIVED
        )

        repository.saveExercise(draftEx)
        repository.saveExercise(archivedEx)

        val published = repository.getPublishedExercises().first()
        assertNull("Normal users must NOT see draft exercises", published.find { it.id == "test_draft_ex" })
        assertNull("Normal users must NOT see archived exercises", published.find { it.id == "test_archived_ex" })

        val allForAdmin = repository.getAllExercisesForAdmin().first()
        assertNotNull("Admin must be able to see draft exercises", allForAdmin.find { it.id == "test_draft_ex" })
        assertNotNull("Admin must be able to see archived exercises", allForAdmin.find { it.id == "test_archived_ex" })
    }

    @Test
    fun `test language preference persistence across restarts`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = context.getSharedPreferences("formax_prefs", Context.MODE_PRIVATE)

        // Save English
        prefs.edit().putString("pref_language", AppLanguage.ENGLISH.code).apply()
        val loadedCode = prefs.getString("pref_language", "ar")
        val restoredLanguage = AppLanguage.fromCode(loadedCode ?: "ar")

        assertEquals(AppLanguage.ENGLISH, restoredLanguage)
        assertFalse(restoredLanguage.isRtl)

        // Switch to Arabic
        prefs.edit().putString("pref_language", AppLanguage.ARABIC.code).apply()
        val restoredArabic = AppLanguage.fromCode(prefs.getString("pref_language", "en") ?: "en")
        assertEquals(AppLanguage.ARABIC, restoredArabic)
        assertTrue(restoredArabic.isRtl)
    }
}
