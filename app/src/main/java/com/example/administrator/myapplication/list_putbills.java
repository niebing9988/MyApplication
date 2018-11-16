package com.example.administrator.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.example.administrator.myapplication.StringFilter.showToast;

public class list_putbills extends AppCompatActivity {
    View img_zjt;
    TextColorNumberPicker spinner_minyear;
    TextColorNumberPicker spinner_minmonth;
    TextColorNumberPicker spinner_minday;
    TextColorNumberPicker spinner_maxyear;
    TextColorNumberPicker spinner_maxmonth;
    TextColorNumberPicker spinner_maxday;
    ListView list_pubill;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_list_putbills);
        img_zjt = findViewById(R.id.icon_zjt);
        spinner_minyear = findViewById(R.id.minyear);
        spinner_minmonth = findViewById(R.id.minmonth);
        spinner_minday = findViewById(R.id.minday);
        spinner_maxmonth = findViewById(R.id.maxmonth);
        spinner_maxyear = findViewById(R.id.maxyear);
        spinner_maxday = findViewById(R.id.maxday);
        list_pubill = findViewById(R.id.list_putbill);

        img_zjt.setOnClickListener(listener_zjt);
        spinner_minmonth.setOnValueChangedListener(listener_minvalue);
        spinner_minday.setOnValueChangedListener(listener_date);
        spinner_minyear.setOnValueChangedListener(listener_date);
        spinner_maxyear.setOnValueChangedListener(listener_date);
        spinner_maxday.setOnValueChangedListener(listener_date);
        spinner_maxmonth.setOnValueChangedListener(listener_maxvalue);
        spinner_init();
        addString("select * from tb_putbill");
    }

    private void addString(String sql){
        ArrayList<HashMap<String, String>> mList = null;
        mList = new ArrayList<>();
        AssetsDatabaseManager.initManager(getApplication());
        // 获取管理对象，因为数据库需要通过管理对象才能够获取
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        // 通过管理对象获取数据库
        SQLiteDatabase db1 = mg.getDatabase("store_management.db");
        Cursor cursor = db1.rawQuery(sql,null);
        int mindate;
        int maxdate;

        String s_minmonth;
        String s_minday;
        String s_maxmonth;
        String s_maxday;
        int minmonth = spinner_minmonth.getValue();
        int minday = spinner_minday.getValue();
        int maxmonth = spinner_maxmonth.getValue();
        int maxday = spinner_maxday.getValue();
        if(minmonth<10)
        {
            s_minmonth = String.valueOf("0"+minmonth);
        }
        else s_minmonth = String.valueOf(minmonth);
        if(minday<10)
        {
            s_minday = String.valueOf("0"+minday);
        }
        else s_minday = String.valueOf(minday);
        if(maxmonth<10)
        {
            s_maxmonth = String.valueOf("0"+maxmonth);
        }
        else s_maxmonth = String.valueOf(maxmonth);
        if(maxday<10)
        {
            s_maxday = String.valueOf("0"+maxday);
        }
        else s_maxday = String.valueOf(maxday);
        mindate = Integer.valueOf(spinner_minyear.getValue()+s_minmonth+s_minday);
        maxdate = Integer.valueOf(spinner_maxyear.getValue()+s_maxmonth+s_maxday);
        if (mindate>maxdate)
        {
            showToast(context,"时间段输入错误!");
        }
        //Log.i("aaa", mindate+"   "+maxdate +"  "+temp);
        while(cursor.moveToNext())
        {
            String date = cursor.getString(cursor.getColumnIndex("date"));
            int temp = Integer.valueOf(date.substring(0,4)+date.substring(5,7)+date.substring(8,10));

            if(temp<=maxdate&&temp>=mindate) {
                String billnum = cursor.getString(cursor.getColumnIndex("billnum"));
                String proname = cursor.getString(cursor.getColumnIndex("proname"));
                String[] splitname = proname.split(",");
                String putnumber = cursor.getString(cursor.getColumnIndex("putnumber"));
                String[] splitnumber = putnumber.split(",");
                int num = 0;
                for (String string2 : splitnumber) {
                    num = num + Integer.valueOf(string2);
                }
                String summoney = String.valueOf(cursor.getInt(cursor.getColumnIndex("summoney")));
                //Log.i("aaaaaaaaaa", proname+unit);
                HashMap<String, String> map = new HashMap<>();
                map.put("billnum", billnum);
                map.put("date", date);
                map.put("proname", +splitname.length + "种商品共" + num + "件");
                map.put("summoney", "总金额:" + summoney + "元");
                mList.add(map);
            }
            else continue;
        }
        cursor.close();
        SimpleAdapter simpleAdapter = new SimpleAdapter(list_putbills.this,
                mList,
                R.layout.lsit_item_putbills,
                new String[]{"billnum","date","proname","summoney"},
                new int[]{R.id.text_billnum,R.id.text_date,R.id.text_proname,R.id.text_summoney});
        list_pubill.setAdapter(simpleAdapter);
    }
    TextColorNumberPicker.OnValueChangeListener listener_maxvalue = new TextColorNumberPicker.OnValueChangeListener()
    {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            Calendar ccd = Calendar.getInstance();
            ccd.set(Calendar.MONTH,newVal-1);
            ccd.set(Calendar.DATE, 1);//把日期设置为当月第一天
            ccd.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
            int minday = ccd.get(Calendar.DATE);
            //Log.i("aaa", "onValueChange: "+minday);
            spinner_maxday.setMaxValue(minday);
            addString("select * from tb_putbill");
        }
    };
    TextColorNumberPicker.OnValueChangeListener listener_minvalue = new TextColorNumberPicker.OnValueChangeListener()
    {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            Calendar ccd = Calendar.getInstance();
            ccd.set(Calendar.MONTH,newVal-1);
            ccd.set(Calendar.DATE, 1);//把日期设置为当月第一天
            ccd.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
            int minday = ccd.get(Calendar.DATE);
            //Log.i("aaa", "onValueChange: "+minday);
            spinner_minday.setMaxValue(minday);
            addString("select * from tb_putbill");
        }
    };
    TextColorNumberPicker.OnValueChangeListener listener_date = new TextColorNumberPicker.OnValueChangeListener()
    {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            addString("select * from tb_putbill");
        }
    };
    private void spinner_init(){
        Calendar ccd = Calendar.getInstance();
        int yearnow = ccd.get(Calendar.YEAR);
        ccd.set(Calendar.DATE, 1);//把日期设置为当月第一天
        ccd.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int minday = ccd.get(Calendar.DATE);
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DATE,cd.get(Calendar.DATE)-7);
        spinner_minday.setMinValue(1);
        spinner_minday.setMaxValue(minday);
        spinner_minday.setValue(cd.get(Calendar.DATE));
        spinner_minmonth.setMinValue(1);
        spinner_minmonth.setMaxValue(12);
        spinner_minmonth.setValue(cd.get(Calendar.MONTH)+1);
        spinner_minyear.setMinValue(yearnow-5);
        spinner_minyear.setMaxValue(yearnow);
        spinner_minyear.setValue(cd.get(Calendar.YEAR));

        Calendar cdd = Calendar.getInstance();
        spinner_maxday.setMinValue(1);
        spinner_maxday.setMaxValue(minday);
        spinner_maxday.setValue(cdd.get(Calendar.DATE));
        spinner_maxmonth.setMinValue(1);
        spinner_maxmonth.setMaxValue(12);
        spinner_maxmonth.setValue(cdd.get(Calendar.MONTH)+1);
        spinner_maxyear.setMinValue(yearnow-5);
        spinner_maxyear.setMaxValue(yearnow);
        spinner_maxyear.setValue(cdd.get(Calendar.YEAR));

    }
    View.OnClickListener listener_zjt = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            finish();
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
            }
        return super.onKeyDown(keyCode, event);
    }
}
