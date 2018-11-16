package com.example.administrator.myapplication;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.TextView;
public class add_address extends AppCompatActivity {
    View img_zjt;
    View img_tj;
    View img_tianjia;
    TextView text_conname;
    TextView text_adname;
    TextView text_adtell;
    TextView text_fixedtell;
    TextView text_address;
    TextView text_url;
    TextView text_remarke;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        context = this;

        img_zjt = findViewById(R.id.icon_zjt);
        img_tj = findViewById(R.id.img_tj);
        img_tianjia = findViewById(R.id.icon_bianji);
        text_conname = findViewById(R.id.text_conname);
        text_adname = findViewById(R.id.text_adname);
        text_adtell = findViewById(R.id.text_adtell);
        text_fixedtell = findViewById(R.id.text_fixedtell);
        text_address = findViewById(R.id.text_address);
        text_url = findViewById(R.id.text_url);
        text_remarke = findViewById(R.id.text_remarke);

        img_tj.setOnClickListener(listener_tj);
        img_tianjia.setOnClickListener(listener_tianjia);
        img_zjt.setOnClickListener(listener_listadr);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent back = new Intent(add_address.this,list_address.class);
            startActivity(back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    View.OnClickListener listener_tianjia = new View.OnClickListener(){
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
                final AlertDialog dialog = builder.setTitle("添加失败！").setMessage(
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
                final AlertDialog dialog = builder.setTitle("添加失败！").setMessage(
                        "联系人或移动电话不能为空！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(2000);
                return;
            }
            AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
            SQLiteDatabase db1 = mg.getDatabase("store_management.db");
            try {
               db1.execSQL("INSERT INTO tb_address (conname,adname,adtell,fixedtell,address,url,remarke) VALUES (?,?,?,?,?,?,?)", new Object[]{conname,adname,adtell,fixedtell,address,url,remarke});
               text_conname.setText("");
               text_adtell.setText("");
               text_adname.setText("");
               text_fixedtell.setText("");
               text_address.setText("");
               text_url.setText("");
               text_remarke.setText("");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("提示").setMessage(
                        "添加成功！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(1500);
            }
            catch (Exception e)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("提示").setMessage(
                        "添加失败！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(1500);
            }

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
    View.OnClickListener listener_listadr = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(add_address.this,list_address.class);
            startActivity(intent);
            finish();
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
