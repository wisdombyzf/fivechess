package com.example.fivechess30;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Handler;

/**
 * Created by 张帆 on 2017/5/12.
 */
/*线程要结束后才会统一结算。。。。。总共卡了三天半，终于解决了ヾ(o◕∀◕)ﾉヾ
* 估计也只有我自己能看懂我写的什么.........
*
* */

public class renren extends View {
    private Paint mypait = new Paint();
    private Bitmap white;
    private Bitmap black;
    private float lineHeight;
    private int panewidth;
    private int max_line = 10;//棋盘总格数
    boolean BorW = true;//下棋的颜色.,白色为true,人人对战需要
    private boolean gameover = false;
    public int map[][] = new int[max_line][max_line];//0为空，1为白，2为黑
    private boolean wite = true;
    private int id = 0;
    private int tmp = 0;
    int color = 1314520;


    public renren(Context context, AttributeSet attrs) {
        super(context, attrs);
        ini();
    }


    public void ini() {
        mypait.setColor(0xff000000);
        mypait.setAntiAlias(true);
        mypait.setDither(true);
        mypait.setStyle(Paint.Style.STROKE);
        white = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        black = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
        class sethread extends Thread {
            @Override
            public void run() {
                super.run();
                changehttp();
            }

            private void changehttp() {
                String aa;
                if (tmp == 1) {
                    aa = "http://www.cxyzf.cn/apptest.php?id=0&zuobiao=0";
                } else {
                    aa = "http://www.cxyzf.cn/apptest.php?id=0&zuobiao=0";
                }
                try {
                    URL mm = new URL(aa);
                    HttpURLConnection con2 = (HttpURLConnection) mm.openConnection();
                    con2.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(con2.getInputStream()));
                    String line;
                    int aaa = 0;
                    while ((line = in.readLine()) != null) {
                        aaa = Integer.parseInt(line);
                    }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        class firthread extends Thread {
            @Override
            public void run() {
                super.run();
                httpini();
            }

            private void httpini() {
                String m = "http://www.cxyzf.cn/apptest.php";
                try {
                    URL myurl = new URL(m);
                    HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
                    con.connect();
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line;
                    int aaa = 0;
                    while ((line = in.readLine()) != null) {
                        aaa = Integer.parseInt(line);
                    }
                    if (aaa == 10000) {
                        BorW = true;
                        id = 1;
                        tmp = 2;
                    } else {
                        BorW = false;
                        id = 2;
                        tmp = 1;
                    }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        class thirdthread extends Thread {
            @Override
            public void run() {
                int aa = 0;
                int color = 0;
                int httpcoluom = 0;
                int httpline = 0;
                super.run();
                do {
                    String pp;
                    pp = "http://www.cxyzf.cn/apptest.php";
                    URL myurl = null;
                    try {
                        myurl = new URL(pp);
                        HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
                        con.connect();
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String line;
                        while ((line = in.readLine()) != null) {
                            aa = Integer.parseInt(line);
                        }
                        color = aa / 10000;
                        httpcoluom = (aa % 10000) / 100;
                        httpline = (aa % 100);
                        map[httpcoluom][httpline] = color;
                        handler.sendEmptyMessage(0x123);
                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                } while ((true));
            }
        }

        Thread t1 = new firthread();
        t1.start();
        try {
            t1.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread t2 = new sethread();
        t2.start();
        try {
            t2.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread t3 = new thirdthread();
        t3.start();
        updata();
    }


    public void updata()
    {
        class thirdthread extends Thread {
            @Override
            public void run() {
                int aa = 0;
                int color = 0;
                int httpcoluom = 0;
                int httpline = 0;
                super.run();
                do {
                    String pp;
                    pp = "http://www.cxyzf.cn/apptest.php";
                    URL myurl = null;
                    try {
                        myurl = new URL(pp);
                        HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
                        con.connect();
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String line;
                        while ((line = in.readLine()) != null) {
                            aa = Integer.parseInt(line);
                        }
                        color = aa / 10000;
                        httpcoluom = (aa % 10000) / 100;
                        httpline = (aa % 100);
                        map[httpcoluom][httpline] = color;
                        handler.sendEmptyMessage(0x123);
                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                } while ((color!=9988770));
            }
        }
        new thirdthread().start();
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
        if ((event.getAction() == MotionEvent.ACTION_DOWN)&&!gameover&&wite)
        {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            if (map[(int) (y / lineHeight)][(int) (x / lineHeight)] == 0)
            {
                wite=false;
                int ccccc=1;
                if (BorW)
                {
                    ccccc=1;
                }else
                {
                    ccccc=2;
                }
                map[(int) (y / lineHeight)][(int) (x / lineHeight)] = ccccc;
                check((int) (y / lineHeight), (int) (x / lineHeight));
                new Thread()
                {
                    @Override
                    public void run() {
                        super.run();
                        inihttp();
                    }

                    private void inihttp() {
                        int aa=0;
                        int qise=0;
                        if (BorW)
                        {
                            qise=10000;
                        }else
                        {
                            qise=20000;
                        }
                        int message=qise+(int) (y / lineHeight)*100+ (int) (x / lineHeight);
                        int httpcoluom = 0;
                        int httpline=0;
                        String pp;
                        if (id==1)
                        {
                            pp = "http://www.cxyzf.cn/apptest.php?id=1&zuobiao="+message;
                        }else
                        {
                            pp = "http://www.cxyzf.cn/apptest.php?id=2&zuobiao="+message;
                        }
                        try {
                            URL myurl = new URL(pp);
                            HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
                            con.connect();
                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            String line;
                            while ((line = in.readLine()) != null) {
                                aa = Integer.parseInt(line);
                            }
                            // TODO: 2017/5/12  五位数代表颜色-纵坐标-横坐标，0为初始，1为白，2为黑，10204代表三列5行的白棋
                            color = aa / 10000;
                            httpcoluom = (aa % 10000) / 100;
                            httpline = (aa % 100);
                            map[httpcoluom][httpline]=color;
                            check(httpcoluom,httpline);
                        }
                        catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                class setqi extends Thread{
                    @Override
                    public void run() {
                        super.run();
                        httpget();
                    }
                    private void httpget()
                    {
                        int aa=0;
                        int qise=0;
                        if (BorW)
                        {
                            qise=10000;
                        }else
                        {
                            qise=20000;
                        }
                        int message=qise+(int) (y / lineHeight)*100+ (int) (x / lineHeight);
                        try {
                            int httpcoluom = 0;
                            int httpline=0;

                            do {
                                String pp;
                                wite=false;
                                if (id==1)
                                {
                                    pp = "http://www.cxyzf.cn/apptest.php?id=1&zuobiao="+message;  //为什么连加不行，de了一下午的bug
                                }else
                                {
                                    pp = "http://www.cxyzf.cn/apptest.php?id=2&zuobiao="+message;
                                }
                                URL myurl = new URL(pp);
                                HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
                                con.connect();
                                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                                String line;
                                while ((line = in.readLine()) != null) {
                                    aa = Integer.parseInt(line);
                                }
                                // TODO: 2017/5/12  五位数代表颜色-纵坐标-横坐标，0为初始，1为白，2为黑，10204代表三列5行的白棋
                                color = aa / 10000;
                                httpcoluom = (aa % 10000) / 100;
                                httpline = (aa % 100);
                                map[httpcoluom][httpline]=color;
                                //handler.sendEmptyMessage(0x123);
                                //invalidate();
                            }while ((color==id)||(color==0)||(gameover));
                            wite=true;
                        }
                        catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
                Thread set1=new setqi();
                set1.start();
                return true;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

   android.os.Handler handler = new android.os.Handler()
   {
       @Override
       public void handleMessage(Message msg)
       {
           if(msg.what == 0x123)
           {
               invalidate();
           }
       }
   };



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
        ResetPhp();

    }

    private void Bwin()
    {
        gameover=true;
        Dialog("黑方胜");
        ResetPhp();

    }

    private void ResetPhp()         //重置PHP
    {
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                try {
                    URL re=new URL("http://www.cxyzf.cn/apptest.php?id=0&zuobiao=0");
                    HttpURLConnection con= (HttpURLConnection) re.openConnection();
                    con.connect();
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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