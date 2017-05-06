package com.example.fivechess20;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 张帆 on 2017/4/30.
 */



public class fivechess extends View
{
    private Paint mypait=new Paint();
    private Bitmap white;
    private Bitmap black;
    private float lineHeight;
    private int panewidth;
    private int max_line=10;//棋盘总格数
    private int map[][]=new int[max_line][max_line];//0为空，1为白，2为黑
    boolean BorW=true;//下棋的颜色.,白色为true,人人对战需要


    public fivechess(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        setBackgroundColor(0x440000ff);
        ini();
    }

    public void ini()
    {
        mypait.setColor(0xff000000);
        mypait.setAntiAlias(true);
        mypait.setDither(true);
        mypait.setStyle(Paint.Style.STROKE);
        white= BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        black=BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);

    }

    @Override
    //确定棋盘大小
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widsize=MeasureSpec.getSize(widthMeasureSpec);
        //int widmode=MeasureSpec.getMode(widthMeasureSpec);
        //待定，如果不出现bug 就不用改

        int  heisize=MeasureSpec.getSize(heightMeasureSpec);
        //int heimode=MeasureSpec.getMode(heightMeasureSpec);



        int length= Math.min(widsize,heisize);
        setMeasuredDimension(length,length);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        panewidth=w;
        lineHeight=panewidth*1.0f/max_line;

        white=Bitmap.createScaledBitmap(white,(int)lineHeight*3/4,(int)lineHeight*3/4,false);
        black=Bitmap.createScaledBitmap(black,(int)lineHeight*3/4,(int)lineHeight*3/4,false);
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
        for (int i=0;i<max_line;i++)
        {
            for (int j=0;j<max_line;j++)
            {
                if (map[i][j]==1)
                {
                    canvas.drawBitmap(white,(float)(j)*lineHeight,(float)(i)*lineHeight,mypait);
                }
                if (map[i][j]==2)
                {
                    canvas.drawBitmap(black,(float)(j)*lineHeight,(float)(i)*lineHeight,mypait);
                }
            }
        }
    }

    private void drawBoard(Canvas canvas)//绘制棋盘
    {
        int w=panewidth;
        float linel=lineHeight;
        for (int i=0;i<max_line;i++)
        {
            int startX= (int) (lineHeight/2);
            int endX=(int)(w-lineHeight/2);

            int y= (int) ((0.5+i)*lineHeight);
            canvas.drawLine(startX,y,endX,y,mypait);//绘制棋盘横向

            int startY= (int) (lineHeight/2);
            int endY=(int)(w-lineHeight/2);
            int x= (int) ((0.5+i)*lineHeight);
            canvas.drawLine(x,startY,x,endY,mypait);//绘制棋盘纵向

        }


    }

    @Override
    //获取触摸点
    public boolean onTouchEvent(MotionEvent event)
    {
        //关于事件的传递，暂时还没有弄懂，只知道要这样写

        if (event.getAction()==MotionEvent.ACTION_DOWN)
        {
            int x= (int) event.getX();
            int y= (int) event.getY();
            if (map[(int)(y/lineHeight)][(int)(x/lineHeight)]==0)
            {
              /*  if (BorW)
                {

                    map[(int) (y / lineHeight)][(int) (x / lineHeight)] = 1;
                    BorW = !BorW;
                } else*/
                {
                    map[(int) (y / lineHeight)][(int) (x / lineHeight)] = 2;

                    invalidate();
                    ai();

                    //BorW = !BorW;
                }
                check((int) (y / lineHeight), (int) (x / lineHeight));
                invalidate();
                return true;
            }
            invalidate();
            return true;
        }
        invalidate();
        return super.onTouchEvent(event);
    }



    private void ai()
    {
       int map1[][]=new int[max_line][max_line];
        for (int i=0;i<max_line;i++)
        {
            for (int j=0;j<max_line;j++)
            {
                if (map[i][j]==0)
                {
                    map1[i][j]+=evalu(i,j);
                }

            }
        }
        int x=5;
        int y=5;
        int max=0;
        for (int i=0;i<max_line;i++)
        {
            for (int j=0;j<max_line;j++)
            {
                if (map1[i][j]>max)
                {
                    max=map1[i][j];
                    x=i;
                    y=j;
                }
            }
        }
        map[x][y]=1;
        invalidate();
        check(x,y);
    }

    

    //评估函数
    private int evalu(int i, int j)
    {
        int sum=0;
        //设计时竟然忘了黑棋，啪啪啪的打脸啊，要学会写文档。

        //攻击分数
        int l,h,a,b;    //l为横向，h为纵向，a为\向，b为/向
        //横向
        String heng="-1";
        for (int y=0;y<max_line;y++)
        {
            if (y==j)
            {
                heng=heng+"1";
            }else
            {
                heng=heng+map[i][y];
            }

        }
         l=onelinejudge(heng,i,j);
        //纵向
        String shu="-1";
        for (int y=0;y<max_line;y++)
        {
            if (y==i)
            {
                shu=shu+"1";
            }else
            {
                shu=shu+map[y][j];
            }
        }
        h=onelinejudge(shu,i,j);
        // \向
        String aa="-1";
        int x1 = i, y1 = j;
        if (x1>y1)
        {
            x1 = x1 - y1;
            y1 = 0;
        }
        else
        {
            y1 = y1 - x1;
            x1 = 0;
        }
        for (;(x1<max_line)&&(y1<max_line);y1++,x1++)
        {
            if ((x1==i)&&(y1==j))
            {
                aa=aa+"1";
            }
            else
            {
                aa=aa+map[x1][y1];
            }
        }
        a=onelinejudge(aa,i,j);

        //  /向
        String bb="-1";
        int x2 =i, y2 = j;
        if ((x2+y2+1)<=max_line)
        {
            x2 = 0;
            y2 = (i + j);
        }
        else
        {
            y2 = max_line-1;
            x2 = (i -(max_line-1- j));
        }
        for (;(y2 >= 0) && (x2<max_line); x2++,y2--)
        {
            if ((x2==i)&&(y2==j))
            {
                bb=bb+"1";
            }
            else
            {
                bb=bb+map[x2][y2];
            }
        }
        b=onelinejudge(bb,i,j);
        sum +=alljudge(l,h,a,b);



        //防守分数
        int mapcopy[][]=new int[max_line][max_line];
        for (int ii=0;ii<max_line;ii++)
        {
            for (int jj=0;jj<max_line;jj++)
            {
                if (map[ii][jj]==0)
                {
                    mapcopy[ii][jj]=0;
                }else
                {
                    if (map[ii][jj]==1)
                    {
                        mapcopy[ii][jj]=2;
                    }else
                    {
                        mapcopy[ii][jj]=1;
                    }
                }
            }
        }


        //横向
        heng="-1";
        for (int y=0;y<max_line;y++)
        {
            if (y==j)
            {
                heng=heng+"1";
            }else
            {
                heng=heng+mapcopy[i][y];
            }

        }
        l=onelinejudge(heng,i,j);
        //纵向
        shu="-1";
        for (int y=0;y<max_line;y++)
        {
            if (y==i)
            {
                shu=shu+"1";
            }else
            {
                shu=shu+mapcopy[y][j];
            }
        }
        h=onelinejudge(shu,i,j);
        // \向
        aa="-1";
        int x11 = i, y11 = j;
        if (x11>y11)
        {
            x11 = x11 - y11;
            y11 = 0;
        }
        else
        {
            y11 = y11 - x11;
            x11 = 0;
        }
        for (;(x11<max_line)&&(y11<max_line);y11++,x11++)
        {
            if ((x11==i)&&(y11==j))
            {
                aa=aa+"1";
            }
            else
            {
                aa=aa+mapcopy[x11][y11];
            }
        }
        a=onelinejudge(aa,i,j);

        //  /向
        bb="-1";
        x2 =i;
        y2 =j;
        if ((x2+y2+1)<=max_line)
        {
            x2 = 0;
            y2 = (i + j);
        }
        else
        {
            y2 = max_line-1;
            x2 = (i -(max_line-1- j));
        }
        for (;(y2 >= 0) && (x2<max_line); x2++,y2--)
        {
            if ((x2==i)&&(y2==j))
            {
                bb=bb+"1";
            }
            else
            {
                bb=bb+mapcopy[x2][y2];
            }
        }
        b=onelinejudge(bb,i,j);

        sum +=alljudge(l,h,a,b);
        return sum;
    }

    private int alljudge(int l, int h, int a, int b)
    {
        int sum=0;
        //长连
        if ((l==1)||(h==1)||(a==1)||(b==1))
        {
            return sum=100000;
        }
        //活4
        if ((l==2)||(h==2)||(a==2)||(b==2))
        {
            return sum=10000;
        }
        //双冲4
        if (((l==3)&&(h==3))||((l==3)&&(a==3))||((l==3)&&(b==3))|| ((h==3)&&(a==3))||((h==3)&&(b==3))||((a==3)&&(b==3)))
        {
            return sum=10000;
        }
        //冲4活3
        if ((l==3)||(h==3)||(a==3)||(b==3))
        {
            if ((l==4)||(h==4)||(a==4)||(b==4))
            {
                return sum=10000;
            }
        }
        //双活3
        if (((l==4)&&(h==4))||((l==4)&&(a==4))||((l==4)&&(b==4))|| ((h==4)&&(a==4))||((h==4)&&(b==4))||((a==4)&&(b==4)))
        {
            return sum=5000;
        }
        //活3眠3
        if ((l==5)||(h==5)||(a==5)||(b==5))
        {
            if ((l==4)||(h==4)||(a==4)||(b==4))
            {
                return sum=1000;
            }
        }
        //活三
        if ((l==4)||(h==4)||(a==4)||(b==4))
        {
            return sum=200;
        }
        //双活2
        if (((l==6)&&(h==6))||((l==6)&&(a==6))||((l==6)&&(b==6))|| ((h==6)&&(a==6))||((h==6)&&(b==6))||((a==6)&&(b==6)))
        {
            return sum=100;
        }
        //眠3
        if ((l==5)||(h==5)||(a==5)||(b==5))
        {
            return sum=50;
        }
        //活2眠2
        if ((l==6)||(h==6)||(a==6)||(b==6))
        {
            if ((l==7)||(h==7)||(a==7)||(b==7))
            {
                return sum=10;
            }
        }
        //活2
        if ((l==6)||(h==6)||(a==6)||(b==6))
        {
                return sum=5;
        }
        //眠2
        if ((l==7)||(h==7)||(a==7)||(b==7))
        {
            return sum=3;
        }
        //死4
        if ((l==8)||(h==8)||(a==8)||(b==8))
        {
            return sum=-5;
        }

        if ((l==9)||(h==9)||(a==9)||(b==9))
        {
            return sum=-5;
        }
        if ((l==10)||(h==10)||(a==10)||(b==10))
        {
            return sum=-5;
        }
        return sum;
    }


    //棋型给分判断，可继续添加棋型,通过正则表达式匹配，本质是一个匹配子串问题
    private int onelinejudge(String heng, int i, int j)
    {
        int sum=0;
        String changlian= "11111";
        String huosi1="011110";
        String chongsi1="011112";
        String chongsi2="0101110";
        String chongsi3="0110110";
        String chongsi4="0111010";
        String huosan1="01110";
        String huosan2="010110";
        String miansan1="001112";
        String miansan2="010112";
        String miansan3="011012";
        String miansan4="10011";
        String miansan5="10101";
        String miansan6="2011102";
        String huoer1="00110";
        String huoer2="01010";
        String huoer3="010010";
        String mianer1="000112";
        String mianer2="001012";
        String mianer3="010012";
        String mianer4="10001";
        String mianer5="2010102";
        String mianer6="2011002";
        String shisi="211112";
        String shisan="21112";
        String shier="2112";


       /* String te="000";
        Pattern pte = Pattern.compile(te);
        Matcher mte=pte.matcher(heng);
        if (mte.find())
        {
            return sum=10909898;
        }*/


        Pattern pchanglian = Pattern.compile(changlian);
        Pattern phuosi1 = Pattern.compile(huosi1);
        Pattern pchongsi1 = Pattern.compile(chongsi1);
        Pattern pchongsi2 = Pattern.compile(chongsi2);
        Pattern pchongsi3 = Pattern.compile(chongsi3);
        Pattern pchongsi4 = Pattern.compile(chongsi4);
        Pattern phuosan1 = Pattern.compile(huosan1);
        Pattern phuosan2 = Pattern.compile(huosan2);
        Pattern pmiansan1 = Pattern.compile(miansan1);
        Pattern pmiansan2 = Pattern.compile(miansan2);
        Pattern pmiansan3 = Pattern.compile(miansan3);
        Pattern pmiansan4 = Pattern.compile(miansan4);
        Pattern pmiansan5 = Pattern.compile(miansan5);
        Pattern pmiansan6 = Pattern.compile(miansan6);
        Pattern phuoer1 = Pattern.compile(huoer1);
        Pattern phuoer2 = Pattern.compile(huoer2);
        Pattern phuoer3 = Pattern.compile(huoer3);
        Pattern pmianer1 = Pattern.compile(mianer1);
        Pattern pmianer2 = Pattern.compile(mianer2);
        Pattern pmianer3 = Pattern.compile(mianer3);
        Pattern pmianer4 = Pattern.compile(mianer4);
        Pattern pmianer5 = Pattern.compile(mianer5);
        Pattern pmianer6 = Pattern.compile(mianer6);
        Pattern pshisi = Pattern.compile(shisi);
        Pattern pshisan = Pattern.compile(shisan);
        Pattern pshier = Pattern.compile(shier);


        Matcher mchanglian=pchanglian.matcher(heng);
        Matcher mhuosi1=phuosi1.matcher(heng);
        Matcher mchongsi1=pchongsi1.matcher(heng);
        Matcher mchongsi2=pchongsi2.matcher(heng);
        Matcher mchongsi3=pchongsi3.matcher(heng);
        Matcher mchongsi4=pchongsi4.matcher(heng);
        Matcher mhuosan1=phuosan1.matcher(heng);
        Matcher mhuosan2=phuosan2.matcher(heng);
        Matcher mmiansan1=pmiansan1.matcher(heng);
        Matcher mmiansan2=pmiansan2.matcher(heng);
        Matcher mmiansan3=pmiansan3.matcher(heng);
        Matcher mmiansan4=pmiansan4.matcher(heng);
        Matcher mmiansan5=pmiansan5.matcher(heng);
        Matcher mmiansan6=pmiansan6.matcher(heng);
        Matcher mhuoer1=phuoer1.matcher(heng);
        Matcher mhuoer2=phuoer2.matcher(heng);
        Matcher mhuoer3=phuoer3.matcher(heng);
        Matcher mmianer1=pmianer1.matcher(heng);
        Matcher mmianer2=pmianer2.matcher(heng);
        Matcher mmianer3=pmianer3.matcher(heng);
        Matcher mmianer4=pmianer4.matcher(heng);
        Matcher mmianer5=pmianer5.matcher(heng);
        Matcher mmianer6=pmianer6.matcher(heng);
        Matcher mshisi=pshisi.matcher(heng);
        Matcher mshisan=pshisan.matcher(heng);
        Matcher mshier=pshier.matcher(heng);


        if (mchanglian.find())
        {
            return sum=1;
        }
        if (mhuosi1.find())
        {
            return sum=2;
        }
        if (mchongsi1.find()||mchongsi2.find()||mchongsi3.find()||mchongsi4.find())
        {
            return sum=3;
        }
        if (mhuosan1.find()||mhuosan2.find())
        {
            return sum=4;
        }
        if (mmiansan1.find()||mmiansan2.find()||mmiansan3.find()||mmiansan4.find()||mmiansan5.find()||mmiansan6.find())
        {
            return sum=5;
        }
        if (mhuoer1.find()||mhuoer2.find()||mhuoer3.find())
        {
            return sum=6;
        }
        if (mmianer1.find()||mmianer2.find()||mmianer3.find()||mmianer4.find()||mmianer5.find()||mmianer6.find())
        {
            return sum=7;
        }

        if (mshisi.find())
        {
            return sum=8;
        }
        if (mshisan.find())
        {
            return sum=9;
        }
        if (mshier.find())
        {
            return sum=10;
        }
        return sum;
    }

    //0为什么也没有，1为长连，2为活四，3为冲四，4为活三，5为眠三，6为活二，7为眠二，8为死四，9为死三，10为死二

    private void check(int x,int y)
    {
        int hei = 0, bai = 0;
        for (int i = 0; i < max_line; i++)			//横向判断
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
        for (int i = 0; i < max_line; i++)			//纵向判断
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
        if (x1>y1)
        {
            x1 = x1 - y1;
            y1 = 0;
        }
        else
        {
            y1 = y1 - x1;
            x1 = 0;
        }
        hei = 0;
        bai = 0;
        while ((x1<max_line) && (y1<max_line))
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
        if ((x2+y2+1)<=max_line)
        {
            x2 = 0;
            y2 = (y + x);
        }
        else
        {
            y2 = max_line-1;
            x2 = (x -(max_line-1- y));

        }
        hei = 0;
        bai = 0;
        while ((y2 >= 0) && (x2<max_line))
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
        Dialog("白方胜");
    }

    private void Bwin()
    {

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
                .setNeutralButton("查看棋盘！", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("退出",
                        new DialogInterface.OnClickListener()
                        {

                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        }).show();
    }

    private void restart()
    {
    }
}
