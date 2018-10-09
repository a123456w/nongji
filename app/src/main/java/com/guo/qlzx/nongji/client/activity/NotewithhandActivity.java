package com.guo.qlzx.nongji.client.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.adapter.NotewihhandAdapter;
import com.guo.qlzx.nongji.client.bean.NotewithhandListBean;
import com.guo.qlzx.nongji.client.event.TagForStart;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogRecordDetailsActivity;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 用户端———随手记
 */
public class NotewithhandActivity extends BaseActivity {
    @BindView(R.id.but_remember)
    Button butRemember;
    @BindView(R.id.re_cycle)
    RecyclerView reCycle;
    @BindView(R.id.tv_riqi)
    TextView tvRiqi;
    @BindView(R.id.tv_zhichu)
    TextView tvZhichu;
    @BindView(R.id.tv_shouru)
    TextView tvShouru;
    @BindView(R.id.tv_lirun)
    TextView tvLirun;
    @BindView(R.id.tv_end_riqi)
    TextView tvEndRiqi;
    private NotewihhandAdapter adapter;
    private PreferencesHelper helper;
    private NotewithhandListBean data;
    String fistDay = null;
    String endDay = null;
    //日历控件
    DatePickerDialog dp;
    private List<NotewithhandListBean.ListBean> listData = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_notewithhand;
    }

    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;


    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("随手记");
        adapter = new NotewihhandAdapter(reCycle);
        reCycle.setLayoutManager(new LinearLayoutManager(this));
        reCycle.setAdapter(adapter);
        adapter.setData(listData);
        getRotate(270, ivLeft, R.drawable.ic_zuo);
        getRotate(90, ivRight, R.drawable.ic_right_white);
    }

    private void getRotate(int rotate, ImageView imageView, int drawble) {
        Matrix matrix = new Matrix();
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(drawble)).getBitmap();
        // 设置旋转角度
        matrix.setRotate(rotate);
        // 重新绘制Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void getData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        endDay = simpleDateFormat.format(Calendar.getInstance().getTimeInMillis());
        Date date = null;
        try {
            date = simpleDateFormat.parse(endDay);
            date.setDate(01);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fistDay = simpleDateFormat.format(date);
        tvRiqi.setText(fistDay);
        tvEndRiqi.setText("至    " + endDay);
        long fist = ToolUtil.getTimeStamp(fistDay, "yyyy-MM-dd");
        long end = ToolUtil.getTimeStamp(endDay, "yyyy-MM-dd");
        //调用随手记列表方法
        getNoteDate(new PreferencesHelper(NotewithhandActivity.this).getToken(), fist, end);
    }

    /**
     * 随手记列表方法
     */
    private void getNoteDate(String token, long start, long end) {
        HttpHelp.getInstance().create(RemoteApi.class).getnotelist(token, start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<NotewithhandListBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<NotewithhandListBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            data = listBaseBean.data;
                            listData.clear();
                            if (data.getList() != null) {
                                listData.addAll(data.getList());
                            }
                            adapter.setData(listData);
                            getInitDate();
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.getOutLogs(NotewithhandActivity.this);
                        } else {
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    /**
     * 赋值
     */
    private void getInitDate() {
        tvZhichu.setText(data.getExpenditure() + "");
        tvShouru.setText(data.getIncome() + "");
        tvLirun.setText(data.getProfit() + "");
    }

    /**
     * 记一笔
     */
    @OnClick(R.id.but_remember)
    public void onViewClicked1() {
        //跳入注册页面
        // startActivity(new Intent(this, RememberActivity.class));
        startActivityForResult(new Intent(this, RememberActivity.class), TagForStart.Notewithhand);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == TagForStart.Notewithhand) {
            getNoteDate(new PreferencesHelper(NotewithhandActivity.this).getToken(), ToolUtil.getTimeStamp(fistDay, "yyyy-MM-dd"), ToolUtil.getTimeStamp(endDay, "yyyy-MM-dd"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

    }

    @OnClick({R.id.iv_left, R.id.iv_right,R.id.tv_riqi,R.id.tv_end_riqi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                addDialog(true);
                break;
            case R.id.iv_right:
                addDialog(false);
                break;
            case R.id.tv_end_riqi:
                addDialog(false);
                break;
            case R.id.tv_riqi:
                addDialog(true);
                break;
        }
    }

    private boolean compareDay(long fist, long end, boolean tag) {
        boolean b = false;
        if (tag ? fist <= end : fist <= end) {
            b = true;
        }
        return b;
    }

    private void addDialog(final Boolean tag) {
        dp = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int iyear, int monthOfYear, int dayOfMonth) {
                int year = datePicker.getYear();//年
                int month = datePicker.getMonth();//月-1
                int dayOfMonth1 = datePicker.getDayOfMonth();//日*
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = simpleDateFormat.parse(endDay);
                    Date startDate = simpleDateFormat.parse(fistDay);
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date pickerDate = simpleDateFormat.parse(year + "-" + (month + 1) + "-" + dayOfMonth1);
                    if (pickerDate.getTime() > Calendar.getInstance().getTime().getTime()) {
                        ToastUtil.showToast(context, "不能大于当前时间");
                        return;
                    }
                    if (compareDay(pickerDate.getTime(), date.getTime(), tag) && tag ? true : startDate.getTime() < pickerDate.getTime()) {


                        if (tag) {
                            fistDay = year + "-" + (month + 1) + "-" + dayOfMonth;
                            tvRiqi.setText(fistDay);
                        } else {
                            endDay = year + "-" + (month + 1) + "-" + dayOfMonth;
                            tvEndRiqi.setText("至    " + endDay);
                        }

                        getNoteDate(new PreferencesHelper(NotewithhandActivity.this).getToken(), ToolUtil.getTimeStamp(fistDay, "yyyy-MM-dd"), ToolUtil.getTimeStamp(endDay, "yyyy-MM-dd"));
                    } else if (tag) {
                        ToastUtil.showToast(context, "起始时间不能大于结束时间");
                    } else {
                        ToastUtil.showToast(context, "结束时间不能小于起始时间");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1);
        dp.show();

    }

}
