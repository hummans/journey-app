package ng.startup.journeyapp.alarm_manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ng.startup.journeyapp.reflect.ReflectActivity;

/**
 * Created by Jaison on 17/06/17.
 */

public class AlarmReceiverReflect extends BroadcastReceiver {

    String TAG = "AlarmReceiverReflect";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                NotificationScheduler.setReminderReflect(context, AlarmReceiverReflect.class, localData.get_hour_reflect(), localData.get_min_reflect());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        //Trigger the notification
        NotificationScheduler.showNotificationReflect(context, ReflectActivity.class,
                "Time to reflect on your day", "Open App now?");

    }
}
