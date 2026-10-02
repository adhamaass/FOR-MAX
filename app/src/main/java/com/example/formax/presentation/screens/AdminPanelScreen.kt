package com.example.formax.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formax.data.repository.AdminMetrics
import com.example.formax.domain.engine.MediaEngine
import com.example.formax.domain.models.*
import com.example.formax.presentation.components.ForMaxMediaSection
import com.example.ui.theme.*
import java.util.UUID

@Composable
fun AdminPanelScreen(
    exercises: List<Exercise>,
    allMedia: List<ExerciseMedia>,
    programs: List<TrainingProgram>,
    adminMetrics: AdminMetrics,
    onSaveExercise: (Exercise) -> Unit,
    onDeleteExercise: (String) -> Unit,
    onSaveMedia: (ExerciseMedia) -> Unit,
    onDeleteMedia: (String) -> Unit,
    onExitAdmin: () -> Unit
) {
    var adminTab by remember { mutableIntStateOf(0) }
    // 0: Dashboard, 1: Exercises CRUD, 2: Media Manager, 3: Quality Checker, 4: Analytics

    var editingExercise by remember { mutableStateOf<Exercise?>(null) }
    var previewingExercise by remember { mutableStateOf<Exercise?>(null) }
    var addingMediaForExerciseId by remember { mutableStateOf<String?>(null) }
    var adminFilter by remember { mutableStateOf("ALL") }
    var adminSearch by remember { mutableStateOf("") }

    // Preview Dialog
    val currentPreview = previewingExercise
    if (currentPreview != null) {
        val exMedia = remember(currentPreview, allMedia) {
            allMedia.filter { it.exerciseId == currentPreview.id }
        }
        AlertDialog(
            onDismissRequest = { previewingExercise = null },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("معاينة: ${currentPreview.name}", color = ForMaxElectricGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Surface(
                        color = if (currentPreview.status == ContentStatus.PUBLISHED) ForMaxElectricGreen.copy(alpha = 0.2f) else ForMaxWarning.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (currentPreview.status == ContentStatus.PUBLISHED) "منشور" else "مسودة",
                            color = if (currentPreview.status == ContentStatus.PUBLISHED) ForMaxElectricGreen else ForMaxWarning,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ForMaxMediaSection(exercise = currentPreview, mediaList = exMedia)

                    Text("التعليمات الفنية والتكنيك:", color = ForMaxElectricGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(currentPreview.instructions.ifBlank { "الإعداد: ${currentPreview.setupInstructions}\nالأداء: ${currentPreview.executionInstructions}" }, color = Color.White, fontSize = 12.sp)

                    Text("المعايير المحددة:", color = ForMaxElectricGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("الجولات: ${currentPreview.recommendedSets} | التكرارات: ${currentPreview.minReps}-${currentPreview.maxReps} | RIR: ${currentPreview.rirTarget} | الراحة: ${currentPreview.restTimeSeconds}ث", color = Color.White, fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSaveExercise(currentPreview.copy(status = ContentStatus.PUBLISHED))
                        previewingExercise = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen, contentColor = Color(0xFF0B0D0F))
                ) {
                    Text("نشر التمرين الآن", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { previewingExercise = null }) {
                    Text("إغلاق المعاينة", color = ForMaxTextSecondary)
                }
            },
            containerColor = ForMaxSurface,
            shape = RoundedCornerShape(18.dp)
        )
    }

    // Edit / Create Exercise Dialog
    val currentEditing = editingExercise
    if (currentEditing != null) {
        var formName by remember(currentEditing) { mutableStateOf(currentEditing.name) }
        var formPrimaryMuscle by remember(currentEditing) { mutableStateOf(currentEditing.primaryMuscle) }
        var formPattern by remember(currentEditing) { mutableStateOf(currentEditing.movementPattern) }
        var formEquipment by remember(currentEditing) { mutableStateOf(currentEditing.equipment) }
        var formDifficulty by remember(currentEditing) { mutableStateOf(currentEditing.difficulty) }
        var formType by remember(currentEditing) { mutableStateOf(currentEditing.exerciseType) }
        var formSets by remember(currentEditing) { mutableStateOf(currentEditing.recommendedSets.toString()) }
        var formMinReps by remember(currentEditing) { mutableStateOf(currentEditing.minReps.toString()) }
        var formMaxReps by remember(currentEditing) { mutableStateOf(currentEditing.maxReps.toString()) }
        var formRir by remember(currentEditing) { mutableStateOf(currentEditing.rirTarget.toString()) }
        var formRest by remember(currentEditing) { mutableStateOf(currentEditing.restTimeSeconds.toString()) }
        var formSetup by remember(currentEditing) { mutableStateOf(currentEditing.setupInstructions) }
        var formExecution by remember(currentEditing) { mutableStateOf(currentEditing.executionInstructions) }
        var formBreathing by remember(currentEditing) { mutableStateOf(currentEditing.breathingInstructions) }
        var formCues by remember(currentEditing) { mutableStateOf(currentEditing.coachingCues) }
        var formStatus by remember(currentEditing) { mutableStateOf(currentEditing.status) }

        AlertDialog(
            onDismissRequest = { editingExercise = null },
            title = {
                Text(
                    text = if (currentEditing.id.startsWith("new_")) "إضافة تمرين جديد" else "تعديل بيانات التمرين",
                    color = ForMaxElectricGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = formName,
                        onValueChange = { formName = it },
                        label = { Text("اسم التمرين") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForMaxElectricGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = formSets,
                            onValueChange = { formSets = it },
                            label = { Text("الجولات") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForMaxElectricGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        OutlinedTextField(
                            value = formMinReps,
                            onValueChange = { formMinReps = it },
                            label = { Text("أدنى تكرار") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForMaxElectricGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        OutlinedTextField(
                            value = formMaxReps,
                            onValueChange = { formMaxReps = it },
                            label = { Text("أقصى تكرار") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForMaxElectricGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = formRir,
                            onValueChange = { formRir = it },
                            label = { Text("معدل RIR") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForMaxElectricGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                        OutlinedTextField(
                            value = formRest,
                            onValueChange = { formRest = it },
                            label = { Text("الراحة (ث)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForMaxElectricGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                        )
                    }

                    OutlinedTextField(
                        value = formSetup,
                        onValueChange = { formSetup = it },
                        label = { Text("إرشادات الإعداد والوضعية") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForMaxElectricGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    OutlinedTextField(
                        value = formExecution,
                        onValueChange = { formExecution = it },
                        label = { Text("طريقة التنفيذ الحركي") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForMaxElectricGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    OutlinedTextField(
                        value = formBreathing,
                        onValueChange = { formBreathing = it },
                        label = { Text("تعليمات التنفس") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForMaxElectricGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    OutlinedTextField(
                        value = formCues,
                        onValueChange = { formCues = it },
                        label = { Text("نصائح وتنبيهات التدريب") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForMaxElectricGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    Text(text = "حالة النشر في التطبيق:", color = ForMaxTextSecondary, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ContentStatus.values().forEach { st ->
                            val isSel = st == formStatus
                            val stNameAr = when (st) {
                                ContentStatus.PUBLISHED -> "منشور"
                                ContentStatus.DRAFT -> "مسودة"
                                ContentStatus.ARCHIVED -> "مؤرشف"
                            }
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { formStatus = st },
                                color = if (isSel) ForMaxElectricGreen else ForMaxSurfaceElevated
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = stNameAr,
                                        color = if (isSel) Color(0xFF0B0D0F) else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val updated = currentEditing.copy(
                            name = formName,
                            primaryMuscle = formPrimaryMuscle,
                            movementPattern = formPattern,
                            equipment = formEquipment,
                            difficulty = formDifficulty,
                            exerciseType = formType,
                            recommendedSets = formSets.toIntOrNull() ?: 3,
                            minReps = formMinReps.toIntOrNull() ?: 8,
                            maxReps = formMaxReps.toIntOrNull() ?: 12,
                            rirTarget = formRir.toIntOrNull() ?: 2,
                            restTimeSeconds = formRest.toIntOrNull() ?: 120,
                            setupInstructions = formSetup,
                            executionInstructions = formExecution,
                            breathingInstructions = formBreathing,
                            coachingCues = formCues,
                            status = formStatus
                        )
                        onSaveExercise(updated)
                        editingExercise = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen, contentColor = Color(0xFF0B0D0F))
                ) {
                    Text("حفظ التمرين", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingExercise = null }) {
                    Text("إلغاء", color = ForMaxTextSecondary)
                }
            },
            containerColor = ForMaxSurface,
            shape = RoundedCornerShape(18.dp)
        )
    }

    // Add Media Dialog
    if (addingMediaForExerciseId != null) {
        var mediaType by remember { mutableStateOf(MediaType.VIDEO) }
        var mediaUrl by remember { mutableStateOf("") }
        var mediaTitle by remember { mutableStateOf("") }
        var isPrimary by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { addingMediaForExerciseId = null },
            title = {
                Text("إرفاق وسائط بالتمرين", color = ForMaxElectricGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("نوع الوسائط المرفقة:", color = ForMaxTextSecondary, fontSize = 11.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(MediaType.VIDEO, MediaType.GIF, MediaType.IMAGE).forEach { mt ->
                            val isSel = mt == mediaType
                            val mtNameAr = when (mt) {
                                MediaType.VIDEO -> "فيديو"
                                MediaType.GIF -> "حركة متحركة"
                                MediaType.IMAGE -> "صورة"
                                else -> mt.name
                            }
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { mediaType = mt },
                                color = if (isSel) ForMaxElectricGreen else ForMaxSurfaceElevated
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = mtNameAr,
                                        color = if (isSel) Color(0xFF0B0D0F) else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = mediaTitle,
                        onValueChange = { mediaTitle = it },
                        label = { Text("عنوان الوسائط أو الزاوية") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForMaxElectricGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    OutlinedTextField(
                        value = mediaUrl,
                        onValueChange = { mediaUrl = it },
                        label = { Text("رابط الفيديو أو الصورة أو المسار المحلي") },
                        placeholder = { Text("https://... أو assets/...") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForMaxElectricGreen, focusedTextColor = Color.White, unfocusedTextColor = Color.White)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isPrimary,
                            onCheckedChange = { isPrimary = it },
                            colors = CheckboxDefaults.colors(checkedColor = ForMaxElectricGreen)
                        )
                        Text("تعيين كوسائط رئيسية لهذا التمرين", color = Color.White, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val m = ExerciseMedia(
                            mediaId = UUID.randomUUID().toString(),
                            exerciseId = addingMediaForExerciseId!!,
                            type = mediaType,
                            url = mediaUrl,
                            title = mediaTitle,
                            isPrimary = isPrimary
                        )
                        onSaveMedia(m)
                        addingMediaForExerciseId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen, contentColor = Color(0xFF0B0D0F))
                ) {
                    Text("إرفاق الوسائط", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { addingMediaForExerciseId = null }) {
                    Text("إلغاء", color = ForMaxTextSecondary)
                }
            },
            containerColor = ForMaxSurface,
            shape = RoundedCornerShape(18.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForMaxBackground)
            .statusBarsPadding()
    ) {
        // Admin Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.Security, contentDescription = null, tint = ForMaxElectricGreen, modifier = Modifier.size(20.dp))
                Column {
                    Text("إدارة فور ماكس FOR MAX", color = ForMaxElectricGreen, fontSize = 11.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    Text("نظام إدارة المحتوى والتمارين", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }

            IconButton(
                onClick = onExitAdmin,
                modifier = Modifier
                    .size(36.dp)
                    .background(ForMaxSurfaceElevated, CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "إغلاق لوحة الإدارة", tint = Color.White)
            }
        }

        // Navigation Tabs (Dashboard, Exercises, Media, Quality Checker, Analytics)
        val adminTabScroll = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(adminTabScroll)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val tabs = listOf("لوحة التحكم", "التمارين (${exercises.size})", "الوسائط (${allMedia.size})", "فاحص الجودة", "التحليلات")
            tabs.forEachIndexed { index, tab ->
                val isSel = adminTab == index
                Surface(
                    modifier = Modifier
                        .height(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { adminTab = index },
                    color = if (isSel) ForMaxElectricGreen else ForMaxSurfaceElevated
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 12.dp)) {
                        Text(
                            text = tab,
                            color = if (isSel) Color(0xFF0B0D0F) else Color.White,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Black else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        when (adminTab) {
            0 -> {
                // Tab 0: Admin Dashboard
                val scroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scroll)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("نظرة عامة على المحتوى", color = ForMaxTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                    // Stats Grid
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AdminStatCard(title = "إجمالي التمارين", value = "${adminMetrics.totalExercises}", subtitle = "${adminMetrics.publishedExercises} تمرين منشور", modifier = Modifier.weight(1f))
                        AdminStatCard(title = "ملفات الوسائط", value = "${adminMetrics.totalMediaFiles}", subtitle = "فيديوهات، حركات، صور", modifier = Modifier.weight(1f))
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AdminStatCard(title = "تمارين بلا وسائط", value = "${adminMetrics.exercisesMissingMedia}", subtitle = "تحتاج فيديو أو صورة", color = if (adminMetrics.exercisesMissingMedia > 0) ForMaxWarning else ForMaxElectricGreen, modifier = Modifier.weight(1f))
                        AdminStatCard(title = "تمارين بلا بدائل", value = "${adminMetrics.exercisesMissingAlternatives}", subtitle = "تحتاج ربط بدائل", color = if (adminMetrics.exercisesMissingAlternatives > 0) ForMaxWarning else ForMaxElectricGreen, modifier = Modifier.weight(1f))
                    }

                    // Quick Actions
                    Card(
                        modifier = Modifier.fillMaxWidth().border(1.dp, ForMaxCardBorder, RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("إجراءات الإدارة السريعة", color = ForMaxElectricGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Button(
                                onClick = {
                                    editingExercise = Exercise(
                                        id = "new_${UUID.randomUUID()}",
                                        name = "",
                                        primaryMuscle = MuscleGroup.CHEST,
                                        movementPattern = MovementPattern.HORIZONTAL_PUSH,
                                        status = ContentStatus.DRAFT
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen, contentColor = Color(0xFF0B0D0F)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("إنشاء تمرين جديد", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            1 -> {
                // Tab 1: Exercise CRUD Management
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Filter Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = adminSearch,
                            onValueChange = { adminSearch = it },
                            placeholder = { Text("تصفية التمارين بالاسم...", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f).height(44.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ForMaxSurface,
                                unfocusedContainerColor = ForMaxSurface,
                                focusedBorderColor = ForMaxElectricGreen,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                editingExercise = Exercise(
                                    id = "new_${UUID.randomUUID()}",
                                    name = "",
                                    primaryMuscle = MuscleGroup.CHEST,
                                    movementPattern = MovementPattern.HORIZONTAL_PUSH,
                                    status = ContentStatus.DRAFT
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen, contentColor = Color(0xFF0B0D0F)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(44.dp)
                        ) {
                            Text("+ تمرين جديد", fontWeight = FontWeight.Bold)
                        }
                    }

                    val filtered = exercises.filter {
                        adminSearch.isBlank() || it.name.contains(adminSearch, ignoreCase = true)
                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(filtered) { ex ->
                            Card(
                                modifier = Modifier.fillMaxWidth().border(1.dp, ForMaxCardBorder, RoundedCornerShape(12.dp)),
                                colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text(text = ex.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            val statusText = when(ex.status) {
                                                ContentStatus.PUBLISHED -> "منشور"
                                                ContentStatus.DRAFT -> "مسودة"
                                                ContentStatus.ARCHIVED -> "مؤرشف"
                                            }
                                            Surface(
                                                color = if (ex.status == ContentStatus.PUBLISHED) ForMaxElectricGreen.copy(alpha = 0.2f) else ForMaxWarning.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = statusText,
                                                    color = if (ex.status == ContentStatus.PUBLISHED) ForMaxElectricGreen else ForMaxWarning,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                        Text(text = "${ex.primaryMuscle.displayNameAr} • ${ex.equipment.displayNameAr}", color = ForMaxTextSecondary, fontSize = 11.sp)
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                        IconButton(onClick = { previewingExercise = ex }, modifier = Modifier.size(32.dp)) {
                                            Icon(Icons.Default.Visibility, contentDescription = "معاينة", tint = ForMaxElectricGreen, modifier = Modifier.size(16.dp))
                                        }
                                        IconButton(onClick = { editingExercise = ex }, modifier = Modifier.size(32.dp)) {
                                            Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = Color.White, modifier = Modifier.size(16.dp))
                                        }
                                        IconButton(onClick = { addingMediaForExerciseId = ex.id }, modifier = Modifier.size(32.dp)) {
                                            Icon(Icons.Default.AttachFile, contentDescription = "وسائط", tint = Color(0xFF64B5F6), modifier = Modifier.size(16.dp))
                                        }
                                        IconButton(onClick = { onDeleteExercise(ex.id) }, modifier = Modifier.size(32.dp)) {
                                            Icon(Icons.Default.Delete, contentDescription = "حذف", tint = ForMaxError, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Tab 2: Media Management
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("دليل وسائط التمارين (${allMedia.size})", color = ForMaxTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(allMedia) { media ->
                            Card(
                                modifier = Modifier.fillMaxWidth().border(1.dp, ForMaxCardBorder, RoundedCornerShape(12.dp)),
                                colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            val mTypeName = when(media.type) {
                                                MediaType.VIDEO -> "فيديو"
                                                MediaType.GIF -> "حركة"
                                                MediaType.IMAGE -> "صورة"
                                                else -> media.type.name
                                            }
                                            Surface(color = ForMaxElectricGreen.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                                Text(mTypeName, color = ForMaxElectricGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                            }
                                            Text(media.title.ifBlank { "بدون عنوان" }, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        }
                                        Text(text = "التمرين المستهدف: ${media.exerciseId}", color = ForMaxTextSecondary, fontSize = 10.sp)
                                        Text(text = media.url.ifBlank { media.localPath }, color = Color(0xFF90CAF9), fontSize = 10.sp, maxLines = 1)
                                    }
                                    IconButton(onClick = { onDeleteMedia(media.mediaId) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "حذف الوسائط", tint = ForMaxError)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            3 -> {
                // Tab 3: Content Quality Checker
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("الفحص الآلي لجودة محتوى التمارين", color = ForMaxElectricGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("تدقيق اكتمال الشرح، البدائل، والوسائط لكل التمارين", color = ForMaxTextSecondary, fontSize = 11.sp)

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(exercises) { ex ->
                            val hasInstructions = ex.instructions.isNotBlank() || ex.setupInstructions.isNotBlank()
                            val hasAlts = ex.alternativeExerciseIds.isNotEmpty()
                            val isComplete = hasInstructions && hasAlts && ex.recommendedSets > 0

                            Surface(
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)),
                                color = ForMaxSurface
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(ex.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        if (isComplete) {
                                            Surface(color = ForMaxElectricGreen.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                                Text("✓ مكتمل وموثق", color = ForMaxElectricGreen, fontSize = 9.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                            }
                                        } else {
                                            Surface(color = ForMaxWarning.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                                Text("⚠️ يتطلب إكمال البيانات", color = ForMaxWarning, fontSize = 9.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                            }
                                        }
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Text("✓ العضلة: ${ex.primaryMuscle.displayNameAr}", color = ForMaxTextSecondary, fontSize = 10.sp)
                                        Text(if (hasInstructions) "✓ التعليمات" else "❌ بدون تعليمات", color = if (hasInstructions) ForMaxTextSecondary else ForMaxError, fontSize = 10.sp)
                                        Text(if (hasAlts) "✓ البدائل" else "❌ بدون بدائل", color = if (hasAlts) ForMaxTextSecondary else ForMaxError, fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            4 -> {
                // Tab 4: Content Analytics
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("التمارين الأكثر تصفحاً ومشاهدة", color = ForMaxTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                    val topViewed = exercises.sortedByDescending { it.viewsCount }.take(5)
                    topViewed.forEach { ex ->
                        Surface(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)), color = ForMaxSurface) {
                            Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(ex.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("${ex.viewsCount} مشاهدة", color = ForMaxElectricGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("التمارين الأكثر إنجازاً بالجلسات", color = ForMaxTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    val topCompleted = exercises.sortedByDescending { it.completionCount }.take(5)
                    topCompleted.forEach { ex ->
                        Surface(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)), color = ForMaxSurface) {
                            Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(ex.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("${ex.completionCount} تمرين", color = Color(0xFF64B5F6), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminStatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color = ForMaxElectricGreen,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.border(1.dp, ForMaxCardBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title.uppercase(), color = ForMaxTextSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = color, fontSize = 22.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, color = ForMaxTextSecondary, fontSize = 10.sp)
        }
    }
}
