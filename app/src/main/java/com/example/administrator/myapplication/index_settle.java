package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class index_settle extends AppCompatActivity {
    TextView text_put;
    TextView text_out;
    TextView text_pro;
    View img_tx;
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_settle);
        MyApplication application = (MyApplication)this.getApplication();
        application.setScore("settle");

        text_put = findViewById(R.id.text_put);
        text_out = findViewById(R.id.text_out);
        text_pro = findViewById(R.id.text_product);
        img_tx = findViewById(R.id.img_tx);

        img_tx.setOnClickListener(listener_tx);
        text_put.setOnClickListener(listener_put);
        text_out.setOnClickListener(listener_out);
        text_pro.setOnClickListener(listener_pro);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((System.currentTimeMillis()-exitTime) > 2000){
            exitTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            mHandler.postDelayed(mFinish, 0);
        }
        //return super.onKeyDown(keyCode, event);
        return false;
    }
    private long exitTime = 0;

    private Handler mHandler = new Handler();
    private Runnable mFinish = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    View.OnClickListener listener_tx =new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(index_settle.this,list_address.class);
            startActivity(intent);
            finish();
        }
    };
    TextView.OnClickListener listener_put = new TextView.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_settle.this,index_put.class);
            startActivity(intent);
            index_settle.this.finish();
        }
    };
    TextView.OnClickListener listener_out = new TextView.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_settle.this,index_out.class);
            startActivity(intent);
            index_settle.this.finish();
        }
    };
    TextView.OnClickListener listener_pro = new TextView.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_settle.this,index_product.class);
            startActivity(intent);
            index_settle.this.finish();
        }
    };
}
