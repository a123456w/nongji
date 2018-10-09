package com.guo.qlzx.nongji.client.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.guo.qlzx.nongji.R;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;

/**
 * 用户端———解绑
 */
public class UntieActivity extends BaseActivity {


    @Override
    public int getContentView() {
        return R.layout.activity_untie;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);

    }

    @Override
    public void getData() {

    }
}
