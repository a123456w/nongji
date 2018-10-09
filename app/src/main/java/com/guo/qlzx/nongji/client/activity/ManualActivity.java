package com.guo.qlzx.nongji.client.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.adapter.ManualListAdapter;
import com.guo.qlzx.nongji.client.bean.ArticleListBean;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.service.activity.RechargeRecordActivity;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ManualActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener {
    @BindView(R.id.can_content_view)
    RecyclerView can_content_view;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    private String title;
    private String type;
    Unbinder unbinder;
    private int page = 1;

    @Override
    public int getContentView() {
        return R.layout.activity_manual;
    }

    @Override
    public void initView() {
        unbinder = ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        title = getIntent().getStringExtra("title");
        titleBar.setTitleText(title);
        type = getIntent().getStringExtra("type");
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
    }

    @Override
    public void getData() {
        getArticleListData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public static void startMyActivity(Context context, String title, String type) {
        Intent intent = new Intent(context,ManualActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        context.startActivity(intent);
        //context = null;
    }

    /*
     * 文章列表接口
     * */
    private void getArticleListData() {
        final ManualListAdapter adapter = new ManualListAdapter(can_content_view);
        can_content_view.setFocusable(false);
        can_content_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //添加Android自带的分割线
        can_content_view.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        can_content_view.setAdapter(adapter);
        HttpHelp.getInstance().create(RemoteApi.class).articleList(type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ArticleListBean>>>(context, null) {
                    @Override
                    public void onNext(final BaseBean<List<ArticleListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if(refresh != null){
                            refresh.refreshComplete();
                            refresh.loadMoreComplete();
                        }

                        if (listBaseBean.code == 0) {
                            if (page == 1) {
                                if (listBaseBean.data != null && listBaseBean.data.size() > 0) {
                                    adapter.setData(listBaseBean.data);
                                } else {
                                    loadingLayout.setStatus(LoadingLayout.Empty);
                                }
                            } else {
                                if (listBaseBean.data != null && listBaseBean.data.size() > 0) {
                                    adapter.addMoreData(listBaseBean.data);
                                } else {
                                    ToastUtil.showToast(ManualActivity.this,getString(R.string.no_more));
                                }
                            }
                            adapter.setData(listBaseBean.data);
                            adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(ViewGroup parent, View childView, int position) {
                                    WebActivity.startActivity(context, title, listBaseBean.data.get(position).getUrl());
                                }
                            });
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if(refresh != null){
                            refresh.refreshComplete();
                            refresh.loadMoreComplete();
                        }
                    }
                });


    }


    @Override
    public void onLoadMore() {
        page++;
        getData();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }

}
