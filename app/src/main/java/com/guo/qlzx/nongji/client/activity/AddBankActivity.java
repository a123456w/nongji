package com.guo.qlzx.nongji.client.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.AddBanksBean;
import com.guo.qlzx.nongji.client.bean.BankNameBean;
import com.guo.qlzx.nongji.client.event.WalletEvent;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/6/4.
 * 添加银行卡
 */

public class AddBankActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_pw)
    EditText etPw;
    @BindView(R.id.ed_identity)
    EditText edIdentity;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.ln_identity)
    LinearLayout lnIdentity;

    private PreferencesHelper helper;
    public static Activity instance;

    @Override
    public int getContentView() {
        return R.layout.activity_add_bank;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("添加银行卡");
        EventBusUtil.register(this);
        helper = new PreferencesHelper(this);
        instance = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    /**
     * 重新请求是否设置过密码
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateWalletEvent(WalletEvent event) {
        getUserName(helper.getToken());
    }

    @Override
    public void getData() {
        getUserName(helper.getToken());
    }

    //获取用户名字
    private void getUserName(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getUserName(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<AddBanksBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<AddBanksBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            //是否实名
                            String is_realname = baseBean.data.getIs_realname();
                            if (is_realname.equals("1")) {
                                etName.setText(baseBean.data.getName());
                                lnIdentity.setVisibility(View.GONE);
                            }else if (is_realname.equals("2")){

                                ToastUtil.showToast(context,"请填写信息");
                            }
                        }  else if (baseBean.code == 2) {
                            getPassword();
                        } else if (baseBean.code == 4) {
                            ToolUtil.getOutLogs(AddBankActivity.this);
                        } else {
                            ToastUtil.showToast(AddBankActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
    private void getBankTypeIdentity(String token, String bank_number, String pay_password,String name,String id_number){
        HttpHelp.getInstance().create(RemoteApi.class).getBankTypeIdentity(token, bank_number, pay_password,name,id_number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<BankNameBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<BankNameBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            Intent intent = new Intent(AddBankActivity.this, FinishBankActivity.class);
                            intent.putExtra("TYPE", baseBean.data.getType());
                            intent.putExtra("BANKCARD", baseBean.data.getBank_number());
                            startActivity(intent);
                        } else if (baseBean.code == 4) {
                            ToolUtil.getOutLogs(AddBankActivity.this);
                        } else {
                            ToastUtil.showToast(AddBankActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
    //获取银行卡类型
    private void getBankType(String token, String bank_number, String pay_password) {
        HttpHelp.getInstance().create(RemoteApi.class).getBankType(token, bank_number, pay_password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<BankNameBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<BankNameBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            Intent intent = new Intent(AddBankActivity.this, FinishBankActivity.class);
                            intent.putExtra("TYPE", baseBean.data.getType());
                            intent.putExtra("BANKCARD", baseBean.data.getBank_number());
                            startActivity(intent);
                        } else if (baseBean.code == 4) {
                            ToolUtil.getOutLogs(AddBankActivity.this);
                        } else {
                            ToastUtil.showToast(AddBankActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                String identiy = edIdentity.getText().toString();
                String name = etName.getText().toString();
                String number = etNumber.getText().toString();
                String password = etPw.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showToast(AddBankActivity.this, "姓名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(number)) {
                    ToastUtil.showToast(AddBankActivity.this, "银行卡号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showToast(AddBankActivity.this, "交易密码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(identiy)&&lnIdentity.getVisibility() == View.VISIBLE) {
                    ToastUtil.showToast(AddBankActivity.this, "身份证号不能为空");
                    return;
                }
                if(lnIdentity.getVisibility() == View.GONE)
                getBankType(helper.getToken(), number, password);
                else getBankTypeIdentity(helper.getToken(), number, password,name,identiy);
                break;
        }
    }

    /**
     * 未未进行实名认证，请联系客服进行实名认证？
     */
    private void getAuthentication() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.authen, null);
        Button imageView = contentView.findViewById(R.id.but_cans);
        final Button com = contentView.findViewById(R.id.but_coms);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }

    /**
     * 未设置交易密码，是否前往设置？
     */
    private void getPassword() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.password, null);
        Button imageView = contentView.findViewById(R.id.but_cans);
        final Button com = contentView.findViewById(R.id.but_coms);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                finish();
            }
        });
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                Intent intent = new Intent(AddBankActivity.this, ChangePayPasswordWordActivity.class);
                startActivity(intent);
                bottomDialog.dismiss();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
