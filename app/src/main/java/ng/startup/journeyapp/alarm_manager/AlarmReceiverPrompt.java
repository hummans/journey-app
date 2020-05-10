package ng.startup.journeyapp.alarm_manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ng.startup.journeyapp.prompt.PromptActivity;

/**
 * Created by Jaison on 17/06/17.
 */

public class AlarmReceiverPrompt extends BroadcastReceiver {

    String TAG = "AlarmReceiverPrompt";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                NotificationScheduler.setReminderPrompt(context, AlarmReceiverPrompt.class, localData.get_hour_prompt(), localData.get_min_prompt());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        //Trigger the notification
        NotificationScheduler.showNotificationPrompt(context, PromptActivity.class,
                "Time to Set Steps", "Open App Now?");

    }
}
