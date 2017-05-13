package com.example.fivechess30;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button black= (Button) findViewById(R.id.button2);
        Button white= (Button) findViewById(R.id.button3);
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setblack();
            }
        });
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setwhite();
            }
        });

    }

    private void setwhite() {
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                httpini();
            }

            private void httpini()
            {
                String m="http://www.cxyzf.cn/apptest.php?id=1&zuobiao=10000";
                try {
                    URL myurl=new URL(m);
                    HttpURLConnection con= (HttpURLConnection) myurl.openConnection();
                    con.setRequestProperty("connection", "Keep-Alive");
                    con.connect();
                    BufferedReader in=new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line;
                    int aaa;
                    while ((line=in.readLine())!=null)
                    {
                        aaa= Integer.parseInt(line);
                    }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Intent intent=new Intent(this,renrenduizhan.class);
        startActivity(intent);
    }

    private void setblack() {
        new Thread()
        {
            @Override
            public void run() {
                super.run();
                httpini();
            }

            private void httpini()
            {
                String m="http://www.cxyzf.cn/apptest.php?id=2&zuobiao=20000";
                try {
                    URL myurl=new URL(m);
                    HttpURLConnection con= (HttpURLConnection) myurl.openConnection();
                    con.setRequestProperty("connection", "Keep-Alive");
                    con.connect();
                    BufferedReader in=new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line;
                    int aaa;
                    while ((line=in.readLine())!=null)
                    {
                        aaa= Integer.parseInt(line);
                    }
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Intent intent=new Intent(this,renrenduizhan.class);
        startActivity(intent);
    }
}
