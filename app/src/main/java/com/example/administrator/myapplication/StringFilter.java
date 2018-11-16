package com.example.administrator.myapplication;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringFilter {
    private static Toast toast;
    public static String fstring(String str) throws PatternSyntaxException {
        String regEx = "[\"+,.&=]*";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    public static void showToast(Context context, String message){
        if (toast==null){
            toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        }else {
            toast.setText(message);
        }
        toast.show();//设置新的消息提示
    }
}
