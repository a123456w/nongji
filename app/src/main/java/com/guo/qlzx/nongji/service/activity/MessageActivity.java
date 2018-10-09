package com.guo.qlzx.nongji.service.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.activity.LoginActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.adapter.ManageMentAdapter;
import com.guo.qlzx.nongji.service.adapter.MessageAdapter;
import com.guo.qlzx.nongji.service.bean.MessageDetailsBean;
import com.guo.qlzx.nongji.service.bean.MessageListBean;
import com.guo.qlzx.nongji.service.bean.logOutBean;
import com.guo.qlzx.nongji.service.costom.SwipeItemLayout;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
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
 * Created by 李 on 2018/5/29.
 * 消息
 */

public class MessageActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener, OnItemChildClickListener {
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    private int page=1;

    private MessageAdapter adapter;
    private List<MessageListBean> listBeans=new ArrayList<>();
    @Override
    public int getContentView() {
        return R.layout.activity_message;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);

        titleBar.setTitleText("消息");
        adapter = new MessageAdapter(canContentView);
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
        adapter.setOnItemChildClickListener(this);
        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

        canContentView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
    }

    @Override
    public void getData() {
        getdetermine(new PreferencesHelper(MessageActivity.this).getToken(),page);
    }
    public void getdetermine(String token, final int page){
        HttpHelp.getInstance().create(RemoteApi.class).getMessageData(token,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<MessageListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<MessageListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code==0){
                            refresh.refreshComplete();
                            refresh.loadMoreComplete();
                            listBeans=listBaseBean.data;
                            if (page == 1) {
                                if (listBeans != null && listBeans.size() > 0) {
                                    adapter.setData(listBeans);
                                } else {
                                    loadingLayout.setStatus(LoadingLayout.Empty);
                                }
                            } else {
                                if (listBeans != null && listBeans.size() > 0) {
                                    adapter.addMoreData(listBeans);
                                } else {
                                    ToastUtil.showToast(MessageActivity.this,getString(R.string.no_more));
                                }
                            }
                        }else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(MessageActivity.this);
                        }else {
                            ToastUtil.showToast(MessageActivity.this,listBaseBean.message);
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
    public void deleteMessage(String token, String id){
        HttpHelp.getInstance().create(RemoteApi.class).deleteMessage(token,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code==0){
                            ToastUtil.showToast(MessageActivity.this,listBaseBean.message);
                            getdetermine(new PreferencesHelper(MessageActivity.this).getToken(),page);
                        }else {
                            ToastUtil.showToast(MessageActivity.this,listBaseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(MessageActivity.this,"消息连接失败");
                    }
                });
    }
    @Override
    public void onLoadMore() {
        page++;
        getdetermine(new PreferencesHelper(MessageActivity.this).getToken(),page);
    }

    @Override
    public void onRefresh() {
        page=1;
        getdetermine(new PreferencesHelper(MessageActivity.this).getToken(),page);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()){
            case R.id.btn_delete:
                deleteMessage(new PreferencesHelper(MessageActivity.this).getToken(),adapter.getItem(position).getId());
                break;
            case R.id.ll_content:
               Intent intent=new Intent(MessageActivity.this,MessageDetailsActivity.class);
               intent.putExtra("MESSAGEID",adapter.getItem(position).getId());
               startActivity(intent);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getdetermine(new PreferencesHelper(MessageActivity.this).getToken(),page);
    }
}
