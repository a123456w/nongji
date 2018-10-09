package com.guo.qlzx.nongji.service.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.bean.MessageDetailsBean;
import com.guo.qlzx.nongji.service.bean.MessageListBean;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/5/30.
 * 消息详情
 */

public class MessageDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_content)
    TextView tvContent;

    private String id="";
    private PreferencesHelper helper;
    @Override
    public int getContentView() {
        return R.layout.activity_message_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("消息");
        helper=new PreferencesHelper(this);
        id= getIntent().getStringExtra("MESSAGEID");
    }

    @Override
    public void getData() {
        getDetails(helper.getToken(),id);
    }
    public void getDetails(String token, String id){
        HttpHelp.getInstance().create(RemoteApi.class).getMessageDetails(token,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<MessageDetailsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<MessageDetailsBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code==0){
                            tvContent.setText(listBaseBean.data.getContent());
                        }
                        else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(MessageDetailsActivity.this);
                        }else {
                            ToastUtil.showToast(MessageDetailsActivity.this,listBaseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
}
