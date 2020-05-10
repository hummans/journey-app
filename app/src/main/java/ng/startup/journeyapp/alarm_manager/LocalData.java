package ng.startup.journeyapp.alarm_manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jaison on 18/06/17.
 */

public class LocalData {

    private static final String APP_SHARED_PREFS = "RemindMePref";

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    private static final String reminderStatusPrompt ="reminderStatusPrompt";
    private static final String reminderStatusReflect ="reminderStatusReflect";
    private static final String hour_prompt ="hour_prompt";
    private static final String min_prompt ="min_prompt";
    private static final String hour_reflect ="hour_reflect";
    private static final String min_reflect ="min_reflect";

    public LocalData(Context context)
    {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    // Settings Page Set Reminder

    public boolean getReminderStatusPrompt()
    {
        return appSharedPrefs.getBoolean(reminderStatusPrompt, false);
    }

    public boolean getReminderStatusReflect()
    {
        return appSharedPrefs.getBoolean(reminderStatusPrompt, false);
    }

    public void setReminderStatusPrompt(boolean status)
    {
        prefsEditor.putBoolean(reminderStatusPrompt, status);
        prefsEditor.commit();
    }

    public void setReminderStatusReflect(boolean status)
    {
        prefsEditor.putBoolean(reminderStatusReflect, status);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Hour)

    public int get_hour_prompt()
    {
        return appSharedPrefs.getInt(hour_prompt, 8);
    }

    public int get_hour_reflect()
    {
        return appSharedPrefs.getInt(hour_reflect, 21);
    }

    public void set_hour_prompt(int h)
    {
        prefsEditor.putInt(hour_prompt, h);
        prefsEditor.commit();
    }

    public void set_hour_reflect(int h)
    {
        prefsEditor.putInt(hour_reflect, h);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Minutes)

    public int get_min_prompt()
    {
        return appSharedPrefs.getInt(min_prompt, 0);
    }

    public int get_min_reflect()
    {
        return appSharedPrefs.getInt(min_reflect, 0);
    }

    public void set_min_prompt(int m)
    {
        prefsEditor.putInt(min_prompt, m);
        prefsEditor.commit();
    }

    public void set_min_reflect(int m)
    {
        prefsEditor.putInt(min_reflect, m);
        prefsEditor.commit();
    }

    public void reset()
    {
        prefsEditor.clear();
        prefsEditor.commit();

    }

}