package com.guo.qlzx.nongji.service.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.activity.ManualActivity;
import com.guo.qlzx.nongji.client.activity.WebActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.adapter.RechargeRecordAdapter;
import com.guo.qlzx.nongji.service.bean.RechargeRecordListBean;
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
 * 订单-充值记录 与 钱包-交易记录
 * 李-完成
 */
public class RechargeRecordActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener {

    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    private int page = 1;

    private  int type=0;
    private RechargeRecordAdapter adapter;
    private List<RechargeRecordListBean> listBeans=new ArrayList<>();
    private String orderId="";
    @Override
    public int getContentView() {
        return R.layout.layout_recycle;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        //钱包为1 订单为0
        type = getIntent().getIntExtra("TYPE", 0);
        orderId=getIntent().getStringExtra("ORDERID");
        if (0 == type) {
            titleBar.setTitleText("交易记录");
        } else {
            titleBar.setTitleText("充值记录");
        }
        titleBar.setRightImageRes(R.drawable.ic_help);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WebActivity.startActivity(RechargeRecordActivity.this,"帮助中心","http://www.e-agri.cn/Api/Article/article/type/3");
                //startActivity(new Intent(RechargeRecordActivity.this,HelpCenterActivity.class));
                ManualActivity.startMyActivity(context,"帮助中心","6");
            }
        });
        adapter = new RechargeRecordAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(this));
        canContentView.setAdapter(adapter);
        adapter.setType(type);
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
        if (type==0){
            getMemoryListData(new PreferencesHelper(RechargeRecordActivity.this).getToken(),page);
        }else {
            getOrderListData(new PreferencesHelper(RechargeRecordActivity.this).getToken(),page,orderId);
        }
    }

    //钱包
    private void getMemoryListData(String token,final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getForwardList(token,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<RechargeRecordListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<RechargeRecordListBean>> listBaseBean) {
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
                                    ToastUtil.showToast(RechargeRecordActivity.this,getString(R.string.no_more));
                                }
                            }
                        }else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(RechargeRecordActivity.this);
                        }else {
                            ToastUtil.showToast(RechargeRecordActivity.this,listBaseBean.message);
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

    //订单
    private void getOrderListData(String token,final int page,String orderId) {
  HttpHelp.getInstance().create(RemoteApi.class).getOrderRecharge(token,page,orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<RechargeRecordListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<RechargeRecordListBean>> listBaseBean) {
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
                                    ToastUtil.showToast(RechargeRecordActivity.this,getString(R.string.no_more));
                                }
                            }
                        }else {
                            ToastUtil.showToast(RechargeRecordActivity.this,listBaseBean.message);
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
        if (type==0){
            getMemoryListData(new PreferencesHelper(RechargeRecordActivity.this).getToken(),page);
        }else {
            getOrderListData(new PreferencesHelper(RechargeRecordActivity.this).getToken(),page,orderId);
        }
    }

    @Override
    public void onRefresh() {
        page=1;
        if (type==0){
            getMemoryListData(new PreferencesHelper(RechargeRecordActivity.this).getToken(),page);
        }else {
            getOrderListData(new PreferencesHelper(RechargeRecordActivity.this).getToken(),page,orderId);
        }
    }
}
