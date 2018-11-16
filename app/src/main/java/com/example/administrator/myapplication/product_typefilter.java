package com.example.administrator.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class product_typefilter extends AppCompatActivity {
    View img_zjt;
    Spinner spinner_type;
    ListView list_pro;
    MyApplication typevalue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_typefilter);
        MyApplication typepage = (MyApplication)this.getApplication();
        typepage.setScore("yes");

        img_zjt = findViewById(R.id.icon_zjt);
        spinner_type = findViewById(R.id.spinner_type);
        list_pro = findViewById(R.id.list_pro);

        img_zjt.setOnClickListener(listener_zjt);
        list_pro.setOnItemClickListener(listener_listpro);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerAdapter Adapter= spinner_type.getAdapter();
                String values = Adapter.getItem(position).toString();
                //Log.i("aaa", "onItemSelected: "+values);
                addString(values);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        init();
        String type = spinner_type.getSelectedItem().toString();
        addString(type);
    }
    ListView.OnItemClickListener listener_listpro= new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AssetsDatabaseManager.initManager(getApplication());
            // 获取管理对象，因为数据库需要通过管理对象才能够获取
            AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
            // 通过管理对象获取数据库
            SQLiteDatabase db1 = mg.getDatabase("store_management.db");
            Cursor cursor=null;
            String type = spinner_type.getSelectedItem().toString();
            typevalue = (MyApplication) product_typefilter.this.getApplication();
            typevalue.setScore_type(type);
            cursor = db1.rawQuery("select * from tb_product where typename = '" +type+"'",null);
            int temp= -1;
            while (cursor.moveToNext())
            {
                temp++;
                if(temp==position)
                {
                    int viewid = cursor.getInt(cursor.getColumnIndex("id"));
                    String Sviewid = String.valueOf(viewid);
                    Intent intent = new Intent(product_typefilter.this,information_product.class);
                    intent.putExtra("proid",Sviewid);
                    intent.putExtra("typepage","yes");
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        }
    };

    private void addString(String ss){
        ArrayList<HashMap<String, String>> mList = null;
        mList = new ArrayList<>();
        AssetsDatabaseManager.initManager(getApplication());
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        // 通过管理对象获取数据库
        SQLiteDatabase db1 = mg.getDatabase("store_management.db");
        //Log.i("aaa", "addString: "+type);
        Cursor cursor = db1.rawQuery("select * from tb_product where typename = '" +ss+"'",null);
        while(cursor.moveToNext())
        {
            String proname = cursor.getString(cursor.getColumnIndex("proname"));
            String unit = cursor.getString(cursor.getColumnIndex("unit"));
            //Log.i("aaaaaaaaaa", proname+unit);
            String residue = null;
            try{
                Cursor cur = db1.rawQuery("select * from tb_instock where proname='"+proname+"'",null);
                while (cur.moveToNext())
                {
                    residue = cur.getString(cur.getColumnIndex("storenumber"));
                }
                if(residue.equals(null))
                {
                    residue = "无";
                }
                else
                    residue = residue+unit;
                cur.close();
            }catch (Exception e)
            {
                residue = "无";
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("proname", proname);
            map.put("procode", cursor.getString(cursor.getColumnIndex("procode")));
            map.put("storenumber", residue);
            mList.add(map);
        }
        cursor.close();
        SimpleAdapter simpleAdapter = new SimpleAdapter(product_typefilter.this,
                mList,
                R.layout.lsit_item_product,
                new String[]{"proname","procode","storenumber"},
                new int[]{R.id.text_conname,R.id.text_procode,R.id.text_storenum});
        list_pro.setAdapter(simpleAdapter);
    }
    private void init()
    {
        AssetsDatabaseManager.initManager(getApplication());
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        // 通过管理对象获取数据库
        SQLiteDatabase db1 = mg.getDatabase("store_management.db");
        Cursor cursor = db1.rawQuery("select typename from tb_type",null);
        String[] ctype_type = new String[cursor.getCount()];
        int tmpe = 0;
        while(cursor.moveToNext())
        {
            ctype_type[tmpe] = cursor.getString(cursor.getColumnIndex("typename"));
            tmpe++;
        }
        cursor.close();
        ArrayAdapter<String> adapter_type = new ArrayAdapter<String>(this, R.layout.activity_tipsprice_spinner_black, ctype_type);  //创建一个数组适配器
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        spinner_type.setAdapter(adapter_type);

        SpinnerAdapter Adap= spinner_type.getAdapter();
        typevalue = (MyApplication) product_typefilter.this.getApplication();
        String sss = typevalue.getScore_type();
        int k= Adap.getCount();
        for(int i=0;i<k;i++){
            if(sss.equals(Adap.getItem(i).toString())){
                spinner_type.setSelection(i,true);
                break;
            }
        }
    }
    View.OnClickListener listener_zjt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(product_typefilter.this,index_product.class);
            startActivity(intent);
            finish();
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent back = new Intent(product_typefilter.this,index_product.class);
            startActivity(back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
