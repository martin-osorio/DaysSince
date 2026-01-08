package com.martinosorio.dayssince.widget

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import com.martinosorio.dayssince.DaysSince
import com.martinosorio.dayssince.Prefs
import com.martinosorio.dayssince.R
import java.time.LocalDate
import java.time.LocalTime

class DayOfMonthAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        for (appWidgetId in appWidgetIds) {
            appWidgetManager.updateAppWidget(
                appWidgetId,
                buildRemoteViews(context)
            )
        }

        // Recompute periodically so the value stays current relative to "now".
        scheduleHourlyUpdate(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            ACTION_UPDATE_WIDGETS,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_TIME_TICK -> {
                val manager = AppWidgetManager.getInstance(context)
                val component =
                    android.content.ComponentName(context, DayOfMonthAppWidgetProvider::class.java)
                val ids = manager.getAppWidgetIds(component)
                if (ids.isNotEmpty()) {
                    onUpdate(context, manager, ids)
                }
            }
        }
    }

    private fun buildRemoteViews(context: Context): RemoteViews {
        val prefs = Prefs.get(context)

        val pickedDate = prefs.getString(PREF_SELECTED_DATE, null)
            ?.runCatching(LocalDate::parse)
            ?.getOrNull() ?: LocalDate.now()

        val pickedTime = prefs.getString(PREF_SELECTED_TIME, null)
            ?.runCatching(LocalTime::parse)
            ?.getOrNull() ?: LocalTime.MIDNIGHT

        val days = DaysSince.sincePicked(pickedDate, pickedTime)

        return RemoteViews(context.packageName, R.layout.widget_day_of_month).apply {
            setTextViewText(R.id.widget_day_number, days.toString())
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleHourlyUpdate(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, DayOfMonthAppWidgetProvider::class.java).apply {
            action = ACTION_UPDATE_WIDGETS
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Use elapsed realtime for robustness across time changes.
        val firstTriggerElapsed = SystemClock.elapsedRealtime() + 60_000L

        alarmManager.cancel(pendingIntent)
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            firstTriggerElapsed,
            AlarmManager.INTERVAL_HOUR,
            pendingIntent
        )
    }

    companion object {
        private const val REQUEST_CODE = 10101

        // Public so the app UI can broadcast an update when the user changes date/time.
        const val ACTION_UPDATE_WIDGETS = "com.martinosorio.dayssince.widget.ACTION_UPDATE_WIDGETS"

        private const val PREF_SELECTED_DATE = "selected_date"
        private const val PREF_SELECTED_TIME = "selected_time"

        fun requestUpdate(context: Context) {
            val intent = Intent(context, DayOfMonthAppWidgetProvider::class.java).apply {
                action = ACTION_UPDATE_WIDGETS
            }
            context.sendBroadcast(intent)
        }
    }
}
