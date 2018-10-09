package com.guo.qlzx.nongji.client.activity;


import com.guo.qlzx.nongji.R;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;

/**
 * 用户端---充值
 */
public class RechargeActivity extends BaseActivity {


    @Override
    public int getContentView() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);

    }

    @Override
    public void getData() {

    }
}
