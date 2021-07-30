package com.example.minesweeper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;

public class gridView extends View {
    private Paint TilePaint;
    private Paint TextPaint;
    private int Width;
    private int Height;
    private ArrayList<Tile> TileList = new ArrayList<>();
    public boolean isGameOver = false;
    public boolean ToStart = true;
    public boolean displayMine=false;
    public int Score=0;
    public Bitmap mine;
    public Vibrator Vibrator;
    public int NumberOfMines=0;

    public void GetVibrator(Vibrator vibrator){
        Vibrator=vibrator;
    }

    public gridView(Context context) {
        super(context);
        init(null);
    }

    public gridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public gridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public gridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(@Nullable AttributeSet attributeSet) {
        //Tiles = new Rect();
        TilePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TilePaint.setColor(Color.parseColor("#2A91EB"));
        TilePaint.setStrokeWidth(20);
        TextPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        TextPaint.setTextSize(50);
        TextPaint.setTextAlign(Paint.Align.CENTER);
        mine= BitmapFactory.decodeResource(getResources(),R.drawable.mine_removebg_1__1_);
    }

    private int TileWidth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        Width = getWidth();
        Height = getHeight();
        TileWidth = (Width - 7 * 20 - 100) / 8;
        canvas.drawColor(Color.parseColor("#000000"));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (ToStart) {
                    canvas.drawRoundRect(50 + j * (TileWidth + 20), 200 + i * (TileWidth + 20) + TileWidth, 50 + j * (TileWidth + 20) + TileWidth, 200 + i * (TileWidth + 20), 20, 20, TilePaint);
                    TileList.add(new Tile());
                } else if (!isGameOver) {
                    if (TileList.get(i * 8 + j).visibility == 0) {
                        canvas.drawRoundRect(50 + j * (TileWidth + 20), 200 + i * (TileWidth + 20) + TileWidth, 50 + j * (TileWidth + 20) + TileWidth, 200 + i * (TileWidth + 20), 20, 20, TilePaint);
                    } else if (TileList.get(i * 8 + j).Data == -1) {
                        canvas.drawBitmap(mine,50 + j * (TileWidth + 20) + TileWidth / 2-25, 200 + i * (TileWidth + 20) + TileWidth / 2-25, null);
                    } else {
                        if(TileList.get(i*8+j).Data==0)
                            TextPaint.setColor(Color.parseColor("#880061"));
                        else if(TileList.get(i*8+j).Data==1)
                            TextPaint.setColor(Color.parseColor("#008B00"));
                        else if(TileList.get(i*8+j).Data==2)
                            TextPaint.setColor(Color.parseColor("#C6C400"));
                        else if(TileList.get(i*8+j).Data==3)
                            TextPaint.setColor(Color.parseColor("#F37E00"));
                        else if(TileList.get(i*8+j).Data==4)
                            TextPaint.setColor(Color.parseColor("#0000E4"));
                        TextPaint.setTextSize(50);
                        canvas.drawText("" + TileList.get(i * 8 + j).Data, 50 + j * (TileWidth + 20) + TileWidth / 2, 200 + i * (TileWidth + 20)+25 + TileWidth / 2, TextPaint);
                    }
                }
                else if(isGameOver){
                    TextPaint.setTextSize(150);
                    TextPaint.setColor(Color.parseColor("#FFFFFF"));
                    canvas.drawText("SCORE:" + Score, Width / 2, Height / 2 - 75,TextPaint);
                    TextPaint.setColor(Color.parseColor("#8C8A8A"));
                    TextPaint.setTextSize(100);
                    canvas.drawText("Press Back Button", Width / 2, Height - 200, TextPaint);

                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int x;
        int y;
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !isGameOver && this.getVisibility() == VISIBLE) {
            x = (int) motionEvent.getX() - 50;
            y = (int) motionEvent.getY() - 200;
            if (y >= 0 && x >= 0) {
                if (x % (TileWidth + 20) <= TileWidth)
                    x = x / (TileWidth + 20);
                if (y % (TileWidth + 20) <= TileWidth)
                    y = y / (TileWidth + 20);
                if (x < 8 && y < 8) {
                    if(TileList.get(8*y+x).Data!=-1&&TileList.get(8*y+x).visibility==0) {
                        Score += 1;
                    }
                    if (ToStart) {
                        AssignData(y * 8 + x);
                        VisibleSurroundings(x,y);
                        ToStart = false;
                        Score=0;
                    }


                    TileList.get(8 * y + x).visibility = 1;
                    if(TileList.get(8 * y + x).Data==-1){
                        DisplayMine();
                        displayMine=true;

                    }
                    else if(!displayMine){
                        TileList.get(8 * y + x).visibility = 1;
                        invalidate();
                    }

                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void AssignData(int a) {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < 64; i++) {
            if (i != a)
                numbers.add(i);
        }
        Collections.shuffle(numbers);
        for (int i = 0; i < NumberOfMines; i++) {
            TileList.get(numbers.get(i)).Data = -1;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (TileList.get(i * 8 + j).Data != -1) {
                    if (!(i == 0 || j == 0 || i == 7 || j == 7)) {
                        for (int k = 0; k < 3; k++) {
                            if (TileList.get((i - 1) * 8 + j - 1 + k).Data == -1) {
                                TileList.get(i * 8 + j).Data++;
                            }
                            if (TileList.get((i + 1) * 8 + j - 1 + k).Data == -1) {
                                TileList.get(i * 8 + j).Data++;
                            }
                            if (TileList.get((i) * 8 + j - 1 + k).Data == -1 && (k != 1)) {
                                TileList.get(i * 8 + j).Data++;
                            }
                        }
                    } else if (j == 0) {
                        if (i == 0) {
                            for (int k = 0; k < 2; k++) {
                                if (TileList.get((i + 1) * 8 + j + k).Data == -1) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                                if (TileList.get((i) * 8 + j + k).Data == -1 && (k != 0)) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                            }
                        } else if (i == 7) {
                            for (int k = 0; k < 2; k++) {
                                if (TileList.get((i - 1) * 8 + j + k).Data == -1) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                                if (TileList.get((i) * 8 + j + k).Data == -1 && (k != 0)) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                            }
                        } else {
                            for (int k = 0; k < 2; k++) {
                                if (TileList.get((i - 1) * 8 + j + k).Data == -1) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                                if (TileList.get((i + 1) * 8 + j + k).Data == -1) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                                if (TileList.get((i) * 8 + j + k).Data == -1 && (k != 0)) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                            }
                        }
                    } else if (j == 7) {
                        if (i == 0) {
                            for (int k = 0; k < 2; k++) {
                                if (TileList.get((i + 1) * 8 + j + k - 1).Data == -1) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                                if (TileList.get((i) * 8 + j + k - 1).Data == -1 && (k != 1)) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                            }
                        } else if (i == 7) {
                            for (int k = 0; k < 2; k++) {
                                if (TileList.get((i - 1) * 8 + j + k - 1).Data == -1) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                                if (TileList.get((i) * 8 + j + k - 1).Data == -1 && (k != 1)) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                            }
                        } else {
                            for (int k = 0; k < 2; k++) {
                                if (TileList.get((i - 1) * 8 + j + k - 1).Data == -1) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                                if (TileList.get((i + 1) * 8 + j + k - 1).Data == -1) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                                if (TileList.get((i) * 8 + j + k - 1).Data == -1 && (k != 1)) {
                                    TileList.get(i * 8 + j).Data++;
                                }
                            }
                        }
                    } else if (i == 0) {
                        for (int k = 0; k < 3; k++) {
                            if (TileList.get((i) * 8 + j + k - 1).Data == -1) {
                                TileList.get(i * 8 + j).Data++;
                            }
                            if (TileList.get((i + 1) * 8 + j + k - 1).Data == -1) {
                                TileList.get(i * 8 + j).Data++;
                            }
                        }
                    } else if (i == 7) {
                        for (int k = 0; k < 3; k++) {
                            if (TileList.get((i) * 8 + j + k - 1).Data == -1) {
                                TileList.get(i * 8 + j).Data++;
                            }
                            if (TileList.get((i - 1) * 8 + j + k - 1).Data == -1) {
                                TileList.get(i * 8 + j).Data++;
                            }
                        }
                    }
                }
            }
        }
    }
    public void VisibleSurroundings(int x,int y){
        for(int i=0;i<3;i++){
            if(y-1<8&&y-1>=0){
               if(x+i-1>=0&&x+i-1<8){
                   if(TileList.get((y-1)*8+x+i-1).Data==0&&TileList.get((y-1)*8+x+i-1).visibility==0) {
                       TileList.get((y-1)*8+x+i-1).visibility=1;
                       VisibleSurroundings(x + i - 1, y - 1);
                   }
                   else if(TileList.get((y-1)*8+x+i-1).Data==-1)
                       TileList.get((y-1)*8+x+i-1).visibility=0;
                   else TileList.get((y-1)*8+x+i-1).visibility=1;
               }
            }
            if(y+1<8&&y+1>=0){
                if(x+i-1>=0&&x+i-1<8){
                    if(TileList.get((y+1)*8+x+i-1).Data==0&&TileList.get((y+1)*8+x+i-1).visibility==0) {
                        TileList.get((y+1)*8+x+i-1).visibility=1;
                        VisibleSurroundings(x + i - 1, y + 1);
                    }
                    else if(TileList.get((y+1)*8+x+i-1).Data==-1)
                        TileList.get((y+1)*8+x+i-1).visibility=0;
                    else TileList.get((y+1)*8+x+i-1).visibility=1;
                }
            }
            if(x+i-1>=0&&x+i-1<8&&i!=1){
                if(TileList.get((y)*8+x+i-1).Data==0&&TileList.get((y)*8+x+i-1).visibility==0){
                    TileList.get((y)*8+x+i-1).visibility=1;
                    VisibleSurroundings(x+i-1,y);
                }
                else if(TileList.get((y)*8+x+i-1).Data==-1)
                    TileList.get((y)*8+x+i-1).visibility=0;
                else TileList.get((y)*8+x+i-1).visibility=1;
            }
        }
    }
    public void endGame(){
        TileList.clear();
        Score=0;
        isGameOver=false;
        ToStart=true;
        displayMine=false;
    }
    public void DisplayMine(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                  if(TileList.get(i*8+j).Data==-1)
                      TileList.get(i*8+j).visibility=1;
            }
        }
        Vibrator.vibrate(1000);
        invalidate();
        new CountDownTimer(1000,10) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isGameOver=true;
                invalidate();
            }
        }.start();
    }

}
