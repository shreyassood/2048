package in.net.shreyas.game2048;

import com.google.android.gms.games.Games;
import com.google.android.gms.plus.PlusOneButton;
import com.google.example.games.basegameutils.BaseGameActivity;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class Start extends BaseGameActivity
implements View.OnClickListener, OnMenuItemClickListener  {
	
	PlusOneButton mPlusOneButton;
	private static final int PLUS_ONE_REQUEST_CODE = 0;
	private static final String URL = "https://market.android.com/details?id=in.net.shreyas.game2048";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
	    RateMeMaybe rmm = new RateMeMaybe(this);
	    rmm.setPromptMinimums(5, 0, 6, 1);
	    rmm.setDialogMessage("Hope you like this app and consider donating to the developer!");
	    rmm.setDialogTitle("Donate");
	    rmm.setPositiveBtn("Sure!");
	    rmm.run();
        mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_button);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.leaderboards).setOnClickListener(this);  
        findViewById(R.id.ac).setOnClickListener(this);
    }
	@Override
	protected void onResume() {
		super.onResume();
		mPlusOneButton.initialize(URL, PLUS_ONE_REQUEST_CODE);
	};
   
    public void open_play(View view){
    	if (isSignedIn()) {
    	Intent intent = new Intent(this, Play.class);
    	startActivity(intent);
    	overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
    	}
    	else{
    		Intent intent = new Intent(this, PlayLess.class);
        	startActivity(intent);
        	overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
    	}
    }
    @Override
	public void onSignInSucceeded() {
        // show sign-out button, hide the sign-in button
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.leaderboards).setVisibility(View.VISIBLE);
        findViewById(R.id.ac).setVisibility(View.VISIBLE);

        // (your code here: update UI, enable functionality that depends on sign in, etc)
    }
    @Override
	public void onSignInFailed() {
        // Sign in has failed. So show the user the sign-in button.
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        findViewById(R.id.leaderboards).setVisibility(View.GONE);
        findViewById(R.id.ac).setVisibility(View.GONE);
    }
    public void more(View view){
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.more, popup.getMenu());
        popup.show();
    }
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tut:
            	Intent intent = new Intent(this, Tutorial.class);
    	    	startActivity(intent);
    	    	overridePendingTransition(R.anim.fade_in,0);
                return true;
            case R.id.mor:
            	Intent intent2 = new Intent(this, More.class);
            	startActivity(intent2);
            	return true;
            case R.id.set:
            	Intent intent3 = new Intent(this, SettingsActivity.class);
            	startActivity(intent3);
            	return true;	
            case R.id.don:
            	String appPackageName = "com.shreyas.donate2"; // getPackageName() from Context or Activity object
            	try {
            	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            	} catch (android.content.ActivityNotFoundException anfe) {
            	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
            	}
            	return true;		
            	
            default:
                return false;
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button) {
            // start the asynchronous sign in flow
            beginUserInitiatedSignIn();
        }
        else if (view.getId() == R.id.leaderboards) {
            // sign out.
        	startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),1);
        }
        else if (view.getId() == R.id.ac) {
            // sign out.
        	startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),1);
        }
    }

  
}
