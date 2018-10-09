package com.guo.qlzx.nongji.service.activity;


import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.activity.SPersonalCentercActivity;
import com.guo.qlzx.nongji.client.adapter.ServiceTelAdapter;
import com.guo.qlzx.nongji.client.bean.SerBean;
import com.guo.qlzx.nongji.commen.activity.LoginActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.bean.logOutBean;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
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

/**
 * 个人中心
 */
public class PersonalCenterActivity extends BaseActivity {
    private List<String> telList = new ArrayList<>();
    //获取控件id
    @BindView(R.id.ln_system)
    LinearLayout lnSystem;
    @BindView(R.id.ln_changepwd)
    LinearLayout lnChangepwd;
    @BindView(R.id.tv_off)
    TextView tvOff;
    @BindView(R.id.vi_c)
    View viC;
    private PreferencesHelper helper;

    @Override
    public int getContentView() {
        return R.layout.activity_personal_center;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(this);
        titleBar.setTitleText("个人中心");
        titleBar.setRightImageRes(R.drawable.ic_service);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (telList == null || telList.size() == 0) {
                    ToastUtil.showToast(context, "客服暂未开通");
                    return;
                }
                show1();
            }
        });
    }
    /**
     * 客服
     */
    private void getDateKefu() {
        HttpHelp.getInstance().create(RemoteApi.class).gettel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<SerBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<SerBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            List<SerBean> data = listBaseBean.data;
                            for (SerBean list : data) {
                                telList.add(list.getPhone_number());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });

    }
    private void show1() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_normal, null);
        ImageView imageView = contentView.findViewById(R.id.iv_close);
        final ListView listView = contentView.findViewById(R.id.lv_list);
        ServiceTelAdapter adapter = new ServiceTelAdapter(listView);
        listView.setAdapter(adapter);
        adapter.setData(telList);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomDialog.cancel();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToolUtil.goToCall(context, telList.get(position));
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
//        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog);
        bottomDialog.show();
    }
    @Override
    public void getData() {
        String userRole = helper.getUserRole();
        getDateKefu();
        if (userRole.equals("5")) {
            lnSystem.setVisibility(View.GONE); // 隐藏
            viC.setVisibility(View.GONE);
        }
    }

    /**
     * 管理系统
     */
    @OnClick(R.id.ln_system)
    public void onviewclick() {
        startActivity(PersonalCenterActivity.this, ManageMentActivity.class);
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.ln_changepwd)
    public void onviewclick1() {
        startActivity(PersonalCenterActivity.this, StorageChgPassword.class);
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.tv_off)
    public void onviewclick2() {
        //调用退出登录弹框方法
        getOutLog();
    }


    /**
     * 退出登录AlertDialog弹框
     */
    private void getOutLog() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_write_log, null);
        Button imageView = contentView.findViewById(R.id.but_cans);
        final Button com = contentView.findViewById(R.id.but_coms);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.cancel();
            }
        });
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdetermine(helper.getToken());
                bottomDialog.cancel();
                helper.clear();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }

    /**
     * 退出登录方法
     */
    public void getdetermine(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).loginout(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<logOutBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<logOutBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            ToastUtil.showToast(PersonalCenterActivity.this, "退出成功");
                            startActivity(PersonalCenterActivity.this, LoginActivity.class);
                        }else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(PersonalCenterActivity.this);
                        } else {
                            ToastUtil.showToast(PersonalCenterActivity.this, listBaseBean.message);
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