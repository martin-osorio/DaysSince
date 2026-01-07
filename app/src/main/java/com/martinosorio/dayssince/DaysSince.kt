package com.martinosorio.dayssince

import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
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

    /**
     * Whole days since the user-picked date & time in the device time zone.
     *
     * - Returns 0 if the picked timestamp is in the future.
     * - Uses floor whole days (i.e., <24h => 0).
     */
    fun sincePicked(
        pickedDate: LocalDate,
        pickedTime: LocalTime,
        clock: Clock = Clock.systemDefaultZone(),
        zoneId: ZoneId = ZoneId.systemDefault()
    ): Long {
        val start = LocalDateTime.of(pickedDate, pickedTime)
        val now = LocalDateTime.now(clock.withZone(zoneId))
        val days = ChronoUnit.DAYS.between(start, now)
        return if (days < 0) 0 else days
    }
}
