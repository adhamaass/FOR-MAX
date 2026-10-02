package com.example.formax.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formax.domain.models.*
import com.example.ui.theme.*
import java.util.UUID

@Composable
fun ProgressScreen(
    weeklyReport: WeeklyReport,
    measurements: List<BodyMeasurement>,
    progressPhotos: List<ProgressPhoto>,
    cardioSessions: List<CardioSession>,
    onAddMeasurement: (BodyMeasurement) -> Unit,
    onAddProgressPhoto: (ProgressPhoto) -> Unit,
    onAddCardioSession: (CardioSession) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Weekly Report, 1: Measurements, 2: Photos, 3: Cardio

    var showAddMeasurementDialog by remember { mutableStateOf(false) }
    var showAddPhotoDialog by remember { mutableStateOf(false) }
    var showAddCardioDialog by remember { mutableStateOf(false) }

    // Dialog for adding measurements
    if (showAddMeasurementDialog) {
        var weight by remember { mutableStateOf("78.5") }
        var waist by remember { mutableStateOf("81.0") }
        var chest by remember { mutableStateOf("103.0") }
        var arm by remember { mutableStateOf("38.0") }
        var thigh by remember { mutableStateOf("58.5") }

        AlertDialog(
            onDismissRequest = { showAddMeasurementDialog = false },
            title = {
                Text("تسجيل قياسات الجسم", color = ForMaxElectricGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = weight,
                        onValueChange = { weight = it },
                        label = { Text("وزن الجسم (كجم)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForMaxElectricGreen,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = waist,
                        onValueChange = { waist = it },
                        label = { Text("محيط الخصر (سم)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForMaxElectricGreen,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = chest,
                        onValueChange = { chest = it },
                        label = { Text("محيط الصدر (سم)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForMaxElectricGreen,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = arm,
                        onValueChange = { arm = it },
                        label = { Text("محيط الذراع (سم)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForMaxElectricGreen,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val m = BodyMeasurement(
                            id = UUID.randomUUID().toString(),
                            weightKg = weight.toDoubleOrNull() ?: 78.5,
                            waistCm = waist.toDoubleOrNull(),
                            chestCm = chest.toDoubleOrNull(),
                            armCm = arm.toDoubleOrNull(),
                            thighCm = thigh.toDoubleOrNull()
                        )
                        onAddMeasurement(m)
                        showAddMeasurementDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen, contentColor = Color(0xFF0B0D0F))
                ) {
                    Text("حفظ القياس", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMeasurementDialog = false }) {
                    Text("إلغاء", color = ForMaxTextSecondary)
                }
            },
            containerColor = ForMaxSurface,
            shape = RoundedCornerShape(18.dp)
        )
    }

    // Dialog for adding progress photo
    if (showAddPhotoDialog) {
        var selectedPose by remember { mutableStateOf(PhotoPose.FRONT) }
        var weekLabel by remember { mutableStateOf("الأسبوع 4") }

        AlertDialog(
            onDismissRequest = { showAddPhotoDialog = false },
            title = {
                Text("إضافة صورة تطور", color = ForMaxElectricGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "الصور تبقى مشفرة وخاصة بالكامل على هاتفك ولا تغادر جهازك.",
                        color = ForMaxTextSecondary,
                        fontSize = 11.sp
                    )

                    OutlinedTextField(
                        value = weekLabel,
                        onValueChange = { weekLabel = it },
                        label = { Text("الاسم أو التاريخ (مثال: الأسبوع 1، الأسبوع 4)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForMaxElectricGreen,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Text(text = "زاوية التصوير:", color = ForMaxTextSecondary, fontSize = 12.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        PhotoPose.values().forEach { pose ->
                            val isSel = pose == selectedPose
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedPose = pose },
                                color = if (isSel) ForMaxElectricGreen else ForMaxSurfaceElevated
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = pose.displayNameAr,
                                        color = if (isSel) Color(0xFF0B0D0F) else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
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
                        val photo = ProgressPhoto(
                            id = UUID.randomUUID().toString(),
                            weekLabel = weekLabel,
                            pose = selectedPose,
                            imagePathOrUri = "local://photos/${weekLabel}_${selectedPose.name.lowercase()}.jpg"
                        )
                        onAddProgressPhoto(photo)
                        showAddPhotoDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen, contentColor = Color(0xFF0B0D0F))
                ) {
                    Text("إضافة الصورة", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddPhotoDialog = false }) {
                    Text("إلغاء", color = ForMaxTextSecondary)
                }
            },
            containerColor = ForMaxSurface,
            shape = RoundedCornerShape(18.dp)
        )
    }

    // Dialog for adding cardio session
    if (showAddCardioDialog) {
        var cardioType by remember { mutableStateOf(CardioType.RUNNING) }
        var durationText by remember { mutableStateOf("30") }
        var distanceText by remember { mutableStateOf("5.0") }
        var intensity by remember { mutableStateOf(CardioIntensity.MODERATE) }

        AlertDialog(
            onDismissRequest = { showAddCardioDialog = false },
            title = {
                Text("تسجيل جلسة كارديو", color = ForMaxElectricGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "نوع النشاط الهوائي", color = ForMaxTextSecondary, fontSize = 12.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf(CardioType.WALKING, CardioType.RUNNING, CardioType.CYCLING, CardioType.ROWING).forEach { ct ->
                            val isSel = ct == cardioType
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { cardioType = ct },
                                color = if (isSel) ForMaxElectricGreen else ForMaxSurfaceElevated
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = ct.displayNameAr,
                                        color = if (isSel) Color(0xFF0B0D0F) else Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = durationText,
                            onValueChange = { durationText = it },
                            label = { Text("المدة (دقيقة)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ForMaxElectricGreen,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )

                        OutlinedTextField(
                            value = distanceText,
                            onValueChange = { distanceText = it },
                            label = { Text("المسافة (كم)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ForMaxElectricGreen,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val session = CardioSession(
                            id = UUID.randomUUID().toString(),
                            type = cardioType,
                            durationMinutes = durationText.toIntOrNull() ?: 30,
                            distanceKm = distanceText.toDoubleOrNull(),
                            intensity = intensity
                        )
                        onAddCardioSession(session)
                        showAddCardioDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen, contentColor = Color(0xFF0B0D0F))
                ) {
                    Text("حفظ الكارديو", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCardioDialog = false }) {
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
        // Header
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = "تحليلات الأداء الرياضي",
                color = ForMaxElectricGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "التقدم والتقارير الأسبوعية",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }

        // Tab Selector Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val tabs = listOf("التقرير الأسبوعي", "قياسات الجسم", "صور التطور", "الكارديو")
            tabs.forEachIndexed { index, tab ->
                val isSel = selectedTab == index
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { selectedTab = index },
                    color = if (isSel) ForMaxElectricGreen else ForMaxSurfaceElevated
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = tab,
                            color = if (isSel) Color(0xFF0B0D0F) else Color.White,
                            fontSize = 10.sp,
                            fontWeight = if (isSel) FontWeight.Black else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tab Content
        when (selectedTab) {
            0 -> {
                // Tab 0: Weekly Report
                val scroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scroll)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Report Summary Banner
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, ForMaxElectricGreen, RoundedCornerShape(18.dp)),
                        colors = CardDefaults.cardColors(containerColor = ForMaxSurfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ملخص الأسبوع التدريبي",
                                    color = ForMaxElectricGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "نسبة الالتزام ${weeklyReport.consistencyPercentage.toInt()}%",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(text = "الجلسات المنجزة", color = ForMaxTextSecondary, fontSize = 11.sp)
                                    Text(
                                        text = "${weeklyReport.workoutsCompleted} / ${weeklyReport.targetWorkouts}",
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                                Column {
                                    Text(text = "حجم الأوزان", color = ForMaxTextSecondary, fontSize = 11.sp)
                                    val vol = if (weeklyReport.totalVolumeKg > 0) weeklyReport.totalVolumeKg.toInt() else 14200
                                    Text(
                                        text = "$vol كجم",
                                        color = ForMaxElectricGreen,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                                Column {
                                    Text(text = "أرقام قياسية (PRs)", color = ForMaxTextSecondary, fontSize = 11.sp)
                                    Text(
                                        text = "${weeklyReport.prsAchieved}",
                                        color = Color(0xFFFFD700),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    }

                    // Muscle Group Volume Breakdown
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "توزيع حجم التدريب على العضلات",
                                color = ForMaxTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )

                            val muscles = listOf(
                                MuscleGroup.CHEST to 0.85f,
                                MuscleGroup.BACK to 0.95f,
                                MuscleGroup.LEGS to 1.0f,
                                MuscleGroup.SHOULDERS to 0.70f,
                                MuscleGroup.BICEPS to 0.55f,
                                MuscleGroup.TRICEPS to 0.60f
                            )

                            muscles.forEach { (muscle, fraction) ->
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = muscle.displayNameAr, color = Color.White, fontSize = 12.sp)
                                        Text(
                                            text = "~${(fraction * 12).toInt()} جولة صلبة",
                                            color = ForMaxElectricGreen,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    LinearProgressIndicator(
                                        progress = { fraction },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp)),
                                        color = ForMaxElectricGreen,
                                        trackColor = ForMaxSurfaceElevated
                                    )
                                }
                            }
                        }
                    }

                    // Exercise Progression Status (Improved, Stable, Declining)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "اتجاه تطور الحركات والأوزان",
                                color = ForMaxTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )

                            // Improved
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ForMaxSurfaceElevated, RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "بنش برس بالبار (Barbell Bench Press)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "السابق: 77.5 × 10 • الحالي: 80.0 × 10", color = ForMaxTextSecondary, fontSize = 11.sp)
                                }
                                Surface(color = ForMaxElectricGreen.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                                    Text(
                                        text = "تطور مستمر ↑",
                                        color = ForMaxElectricGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            // Stable
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ForMaxSurfaceElevated, RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "سحب ظهر عريض (Lat Pulldown)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "السابق: 65.0 × 12 • الحالي: 65.0 × 12", color = ForMaxTextSecondary, fontSize = 11.sp)
                                }
                                Surface(color = Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp)) {
                                    Text(
                                        text = "ثبات الأداء =",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            // Declining / Recovery Alert
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ForMaxSurfaceElevated, RoundedCornerShape(8.dp))
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = "ضغط أكتاف واقف بالبار (OHP)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "السابق: 50.0 × 8 • الحالي: 47.5 × 8", color = ForMaxTextSecondary, fontSize = 11.sp)
                                }
                                Surface(color = ForMaxWarning.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                                    Text(
                                        text = "مراجعة الاستشفاء",
                                        color = ForMaxWarning,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(60.dp))
                }
            }
            1 -> {
                // Tab 1: Measurements
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "سجل قياسات المحيطات",
                            color = ForMaxTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { showAddMeasurementDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen, contentColor = Color(0xFF0B0D0F)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("+ تسجيل قياس", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (measurements.isNotEmpty()) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(measurements) { m ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().border(1.dp, ForMaxCardBorder, RoundedCornerShape(12.dp)),
                                    colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = "${m.weightKg} كجم", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                            Text(
                                                text = "الخصر: ${m.waistCm ?: "-"} سم • الصدر: ${m.chestCm ?: "-"} سم • الذراع: ${m.armCm ?: "-"} سم",
                                                color = ForMaxTextSecondary,
                                                fontSize = 11.sp
                                            )
                                        }
                                        Text(text = "تم التسجيل", color = ForMaxElectricGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = ForMaxSurface,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Straighten, contentDescription = null, tint = ForMaxElectricGreen, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("لا توجد قياسات مسجلة بعد", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("تتبع محيط الخصر، الصدر، الذراعين، والأفخاذ لتقييم التغير الحقيقي في الكتلة العضلية.", color = ForMaxTextSecondary, fontSize = 11.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
            2 -> {
                // Tab 2: Progress Photos (Front, Side, Back with side-by-side comparison)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "سجل التطور الجسدي الخاص",
                                color = ForMaxTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "الصور محفوظة على جهازك فقط",
                                color = ForMaxTextSecondary.copy(alpha = 0.6f),
                                fontSize = 10.sp
                            )
                        }
                        Button(
                            onClick = { showAddPhotoDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen, contentColor = Color(0xFF0B0D0F)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("+ إضافة صورة", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Side-by-Side Comparison Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ForMaxCardBorder, RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "مقارنة جنباً إلى جنب", color = ForMaxElectricGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                // Before
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(130.dp)
                                        .background(ForMaxSurfaceElevated, RoundedCornerShape(10.dp))
                                        .border(1.dp, ForMaxCardBorder, RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = ForMaxTextSecondary)
                                        Text("الأسبوع 1 (البداية)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text("78.5 كجم", color = ForMaxTextSecondary, fontSize = 10.sp)
                                    }
                                }

                                // After / Current
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(130.dp)
                                        .background(ForMaxSurfaceElevated, RoundedCornerShape(10.dp))
                                        .border(1.dp, ForMaxElectricGreen.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = ForMaxElectricGreen)
                                        Text("الأسبوع 4 (الحالي)", color = ForMaxElectricGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text("77.2 كجم", color = Color.White, fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }

                    // Photos timeline
                    if (progressPhotos.isNotEmpty()) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(progressPhotos) { photo ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)),
                                    color = ForMaxSurface
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "${photo.weekLabel} • وضعية ${photo.pose.displayNameAr}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Icon(Icons.Default.Lock, contentDescription = "خاص", tint = ForMaxTextSecondary, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            3 -> {
                // Tab 3: Cardio Sessions Tracker
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "تمارين اللياقة الهوائية والكارديو", color = ForMaxTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Button(
                            onClick = { showAddCardioDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen, contentColor = Color(0xFF0B0D0F)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("+ تسجيل كارديو", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (cardioSessions.isNotEmpty()) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(cardioSessions) { cs ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().border(1.dp, ForMaxCardBorder, RoundedCornerShape(12.dp)),
                                    colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(text = cs.type.displayNameAr, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text(
                                                text = "${cs.durationMinutes} دقيقة • ${cs.distanceKm ?: 0.0} كم • شدة ${cs.intensity.displayNameAr}",
                                                color = ForMaxTextSecondary,
                                                fontSize = 11.sp
                                            )
                                        }
                                        Icon(Icons.Default.DirectionsRun, contentDescription = null, tint = ForMaxElectricGreen)
                                    }
                                }
                            }
                        }
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = ForMaxSurface,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.DirectionsRun, contentDescription = null, tint = ForMaxElectricGreen, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("لا توجد جلسات كارديو مسجلة بعد", color = Color.White, fontWeight = FontWeight.Bold)
                                Text("سجل جلسات المشي أو الجري أو الدراجة لمتابعة لياقتك البدنية والنظامية.", color = ForMaxTextSecondary, fontSize = 11.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }
}
