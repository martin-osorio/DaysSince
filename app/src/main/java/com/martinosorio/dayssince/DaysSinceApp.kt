package com.martinosorio.dayssince

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.martinosorio.dayssince.ui.theme.DaysSinceTheme
import java.time.LocalDate
import java.time.LocalTime

private const val PREF_SELECTED_DATE = "selected_date"
private const val PREF_SELECTED_TIME = "selected_time"

@Composable
fun DaysSinceApp(darkTheme: Boolean = true) {
    DaysSinceTheme(darkTheme = darkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Days Since January 1st, 2026",
                    style = MaterialTheme.typography.headlineMedium
                )

                DaysSinceWidget(
                    modifier = Modifier.padding(top = 24.dp)
                )

                NativePickers(
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun DaysSinceWidget(
    modifier: Modifier = Modifier
) {
    val days = DaysSince.sinceJan1st2026()

    Text(
        modifier = modifier,
        text = days.toString(),
        style = MaterialTheme.typography.displayMedium
    )
}

@Composable
private fun NativePickers(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember(context) { Prefs.get(context) }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now().withSecond(0).withNano(0)) }

    // Load persisted values once.
    LaunchedEffect(prefs) {
        prefs.getString(PREF_SELECTED_DATE, null)
            ?.runCatching(LocalDate::parse)
            ?.getOrNull()
            ?.let { selectedDate = it }

        prefs.getString(PREF_SELECTED_TIME, null)
            ?.runCatching(LocalTime::parse)
            ?.getOrNull()
            ?.let { selectedTime = it }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val dialog = DatePickerDialog(
                    context,
                    { _, year, monthZeroBased, dayOfMonth ->
                        val newDate = LocalDate.of(year, monthZeroBased + 1, dayOfMonth)
                        selectedDate = newDate
                        prefs.edit().putString(PREF_SELECTED_DATE, newDate.toString()).apply()
                    },
                    selectedDate.year,
                    selectedDate.monthValue - 1,
                    selectedDate.dayOfMonth
                )
                dialog.show()
            }
        ) {
            Text("Pick date")
        }

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Selected date: $selectedDate",
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                val dialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        val newTime = LocalTime.of(hourOfDay, minute)
                        selectedTime = newTime
                        prefs.edit().putString(PREF_SELECTED_TIME, newTime.toString()).apply()
                    },
                    selectedTime.hour,
                    selectedTime.minute,
                    true
                )
                dialog.show()
            }
        ) {
            Text("Pick time")
        }

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Selected time: %02d:%02d".format(selectedTime.hour, selectedTime.minute),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
