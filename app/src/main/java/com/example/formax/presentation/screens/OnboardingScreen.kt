package com.example.formax.presentation.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formax.domain.engine.TrainingEngine
import com.example.formax.domain.models.*
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    onCompleteOnboarding: (UserProfile) -> Unit
) {
    var step by remember { mutableIntStateOf(0) }

    var name by remember { mutableStateOf("البطل") }
    var ageText by remember { mutableStateOf("26") }
    var sex by remember { mutableStateOf(Sex.MALE) }
    var heightText by remember { mutableStateOf("178") }
    var weightText by remember { mutableStateOf("78.5") }
    var experience by remember { mutableStateOf(TrainingExperience.INTERMEDIATE) }
    var goal by remember { mutableStateOf(FitnessGoal.FAT_LOSS_AND_MUSCLE_GAIN) }
    var equipment by remember { mutableStateOf(EquipmentAvailable.FULL_GYM) }
    var targetDays by remember { mutableIntStateOf(4) }
    var selectedSplit by remember { mutableStateOf(SplitSystemType.UPPER_LOWER) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ForMaxBackground)
    ) {
        when (step) {
            0 -> {
                WelcomeHeroStep(
                    onGetStarted = { step = 1 }
                )
            }
            1 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .verticalScroll(scrollState)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "فور ماكس: البيانات الأساسية",
                                color = ForMaxElectricGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "ملف الرياضي",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Text(
                            text = "الخطوة 1 من 2",
                            color = ForMaxTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    // Biometrics Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text(
                                text = "البيانات الجسمانية",
                                color = ForMaxTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("الاسم أو اللقب") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("onboarding_name_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = ForMaxElectricGreen,
                                    unfocusedBorderColor = ForMaxCardBorder,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                OutlinedTextField(
                                    value = ageText,
                                    onValueChange = { ageText = it },
                                    label = { Text("العمر") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("onboarding_age_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ForMaxElectricGreen,
                                        unfocusedBorderColor = ForMaxCardBorder,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )

                                OutlinedTextField(
                                    value = heightText,
                                    onValueChange = { heightText = it },
                                    label = { Text("الطول (سم)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("onboarding_height_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ForMaxElectricGreen,
                                        unfocusedBorderColor = ForMaxCardBorder,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )

                                OutlinedTextField(
                                    value = weightText,
                                    onValueChange = { weightText = it },
                                    label = { Text("الوزن (كجم)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("onboarding_weight_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ForMaxElectricGreen,
                                        unfocusedBorderColor = ForMaxCardBorder,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    )
                                )
                            }

                            Text(text = "الجنس البيولوجي", color = ForMaxTextSecondary, fontSize = 12.sp)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Sex.values().forEach { s ->
                                    val isSel = s == sex
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(38.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { sex = s },
                                        color = if (isSel) ForMaxElectricGreen else ForMaxSurfaceElevated,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = s.displayNameAr,
                                                color = if (isSel) Color(0xFF0B0D0F) else Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Main Goal Selection
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "الهدف التدريبي الأساسي",
                                color = ForMaxTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            FitnessGoal.values().forEach { g ->
                                val isSel = g == goal
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .border(
                                            1.dp,
                                            if (isSel) ForMaxElectricGreen else ForMaxCardBorder,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable { goal = g },
                                    color = if (isSel) ForMaxElectricGreen.copy(alpha = 0.12f) else ForMaxSurfaceElevated
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = g.displayNameAr,
                                            color = if (isSel) ForMaxElectricGreen else Color.White,
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 14.sp
                                        )
                                        if (isSel) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = ForMaxElectricGreen,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Experience Level
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "الخبرة في تمارين المقاومة",
                                color = ForMaxTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TrainingExperience.values().forEach { exp ->
                                    val isSel = exp == experience
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { experience = exp },
                                        color = if (isSel) ForMaxElectricGreen else ForMaxSurfaceElevated
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = exp.displayNameAr,
                                                color = if (isSel) Color(0xFF0B0D0F) else Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Equipment Available
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "الأجهزة والمعدات المتوفرة",
                                color = ForMaxTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            EquipmentAvailable.values().forEach { eq ->
                                val isSel = eq == equipment
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { equipment = eq },
                                    color = if (isSel) ForMaxElectricGreen.copy(alpha = 0.15f) else ForMaxSurfaceElevated
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = eq.displayNameAr,
                                            color = if (isSel) ForMaxElectricGreen else Color.White,
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                        if (isSel) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = ForMaxElectricGreen,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Training Frequency Days per week
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "أيام التدريب في الأسبوع: $targetDays أيام",
                                color = ForMaxTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                (3..6).forEach { d ->
                                    val isSel = d == targetDays
                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(40.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { targetDays = d },
                                        color = if (isSel) ForMaxElectricGreen else ForMaxSurfaceElevated
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "$d أيام",
                                                color = if (isSel) Color(0xFF0B0D0F) else Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Next Button: Analyze and Recommend System
                    Button(
                        onClick = {
                            val rec = TrainingEngine.recommendTrainingSystem(
                                experience = experience,
                                goal = goal,
                                targetDays = targetDays,
                                equipment = equipment
                            )
                            selectedSplit = rec.recommendedSystem
                            step = 2
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("onboarding_continue_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForMaxElectricGreen,
                            contentColor = Color(0xFF0B0D0F)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "تحليل وتحديد النظام الأمثل ←",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            2 -> {
                // Step 2: Split Recommendation System
                val recommendation = remember(experience, goal, targetDays, equipment) {
                    TrainingEngine.recommendTrainingSystem(experience, goal, targetDays, equipment)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .verticalScroll(scrollState)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { step = 1 }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "رجوع", tint = ForMaxTextSecondary)
                        Text(text = "تعديل البيانات الأساسية", color = ForMaxTextSecondary, fontSize = 13.sp)
                    }

                    // Recommendation Highlight Banner
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, ForMaxElectricGreen, RoundedCornerShape(18.dp)),
                        colors = CardDefaults.cardColors(containerColor = ForMaxSurfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(ForMaxElectricGreen)
                                )
                                Text(
                                    text = "موصى به لك",
                                    color = ForMaxElectricGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Text(
                                text = recommendation.recommendedSystem.displayNameAr,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )

                            Text(
                                text = recommendation.explanation,
                                color = Color(0xFFCFD8DC),
                                fontSize = 13.sp,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ForMaxSurfaceElevated, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "الحجم التدريبي المبدئي:",
                                    color = ForMaxTextSecondary,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "~10 جولات صلبة / عضلة / أسبوع",
                                    color = ForMaxElectricGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Text(
                        text = "استكشف كافة الأنظمة التدريبية",
                        color = ForMaxTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // System Selection Cards
                    recommendation.allSystemsWithContext.forEach { sysCtx ->
                        val isSelected = sysCtx.systemType == selectedSplit
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) ForMaxElectricGreen else ForMaxCardBorder,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { selectedSplit = sysCtx.systemType },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) ForMaxElectricGreen.copy(alpha = 0.08f) else ForMaxSurface
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = sysCtx.systemType.displayNameAr,
                                        color = if (isSelected) ForMaxElectricGreen else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    if (sysCtx.isRecommended) {
                                        Surface(
                                            color = ForMaxElectricGreen.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "الأنسب لك",
                                                color = ForMaxElectricGreen,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = sysCtx.rationale,
                                    color = ForMaxTextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Final Launch Button
                    Button(
                        onClick = {
                            val programId = when (selectedSplit) {
                                SplitSystemType.UPPER_LOWER -> "upper_lower_4day"
                                SplitSystemType.FULL_BODY -> "full_body_3day"
                                SplitSystemType.PUSH_PULL_LEGS -> "ppl_6day"
                                SplitSystemType.FIVE_DAY_HYBRID -> "hybrid_5day"
                                SplitSystemType.CUSTOM_ADVANCED -> "upper_lower_4day"
                            }

                            val profile = UserProfile(
                                name = name.ifBlank { "البطل" },
                                age = ageText.toIntOrNull() ?: 26,
                                sex = sex,
                                heightCm = heightText.toDoubleOrNull() ?: 178.0,
                                weightKg = weightText.toDoubleOrNull() ?: 78.5,
                                experience = experience,
                                mainGoal = goal,
                                equipment = equipment,
                                targetDaysPerWeek = targetDays,
                                selectedProgramId = programId,
                                isOnboarded = true,
                                isAdminMode = false
                            )
                            onCompleteOnboarding(profile)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("onboarding_finish_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForMaxElectricGreen,
                            contentColor = Color(0xFF0B0D0F)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "ابدأ التدريب مع فور ماكس",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun WelcomeHeroStep(
    onGetStarted: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0B0D0F), Color(0xFF13181F), Color(0xFF0B0D0F))
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(ForMaxElectricGreen.copy(alpha = 0.25f), Color.Transparent)
                        )
                    )
                    .border(2.dp, ForMaxElectricGreen, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = ForMaxElectricGreen,
                    modifier = Modifier.size(46.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "FOR MAX",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "ابنِ جسمك المثالي.",
                color = ForMaxElectricGreen,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "تدرب بذكاء. تتبع كل شيء. كن أقوى.\nنظام تدريبي مبني على قواعد علمية وبدون عشوائية.",
                color = ForMaxTextSecondary,
                fontSize = 13.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center
            )
        }

        // Get Started CTA
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("get_started_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForMaxElectricGreen,
                    contentColor = Color(0xFF0B0D0F)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "ابدأ الآن",
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
