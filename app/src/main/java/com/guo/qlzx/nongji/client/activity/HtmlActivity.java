package com.guo.qlzx.nongji.client.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/6/1.
 */

public class HtmlActivity extends BaseActivity {
    @BindView(R.id.tv_content)
    TextView tvContent;

    private static String mTitle="";
    @Override
    public int getContentView() {
        return R.layout.activity_html;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText(mTitle);
    }

    @Override
    public void getData() {

    }
    public static void startActivity(Context context,String title){
        context.startActivity(new Intent(context,HtmlActivity.class));
        mTitle=title;
    }
}
