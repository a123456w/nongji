package com.guo.qlzx.nongji.service.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.activity.DeclareRecordActivity;
import com.guo.qlzx.nongji.client.adapter.DeclareRecordAdapter;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.adapter.MaintainingAdapter;
import com.guo.qlzx.nongji.service.bean.MaintainingBean;
import com.guo.qlzx.nongji.service.fragment.TechnicalSupportFragment;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 维护保养
 */
public class MaintainingActivity extends BaseActivity implements OnRVItemClickListener ,CanRefreshLayout.OnRefreshListener/*,CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener*/{


    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.ll_none)
    LinearLayout llNone;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    private List<MaintainingBean> data1 = new ArrayList<>();
    private MaintainingAdapter adapter;
    private int page=1;

    @Override
    public int getContentView() {
        return R.layout.activity_maintaining;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);

        titleBar.setTitleText("维护保养");
        //返回点击事件
        titleBar.setLeftTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MaintainingActivity.this, TechnicalSupportFragment.class);
            }
        });
        adapter = new MaintainingAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(this));
        canContentView.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);
        canContentView.addItemDecoration(new RecycleViewDivider(this));
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");
    }

    @Override
    public void getData() {
        //调用维护保养列表方法
        getMaintainList(new PreferencesHelper(this).getToken(), page);
    }

    /**
     * 维护保养列表方法
     */
    private void getMaintainList(String token, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getmaintain(token, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<MaintainingBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<MaintainingBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        canContentView.setVisibility(View.VISIBLE);
                        llNone.setVisibility(View.GONE);
                        refresh.refreshComplete();
                        if (listBaseBean.code == 0) {
                            data1 = listBaseBean.data;
                            if (data1 != null && data1.size() > 0) {
                                adapter.setData(data1);
                            } else {
                                //加载图片
                                canContentView.setVisibility(View.GONE);
                                llNone.setVisibility(View.VISIBLE);
                            }
                        }
                        else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(MaintainingActivity.this);
                        }else {
                            ToastUtil.showToast(MaintainingActivity.this,listBaseBean.message);
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    // 点击子条目跳转
    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(MaintainingActivity.this, MaintenanceDetailsActivity.class);
        intent.putExtra("ORDERID", adapter.getItem(position).getMachine_id());
        intent.putExtra("ORDERID1", adapter.getItem(position).getName());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        getMaintainList(new PreferencesHelper(this).getToken(), page);
    }
}
