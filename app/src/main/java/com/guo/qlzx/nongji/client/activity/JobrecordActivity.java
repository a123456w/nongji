package com.guo.qlzx.nongji.client.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.adapter.JobRecordAdapter;
import com.guo.qlzx.nongji.client.bean.JobRecordListBean;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogRecordDetailsActivity;
import com.guo.qlzx.nongji.service.activity.MessageActivity;
import com.guo.qlzx.nongji.service.adapter.ManageMentAdapter;
import com.guo.qlzx.nongji.service.bean.MessageListBean;
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
 * 作业记录———用户端
 */
public class JobrecordActivity extends BaseActivity  {

    @BindView(R.id.can_content_view)
    RecyclerView canContentView;

    private JobRecordAdapter adapter;
    private PreferencesHelper helper;
    private List<JobRecordListBean> listBeans=new ArrayList<>();
    @Override
    public int getContentView() {
        return R.layout.activity_jobrecord;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(this);
        titleBar.setTitleText("作业记录");
        adapter = new JobRecordAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(this));
        canContentView.setAdapter(adapter);
    }

    @Override
    public void getData() {
        getListData(helper.getToken());
    }
    public void getListData(String token){
        HttpHelp.getInstance().create(RemoteApi.class).getJobRecordData(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<JobRecordListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<JobRecordListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code==0){

                            listBeans=listBaseBean.data;
                            adapter.setData(listBeans);
                        }else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(JobrecordActivity.this);
                        }else {
                            //加载图片
                            loadingLayout.setStatus(LoadingLayout.Empty);
                            ToastUtil.showToast(JobrecordActivity.this,listBaseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
}
