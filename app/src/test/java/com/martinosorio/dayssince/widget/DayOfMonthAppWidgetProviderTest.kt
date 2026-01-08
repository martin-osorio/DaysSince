package com.martinosorio.dayssince.widget

import android.widget.TextView
import com.martinosorio.dayssince.Prefs
import com.martinosorio.dayssince.R
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class DayOfMonthAppWidgetProviderTest {

    @Test
    fun buildRemoteViews_rendersNumericText() {
        val context = RuntimeEnvironment.getApplication()

        // Set a deterministic picked date/time.
        val prefs = Prefs.get(context)
        prefs.edit().clear().commit()
        prefs.edit()
            .putString("selected_date", "2026-01-01")
            .putString("selected_time", "00:00")
            .commit()

        val provider = DayOfMonthAppWidgetProvider()

        // Call provider logic and apply the RemoteViews. This avoids Robolectric's
        // appwidget-provider XML handling entirely.
        val remoteViews = provider
            .javaClass
            .getDeclaredMethod("buildRemoteViews", android.content.Context::class.java)
            .apply { isAccessible = true }
            .invoke(provider, context) as android.widget.RemoteViews

        val view = remoteViews.apply(context, null)
        val tv = view.findViewById<TextView>(R.id.widget_day_number)
        assertNotNull(tv)

        val text = tv.text?.toString().orEmpty()
        assertTrue(text.isNotBlank())
        assertTrue(text.all { ch -> ch.isDigit() })
    }
}
