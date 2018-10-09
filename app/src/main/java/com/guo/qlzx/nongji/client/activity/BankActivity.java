package com.guo.qlzx.nongji.client.activity;


/**
 * 客户端 钱包-银行卡
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.Costom.DeclarePopWindow;
import com.guo.qlzx.nongji.client.adapter.BankAdapter;
import com.guo.qlzx.nongji.client.bean.BankListBean;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogRecordDetailsActivity;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BankActivity extends BaseActivity implements OnRVItemClickListener {


    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    private BankAdapter adapter;
    private PreferencesHelper helper;

    @Override
    public int getContentView() {
        return R.layout.activity_bank;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("银行卡");
        adapter=new BankAdapter(rvList);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);
        helper=new PreferencesHelper(this);
        adapter.setOnRVItemClickListener(this);

    }

    @Override
    public void getData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getBankListData(helper.getToken());
    }

    private void getBankListData(String token){
        HttpHelp.getInstance().create(RemoteApi.class).getBankList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<BankListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<BankListBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                           adapter.setData(baseBean.data);

                        }else if (baseBean.code==4){
                            ToolUtil.getOutLogs(BankActivity.this);
                        } else {
                            ToastUtil.showToast(BankActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
    @OnClick({R.id.ll_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_add:
                //添加银行卡
                startActivity(new Intent(BankActivity.this,AddBankActivity.class));
               break;
        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent=new Intent(BankActivity.this,BankDetailsActivity.class);
        intent.putExtra("BANKID",adapter.getItem(position).getId());
        startActivity(intent);
    }
}
