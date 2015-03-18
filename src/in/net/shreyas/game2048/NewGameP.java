package in.net.shreyas.game2048;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;

public class NewGameP extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game_p);
	}
   
    public void close_p(View view){
    	this.finish();
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
         editor.putLong(Play.MAX, appState.getg().game.maxmerged);
         editor.putLong(Play.HIGH_SCORE, appState.getg().game.highScore);
         editor.putBoolean(Play.WON, appState.getg().game.won);
         editor.putBoolean(Play.LOSE, appState.getg().game.lose);
         editor.commit();
         this.finish();
    }
}
