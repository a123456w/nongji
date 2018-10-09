package com.guo.qlzx.nongji.commen.util;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.activity.LoginActivity;
import com.guo.qlzx.nongji.service.costom.LogInFailureDialog;
import com.qlzx.mylibrary.collector.ActivityCollector;
import com.qlzx.mylibrary.util.PreferencesHelper;

import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by dillon on 2017/7/19.
 */

public class ToolUtil {


    public static String hideText(String string) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < string.length() - 4; i++) {
            stringBuffer.append("*");
        }
        String subString = string.substring(string.length() - 4);
        stringBuffer.append(subString);
        return stringBuffer.toString();
    }

    /**
     * 正则表达式 判断邮箱格式是否正确
     * */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断手机号的正则表达式
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^1(3|4|5|7|8)\\d{9}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * md5加密
     * @param info
     * @return
     */
    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }









    /**
     * 时间戳转为时间(年月日，时分秒)
     *
     * @param cc_time 时间戳
     * @return
     */
    public static String getStrTime(String cc_time,String pattern) {
        String re_StrTime = null;
        //同理也可以转为其它样式的时间格式.例如："yyyy/MM/dd HH:mm:ss"
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));

        return re_StrTime;
    }

    /**
     * 时间转换为时间戳
     *
     * @param timeStr 时间 例如: 2016-03-09
     * @param format  时间对应格式  例如: yyyy-MM-dd
     * @return
     */
    public static long getTimeStamp(String timeStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(timeStr);
            long timeStamp = date.getTime();
            return timeStamp/1000  ;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取手机 Mac 地址
     * @return
     */
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    /**
     * 手机号用****号隐藏中间数字
     *
     * @param phone
     * @return
     */
    public static String setTingPhone(String phone) {
        String phone_s = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phone_s;
    }

    /**
     * 拨打电话
     * @param context
     * @param phoneNum
     */
    public static void goToCall(final Context context, final String phoneNum) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("您将拨打电话" + phoneNum);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + phoneNum);
                intent.setData(data);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},
                            100);
                }else {
                    context.startActivity(intent);
                    dialog.cancel();
                }
            }
        });
        builder.create().show();
    }
    public static long getPresentTime(){
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        long time=getTimeStamp(str,"yyyy年MM月dd日   HH:mm:ss");
        return time;
    }
    //将list转换为带有 ， 的字符串
    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    public static String stringFilter(String str)throws PatternSyntaxException{
        // 只允许字母、数字和汉字
        String   regEx  =  "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern   p   =   Pattern.compile(regEx);
        Matcher   m   =   p.matcher(str);
        return   m.replaceAll("").trim();
    }
    /*
    * 账号失效 重新登陆提示
    * */
    public static void getOutLogs(final Context activity) {
        ActivityCollector.finishAll();
        new PreferencesHelper(activity).clear();
        Intent intent=new  Intent(activity,LoginActivity.class).putExtra("tag","1");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);

    }
}
