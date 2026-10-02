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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formax.domain.models.*
import com.example.ui.theme.*

@Composable
fun ExerciseLibraryScreen(
    exercises: List<Exercise>,
    onSelectExercise: (Exercise) -> Unit,
    onToggleFavorite: (Exercise) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedMuscleFilter by remember { mutableStateOf<MuscleGroup?>(null) }
    var selectedDifficulty by remember { mutableStateOf<Difficulty?>(null) }
    var showFavoritesOnly by remember { mutableStateOf(false) }

    val filteredExercises = remember(exercises, searchQuery, selectedMuscleFilter, selectedDifficulty, showFavoritesOnly) {
        exercises.filter { ex ->
            val matchesQuery = searchQuery.isBlank() ||
                ex.name.contains(searchQuery, ignoreCase = true) ||
                ex.alternativeNames.any { it.contains(searchQuery, ignoreCase = true) } ||
                ex.primaryMuscle.displayName.contains(searchQuery, ignoreCase = true) ||
                ex.equipment.displayName.contains(searchQuery, ignoreCase = true)

            val matchesMuscle = selectedMuscleFilter == null || ex.primaryMuscle == selectedMuscleFilter
            val matchesDifficulty = selectedDifficulty == null || ex.difficulty == selectedDifficulty
            val matchesFavorite = !showFavoritesOnly || ex.isFavorite

            matchesQuery && matchesMuscle && matchesDifficulty && matchesFavorite
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForMaxBackground)
            .statusBarsPadding()
            .padding(top = 12.dp)
    ) {
        // Top Header
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "قاعدة بيانات التمارين",
                color = ForMaxElectricGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "المكتبة (${filteredExercises.size} تمرين)",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("ابحث باسم التمرين، العضلة، أو المعدات...", color = ForMaxTextSecondary, fontSize = 13.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "بحث", tint = ForMaxElectricGreen)
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "مسح", tint = ForMaxTextSecondary)
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("exercise_search_bar"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ForMaxSurface,
                    unfocusedContainerColor = ForMaxSurface,
                    focusedBorderColor = ForMaxElectricGreen,
                    unfocusedBorderColor = ForMaxCardBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Muscle Filter Horizontal Chip Row
        val chipScroll = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(chipScroll)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                label = "الكل",
                isSelected = selectedMuscleFilter == null && !showFavoritesOnly,
                onClick = { selectedMuscleFilter = null; showFavoritesOnly = false }
            )

            FilterChip(
                label = "المفضلة ★",
                isSelected = showFavoritesOnly,
                onClick = { showFavoritesOnly = !showFavoritesOnly }
            )

            MuscleGroup.values().filter { it != MuscleGroup.FULL_BODY }.forEach { mg ->
                FilterChip(
                    label = mg.displayNameAr,
                    isSelected = selectedMuscleFilter == mg,
                    onClick = {
                        selectedMuscleFilter = if (selectedMuscleFilter == mg) null else mg
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Exercise List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredExercises, key = { it.id }) { exercise ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, ForMaxCardBorder, RoundedCornerShape(14.dp))
                        .clickable { onSelectExercise(exercise) }
                        .testTag("exercise_card_${exercise.id}"),
                    colors = CardDefaults.cardColors(containerColor = ForMaxSurface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    color = ForMaxElectricGreen.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = exercise.primaryMuscle.displayNameAr,
                                        color = ForMaxElectricGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Surface(
                                    color = ForMaxSurfaceElevated,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = exercise.difficulty.displayNameAr,
                                        color = ForMaxTextSecondary,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = exercise.name,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "${exercise.equipment.displayNameAr} • ${exercise.movementPattern.displayNameAr}",
                                color = ForMaxTextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { onToggleFavorite(exercise) }) {
                                Icon(
                                    imageVector = if (exercise.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "المفضلة",
                                    tint = if (exercise.isFavorite) ForMaxElectricGreen else ForMaxTextSecondary
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = null,
                                tint = ForMaxTextSecondary
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) ForMaxElectricGreen else ForMaxSurfaceElevated,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) Color(0xFF0B0D0F) else Color.White,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
