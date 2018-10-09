package com.guo.qlzx.nongji.client.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.Costom.HintDialog;
import com.guo.qlzx.nongji.client.bean.BankNameBean;
import com.guo.qlzx.nongji.client.event.BankEvent;
import com.guo.qlzx.nongji.commen.activity.RegActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogRecordDetailsActivity;
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
import butterknife.OnClick;
import retrofit2.http.Field;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/6/4.
 */

public class FinishBankActivity extends BaseActivity {
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_get)
    Button btnGet;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private PreferencesHelper helper;
    private String type = "";
    private String bankNum = "";

    @Override
    public int getContentView() {
        return R.layout.activity_finish_bank;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("添加银行卡");
        helper = new PreferencesHelper(this);
        type = getIntent().getStringExtra("TYPE");
        bankNum = getIntent().getStringExtra("BANKCARD");
        tvType.setText(type);
    }

    @Override
    public void getData() {

    }

    public void getCode(String mobile, int type) {
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
                                                btnGet.setEnabled(true);
                                                btnGet.setText("重新获取验证码");
                                            } else {
                                                btnGet.setText(i + "秒后重新获取");
                                                btnGet.setEnabled(false);
                                            }
                                        }
                                    });
                                }
                            }, 1000, 1000);
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.getOutLogs(FinishBankActivity.this);
                        } else {
                            ToastUtil.showToast(FinishBankActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }

                });
    }

    private void submit(String token, String phone, String code, String ic_number) {
        HttpHelp.getInstance().create(RemoteApi.class).finishBankCard(token, phone, code, ic_number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            HintDialog hintDialog = HintDialog.instance(FinishBankActivity.this, baseBean.message);
                            hintDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    if (AddBankActivity.instance != null) {
                                        AddBankActivity.instance.finish();
                                        AddBankActivity.instance = null;
                                    }
                                    EventBusUtil.post(new BankEvent());
                                    finish();
                                }
                            });
                        } else if (baseBean.code == 4) {
                            ToolUtil.getOutLogs(FinishBankActivity.this);
                        } else {
                            ToastUtil.showToast(FinishBankActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @OnClick({R.id.btn_submit, R.id.btn_get})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                String number = etNumber.getText().toString();
                String password = etCode.getText().toString();
                if (TextUtils.isEmpty(number)) {
                    ToastUtil.showToast(FinishBankActivity.this, "手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showToast(FinishBankActivity.this, "验证码不能为空");
                    return;
                }
                submit(helper.getToken(), number, password, bankNum);
                break;
            case R.id.btn_get:
                //发送验证码
                String phone = etNumber.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast(FinishBankActivity.this, "手机号不能为空");
                    return;
                }
                if (!ToolUtil.isChinaPhoneLegal(phone)) {
                    ToastUtil.showToast(FinishBankActivity.this, "手机号格式不正确");
                    return;
                }
                getCode(phone, 4);
                break;
        }
    }
}
