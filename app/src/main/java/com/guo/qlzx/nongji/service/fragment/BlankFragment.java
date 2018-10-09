package com.guo.qlzx.nongji.service.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.ManageMentActivity;
import com.guo.qlzx.nongji.service.activity.MessageActivity;
import com.guo.qlzx.nongji.service.activity.OrderDetails;
import com.guo.qlzx.nongji.service.activity.OrderManageMentActivity;
import com.guo.qlzx.nongji.service.adapter.MessageAdapter;
import com.guo.qlzx.nongji.service.adapter.OrderAdapter;
import com.guo.qlzx.nongji.service.bean.MessageListBean;
import com.guo.qlzx.nongji.service.bean.OrderListBean;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
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
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/5/30.
 */

public class BlankFragment extends BaseFragment implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener,OnRVItemClickListener {
    Unbinder unbinder;
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.ll_none)
    LinearLayout llNone;
    private int type=1;
    private Context mContext;
    private String orderId="";
    private PreferencesHelper helper;
    private OrderAdapter adapter;
    private List<OrderListBean> listBeans=new ArrayList<>();
    private int page=1;

    @SuppressLint("ValidFragment")
    public BlankFragment(int type,Context context){
        this.type=type;
        this.mContext=context;
    }
    public BlankFragment(){

    }
    //页面类型 1全部，2未完成,3已完成
    public void setStatus(int state,Context context){
        this.type=state;
        this.mContext=context;
    }
    //根据id搜索内容
    public void setOrderId(String orderId){
        this.orderId=orderId;
        getOrderData(helper.getToken(),page,type,orderId);
    }
    public void setOrder(String orderId){
        this.orderId=orderId;
    }
    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.layout_recycle, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(mContext);
        titleBar.setVisibility(View.GONE);
        adapter = new OrderAdapter(canContentView);
        adapter.setOnRVItemClickListener(this);
        adapter.setContext(mContext);
        canContentView.setLayoutManager(new LinearLayoutManager(mContext));
        canContentView.setAdapter(adapter);
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(mContext, 150));
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
    //点击子条目跳转
    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent=new Intent(mContext,OrderDetails.class);
        intent.putExtra("ORDERID",adapter.getItem(position).getId());
        startActivity(intent);
    }
    @Override
    public void getData() {
        getOrderData(helper.getToken(),page,type,orderId);
    }
    private void getOrderData(String token, final int page,int type,String search){
        HttpHelp.getInstance().create(RemoteApi.class).getOrderList(token,page,type,search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<OrderListBean>>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<List<OrderListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code==0){
                            refresh.refreshComplete();
                            refresh.loadMoreComplete();
                            canContentView.setVisibility(View.VISIBLE);
                            llNone.setVisibility(View.GONE);
                            listBeans=listBaseBean.data;
                            if (page == 1) {
                                if (listBeans != null && listBeans.size() > 0) {
                                    adapter.setData(listBeans);
                                } else {
                                    canContentView.setVisibility(View.GONE);
                                    llNone.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (listBeans != null && listBeans.size() > 0) {
                                    adapter.addMoreData(listBeans);
                                } else {
                                    ToastUtil.showToast(mContext,getString(R.string.no_more));
                                }
                            }
                        }else if (listBaseBean.code==4){
                           ToolUtil.getOutLogs(mContext);
                        }else {
                            ToastUtil.showToast(mContext,listBaseBean.message);
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
        getOrderData(helper.getToken(),page,type,orderId);
    }

    @Override
    public void onRefresh() {
        page=1;
        getOrderData(helper.getToken(),page,type,orderId);
    }

}
