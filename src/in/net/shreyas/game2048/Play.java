package in.net.shreyas.game2048;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Play extends BaseGameActivity implements MyListener {
	MainView gameview;
	final static String WIDTH = "width";
	final static String HEIGHT= "height";
	final static String SCORE = "score";
	final static String MAX = "maxmerged";
	final static String HIGH_SCORE = "high score";
	final static String WON = "won";
	final static String LOSE = "lose";
	boolean isc;
	public static final String PREFS_NAME = "First";
	private SoundPool soundPool;
	  private int soundID;
	  boolean loaded = false;
	  float vol;
	  boolean imm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_play);
        SharedPreferences data = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        imm = sharedPref.getBoolean(SettingsActivity.IMM, true);
        boolean themebutton = sharedPref.getBoolean(SettingsActivity.THEME_BUTTON, false);
        if(themebutton)
        		((ImageButton)findViewById(R.id.themebutton)).setVisibility(View.VISIBLE);
        String theme = sharedPref.getString(SettingsActivity.THEME, "1");
        if(theme.equals("2")){
        	findViewById(R.id.PlayScreen).setBackgroundColor(getResources().getColor(R.color.theme_dark));
        	TextView score = (TextView) findViewById(R.id.Score);
        	TextView score2 = (TextView) findViewById(R.id.Score2);
        	score.setTextColor(getResources().getColor(R.color.white));
        	score2.setTextColor(getResources().getColor(R.color.white));
        }
        if(theme.equals("3")){
        	findViewById(R.id.PlayScreen).setBackgroundColor(getResources().getColor(R.color.classic));
        }
        if(imm==false)findViewById(R.id.PlayScreen).setFitsSystemWindows(true);
        boolean first = data.getBoolean("first", true);
        vol = data.getFloat("vol", 1);
        ImageButton i = (ImageButton) findViewById(R.id.vol);
        if(vol==0){
        	i.setImageResource(R.drawable.button_mute);
    		}
    		else{
    	    	i.setImageResource(R.drawable.button_vol);
    		}
        if(first){
	    	Intent intent = new Intent(this, Tutorial.class);
	    	startActivity(intent);
	    	overridePendingTransition(R.anim.fade_in,0);
	    	SharedPreferences.Editor editor = data.edit();
	    	editor.putBoolean("first", false);
		    editor.commit();
	    }
	    soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
	      @Override
	      public void onLoadComplete(SoundPool soundPool, int sampleId,
	          int status) {
	        loaded = true;
	      }
	    });
	    soundID = soundPool.load(this, R.raw.merge, 1);
        this.UiChangeListener();
        TextView score = (TextView) findViewById(R.id.Score); 
        RelativeLayout gamecanvas = (RelativeLayout) findViewById(R.id.game);
        gameview = new MainView(this,score,this);
        MyApp appState = ((MyApp)getApplicationContext());
        appState.setg(gameview);
        
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gameview.hasSaveState = settings.getBoolean("save_state", false);
        
        if (savedInstanceState != null) {
        	int[][] saveState = new int[gameview.game.grid.field.length][gameview.game.grid.field[0].length];
            for (int xx = 0; xx < saveState.length; xx++) {
                saveState[xx] = savedInstanceState.getIntArray("" + xx);
            }
            for (int xx = 0; xx < saveState.length; xx++) {
                for (int yy = 0; yy < saveState[0].length; yy++) {
                    if (saveState[xx][yy] != 0) {
                        gameview.game.grid.field[xx][yy] = new Tile(xx, yy, saveState[xx][yy]);
                    } else {
                        gameview.game.grid.field[xx][yy] = null;
                    }
                }
            }
            gameview.game.score = savedInstanceState.getLong(SCORE);
            gameview.game.maxmerged = savedInstanceState.getLong(MAX);
            gameview.game.highScore = savedInstanceState.getLong(HIGH_SCORE);
            gameview.game.won = savedInstanceState.getBoolean(WON);
            gameview.game.lose = savedInstanceState.getBoolean(LOSE);
        }
        gamecanvas.addView(gameview);
    }
        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        TextView a = (TextView) findViewById(R.id.tile11);
                        Animation move = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.move); 
                        a.startAnimation(move);
                    }
                }, 800);*/
        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        TextView b = (TextView) findViewById(R.id.tile2);
                        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.fade_in);  
                        b.setBackgroundColor(getResources().getColor(R.color.blue));
                        b.setText("512");
                        b.startAnimation(fade);
                    }
                }, 1200);*/
	public void newGameP(View view){
    	Intent intent = new Intent(this, NewGameP.class);
    	startActivity(intent);
    	overridePendingTransition(R.anim.fade_in,0);
    }
	public void themeP(View view){
    	/**/
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		String theme = sharedPref.getString(SettingsActivity.THEME, "1");
		int t = Integer.parseInt(theme);
		if(t<3){
			t++;
		}
		else t=1;
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(SettingsActivity.THEME, String.valueOf(t));
	    editor.commit();
	    Intent intent = getIntent();
	    //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    finish();
	    startActivity(intent);
	    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
	public void volP(View view){
    	ImageButton i = (ImageButton) findViewById(R.id.vol);
		if(vol==1){
    	vol=0;
    	i.setImageResource(R.drawable.button_mute);
		}
		else{
			vol=1;
	    	i.setImageResource(R.drawable.button_vol);
		}
		SharedPreferences data = getSharedPreferences(PREFS_NAME, 0);
    	SharedPreferences.Editor editor = data.edit();
    	editor.putFloat("vol", vol);
	    editor.commit();
    }
	public void pauseP(View view){
		this.finish();
    }
	public void UiChangeListener()
    {
        final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener (new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    setFull(decorView);
                }
            }
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
        	View decorView = this.getWindow().getDecorView();
        	setFull(decorView);}
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Tile[][] field = gameview.game.grid.field;
        int[][] saveState = new int[field.length][field[0].length];
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] != null) {
                    saveState[xx][yy] = field[xx][yy].getValue();
                } else {
                    saveState[xx][yy] = 0;
                }
            }
        }
        for (int xx = 0; xx < saveState.length; xx++) {
            savedInstanceState.putIntArray("" + xx, saveState[xx]);
        }
        savedInstanceState.putLong(SCORE, gameview.game.score);
        savedInstanceState.putLong(MAX, gameview.game.maxmerged);
        savedInstanceState.putLong(HIGH_SCORE, gameview.game.highScore);
        savedInstanceState.putBoolean(WON, gameview.game.won);
        savedInstanceState.putBoolean(LOSE, gameview.game.lose);
    }
    
    protected void onPause() {
        super.onPause();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        Tile[][] field = gameview.game.grid.field;
        editor.putInt(WIDTH, field.length);
        editor.putInt(HEIGHT, field.length);
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] != null) {
                    editor.putInt(xx + " " + yy, field[xx][yy].getValue());
                } else {
                    editor.putInt(xx + " " + yy, 0);
                }
            }
        }
        editor.putLong(SCORE, gameview.game.score);
        editor.putLong(MAX, gameview.game.maxmerged);
        editor.putLong(HIGH_SCORE, gameview.game.highScore);
        editor.putBoolean(WON, gameview.game.won);
        editor.putBoolean(LOSE, gameview.game.lose);
        editor.commit();
    }

    protected void onResume() {
        super.onResume();

        //Stopping all animations
        if(!gameview.over){
            gameview.game.aGrid.cancelAnimations();
            gameview.over = false;
        }
       
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        for (int xx = 0; xx < gameview.game.grid.field.length; xx++) {
            for (int yy = 0; yy < gameview.game.grid.field[0].length; yy++) {
                int value = settings.getInt(xx + " " + yy, -1);
                if (value > 0) {
                    gameview.game.grid.field[xx][yy] = new Tile(xx, yy, value);
                } else if (value == 0) {
                    gameview.game.grid.field[xx][yy] = null;
                }
            }
        }

        gameview.game.score = settings.getLong(SCORE, gameview.game.score);
        gameview.game.maxmerged = settings.getLong(MAX, gameview.game.maxmerged);
        gameview.game.highScore = settings.getLong(HIGH_SCORE, gameview.game.highScore);
        gameview.game.won = settings.getBoolean(WON, gameview.game.won);
        gameview.game.lose = settings.getBoolean(LOSE, gameview.game.lose);
    }
    
    public void setFull(View decor){
    	if(imm==true){
    	decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    	}
    	}
	public void hideFull(View decor){
    	decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }
	
	@Override
	public void onSignInFailed() {
		
	}
	@Override
	public void onSignInSucceeded() {
		
	}
	@Override
	public void callback(long score, long max) {
		if (isSignedIn()) {
		Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_high_scores), score);
		Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_largest_tile), max);
		}
	}
	@Override
	public void callback2() {
		if (isSignedIn()) {
		Games.Achievements.increment(getApiClient(), getString(R.string.achievement_way_to_go), 1);
		Games.Achievements.increment(getApiClient(), getString(R.string.achievement_road_runner), 1);
		}
	}
	@Override
	public void callback3() {
		if (isSignedIn()) {
		Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_almost_there));
		}
	}
	@Override
	public void callback4() {
		if (isSignedIn()) {
		Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_winner));
		}
	}
	@Override
	public void callback5() {
		if (isSignedIn()) {
		Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_success));
		}
	}
	@Override
	public void callback6() {
		if (isSignedIn()) {
		startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),1);
		}
	}
	@Override
	public boolean signedin() {
		return isSignedIn();
	}
	@Override
	public void mergesound() {
		if (loaded) {
	        soundPool.play(soundID, this.vol, this.vol, 1, 0, 1.8f);
	      }
	}

}
