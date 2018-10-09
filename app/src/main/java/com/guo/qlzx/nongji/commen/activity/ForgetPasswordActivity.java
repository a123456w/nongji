package com.guo.qlzx.nongji.commen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.activity.ShomeActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.service.activity.HomeActivity;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.ic_comeback)
    ImageView icComeback;
    @BindView(R.id.et_regtells)
    EditText etRegtells;
    @BindView(R.id.et_regcodes)
    EditText etRegcodes;
    @BindView(R.id.tv_reggetcodes)
    Button tvReggetcodes;
    @BindView(R.id.et_regpwd)
    EditText etRegpwd;
    @BindView(R.id.et_regpwds)
    EditText etRegpwds;
    @BindView(R.id.but_comm)
    Button butcomm;
    private Object data;

    @Override
    public int getContentView() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        new PreferencesHelper(this);
        hideTitleBar();
        icComeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void getData() {

    }

    /**
     * 注册
     */
    @OnClick(R.id.but_comm)
    public void onViewClicked1() {
        String etRegtell = etRegtells.getText().toString();//请输入手机号
        String etRegcode = etRegcodes.getText().toString();//请输入验证码
        String etRegpw = etRegpwd.getText().toString();//请输入新密码
        String etRegpwd = etRegpwds.getText().toString();//请再次输入密码
        if (TextUtils.isEmpty(etRegtell)) {
            ToastUtil.showToast(ForgetPasswordActivity.this, "手机号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(etRegcode)) {
            ToastUtil.showToast(ForgetPasswordActivity.this, "验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(etRegpw)) {
            ToastUtil.showToast(ForgetPasswordActivity.this, "新密码不能为空");
            return;
        }
        if (etRegpw.length()<8||etRegpw.length()>16){
            ToastUtil.showToast(ForgetPasswordActivity.this,"密码长度是8-16位");
            return;
        }
        if (TextUtils.isEmpty(etRegpwd)) {
            ToastUtil.showToast(ForgetPasswordActivity.this, "密码不能为空");
            return;
        }
        if(!etRegpwd.equals(etRegpw)){
            ToastUtil.showToast(ForgetPasswordActivity.this, "密码输入不一致");
            return;
        }
        //调用注册方法
        getFor(etRegtell,etRegpw,etRegcode);

    }

    private void getFor(String mobile,String password,String code) {
        HttpHelp.getInstance().create(RemoteApi.class).getForgetPassword(mobile,password,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            data = listBaseBean.data;
                            startActivity(ForgetPasswordActivity.this,LoginActivity.class);
                            ToastUtil.showToast(ForgetPasswordActivity.this, "修改成功");
                        }else {
                            ToastUtil.showToast(context,listBaseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });



    }
    /**
     * 获取验证码
     */
    @OnClick(R.id.tv_reggetcodes)
    public void onViewClicked() {
        String etRegtell = etRegtells.getText().toString();//请输入手机号
        if (TextUtils.isEmpty(etRegtell)) {
            ToastUtil.showToast(ForgetPasswordActivity.this, "手机号码不能为空");
            return;
        }
        //调用验证码方法
        getcode(etRegtell, 3);
    }
    /**
     * 验证码
     *
     * @param mobile
     * @param type
     */
    public void getcode(String mobile, int type) {
        HttpHelp.getInstance().create(RemoteApi.class).getCode(mobile, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            final Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                int i = 60;

                                @Override
                                public void run() {
                                    i--;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (i == 0) {
                                                timer.cancel();
                                                tvReggetcodes.setEnabled(true);
                                                tvReggetcodes.setText("重新获取验证码");
                                            } else {
                                                tvReggetcodes.setText(i + "秒后重新获取");
                                                tvReggetcodes.setEnabled(false);
                                            }
                                        }
                                    });
                                }
                            }, 1000, 1000);
                        } else {
                            ToastUtil.showToast(ForgetPasswordActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }

                });
    }

}
