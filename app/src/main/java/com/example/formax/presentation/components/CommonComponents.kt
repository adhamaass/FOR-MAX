package com.example.formax.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formax.domain.models.ExerciseSet
import com.example.formax.domain.models.RecoveryCheck
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun RestTimerBanner(
    initialSeconds: Int = 120,
    isActive: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var remainingSeconds by remember(initialSeconds, isActive) { mutableIntStateOf(initialSeconds) }
    var isPaused by remember { mutableStateOf(false) }

    LaunchedEffect(isActive, isPaused, remainingSeconds) {
        if (isActive && !isPaused && remainingSeconds > 0) {
            delay(1000)
            remainingSeconds -= 1
        }
    }

    AnimatedVisibility(
        visible = isActive && remainingSeconds > 0,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, ForMaxElectricGreen.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .testTag("rest_timer_banner"),
            colors = CardDefaults.cardColors(containerColor = ForMaxSurfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ForMaxElectricGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "مؤقت",
                            tint = ForMaxElectricGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "فترة الراحة",
                            color = ForMaxTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        val mins = remainingSeconds / 60
                        val secs = remainingSeconds % 60
                        Text(
                            text = "${mins}:${secs.toString().padStart(2, '0')}",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // +30s button
                    OutlinedButton(
                        onClick = { remainingSeconds += 30 },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ForMaxElectricGreen),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = androidx.compose.ui.graphics.SolidColor(ForMaxElectricGreen))
                    ) {
                        Text("+30ث", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    // Pause / Resume
                    IconButton(
                        onClick = { isPaused = !isPaused },
                        modifier = Modifier
                            .size(34.dp)
                            .background(ForMaxSurfaceElevated, CircleShape)
                    ) {
                        Icon(
                            imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (isPaused) "استئناف" else "إيقاف مؤقت",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Skip button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(34.dp)
                            .background(ForMaxSurfaceElevated, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "تخطي الراحة",
                            tint = ForMaxTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SetInputRow(
    setNumber: Int,
    initialWeight: Double = 0.0,
    initialReps: Int = 0,
    initialRir: Int = 2,
    lastSessionSet: ExerciseSet? = null,
    isCompleted: Boolean = false,
    onSaveSet: (weight: Double, reps: Int, rir: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var weightText by remember(initialWeight) {
        mutableStateOf(if (initialWeight > 0) initialWeight.toString() else "")
    }
    var repsText by remember(initialReps) {
        mutableStateOf(if (initialReps > 0) initialReps.toString() else "")
    }
    var rirText by remember(initialRir) {
        mutableStateOf(initialRir.toString())
    }
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                if (isCompleted) ForMaxElectricGreen.copy(alpha = 0.5f) else ForMaxCardBorder,
                RoundedCornerShape(12.dp)
            )
            .testTag("set_row_$setNumber"),
        color = if (isCompleted) ForMaxElectricGreen.copy(alpha = 0.06f) else ForMaxSurfaceElevated
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Set Indicator
            Column(modifier = Modifier.width(36.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "الجولة",
                    color = ForMaxTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$setNumber",
                    color = if (isCompleted) ForMaxElectricGreen else Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }

            // Previous Reference (e.g. 20 kg × 10 reps)
            Column(modifier = Modifier.width(76.dp)) {
                Text(
                    text = "السابق",
                    color = ForMaxTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                if (lastSessionSet != null && lastSessionSet.weightKg > 0) {
                    Text(
                        text = "${lastSessionSet.weightKg} كجم × ${lastSessionSet.reps}",
                        color = Color(0xFFCFD8DC),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = "أول مرة",
                        color = ForMaxTextSecondary.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }
            }

            // Weight Input
            Column(modifier = Modifier.width(64.dp)) {
                Text(text = "كجم", color = ForMaxTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .height(44.dp)
                        .testTag("weight_input_$setNumber"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ForMaxSurface,
                        unfocusedContainerColor = ForMaxSurface,
                        focusedBorderColor = ForMaxElectricGreen,
                        unfocusedBorderColor = ForMaxCardBorder
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            // Reps Input
            Column(modifier = Modifier.width(54.dp)) {
                Text(text = "تكرار", color = ForMaxTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = repsText,
                    onValueChange = { repsText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .height(44.dp)
                        .testTag("reps_input_$setNumber"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ForMaxSurface,
                        unfocusedContainerColor = ForMaxSurface,
                        focusedBorderColor = ForMaxElectricGreen,
                        unfocusedBorderColor = ForMaxCardBorder
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            // RIR Input
            Column(modifier = Modifier.width(50.dp)) {
                Text(text = "RIR", color = ForMaxTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = rirText,
                    onValueChange = { rirText = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .height(44.dp)
                        .testTag("rir_input_$setNumber"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ForMaxSurface,
                        unfocusedContainerColor = ForMaxSurface,
                        focusedBorderColor = ForMaxElectricGreen,
                        unfocusedBorderColor = ForMaxCardBorder
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            // Save / Check Button
            IconButton(
                onClick = {
                    val w = weightText.toDoubleOrNull() ?: 0.0
                    val r = repsText.toIntOrNull() ?: 0
                    val rir = rirText.toIntOrNull() ?: 2
                    if (r > 0) {
                        onSaveSet(w, r, rir)
                        focusManager.clearFocus()
                    }
                },
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isCompleted) ForMaxElectricGreen else ForMaxSurfaceVariant)
                    .testTag("save_set_btn_$setNumber")
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "حفظ الجولة",
                    tint = if (isCompleted) Color(0xFF0B0D0F) else ForMaxTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun RecoveryCheckDialog(
    onDismiss: () -> Unit,
    onSubmit: (RecoveryCheck) -> Unit
) {
    var energy by remember { mutableIntStateOf(4) }
    var sleep by remember { mutableIntStateOf(4) }
    var soreness by remember { mutableIntStateOf(2) }
    var stress by remember { mutableIntStateOf(2) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "فحص الاستشفاء قبل التمرين",
                    color = ForMaxElectricGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "كيف تشعر اليوم بدنياً وذهنياً؟",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "بيانات داعمة لتعديل مقترحات التطور الرياضي بذكاء.",
                    color = ForMaxTextSecondary,
                    fontSize = 11.sp
                )

                RecoveryRatingRow(label = "مستوى الطاقة", value = energy, onValueChange = { energy = it })
                RecoveryRatingRow(label = "جودة النوم", value = sleep, onValueChange = { sleep = it })
                RecoveryRatingRow(label = "إجهاد وألم العضلات", value = soreness, onValueChange = { soreness = it })
                RecoveryRatingRow(label = "مستوى الضغط والتوتر", value = stress, onValueChange = { stress = it })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSubmit(RecoveryCheck(energy = energy, sleep = sleep, soreness = soreness, stress = stress))
                },
                colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen, contentColor = Color(0xFF0B0D0F)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("ابدأ التمرين", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("تخطي الفحص", color = ForMaxTextSecondary)
            }
        },
        containerColor = ForMaxSurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun RecoveryRatingRow(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(text = "$value / 5", color = ForMaxElectricGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            (1..5).forEach { num ->
                val isSel = num <= value
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onValueChange(num) },
                    color = if (isSel) ForMaxElectricGreen else ForMaxSurfaceElevated,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "$num",
                            color = if (isSel) Color(0xFF0B0D0F) else ForMaxTextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color = ForMaxElectricGreen,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = ForMaxTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(iconColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = ForMaxTextSecondary,
                fontSize = 11.sp
            )
        }
    }
}
