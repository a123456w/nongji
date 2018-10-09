package com.guo.qlzx.nongji.client.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.Costom.AmountEditText;
import com.guo.qlzx.nongji.client.Costom.DeclarePopWindow;
import com.guo.qlzx.nongji.client.Costom.HintDialog;
import com.guo.qlzx.nongji.client.bean.BankListBean;
import com.guo.qlzx.nongji.client.bean.FailureDeclareListBean;
import com.guo.qlzx.nongji.client.bean.MemoryBean;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.http.Field;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 客户端钱包提现
 */
public class WithdrawDdepositActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.et_cash)
    AmountEditText etCash;
    @BindView(R.id.et_pay)
    EditText etPay;
    @BindView(R.id.btn_cash)
    Button btnCash;

    private PreferencesHelper helper;
    private Double memory=0.0;
    //银行卡
    private List<BankListBean> list=new ArrayList<>();
    private String id="";
    @Override
    public int getContentView() {
        return R.layout.activity_withdraw_ddeposit;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("提现");
        helper=new PreferencesHelper(this);
    }

    @Override
    public void getData() {
//        //获取银行卡列表
//        getBankList(helper.getToken());
        //获取余额
        getMemoryData(helper.getToken());
    }
    private void submit(String token,String bank_id, String money,String pay_password){
        HttpHelp.getInstance().create(RemoteApi.class).getMemoryCash(token,bank_id,money,pay_password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {

                            HintDialog hintDialog=HintDialog.instance(WithdrawDdepositActivity.this,baseBean.message);
                            etCash.setText("");
                            etPay.setText("");
                        }
                        else if (baseBean.code==4){
                            ToolUtil.getOutLogs(WithdrawDdepositActivity.this);
                        }else {
                            ToastUtil.showToast(WithdrawDdepositActivity.this, baseBean.message);
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
                            memory=Double.valueOf(baseBean.data.getMoney());
                        } else {
                            ToastUtil.showToast(WithdrawDdepositActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }
    public void getBankList(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getBankList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<BankListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<BankListBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                             list=baseBean.data;
                             if (list!=null&&list.size()!=0){
                                 tvName.setText(list.get(0).getBank());
                                 id=list.get(0).getId();
                             }else {
                                 getAddbank();
                             }
                        }else if (baseBean.code==4){
                            ToolUtil.getOutLogs(WithdrawDdepositActivity.this);
                        } else {
                            ToastUtil.showToast(WithdrawDdepositActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }
    @OnClick({R.id.btn_cash, R.id.tv_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //立即提现
            case R.id.btn_cash:
                Double erMemory=Double.valueOf(etCash.getText().toString());
                String pay_password =etPay.getText().toString();
                if (TextUtils.isEmpty(etCash.getText().toString())){
                    ToastUtil.showToast(WithdrawDdepositActivity.this,"提现金额不能为空");
                    return;
                }
                if (erMemory>memory){
                    ToastUtil.showToast(WithdrawDdepositActivity.this,"提现金额不能大于余额");
                    return;
                }
                if (TextUtils.isEmpty(pay_password)){
                    ToastUtil.showToast(WithdrawDdepositActivity.this,"支付密码不能为空");
                    return;
                }
                submit(helper.getToken(),id,etCash.getText().toString(),pay_password);
                break;
            //银行卡
            case R.id.tv_name:
                if (list==null&&0==list.size()){
                    return;
                }
                final List<String> arrayList=new ArrayList<>();
                for (BankListBean datas:list){
                    arrayList.add(datas.getBank());
                }
                DeclarePopWindow popWindow=new DeclarePopWindow(WithdrawDdepositActivity.this,arrayList,tvName);
                popWindow.setOnPopWindowItemClick(new DeclarePopWindow.setOnPopWindowItemClick() {
                    @Override
                    public void onItem(int pos) {
                        tvName.setText(list.get(pos).getBank());
                        id=list.get(pos).getId();
                    }
                });
                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getBankList(helper.getToken());
    }

    /**
     * 未添加银行卡，是否前往添加
     */
    private void getAddbank() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.add_bank, null);
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
                Intent intent = new Intent(WithdrawDdepositActivity.this, AddBankActivity.class);
                startActivity(intent);
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }
}
