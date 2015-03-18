package in.net.shreyas.game2048;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


public class MainView extends View {

    Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    public MainGame game;
    public boolean hasSaveState = false;
    public boolean over = false;
    public Context c;
    public TextView score;
    MyListener ml;

    boolean getScreenSize = true;
    int cellSize = 0;
    float cellTextSize = 0;
    float textSize = 0;
    int gridWidth = 0;
    int screenMiddleX = 0;
    int screenMiddleY = 0;
    int boardMiddleX = 0;
    int boardMiddleY = 0;
    Drawable backgroundRectangle;
    Drawable[] cellRectangle = new Drawable[17];
    BitmapDrawable[] bitmapCell= new BitmapDrawable[17];
    Drawable settingsIcon;
    Drawable lightUpRectangle;
    Drawable fadeRectangle;
    Bitmap background = null;
    int TEXT_BLACK;
    int TEXT_WHITE;
    int TEXT_BROWN;
    String theme;


    int halfNumSquaresX;
    int halfNumSquaresY;

    int startingX;
    int startingY;
    int endingX;
    int endingY;

    int sYAll;
    int titleStartYAll;
    int bodyStartYAll;
    int eYAll;
    int titleWidthHighScore;
    int titleWidthScore;

    static int sYIcons;
    static int sXNewGame;

    static int iconSize;
    long lastFPSTime = System.nanoTime();
    long currentTime = System.nanoTime();

    float titleTextSize;
    float bodyTextSize;
    float headerTextSize;
    float instructionsTextSize;
    float gameOverTextSize;

    boolean refreshLastTime = true;

    static final int BASE_ANIMATION_TIME = 100000000;
    static int textPaddingSize = 0;
    static int iconPaddingSize = 0;

    static final float MERGING_ACCELERATION = (float) -0.5;
    static final float INITIAL_VELOCITY = (1 - MERGING_ACCELERATION) / 4;

    @Override
    public void onDraw(Canvas canvas) {
        //Reset the transparency of the screen

        canvas.drawBitmap(background, 0, 0, paint);

        drawScoreText(/*canvas*/);

        drawCells(canvas);

        drawEndGameState(canvas);

        //Refresh the screen if there is still an animation running
        if (game.aGrid.isAnimationActive()) {
            invalidate(startingX, startingY, endingX, endingY);
            tick();
        //Refresh one last time on game end.
        } else if ((game.lose) && refreshLastTime) {
            invalidate();
            refreshLastTime = false;
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        getLayout(width, height);
        createBackgroundBitmap(width, height);
        createBitmapCells();
    }

    public void drawDrawable(Canvas canvas, Drawable draw, int startingX, int startingY, int endingX, int endingY) {
        draw.setBounds(startingX, startingY, endingX, endingY);
        draw.draw(canvas);
    }

    public void drawCellText(Canvas canvas, int value, int sX, int sY) {
        int textShiftY = centerText();
        paint.setColor(TEXT_WHITE);
        if(theme.equals("3")){
        	if (value >= 8) paint.setColor(TEXT_WHITE);
        	else paint.setColor(TEXT_BLACK);
        }
        canvas.drawText("" + value, sX + cellSize / 2, sY + cellSize / 2 - textShiftY, paint);
    }

    public void drawScoreText() { 
       	score.setText(String.valueOf(game.score));
    }


    public void drawBackground(Canvas canvas) {
    	
        drawDrawable(canvas, backgroundRectangle, startingX, startingY, endingX, endingY);
        
    }

    public void drawBackgroundGrid(Canvas canvas) {
        // Outputting the game grid
        for (int xx = 0; xx < game.numSquaresX; xx++) {
            for (int yy = 0; yy < game.numSquaresY; yy++) {
                int sX = startingX + gridWidth + (cellSize + gridWidth) * xx;
                int eX = sX + cellSize;
                int sY = startingY + gridWidth + (cellSize + gridWidth) * yy;
                int eY = sY + cellSize;

                drawDrawable(canvas, cellRectangle[0], sX, sY, eX, eY);
            }
        }
    }

    public void drawCells(Canvas canvas) {
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        // Outputting the individual cells
        for (int xx = 0; xx < game.numSquaresX; xx++) {
            for (int yy = 0; yy < game.numSquaresY; yy++) {
                int sX = startingX + gridWidth + (cellSize + gridWidth) * xx;
                int eX = sX + cellSize;
                int sY = startingY + gridWidth + (cellSize + gridWidth) * yy;
                int eY = sY + cellSize;
                Tile currentTile = game.grid.getCellContent(xx,yy);
                if (currentTile != null) {
                    //Get and represent the value of the tile
                    int value = currentTile.getValue();
                    int index = log2(value);

                    //Check for any active animations
                    ArrayList<AnimationCell> aArray = game.aGrid.getAnimationCell(xx, yy);
                    boolean animated = false;
                    for (int i = aArray.size() - 1; i >= 0; i--) {
                        AnimationCell aCell = aArray.get(i);
                        //If this animation is not active, skip it
                        if (aCell.getAnimationType() == MainGame.SPAWN_ANIMATION) {
                            animated = true;
                        }
                        if (!aCell.isActive()) {
                            continue;
                        }

                        if (aCell.getAnimationType() == MainGame.SPAWN_ANIMATION) { // Spawning animation
                            double percentDone = aCell.getPercentageDone();
                            float textScaleSize = (float) (percentDone);
                            paint.setTextSize(textSize * textScaleSize);

                            float cellScaleSize = cellSize / 2 * (1 - textScaleSize);
                            bitmapCell[index].setBounds((int) (sX + cellScaleSize), (int) (sY + cellScaleSize), (int) (eX - cellScaleSize), (int) (eY - cellScaleSize));
                            bitmapCell[index].draw(canvas);
                        } else if (aCell.getAnimationType() == MainGame.MERGE_ANIMATION) { // Merging Animation
                            double percentDone = aCell.getPercentageDone();
                            float textScaleSize = (float) (1 + INITIAL_VELOCITY * percentDone
                                    + MERGING_ACCELERATION * percentDone * percentDone / 2);
                            paint.setTextSize(textSize * textScaleSize);
                            

                            float cellScaleSize = cellSize / 2 * (1 - textScaleSize);
                            bitmapCell[index].setBounds( (int) (sX + cellScaleSize), (int) (sY + cellScaleSize), (int) (eX - cellScaleSize), (int) (eY - cellScaleSize));
                            bitmapCell[index].draw(canvas);
                        } else if (aCell.getAnimationType() == MainGame.MOVE_ANIMATION) {  // Moving animation
                            double percentDone = aCell.getPercentageDone();
                            int tempIndex = index;
                            if (aArray.size() >= 2) {
                                tempIndex = tempIndex - 1;
                            }
                            int previousX = aCell.extras[0];
                            int previousY = aCell.extras[1];
                            int currentX = currentTile.getX();
                            int currentY = currentTile.getY();
                            int dX = (int) ((currentX - previousX) * (cellSize + gridWidth) * (percentDone - 1) * 1.0);
                            int dY = (int) ((currentY - previousY) * (cellSize + gridWidth) * (percentDone - 1) * 1.0);
                            bitmapCell[tempIndex].setBounds(sX + dX, sY + dY, eX + dX, eY + dY);
                            bitmapCell[tempIndex].draw(canvas);
                        }
                        animated = true;
                    }

                    //No active animations? Just draw the cell
                    if (!animated) {
                        bitmapCell[index].setBounds(sX, sY, eX, eY);
                        bitmapCell[index].draw(canvas);
                    }
                }
            }
        }
    }

    public void drawEndGameState(Canvas canvas) {
        // Displaying game over
        if (game.lose && !game.shown) {
        	ml.callback(this.game.score,this.game.maxmerged);
        	ml.callback2();
            game.shown = true;
        	Intent intent = new Intent(((Activity) c), GameOver.class);
        	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);;
        	((Activity) c).startActivity(intent);
        	((Activity) c).overridePendingTransition(R.anim.fade_in,0);
        }
    }

    public void createBackgroundBitmap(int width, int height) {
        background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        drawBackground(canvas);
        drawBackgroundGrid(canvas);
        //drawInstructions(canvas);

    }
    public void createBitmapCells() {
    	         paint.setTextSize(textSize);
    	         paint.setTextAlign(Paint.Align.CENTER);
    	         Resources resources = getResources();
    	         for (int xx = 0; xx < bitmapCell.length; xx++) {
            int value = (int) Math.pow(2, xx);
            paint.setTextSize(cellTextSize);
            String text = (value<2048)?"0000":String.valueOf(value);
            float tempTextSize = cellTextSize * cellTextSize / Math.max(cellTextSize, paint.measureText(text));
            paint.setTextSize(tempTextSize);
            Bitmap bitmap = Bitmap.createBitmap(cellSize, cellSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawDrawable(canvas, cellRectangle[xx], 0, 0, cellSize, cellSize);
            drawCellText(canvas, value, 0, 0);
            bitmapCell[xx] = new BitmapDrawable(resources, bitmap);
    	         }
    	    }
    public void almost(){
    	ml.callback3();
    }
    public void winner(){
    	ml.callback4();
    }
    public void success(){
    	ml.callback5();
    }
    public void lead(){
    	ml.callback6();
    }
    public boolean sign(){
    	return ml.signedin();
    }
    public void sound(){
    	ml.mergesound();
    }

    public void tick() {
        currentTime = System.nanoTime();
        game.aGrid.tickAll(currentTime - lastFPSTime);
        lastFPSTime = currentTime;
    }

    public void resyncTime() {
        lastFPSTime = System.nanoTime();
    }

    public static int log2(int n){
        if(n <= 0) throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    public void getLayout(int width, int height) {
        gridWidth = 2;
        cellSize = Math.min(width / (game.numSquaresX) - gridWidth*2, height / (game.numSquaresY) - gridWidth*2);
    	cellTextSize = cellSize * 0.9f;
        screenMiddleX = width / 2;
        screenMiddleY = height / 2;
        boardMiddleX = screenMiddleX;
        boardMiddleY = screenMiddleY;//+ cellSize / 2;

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(cellSize);
        textSize = cellSize * cellSize / Math.max(cellSize, paint.measureText("0000"));
        textPaddingSize = (int) (textSize / 3);
        iconPaddingSize = (int) (textSize / 5);

        //Grid Dimensions
        halfNumSquaresX = game.numSquaresX / 2;
        halfNumSquaresY = game.numSquaresY / 2;

        startingX = boardMiddleX - (cellSize + gridWidth) * halfNumSquaresX - gridWidth / 2;
        endingX = boardMiddleX + (cellSize + gridWidth) * halfNumSquaresX + gridWidth / 2;
        startingY = boardMiddleY - (cellSize + gridWidth) * halfNumSquaresY - gridWidth / 2;
        endingY = boardMiddleY + (cellSize + gridWidth) * halfNumSquaresY + gridWidth / 2;

        resyncTime();
        getScreenSize = false;
    }

    public int centerText() {
        return (int) ((paint.descent() + paint.ascent()) / 2);
    }

    public MainView(Context context,TextView score, MyListener ml) {
        super(context);
        this.c = context;
        this.score = score;
        this.ml=ml;
    	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        theme = sharedPref.getString(SettingsActivity.THEME, "1");
        Resources resources = context.getResources();
        //Loading resources
        game = new MainGame(context, this);
        try {
            backgroundRectangle =  resources.getDrawable(R.drawable.background_rectangle);
            cellRectangle[0] =  resources.getDrawable(R.drawable.cell_rectangle);
            cellRectangle[1] =  resources.getDrawable(R.drawable.cell_rectangle_2);
            cellRectangle[2] =  resources.getDrawable(R.drawable.cell_rectangle_4);
            cellRectangle[3] =  resources.getDrawable(R.drawable.cell_rectangle_8);
            cellRectangle[4] =  resources.getDrawable(R.drawable.cell_rectangle_16);
            cellRectangle[5] =  resources.getDrawable(R.drawable.cell_rectangle_32);
            cellRectangle[6] =  resources.getDrawable(R.drawable.cell_rectangle_64);
            cellRectangle[7] =  resources.getDrawable(R.drawable.cell_rectangle_128);
            cellRectangle[8] =  resources.getDrawable(R.drawable.cell_rectangle_256);
            cellRectangle[9] =  resources.getDrawable(R.drawable.cell_rectangle_512);
            cellRectangle[10] = resources.getDrawable(R.drawable.cell_rectangle_1024);
            cellRectangle[11] = resources.getDrawable(R.drawable.cell_rectangle_2048);
            cellRectangle[12] = resources.getDrawable(R.drawable.cell_rectangle_4096);
            cellRectangle[13] = resources.getDrawable(R.drawable.cell_rectangle_4096);
            cellRectangle[14] = resources.getDrawable(R.drawable.cell_rectangle_4096);
            cellRectangle[15] = resources.getDrawable(R.drawable.cell_rectangle_4096);
            cellRectangle[16] = resources.getDrawable(R.drawable.cell_rectangle_4096);
            fadeRectangle = resources.getDrawable(R.drawable.fade_rectangle);
            TEXT_WHITE = resources.getColor(R.color.text_white);
            TEXT_BLACK = resources.getColor(R.color.text_black);
            TEXT_BROWN = resources.getColor(R.color.text_brown);
            if(theme.equals("2")){
            	backgroundRectangle = resources.getDrawable(R.drawable.background_rectangle_dark);
            }
            if(theme.equals("3")){
            	 backgroundRectangle =  resources.getDrawable(R.drawable.background_rectangle_classic);
                 cellRectangle[0] =  resources.getDrawable(R.drawable.cell_rectangle_classic);
                 cellRectangle[1] =  resources.getDrawable(R.drawable.cell_rectangle_2_classic);
                 cellRectangle[2] =  resources.getDrawable(R.drawable.cell_rectangle_4_classic);
                 cellRectangle[3] =  resources.getDrawable(R.drawable.cell_rectangle_8_classic);
                 cellRectangle[4] =  resources.getDrawable(R.drawable.cell_rectangle_16_classic);
                 cellRectangle[5] =  resources.getDrawable(R.drawable.cell_rectangle_32_classic);
                 cellRectangle[6] =  resources.getDrawable(R.drawable.cell_rectangle_64_classic);
                 cellRectangle[7] =  resources.getDrawable(R.drawable.cell_rectangle_128_classic);
                 cellRectangle[8] =  resources.getDrawable(R.drawable.cell_rectangle_256_classic);
                 cellRectangle[9] =  resources.getDrawable(R.drawable.cell_rectangle_512_classic);
                 cellRectangle[10] = resources.getDrawable(R.drawable.cell_rectangle_1024_classic);
                 cellRectangle[11] = resources.getDrawable(R.drawable.cell_rectangle_2048_classic);
                 cellRectangle[12] = resources.getDrawable(R.drawable.cell_rectangle_4096_classic);
                 cellRectangle[13] = resources.getDrawable(R.drawable.cell_rectangle_4096_classic);
                 cellRectangle[14] = resources.getDrawable(R.drawable.cell_rectangle_4096_classic);
                 cellRectangle[15] = resources.getDrawable(R.drawable.cell_rectangle_4096_classic);
                 cellRectangle[16] = resources.getDrawable(R.drawable.cell_rectangle_4096_classic);
            }
            Typeface font = Typeface.create("sans-serif-thin", Typeface.NORMAL);
            paint.setTypeface(font);
        } catch (Exception e) {
            System.out.println("Error getting assets?");
        }
        //Gesture for full activity instead of canvas
        int sens = (int)(10+(sharedPref.getFloat(SettingsActivity.SENSITIVTY, (float) 0.7) * 50));
        ((Activity)c).findViewById(R.id.PlayScreen).setOnTouchListener(new InputListener(this,c, sens));
        game.newGame();
    }

}