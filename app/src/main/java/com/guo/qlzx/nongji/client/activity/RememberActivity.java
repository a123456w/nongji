package com.guo.qlzx.nongji.client.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.Costom.PickTimeView;
import com.guo.qlzx.nongji.client.event.OnShowDataDialog;
import com.guo.qlzx.nongji.client.fragment.ExpendFragment;
import com.guo.qlzx.nongji.client.fragment.IncomeFragment;

import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 客户端--随手记--记一笔
 */
public class RememberActivity extends BaseActivity implements OnShowDataDialog {

    @BindView(R.id.chengke)
    TextView chengke;
    @BindView(R.id.siji)
    TextView siji;
    @BindView(R.id.main_container)
    FrameLayout mainContainer;
    private RememberActivity context;
    private int currentId;
    private ExpendFragment expendFragment;
    private IncomeFragment incomeFragment;

    @Override
    public int getContentView() {
        return R.layout.activity_remember;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleBar.setTitleText("随手记");
        context = this;
        loadingLayout.setStatus(LoadingLayout.Success);
        expendFragment = new ExpendFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, expendFragment).show(expendFragment).commit();
        expendFragment.setOnShowDataDialog(this);
        chengke.setBackgroundColor(Color.parseColor("#F5F5F5"));
        siji.setTextColor(Color.parseColor("#333333"));
        siji.setBackgroundColor(Color.parseColor("#ffffff"));
        currentId = R.id.chengke;
    }

    @Override
    public void getData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.chengke, R.id.siji})
    public void onViewClicked(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (view.getId()) {
                case R.id.chengke:
                    chengke.setBackgroundColor(Color.parseColor("#F5F5F5"));
                    siji.setTextColor(Color.parseColor("#333333"));
                    siji.setBackgroundColor(Color.parseColor("#ffffff"));
                    changeFragment(R.id.chengke, transaction);
                    currentId = R.id.chengke;
                    break;
                case R.id.siji:
                    siji.setBackgroundColor(Color.parseColor("#F5F5F5"));
                    siji.setTextColor(Color.parseColor("#333333"));
                    chengke.setBackgroundColor(Color.parseColor("#ffffff"));
                    changeFragment(R.id.siji, transaction);
                    currentId = R.id.siji;
                    break;
            }

    }

    /**
     * 改变fragment的显示
     *
     * @param resId
     */
    private void changeFragment(int resId, FragmentTransaction transaction) {
        //开启一个Fragment事务
        hideFragments(transaction);//隐藏所有fragment

        if (resId == R.id.chengke) {//主页
            expendFragment = new ExpendFragment();
            transaction.replace(R.id.main_container, expendFragment);
            expendFragment.setOnShowDataDialog(this);
        } else if (resId == R.id.siji) {//动态
            incomeFragment = new IncomeFragment();//司机
            transaction.replace(R.id.main_container, incomeFragment);
            incomeFragment.setOnShowDataDialog(this);
        }
        transaction.commit();//一定要记得提交事务
    }

    /**
     * 显示之前隐藏所有fragment
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (expendFragment != null)//不为空才隐藏,如果不判断第一次会有空指针异常
            transaction.remove(expendFragment);
        if (incomeFragment != null)
            transaction.remove(incomeFragment);
    }


    @Override
    public void onShowDialog(String data) {

            showPop(data);

    }
    private void showPop(String data){
        View  view = LayoutInflater.from(context).inflate(R.layout.data_window_layout,null);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        final PickTimeView pickDate = view.findViewById(R.id.pickDate);
        pickDate.setViewType(PickTimeView.TYPE_PICK_DATE);
        final TextView tvData = view.findViewById(R.id.tv_data);
        final PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(true);

        popupWindow.setTouchable(true);
        popupWindow.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的高
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        popupWindow.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.dialog_style);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        setBackgroundAlpha(0.5f);//设置屏幕透明度
        //设置SelectPicPopupWindow弹出窗体的背景
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(chengke, Gravity.CENTER,0,0);
        final String[] str = {data};
        SimpleDateFormat  sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
           Date date = sdfDate.parse(data);
            Calendar calend = Calendar.getInstance();
            calend.setTimeInMillis(date.getTime());
            setTvData(calend.get(Calendar.DAY_OF_WEEK),tvData,sdfDate,date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        pickDate.setOnSelectedChangeListener(new PickTimeView.onSelectedChangeListener() {
            @Override
            public void onSelected(PickTimeView view, long timeMillis) {
                SimpleDateFormat  sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calend = Calendar.getInstance();
                calend.setTimeInMillis(timeMillis);
                setTvData(calend.get(Calendar.DAY_OF_WEEK),tvData,sdfDate,timeMillis);


            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1);
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat  sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                str[0] = sdfDate.format(pickDate.getData());
                popupWindow.dismiss();
                if(Calendar.getInstance().getTimeInMillis()<pickDate.getData()){
                    ToastUtil.showToast(context,"不可大于当前时间");
                    return;
                }
                if( expendFragment.isVisible()){
                    expendFragment.setDataText(str[0]);
                }else if(incomeFragment.isVisible()){
                    incomeFragment.setDataText(str[0]);
                }

            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
       getWindow().setAttributes(lp);
    }
    private void setTvData(int i,TextView tvData,SimpleDateFormat sdfDate,long timeMillis){
        switch (i){
            case Calendar.SUNDAY:
                tvData.setText(sdfDate.format(timeMillis)+ "    "+"周日");
                break;
            case Calendar.MONDAY:
                tvData.setText(sdfDate.format(timeMillis)+ "    "+"周一");
                break;
            case Calendar.TUESDAY:
                tvData.setText(sdfDate.format(timeMillis)+ "    "+"周二");
                break;
            case Calendar.WEDNESDAY:
                tvData.setText(sdfDate.format(timeMillis)+ "    "+"周三");
                break;
            case Calendar.THURSDAY:
                tvData.setText(sdfDate.format(timeMillis)+ "    "+"周四");
                break;
            case Calendar.FRIDAY:
                tvData.setText(sdfDate.format(timeMillis)+ "    "+"周五");
                break;
            case Calendar.SATURDAY:
                tvData.setText(sdfDate.format(timeMillis)+ "    "+"周六");
                break;
        }
    }
}