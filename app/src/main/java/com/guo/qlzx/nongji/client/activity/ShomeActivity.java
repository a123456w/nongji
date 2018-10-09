package com.guo.qlzx.nongji.client.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.fragment.SFailureDeclareFragment;
import com.guo.qlzx.nongji.client.fragment.SHomePageFragment;
import com.guo.qlzx.nongji.client.fragment.SLogRecordFragment;
import com.guo.qlzx.nongji.client.fragment.STechnicalSupportFragment;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 客户端  主页面
 */
public class ShomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private PreferencesHelper helper;
    @BindView(R.id.rg_sbottom_nav)
    RadioGroup rgsBottomNav;
    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private FragmentManager fragmentManager;
    private Fragment f1, f2, f3, f4;
    private double exitTime;
    private int page=1;
    @Override
    public int getContentView() {
        return R.layout.activity_shome;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(this);
        hideTitleBar();
        rgsBottomNav.setOnCheckedChangeListener(this);
        f1 = new SHomePageFragment();//首页
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.main_scontent, f1).commit();
    }

    /**
     * 隐藏所有Fragment
     *
     * @param transaction
     */
    public void hideAllFragment(FragmentTransaction transaction) {
        if (f1 != null) {
            transaction.hide(f1);
        }
        if (f2 != null) {
            transaction.hide(f2);
        }
        if (f3 != null) {
            transaction.hide(f3);
        }
        if (f4 != null) {
            transaction.hide(f4);
        }
    }

    @Override
    public void getData() {

    }

/*    *//**
     * 传递角色到这个页面
     * @param context
     * @param roles
     *//*
    public static void startActivityWithParmeter(Context context,String roles){
        Intent intent = new Intent(context,ShomeActivity.class);
        intent.putExtra("SHOMEACTIVITYROLE",roles);
        context.startActivity(intent);
    }

    *//**
     * 获取角色
     * @return
     *//*
    public String getRoles(){
        String str = getIntent().getStringExtra("SHOMEACTIVITYROLE");
        return str;
    }*/

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (checkedId) {
            //首页
            case R.id.rb_shome:
                fragmentTransaction.show(f1);
                ((SHomePageFragment)f1).getdetermine(new PreferencesHelper(ShomeActivity.this).getToken(),page);
                break;
            //日志
            case R.id.rb_slogreo:
                if (f2 == null) {
                    f2 = new SLogRecordFragment();
                    fragmentTransaction.add(R.id.main_scontent, f2);
                } else {
                    fragmentTransaction.show(f2);
                }

                break;

            //技术支持
            case R.id.rb_stechnic:
                if (f3 == null) {
                    f3 = new STechnicalSupportFragment();
                    fragmentTransaction.add(R.id.main_scontent, f3);
                } else {
                    fragmentTransaction.show(f3);
                }

                break;
            //订单
            case R.id.rb_sorder:
                if (f4 == null) {
                    f4 = new SFailureDeclareFragment();
                    fragmentTransaction.add(R.id.main_scontent, f4);
                } else {
                    fragmentTransaction.show(f4);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (f1 == null && fragment instanceof SHomePageFragment)
            f1 = fragment;
        if (f2 == null && fragment instanceof SLogRecordFragment)
            f2 = fragment;
        if (f3 == null && fragment instanceof STechnicalSupportFragment)
            f3 = fragment;
        if (f4 == null && fragment instanceof SFailureDeclareFragment)
            f4 = fragment;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照回调
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (null != f4 && f4.isVisible()) {
                f4.onActivityResult(requestCode, resultCode, data);
            }
        }


    }

    @Override
    public void onReload(View v) {
        getData();
    }

}


