package com.martinosorio.dayssince.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.widget.RemoteViews
import com.martinosorio.dayssince.R

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

        // Keep it fresh daily (and after reboot if the device/app chooses to).
        scheduleNextMidnightUpdate(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (ACTION_UPDATE_WIDGETS == intent.action) {
            val manager = AppWidgetManager.getInstance(context)
            val component = android.content.ComponentName(context, DayOfMonthAppWidgetProvider::class.java)
            val ids = manager.getAppWidgetIds(component)
            if (ids.isNotEmpty()) {
                onUpdate(context, manager, ids)
            }
        }
    }

    private fun buildRemoteViews(context: Context): RemoteViews {
        val today = Calendar.getInstance()
        val day = today.get(Calendar.DAY_OF_MONTH)

        return RemoteViews(context.packageName, R.layout.widget_day_of_month).apply {
            setTextViewText(R.id.widget_day_number, day.toString())
        }
    }

    private fun scheduleNextMidnightUpdate(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, DayOfMonthAppWidgetProvider::class.java).apply {
            action = ACTION_UPDATE_WIDGETS
        }

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            flags
        )

        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            timeInMillis = now.timeInMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 5)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_MONTH, 1)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC,
            next.timeInMillis,
            pendingIntent
        )
    }

    companion object {
        private const val REQUEST_CODE = 10101
        private const val ACTION_UPDATE_WIDGETS = "com.martinosorio.dayssince.widget.ACTION_UPDATE_WIDGETS"
    }
}

