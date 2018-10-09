package com.guo.qlzx.nongji.client.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.Costom.PasswordInputEdt;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogRecordDetailsActivity;
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
 * Created by 李 on 2018/6/5.
 * 解绑页面
 */

public class UnBindBankCardActivity extends BaseActivity {
    @BindView(R.id.et_pw)
    PasswordInputEdt etPw;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private String id = "";
    private PreferencesHelper helper;

    private String pw="";
    @Override
    public int getContentView() {
        return R.layout.activity_unbind_bank;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(this);
        titleBar.setTitleText("解绑");
        id = getIntent().getStringExtra("BANKID");
        etPw.setOnInputOverListener(new PasswordInputEdt.onInputOverListener() {
            @Override
            public void onInputOver(String text) {
                pw=text;
                btnSubmit.setEnabled(true);
            }
        });
        etPw.setLessThanSixClick(new PasswordInputEdt.onWordLessThanSixClick() {
            @Override
            public void onClick(String word) {
                btnSubmit.setEnabled(false);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(helper.getToken(),id,pw);
            }
        });
    }

    @Override
    public void getData() {

    }

    private void submit(String token, String id, String pw) {
        HttpHelp.getInstance().create(RemoteApi.class).unBindBankCard(token, id, pw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            setResult(200);
                                finish();
                        }else if (baseBean.code==4){
                            ToolUtil.getOutLogs(UnBindBankCardActivity.this);
                        } else {
                            ToastUtil.showToast(UnBindBankCardActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
}
