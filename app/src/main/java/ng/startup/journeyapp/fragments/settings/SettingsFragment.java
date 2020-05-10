package ng.startup.journeyapp.fragments.settings;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ng.startup.journeyapp.R;
import ng.startup.journeyapp.alarm_manager.LocalData;
import ng.startup.journeyapp.alarm_manager.NotificationScheduler;
import ng.startup.journeyapp.alarm_manager.AlarmReceiverPrompt;
import ng.startup.journeyapp.alarm_manager.AlarmReceiverReflect;
import ng.startup.journeyapp.authentication.LoginScreen;

/**
 * Created by mungwaagu on 05/11/2018.
 */

public class SettingsFragment extends Fragment{

    EditText dailyPromptField, reflectTimeField;
    Switch journalSwitch, quotesSwitch;

    String TAG = "RemindMe";
    LocalData localData;

    int hour_prompt, min_prompt, hour_reflect, min_reflect;

    public static final String MyStoredSettingsPREFERENCES = "MySettingsPrefs" ;
    public static final String JOURNAL_SWITCH = "journal";
    public static final String QUOTES_SWITCH = "quotes";
    SharedPreferences sharedpreferencesSettings;

    public static final String MyStoredPREFERENCES = "MyLoginPrefs" ;
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    SharedPreferences sharedpreferences;

    String usernameString, journalString, quoteString;

    TextView textUsername, textDeactivate, signoutText;
    String signinStatus;


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View x = inflater.inflate(R.layout.fragment_settings, null);

        sharedpreferencesSettings = getActivity().getSharedPreferences(MyStoredSettingsPREFERENCES, Context.MODE_PRIVATE);
        String storedJournalSwitch = sharedpreferencesSettings.getString(JOURNAL_SWITCH, "");
        String storedQuoteSwitch = sharedpreferencesSettings.getString(QUOTES_SWITCH, "");



        journalString = "true";
        quoteString = "true";

        sharedpreferences = getActivity().getSharedPreferences(MyStoredPREFERENCES, Context.MODE_PRIVATE);
        usernameString = sharedpreferences.getString(USERNAME, "");

        textUsername = (TextView) x.findViewById(R.id.textviewUsername);
        textDeactivate = (TextView) x.findViewById(R.id.textviewDeactivate);
        signoutText = (TextView) x.findViewById(R.id.signoutButton);

        journalSwitch = (Switch) x.findViewById(R.id.switchJournal);
        quotesSwitch = (Switch) x.findViewById(R.id.switchQuotes);

        if(storedJournalSwitch.equalsIgnoreCase("true")){
            journalSwitch.setChecked(true);
        }else if (storedJournalSwitch.equalsIgnoreCase("false")){
            journalSwitch.setChecked(false);
        }

        if(storedQuoteSwitch.equalsIgnoreCase("true")){
            quotesSwitch.setChecked(true);
        }else if (storedQuoteSwitch.equalsIgnoreCase("false")){
            quotesSwitch.setChecked(false);
        }

        journalSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    journalString = "true";
                    sharedpreferencesSettings = getActivity().getSharedPreferences(MyStoredSettingsPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferencesSettings.edit();
                    editor.putString(JOURNAL_SWITCH, journalString);
                    editor.apply();
                }
                if (!isChecked){
                    journalString = "false";
                    sharedpreferencesSettings = getActivity().getSharedPreferences(MyStoredSettingsPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferencesSettings.edit();
                    editor.putString(JOURNAL_SWITCH, journalString);
                    editor.apply();
                }
            }
        });

        quotesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    quoteString = "true";
                    sharedpreferencesSettings = getActivity().getSharedPreferences(MyStoredSettingsPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferencesSettings.edit();
                    editor.putString(QUOTES_SWITCH, quoteString);
                    editor.apply();
                }
                if (!isChecked){
                    quoteString = "false";
                    sharedpreferencesSettings = getActivity().getSharedPreferences(MyStoredSettingsPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferencesSettings.edit();
                    editor.putString(QUOTES_SWITCH, quoteString);
                    editor.apply();
                }
            }
        });

        if(usernameString.equalsIgnoreCase("")){
            textUsername.setVisibility(View.INVISIBLE);
            textDeactivate.setVisibility(View.INVISIBLE);
            signinStatus = "Sign In";
            signoutText.setText(signinStatus);

        }else{
            textUsername.setVisibility(View.VISIBLE);
            textDeactivate.setVisibility(View.VISIBLE);
            signinStatus = "Sign Out";
            signoutText.setText(signinStatus);
            textUsername.setText(usernameString);
        }

        if (signinStatus.equalsIgnoreCase("Sign In")){
            signoutText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginScreen.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }else if(signinStatus.equalsIgnoreCase("Sign Out")){
            signoutText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedpreferences = getActivity().getSharedPreferences(MyStoredPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.remove(USERNAME).commit();
                    editor.remove(PASSWORD).commit();

                    Intent intent = new Intent(getActivity(), LoginScreen.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }


        localData = new LocalData(getActivity());

        hour_prompt = localData.get_hour_prompt();
        min_prompt = localData.get_min_prompt();
        hour_reflect = localData.get_hour_reflect();
        min_reflect = localData.get_min_reflect();

        dailyPromptField = (EditText) x.findViewById(R.id.promptTime);
        reflectTimeField = (EditText) x.findViewById(R.id.reflectTime);

        dailyPromptField.setText(getFormatedTime(hour_prompt, min_prompt));
        reflectTimeField.setText(getFormatedTime(hour_reflect, min_reflect));


        dailyPromptField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTimePickerDialogForPrompt(localData.get_hour_prompt(), localData.get_min_prompt());

            }
        });

        reflectTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showTimePickerDialogForReflect(localData.get_hour_reflect(), localData.get_min_reflect());
               
            }
        });

        return x;
    }

    private void showTimePickerDialogForPrompt(int h, int m) {

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                dailyPromptField.setText(getFormatedTime(selectedHour, selectedMinute));
                Log.d(TAG, "onTimeSet: hour " + selectedHour);
                Log.d(TAG, "onTimeSet: min " + selectedMinute);
                localData.set_hour_prompt(selectedHour);
                localData.set_min_prompt(selectedMinute);
                NotificationScheduler.setReminderPrompt(getActivity(), AlarmReceiverPrompt.class,
                        localData.get_hour_prompt(), localData.get_min_prompt());
            }
        }, h, m, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void showTimePickerDialogForReflect(int h, int m) {

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                reflectTimeField.setText(getFormatedTime(selectedHour, selectedMinute));
                Log.d(TAG, "onTimeSet: hour " + selectedHour);
                Log.d(TAG, "onTimeSet: min " + selectedMinute);
                localData.set_hour_reflect(selectedHour);
                localData.set_min_reflect(selectedMinute);
                NotificationScheduler.setReminderReflect(getActivity(), AlarmReceiverReflect.class,
                        localData.get_hour_reflect(), localData.get_min_reflect());
            }
        }, h, m, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    public String getFormatedTime(int h, int m) {
        final String OLD_FORMAT = "HH:mm";
        final String NEW_FORMAT = "hh:mm a";

        String oldDateString = h + ":" + m;
        String newDateString = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, getCurrentLocale());
            Date d = sdf.parse(oldDateString);
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDateString;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

}
