package com.saeid.electroniccalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.saeid.electroniccalculator.data.Language
import com.saeid.electroniccalculator.data.LocalizationManager
import com.saeid.electroniccalculator.presentation.MainApp
import kotlinx.coroutines.launch

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    tertiary = Color(0xFFFF0266)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC6),
    tertiary = Color(0xFFFF0266)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentLanguage by remember { mutableStateOf(Language.ENGLISH) }

            LaunchedEffect(Unit) {
                lifecycleScope.launch {
                    LocalizationManager.getLanguageFlow(this@MainActivity).collect { langCode ->
                        currentLanguage = if (langCode == "fa") Language.PERSIAN else Language.ENGLISH
                    }
                }
            }

            MaterialTheme(
                colorScheme = if (isSystemInDarkMode()) DarkColorScheme else LightColorScheme
            ) {
                MainApp(
                    currentLanguage = currentLanguage,
                    onLanguageChanged = { language ->
                        lifecycleScope.launch {
                            LocalizationManager.setLanguage(
                                this@MainActivity,
                                language.code
                            )
                        }
                    }
                )
            }
        }
    }
}
