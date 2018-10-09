package com.guo.qlzx.nongji.service.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ActionItem;
import com.guo.qlzx.nongji.commen.util.TitlePopup;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.adapter.OrderManageMentAdapter;
import com.guo.qlzx.nongji.service.bean.CommitAddListBean;
import com.guo.qlzx.nongji.service.bean.CommitBean;
import com.qlzx.mylibrary.base.BaseActivity;
;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.StringUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 管理系统添加类
 */
public class AddMachinistActivity extends BaseActivity {


    @BindView(R.id.et_tractordriver)
    EditText etTractordriver;
    @BindView(R.id.tv_jiqi)
    EditText tvJiqi;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.but_commit)
    Button butCommit;
    private List<CommitAddListBean> beanList = new ArrayList<>();
    private CommitBean beanLists;
    private TitlePopup titlePopup;
    private String currentChooseType = "";
    private String status = "1";

    private String orderId = "";
    private String machine_id = "";
    private PreferencesHelper helper;

    @Override
    public int getContentView() {
        return R.layout.activity_add_machinist;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(this);
        titleBar.setTitleText("添加机手");
        //实例化popwi
        titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {

                tvJiqi.setText(beanList.get(position).getMachine_name());
                machine_id = beanList.get(position).getMachine_id();

            }
        });
        orderId = getIntent().getStringExtra("ORDERID");
    }

    @Override
    public void getData() {
        getCommitDate(helper.getToken(), orderId);
    }

    @OnClick({R.id.iv_pic, R.id.but_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pic:  //案件类型
                if (titlePopup.getAllAction().size() == 0) {
                    ToastUtil.showToast(context, "暂无可用机器");
                    return;
                }
                titlePopup.show(view);
                break;
            //搜索
            case R.id.but_commit:
                String tell = etTractordriver.getText().toString();
                if (TextUtils.isEmpty(tell)) {
                    ToastUtil.showToast(AddMachinistActivity.this, "手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(machine_id)) {
                    ToastUtil.showToast(AddMachinistActivity.this, "请选择机器型号");
                    return;
                }
                //提交添加方法
                getChooseMachine(helper.getToken(), orderId, machine_id, tell);
                break;


        }
    }

    /**
     * 添加提交方法
     *
     * @param token
     * @param order_id
     * @param machine_id
     * @param mobile
     */
    private void getChooseMachine(String token, String order_id, String machine_id, String mobile) {
        HttpHelp.getInstance().create(RemoteApi.class).getcommit(token, order_id, machine_id, mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            finish();
                            ToastUtil.showToast(AddMachinistActivity.this, listBaseBean.message);
                        } else {
                            ToastUtil.showToast(AddMachinistActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }

    /**
     * 展示数据方法
     *
     * @param token
     * @param order_id
     */
    private void getCommitDate(String token, String order_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getaddlist(token, order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<CommitAddListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<CommitAddListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            beanList = listBaseBean.data;

                            if (beanList != null && beanList.size() != 0) {

                                for (int i = 0; i < beanList.size(); i++) {
                                    titlePopup.addAction(new ActionItem(AddMachinistActivity.this, beanList.get(i).getMachine_name(), R.drawable.ic_about));
                                }

                            } else {
                            }

                        } else if (listBaseBean.code == 4) {
                            ToolUtil.getOutLogs(AddMachinistActivity.this);
                        } else {
                            ToastUtil.showToast(AddMachinistActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
