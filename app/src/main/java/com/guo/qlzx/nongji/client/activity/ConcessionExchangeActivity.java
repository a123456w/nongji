package com.guo.qlzx.nongji.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.ConcessionExchangeBean;
import com.guo.qlzx.nongji.commen.activity.LoginActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogRecordDetailsActivity;
import com.guo.qlzx.nongji.service.activity.PersonalCenterActivity;
import com.guo.qlzx.nongji.service.bean.LogBean;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 用户端——优惠码兑换
 */

public class ConcessionExchangeActivity extends BaseActivity {
    private PreferencesHelper helper;
    @BindView(R.id.et_concessioncode)
    EditText etConcessioncode;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private String token;

    @Override
    public int getContentView() {
        return R.layout.activity_concession_exchange;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("优惠码兑换");
        helper=new PreferencesHelper(this);
        token = helper.getToken();
    }

    @Override
    public void getData() {

    }



    @OnClick( R.id.btn_confirm)
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_confirm:
                String strcode = etConcessioncode.getText().toString();
                if (TextUtils.isEmpty(strcode)){
                    ToastUtil.showToast(ConcessionExchangeActivity.this,"优惠券码不能为空");
                    return;
                }
                exchangecode(token,strcode);
                break;
        }
    }
    public void exchangecode(String token,String coupon){
        HttpHelp.getInstance().create(RemoteApi.class).getconcession(token, coupon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ConcessionExchangeBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ConcessionExchangeBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code==0){
                            etConcessioncode.setText("");
                            ToastUtil.showToast(ConcessionExchangeActivity.this,listBaseBean.message);

                        }else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(ConcessionExchangeActivity.this);
                        }else {
                            ToastUtil.showToast(ConcessionExchangeActivity.this,listBaseBean.message);


                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

}
