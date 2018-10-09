package com.guo.qlzx.nongji.client.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.activity.ManualActivity;
import com.guo.qlzx.nongji.client.activity.SPersonalCentercActivity;
import com.guo.qlzx.nongji.client.activity.WebActivity;
import com.guo.qlzx.nongji.commen.activity.FeedbackActivity;
import com.guo.qlzx.nongji.service.activity.MaintainingActivity;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 客户端技术支持
 */
public class STechnicalSupportFragment extends BaseFragment {

    @BindView(R.id.tv_tending)
    LinearLayout tvTending;
    @BindView(R.id.ln_usermanual)
    LinearLayout lnUsermanual;
    @BindView(R.id.ln_maintenance)
    LinearLayout lnMaintenance;
    @BindView(R.id.ln_faq)
    LinearLayout lnFaq;
    @BindView(R.id.ln_feedback)
    LinearLayout lnFeedback;

    private PreferencesHelper helper;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_stechnical_support_fragment, frameLayout, false);
        ButterKnife.bind(this, view);
        //点击事件方法
        getOnclick();
        return view;
    }

    //点击事件
    private void getOnclick() {
        /**
         * 维护保养
         */
        tvTending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MaintainingActivity.class));
            }
        });
        /**
         * 用户手册
         */
        lnUsermanual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManualActivity.startMyActivity(getActivity(),"用户手册","4");

            }
        });
        /**
         * 保养说明
         */
        lnMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WebActivity.startActivity(mContext, "保养说明", Constants.HOST + "Article/article/type/5");
                ManualActivity.startMyActivity(getActivity(),"保养说明","5");

            }
        });
        /**
         * 常见问题
         */
        lnFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WebActivity.startActivity(mContext, "常见问题", Constants.HOST + "Article/article/type/6");
                ManualActivity.startMyActivity(getActivity(),"常见问题","6");
            }
        });
        /**
         * 意见反馈
         */
        lnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(mContext, FeedbackActivity.class));

            }
        });
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(getActivity());
        titleBar.setTitleText("技术支持");
        titleBar.setLeftImageRes(R.drawable.ic_geren);
        titleBar.setLeftImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SPersonalCentercActivity.class));
            }
        });

    }

    @Override
    public void getData() {

    }

}
