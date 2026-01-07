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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
                    text = "Days Since Picked Date/Time",
                    style = MaterialTheme.typography.headlineMedium
                )

                DaysSinceWidget(
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

    val daysSincePicked by remember(selectedDate, selectedTime) {
        derivedStateOf { DaysSince.sincePicked(selectedDate, selectedTime) }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = daysSincePicked.toString(),
            style = MaterialTheme.typography.displayMedium
        )

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = "From: $selectedDate at %02d:%02d".format(selectedTime.hour, selectedTime.minute),
            style = MaterialTheme.typography.bodyLarge
        )

        NativePickers(
            modifier = Modifier.padding(top = 24.dp),
            selectedDate = selectedDate,
            selectedTime = selectedTime,
            onSelectedDateChange = {
                selectedDate = it
                prefs.edit().putString(PREF_SELECTED_DATE, it.toString()).apply()
            },
            onSelectedTimeChange = {
                selectedTime = it
                prefs.edit().putString(PREF_SELECTED_TIME, it.toString()).apply()
            }
        )
    }
}

@Composable
private fun NativePickers(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    selectedTime: LocalTime,
    onSelectedDateChange: (LocalDate) -> Unit,
    onSelectedTimeChange: (LocalTime) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val dialog = DatePickerDialog(
                    context,
                    { _, year, monthZeroBased, dayOfMonth ->
                        onSelectedDateChange(LocalDate.of(year, monthZeroBased + 1, dayOfMonth))
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

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                val dialog = TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        onSelectedTimeChange(LocalTime.of(hourOfDay, minute))
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
    }
}
