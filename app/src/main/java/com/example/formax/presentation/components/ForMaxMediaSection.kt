package com.example.formax.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.formax.domain.engine.MediaEngine
import com.example.formax.domain.engine.ResolvedMediaDisplay
import com.example.formax.domain.models.Exercise
import com.example.formax.domain.models.ExerciseMedia
import com.example.formax.domain.models.MediaType
import com.example.ui.theme.*
import kotlinx.coroutines.delay

import android.net.Uri
import android.widget.VideoView
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ForMaxMediaSection(
    exercise: Exercise,
    mediaList: List<ExerciseMedia>,
    modifier: Modifier = Modifier
) {
    val videos = remember(mediaList) { mediaList.filter { it.type == MediaType.VIDEO } }
    val gifs = remember(mediaList) { mediaList.filter { it.type == MediaType.GIF } }
    val images = remember(mediaList) { mediaList.filter { it.type == MediaType.IMAGE || it.type == MediaType.THUMBNAIL } }

    var selectedMediaType by remember(mediaList) {
        val hasVideo = videos.isNotEmpty()
        val hasGif = gifs.isNotEmpty()
        val hasImage = images.isNotEmpty()
        mutableStateOf(
            when {
                hasVideo -> MediaType.VIDEO
                hasGif -> MediaType.GIF
                hasImage -> MediaType.IMAGE
                else -> MediaType.IMAGE
            }
        )
    }

    var selectedImageIndex by remember { mutableIntStateOf(0) }
    var isMuted by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(true) }
    var playbackProgress by remember { mutableFloatStateOf(0f) }
    var currentDurationMs by remember { mutableIntStateOf(0) }
    var currentPositionMs by remember { mutableIntStateOf(0) }
    var isFullscreen by remember { mutableStateOf(false) }
    var videoPlaybackError by remember { mutableStateOf(false) }

    // Fallback handler if video playback fails
    val onVideoFailed = {
        videoPlaybackError = true
        selectedMediaType = when {
            gifs.isNotEmpty() -> MediaType.GIF
            images.isNotEmpty() -> MediaType.IMAGE
            else -> MediaType.IMAGE
        }
    }

    LaunchedEffect(isPlaying, selectedMediaType) {
        if (isPlaying && (selectedMediaType == MediaType.VIDEO || selectedMediaType == MediaType.GIF)) {
            while (true) {
                delay(300)
                playbackProgress = (playbackProgress + 0.04f).let { if (it > 1f) 0f else it }
            }
        }
    }

    val resolved = remember(mediaList, exercise) {
        MediaEngine.resolvePrimaryDisplayMedia(mediaList, exercise)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, ForMaxCardBorder, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
    ) {
        Column {
            // Media Viewport Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isFullscreen) 320.dp else 230.dp)
                    .background(Color(0xFF090B0D))
                    .testTag("exercise_media_viewport"),
                contentAlignment = Alignment.Center
            ) {
                when {
                    selectedMediaType == MediaType.VIDEO && videos.isNotEmpty() -> {
                        val currentVideo = videos.first()
                        VideoPlayerSurface(
                            video = currentVideo,
                            isPlaying = isPlaying,
                            isMuted = isMuted,
                            progress = playbackProgress,
                            onTogglePlay = { isPlaying = !isPlaying },
                            onToggleMute = { isMuted = !isMuted },
                            onReplay = { playbackProgress = 0f; isPlaying = true },
                            onToggleFullscreen = { isFullscreen = !isFullscreen },
                            onVideoError = {
                                if (gifs.isNotEmpty()) {
                                    selectedMediaType = MediaType.GIF
                                } else if (images.isNotEmpty()) {
                                    selectedMediaType = MediaType.IMAGE
                                }
                            }
                        )
                    }

                    selectedMediaType == MediaType.GIF && gifs.isNotEmpty() -> {
                        val currentGif = gifs.first()
                        GifAnimationSurface(
                            gif = currentGif,
                            isPlaying = isPlaying,
                            progress = playbackProgress,
                            onTogglePlay = { isPlaying = !isPlaying },
                            onReplay = { playbackProgress = 0f; isPlaying = true }
                        )
                    }

                    selectedMediaType == MediaType.IMAGE && images.isNotEmpty() -> {
                        val currentImg = images.getOrNull(selectedImageIndex) ?: images.first()
                        ImageGallerySurface(
                            image = currentImg,
                            exerciseName = exercise.name
                        )
                    }

                    else -> {
                        ExerciseBiomechanicsPlaceholder(
                            exerciseName = exercise.name,
                            primaryMuscle = exercise.primaryMuscle.displayNameAr,
                            movementPattern = exercise.movementPattern.displayNameAr
                        )
                    }
                }

                // Top Floating Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(8.dp))
                        .border(0.5.dp, ForMaxCardBorder, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(ForMaxElectricGreen)
                        )
                        Text(
                            text = when (selectedMediaType) {
                                MediaType.VIDEO -> "فيديو شرح التكنيك 4K"
                                MediaType.GIF -> "حركة متحركة متكررة"
                                MediaType.IMAGE -> "معرض زوايا الأداء"
                                else -> "توضيح حركي فور ماكس"
                            },
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Media Tab Selectors
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ForMaxSurfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (videos.isNotEmpty()) {
                        MediaTabChip(
                            label = "فيديو",
                            icon = Icons.Default.PlayCircle,
                            isSelected = selectedMediaType == MediaType.VIDEO,
                            onClick = { selectedMediaType = MediaType.VIDEO; isPlaying = true }
                        )
                    }
                    if (gifs.isNotEmpty()) {
                        MediaTabChip(
                            label = "حركة متكررة",
                            icon = Icons.Default.Loop,
                            isSelected = selectedMediaType == MediaType.GIF,
                            onClick = { selectedMediaType = MediaType.GIF; isPlaying = true }
                        )
                    }
                    if (images.isNotEmpty()) {
                        MediaTabChip(
                            label = "صور (${images.size})",
                            icon = Icons.Default.PhotoLibrary,
                            isSelected = selectedMediaType == MediaType.IMAGE,
                            onClick = { selectedMediaType = MediaType.IMAGE }
                        )
                    }
                }

                Text(
                    text = exercise.movementPattern.displayNameAr,
                    color = ForMaxTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Image Thumbnails Gallery
            if (selectedMediaType == MediaType.IMAGE && images.size > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    images.forEachIndexed { index, img ->
                        val isSel = index == selectedImageIndex
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    width = if (isSel) 1.5.dp else 1.dp,
                                    color = if (isSel) ForMaxElectricGreen else ForMaxCardBorder,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedImageIndex = index },
                            color = if (isSel) ForMaxElectricGreen.copy(alpha = 0.15f) else ForMaxSurfaceElevated
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(4.dp)) {
                                Text(
                                    text = img.title.ifBlank { "زاوية ${index + 1}" },
                                    color = if (isSel) ForMaxElectricGreen else ForMaxTextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VideoPlayerSurface(
    video: ExerciseMedia,
    isPlaying: Boolean,
    isMuted: Boolean,
    progress: Float,
    onTogglePlay: () -> Unit,
    onToggleMute: () -> Unit,
    onReplay: () -> Unit,
    onToggleFullscreen: () -> Unit,
    onVideoError: () -> Unit = {}
) {
    var videoViewRef by remember { mutableStateOf<android.widget.VideoView?>(null) }
    var mediaPlayerRef by remember { mutableStateOf<android.media.MediaPlayer?>(null) }
    var hasError by remember { mutableStateOf(false) }

    val videoUri = remember(video) {
        val path = video.url.ifBlank { video.localPath }
        if (path.isNotBlank()) {
            try {
                android.net.Uri.parse(path)
            } catch (e: Exception) {
                null
            }
        } else null
    }

    LaunchedEffect(isPlaying) {
        videoViewRef?.let { vv ->
            if (isPlaying) {
                if (!vv.isPlaying) vv.start()
            } else {
                if (vv.isPlaying) vv.pause()
            }
        }
    }

    LaunchedEffect(isMuted) {
        mediaPlayerRef?.let { mp ->
            val vol = if (isMuted) 0f else 1f
            mp.setVolume(vol, vol)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (videoUri != null && !hasError) {
            AndroidView(
                factory = { ctx ->
                    android.widget.VideoView(ctx).apply {
                        setVideoURI(videoUri)
                        setOnPreparedListener { mp ->
                            mediaPlayerRef = mp
                            mp.isLooping = true
                            val vol = if (isMuted) 0f else 1f
                            mp.setVolume(vol, vol)
                            if (isPlaying) start()
                        }
                        setOnErrorListener { _, _, _ ->
                            hasError = true
                            onVideoError()
                            true
                        }
                        videoViewRef = this
                    }
                },
                update = { vv ->
                    videoViewRef = vv
                    if (isPlaying && !vv.isPlaying) {
                        vv.start()
                    } else if (!isPlaying && vv.isPlaying) {
                        vv.pause()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Elegant athletic coaching placeholder when video is loading or fallback
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF141920), Color(0xFF0B0D10))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = ForMaxElectricGreen.copy(alpha = 0.85f),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = video.title.ifBlank { "تحليل احترافي للتكنيك الرياضي" },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "شرح المسار الحركي والإرشادات البيوميكانيكية للوقاية والبناء",
                        color = ForMaxTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Overlay Controls Bottom Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                    )
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = ForMaxElectricGreen,
                trackColor = Color.White.copy(alpha = 0.2f),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onTogglePlay, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "إيقاف مؤقت" else "تشغيل",
                            tint = ForMaxElectricGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(onClick = onReplay, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "إعادة التشغيل",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    val totalDuration = if (video.durationSeconds > 0) video.durationSeconds else 48
                    val currentSec = (progress * totalDuration).toInt()
                    Text(
                        text = "0:${currentSec.toString().padStart(2, '0')} / 0:${totalDuration.toString().padStart(2, '0')}",
                        color = ForMaxTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onToggleMute, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = if (isMuted) "إلغاء كتم الصوت" else "كتم الصوت",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(onClick = onToggleFullscreen, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Fullscreen,
                            contentDescription = "ملء الشاشة",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GifAnimationSurface(
    gif: ExerciseMedia,
    isPlaying: Boolean,
    progress: Float,
    onTogglePlay: () -> Unit,
    onReplay: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F141A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(ForMaxElectricGreen.copy(alpha = 0.1f))
                    .border(2.dp, ForMaxElectricGreen.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Repeat,
                    contentDescription = null,
                    tint = ForMaxElectricGreen,
                    modifier = Modifier.size(44.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = gif.title.ifBlank { "عرض متواصل للحركة" },
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "حلقة مستمرة: وضع البداية ← مدى الحركة الكامل ← الانقباض العضلي",
                color = ForMaxTextSecondary,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onTogglePlay, modifier = Modifier.size(30.dp)) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = ForMaxElectricGreen,
                    modifier = Modifier.size(16.dp)
                )
            }
            IconButton(onClick = onReplay, modifier = Modifier.size(30.dp)) {
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun ImageGallerySurface(
    image: ExerciseMedia,
    exerciseName: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101318)),
        contentAlignment = Alignment.Center
    ) {
        if (image.url.isNotBlank() && (image.url.startsWith("http") || image.url.startsWith("content"))) {
            AsyncImage(
                model = image.url,
                contentDescription = image.title,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = ForMaxElectricGreen,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = image.title.ifBlank { "معرض صور التمرين" },
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = image.description.ifBlank { "مرجع عالي الدقة لزاوية أداء $exerciseName" },
                    color = ForMaxTextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun ExerciseBiomechanicsPlaceholder(
    exerciseName: String,
    primaryMuscle: String,
    movementPattern: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF1B232E), Color(0xFF0B0D10))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(ForMaxElectricGreen.copy(alpha = 0.15f))
                    .border(1.5.dp, ForMaxElectricGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = ForMaxElectricGreen,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = exerciseName,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Text(
                text = "العضلة المستهدفة: $primaryMuscle • نمط الحركة: $movementPattern",
                color = ForMaxTextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun MediaTabChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) ForMaxElectricGreen else Color.Transparent,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF0B0D0F) else ForMaxTextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                color = if (isSelected) Color(0xFF0B0D0F) else ForMaxTextSecondary,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}
