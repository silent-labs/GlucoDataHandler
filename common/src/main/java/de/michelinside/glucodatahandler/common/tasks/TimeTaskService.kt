package de.michelinside.glucodatahandler.common.tasks

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.michelinside.glucodatahandler.common.notifier.NotifySource

@SuppressLint("StaticFieldLeak")
object TimeTaskService: BackgroundTaskService(42, "GDH.Task.TimeTaskService") {
    override fun getAlarmReceiver() : Class<*> = TimeAlarmReceiver::class.java

    override fun getNotifySourceFilter() : MutableSet<NotifySource> = mutableSetOf(NotifySource.NOTIFIER_CHANGE)

    override fun getBackgroundTasks(): MutableList<BackgroundTask> =
        mutableListOf(ElapsedTimeTask(), ObsoleteTask())
}


class TimeAlarmReceiver(): BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        TimeTaskService.alarmTrigger(intent)
    }
}
