package com.guo.qlzx.nongji.client.Costom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 李 on 2018/6/5.
 */

public class HintDialog extends Dialog {
    private static HintDialog hintDialog;
    private TextView tvName;
    public HintDialog(@NonNull Context context) {
        super(context);
    }

    public static HintDialog instance(Context context,String name){
        hintDialog=new HintDialog(context);
        hintDialog.show();
        hintDialog.setTvName(name);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                hintDialog.cancel();
            }
        },3000);
        return hintDialog;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pic);
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.dimAmount = 0.5f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.gravity = Gravity.CENTER;
        getWindow().setAttributes(attributes);
        tvName=findViewById(R.id.tv_name);
    }

    public void setTvName(String name) {
        tvName=findViewById(R.id.tv_name);
        tvName.setText(name);
    }
}
