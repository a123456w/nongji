package com.guo.qlzx.nongji.client.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.adapter.FailureDeclareAdapter;
import com.guo.qlzx.nongji.client.adapter.ServiceTelAdapter;
import com.guo.qlzx.nongji.client.bean.ConcessionExchangeBean;
import com.guo.qlzx.nongji.client.bean.SerBean;
import com.guo.qlzx.nongji.commen.activity.FeedbackActivity;
import com.guo.qlzx.nongji.commen.activity.LoginActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.PartsDetailsActivity;
import com.guo.qlzx.nongji.service.activity.ServeOrderActivity;
import com.guo.qlzx.nongji.service.activity.StorageChgPassword;
import com.guo.qlzx.nongji.service.bean.ParsDetailsCommitBean;
import com.guo.qlzx.nongji.service.bean.logOutBean;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 用户端个人中心
 */
public class SPersonalCentercActivity extends BaseActivity {
    @BindView(R.id.ll_dingdan)
    LinearLayout llDingdan;
    @BindView(R.id.ll_zuoyejilu)
    LinearLayout llZuoyejilu;
    @BindView(R.id.ll_qianbao)
    LinearLayout llQianbao;
    @BindView(R.id.ll_youhuijuan)
    LinearLayout llYouhuijuan;
    @BindView(R.id.ll_suishouji)
    LinearLayout llSuishouji;
    @BindView(R.id.ll_updatapaypassword)
    LinearLayout llUpdatapaypassword;
    @BindView(R.id.ll_updata_password)
    LinearLayout llUpdataPassword;
    @BindView(R.id.ll_finshlogin)
    LinearLayout llFinshlogin;
    @BindView(R.id.ll_yjzx)
    LinearLayout llYjzx;
    @BindView(R.id.tv_ver_name)
    TextView tvVerName;
    private PreferencesHelper helper;

    private List<String> telList = new ArrayList<>();
    private int isTrue = 0;
    private SerBean listdate;
    private SerBean listdate1;

    @Override
    public int getContentView() {
        return R.layout.activity_personal_center_c;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("我的");
        titleBar.setRightImageRes(R.drawable.ic_service);
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (telList == null || telList.size() == 0) {
                    ToastUtil.showToast(SPersonalCentercActivity.this, "客服暂未开通");
                    return;
                }
                show1();
            }
        });
        helper = new PreferencesHelper(this);
        tvVerName.setText(getVerName(this));
        if (new PreferencesHelper(this).getUserRole().equals("2")) {
            llDingdan.setVisibility(View.GONE);
        }
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
                ToolUtil.goToCall(SPersonalCentercActivity.this, telList.get(position));
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

    @Override
    public void getData() {
        getDateKefu();
        isSetPayPw(helper.getToken());
    }

    @OnClick({R.id.ll_dingdan, R.id.ll_zuoyejilu, R.id.ll_qianbao, R.id.ll_youhuijuan, R.id.ll_suishouji, R.id.ll_updatapaypassword, R.id.ll_updata_password, R.id.ll_finshlogin, R.id.ll_yjzx, R.id.tv_feedback})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            //订单
            case R.id.ll_dingdan:
                startActivity(new Intent(this, ServeOrderActivity.class));
                break;
            //作业流程
            case R.id.ll_zuoyejilu:
                startActivity(new Intent(SPersonalCentercActivity.this, JobrecordActivity.class));
                break;
            //钱包
            case R.id.ll_qianbao:
                startActivity(new Intent(SPersonalCentercActivity.this, WalletActivity.class));

                break;
            //优惠卷
            case R.id.ll_youhuijuan:
                startActivity(new Intent(SPersonalCentercActivity.this, ConcessionExchangeActivity.class));

                break;
            //随手记
            case R.id.ll_suishouji:
                startActivity(new Intent(SPersonalCentercActivity.this, NotewithhandActivity.class));

                break;
            //修改支付密码
            case R.id.ll_updatapaypassword:
                if (2 == isTrue) {
                    ToastUtil.showToast(SPersonalCentercActivity.this, "网络加载中，请稍后");
                    return;
                }
                intent = new Intent(SPersonalCentercActivity.this, ChangePayPasswordWordActivity.class);
                intent.putExtra("TYPE", isTrue);
                startActivity(intent);
                break;
            //修改密码
            case R.id.ll_updata_password:
                startActivity(new Intent(SPersonalCentercActivity.this, StorageChgPassword.class));
                break;
            //退出登录
            case R.id.ll_finshlogin:
                getOutLog();
                break;
            //永久注销
            case R.id.ll_yjzx:
                String token = helper.getToken();
                //注销状态
                getcheckCancel(token);
                break;
                //意见反馈
            case R.id.tv_feedback:
                startActivity(new Intent(context, FeedbackActivity.class));
                break;

        }
    }

    /**
     * 撤销状态AlertDialog弹框
     */
    private void getcheckCancelons() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_write_review, null);
        final Button com = contentView.findViewById(R.id.but_cans);
        final Button coms = contentView.findViewById(R.id.but_cants);
        coms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.cancel();
            }
        });
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNoCancellations(helper.getToken());
                bottomDialog.cancel();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }

    /**
     * 申请提交AlertDialog弹框
     */
    private void getcheckCancelon() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_write_state, null);
        final Button com = contentView.findViewById(R.id.but_commit);
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.cancel();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }

    public void isSetPayPw(String token) {

        HttpHelp.getInstance().create(RemoteApi.class).isSetPayPw(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            isTrue = 0;

                        } else if (listBaseBean.code == 1) {
                            isTrue = 1;

                        } else {
                            isTrue = 2;
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        isTrue = 2;
                    }
                });

    }

    /**
     * 注销状态
     */
    private void getcheckCancel(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getcancel(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            //注销AlertDialog弹框
                            getCancellation();
                        } else if (listBaseBean.code == 1) {
                            //撤销
                            getcheckCancelons();
                        } else if (listBaseBean.code == 3) {
                            //注销AlertDialog弹框
                            getCancellation();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(SPersonalCentercActivity.this, "取消注销失败");
                    }
                });
    }

    /**
     * 注销AlertDialog弹框
     */
    private void getCancellation() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_write_off, null);
        Button imageView = contentView.findViewById(R.id.but_can);
        final Button com = contentView.findViewById(R.id.but_com);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.cancel();
            }
        });
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCancellations(helper.getToken());
                bottomDialog.cancel();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }

    /**
     * 撤销注销的方法
     *
     * @param token
     */
    private void getNoCancellations(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getnorevocation(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            ToastUtil.showToast(SPersonalCentercActivity.this, "撤销成功");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(SPersonalCentercActivity.this, "撤销失败");

                    }
                });
    }

    /**
     * 注销的方法
     *
     * @param token
     */
    private void getCancellations(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getrevocation(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            getcheckCancelon();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(SPersonalCentercActivity.this, "注销失败");
                    }
                });

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
                            ToastUtil.showToast(SPersonalCentercActivity.this, "退出成功");
                            startActivity(SPersonalCentercActivity.this, LoginActivity.class);
                        } else if (listBaseBean.code == 4) { //未登录
                            ToastUtil.showToast(SPersonalCentercActivity.this, "退出成功");
                            startActivity(SPersonalCentercActivity.this, LoginActivity.class);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(SPersonalCentercActivity.this, "退出失败");
                    }
                });
    }

    public String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "V" + verName;
    }
}
