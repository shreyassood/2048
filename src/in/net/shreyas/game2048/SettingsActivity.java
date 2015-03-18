package in.net.shreyas.game2048;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.NavUtils;


public class SettingsActivity extends PreferenceActivity {
	boolean prem;
	public static final String IMM = "pref_imm";
	public static final String THEME = "pref_theme";
	public static final String SENSITIVTY = "pref_sensitivity";
	public static final String THEME_BUTTON = "pref_themebutton";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);	
        //getPreferenceScreen().findPreference("pref_theme").setEnabled(prem);
        
    }
    void Prem(View view){
    	
    }
}

