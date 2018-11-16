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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.shenyuanqing.zxingsimplify.zxing.Activity.CaptureActivity;

public class add_product extends AppCompatActivity {
    View img_zjt;
    View img_tj;
    View img_sys;
    TextView text_procode;
    TextView text_proname;
    Spinner spinner_type;
    Spinner spinner_unit;
    TextColorNumberPicker picker_min;
    TextColorNumberPicker picker_max;
    Spinner spinner_adname;
    TextView text_price;
    TextView text_remarke;
    Context context;
    private static final int REQUEST_SCAN = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        context = this;

        img_zjt = findViewById(R.id.icon_zjt);
        img_tj = findViewById(R.id.icon_tj);
        img_sys = findViewById(R.id.img_syspro);
        text_procode = findViewById(R.id.text_procode);
        text_proname = findViewById(R.id.text_proname);
        spinner_type = findViewById(R.id.spinner_type);
        spinner_unit = findViewById(R.id.spinner_unit);
        picker_min = findViewById(R.id.picker_min);
        picker_max = findViewById(R.id.picker_max);
        spinner_adname = findViewById(R.id.spinner_adname);
        text_price = findViewById(R.id.text_price);
        text_remarke = findViewById(R.id.text_remarke);

        img_zjt.setOnClickListener(listener_zjt);
        img_sys.setOnClickListener(listener_sys);
        img_tj.setOnClickListener(listener_tj);
        picker_min.setMinValue(1);
        picker_min.setMaxValue(100);
        picker_min.setValue(5);
        picker_max.setMinValue(1);
        picker_max.setMaxValue(200);
        picker_max.setValue(100);

        spinner_init();
    }
    View.OnClickListener  listener_tj = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            String procode = text_procode.getText().toString();
            String proname = text_proname.getText().toString();
            String type = spinner_type.getSelectedItem().toString();
            String unit = spinner_unit.getSelectedItem().toString();
            int minvalue = picker_min.getValue();
            int maxvalue = picker_max.getValue();
            String adname = spinner_adname.getSelectedItem().toString();
            String price = text_price.getText().toString();
            String remarke = text_remarke.getText().toString();
            if (proname.equals("")||price.equals(""))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("添加失败！").setMessage(
                        "货品名称或入库价格不能为空！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(2000);
                return;
            }
            int pric;
            try {
                pric = Integer.valueOf(price);
            }catch (Exception e){AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("添加失败！").setMessage(
                        "价格不符合规范！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(2000);
                return;}

            if (minvalue>=maxvalue)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("添加失败！").setMessage(
                        "上限和下限不符合规范！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(2000);
                return;
            }
            AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
            SQLiteDatabase db1 = mg.getDatabase("store_management.db");
            try {
                db1.execSQL("insert into tb_instock (proname,storenumber) values (?,?)",new Object[]{proname,0});
                db1.execSQL("insert into tb_product (procode,proname,typename,unit,underline,online,adname,price,remarke) values (?,?,?,?,?,?,?,?,?)", new Object[]{procode,proname,type,unit,minvalue,maxvalue,adname,pric,remarke});
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("提示").setMessage(
                        "添加成功！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(1500);
                spinner_init();
                text_price.setText("");
                text_proname.setText("");
                text_procode.setText("");
                text_remarke.setText("");
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

    View.OnClickListener listener_sys = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            getRuntimeRight();
        }
    };

    private void getRuntimeRight() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(add_product.this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            jumpScanPage();
        }
    }
    private void jumpScanPage() {
        startActivityForResult(new Intent(add_product.this, CaptureActivity.class),REQUEST_SCAN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    jumpScanPage();
                } else {
                    Toast.makeText(context, "拒绝", Toast.LENGTH_LONG).show();
                }
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SCAN && resultCode == RESULT_OK){
            text_procode.setText(data.getStringExtra("barCode"));
        }
    }

    public void spinner_init(){
        String[] ctype = new String[]{"件", "条", "本", "袋", "斤"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.activity_tipsprice_spinner, ctype);  //创建一个数组适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        spinner_unit.setAdapter(adapter);

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
        ArrayAdapter<String> adapter_type = new ArrayAdapter<String>(this, R.layout.activity_tipsprice_spinner, ctype_type);  //创建一个数组适配器
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        spinner_type.setAdapter(adapter_type);

        Cursor cursor_ad = db1.rawQuery("select adname from tb_address",null);
//        Log.i("aaa", "spinner_init: "+cursor_ad.getCount());
        String[] ctype_ad = new String[cursor_ad.getCount()];
        int tmpee = 0;
        while(cursor_ad.moveToNext())
        {
            ctype_ad[tmpee] = cursor_ad.getString(cursor_ad.getColumnIndex("adname"));
            tmpee++;
        }
        cursor_ad.close();
        ArrayAdapter<String> adapter_ad = new ArrayAdapter<String>(this, R.layout.activity_tipsprice_spinner, ctype_ad);  //创建一个数组适配器
        adapter_ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);     //设置下拉列表框的下拉选项样式
        spinner_adname.setAdapter(adapter_ad);
    }

    View.OnClickListener listener_zjt = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(add_product.this,index_product.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent back = new Intent(add_product.this,index_product.class);
            startActivity(back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
