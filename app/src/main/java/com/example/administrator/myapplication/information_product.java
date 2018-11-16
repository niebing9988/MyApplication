package com.example.administrator.myapplication;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import java.lang.reflect.Field;

public class information_product extends AppCompatActivity {
    TextColorNumberPicker maxPicker;
    TextColorNumberPicker minPicker;
    TextView text_proname;
    TextView text_price;
    TextView text_remarke;
    Spinner spinner_type;
    Spinner spinner_unit;
    Spinner spinner_adname;
    Context context;
    View img_zjt;
    int productid;
    Button btn_del;
    Button btn_finish;
    View img_bianji;
    MyApplication typepage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_product);
        context = this;
        typepage = (MyApplication) this.getApplication();

        minPicker =findViewById(R.id.Picker_min);
        maxPicker =findViewById(R.id.Picker_max);
        text_price = findViewById(R.id.text_price);
        text_proname = findViewById(R.id.text_proname);
        text_remarke = findViewById(R.id.text_remarke);
        spinner_type = findViewById(R.id.spin_typename);
        spinner_adname = findViewById(R.id.spin_adname);
        spinner_unit = findViewById(R.id.spin_unit);
        img_zjt = findViewById(R.id.icon_zjt);
        btn_finish = findViewById(R.id.btn_finish);
        btn_del = findViewById(R.id.btn_del);
        img_bianji = findViewById(R.id.icon_bianji);

        // 设置NumberPicker属性
        minPicker.setMinValue(1);
        minPicker.setMaxValue(100);
        minPicker.setValue(5);
        maxPicker.setMinValue(1);
        maxPicker.setMaxValue(200);
        maxPicker.setValue(100);
        img_zjt.setOnClickListener(listener_zjt);
        img_bianji.setOnClickListener(listener_bianji);
        spinner_init();
        addstring();
        advance();
    }
    View.OnClickListener listener_bianji = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            text_proname.setEnabled(true);
            spinner_type.setEnabled(true);
            spinner_unit.setEnabled(true);
            minPicker.setEnabled(true);
            maxPicker.setEnabled(true);
            spinner_adname.setEnabled(true);
            text_remarke.setEnabled(true);
            text_price.setEnabled(true);
            text_price.setText(text_price.getText().toString().substring(0,text_price.getText().toString().length()-1));

            btn_finish.setVisibility(View.VISIBLE);
            btn_del.setVisibility(View.VISIBLE);

            btn_del.setOnClickListener(delproduct);
            btn_finish.setOnClickListener(finishproduct);
        }
    };

    Button.OnClickListener finishproduct = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            String proname = text_proname.getText().toString();
            String type = spinner_type.getSelectedItem().toString();
            String unit = spinner_unit.getSelectedItem().toString();
            int minvalue = minPicker.getValue();
            int maxvalue = maxPicker.getValue();
            String adname = spinner_adname.getSelectedItem().toString();
            String price = text_price.getText().toString();
            String remarke = text_remarke.getText().toString();
            if (proname.equals("")||price.equals(""))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("修改失败！").setMessage(
                        "货品名称或入库价格不能为空！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(2000);
                return;
            }
            int pric;
            try {
                 pric = Integer.valueOf(price);
            }catch (Exception e){AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("修改失败！").setMessage(
                        "价格不符合规范！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(2000);
                return;}

            if (minvalue>=maxvalue)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("修改失败！").setMessage(
                        "上限和下限不符合规范！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(2000);
                return;
            }
            AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
            SQLiteDatabase db1 = mg.getDatabase("store_management.db");
            try {
                db1.execSQL("UPDATE tb_product SET proname=?,typename=?,unit=?,underline=?,online=?,adname=?,price=?,remarke=? where id = ?", new Object[]{proname,type,unit,minvalue,maxvalue,adname,pric,remarke,productid});
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog dialog = builder.setTitle("提示").setMessage(
                        "修改成功！").create();
                final alertDialog d = new alertDialog(dialog);
                d.show(1500);
                text_price.setText(text_price.getText().toString()+"元");
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

    Button.OnClickListener delproduct = new Button.OnClickListener(){

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
                                db1.execSQL("DELETE FROM tb_instock WHERE id= ? ", new Object[]{productid});
                                db1.execSQL("DELETE FROM tb_product WHERE id= ? ", new Object[]{productid});
                                Intent intent = new Intent(information_product.this,list_product.class);
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

    public void advance(){
        text_proname.setEnabled(false);
        spinner_type.setEnabled(false);
        spinner_unit.setEnabled(false);
        minPicker.setEnabled(false);
        maxPicker.setEnabled(false);
        spinner_adname.setEnabled(false);
        text_remarke.setEnabled(false);
        text_price.setEnabled(false);

        btn_finish.setVisibility(View.INVISIBLE);
        btn_del.setVisibility(View.INVISIBLE);
    };
    public void addstring(){
        //Log.i("aaaaaaaaa",getIntent().getStringExtra("addressid"));
        productid = Integer.parseInt(getIntent().getStringExtra("proid"));
        AssetsDatabaseManager.initManager(getApplication());
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        // 通过管理对象获取数据库
        SQLiteDatabase db1 = mg.getDatabase("store_management.db");
        Cursor cursor = db1.rawQuery("select * from tb_product where id = "+productid,null);
        while (cursor.moveToNext())
        {
            text_proname.setText(cursor.getString(cursor.getColumnIndex("proname")));

            SpinnerAdapter Adapter_type= spinner_type.getAdapter();
            int k_type= Adapter_type.getCount();
            for(int i=0;i<k_type;i++){
                if(cursor.getString(cursor.getColumnIndex("typename")).equals(Adapter_type.getItem(i).toString())){
                    spinner_type.setSelection(i,true);
                    break;
                }
            }

            SpinnerAdapter Adapter_unit= spinner_unit.getAdapter();
            int k_unit= Adapter_unit.getCount();
            for(int i=0;i<k_unit;i++){
                if(cursor.getString(cursor.getColumnIndex("unit")).equals(Adapter_unit.getItem(i).toString())){
                    spinner_unit.setSelection(i,true);
                    break;
                }
            }

            minPicker.setValue(cursor.getInt(cursor.getColumnIndex("underline")));
            maxPicker.setValue(cursor.getInt(cursor.getColumnIndex("online")));

            SpinnerAdapter Adapter_adname= spinner_adname.getAdapter();
            int k_adname= Adapter_adname.getCount();
            for(int i=0;i<k_adname;i++){
                if(cursor.getString(cursor.getColumnIndex("adname")).equals(Adapter_adname.getItem(i).toString())){
                    spinner_adname.setSelection(i,true);
                    break;
                }
            }
            text_price.setText(cursor.getString(cursor.getColumnIndex("price"))+"元");
            text_remarke.setText(cursor.getString(cursor.getColumnIndex("remarke")));
        }
    }

    View.OnClickListener listener_zjt = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            String ss = typepage.getScore();
            Intent back;
            if (ss.equals("yes")) {
                back = new Intent(information_product.this, product_typefilter.class);
            }
            else back = new Intent(information_product.this, list_product.class);
            startActivity(back);
            finish();
        }
    };
    private void spinner_init(){
        String[] ctype = new String[]{"件", "条", "本", "袋", "斤"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_tipsprice_spinner, ctype);  //创建一个数组适配器
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
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        boolean result = false;
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    result = true;
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String ss = typepage.getScore();
            Intent back;
            if (ss.equals("yes")) {
                back = new Intent(information_product.this, product_typefilter.class);
            }
            else back = new Intent(information_product.this, list_product.class);
            startActivity(back);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
