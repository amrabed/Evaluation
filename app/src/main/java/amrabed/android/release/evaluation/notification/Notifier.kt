package amrabed.android.release.evaluation.notification

import amrabed.android.release.evaluation.MainActivity
import amrabed.android.release.evaluation.R
import amrabed.android.release.evaluation.locale.LocaleManager
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import org.joda.time.LocalTime

private val TAG = Notifier::class.java.name

/**
 * Notifier to set up and show reminder notifications
 */
class Notifier : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null) {
            LocaleManager.setLocale(context.applicationContext)
            showNotification(context)
        }
    }
    companion object {

        private var intent: PendingIntent? = null
        private fun showNotification(context: Context) {
            val channelId = "Reminder Channel"
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager.createNotificationChannel(NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT))
            }
            Log.i(TAG, "Showing Notification")
            val intent = TaskStackBuilder.create(context)
                    .addParentStack(MainActivity::class.java)
                    .addNextIntent(Intent(context, MainActivity::class.java))
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            val notification = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(context.getString(R.string.notification_content))
                    .setContentIntent(intent)
                    .setAutoCancel(true)
                    .build()
            manager.notify(0, notification)
        }

        fun createNotificationChannel(appContext: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(TAG, appContext.getString(R.string.app_name),
                        NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = appContext.getString(R.string.app_name)
                val manager = appContext.getSystemService(NotificationManager::class.java)
                manager?.createNotificationChannel(channel)
            }
        }

        fun scheduleNotifications(context: Context?) {
            Log.i(TAG, "Scheduling notification")
            val intent = getPendingIntent(context)
            getAlarmManager(context).setInexactRepeating(AlarmManager.RTC,
                    LocalTime(19, 0).toDateTimeToday().millis,
                    AlarmManager.INTERVAL_DAY, intent)
        }

        fun cancelNotifications(context: Context?) {
            Log.i(TAG, "Cancelling notification")
            getAlarmManager(context).cancel(getPendingIntent(context))
        }


        private fun getPendingIntent(context: Context?): PendingIntent? {
            if (intent != null) {
                return intent
            }
            intent = PendingIntent.getBroadcast(context, 0,
                    Intent(context, Notifier::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
            return intent
        }

        private fun getAlarmManager(context: Context?): AlarmManager {
            return context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }
    }
}