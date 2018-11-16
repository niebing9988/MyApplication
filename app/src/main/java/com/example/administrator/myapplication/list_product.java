package com.example.administrator.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.github.shenyuanqing.zxingsimplify.zxing.Activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class list_product extends AppCompatActivity {
    View img_zjt;
    View img_sys;
    EditText text_sousuo;
    ListView list_pro;
    Context con;
    private Activity mActivity;
    private static final int REQUEST_SCAN = 0;
    MyApplication alc_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        con = this;
        mActivity = this;

        img_sys = findViewById(R.id.img_sys);
        img_zjt = findViewById(R.id.icon_zjt);
        text_sousuo = findViewById(R.id.edit_sousuo);
        list_pro = findViewById(R.id.list_pro);
        alc_list = (MyApplication) this.getApplication();
        String listString = alc_list.getScore();
        if (listString.equals("list")) {
            addString("select * from tb_product");
        }
        else if (listString.equals("loss"))
            addString("select * from tb_product INNER JOIN tb_instock on tb_product.id=tb_instock.id WHERE tb_instock.storenumber<tb_product.underline");
        else
            addString("select * from tb_product INNER JOIN tb_instock on tb_product.id=tb_instock.id WHERE tb_instock.storenumber>tb_product.online");
        img_zjt.setOnClickListener(listener_zjt);
        list_pro.setOnItemClickListener(listener_item);
        init();
        text_sousuo.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String text_ss =text_sousuo.getText().toString();
                text_ss = StringFilter.fstring(text_ss);
                // Log.i("log", text_sousuo);
                alc_list = (MyApplication) list_product.this.getApplication();
                String listString = alc_list.getScore();
                if (listString.equals("list")) {
                    addString("select * from tb_product where proname LIKE '%%" + text_ss + "%%' or procode LIKE '%%" + text_ss + "%%'");
                }
                else if (listString.equals("loss"))
                    addString("select * from tb_product INNER JOIN tb_instock on tb_product.id=tb_instock.id WHERE tb_instock.storenumber<tb_product.underline and (tb_product.proname LIKE '%%" + text_ss + "%%' or procode LIKE '%%" + text_ss + "%%')");
                else
                    addString("select * from tb_product INNER JOIN tb_instock on tb_product.id=tb_instock.id WHERE tb_instock.storenumber>tb_product.online and (tb_product.proname LIKE '%%" + text_ss + "%%' or procode LIKE '%%" + text_ss + "%%')");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    ListView.OnItemClickListener listener_item= new ListView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AssetsDatabaseManager.initManager(getApplication());
            // 获取管理对象，因为数据库需要通过管理对象才能够获取
            AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
            // 通过管理对象获取数据库
            SQLiteDatabase db1 = mg.getDatabase("store_management.db");
            Cursor cursor=null;
            alc_list = (MyApplication) list_product.this.getApplication();
            String listString = alc_list.getScore();
            if (listString.equals("list")) {
                cursor = db1.rawQuery("select * from tb_product",null);
            }
            else if (listString.equals("loss"))
                cursor = db1.rawQuery("select * from tb_product INNER JOIN tb_instock on tb_product.id=tb_instock.id WHERE tb_instock.storenumber<tb_product.underline",null);
            else
                cursor = db1.rawQuery("select * from tb_product INNER JOIN tb_instock on tb_product.id=tb_instock.id WHERE tb_instock.storenumber>tb_product.online",null);
            int temp= -1;
            while (cursor.moveToNext())
            {
                temp++;
                if(temp==position)
                {
                    int viewid = cursor.getInt(cursor.getColumnIndex("id"));
                    String Sviewid = String.valueOf(viewid);
                    Intent intent = new Intent(list_product.this,information_product.class);
                    intent.putExtra("proid",Sviewid);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        }
    };

    public void init() {
        img_sys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRuntimeRight();
//                Log.i("aaa", "onClick: 123212311");
            }
        });
    }
    private void getRuntimeRight() {
        if (ContextCompat.checkSelfPermission(con, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            jumpScanPage();
        }
    }
    private void jumpScanPage() {
        startActivityForResult(new Intent(list_product.this, CaptureActivity.class),REQUEST_SCAN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    jumpScanPage();
                } else {
                    Toast.makeText(con, "拒绝", Toast.LENGTH_LONG).show();
                }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SCAN && resultCode == RESULT_OK){
            text_sousuo.setText(data.getStringExtra("barCode"));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent back = new Intent(list_product.this,index_product.class);
            startActivity(back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    View.OnClickListener listener_zjt = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(list_product.this,index_product.class);
            startActivity(intent);
            finish();
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(list_product.this,
                mList,
                R.layout.lsit_item_product,
                new String[]{"proname","procode","storenumber"},
                new int[]{R.id.text_conname,R.id.text_procode,R.id.text_storenum});
        list_pro.setAdapter(simpleAdapter);
    }
}
