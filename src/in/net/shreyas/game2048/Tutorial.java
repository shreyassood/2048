package in.net.shreyas.game2048;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class Tutorial extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		// Show the Up button in the action bar.
	}
	public void close(View view){
    	this.finish();
    }
	@Override
    public void onBackPressed() {
        this.finish();
    }
	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
        	View decorView = this.getWindow().getDecorView();
        	setFull(decorView);}
    }
	public void setFull(View decor){
    	decor.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}
