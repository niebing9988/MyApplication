package com.example.administrator.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class information_address extends AppCompatActivity {
    View img_zjt;
    View img_tj;
    View img_bianji;
    View img_callout;
    View img_fixedcall;
    Button btn_del;
    Button btn_finish;
    TextView text_conname;
    TextView text_adname;
    TextView text_adtell;
    TextView text_fixedtell;
    TextView text_address;
    TextView text_url;
    TextView text_remarke;
    int addressid;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_address);
        context = this;

        img_zjt = findViewById(R.id.icon_zjt);
        img_tj = findViewById(R.id.img_tj);
        img_bianji = findViewById(R.id.icon_bianji);
        img_callout = findViewById(R.id.img_callout);
        img_fixedcall = findViewById(R.id.img_fixedcall);
        text_conname = findViewById(R.id.text_conname);
        text_adname = findViewById(R.id.text_adname);
        text_adtell = findViewById(R.id.text_adtell);
        text_fixedtell = findViewById(R.id.text_fixedtell);
        text_address = findViewById(R.id.text_address);
        text_url = findViewById(R.id.text_url);
        text_remarke = findViewById(R.id.text_remarke);
        btn_del = findViewById(R.id.btn_del);
        btn_finish = findViewById(R.id.btn_finish);

        img_zjt.setOnClickListener(listener_listadr);
        img_bianji.setOnClickListener(listener_bianji);


        advance();
        addstring();
    }
    public void advance(){
        text_conname.setEnabled(false);
        text_adname.setEnabled(false);
        text_adtell.setEnabled(false);
        text_address.setEnabled(false);
        text_url.setEnabled(false);
        text_remarke.setEnabled(false);
        text_fixedtell.setEnabled(false);
        btn_finish.setVisibility(View.INVISIBLE);
        btn_del.setVisibility(View.INVISIBLE);
        img_tj.setOnClickListener(null);
        img_callout.setOnClickListener(listener_call);
        img_fixedcall.setOnClickListener(listener_fixedcall);
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent back = new Intent(information_address.this,list_address.class);
            startActivity(back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void addstring(){
        //Log.i("aaaaaaaaa",getIntent().getStringExtra("addressid"));
        addressid = Integer.parseInt(getIntent().getStringExtra("addressid"));
        AssetsDatabaseManager.initManager(getApplication());
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        // 通过管理对象获取数据库
        SQLiteDatabase db1 = mg.getDatabase("store_management.db");
        Cursor cursor = db1.rawQuery("select * from tb_address where id = "+addressid,null);
        while (cursor.moveToNext())
        {
            text_conname.setText(cursor.getString(cursor.getColumnIndex("conname")));
            text_adname.setText(cursor.getString(cursor.getColumnIndex("adname")));
            text_adtell.setText(cursor.getString(cursor.getColumnIndex("adtell")));
            if (cursor.getString(cursor.getColumnIndex("fixedtell")).equals("0"))
            {
                text_fixedtell.setText("");
            }
            else
            {
                text_fixedtell.setText(cursor.getString(cursor.getColumnIndex("fixedtell")));
            }
            text_address.setText(cursor.getString(cursor.getColumnIndex("address")));
            text_url.setText(cursor.getString(cursor.getColumnIndex("url")));
            text_remarke.setText(cursor.getString(cursor.getColumnIndex("remarke")));
        }
    }
    View.OnClickListener listener_bianji = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
        text_conname.setEnabled(true);
        text_adname.setEnabled(true);
        text_adtell.setEnabled(true);
        text_address.setEnabled(true);
        text_url.setEnabled(true);
        text_remarke.setEnabled(true);
        text_fixedtell.setEnabled(true);

        text_conname.setFocusableInTouchMode(true);
        text_adname.setFocusableInTouchMode(true);
        text_adtell.setFocusableInTouchMode(true);
        text_fixedtell.setFocusableInTouchMode(true);
        text_address.setFocusableInTouchMode(true);
        text_url.setFocusableInTouchMode(true);
        text_remarke.setFocusableInTouchMode(true);
        img_tj.setOnClickListener(listener_tj);

        btn_finish.setVisibility(View.VISIBLE);
        btn_del.setVisibility(View.VISIBLE);
        btn_del.setOnClickListener(deladdress);
        btn_finish.setOnClickListener(finishadr);
        }
    };
    View.OnClickListener listener_fixedcall = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            String phone = text_fixedtell.getText().toString();
            Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };
    View.OnClickListener listener_call = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            String phone = text_adtell.getText().toString();
            Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };
    Button.OnClickListener finishadr = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            String conname = text_conname.getText().toString();
            String adname = text_adname.getText().toString();
            String adtell = text_adtell.getText().toString();
            int fixedtell = 0;
            try {
                if (!text_fixedtell.getText().toString().equals("")) {
                    fixedtell = Integer.parseInt(text_fixedtell.getText().toString());
                }
            }
            catch (Exception e)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("修改失败！").setMessage(
                        "固定电话格式不规范！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(2000);
                return;
            }
            String address = text_address.getText().toString();
            String url = text_url.getText().toString();
            String remarke = text_remarke.getText().toString();
            //Log.i("log", adname+adtell);
            if(adname.equals("")||adtell.equals(""))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("修改失败！").setMessage(
                        "联系人或移动电话不能为空！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(2000);
                return;
            }
            AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
            SQLiteDatabase db1 = mg.getDatabase("store_management.db");
            try {
                db1.execSQL("UPDATE tb_address SET conname=?,adname=?,adtell=?,fixedtell=?,address=?,url=?,remarke=? WHERE id = ?", new Object[]{conname,adname,adtell,fixedtell,address,url,remarke,addressid});
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("提示").setMessage(
                        "修改成功！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(1500);
                advance();
            }
            catch (Exception e)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("提示").setMessage(
                        "修改失败！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(1500);
            }
        }
    };
    Button.OnClickListener deladdress = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            new AlertDialog.Builder(context)
                    .setTitle("确定删除吗？")
                    .setPositiveButton("是", new  DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            try {
                                AssetsDatabaseManager.initManager(getApplication());
                                // 获取管理对象，因为数据库需要通过管理对象才能够获取
                                AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
                                // 通过管理对象获取数据库
                                SQLiteDatabase db1 = mg.getDatabase("store_management.db");
                                db1.execSQL("DELETE FROM tb_address WHERE id= ? ", new Object[]{addressid});
                                Intent intent = new Intent(information_address.this,list_address.class);
                                startActivity(intent);
                                finish();
                            }
                            catch (Exception e)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                final AlertDialog dialog = builder.setTitle("提示").setMessage(
                                        "删除失败！").create();
                                final alertDialog d = new alertDialog(dialog);
                                d.show(1500);
                            }
                        }
                    })
                    .setNegativeButton("否", null)
                    .show();
        }
    };
    View.OnClickListener listener_listadr = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(information_address.this,list_address.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener listener_tj =new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.PICK");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setType("vnd.android.cursor.dir/phone_v2");
            startActivityForResult(intent, 0x30);
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x30) {
            if (data != null) {
                Uri uri = data.getData();
                String phoneNum = null;
                String contactName = null;
                // 创建内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = null;
                if (uri != null) {
                    cursor = contentResolver.query(uri,
                            new String[]{"display_name","data1"}, null, null, null);
                }
                while (cursor.moveToNext()) {
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                cursor.close();
                //  把电话号码中的  -  符号 替换成空格
                if (phoneNum != null) {
                    phoneNum = phoneNum.replaceAll("-", " ");
                    // 空格去掉  为什么不直接-替换成"" 因为测试的时候发现还是会有空格 只能这么处理
                    phoneNum= phoneNum.replaceAll(" ", "");
                }

                text_adname.setText(contactName);
                text_adtell.setText(phoneNum);
            }
        }
    }
}
