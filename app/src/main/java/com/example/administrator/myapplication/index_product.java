package com.example.administrator.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class index_product extends AppCompatActivity {
    TextView text_put;
    TextView text_out;
    TextView text_settle;
    View img_tx;
    View body_list;
    View body_addlist;
    View body_listbytype;
    View body_understock;
    View body_onstock;
    View body_unify;
    TextView text_count;
    TextView text_less;
    TextView text_more;
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_product);
        MyApplication application = (MyApplication)this.getApplication();
        application.setScore("product");

        text_put = findViewById(R.id.text_put);
        text_out = findViewById(R.id.text_out);
        text_settle = findViewById(R.id.text_settle);
        img_tx = findViewById(R.id.icon_bianji);
        body_list = findViewById(R.id.body1);
        body_addlist = findViewById(R.id.body2);
        body_listbytype = findViewById(R.id.body3);
        body_understock = findViewById(R.id.body4);
        body_onstock = findViewById(R.id.body5);
        body_unify = findViewById(R.id.body6);
        text_count = findViewById(R.id.text_count);
        text_less = findViewById(R.id.text_less);
        text_more=  findViewById(R.id.text_more);

        text_put.setOnClickListener(listener_put);
        text_out.setOnClickListener(listener_out);
        text_settle.setOnClickListener(listener_settle);
        img_tx.setOnClickListener(listener_tx);
        body_list.setOnClickListener(listener_list);
        body_addlist.setOnClickListener(listenner_addlist);
        body_listbytype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index_product.this,product_typefilter.class);
                startActivity(intent);
                finish();
            }
        });
        body_understock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication alc_list = (MyApplication)index_product.this.getApplication();
                alc_list.setScore("loss");
                Intent intent = new Intent(index_product.this,list_product.class);
                startActivity(intent);
                finish();
            }
        });
        body_onstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication alc_list = (MyApplication)index_product.this.getApplication();
                alc_list.setScore("more");
                Intent intent = new Intent(index_product.this,list_product.class);
                startActivity(intent);
                finish();
            }
        });
        proinit();
    }
    private void proinit(){
        AssetsDatabaseManager.initManager(getApplication());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        SQLiteDatabase db1 = mg.getDatabase("store_management.db");
        Cursor cursor_count = db1.rawQuery("select * from tb_product",null);
        text_count.setText("共有"+cursor_count.getCount()+"种货品");
        Cursor cursor_loss = db1.rawQuery("select * from tb_product INNER JOIN tb_instock on tb_product.id=tb_instock.id where tb_instock.storenumber<tb_product.underline",null);
        text_less.setText("库存不足("+cursor_loss.getCount()+"种)");
        Cursor cursor_more = db1.rawQuery("select * from tb_product INNER JOIN tb_instock on tb_product.id=tb_instock.id where tb_instock.storenumber>tb_product.online",null);
        text_more.setText("库存过多("+cursor_more.getCount()+"种)");
    }
    View.OnClickListener listenner_addlist =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(index_product.this,add_product.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
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
    View.OnClickListener listener_list =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // Log.i("1231123123", "11111111111111111111111111");
            MyApplication alc_list = (MyApplication)index_product.this.getApplication();
            alc_list.setScore("list");
            Intent intent = new Intent(index_product.this,list_product.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener listener_tx = new View.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_product.this,list_address.class);
            startActivity(intent);
            finish();
        }
    };
    TextView.OnClickListener listener_put = new TextView.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_product.this,index_put.class);
            startActivity(intent);
            index_product.this.finish();
        }
    };
    TextView.OnClickListener listener_out = new TextView.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_product.this,index_out.class);
            startActivity(intent);
            index_product.this.finish();
        }
    };
    TextView.OnClickListener listener_settle = new TextView.OnClickListener(){
        public void onClick(View v) {
            Intent intent = new Intent(index_product.this,index_settle.class);
            startActivity(intent);
            index_product.this.finish();
        }
    };

}
