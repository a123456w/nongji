package com.guo.qlzx.nongji.service.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.service.activity.LogsAsrvicesDetailsActivity;
import com.guo.qlzx.nongji.service.bean.LogsSrrvicesBean;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/6/5.
 * 服务日志列表
 */

public class LogsSrrvicesAdapter extends RecyclerViewAdapter<LogsSrrvicesBean> {

    private Object data;
    private PreferencesHelper helper;
    private String sn;
    private String id;
    private boolean isCheckeds = false;
    private ImageView im_accident;
    private TextView tv_chuli;

    public LogsSrrvicesAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_logssrrvices);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, final LogsSrrvicesBean model) {
        Button butNormal = viewHolderHelper.getView(R.id.but_normal);
        Button butEntering = viewHolderHelper.getView(R.id.but_entering);
        sn = model.getName();
        im_accident = viewHolderHelper.getView(R.id.im_accident);
        tv_chuli = viewHolderHelper.getView(R.id.tv_chuli);
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getState(), viewHolderHelper.getImageView(R.id.im_accident));
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), viewHolderHelper.getImageView(R.id.im_serjiqi));
        viewHolderHelper.setText(R.id.tv_kun, "当日作业捆数:" + model.getToday_nums() + "捆");
        viewHolderHelper.setText(R.id.tv_time, "当日作业时长:" + model.getToday_time() + "小时");
        viewHolderHelper.setText(R.id.tv_sumkun, "总作业捆数:" + model.getTotal_nums() + "捆");
        viewHolderHelper.setText(R.id.tv_numtime, "总作业时长:" + model.getTotal_days() + "天");
        viewHolderHelper.setText(R.id.tv_number, model.getName());

        int state = model.getState();
        if (state == 1) {
            viewHolderHelper.setText(R.id.tv_chuli, "已处理");
            im_accident.setVisibility(View.INVISIBLE);//表示隐藏；
            butNormal.setEnabled(false);
            butEntering.setEnabled(false);
            viewHolderHelper.getView(R.id.rl_right).setBackgroundResource(0);
        } else if (state == 2) {
            tv_chuli.setVisibility(View.GONE);
            viewHolderHelper.getView(R.id.rl_right).setBackgroundResource(R.drawable.logs_back);
            GlideUtil.display(mContext, Constants.IMG_HOST + R.drawable.ic_label, viewHolderHelper.getImageView(R.id.im_accident));
            viewHolderHelper.getView(R.id.rl_right).setBackgroundResource(R.drawable.logs_setting);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                butNormal.setBackground(mContext.getResources().getDrawable(R.drawable.shape_searchs));
                butNormal.setEnabled(false);
            }
            butEntering.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LogsAsrvicesDetailsActivity.class);
                    intent.putExtra("ORDERID1", model.getName());
                    intent.putExtra("ORDERID2", model.getId());
                    mContext.startActivity(intent);
                }
            });
        } else if (state == 3) {
            tv_chuli.setVisibility(View.GONE);
            im_accident.setVisibility(View.INVISIBLE);//表示隐藏；
            viewHolderHelper.getView(R.id.rl_right).setBackgroundResource(0);
            butNormal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initDialog();
                    helper = new PreferencesHelper(mContext);
                    id = model.getId();
                }
            });
            butEntering.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LogsAsrvicesDetailsActivity.class);
                    intent.putExtra("ORDERID1", model.getName());
                    intent.putExtra("ORDERID2", model.getId());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    /**
     * 正常提交弹框
     */
    @SuppressLint("ResourceType")
    private void initDialog() {
        final Dialog bottomDialog = new Dialog(mContext, R.style.BottomDialog);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_write_commit, null);
        Button imageView = contentView.findViewById(R.id.but_ca);
        TextView tv_sn = contentView.findViewById(R.id.tv_sn);
        tv_sn.setText("是否确定" + sn + "正常提交？");
        final Button com = contentView.findViewById(R.id.but_co);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.cancel();
            }
        });
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNormalDate(new PreferencesHelper(mContext).getToken(), id);
                bottomDialog.cancel();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }

    /**
     * 正常提交方法
     */
    private void getNormalDate(String token, String id) {
        HttpHelp.getInstance().create(RemoteApi.class).getNormal(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(mContext, null) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            data = baseBean.data;
                            ToastUtil.showToast(mContext, "提交成功");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(mContext, "提交失败");
                    }
                });
    }
}