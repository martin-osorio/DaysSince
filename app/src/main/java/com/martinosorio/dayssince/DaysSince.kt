package com.martinosorio.dayssince

import java.time.Clock
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object DaysSince {
    // Contract: returns >= 0, where Jan 1, 2026 => 0.
    // For dates before Jan 1, 2026, we clamp to 0.
    fun sinceJan1st2026(clock: Clock = Clock.systemDefaultZone()): Long {
        val start = LocalDate.of(2026, 1, 1)
        val today = LocalDate.now(clock)
        val days = ChronoUnit.DAYS.between(start, today)
        return if (days < 0) 0 else days
    }
}

