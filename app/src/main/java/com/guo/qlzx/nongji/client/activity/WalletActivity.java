package com.guo.qlzx.nongji.client.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.MemoryBean;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogRecordDetailsActivity;
import com.guo.qlzx.nongji.service.activity.RechargeRecordActivity;
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
 * 钱包————用户端
 */
public class WalletActivity extends BaseActivity {
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_deposit)
    TextView tvDeposit;
    @BindView(R.id.ll_rechage)
    LinearLayout llRechage;
    @BindView(R.id.ll_postforword)
    LinearLayout llPostforword;
    @BindView(R.id.ll_bankcard)
    LinearLayout llBankcard;
    @BindView(R.id.ll_all)
    LinearLayout llAll;

    private PreferencesHelper helper;

    @Override
    public int getContentView() {
        return R.layout.activity_wallet;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();
        helper = new PreferencesHelper(this);
        llAll.setSystemUiVisibility(View.INVISIBLE);
    }

    @Override
    public void getData() {
        getMemoryData(helper.getToken());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSetPayPw(helper.getToken());
    }

    public void isSetPayPw(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).isSetPayPw(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {

                        } else if (listBaseBean.code == 1) {
                            getPay();
                        }else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(WalletActivity.this);
                        } else {
                            ToastUtil.showToast(WalletActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });

    }

    public void getMemoryData(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getMemoryData(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<MemoryBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<MemoryBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            tvBalance.setText("¥" + baseBean.data.getMoney());
                            tvDeposit.setText("¥" + baseBean.data.getDeposit());
                        } else {
                            ToastUtil.showToast(WalletActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    @OnClick({R.id.ll_rechage, R.id.ll_postforword, R.id.ll_bankcard, R.id.iv_left, R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //立即充值
            case R.id.ll_rechage:
                ToastUtil.showToast(this, "正在研发");
                break;
            //提现
            case R.id.ll_postforword:
                startActivity(this, WithdrawDdepositActivity.class);
                break;
            //银行卡管理
            case R.id.ll_bankcard:
                startActivity(this, BankActivity.class);
                break;
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_right:
               Intent intent=new Intent(WalletActivity.this, RechargeRecordActivity.class);
               intent.putExtra("TYPE",0);
               startActivity(intent);
                break;
        }
    }
    /**
     * 未设置支付密码，是否前往设置？
     */
    private void getPay() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.pay, null);
        Button imageView=contentView.findViewById(R.id.but_cans);
        final Button com=contentView.findViewById(R.id.but_coms);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletActivity.this, ChangePayPasswordWordActivity.class);
                intent.putExtra("TYPE", 1);
                startActivity(intent);
                bottomDialog.dismiss();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }
}
