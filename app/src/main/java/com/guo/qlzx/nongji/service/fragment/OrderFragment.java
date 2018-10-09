package com.guo.qlzx.nongji.service.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.service.adapter.OrderFragmentAdapter;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 订单
 */
public class OrderFragment extends BaseFragment {

    @BindView(R.id.ll_hint)
    LinearLayout llHint;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.et_search)
    EditText etText;
    Unbinder unbinder;

    private PreferencesHelper helper;
    private String name="全部";
    private OrderFragmentAdapter adapter;
    private List<BlankFragment> fragments = new ArrayList<>();
    private BlankFragment one ;
    private BlankFragment two;
    private BlankFragment three;
    private List<String> list=new ArrayList<>();
    //检测软键盘内容是否发生改变
    boolean lastTag = false;
    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_order_fragment, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(mContext);
        titleBar.setVisibility(View.GONE);
        initDatas();
        initEvent();
    }

    private void initDatas() {
        //每一个fragment
        one=new BlankFragment();
        two= new BlankFragment();
        three=  new BlankFragment();
        one.setStatus(1,mContext);
        two.setStatus(2,mContext);
        three.setStatus(3, mContext);
        fragments.add(one);
        fragments.add(two);
        fragments.add(three);
        //标题数据
        list.add("全部");
        list.add("进行中");
        list.add("已完成");
        adapter = new OrderFragmentAdapter(getActivity().getSupportFragmentManager(), list, fragments);

        viewPager.setAdapter(adapter);
        /*Tab与ViewPager绑定*/
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(tabSelectedListener);
    }

    private  void initSearch(){
        String  content=etText.getText().toString();
        //搜索
        if ("全部".equals(name)){
            one.setOrderId(content);
        }else if ("进行中".equals(name)){
            two.setOrderId(content);
        }else if ("已完成".equals(name)){
            three.setOrderId(content);
        }
    }
    private  void initSearch(String str){
        //搜索
        if ("全部".equals(name)){
            one.setOrder(str);
        }else if ("进行中".equals(name)){
            two.setOrder(str);
        }else if ("已完成".equals(name)){
            three.setOrder(str);
        }
    }
    //输入时隐藏搜索图标
    private  void initEvent(){
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())){
                    llHint.setVisibility(View.VISIBLE);
                }else{
                    llHint.setVisibility(View.GONE);
                }
                initSearch(s.toString());
                lastTag = true;
            }
        });
        etText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_ENTER){
                    initSearch();
                    lastTag = false;
                }
                return false;
            }
        });
    }
    //监听切换的item   以便搜索的实施
    TabLayout.OnTabSelectedListener tabSelectedListener=new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            name=tab.getText().toString();
            if(lastTag){
                initSearch();
                lastTag = false;
            }
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
