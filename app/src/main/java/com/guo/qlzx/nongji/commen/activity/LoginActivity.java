package com.guo.qlzx.nongji.commen.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guo.qlzx.nongji.BuildConfig;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.activity.ChangePayPasswordWordActivity;
import com.guo.qlzx.nongji.client.activity.SPersonalCentercActivity;
import com.guo.qlzx.nongji.client.activity.ShomeActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.service.activity.HomeActivity;
import com.guo.qlzx.nongji.service.activity.PersonalCenterActivity;
import com.guo.qlzx.nongji.service.bean.LogBean;
import com.guo.qlzx.nongji.service.bean.PartyLogBean;
import com.guo.qlzx.nongji.service.bean.SendBean;
import com.guo.qlzx.nongji.service.costom.LogInFailureDialog;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.collector.ActivityCollector;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

/**
 * 登录类
 * Created by dillon on 2018/5/22.
 */

public class LoginActivity extends BaseActivity {
    //获取id

    @BindView(R.id.et_tell)
    EditText etTell;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_forpwd)
    TextView tvForpwd;
    @BindView(R.id.tv_log)
    TextView tvLog;
    @BindView(R.id.tv_reg)
    TextView tvReg;
    @BindView(R.id.im_weixin)
    ImageView imWeixin;
    @BindView(R.id.im_qq)
    ImageView imQq;
    @BindView(R.id.iv_logo)
    ImageView iv_logo;
    private PreferencesHelper helper;
    public static Activity activity;
    private double exitTime;
    private UMAuthListener authListener;
    private String uid;
    private String role;

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(this);
        hideTitleBar();
        tvForpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.this, ForgetPasswordActivity.class);
            }
        });
        activity = this;
        if (getIntent().hasExtra("tag")) {
            final LogInFailureDialog dialog = new LogInFailureDialog(activity);
            dialog.setOnConfirmClickListener(new LogInFailureDialog.onConfirmClickListener() {
                @Override
                public void onConfirm() {
                    dialog.dismiss();
                    //((Activity)activity).finish();
                }
            });
            dialog.show();
        }
        if(BuildConfig.DEBUG){
            iv_logo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final Dialog bottomDialog = new Dialog(LoginActivity.this, R.style.BottomDialog);
                    View contentView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_write_commit, null);
                    Button imageView = contentView.findViewById(R.id.but_ca);
                    TextView tv_sn = contentView.findViewById(R.id.tv_sn);
                    tv_sn.setText("当前服务器为"+(Constants.type?"正式":"测试")+",确认切换？");
                    final Button com = contentView.findViewById(R.id.but_co);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomDialog.cancel();
                        }
                    });
                    com.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Constants.type = Constants.type?false:true;
                            bottomDialog.cancel();
                        }
                    });
                    bottomDialog.setContentView(contentView);
                    ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
                    contentView.setLayoutParams(layoutParams);
                    bottomDialog.show();
                    return true;
                }
            });
        }
    }

    @Override
    public void getData() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_LOGS)
                            != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                            != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.SET_DEBUG_APP)
                            != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)
                            != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)
                            != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_APN_SETTINGS)
                            != PackageManager.PERMISSION_GRANTED) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_LOGS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.SET_DEBUG_APP,
                        Manifest.permission.SYSTEM_ALERT_WINDOW,
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(this, mPermissionList, 123);
            }
        }
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(LoginActivity.this).setShareConfig(config);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

    }

    /**
     * qq登录
     */
    public void loginQQ() {

        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */ /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */ /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */ /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */

        authListener = new UMAuthListener() {
            /**
             * @desc 授权开始的回调
             * @param platform 平台名称
             */
            @Override
            public void onStart(SHARE_MEDIA platform) {

            }

            /**
             * @desc 授权成功的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param data 用户资料返回
             */
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                uid = data.get("uid");
                loadingLayout.setStatus(LoadingLayout.Loading);
                getLog(uid, 1);
            }

            /**
             * @desc 授权失败的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param t 错误原因
             */
            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {

                Toast.makeText(mContext, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }

            /**
             * @desc 授权取消的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             */
            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                Toast.makeText(mContext, "已取消", Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * 微信登录
     */
    public void loginWx() {

        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */ /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */ /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */ /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */

        authListener = new UMAuthListener() {
            /**
             * @desc 授权开始的回调
             * @param platform 平台名称
             */
            @Override
            public void onStart(SHARE_MEDIA platform) {

            }

            /**
             * @desc 授权成功的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param data 用户资料返回
             */
            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                uid = data.get("uid");
                loadingLayout.setStatus(LoadingLayout.Loading);
                getLogW(uid, 2);
            }

            /**
             * @desc 授权失败的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             * @param t 错误原因
             */
            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {

                Toast.makeText(mContext, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }

            /**
             * @desc 授权取消的回调
             * @param platform 平台名称
             * @param action 行为序号，开发者用不上
             */
            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                Toast.makeText(mContext, "已取消", Toast.LENGTH_LONG).show();
            }
        };
    }

    /**
     * 登录
     */
    @OnClick(R.id.tv_log)
    public void onViewClicked() {
        String tell = etTell.getText().toString();
        String pwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(tell)) {
            ToastUtil.showToast(LoginActivity.this, "手机号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.showToast(LoginActivity.this, "密码不能为空");
            return;
        }
        //调取登录方法
        getLogin(tell, pwd, "11");
    }

    /**
     * 注册
     */
    @OnClick(R.id.tv_reg)
    public void onViewClicked1() {
        //跳入注册页面
        startActivity(new Intent(this, RegActivity.class));
    }

    /**
     * 第三方微信登录
     */
    @OnClick(R.id.im_weixin)
    public void onViewClicked2() {
        loginWx();
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
    }

    /**
     * 第三方QQ登录
     */
    @OnClick(R.id.im_qq)
    public void onViewClicked3() {
        loginQQ();
        UMShareAPI.get(this).getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, authListener);
    }

    /**
     * 登录方法
     */
    public void getLogin(String moblie, String password, String registration_id) {
        tvLog.setClickable(false);
        HttpHelp.getInstance().create(RemoteApi.class).Login(moblie, password, registration_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<LogBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<LogBean> listBaseBean) {
                        tvLog.setClickable(true);
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            String token = listBaseBean.data.getToken();
                            helper.saveToken(token);
                            role = listBaseBean.data.getRole();
                            helper.saveUserRole(role);
                            if (role.equals("4")||role.equals("5")) {
                                if (role.equals("5")) {
                                    EventBusUtil.post(new SendBean());
                                    ToastUtil.showToast(LoginActivity.this, "管理人员登录");
                                }
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                //ShomeActivity.startActivityWithParmeter(LoginActivity.this,role);
                                startActivity(new Intent(LoginActivity.this, ShomeActivity.class));
                                finish();

                            }
//                            if (role.equals("5")) {
//
//                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                                finish();
//                            }
                        } else if (listBaseBean.code == 1) {
                            ToastUtil.showToast(LoginActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        tvLog.setClickable(true);
                        super.onError(throwable);
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAll();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 第三方qq登录方法
     */
    public void getLog(String openid, int type) {
        HttpHelp.getInstance().create(RemoteApi.class).getLoginPar(openid, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<PartyLogBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<PartyLogBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        loadingLayout.setStatus(LoadingLayout.Success);
                        if (listBaseBean.code == 0) {
                            role = listBaseBean.data.getRole();
                            helper.saveUserRole(role);
                            if (role.equals("4")||role.equals("5")) {
                                if (role.equals("5")) {
                                    EventBusUtil.post(new SendBean());
                                    ToastUtil.showToast(LoginActivity.this, "管理人员登录");
                                }
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                //ShomeActivity.startActivityWithParmeter(LoginActivity.this,role);
                                startActivity(new Intent(LoginActivity.this, ShomeActivity.class));
                                finish();

                            }
                        } else if (listBaseBean.code == 1) {
                            ToastUtil.showToast(LoginActivity.this, listBaseBean.message);
                        } else if (listBaseBean.code == 2) {
                            Intent intent = new Intent(LoginActivity.this, PartyLoginActivity.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("openid", uid);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        loadingLayout.setStatus(LoadingLayout.Success);
                    }
                });
    }

    /**
     * 第三方微信登录方法
     */
    public void getLogW(String openid, int type) {
        HttpHelp.getInstance().create(RemoteApi.class).getLoginPar(openid, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<PartyLogBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<PartyLogBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        loadingLayout.setStatus(LoadingLayout.Success);
                        if (listBaseBean.code == 0) {
                            role = listBaseBean.data.getRole();
                            helper.saveUserRole(role);
                            if (role.equals("4")||role.equals("5")) {
                                if (role.equals("5")) {
                                    EventBusUtil.post(new SendBean());
                                    ToastUtil.showToast(LoginActivity.this, "管理人员登录");
                                }
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                //ShomeActivity.startActivityWithParmeter(LoginActivity.this,role);
                                startActivity(new Intent(LoginActivity.this, ShomeActivity.class));
                                finish();

                            }
                        } else if (listBaseBean.code == 1) {
                            ToastUtil.showToast(LoginActivity.this,listBaseBean.message );
                        } else if (listBaseBean.code == 2) {
                            Intent intent = new Intent(LoginActivity.this, PartyLoginActivity.class);
                            intent.putExtra("type", "2");
                            intent.putExtra("openid", uid);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        loadingLayout.setStatus(LoadingLayout.Success);
                        super.onError(throwable);
                    }
                });
    }
}