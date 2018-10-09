package com.guo.qlzx.nongji.service.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.bean.ParsDetailsCommitBean;
import com.guo.qlzx.nongji.service.bean.PartsDetailsBean;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 机器保养详情----零件保养
 */
public class PartsDetailsActivity extends BaseActivity {

    @BindView(R.id.tv_hour)
    TextView tvHour;
    @BindView(R.id.tv_servicetime)
    TextView tvServicetime;
    @BindView(R.id.im_apparatus)
    ImageView imApparatus;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.but_have)
    Button butHave;
    private PartsDetailsBean data1;
    private ParsDetailsCommitBean listdate;
    private String machine;
    private String positio;
    private Button imageView;

    @Override
    public int getContentView() {
        return R.layout.activity_parts_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);

        String name = getIntent().getStringExtra("MACHINTENANCEDETAIL");
        titleBar.setTitleText(name);
        //已保养按钮
        butHave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCommitdo();
            }
        });
        butHave.setText("已保养");
        butHave.setClickable(false);
    }
    /**
     * 提交弹框的方法
     */
    private void getCommitdo() {

        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_commit, null);
        imageView = contentView.findViewById(R.id.but_que);
        final Button com=contentView.findViewById(R.id.but_fan);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int work_time = data1.getWork_time();
                getHave(new PreferencesHelper(PartsDetailsActivity.this).getToken(), machine, positio,work_time);
                butHave.setText("已保养");
                bottomDialog.cancel();
            }
        });
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
    /**
     * 已保养方法
     */
    private void getHave(String token, String machine_id, String position_id,int work_time) {
        butHave.setClickable(false);
        HttpHelp.getInstance().create(RemoteApi.class).getdomaintain(token, machine_id, position_id,work_time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ParsDetailsCommitBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ParsDetailsCommitBean> listBaseBean) {
                        butHave.setClickable(true);
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            listdate = listBaseBean.data;
                            tvHour.setText(listdate.getWork_time()+"小时");
                            tvServicetime.setText(listdate.getNext_maintime()+"小时");
                            GlideUtil.display(PartsDetailsActivity.this, Constants.IMG_HOST+listdate.getMaintain_pic(),imApparatus);
                            tvIntroduce.setText(listdate.getDescription());
                            ToastUtil.showToast(PartsDetailsActivity.this,"保养成功");
                        }else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(PartsDetailsActivity.this);
                        }else {
                            ToastUtil.showToast(PartsDetailsActivity.this,listBaseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        butHave.setClickable(true);
                        super.onError(throwable);
                    }
                });
    }

    @Override
    public void getData() {

        machine = getIntent().getStringExtra("ORDERID");
        positio = getIntent().getStringExtra("ORDERID3");
        //调用零件保养
        getShowMain(new PreferencesHelper(PartsDetailsActivity.this).getToken(), machine, positio);
    }

    /**
     * 零件保养方法
     */
    private void getShowMain(String token, String machine_id, String position_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getshow(token, machine_id, position_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<PartsDetailsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<PartsDetailsBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            data1 = listBaseBean.data;
                            upDataUI();
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    private void upDataUI() {
        tvHour.setText(data1.getWork_time()+"小时");
        tvServicetime.setText(data1.getNext_maintime()+"小时");
        GlideUtil.display(PartsDetailsActivity.this, Constants.IMG_HOST+data1.getMaintain_pic(),imApparatus);
        tvIntroduce.setText(data1.getDescription());
        if(data1.getWork_time() <= 0){
            butHave.setText("已保养");
            butHave.setClickable(false);
        }else {
            butHave.setClickable(true);
            butHave.setText("确认保养");
        }
    }


}
