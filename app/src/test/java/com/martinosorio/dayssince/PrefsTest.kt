package com.martinosorio.dayssince

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class PrefsTest {

    @Test
    fun prefs_get_returnsNonNullPrefs() {
        val context: Context = RuntimeEnvironment.getApplication()
        val prefs = Prefs.get(context)
        assertNotNull(prefs)
    }

    @Test
    fun prefs_roundTripString() {
        val context: Context = RuntimeEnvironment.getApplication()
        val prefs = Prefs.get(context)

        prefs.edit().clear().commit()

        prefs.edit().putString("k", "v").commit()
        assertEquals("v", prefs.getString("k", null))
    }

    @Test
    fun prefs_persistsAcrossInstances() {
        val context: Context = RuntimeEnvironment.getApplication()

        val p1 = Prefs.get(context)
        p1.edit().clear().commit()
        p1.edit().putString("k", "v").commit()

        val p2 = Prefs.get(context)
        assertEquals("v", p2.getString("k", null))
    }
}
