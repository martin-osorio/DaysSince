package com.martinosorio.dayssince

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class DaysSinceTest {

    private val zone = ZoneId.of("UTC")

    @Test
    fun sincePicked_sameInstant_returns0() {
        val pickedDate = LocalDate.of(2026, 1, 7)
        val pickedTime = LocalTime.of(0, 0)

        val now = Instant.parse("2026-01-07T00:00:00Z")
        val clock = Clock.fixed(now, zone)

        val days = DaysSince.sincePicked(pickedDate, pickedTime, clock = clock, zoneId = zone)
        assertEquals(0, days)
    }

    @Test
    fun sincePicked_lessThan24Hours_returns0() {
        val pickedDate = LocalDate.of(2026, 1, 6)
        val pickedTime = LocalTime.of(12, 0)

        val now = Instant.parse("2026-01-07T11:59:59Z")
        val clock = Clock.fixed(now, zone)

        val days = DaysSince.sincePicked(pickedDate, pickedTime, clock = clock, zoneId = zone)
        assertEquals(0, days)
    }

    @Test
    fun sincePicked_exactly24Hours_returns1() {
        val pickedDate = LocalDate.of(2026, 1, 6)
        val pickedTime = LocalTime.of(12, 0)

        val now = Instant.parse("2026-01-07T12:00:00Z")
        val clock = Clock.fixed(now, zone)

        val days = DaysSince.sincePicked(pickedDate, pickedTime, clock = clock, zoneId = zone)
        assertEquals(1, days)
    }

    @Test
    fun sincePicked_future_returns0() {
        val pickedDate = LocalDate.of(2026, 1, 8)
        val pickedTime = LocalTime.of(0, 0)

        val now = Instant.parse("2026-01-07T00:00:00Z")
        val clock = Clock.fixed(now, zone)

        val days = DaysSince.sincePicked(pickedDate, pickedTime, clock = clock, zoneId = zone)
        assertEquals(0, days)
    }
}
