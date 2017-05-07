package com.example.fivechess30;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 张帆 on 2017/5/7.
 */

public class renren extends View
{

    private Paint mypait = new Paint();
    private Bitmap white;
    private Bitmap black;
    private float lineHeight;
    private int panewidth;
    private int max_line = 10;//棋盘总格数
    private int map[][] = new int[max_line][max_line];//0为空，1为白，2为黑
    boolean BorW = true;//下棋的颜色.,白色为true,人人对战需要
    private boolean gameover=false;



    public renren(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        //setBackgroundColor(0x440000ff);
        ini();
    }

    public void ini()
    {
        mypait.setColor(0xff000000);
        mypait.setAntiAlias(true);
        mypait.setDither(true);
        mypait.setStyle(Paint.Style.STROKE);
        white = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        black = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);

    }

    @Override
    //确定棋盘大小
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widsize = MeasureSpec.getSize(widthMeasureSpec);
        //int widmode=MeasureSpec.getMode(widthMeasureSpec);
        //待定，如果不出现bug 就不用改

        int heisize = MeasureSpec.getSize(heightMeasureSpec);
        //int heimode=MeasureSpec.getMode(heightMeasureSpec);


        int length = Math.min(widsize, heisize);
        setMeasuredDimension(length, length);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        panewidth = w;
        lineHeight = panewidth * 1.0f / max_line;

        white = Bitmap.createScaledBitmap(white, (int) lineHeight * 3 / 4, (int) lineHeight * 3 / 4, false);
        black = Bitmap.createScaledBitmap(black, (int) lineHeight * 3 / 4, (int) lineHeight * 3 / 4, false);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawqizhi(canvas);
    }

    private void drawqizhi(Canvas canvas)
    {
        for (int i = 0; i < max_line; i++)
        {
            for (int j = 0; j < max_line; j++)
            {
                if (map[i][j] == 1)
                {
                    canvas.drawBitmap(white, (float) (j) * lineHeight, (float) (i) * lineHeight, mypait);
                }
                if (map[i][j] == 2)
                {
                    canvas.drawBitmap(black, (float) (j) * lineHeight, (float) (i) * lineHeight, mypait);
                }
            }
        }
    }

    private void drawBoard(Canvas canvas)//绘制棋盘
    {
        int w = panewidth;
        float linel = lineHeight;
        for (int i = 0; i < max_line; i++)
        {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight / 2);

            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, mypait);//绘制棋盘横向

            int startY = (int) (lineHeight / 2);
            int endY = (int) (w - lineHeight / 2);
            int x = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(x, startY, x, endY, mypait);//绘制棋盘纵向

        }


    }

    @Override
    //获取触摸点
    public boolean onTouchEvent(MotionEvent event)
    {
        //关于事件的传递，暂时还没有弄懂，只知道要这样写

        if ((event.getAction() == MotionEvent.ACTION_DOWN)&&!gameover)
        {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (map[(int) (y / lineHeight)][(int) (x / lineHeight)] == 0)
            {
              if (BorW)
                {

                    map[(int) (y / lineHeight)][(int) (x / lineHeight)] = 1;
                    check((int) (y / lineHeight), (int) (x / lineHeight));
                    invalidate();
                    BorW = !BorW;
                } else
                {
                    map[(int) (y / lineHeight)][(int) (x / lineHeight)] = 2;
                    check((int) (y / lineHeight), (int) (x / lineHeight));
                    invalidate();
                    BorW = !BorW;
                }
                invalidate();
                return true;
            }

            return true;
        }

        return super.onTouchEvent(event);
    }

    private void check(int x, int y)
    {
        int hei = 0, bai = 0;
        for (int i = 0; i < max_line; i++)            //横向判断
        {
            if ((map[x][i] == 0))
            {
                hei = 0;
                bai = 0;
            }
            if ((map[x][i] == 2))
            {
                hei++;
                bai = 0;
                if (hei == 5)
                {
                    Bwin();
                }
            }
            if ((map[x][i] == 1))
            {
                bai++;
                hei = 0;
                if (bai == 5)
                {
                    wwin();
                }
            }

        }
        hei = 0;
        bai = 0;
        for (int i = 0; i < max_line; i++)            //纵向判断
        {

            if ((map[i][y] == 0))
            {
                hei = 0;
                bai = 0;
            }
            if ((map[i][y] == 2))
            {
                hei++;
                bai = 0;
                if (hei == 5)
                {
                    Bwin();
                }
            }
            if ((map[i][y] == 1))
            {
                bai++;
                hei = 0;
                if (bai == 5)
                {
                    wwin();
                }
            }
        }

        //斜向(\向)判断
        int x1 = x, y1 = y;
        if (x1 > y1)
        {
            x1 = x1 - y1;
            y1 = 0;
        } else
        {
            y1 = y1 - x1;
            x1 = 0;
        }
        hei = 0;
        bai = 0;
        while ((x1 < max_line) && (y1 < max_line))
        {
            if ((map[x1][y1] == 0))
            {
                hei = 0;
                bai = 0;
            }
            if ((map[x1][y1] == 2))
            {
                hei++;
                bai = 0;
                if (hei == 5)
                {
                    Bwin();
                }
            }
            if ((map[x1][y1] == 1))
            {
                bai++;
                hei = 0;
                if (bai == 5)
                {
                    wwin();
                }
            }
            x1++;
            y1++;
        }

        //斜向（/向）判断
        int x2 = x, y2 = y;
        if ((x2 + y2 + 1) <= max_line)
        {
            x2 = 0;
            y2 = (y + x);
        } else
        {
            y2 = max_line - 1;
            x2 = (x - (max_line - 1 - y));

        }
        hei = 0;
        bai = 0;
        while ((y2 >= 0) && (x2 < max_line))
        {
            if ((map[x2][y2] == 0))
            {
                hei = 0;
                bai = 0;
            }

            if ((map[x2][y2] == 2))
            {
                hei++;
                bai = 0;
                if (hei == 5)
                {
                    Bwin();
                }
            }
            if ((map[x2][y2] == 1))
            {
                bai++;
                hei = 0;
                if (bai == 5)
                {
                    wwin();
                }
            }
            x2++;
            y2--;
        }
    }


    private void wwin()
    {
        gameover=true;
        Dialog("白方胜");
    }

    private void Bwin()
    {
        gameover=true;
        Dialog("黑方胜");
    }

    //未完成
    private void Dialog(String string)
    {
        new AlertDialog.Builder(getContext()).setTitle(string)
                .setPositiveButton("重来",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                restart();
                            }
                        }
                )
                .setNeutralButton("查看棋盘！", new DialogInterface.OnClickListener()
                {

                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("退出",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {

                                System.exit(1);
                            }
                        }).show();
    }

    private void restart()
    {
        for (int i = 0; i < max_line; i++)
        {
            for (int j = 0; j < max_line; j++)
            {
                map[i][j] = 0;
            }
        }
        gameover=true;
        invalidate();
    }
}

