package com.guo.qlzx.nongji.commen.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.activity.ShomeActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.service.activity.HomeActivity;
import com.guo.qlzx.nongji.service.bean.RegBean;
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

/**
 * 注册类
 */
public class RegActivity extends BaseActivity {

    //获取控件id
    @BindView(R.id.et_regtell)
    EditText etRegtell;
    @BindView(R.id.et_regpwd)
    EditText etRegpwd;
    @BindView(R.id.et_regcode)
    EditText etRegcode;
    @BindView(R.id.tv_reggetcode)
    Button tvReggetcode;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.ic_comeback)
    ImageView icComeback;
    private PreferencesHelper helper;
    @Override
    public int getContentView() {
        return R.layout.activity_reg;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();
        icComeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        helper = new PreferencesHelper(this);
    }

    @Override
    public void getData() {
    }

    /**
     * 注册
     */
    @OnClick(R.id.tv_login)
    public void onViewClicked1() {
        String regtell = etRegtell.getText().toString();
        String regpwd = etRegpwd.getText().toString();
        String regcode = etRegcode.getText().toString();//验证码是code位int类型

        if (TextUtils.isEmpty(regtell)) {
            ToastUtil.showToast(RegActivity.this, "手机号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(regpwd)) {
            ToastUtil.showToast(RegActivity.this, "密码不能为空");
            return;
        }
        if (regpwd.length()<8||regpwd.length()>16){
            ToastUtil.showToast(RegActivity.this,"密码长度是8-16位");
            return;
        }
        if (TextUtils.isEmpty(regcode)) {
            ToastUtil.showToast(RegActivity.this, "验证码不能为空");
            return;
        }

        //调用注册方法
        Reg(regtell, regpwd, regcode, "11");

    }

    /**
     * 获取验证码
     */
    @OnClick(R.id.tv_reggetcode)
    public void onViewClicked() {
        String tell = etRegtell.getText().toString();
        if (TextUtils.isEmpty(tell)) {
            ToastUtil.showToast(RegActivity.this, "手机号码不能为空");
            return;
        }
        //调用验证码方法
        getcode(tell, 1);
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
                                                tvReggetcode.setEnabled(true);
                                                tvReggetcode.setText("重新获取验证码");
                                            } else {
                                                tvReggetcode.setText(i + "秒后重新获取");
                                                tvReggetcode.setEnabled(false);
                                            }
                                        }
                                    });
                                }
                            }, 1000, 1000);
                        } else {
                            ToastUtil.showToast(RegActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 注册
     *
     * @param password
     */
    public void Reg(String moblie, String password, String code, String registration_id) {
        HttpHelp.getInstance().create(RemoteApi.class).register(moblie, password, code, registration_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<RegBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<RegBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            String token = listBaseBean.data.getToken();
                            helper.saveToken(token);
                           String role = listBaseBean.data.getRole();
                            helper.saveUserRole(role);
                            if (role.equals(4)) {
                                startActivity(new Intent(RegActivity.this, HomeActivity.class));
                                finish();
                                LoginActivity.activity.finish();
                            } else {
                                startActivity(new Intent(RegActivity.this, ShomeActivity.class));
                                finish();
                                LoginActivity.activity.finish();
                            }

                        } else {
                            ToastUtil.showToast(RegActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
}
