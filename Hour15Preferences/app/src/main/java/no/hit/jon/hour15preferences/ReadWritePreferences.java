package no.hit.jon.hour15preferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

public class ReadWritePreferences extends Activity {


    //Nøkler til preferences
    public static final String SETTINGS =
            "com.talkingandroid.hour15preferences.settings";
    public static final String FIRST_USE_SETTING =
            "com.talkingandroid.hour15preferences.firstUse";
    public static final String ANTALL_SETTING =
            "com.talkingandroid.hour15preferences.antall";
    public static final String TIME_SETTING =
            "com.talkingandroid.hour15preferences.time";

    TextView textView, textView2, textView3;
    private boolean firstUse;
    private int antallStarter;
    private long timeStamp;


    @Override
    protected void onResume() {

        super.onResume();
        vipref();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_write_preferences);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        // Get Preferences
        SharedPreferences preferences = getSharedPreferences(SETTINGS, MODE_PRIVATE);
        // Read the FIRST_USE_SETTING. Default value=true
        antallStarter = preferences.getInt(ANTALL_SETTING, 0) + 1;
        firstUse = preferences.getBoolean(FIRST_USE_SETTING, true);
        timeStamp = preferences.getLong(TIME_SETTING, 0);

        if (firstUse) {
            textView.setText(R.string.first_time_message);
        } else {
            textView.setText(R.string.return_user_message);
            textView2.setText(
                    "Dette er oppstart nr " + antallStarter + "." +
                            "\n Siste oppstart var \n" + new Date(timeStamp));
        }
        timeStamp = System.currentTimeMillis();
        //timeStamp = new Date().getTime();

    }

    @Override
    protected void onPause() {

        SharedPreferences preferences = getSharedPreferences(SETTINGS, MODE_PRIVATE);
        // Create Preferences Editor and update settings
        Editor editor = preferences.edit();
        editor.putBoolean(FIRST_USE_SETTING, false);
        editor.putInt(ANTALL_SETTING, antallStarter);
        editor.putLong(TIME_SETTING, timeStamp);

        editor.commit();
        super.onPause();
    }


    public void visSettings(View v) {
        Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(i);
    }

    private void vipref() {
        SharedPreferences defPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean likePie = defPrefs.getBoolean("pie", false);
        String pieType = defPrefs.getString("pie_type", null);
        if (likePie) {
            String msg = "Hei vi vet du liker " + pieType + " pie";
            textView3.setText(msg);
        }

    }


}