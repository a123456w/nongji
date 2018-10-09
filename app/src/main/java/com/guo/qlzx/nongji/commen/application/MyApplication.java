package com.guo.qlzx.nongji.commen.application;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.qlzx.mylibrary.util.LogUtil;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

/**
 * Created by 李 on 2018/6/13.
 */

public class MyApplication  extends MultiDexApplication {
    private static Handler mainHandler;//全局的handler
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        //初始化mainHandler
        mainHandler = new Handler();


        //初始化mainHandler
        mainHandler = new Handler();


        UMConfigure.init(this,"5b2095b8f43e484b4b000068"
                ,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0

        PlatformConfig.setWeixin("wxd83c038a76b70839", "0424ca01526f8fb6b257e1d681b085eb");
        PlatformConfig.setQQZone("100424468", "KEYgh8OtxT1oym3qNNo");
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public  int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.e("!!!!!!!!!!",""+versionCode);
        return versionCode;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        SophixManager.getInstance().setContext(this)
                .setAppVersion(String.valueOf(getVersionCode(this)))
                // .setAesKey(null)
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        Log.e("11111111111111111","++++++  +++"+code);
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
        MultiDex.install(this);

    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getMainHandler() {
        return mainHandler;
    }
}
