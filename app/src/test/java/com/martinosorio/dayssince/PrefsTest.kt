package com.martinosorio.dayssince

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.robolectric.RuntimeEnvironment

class PrefsTest {

    @Test
    fun prefs_get_returnsSameFileAcrossCalls() {
        val context = RuntimeEnvironment.getApplication()

        val p1 = Prefs.get(context)
        val p2 = Prefs.get(context)

        assertNotNull(p1)
        assertEquals(p1, p2)
    }

    @Test
    fun prefs_roundTripString() {
        val context = RuntimeEnvironment.getApplication()
        val prefs = Prefs.get(context)

        prefs.edit().clear().commit()

        prefs.edit().putString("k", "v").commit()
        assertEquals("v", prefs.getString("k", null))
    }
}
