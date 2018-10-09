package com.guo.qlzx.nongji.client.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.adapter.DeclareRecordAdapter;
import com.guo.qlzx.nongji.client.adapter.JobRecordAdapter;
import com.guo.qlzx.nongji.client.bean.DeclareRecordListBean;
import com.guo.qlzx.nongji.client.bean.JobRecordListBean;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogRecordDetailsActivity;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/5/31.
 *  客户端-申报记录
 */

public class DeclareRecordActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener {
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.tv_issue)
    TextView tvIssue;
    private PreferencesHelper helper;
    private int page=1;
    private DeclareRecordAdapter adapter;
    private  List<DeclareRecordListBean> listBeans=new ArrayList<>();
    @Override
    public int getContentView() {
        return R.layout.activity_declare_record;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(this);
        titleBar.setTitleText("申报记录");
        adapter = new DeclareRecordAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(this));
        canContentView.setAdapter(adapter);
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");
        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");
        tvIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManualActivity.startMyActivity(context,"常见故障解决","2");
            }
        });
    }

    @Override
    public void getData() {
        getListData(helper.getToken(),page);
    }
    public void getListData(String token, final int page){
        HttpHelp.getInstance().create(RemoteApi.class).getDeclareRecordData(token,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<DeclareRecordListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<DeclareRecordListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code==0){
                            refresh.refreshComplete();
                            refresh.loadMoreComplete();
                            listBeans=listBaseBean.data;
                            if (page == 1) {
                                if (listBeans != null && listBeans.size() > 0) {
                                    adapter.setData(listBeans);
                                    tvIssue.setVisibility(View.VISIBLE);
                                } else {
                                    loadingLayout.setStatus(LoadingLayout.Empty);
                                }
                            } else {
                                if (listBeans != null && listBeans.size() > 0) {
                                    adapter.addMoreData(listBeans);
                                    tvIssue.setVisibility(View.GONE);
                                } else {
                                    ToastUtil.showToast(DeclareRecordActivity.this,getString(R.string.no_more));
                                }
                            }
                        }
                        else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(DeclareRecordActivity.this);
                        }else {
                            ToastUtil.showToast(DeclareRecordActivity.this,listBaseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();
                    }
                });
    }
    @Override
    public void onLoadMore() {
        page++;
        getListData(helper.getToken(),page);
    }

    @Override
    public void onRefresh() {
        page=1;
        getListData(helper.getToken(),page);
    }
}
