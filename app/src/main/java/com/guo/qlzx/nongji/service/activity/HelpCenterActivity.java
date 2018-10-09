package com.guo.qlzx.nongji.service.activity;


import com.guo.qlzx.nongji.R;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.ButterKnife;

/**
 * 帮助中心
 */
public class HelpCenterActivity extends BaseActivity {


    @Override
    public int getContentView() {
        return R.layout.activity_help_center;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("帮助中心");
    }

    @Override
    public void getData() {

    }
}
