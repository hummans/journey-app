package ng.startup.journeyapp.authentication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ng.startup.journeyapp.HomeScreen;
import ng.startup.journeyapp.R;
import ng.startup.journeyapp.network.JSONParser;

public class LoginScreen extends AppCompatActivity {

    TextView skipText;
    EditText usernameField, passwordField;

    public static final String MyStoredPREFERENCES = "MyLoginPrefs" ;
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    SharedPreferences sharedpreferences;

    String usernameString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        usernameField = (EditText) findViewById(R.id.usernameField);
        passwordField = (EditText) findViewById(R.id.passwordField);

        skipText = (TextView) findViewById(R.id.textSkip);

        skipText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void verifyLoginDetails(View view) {

        usernameString = usernameField.getText().toString();
        passwordString = passwordField.getText().toString();

        if(!passwordString.equalsIgnoreCase("") && !usernameString.equalsIgnoreCase("")) {
            loginFunction (usernameString, passwordString);
        }else{
            Toast.makeText(getApplicationContext(), "Enter your details", Toast.LENGTH_LONG).show();
        }


    }

    private void loginFunction (final String username, String password) {

        class LoginAsync extends AsyncTask<String, String, JSONObject> {

            JSONParser jsonParser = new JSONParser();
            private Dialog loadingDialog;
            private static final String LOGIN_URL = "http://journey.developervisiongroup.com/login.php";
            private static final String TAG_SUCCESS = "success";
            private static final String TAG_MESSAGE = "message";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(LoginScreen.this, "Please wait", "Verifying Details...");
            }

            @Override
            protected JSONObject  doInBackground(String... args) {

                try{
                    HashMap<String, String> paramsPost = new HashMap<>();
                    paramsPost.put("username", args[0]);
                    paramsPost.put("password", args[1]);

                    Log.d("request", "starting");

                    JSONObject json = jsonParser.makeHttpRequest(
                            LOGIN_URL, "POST", paramsPost);

                    if (json != null) {
                        Log.d("JSON result", json.toString());

                        return json;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }


            @Override
            protected void onPostExecute(JSONObject json){
                int success = 0;
                String message = "";

                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }

                if (json != null) {

                    try {
                        success = json.getInt(TAG_SUCCESS);
                        message = json.getString(TAG_MESSAGE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (success == 1) {

                    sharedpreferences = getSharedPreferences(MyStoredPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(USERNAME, usernameString);
                    editor.putString(PASSWORD, passwordString);
                    editor.apply();

                    Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
                    finish();
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password);

    }
}
