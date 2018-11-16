package com.example.administrator.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class index_put extends AppCompatActivity {
    TextView text_pro;
    TextView text_out;
    TextView text_settle;
    View img_tx;
    View addputbill;
    View putbilllist;
    View nopaybill;
    TextView text_nopaybill;
    TextView text_countbill;

    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_put);
        MyApplication application = (MyApplication)this.getApplication();
        application.setScore("put");

        text_pro = findViewById(R.id.text_product);
        text_out = findViewById(R.id.text_out);
        text_settle = findViewById(R.id.text_settle);
        img_tx = findViewById(R.id.img_tx);
        text_nopaybill = findViewById(R.id.text_nopaytill);
        text_countbill = findViewById(R.id.text_putbilllist);
        addputbill = findViewById(R.id.body1);
        nopaybill = findViewById(R.id.body2);
        putbilllist = findViewById(R.id.body3);


        img_tx.setOnClickListener(listener_tx);
        text_pro.setOnClickListener(listener_pro);
        text_out.setOnClickListener(listener_out);
        text_settle.setOnClickListener(listener_settle);
        putbilllist.setOnClickListener(listener_billlist);
    }
    View.OnClickListener listener_billlist = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(index_put.this,list_putbills.class);
            startActivity(intent);
        }
    };
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
            Intent intent = new Intent(index_put.this,list_address.class);
            startActivity(intent);
            finish();
        }
    };
    TextView.OnClickListener listener_pro = new TextView.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_put.this,index_product.class);
            startActivity(intent);
            index_put.this.finish();
        }
    };
    TextView.OnClickListener listener_out = new TextView.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_put.this,index_out.class);
            startActivity(intent);
            index_put.this.finish();
        }
    };
    TextView.OnClickListener listener_settle = new TextView.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_put.this,index_settle.class);
            startActivity(intent);
            index_put.this.finish();
        }
    };
}
