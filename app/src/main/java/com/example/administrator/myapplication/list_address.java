package com.example.administrator.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class list_address extends AppCompatActivity {
    View  img_zjt;
    ListView list_adr;
    View img_tianjia;
    EditText edit_sousuo;
    Context con;
    MyApplication application;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_address);
        application = (MyApplication)this.getApplication();

        img_tianjia = findViewById(R.id.icon_bianji);
        img_zjt = findViewById(R.id.icon_zjt);
        edit_sousuo = findViewById(R.id.edit_sousuo);
        list_adr = findViewById(R.id.list_adr);

        list_adr.setOnItemClickListener(listener_item);
        img_zjt.setOnClickListener(listener_zjt);
        img_tianjia.setOnClickListener(listener_tianjia);
        edit_sousuo.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String text_sousuo = edit_sousuo.getText().toString();
                text_sousuo = StringFilter.fstring(text_sousuo);
               // Log.i("log", text_sousuo);
                addString("select adname from tb_address where adname LIKE '%%"+text_sousuo+"%%' order by adname asc");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        list_adr = findViewById(R.id.list_adr);
        con = this;
        addString("select adname from tb_address order by adname asc");
    }
    ListView.OnItemClickListener listener_item= new ListView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AssetsDatabaseManager.initManager(getApplication());
            // 获取管理对象，因为数据库需要通过管理对象才能够获取
            AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
            // 通过管理对象获取数据库
            SQLiteDatabase db1 = mg.getDatabase("store_management.db");
            Cursor cursor = db1.rawQuery("select * from tb_address order by adname asc",null);
            int temp= -1;
            while (cursor.moveToNext())
            {
                temp++;
                if(temp==position)
                {
                    int viewid = cursor.getInt(cursor.getColumnIndex("id"));
                    String Sviewid = String.valueOf(viewid);
                    Intent intent = new Intent(list_address.this,information_address.class);
                    intent.putExtra("addressid",Sviewid);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        }
    };
    private void addString(String sql){
        ArrayList<HashMap<String, String>> mList = null;
        mList = new ArrayList<>();
        AssetsDatabaseManager.initManager(getApplication());
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        // 通过管理对象获取数据库
        SQLiteDatabase db1 = mg.getDatabase("store_management.db");
        Cursor cursor = db1.rawQuery(sql,null);
        while(cursor.moveToNext())
        {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", cursor.getString(cursor.getColumnIndex("adname")));
            mList.add(map);
        }
        cursor.close();
        SimpleAdapter simpleAdapter = new SimpleAdapter(list_address.this,
                mList,
                R.layout.lsit_item_txl,
                new String[]{"name"},
                new int[]{R.id.adrname});
        list_adr.setAdapter(simpleAdapter);
    }

    View.OnClickListener listener_zjt = new View.OnClickListener(){
        public void onClick(View v) {
           String reback = application.getScore();
           Intent intent = null;
           // Log.i("aaa", reback);
           if(reback.equals("product"))
                intent = new Intent(list_address.this,index_product.class);
           else if(reback.equals("put"))
                intent = new Intent(list_address.this,index_put.class);
           else if(reback.equals("out"))
                intent = new Intent(list_address.this,index_out.class);
           else
                intent = new Intent(list_address.this,index_settle.class);
           startActivity(intent);
           list_address.this.finish();
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String reback = application.getScore();
            Intent intent = null;
            // Log.i("aaa", reback);
            if(reback.equals("product"))
                intent = new Intent(list_address.this,index_product.class);
            else if(reback.equals("put"))
                intent = new Intent(list_address.this,index_put.class);
            else if(reback.equals("out"))
                intent = new Intent(list_address.this,index_out.class);
            else
                intent = new Intent(list_address.this,index_settle.class);
            startActivity(intent);
            list_address.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    View.OnClickListener listener_tianjia = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(list_address.this,add_address.class);
            startActivity(intent);
            finish();
        }
    };
}
