package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MapType
import com.example.data.model.MatchMode
import com.example.data.model.Tournament
import com.example.ui.components.GlassmorphicCard
import com.example.ui.theme.BorderGlow
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CardNavy
import com.example.ui.theme.CardNavyElevated
import com.example.ui.theme.DeepNavyBg
import com.example.ui.theme.FireBlueDark
import com.example.ui.theme.FireBluePrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen

@Composable
fun TournamentsScreen(
    tournaments: List<Tournament>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedMode: MatchMode?,
    onSelectMode: (MatchMode?) -> Unit,
    selectedMap: MapType?,
    onSelectMap: (MapType?) -> Unit,
    onNavigateTo: (AppScreen, String?) -> Unit,
    onJoinTournament: (Tournament) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavyBg)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🏆 Tournament Arena",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Find, register, and battle in Free Fire custom room scrims.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Search input
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("tournament_search_input"),
                        placeholder = { Text("Search by tournament name or map...", color = TextMuted, fontSize = 14.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = FireBluePrimary)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FireBluePrimary,
                            unfocusedBorderColor = BorderSubtle,
                            focusedContainerColor = CardNavy,
                            unfocusedContainerColor = CardNavy,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Mode Filters
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterModeChip(
                                label = "All Modes",
                                isSelected = selectedMode == null,
                                onClick = { onSelectMode(null) },
                                testTag = "filter_tourney_mode_all"
                            )
                        }
                        items(MatchMode.values()) { mode ->
                            FilterModeChip(
                                label = mode.displayName,
                                isSelected = selectedMode == mode,
                                onClick = { onSelectMode(mode) },
                                testTag = "filter_tourney_mode_${mode.name.lowercase()}"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Map Filters
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            FilterModeChip(
                                label = "All Maps",
                                isSelected = selectedMap == null,
                                onClick = { onSelectMap(null) },
                                testTag = "filter_tourney_map_all"
                            )
                        }
                        items(MapType.values()) { map ->
                            FilterModeChip(
                                label = "📍 ${map.displayName}",
                                isSelected = selectedMap == map,
                                onClick = { onSelectMap(map) },
                                testTag = "filter_tourney_map_${map.name.lowercase()}"
                            )
                        }
                    }
                }
            }

            if (tournaments.isEmpty()) {
                item {
                    GlassmorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🔍", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No Tournaments Found",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Try clearing filters or search query, or host your own tournament.",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
                items(tournaments) { tournament ->
                    TournamentCard(
                        tournament = tournament,
                        onCardClick = { onNavigateTo(AppScreen.TOURNAMENT_DETAILS, tournament.id) },
                        onJoinClick = { onJoinTournament(tournament) }
                    )
                }
            }
        }

        // Floating Action Button: Host / Create Tournament
        FloatingActionButton(
            onClick = { onNavigateTo(AppScreen.CREATE_TOURNAMENT, null) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 100.dp, end = 20.dp)
                .testTag("fab_create_tournament"),
            containerColor = FireBluePrimary,
            contentColor = DeepNavyBg,
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Tournament")
                Text(
                    text = " Host Match",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
