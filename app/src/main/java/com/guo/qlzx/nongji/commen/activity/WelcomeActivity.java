package com.guo.qlzx.nongji.commen.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.activity.ShomeActivity;
import com.guo.qlzx.nongji.commen.application.MyApplication;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.service.activity.HomeActivity;
import com.guo.qlzx.nongji.service.bean.SendBean;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.logging.Handler;

import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/6/13.
 */

public class WelcomeActivity extends AppCompatActivity {
    private PreferencesHelper preferencesHelper;
    private Handler mHandler;
    private PreferencesHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        helper = new PreferencesHelper(this);
        preferencesHelper = new PreferencesHelper(this);
        // requestPermission();
        if (!AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            requestPermission();
            return;
        }
        if (preferencesHelper.isFirstTime()) {
            gotoGuide();
        } else {
            gotoMain();
        }
    }

    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            AlertDialog.newBuilder(WelcomeActivity.this)
                    .setTitle("友好提醒")
                    .setMessage("为保证APP的正常运行，需要以下权限:\n1.访问SD卡（选择图片等功能）\n2.调用摄像头（扫码下单等功能）")
                    .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rationale.resume();
                            if (preferencesHelper.isFirstTime()) {
                                gotoGuide();
                            } else {
                                gotoMain();
                            }
                        }
                    })
                    .setNegativeButton("依然拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rationale.cancel();
                        }
                    }).show();
        }
    };

    private void requestPermission() {
        if (AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)) {
            // 有权限，直接do anything.
        } else
            // 申请多个权限。
            AndPermission.with(this)
                    .requestCode(REQUIRES_PERMISSION)
                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
                    .callback(permissionListener)
                    // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                    // 这样避免用户勾选不再提示，导致以后无法申请权限。
                    // 你也可以不设置。
                    .rationale(rationaleListener)
                    .start();
    }

    private final int REQUIRES_PERMISSION = 0;
    private static final int REQUEST_CODE_SETTING = 300;


    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantPermissions) {
            LogUtil.d("权限申请成功  onSucceed");
            switch (requestCode) {

                case REQUIRES_PERMISSION: {
                    if (preferencesHelper.isFirstTime()) {
                        gotoGuide();
                    } else {
                        gotoMain();
                    }
                }
            }
        }


        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            switch (requestCode) {

                case REQUIRES_PERMISSION: {
                    // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                    if (AndPermission.hasAlwaysDeniedPermission(WelcomeActivity.this, deniedPermissions)) {
                        // 第一种：用默认的提示语。
//                AndPermission.defaultSettingDialog(ListenerActivity.this, REQUEST_CODE_SETTING).show();

                        // 第二种：用自定义的提示语。
                        AndPermission.defaultSettingDialog(WelcomeActivity.this, REQUEST_CODE_SETTING)
                                .setTitle("权限申请失败")
                                .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                                .setPositiveButton("好，去设置")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtil.showToast(WelcomeActivity.this, "权限申请失败，无法进入app");
                                        finish();
                                    }
                                })
                                .show();
                    } else {
                        ToastUtil.showToast(WelcomeActivity.this, "权限申请失败，无法进入app");
                        finish();
                    }

                    break;
                }
            }


        }
    };


    private void gotoMain() {
        MyApplication.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String userRole = helper.getUserRole();
                if (TextUtils.isEmpty(preferencesHelper.getToken())) {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if (userRole.equals("4")||userRole.equals("5")) {
                    if (userRole.equals("5")) {
                        EventBusUtil.post(new SendBean());
                        ToastUtil.showToast(WelcomeActivity.this, "管理人员登录");
                    }
                    startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(WelcomeActivity.this, ShomeActivity.class));
                    finish();
                }
            }
        }, 3000);
    }

    private void gotoGuide() {
        MyApplication.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
