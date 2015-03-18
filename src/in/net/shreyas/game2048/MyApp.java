package in.net.shreyas.game2048;

import android.app.Application;

public class MyApp extends Application {
  private MainView g;
  public MainView getg(){
    return g;
  }
  public void setg(MainView g){
    this.g = g; 
  }
}