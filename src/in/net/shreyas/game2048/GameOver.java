package in.net.shreyas.game2048;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameOver extends Activity {
	public static final String PREFS_NAME = "MyScores";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		MyApp appState = ((MyApp)getApplicationContext());
		if(appState!=null){
		if(appState.getg().sign()){
			ImageButton i = (ImageButton) findViewById(R.id.lead);
			i.setVisibility(View.VISIBLE);
		}
		}
		TextView score = (TextView) findViewById(R.id.gamescore);
		score.setText("Score: " + String.valueOf(appState.getg().game.score));
		TextView highscore = (TextView) findViewById(R.id.gamehighscore);
		SharedPreferences scores = getSharedPreferences(PREFS_NAME, 0);
	    long high = scores.getLong("high", 0);
	    if(high<appState.getg().game.score){
	    SharedPreferences.Editor editor = scores.edit();
	    editor.putLong("high", appState.getg().game.score);
	    editor.commit();
	    highscore.setText("High Score: " + String.valueOf(appState.getg().game.score));
	    }
	    else{
		highscore.setText("High Score: " + String.valueOf(high));
	    }


	}
   
    public void newgame_p(View view){
    	MyApp appState = ((MyApp)getApplicationContext());
        appState.getg().game.newGame();
        appState.getg().over = true;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        Tile[][] field = appState.getg().game.grid.field;
        editor.putInt(Play.WIDTH, field.length);
        editor.putInt(Play.HEIGHT, field.length);
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] != null) {
                    editor.putInt(xx + " " + yy, field[xx][yy].getValue());
                } else {
                    editor.putInt(xx + " " + yy, 0);
                }
            }
        }
        editor.putLong(Play.SCORE, appState.getg().game.score);
        editor.putLong(Play.HIGH_SCORE, appState.getg().game.highScore);
        editor.putBoolean(Play.WON, appState.getg().game.won);
        editor.putBoolean(Play.LOSE, appState.getg().game.lose);
        editor.commit();
        this.finish();
    }
    public void lead_p(View view){
    	MyApp appState = ((MyApp)getApplicationContext());
    	appState.getg().lead();
       }
    public void share_p(View view){
    	MyApp appState = ((MyApp)getApplicationContext());
    	Intent sendIntent = new Intent();
    	sendIntent.setAction(Intent.ACTION_SEND);
    	sendIntent.putExtra(Intent.EXTRA_TEXT, "I just scored " + appState.getg().game.score + " points in 2048. Get it here: https://play.google.com/store/apps/details?id=in.net.shreyas.game2048");
    	sendIntent.setType("text/plain");
    	startActivity(Intent.createChooser(sendIntent, "Share score:"));
    }
    @Override
    public void onBackPressed() {
    	/*MyApp appState = ((MyApp)getApplicationContext());
        appState.getg().game.newGame();
        appState.getg().over = true;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        Tile[][] field = appState.getg().game.grid.field;
        editor.putInt(Play.WIDTH, field.length);
        editor.putInt(Play.HEIGHT, field.length);
        for (int xx = 0; xx < field.length; xx++) {
            for (int yy = 0; yy < field[0].length; yy++) {
                if (field[xx][yy] != null) {
                    editor.putInt(xx + " " + yy, field[xx][yy].getValue());
                } else {
                    editor.putInt(xx + " " + yy, 0);
                }
            }
        }
        editor.putLong(Play.SCORE, appState.getg().game.score);
        editor.putLong(Play.HIGH_SCORE, appState.getg().game.highScore);
        editor.putBoolean(Play.WON, appState.getg().game.won);
        editor.putBoolean(Play.LOSE, appState.getg().game.lose);
        editor.commit();*/
        this.finish();
    }
}
