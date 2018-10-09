package com.guo.qlzx.nongji.service.activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.adapter.ManageMentAdapter;
import com.guo.qlzx.nongji.service.bean.ManageMentBean;
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
 * 管理系统类
 */
public class ManageMentActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener, OnRVItemClickListener {

    //获取控件id
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    private ManageMentAdapter adapter;
    private List<ManageMentBean> beanList = new ArrayList<>();
    private PreferencesHelper helper;
    private int page = 1;
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
    @Override
    public int getContentView() {
        return R.layout.activity_manage_ment;
    }
    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(this);
        titleBar.setTitleText("管理系统");
        //调用初始化控件
        initRefresh();
    }

    /**
     * 初始化刷新控件，声明列表点击事件
     */
    public void initRefresh() {
          adapter = new ManageMentAdapter(canContentView);
         canContentView.setLayoutManager(new LinearLayoutManager(this));
         canContentView.setAdapter(adapter);
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        adapter.setOnRVItemClickListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");
        adapter.setOnRVItemClickListener(this);
        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

    }


    //点击子条目跳转
    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent=new Intent(ManageMentActivity.this,OrderManageMentActivity.class);
        intent.putExtra("ORDERID",adapter.getItem(position).getId());
        startActivity(intent);
    }

    @Override
    public void getData() {
        //id为14
        String token = helper.getToken();
        //调用数据展示方法
        getMentDate(helper.getToken(),page+"");
    }

    /**
     * 管理系统数据展示方法
     */
    private void getMentDate(String token,String page) {
        HttpHelp.getInstance().create(RemoteApi.class).getment(token,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ManageMentBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<ManageMentBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (listBaseBean.code == 0) {
                            beanList = listBaseBean.data;
                            if (ManageMentActivity.this.page == 1) {
                                if (beanList != null && beanList.size() > 0) {
                                    adapter.setData(beanList);
                                } else {
                                    loadingLayout.setStatus(LoadingLayout.Empty);
                                }

                            } else {
                                if (beanList != null && beanList.size() > 0) {
                                    adapter.addMoreData(beanList);
                                } else {
                                    ManageMentActivity.this.page--;
                                 ToastUtil.showToast(ManageMentActivity.this,getString(R.string.no_more));
                                }
                            }
                        }else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(ManageMentActivity.this);
                        }else {
                            ToastUtil.showToast(ManageMentActivity.this,listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();

                    }
                });
    }


}