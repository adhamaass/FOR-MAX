package com.example.formax.domain.engine

import com.example.formax.domain.models.ExerciseProgressionSummary
import com.example.formax.domain.models.ExerciseSet
import com.example.formax.domain.models.ProgressionTrend
import kotlin.math.roundToInt

sealed class ProgressionSuggestion {
    abstract val message: String
    abstract val messageEn: String

    data class IncreaseLoad(
        override val message: String,
        override val messageEn: String,
        val recommendedAdditionKg: Double = 2.5
    ) : ProgressionSuggestion()

    data class MaintainLoad(
        override val message: String,
        override val messageEn: String
    ) : ProgressionSuggestion()

    data class DeloadOrReviewRecovery(
        override val message: String,
        override val messageEn: String
    ) : ProgressionSuggestion()

    data class FirstTimeGuidance(
        override val message: String = "اختر وزناً مريحاً يتيح لك أداء الحركة بتحكم كامل.",
        override val messageEn: String = "Choose a comfortable starting weight that allows full movement control."
    ) : ProgressionSuggestion()
}

object ProgressionEngine {

    /**
     * Set volume = weight * reps
     */
    fun calculateSetVolume(weightKg: Double, reps: Int): Double {
        return (weightKg * reps * 10.0).roundToInt() / 10.0
    }

    /**
     * Exercise volume = sum of all completed set volumes
     */
    fun calculateExerciseVolume(sets: List<ExerciseSet>): Double {
        return sets.filter { it.isCompleted }.sumOf { calculateSetVolume(it.weightKg, it.reps) }
    }

    /**
     * Estimated 1 Rep Max using the scientific Epley formula: weight * (1 + reps / 30)
     */
    fun calculateEstimated1RM(weightKg: Double, reps: Int): Double {
        if (reps <= 0) return 0.0
        if (reps == 1) return weightKg
        val e1rm = weightKg * (1.0 + (reps.toDouble() / 30.0))
        return (e1rm * 10.0).roundToInt() / 10.0
    }

    /**
     * Deterministic double-progression engine.
     * Evaluates performance against target rep range and RIR.
     * NEVER invents a starting weight.
     */
    fun evaluateProgression(
        currentSets: List<ExerciseSet>,
        previousSessionSets: List<ExerciseSet>?,
        targetMinReps: Int,
        targetMaxReps: Int,
        targetRir: Int = 2
    ): ProgressionSuggestion {
        val completedSets = currentSets.filter { it.isCompleted }

        // First time exercise session
        if (completedSets.isEmpty() && (previousSessionSets == null || previousSessionSets.isEmpty())) {
            return ProgressionSuggestion.FirstTimeGuidance()
        }

        if (completedSets.isEmpty() && previousSessionSets != null && previousSessionSets.isNotEmpty()) {
            return evaluateSetsAgainstTarget(previousSessionSets, targetMinReps, targetMaxReps, targetRir)
        }

        return evaluateSetsAgainstTarget(completedSets, targetMinReps, targetMaxReps, targetRir, previousSessionSets)
    }

    /**
     * Evaluates multi-session history (considering the last 2-3 sessions) for double progression.
     */
    fun evaluateMultiSessionHistory(
        sessionHistory: List<List<ExerciseSet>>,
        targetMinReps: Int,
        targetMaxReps: Int,
        targetRir: Int = 2
    ): ProgressionSuggestion {
        if (sessionHistory.isEmpty()) {
            return ProgressionSuggestion.FirstTimeGuidance()
        }

        val validSessions = sessionHistory.map { session -> session.filter { it.isCompleted } }
            .filter { it.isNotEmpty() }

        if (validSessions.isEmpty()) {
            return ProgressionSuggestion.FirstTimeGuidance()
        }

        val latest = validSessions.first()
        val previous = validSessions.getOrNull(1)

        // If we have 3 sessions, check for repeated decline trend
        if (validSessions.size >= 3) {
            val s1Avg = validSessions[0].map { it.weightKg }.average()
            val s2Avg = validSessions[1].map { it.weightKg }.average()
            val s3Avg = validSessions[2].map { it.weightKg }.average()

            if (s1Avg < s2Avg && s2Avg <= s3Avg) {
                return ProgressionSuggestion.DeloadOrReviewRecovery(
                    message = "الأداء في اتجاه تنازلي عبر عدة جلسات — راجع جودة الاستشفاء وساعات النوم والتغذية.",
                    messageEn = "Performance is declining across multiple sessions — review recovery, sleep, and nutrition."
                )
            }
        }

        return evaluateSetsAgainstTarget(latest, targetMinReps, targetMaxReps, targetRir, previous)
    }

    private fun evaluateSetsAgainstTarget(
        sets: List<ExerciseSet>,
        targetMinReps: Int,
        targetMaxReps: Int,
        targetRir: Int,
        previousSets: List<ExerciseSet>? = null
    ): ProgressionSuggestion {
        val completed = sets.filter { it.isCompleted }
        if (completed.isEmpty()) {
            return ProgressionSuggestion.FirstTimeGuidance()
        }

        // Check if performance is declining compared to previous
        if (previousSets != null && previousSets.isNotEmpty()) {
            val prevAvgReps = previousSets.map { it.reps }.average()
            val curAvgReps = completed.map { it.reps }.average()
            val prevMaxWeight = previousSets.maxOfOrNull { it.weightKg } ?: 0.0
            val curMaxWeight = completed.maxOfOrNull { it.weightKg } ?: 0.0

            if (curMaxWeight < prevMaxWeight || (curMaxWeight == prevMaxWeight && curAvgReps < prevAvgReps - 2.5)) {
                return ProgressionSuggestion.DeloadOrReviewRecovery(
                    message = "الأداء في اتجاه تنازلي — راجع جودة الاستشفاء وساعات النوم والتغذية.",
                    messageEn = "Performance is declining — review recovery, sleep, and nutrition."
                )
            }
        }

        // Double progression: If all sets hit or exceed the top of the rep range with safe RIR
        val allHitTopRange = completed.all { it.reps >= targetMaxReps }
        val goodEffort = completed.all { it.rir >= 1 }

        if (allHitTopRange && goodEffort) {
            val suggestedBump = if ((completed.firstOrNull()?.weightKg ?: 0.0) >= 60.0) 2.5 else 1.25
            return ProgressionSuggestion.IncreaseLoad(
                message = "اقتراح التطور: فكر في زيادة بسيطة في الوزن (+${suggestedBump} كجم) للجلسة القادمة.",
                messageEn = "Progression Suggestion: Consider a small weight increase (+${suggestedBump} kg) for the next session.",
                recommendedAdditionKg = suggestedBump
            )
        }

        // If performing solidly within rep range
        val allInTargetRange = completed.all { it.reps >= targetMinReps }
        if (allInTargetRange) {
            return ProgressionSuggestion.MaintainLoad(
                message = "حافظ على الوزن الحالي وركز على التدرج للوصول لـ ${targetMaxReps} تكرار مع التحكم الكامل.",
                messageEn = "Keep current weight and focus on hitting ${targetMaxReps} reps with strict control."
            )
        }

        // Slightly below min reps
        return ProgressionSuggestion.MaintainLoad(
            message = "حافظ على الوزن الحالي وركز على التكنيك لتحقيق ما لا يقل عن ${targetMinReps} تكرارات.",
            messageEn = "Keep current load and focus on technique to achieve at least ${targetMinReps} reps."
        )
    }

    /**
     * Generates a complete progression history summary for an exercise (Requirement 12).
     */
    fun generateProgressionSummary(
        exerciseId: String,
        exerciseName: String,
        sessionHistory: List<List<ExerciseSet>>,
        targetMinReps: Int = 8,
        targetMaxReps: Int = 12,
        isArabic: Boolean = true
    ): ExerciseProgressionSummary {
        val completedSessions = sessionHistory
            .map { session -> session.filter { it.isCompleted } }
            .filter { it.isNotEmpty() }

        val latest = completedSessions.getOrNull(0)
        val previous = completedSessions.getOrNull(1)

        val bestOverallSet = completedSessions.flatten().maxByOrNull { it.weightKg }

        val lastSummary = if (latest != null) {
            val maxW = latest.maxOf { it.weightKg }
            val avgR = (latest.map { it.reps }.average() * 10).roundToInt() / 10.0
            if (isArabic) "$maxW كجم × $avgR تكرار (${latest.size} جولات)"
            else "$maxW kg × $avgR reps (${latest.size} sets)"
        } else {
            if (isArabic) "لا توجد جلسات مسجلة" else "No sessions recorded"
        }

        val prevSummary = if (previous != null) {
            val maxW = previous.maxOf { it.weightKg }
            val avgR = (previous.map { it.reps }.average() * 10).roundToInt() / 10.0
            if (isArabic) "$maxW كجم × $avgR تكرار"
            else "$maxW kg × $avgR reps"
        } else {
            if (isArabic) "—" else "—"
        }

        val bestSummary = if (bestOverallSet != null) {
            val e1rm = calculateEstimated1RM(bestOverallSet.weightKg, bestOverallSet.reps).toInt()
            if (isArabic) "${bestOverallSet.weightKg} كجم × ${bestOverallSet.reps} (1RM: $e1rm كجم)"
            else "${bestOverallSet.weightKg} kg × ${bestOverallSet.reps} (1RM: $e1rm kg)"
        } else {
            if (isArabic) "—" else "—"
        }

        val currentSummary = lastSummary

        // Determine trend
        val trend = if (latest != null && previous != null) {
            val curAvgW = latest.map { it.weightKg }.average()
            val prevAvgW = previous.map { it.weightKg }.average()
            val curAvgR = latest.map { it.reps }.average()
            val prevAvgR = previous.map { it.reps }.average()

            if (curAvgW > prevAvgW || (curAvgW == prevAvgW && curAvgR > prevAvgR)) {
                ProgressionTrend.IMPROVED
            } else if (curAvgW < prevAvgW || (curAvgW == prevAvgW && curAvgR < prevAvgR - 1.5)) {
                ProgressionTrend.DECLINING
            } else {
                ProgressionTrend.STABLE
            }
        } else {
            ProgressionTrend.STABLE
        }

        val suggestion = evaluateProgression(
            currentSets = latest ?: emptyList(),
            previousSessionSets = previous,
            targetMinReps = targetMinReps,
            targetMaxReps = targetMaxReps
        )

        val nextStep = if (isArabic) suggestion.message else suggestion.messageEn

        return ExerciseProgressionSummary(
            exerciseId = exerciseId,
            exerciseName = exerciseName,
            lastSessionSummary = lastSummary,
            previousSessionSummary = prevSummary,
            bestPerformanceSummary = bestSummary,
            currentPerformanceSummary = currentSummary,
            trend = trend,
            suggestedNextStep = nextStep
        )
    }
}
