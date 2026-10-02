package com.example.formax.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.formax.domain.models.*
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    allPrograms: List<TrainingProgram>,
    currentLanguage: AppLanguage,
    currentUserRole: UserRole,
    isDevelopmentEnvironment: Boolean,
    onSelectLanguage: (AppLanguage) -> Unit,
    onResetApp: () -> Unit,
    onResetDemoData: () -> Unit,
    onRequestDevAdmin: () -> Unit,
    onAuthenticateRemoteToken: (String) -> Unit,
    onOpenAdminPanel: () -> Unit,
    onExitAdmin: () -> Unit,
    onSelectProgram: (TrainingProgram) -> Unit
) {
    val scrollState = rememberScrollState()

    var showLanguageDialog by remember { mutableStateOf(false) }
    var showResetWarning1 by remember { mutableStateOf(false) }
    var showResetWarning2 by remember { mutableStateOf(false) }
    var showDemoResetDialog by remember { mutableStateOf(false) }
    var showAdminGateDialog by remember { mutableStateOf(false) }
    var remoteTokenInput by remember { mutableStateOf("") }

    // Dialog 1: First Confirmation for Reset App
    if (showResetWarning1) {
        AlertDialog(
            onDismissRequest = { showResetWarning1 = false },
            title = {
                Text(
                    text = stringResource(R.string.reset_dialog_title),
                    fontWeight = FontWeight.Black,
                    color = ForMaxCrimsonAlert
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.reset_dialog_warning),
                    color = Color.White,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResetWarning1 = false
                        showResetWarning2 = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForMaxCrimsonAlert),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("reset_app_first_confirm_btn")
                ) {
                    Text(
                        text = stringResource(R.string.reset_dialog_confirm_btn),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetWarning1 = false }) {
                    Text(text = stringResource(R.string.dialog_cancel), color = ForMaxTextSecondary)
                }
            },
            containerColor = ForMaxSurfaceVariant,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Dialog 2: Second & Final Confirmation for Reset App
    if (showResetWarning2) {
        AlertDialog(
            onDismissRequest = { showResetWarning2 = false },
            title = {
                Text(
                    text = stringResource(R.string.reset_confirm2_title),
                    fontWeight = FontWeight.Black,
                    color = ForMaxCrimsonAlert
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.reset_confirm2_msg),
                    color = Color.White,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showResetWarning2 = false
                        onResetApp()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForMaxCrimsonAlert),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("reset_app_final_confirm_btn")
                ) {
                    Text(
                        text = stringResource(R.string.reset_confirm2_btn),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetWarning2 = false }) {
                    Text(text = stringResource(R.string.dialog_cancel), color = ForMaxTextSecondary)
                }
            },
            containerColor = ForMaxSurfaceVariant,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Demo Data Reset Dialog (Development Only)
    if (showDemoResetDialog) {
        AlertDialog(
            onDismissRequest = { showDemoResetDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.demo_reset_title),
                    fontWeight = FontWeight.Bold,
                    color = ForMaxElectricGreen
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.demo_reset_msg),
                    color = Color.White,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDemoResetDialog = false
                        onResetDemoData()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("confirm_demo_reset_btn")
                ) {
                    Text(
                        text = stringResource(R.string.demo_reset_btn),
                        color = Color(0xFF0B0D0F),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDemoResetDialog = false }) {
                    Text(text = stringResource(R.string.dialog_cancel), color = ForMaxTextSecondary)
                }
            },
            containerColor = ForMaxSurfaceVariant,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Language Selector Dialog
    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.profile_language_title),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppLanguage.values().forEach { lang ->
                        val isSelected = currentLanguage == lang
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .border(
                                    1.dp,
                                    if (isSelected) ForMaxElectricGreen else ForMaxCardBorder,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    onSelectLanguage(lang)
                                    showLanguageDialog = false
                                },
                            color = if (isSelected) ForMaxElectricGreen.copy(alpha = 0.12f) else ForMaxSurfaceElevated
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = lang.displayName,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) ForMaxElectricGreen else Color.White,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = if (lang.isRtl) "Right to Left (RTL)" else "Left to Right (LTR)",
                                        color = ForMaxTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = ForMaxElectricGreen
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(text = stringResource(R.string.dialog_cancel), color = ForMaxTextSecondary)
                }
            },
            containerColor = ForMaxSurfaceVariant,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Development Admin Gate Dialog
    if (showAdminGateDialog) {
        AlertDialog(
            onDismissRequest = {
                showAdminGateDialog = false
                remoteTokenInput = ""
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = ForMaxElectricGreen)
                    Text(
                        text = stringResource(R.string.admin_auth_title),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        color = ForMaxElectricGreen.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ForMaxElectricGreen.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "DEVELOPMENT ONLY — NOT PRODUCTION AUTHENTICATION\n\nThis debug gate is compiled exclusively for local testing of exercises, media, and training templates without a backend server.",
                            color = ForMaxElectricGreen,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = stringResource(R.string.admin_auth_msg),
                        color = ForMaxTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )

                    OutlinedTextField(
                        value = remoteTokenInput,
                        onValueChange = { remoteTokenInput = it },
                        label = { Text("Optional Remote Admin Token") },
                        placeholder = { Text("Leave blank for local debug session") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("admin_token_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForMaxElectricGreen,
                            unfocusedBorderColor = ForMaxCardBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (remoteTokenInput.isNotBlank()) {
                            onAuthenticateRemoteToken(remoteTokenInput)
                        } else {
                            onRequestDevAdmin()
                        }
                        showAdminGateDialog = false
                        remoteTokenInput = ""
                        onOpenAdminPanel()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForMaxElectricGreen),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("admin_auth_confirm_btn")
                ) {
                    Text(
                        text = stringResource(R.string.admin_auth_confirm),
                        color = Color(0xFF0B0D0F),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAdminGateDialog = false
                    remoteTokenInput = ""
                }) {
                    Text(text = stringResource(R.string.dialog_cancel), color = ForMaxTextSecondary)
                }
            },
            containerColor = ForMaxSurfaceVariant,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForMaxBackground)
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        Column {
            Text(
                text = stringResource(R.string.profile_title),
                color = ForMaxElectricGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Text(
                text = userProfile.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }

        // Bio Specs Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.profile_specs_title),
                    color = ForMaxTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.profile_age_sex), color = ForMaxTextSecondary, fontSize = 13.sp)
                    Text(
                        text = "${userProfile.age} • ${if (currentLanguage == AppLanguage.ARABIC) userProfile.sex.displayNameAr else userProfile.sex.name}",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.profile_height_weight), color = ForMaxTextSecondary, fontSize = 13.sp)
                    Text(text = "${userProfile.heightCm.toInt()} cm • ${userProfile.weightKg} kg", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.profile_experience), color = ForMaxTextSecondary, fontSize = 13.sp)
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) userProfile.experience.displayNameAr else userProfile.experience.name,
                        color = ForMaxElectricGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.profile_goal), color = ForMaxTextSecondary, fontSize = 13.sp)
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) userProfile.mainGoal.displayNameAr else userProfile.mainGoal.displayName,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = stringResource(R.string.profile_equipment), color = ForMaxTextSecondary, fontSize = 13.sp)
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) userProfile.equipment.displayNameAr else userProfile.equipment.displayName,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Active Program Selection Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.profile_active_program),
                    color = ForMaxTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                allPrograms.forEach { prog ->
                    val isSel = prog.id == userProfile.selectedProgramId
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                1.dp,
                                if (isSel) ForMaxElectricGreen else ForMaxCardBorder,
                                RoundedCornerShape(10.dp)
                            ),
                        color = if (isSel) ForMaxElectricGreen.copy(alpha = 0.12f) else ForMaxSurfaceElevated
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = prog.name,
                                    color = if (isSel) ForMaxElectricGreen else Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "${prog.weeklyDays} days/week • ${if (currentLanguage == AppLanguage.ARABIC) prog.systemType.displayNameAr else prog.systemType.displayName}",
                                    color = ForMaxTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                            if (isSel) {
                                Surface(
                                    color = ForMaxElectricGreen,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.profile_program_active_badge),
                                        color = Color(0xFF0B0D0F),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            } else {
                                TextButton(onClick = { onSelectProgram(prog) }) {
                                    Text(
                                        text = stringResource(R.string.profile_switch_program),
                                        color = ForMaxElectricGreen,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Application Settings Card (Language & Reset App)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ForMaxCardBorder, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = stringResource(R.string.profile_settings_title),
                    color = ForMaxTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                // Language Option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { showLanguageDialog = true }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(imageVector = Icons.Default.Language, contentDescription = null, tint = ForMaxElectricGreen, modifier = Modifier.size(18.dp))
                            Text(
                                text = stringResource(R.string.profile_language_title),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            text = stringResource(R.string.profile_language_desc),
                            color = ForMaxTextSecondary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Surface(
                        color = ForMaxSurfaceElevated,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ForMaxCardBorder)
                    ) {
                        Text(
                            text = currentLanguage.displayName,
                            color = ForMaxElectricGreen,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                HorizontalDivider(color = ForMaxCardBorder)

                // Production App Reset Option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { showResetWarning1 = true }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, tint = ForMaxCrimsonAlert, modifier = Modifier.size(18.dp))
                            Text(
                                text = stringResource(R.string.profile_reset_app_title),
                                color = ForMaxCrimsonAlert,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            text = stringResource(R.string.profile_reset_app_desc),
                            color = ForMaxTextSecondary,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Button(
                        onClick = { showResetWarning1 = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForMaxCrimsonAlert.copy(alpha = 0.15f),
                            contentColor = ForMaxCrimsonAlert
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("reset_app_init_btn")
                    ) {
                        Text(text = stringResource(R.string.profile_reset_app_title), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Development & Admin Gate Tools (Exclusively visible in development/debug builds)
        if (isDevelopmentEnvironment || currentUserRole == UserRole.ADMIN) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, if (currentUserRole == UserRole.ADMIN) ForMaxElectricGreen else ForMaxCardBorder, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = ForMaxSurfaceVariant)
            ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = ForMaxElectricGreen, modifier = Modifier.size(18.dp))
                    Text(
                        text = stringResource(R.string.profile_dev_tools_title),
                        color = ForMaxElectricGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // Reset Demo Data (Development Only)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.profile_dev_reset_demo),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.profile_dev_reset_demo_desc),
                            color = ForMaxTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    OutlinedButton(
                        onClick = { showDemoResetDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ForMaxCardBorder),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("dev_reset_demo_btn")
                    ) {
                        Text("Reset Demo", fontSize = 11.sp)
                    }
                }

                HorizontalDivider(color = ForMaxCardBorder)

                // Admin Security Gate - No simple toggle!
                if (currentUserRole == UserRole.ADMIN) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(color = ForMaxElectricGreen, shape = RoundedCornerShape(4.dp)) {
                                Text("ADMIN ACTIVE", color = Color(0xFF0B0D0F), fontSize = 10.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                            Text("Content Manager Privileges", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        TextButton(onClick = onExitAdmin) {
                            Text("Exit Admin", color = ForMaxCrimsonAlert, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = onOpenAdminPanel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("open_admin_panel_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForMaxElectricGreen,
                            contentColor = Color(0xFF0B0D0F)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.DashboardCustomize, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(stringResource(R.string.profile_btn_enter_admin), fontWeight = FontWeight.Black, fontSize = 13.sp)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.profile_dev_admin_title),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.profile_dev_admin_desc),
                                color = ForMaxTextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = { showAdminGateDialog = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForMaxElectricGreen.copy(alpha = 0.15f),
                                contentColor = ForMaxElectricGreen
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("request_admin_access_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Admin Access", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

        Spacer(modifier = Modifier.height(60.dp))
    }
}
