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
