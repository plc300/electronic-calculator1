package com.saeid.electroniccalculator.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.saeid.electroniccalculator.R
import com.saeid.electroniccalculator.domain.ElectronicsCalculator

@Composable
fun OhmLawScreen() {
    var voltage by remember { mutableStateOf("") }
    var current by remember { mutableStateOf("") }
    var resistance by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var selectedMode by remember { mutableStateOf(0) }

    val modes = listOf(
        stringResource(R.string.voltage),
        stringResource(R.string.current),
        stringResource(R.string.resistance),
        stringResource(R.string.power)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.ohm_law),
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
            0 -> { // Calculate Voltage
                OutlinedTextField(
                    value = current,
                    onValueChange = { current = it },
                    label = { Text(stringResource(R.string.current)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    value = resistance,
                    onValueChange = { resistance = it },
                    label = { Text(stringResource(R.string.resistance)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            }
            1 -> { // Calculate Current
                OutlinedTextField(
                    value = voltage,
                    onValueChange = { voltage = it },
                    label = { Text(stringResource(R.string.voltage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    value = resistance,
                    onValueChange = { resistance = it },
                    label = { Text(stringResource(R.string.resistance)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            }
            2 -> { // Calculate Resistance
                OutlinedTextField(
                    value = voltage,
                    onValueChange = { voltage = it },
                    label = { Text(stringResource(R.string.voltage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    value = current,
                    onValueChange = { current = it },
                    label = { Text(stringResource(R.string.current)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            }
            3 -> { // Calculate Power
                OutlinedTextField(
                    value = voltage,
                    onValueChange = { voltage = it },
                    label = { Text(stringResource(R.string.voltage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                OutlinedTextField(
                    value = current,
                    onValueChange = { current = it },
                    label = { Text(stringResource(R.string.current)) },
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
                            val i = current.toDoubleOrNull() ?: 0.0
                            val r = resistance.toDoubleOrNull() ?: 0.0
                            "V = ${String.format("%.4f", ElectronicsCalculator.calculateVoltage(i, r))} V"
                        }
                        1 -> {
                            val v = voltage.toDoubleOrNull() ?: 0.0
                            val r = resistance.toDoubleOrNull() ?: 0.0
                            "I = ${String.format("%.4f", ElectronicsCalculator.calculateCurrent(v, r))} A"
                        }
                        2 -> {
                            val v = voltage.toDoubleOrNull() ?: 0.0
                            val i = current.toDoubleOrNull() ?: 0.0
                            "R = ${String.format("%.4f", ElectronicsCalculator.calculateResistance(v, i))} Ω"
                        }
                        else -> {
                            val v = voltage.toDoubleOrNull() ?: 0.0
                            val i = current.toDoubleOrNull() ?: 0.0
                            "P = ${String.format("%.4f", ElectronicsCalculator.calculatePowerFromVI(v, i))} W"
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
                voltage = ""
                current = ""
                resistance = ""
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
