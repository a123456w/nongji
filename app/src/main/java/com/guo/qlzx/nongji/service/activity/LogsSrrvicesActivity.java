package com.guo.qlzx.nongji.service.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.adapter.LogsSrrvicesAdapter;
import com.guo.qlzx.nongji.service.bean.LogsSrrvicesBean;
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
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * 服务日志
 */
public class LogsSrrvicesActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener {

    @BindView(R.id.can_content_view)
    RecyclerView can_content_view;
    @BindView(R.id.ll_none)
    LinearLayout llNone;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    private List<LogsSrrvicesBean> listdata=new ArrayList<>();
    private LogsSrrvicesAdapter adapter;
    private PreferencesHelper helper;


    @Override
    public int getContentView() {
        return R.layout.activity_logs_srrvices;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        ButterKnife.bind(this);
        titleBar.setTitleText("服务日志");

        //返回点击事件
        titleBar.setLeftTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LogsSrrvicesActivity.this, TechnicalSupportFragment.class);
            }
        });
        adapter = new LogsSrrvicesAdapter(can_content_view);
        can_content_view.setLayoutManager(new LinearLayoutManager(this));
        can_content_view.setAdapter(adapter);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //调用日志列表方法
        getServiceJourDate( new PreferencesHelper(this).getToken());
    }

    @Override
    public void getData() {

    }

    /**
     * 日志列表方法
     */
    private void getServiceJourDate(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getServiceJour(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<LogsSrrvicesBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<LogsSrrvicesBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        can_content_view.setVisibility(View.VISIBLE);
                        llNone.setVisibility(View.GONE);
                        refresh.refreshComplete();
                        if (listBaseBean.code == 0) {
                            listdata = listBaseBean.data;
                            if (listdata != null && listdata.size() > 0) {
                                adapter.setData(listdata);
                            } else {
                                //加载图片
                                can_content_view.setVisibility(View.GONE);
                                llNone.setVisibility(View.VISIBLE);
                            }
                        }
                        else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(LogsSrrvicesActivity.this);
                        }else {
                            ToastUtil.showToast(LogsSrrvicesActivity.this,listBaseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }



    @Override
    public void onRefresh() {
        getServiceJourDate( new PreferencesHelper(this).getToken());
    }
}
