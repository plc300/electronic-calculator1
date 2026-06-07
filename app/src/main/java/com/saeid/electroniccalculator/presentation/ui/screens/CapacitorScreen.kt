package com.saeid.electroniccalculator.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.saeid.electroniccalculator.R
import com.saeid.electroniccalculator.domain.ElectronicsCalculator

@Composable
fun CapacitorScreen() {
    var capacitance by remember { mutableStateOf("") }
    var voltage by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var selectedMode by remember { mutableStateOf(0) }

    val modes = listOf("Xc (Reactance)", "Energy")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.capacitor_calc),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Mode selection
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(modes.size) { index ->
                FilterChip(
                    selected = selectedMode == index,
                    onClick = { selectedMode = index },
                    label = { Text(modes[index]) }
                )
            }
        }

        // Input fields
        when (selectedMode) {
            0 -> { // Capacitive Reactance
                OutlinedTextField(
                    value = frequency,
                    onValueChange = { frequency = it },
                    label = { Text(stringResource(R.string.frequency)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    value = capacitance,
                    onValueChange = { capacitance = it },
                    label = { Text(stringResource(R.string.capacitance)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            }
            1 -> { // Energy
                OutlinedTextField(
                    value = capacitance,
                    onValueChange = { capacitance = it },
                    label = { Text(stringResource(R.string.capacitance)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    value = voltage,
                    onValueChange = { voltage = it },
                    label = { Text(stringResource(R.string.voltage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            }
        }

        // Calculate button
        Button(
            onClick = {
                try {
                    result = when (selectedMode) {
                        0 -> {
                            val f = frequency.toDoubleOrNull() ?: 0.0
                            val c = capacitance.toDoubleOrNull() ?: 0.0
                            "Xc = ${String.format("%.4f", ElectronicsCalculator.calculateCapacitiveReactance(f, c))} Ω"
                        }
                        else -> {
                            val c = capacitance.toDoubleOrNull() ?: 0.0
                            val v = voltage.toDoubleOrNull() ?: 0.0
                            "E = ${String.format("%.6f", ElectronicsCalculator.calculateCapacitorEnergy(c, v))} J"
                        }
                    }
                } catch (e: Exception) {
                    result = stringResource(R.string.error)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .height(50.dp)
        ) {
            Text(stringResource(R.string.calculate))
        }

        // Clear button
        OutlinedButton(
            onClick = {
                capacitance = ""
                voltage = ""
                frequency = ""
                result = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(stringResource(R.string.clear))
        }

        // Result display
        if (result.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "${stringResource(R.string.result)}: $result",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
