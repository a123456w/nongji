package com.guo.qlzx.nongji.service.activity;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.adapter.OrderManageMentAdapter;
import com.guo.qlzx.nongji.service.bean.DeleteBean;
import com.guo.qlzx.nongji.service.bean.OrderManageMentBean;
import com.guo.qlzx.nongji.service.bean.ReplaceBean;
import com.guo.qlzx.nongji.service.bean.SendBean;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 管理系统列表详情类
 */
public class OrderManageMentActivity extends BaseActivity {
    //获取控件id
    @BindView(R.id.re_clerviewdetails)
    RecyclerView reClerviewdetails;
    private OrderManageMentAdapter adapter;
    private List<OrderManageMentBean> beanList = new ArrayList<>();
    private String machine_name;
    private DeleteBean beanList1;
    private int id;
    private ReplaceCouponDialog replaceCouponDialog;

    private List<ReplaceBean> beanLists;
    private String id1;
    private String operator_id;
    private String orderId = "";

    @Override
    public int getContentView() {
        return R.layout.activity_order_manage_ment2;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("管理系统");
        titleBar.setRightTextRes("添加");
        orderId = getIntent().getStringExtra("ORDERID");
        //标题跳转
        titleBar.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderManageMentActivity.this, AddMachinistActivity.class);
                intent.putExtra("ORDERID", orderId);
                startActivity(intent);
            }
        });
        adapter = new OrderManageMentAdapter(reClerviewdetails);
        reClerviewdetails.setLayoutManager(new LinearLayoutManager(this));
        reClerviewdetails.setAdapter(adapter);
        //删除按钮的点击事件
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {

                switch (childView.getId()) {
                    case R.id.im_delete:
                        machine_name = beanList.get(position).getMachine_name();
                        id = beanList.get(position).getId();
                        //调用删除弹出框方法
                        if (adapter.getData().size() == 1) {
                            lastDelete();
                        } else {
                            getDelete();
                        }
                        break;
                    case R.id.tv_machinedetails:
//                        machine_name = beanList.get(position).getMachine_name();
//                        id1 = beanList.get(position).getMachine_id();
//                        operator_id = beanList.get(position).getOperator_id();
//                        replaceCouponDialog = new ReplaceCouponDialog(OrderManageMentActivity.this,                  "将" + machine_name + "替换为");
//                        replaceCouponDialog.setOnCancelClick(new ReplaceCouponDialog.OnCancelClick() {
//                            @Override
//                            public void onCancel() {
//                                replaceCouponDialog.cancel();
//                            }
//                        });
//                        replaceCouponDialog.setOnSubmitClick(new ReplaceCouponDialog.OnSubmitClick() {
//                            @Override
//                            public void onSubmit(String id) {
//                                getSubmit(new PreferencesHelper(OrderManageMentActivity.this).getToken(), id1, id, operator_id, orderId);
//                            }
//                        });
//                        replaceCouponDialog.setCanceledOnTouchOutside(false);
//                        replaceCouponDialog.show();
                        break;
                }
            }
        });
    }

    /**
     * 确定修改
     */
    private void getSubmit(String token, String old_id, String new_id, String operator_id, String order_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getreplacement(token, old_id, new_id, operator_id, order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(this, null) {
                    @Override
                    public void onNext(BaseBean<Object> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            replaceCouponDialog.cancel();
                            getOrderDate(new PreferencesHelper(OrderManageMentActivity.this).getToken(), orderId);
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.getOutLogs(OrderManageMentActivity.this);
                        } else {
                            ToastUtil.showToast(OrderManageMentActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }
    /**
     * 删除AlertDialog弹框
     */
    private void getDelete() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_delete, null);
        final Button com=contentView.findViewById(R.id.but_cans);
        final Button coms=contentView.findViewById(R.id.but_cants);
        final TextView jiqi=contentView.findViewById(R.id.tv_jiqi);
        jiqi.setText("确定要删除"+machine_name+"机器吗？");
        coms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.cancel();
            }
        });
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getddeleteDate(new PreferencesHelper(OrderManageMentActivity.this).getToken(), id);
                bottomDialog.cancel();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }
    /**
     * 删除最后一条AlertDialog弹框
     */
    private void lastDelete() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_last, null);
        final Button com=contentView.findViewById(R.id.but_cans);
        final Button coms=contentView.findViewById(R.id.but_cants);
        coms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.cancel();
            }
        });
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getddeleteDate(new PreferencesHelper(OrderManageMentActivity.this).getToken(), id);
                bottomDialog.cancel();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }

    /**
     * 管理系统详情删除
     *
     * @param token
     * @param id
     */
    private void getddeleteDate(String token, int id) {
        HttpHelp.getInstance().create(RemoteApi.class).getdele(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<DeleteBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<DeleteBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            beanList1 = listBaseBean.data;
                            adapter.notifyDataSetChanged();
                            getOrderDate(new PreferencesHelper(OrderManageMentActivity.this).getToken(), orderId);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }

    @Override
    public void getData() {

        //调用管理系统数据展示方法
        getOrderDate(new PreferencesHelper(OrderManageMentActivity.this).getToken(), orderId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * 管理系统详情页面数据展示方法
     *
     * @param token
     * @param id
     */
    private void getOrderDate(String token, String id) {
        HttpHelp.getInstance().create(RemoteApi.class).getorder(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<OrderManageMentBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<OrderManageMentBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            beanList = listBaseBean.data;
                            if (beanList != null && beanList.size() > 0) {
                                adapter.setData(beanList);
                                EventBusUtil.post(new SendBean());
                            } else {
                                //加载图片
                                loadingLayout.setStatus(LoadingLayout.Empty);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getOrderDate(new PreferencesHelper(OrderManageMentActivity.this).getToken(), orderId);
    }
}
