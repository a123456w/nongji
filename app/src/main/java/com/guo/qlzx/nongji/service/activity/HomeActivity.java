package com.guo.qlzx.nongji.service.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.guo.qlzx.nongji.R;

import com.guo.qlzx.nongji.client.activity.ShomeActivity;
import com.guo.qlzx.nongji.client.fragment.SHomePageFragment;
import com.guo.qlzx.nongji.service.fragment.HomePageFragment;
import com.guo.qlzx.nongji.service.fragment.LogRecordFragment;
import com.guo.qlzx.nongji.service.fragment.OrderFragment;
import com.guo.qlzx.nongji.service.fragment.TechnicalSupportFragment;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private PreferencesHelper helper;
    @BindView(R.id.rg_bottom_nav)
    RadioGroup rgBottomNav;
    private int page=1;
    private FragmentManager fragmentManager;
    private Fragment f1, f2,f3, f4;
    private double exitTime;
    @Override
    public int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(this);
        hideTitleBar();
        rgBottomNav.setOnCheckedChangeListener(this);
        f1 = new HomePageFragment();//首页
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.main_content, f1).commit();
     helper.saveToken(new PreferencesHelper(HomeActivity.this).getToken());
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

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (checkedId) {
            //首页
            case R.id.rb_home:
                fragmentTransaction.show(f1);
                ((HomePageFragment)f1).getdetermine(new PreferencesHelper(HomeActivity.this).getToken());

                break;
            //日志
            case R.id.rb_logreo:
                if (f2 == null) {
                    f2 = new LogRecordFragment();
                    fragmentTransaction.add(R.id.main_content, f2);
                } else {
                    fragmentTransaction.show(f2);
                }

                break;

            //技术支持
            case R.id.rb_technic:
                if (f3 == null) {
                    f3 = new TechnicalSupportFragment();
                    fragmentTransaction.add(R.id.main_content, f3);
                } else {
                    fragmentTransaction.show(f3);
                    ((TechnicalSupportFragment)f3).getServiceJourDate(new PreferencesHelper(HomeActivity.this).getToken());
                }

                break;
            //订单
            case R.id.rb_order:
                if (f4 == null) {
                    f4 = new OrderFragment();
                    fragmentTransaction.add(R.id.main_content, f4);
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
        if (f1 == null && fragment instanceof HomePageFragment)
            f1 = fragment;
        if (f2 == null && fragment instanceof LogRecordFragment)
            f2 = fragment;
        if (f3 == null && fragment instanceof TechnicalSupportFragment)
            f3= fragment;
        if (f4 == null && fragment instanceof OrderFragment)
            f4 = fragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onReload(View v) {
        getData();
    }

}

