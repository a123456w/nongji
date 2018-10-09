package com.guo.qlzx.nongji.client.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.activity.ForgetPasswordActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogRecordDetailsActivity;
import com.guo.qlzx.nongji.service.activity.OrderManageMentActivity;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/5/31.
 * 用户端-修改支付密码
 */

public class ChangePayPasswordWordActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_old)
    EditText etOld;
    @BindView(R.id.et_new)
    EditText etNew;
    @BindView(R.id.et_again)
    EditText etAgain;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private PreferencesHelper helper;
    private int type = 0;

    @Override
    public int getContentView() {
        return R.layout.activity_change_pw;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        type = getIntent().getIntExtra("TYPE", 0);
        if (0 == type) {
            titleBar.setTitleText("更改支付密码");
            etOld.setVisibility(View.VISIBLE);
        } else {
            titleBar.setTitleText("设置支付密码");
            etOld.setVisibility(View.GONE);
        }
        titleBar.setRightImageRes(R.drawable.ic_titlewallet);
        btnSubmit.setOnClickListener(this);
        helper = new PreferencesHelper(this);
    }

    @Override
    public void getData() {

    }

    /**
     * 确定修改
     */
    private void getSubmit(String token, String old, String newPw, String again) {
        HttpHelp.getInstance().create(RemoteApi.class).changePayPw(token, old, newPw, again)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            ToastUtil.showToast(ChangePayPasswordWordActivity.this, "设置成功");
                            finish();
                        } else {
                            ToastUtil.showToast(ChangePayPasswordWordActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void setPayPw(String token, String newPw, String again) {
        HttpHelp.getInstance().create(RemoteApi.class).setPayPw(token, newPw, again)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            ToastUtil.showToast(ChangePayPasswordWordActivity.this, "设置成功");
                            finish();
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.getOutLogs(ChangePayPasswordWordActivity.this);
                        } else {
                            ToastUtil.showToast(ChangePayPasswordWordActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        String old = etOld.getText().toString();
        String newPw = etNew.getText().toString();
        String again = etAgain.getText().toString();
        if (etOld.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(old)) {
                ToastUtil.showToast(ChangePayPasswordWordActivity.this, "原密码不能为空");
                return;
            }
        }
        if (TextUtils.isEmpty(newPw)) {
            ToastUtil.showToast(ChangePayPasswordWordActivity.this, "新密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(again)) {
            ToastUtil.showToast(ChangePayPasswordWordActivity.this, "新密码不能为空");
            return;
        }
        if (again.length() != 6) {
            ToastUtil.showToast(ChangePayPasswordWordActivity.this, "密码长度是6位");
            return;
        }
        if (!again.equals(newPw)) {
            ToastUtil.showToast(ChangePayPasswordWordActivity.this, "两次输入不一致");
            return;
        }
        if (etOld.getVisibility() == View.VISIBLE) {
            getSubmit(helper.getToken(), old, newPw, again);
        } else {
            setPayPw(helper.getToken(), newPw, again);
        }
    }
}
