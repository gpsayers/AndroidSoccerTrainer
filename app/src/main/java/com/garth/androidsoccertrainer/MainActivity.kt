package com.garth.androidsoccertrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val TrainerColors = listOf(
    Color(0xFFE53935),
    Color(0xFF1E88E5),
    Color(0xFF2E7D32),
    Color(0xFFFFD600),
    Color(0xFFFB8C00)
)
private val TrainerColorNames = listOf("Red", "Blue", "Green", "Yellow", "Orange")
private val SpeedOptions = listOf(7, 5, 3, 2, 1)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF245C48),
                    onPrimary = Color.White,
                    background = Color(0xFFF3F3EE),
                    surface = Color(0xFFF3F3EE),
                    onSurface = Color(0xFF202521)
                )
            ) {
                ColorTrainerScreen()
            }
        }
    }
}

@Composable
private fun ColorTrainerScreen() {
    val selectedColorIndices = remember { mutableStateListOf(0, 1, 2, 3) }
    var selectedSpeedIndex by remember { mutableIntStateOf(0) }
    var currentColorIndex by remember { mutableIntStateOf(-1) }
    var currentNumber by remember { mutableIntStateOf(1) }
    var showNumber by remember { mutableStateOf(false) }
    var isRunning by remember { mutableStateOf(false) }

    val selectedColors = selectedColorIndices.toList()
    val selectedSpeedSeconds = SpeedOptions[selectedSpeedIndex]

    LaunchedEffect(isRunning, selectedColors, selectedSpeedIndex) {
        if (isRunning) {
            if (currentColorIndex !in selectedColors) {
                currentColorIndex = nextColorIndex(selectedColors)
                currentNumber = (1..9).random()
            }
            while (true) {
                delay(selectedSpeedSeconds * 1000L)
                currentColorIndex = nextColorIndex(selectedColors)
                currentNumber = (1..9).random()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3EE))
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Soccer Trainer",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF202521)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ColorMultiSelect(
                selectedColorIndices = selectedColorIndices,
                showNumber = showNumber,
                onShowNumberChange = { showNumber = it },
                modifier = Modifier.weight(1f)
            )
            ChoiceDropdown(
                label = "Speed",
                value = "$selectedSpeedSeconds ${if (selectedSpeedSeconds == 1) "second" else "seconds"}",
                options = SpeedOptions.map { "$it ${if (it == 1) "second" else "seconds"}" },
                onSelected = { selectedSpeedIndex = it },
                modifier = Modifier.weight(1f)
            )
        }

        val activeColor = TrainerColors.getOrNull(currentColorIndex)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(activeColor ?: Color(0xFFE1E3DE), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (showNumber && activeColor != null) {
                Text(
                    text = currentNumber.toString(),
                    fontSize = 180.sp,
                    fontWeight = FontWeight.Black,
                    color = if (currentColorIndex >= 3) Color(0xFF202521) else Color.White
                )
            }
        }

        Text(
            text = if (isRunning) {
                "Running  -  changes every $selectedSpeedSeconds ${if (selectedSpeedSeconds == 1) "second" else "seconds"}"
            } else {
                "Ready when you are"
            },
            fontSize = 14.sp,
            color = Color(0xFF626A64),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Button(
            onClick = {
                if (isRunning) {
                    isRunning = false
                } else {
                    currentColorIndex = nextColorIndex(selectedColors)
                    currentNumber = (1..9).random()
                    isRunning = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRunning) Color(0xFFB53A32) else Color(0xFF245C48)
            )
        ) {
            Text(
                text = if (isRunning) "Stop" else "Start",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorMultiSelect(
    selectedColorIndices: MutableList<Int>,
    showNumber: Boolean,
    onShowNumberChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = "${selectedColorIndices.size} selected",
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text("Colors") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TrainerColorNames.forEachIndexed { index, colorName ->
                DropdownMenuItem(
                    text = {
                        Box(
                            modifier = Modifier
                                .width(42.dp)
                                .height(24.dp)
                                .background(TrainerColors[index], RoundedCornerShape(4.dp))
                                .border(1.dp, Color(0xFF626A64), RoundedCornerShape(4.dp))
                                .semantics { contentDescription = colorName }
                        )
                    },
                    trailingIcon = {
                        Checkbox(
                            checked = index in selectedColorIndices,
                            onCheckedChange = null
                        )
                    },
                    onClick = {
                        if (index in selectedColorIndices) {
                            if (selectedColorIndices.size > 1) {
                                selectedColorIndices.remove(index)
                            }
                        } else {
                            selectedColorIndices.add(index)
                        }
                    }
                )
            }
            DropdownMenuItem(
                text = { Text("Number") },
                trailingIcon = {
                    Checkbox(
                        checked = showNumber,
                        onCheckedChange = null
                    )
                },
                onClick = { onShowNumberChange(!showNumber) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChoiceDropdown(
    label: String,
    value: String,
    options: List<String>,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(index)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun nextColorIndex(selectedColorIndices: List<Int>): Int = selectedColorIndices.random()
