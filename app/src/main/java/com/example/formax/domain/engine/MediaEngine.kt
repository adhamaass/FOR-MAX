package com.example.formax.domain.engine

import com.example.formax.domain.models.Exercise
import com.example.formax.domain.models.ExerciseAlternative
import com.example.formax.domain.models.ExerciseMedia
import com.example.formax.domain.models.MediaType

sealed class ResolvedMediaDisplay {
    data class VideoMedia(val media: ExerciseMedia) : ResolvedMediaDisplay()
    data class AnimationMedia(val media: ExerciseMedia) : ResolvedMediaDisplay()
    data class ImageMedia(val media: ExerciseMedia) : ResolvedMediaDisplay()
    data class Placeholder(val exerciseName: String, val primaryMuscle: String) : ResolvedMediaDisplay()
}

data class MediaValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList(),
    val warnings: List<String> = emptyList()
)

object MediaEngine {

    /**
     * Fallback resolution priority:
     * 1. Video
     * 2. Animation/GIF
     * 3. Image
     * 4. Placeholder
     * Never crashes on missing or malformed media.
     */
    fun resolvePrimaryDisplayMedia(
        mediaList: List<ExerciseMedia>,
        exercise: Exercise
    ): ResolvedMediaDisplay {
        if (mediaList.isEmpty()) {
            return ResolvedMediaDisplay.Placeholder(exercise.name, exercise.primaryMuscle.displayName)
        }

        // Try Video first (prefer primary, then sorted)
        val videos = mediaList.filter { it.type == MediaType.VIDEO && (it.url.isNotBlank() || it.localPath.isNotBlank()) }
        val primaryVideo = videos.firstOrNull { it.isPrimary } ?: videos.firstOrNull()
        if (primaryVideo != null) {
            return ResolvedMediaDisplay.VideoMedia(primaryVideo)
        }

        // Try GIF/Animation second
        val gifs = mediaList.filter { it.type == MediaType.GIF && (it.url.isNotBlank() || it.localPath.isNotBlank()) }
        val primaryGif = gifs.firstOrNull { it.isPrimary } ?: gifs.firstOrNull()
        if (primaryGif != null) {
            return ResolvedMediaDisplay.AnimationMedia(primaryGif)
        }

        // Try Image third
        val images = mediaList.filter {
            (it.type == MediaType.IMAGE || it.type == MediaType.THUMBNAIL) &&
                (it.url.isNotBlank() || it.localPath.isNotBlank())
        }
        val primaryImage = images.firstOrNull { it.isPrimary } ?: images.firstOrNull()
        if (primaryImage != null) {
            return ResolvedMediaDisplay.ImageMedia(primaryImage)
        }

        // Fallback to placeholder
        return ResolvedMediaDisplay.Placeholder(exercise.name, exercise.primaryMuscle.displayName)
    }

    /**
     * Validates media for Admin Quality checks
     */
    fun validateMedia(media: ExerciseMedia): MediaValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        if (media.title.isBlank()) {
            warnings.add("Media title is empty.")
        }

        if (media.url.isBlank() && media.localPath.isBlank()) {
            errors.add("Neither URL nor local asset path is specified.")
        }

        if (media.url.isNotBlank()) {
            val isHttp = media.url.startsWith("http://") || media.url.startsWith("https://")
            val isContent = media.url.startsWith("content://") || media.url.startsWith("file://")
            if (!isHttp && !isContent) {
                errors.add("URL must start with https://, http://, content://, or file://")
            }
        }

        return MediaValidationResult(
            isValid = errors.isEmpty(),
            errors = errors,
            warnings = warnings
        )
    }
}

object AlternativesEngine {

    /**
     * Ranks potential alternatives:
     * - Same primary muscle (+40 pts)
     * - Same movement pattern (+30 pts)
     * - Similar equipment (+20 pts)
     * - Similar difficulty (+10 pts)
     */
    fun calculateSimilarity(base: Exercise, candidate: Exercise): Int {
        if (base.id == candidate.id) return 0
        var score = 0
        if (base.primaryMuscle == candidate.primaryMuscle) score += 40
        if (base.movementPattern == candidate.movementPattern) score += 30
        if (base.equipment == candidate.equipment) score += 20
        if (base.difficulty == candidate.difficulty) score += 10
        return score.coerceIn(0, 100)
    }

    fun rankAlternatives(
        baseExercise: Exercise,
        allExercises: List<Exercise>,
        explicitAlternativeIds: List<String> = emptyList()
    ): List<Pair<Exercise, Int>> {
        val candidates = allExercises.filter { it.id != baseExercise.id }

        return candidates.map { candidate ->
            val isExplicit = explicitAlternativeIds.contains(candidate.id)
            val baseScore = calculateSimilarity(baseExercise, candidate)
            val finalScore = if (isExplicit) (baseScore + 15).coerceAtMost(100) else baseScore
            Pair(candidate, finalScore)
        }
            .filter { it.second >= 40 }
            .sortedByDescending { it.second }
    }
}
