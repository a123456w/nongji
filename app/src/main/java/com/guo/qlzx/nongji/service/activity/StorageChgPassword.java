package com.guo.qlzx.nongji.service.activity;



import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.activity.ForgetPasswordActivity;
import com.guo.qlzx.nongji.commen.activity.LoginActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.bean.ChangepwdBean;
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
 * 修改密码类
 */
public class StorageChgPassword extends BaseActivity {

    //获取控件id
    @BindView(R.id.et_originalpwd)
    EditText etOriginalpwd;
    @BindView(R.id.et_newpwd)
    EditText etNewpwd;
    @BindView(R.id.et_againpwd)
    EditText etAgainpwd;
    @BindView(R.id.tv_affirm)
    TextView tvAffirm;
    private PreferencesHelper helper;

    @Override
    public int getContentView() {
        return R.layout.activity_storage_chg_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(this);
        titleBar.setTitleText("修改密码");
    }

    @Override
    public void getData() {

    }
    /**
     * 修改密码确认
     */
    @OnClick(R.id.tv_affirm)
    public void onViewClicked2() {
        String originalpwd = etOriginalpwd.getText().toString();
        String newpwd = etNewpwd.getText().toString();
        String againpwd = etAgainpwd.getText().toString();
        if (TextUtils.isEmpty(originalpwd)){
            ToastUtil.showToast(StorageChgPassword.this,"请输入原密码");
            return;
        }
        if (TextUtils.isEmpty(newpwd)){
            ToastUtil.showToast(StorageChgPassword.this,"新密码密码不能为空！");
            return;
        }
        if (newpwd.length()<8||newpwd.length()>16){
            ToastUtil.showToast(StorageChgPassword.this,"密码长度是8-16位！");
            return;
        }
        if (TextUtils.isEmpty(againpwd)){
            ToastUtil.showToast(StorageChgPassword.this,"密码不能为空！");
            return;
        }
        String token = helper.getToken();
        //调用修改密码方法
        getchangepwd(token,originalpwd,newpwd,againpwd);

    }

    /**
     *
     *修改密码方法
     */
    private void getchangepwd(String token,String old_password,String new_password,String new_password2) {
        HttpHelp.getInstance().create(RemoteApi.class).changepassword(token,old_password,new_password,new_password2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code==0){

                          ToastUtil.showToast(StorageChgPassword.this,"密码修改成功");
                          startActivity(StorageChgPassword.this, LoginActivity.class);
                        }else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(StorageChgPassword.this);
                        }else {
                            ToastUtil.showToast(StorageChgPassword.this,listBaseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


}
