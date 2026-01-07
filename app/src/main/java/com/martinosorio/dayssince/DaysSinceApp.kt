package com.martinosorio.dayssince

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.martinosorio.dayssince.ui.theme.DaysSinceTheme
import java.time.LocalDate

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
                    text = "Days Since",
                    style = MaterialTheme.typography.headlineMedium
                )

                DayOfMonthWidget(
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun DayOfMonthWidget(
    modifier: Modifier = Modifier,
    today: LocalDate = LocalDate.now()
) {
    Text(
        modifier = modifier,
        text = today.dayOfMonth.toString(),
        style = MaterialTheme.typography.titleMedium
    )
}
