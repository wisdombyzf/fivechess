package com.example.fivechess20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{

    private Button bt;
    private Button bt1;
    private Button about;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt=(Button)findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                renjiduizhan();
            }
        });

        bt1=(Button)findViewById(R.id.renren);
        bt1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                renrenduizhan();
            }
        });

        about= (Button) findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                aboutmessage();
            }
        });

    }

    private void aboutmessage()
    {

        Intent i=new Intent();
        i.setClass(this,about.class);
        startActivity(i);

    }

    private void renrenduizhan()
    {
        Intent i=new Intent();
        i.setClass(this,renrenduizhan.class);
        startActivity(i);
    }

    private void renjiduizhan()
    {
        Intent i=new Intent();
        i.setClass(this,renjiduizhan.class);
        startActivity(i);
    }


}
