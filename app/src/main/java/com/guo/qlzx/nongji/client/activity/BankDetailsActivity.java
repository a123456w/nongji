package com.guo.qlzx.nongji.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.BankDetailsBean;
import com.guo.qlzx.nongji.client.bean.BankNameBean;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogRecordDetailsActivity;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.lang.ref.PhantomReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/6/5.
 * 银行卡详情页面
 */

public class BankDetailsActivity extends BaseActivity {
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_single)
    TextView tvSingle;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private String id="";
    @Override
    public int getContentView() {
        return R.layout.activity_bank_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);

        titleBar.setTitleText("银行卡");
        id=getIntent().getStringExtra("BANKID");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BankDetailsActivity.this,UnBindBankCardActivity.class);
                intent.putExtra("BANKID",id);
                startActivityForResult(intent,200);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200){
            finish();
        }
    }

    @Override
    public void getData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetailsData( new PreferencesHelper(this).getToken(),id);
    }

    private void getDetailsData(String token, String id){
        HttpHelp.getInstance().create(RemoteApi.class).getBankDetailsData(token,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<BankDetailsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<BankDetailsBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            upDataUi(baseBean.data);

                        }else if (baseBean.code==4){
                            ToolUtil.getOutLogs(BankDetailsActivity.this);
                        }else {
                            ToastUtil.showToast(BankDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    private void upDataUi(BankDetailsBean data) {
        GlideUtil.displayAvatar(BankDetailsActivity.this, Constants.IMG_HOST+data.getPic(),ivImg);
        tvName.setText(data.getBank()+"   "+data.getCard_type());
        tvNumber.setText(data.getCard_number());
        tvDay.setText("¥"+data.getDay_limit());
        tvSingle.setText("¥"+data.getSingle_limit());
    }
}
