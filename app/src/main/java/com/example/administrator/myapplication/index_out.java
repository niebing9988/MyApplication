package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class index_out extends AppCompatActivity {
    TextView text_put;
    TextView text_pro;
    TextView text_settle;
    View img_tx;
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_out);
        MyApplication application = (MyApplication)this.getApplication();
        application.setScore("out");

        text_put = findViewById(R.id.text_put);
        text_pro = findViewById(R.id.text_product);
        text_settle = findViewById(R.id.text_settle);
        img_tx = findViewById(R.id.img_tx);

        img_tx.setOnClickListener(listener_tx);
        text_put.setOnClickListener(listener_put);
        text_pro.setOnClickListener(listener_pro);
        text_settle.setOnClickListener(listener_settle);
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
            Intent intent = new Intent(index_out.this,list_address.class);
            startActivity(intent);
            finish();
        }
    };
    TextView.OnClickListener listener_put = new TextView.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_out.this,index_put.class);
            startActivity(intent);
            index_out.this.finish();
        }
    };
    TextView.OnClickListener listener_pro = new TextView.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_out.this,index_product.class);
            startActivity(intent);
            index_out.this.finish();
        }
    };
    TextView.OnClickListener listener_settle = new TextView.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_out.this,index_settle.class);
            startActivity(intent);
            index_out.this.finish();
        }
    };
}
