package com.guo.qlzx.nongji.service.costom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
/*
* 登陆失效
* */
public class LogInFailureDialog extends Dialog {
    private TextView confirm;
    private TextView hint;
    private onConfirmClickListener onConfirmClickListener;
    public LogInFailureDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_failure);

        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.dimAmount = 0.5f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.gravity = Gravity.CENTER;
        getWindow().setAttributes(attributes);


        initView();
        initEvent();
    }

    private void initEvent() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onConfirmClickListener!=null){
                    onConfirmClickListener.onConfirm();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
           return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initView() {
        confirm=findViewById(R.id.tv_confirm);
    }

    public void setOnConfirmClickListener(LogInFailureDialog.onConfirmClickListener onConfirmClickListener) {
        this.onConfirmClickListener = onConfirmClickListener;
    }

    public interface onConfirmClickListener{
        void onConfirm();
    }
}
