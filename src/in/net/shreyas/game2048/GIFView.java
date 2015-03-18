package in.net.shreyas.game2048;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GIFView extends View {

private Movie mMovie;
private long movieStart;

public GIFView(Context context) {
    super(context);
    initializeView();
}

public GIFView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeView();
}

public GIFView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initializeView();
}

private void initializeView() {
    InputStream is = getContext().getResources().openRawResource(
            R.drawable.grid);
    mMovie = Movie.decodeStream(is);
}

protected void onDraw(Canvas canvas) {
    canvas.drawColor(Color.TRANSPARENT);
    super.onDraw(canvas);
    long now = android.os.SystemClock.uptimeMillis();

    if (movieStart == 0) {
        movieStart = (int) now;
    }
    if (mMovie != null) {
    	int relTime;
    	if(mMovie.duration()!=0)relTime = (int) ((now - movieStart) % mMovie.duration());
    	else relTime = 0;
        mMovie.setTime(relTime);
        mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight()
                - mMovie.height());
        this.invalidate();
    }
}}