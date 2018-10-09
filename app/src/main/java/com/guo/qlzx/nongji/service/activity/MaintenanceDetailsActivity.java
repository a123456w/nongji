package com.guo.qlzx.nongji.service.activity;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.adapter.MaintenanceDetailsAdapter;
import com.guo.qlzx.nongji.service.bean.MaintenanceDetailsBean;
import com.guo.qlzx.nongji.service.fragment.TechnicalSupportFragment;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
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
 * 机器保养详情
 */
public class MaintenanceDetailsActivity extends BaseActivity implements OnRVItemClickListener {

    @BindView(R.id.re_cy)
    RecyclerView recy;
    private PreferencesHelper helper;
    private  MaintenanceDetailsAdapter adapter;
    private List<MaintenanceDetailsBean> data1=new ArrayList<>();
    private String machine;
    private String machineName = "";
    @Override
    public int getContentView() {
        return R.layout.activity_maintenance_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);

        machineName = getIntent().getStringExtra("ORDERID1");
        titleBar.setTitleText(machineName);
        //返回点击事件
        titleBar.setLeftTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MaintenanceDetailsActivity.this, TechnicalSupportFragment.class);
            }
        });
        adapter = new MaintenanceDetailsAdapter(recy);
        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);
    }

    @Override
    public void getData() {
        machine = getIntent().getStringExtra("ORDERID");
        //机器保养详情
        getmaintainInfo(new PreferencesHelper(this).getToken(),machine);
    }

    /**
     * 机器保养详情
     */
    private void getmaintainInfo(String token,String machine_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getmaintaininfo(token,machine_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<MaintenanceDetailsBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<MaintenanceDetailsBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            data1 = listBaseBean.data;

                            if (data1 != null && data1.size() > 0) {
                                adapter.setData(data1);
                            } else {
                                //加载图片
                                loadingLayout.setStatus(LoadingLayout.Empty);
                            }
                        } else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(MaintenanceDetailsActivity.this);
                        }else {
                            ToastUtil.showToast(MaintenanceDetailsActivity.this,listBaseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
            Intent intent = new Intent(MaintenanceDetailsActivity.this,PartsDetailsActivity.class);
            intent.putExtra("ORDERID",machine);
            intent.putExtra("ORDERID3",adapter.getItem(position).getId());
            intent.putExtra("MACHINTENANCEDETAIL",machineName);
            startActivity(intent);

    }
}
