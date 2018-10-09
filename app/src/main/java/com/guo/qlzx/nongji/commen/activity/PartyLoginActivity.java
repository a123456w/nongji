package com.guo.qlzx.nongji.commen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.activity.ShomeActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.service.activity.HomeActivity;
import com.guo.qlzx.nongji.service.bean.PartsDetailsBean;
import com.guo.qlzx.nongji.service.bean.PartyLoginBean;
import com.guo.qlzx.nongji.service.bean.SendBean;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 绑定手机号
 */
public class PartyLoginActivity extends BaseActivity {

    @BindView(R.id.ic_comeback)
    ImageView icComeback;
    @BindView(R.id.et_tells)
    EditText etTells;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_regcode)
    EditText etRegcode;
    @BindView(R.id.tv_reggetcode)
    Button tvReggetcode;
    @BindView(R.id.tv_complete)
    Button tvComplete;
    private PartyLoginBean data;

    private String pwd;
    private String role;
    private PreferencesHelper helper;
    private String etTell;

    @Override
    public int getContentView() {
        return R.layout.activity_party_login;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
          titleBar.setVisibility(View.GONE);
             helper=new PreferencesHelper(this);
         //完成的点击事件
            tvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入手机号
                etTell = etTells.getText().toString();
                //获取输入密码
                pwd = etPwd.getText().toString();

                //获取输入的短信验证码
                String cod = etRegcode.getText().toString();
                String openid = getIntent().getStringExtra("openid");
                String type = getIntent().getStringExtra("type");
                if (TextUtils.isEmpty(etTell)){
                    ToastUtil.showToast(PartyLoginActivity.this,"手机号码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(pwd)){
                    ToastUtil.showToast(PartyLoginActivity.this,"密码不能为空");
                    return;
                }
                if (pwd.length()<8||pwd.length()>16){
                    ToastUtil.showToast(PartyLoginActivity.this,"密码长度是8-16位");
                    return;
                }
                getBind(openid,type,etTell,pwd,cod,"11");
            }
        });
        //获取验证码点击事件
        tvReggetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String etTell = etTells.getText().toString();
                getcode(etTell,5);
            }
        });
        icComeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                            ToastUtil.showToast(PartyLoginActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }

                });
    }

    @Override
    public void getData() {

    }

    /**
     * 绑定手机号
     * @param openid
     * @param type
     * @param mobile
     * @param password
     * @param code
     * @param registration_id
     */
    private void getBind(String openid, String type, String mobile, String password, String code, String registration_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getBindingMobile(openid,type,mobile,password,code,registration_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<PartyLoginBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<PartyLoginBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            role = listBaseBean.data.getRole();
                            helper.saveUserRole(role);
                            if (role.equals("4")){
                                startActivity(new Intent(PartyLoginActivity.this,HomeActivity.class));
                                finish();
                            }else {
                                startActivity(new Intent(PartyLoginActivity.this,ShomeActivity.class));
                                finish();
                            }
                            if (role.equals("5")){
                                EventBusUtil.post(new SendBean());
                                ToastUtil.showToast(PartyLoginActivity.this,"管理人员登录");
                                startActivity(new Intent(PartyLoginActivity.this,HomeActivity.class));
                                finish();
                            }
                        }else if (listBaseBean.code==2){
                            ToastUtil.showToast(PartyLoginActivity.this,"手机号已被注册");
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });



    }
}
