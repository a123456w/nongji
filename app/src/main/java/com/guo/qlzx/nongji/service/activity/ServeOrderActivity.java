package com.guo.qlzx.nongji.service.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.service.adapter.OrderFragmentAdapter;
import com.guo.qlzx.nongji.service.fragment.BlankFragment;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ServeOrderActivity extends BaseActivity {



    @BindView(R.id.ll_hint)
    LinearLayout llHint;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.et_search)
    EditText etText;
    Unbinder unbinder;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private PreferencesHelper helper;
    private String name = "全部";
    private OrderFragmentAdapter adapter;
    private List<BlankFragment> fragments = new ArrayList<>();
    private BlankFragment one;
    private BlankFragment two;
    private BlankFragment three;
    private List<String> list = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_order_fragment;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(this);
        titleBar.setVisibility(View.GONE);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initDatas();
        initEvent();
    }

    private void initDatas() {
        //每一个fragment
        one = new BlankFragment(1, this);
        two = new BlankFragment(2, this);
        three = new BlankFragment(3, this);
        one.setStatus(1, this);
        two.setStatus(2, this);
        three.setStatus(3, this);
        fragments.add(one);
        fragments.add(two);
        fragments.add(three);
        //标题数据
        list.add("全部");
        list.add("进行中");
        list.add("已完成");
        adapter = new OrderFragmentAdapter(this.getSupportFragmentManager(), list, fragments);

        viewPager.setAdapter(adapter);
        /*Tab与ViewPager绑定*/
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
    }

    private void initSearch() {
        String content = etText.getText().toString();
        //搜索
        if ("全部".equals(name)) {
            one.setOrderId(content);
        } else if ("进行中".equals(name)) {
            two.setOrderId(content);
        } else if ("已完成".equals(name)) {
            three.setOrderId(content);
        }
    }

    //输入时隐藏搜索图标
    private void initEvent() {
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    llHint.setVisibility(View.VISIBLE);
                } else {
                    llHint.setVisibility(View.GONE);
                }
            }
        });
        etText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    initSearch();
                }
                return false;
            }
        });
    }

    //监听切换的item   以便搜索的实施
    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            name = tab.getText().toString();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    @Override
    public void getData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
