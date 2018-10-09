package com.guo.qlzx.nongji.service.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.StatisticsBean;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.costom.MyHorizontalScrollView;
import com.guo.qlzx.nongji.utils.MyValueFormatter;
import com.guo.qlzx.nongji.utils.StringAxisValueFormatter;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/5/30.
 * 日志-右上角-统计
 */

public class StatisticsActivity extends BaseActivity {

    @BindView(R.id.tv_period)
    TextView tvPeriod;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_efficiency)
    TextView tvEfficiency;
    @BindView(R.id.chart1)
    BarChart chart1;
    @BindView(R.id.chart2)
    BarChart chart2;
    @BindView(R.id.chart3)
    BarChart chart3;
    @BindView(R.id.tv_end_period)
    TextView tvEndPeriod;
    @BindView(R.id.tv_chart1_top)
    TextView tvChart1Top;
    @BindView(R.id.tv_chart1_center)
    TextView tvChart1Center;
    @BindView(R.id.tv_chart2_top)
    TextView tvChart2Top;
    @BindView(R.id.tv_chart2_center)
    TextView tvChart2Center;
    @BindView(R.id.tv_chart3_top)
    TextView tvChart3Top;
    @BindView(R.id.tv_chart3_center)
    TextView tvChart3Center;
    @BindView(R.id.hv1)
    MyHorizontalScrollView hv1;
    @BindView(R.id.hv2)
    MyHorizontalScrollView hv2;
    @BindView(R.id.hv3)
    MyHorizontalScrollView hv3;
    private String startTime = "";
    private String endTime = "";
    private DatePickerDialog dp;
    private StatisticsBean data;
    private List<String> xAxisValue = new ArrayList<>();
    private List<Float> yAxisValue = new ArrayList<>();
    private List<Float> yAxisValue2 = new ArrayList<>();
    private List<Float> yAxisValue3 = new ArrayList<>();
    private String endDate = "";
    private PreferencesHelper helper;

    @Override
    public int getContentView() {
        return R.layout.activity_statistics;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("合计作业统计");
        helper = new PreferencesHelper(this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        endDate = simpleDateFormat.format(date);
        hv1.setOnScrollChanged(new MyHorizontalScrollView.OnScrollChanged() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt,View view) {
                hv2.scrollTo(l, t);
                hv3.scrollTo(l, t);
            }
        });
        hv2.setOnScrollChanged(new MyHorizontalScrollView.OnScrollChanged() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt,View view) {
                hv1.scrollTo(l, t);
                hv3.scrollTo(l, t);
            }
        });
        hv3.setOnScrollChanged(new MyHorizontalScrollView.OnScrollChanged() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt,View view) {
                hv1.scrollTo(l, t);
                hv2.scrollTo(l, t);
            }
        });
    }

    @OnClick({R.id.tv_period, R.id.tv_end_period})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_period:
                addDialog(true);
                break;
            case R.id.tv_end_period:
                addDialog(false);
                break;
        }
    }

    @Override
    public void getData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        endTime = simpleDateFormat.format(Calendar.getInstance().getTimeInMillis());
        Date date = null;
        try {
            date = simpleDateFormat.parse(endTime);
            date.setDate(01);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startTime = simpleDateFormat.format(date);
        tvPeriod.setText(startTime);
        tvEndPeriod.setText(" - " + endTime);
        getStaticsData(helper.getToken(), String.valueOf(ToolUtil.getTimeStamp(startTime, "yyyy年MM月dd日")), String.valueOf(ToolUtil.getTimeStamp(endTime, "yyyy年MM月dd日")));
    }

    /**
     * create by xuxx 2018-06-21 日期选择弹框
     */
    private void addDialog(final Boolean tag) {
        dp = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int iyear, int monthOfYear, int dayOfMonth) {
                int year = datePicker.getYear();//年
                int month = datePicker.getMonth();//月-1
                int dayOfMonth1 = datePicker.getDayOfMonth();//日*
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                try {
                    Date date = simpleDateFormat.parse(endTime);
                    Date startDate = simpleDateFormat.parse(startTime);
                    simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date pickerDate = simpleDateFormat.parse(year + "-" + (month + 1) + "-" + dayOfMonth1);
                    if (pickerDate.getTime() > Calendar.getInstance().getTime().getTime()) {
                        ToastUtil.showToast(context, "不能大于当前时间");
                        return;
                    }
                    if (compareDay(pickerDate.getTime(), date.getTime(), tag) && tag ? true : startDate.getTime() < pickerDate.getTime()) {


                        if (tag) {
                            startTime = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                            tvPeriod.setText(startTime);
                        } else {
                            endTime = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                            tvEndPeriod.setText("——" + endTime);
                        }
                        getStaticsData(helper.getToken(), String.valueOf(ToolUtil.getTimeStamp(startTime, "yyyy年MM月dd日")), String.valueOf(ToolUtil.getTimeStamp(endTime, "yyyy年MM月dd日")));

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

    /**
     * create by xuxx 2018-06-21 时间对比
     */
    private boolean compareDay(long fist, long end, boolean tag) {
        boolean b = false;
        if (tag ? fist <= end : fist <= Calendar.getInstance().getTime().getTime()) {
            b = true;
        }
        return b;
    }

    private void getStaticsData(String token, String start, String end) {
        HttpHelp.getInstance().create(RemoteApi.class).statistics(token, start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<StatisticsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<StatisticsBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            data = listBaseBean.data;

                            tvCount.setText("作业总量：" + data.getTotal_nums() + "捆");
                            tvDay.setText("总作业天数：" + data.getTotal_days() + "天");
                            tvTime.setText("工作时间：" + data.getWork_hours() + "小时/天");
                            tvEfficiency.setText("工作效率：" + data.getEfficiency() + "捆/小时");
                            if (data.getStart_time() != null && !"".equals(data.getStart_time())) {
                                tvPeriod.setText(ToolUtil.getStrTime(data.getStart_time(), "yyyy年MM月dd日"));
                                tvEndPeriod.setText("——" + endTime);
                            }
                            xAxisValue.clear();
                            yAxisValue.clear();
                            yAxisValue2.clear();
                            yAxisValue3.clear();
                            //图表
                            if (null != data.getData()) {
                                for (int i = 0; i < data.getData().size(); i++) {
                                    xAxisValue.add((new SimpleDateFormat("yyyy-MM-dd").format(new Date((data.getData().get(i).getTime() * 1000)))));
                                    yAxisValue.add(data.getData().get(i).getNums());
                                    yAxisValue2.add(data.getData().get(i).getHours());
                                    yAxisValue3.add(data.getData().get(i).getEfficiency_hour());
                                }
                                ViewGroup.LayoutParams layoutParams = chart1.getLayoutParams();
                                layoutParams.width = DensityUtil.dp2px(StatisticsActivity.this, 30) * (xAxisValue.size());
                                chart1.setLayoutParams(layoutParams);
                                chart1.clear();
                                if(xAxisValue.size() ==0){
                                    chart1.setVisibility(View.GONE);
                                    chart2.setVisibility(View.GONE);
                                    chart3.setVisibility(View.GONE);
                                }else {
                                    chart1.setVisibility(View.VISIBLE);
                                    chart2.setVisibility(View.VISIBLE);
                                    chart3.setVisibility(View.VISIBLE);
                                }
                                setBarChart(chart1, xAxisValue, yAxisValue, "title", R.color.color_blue, tvChart1Top, tvChart1Center);


                                layoutParams = chart2.getLayoutParams();
                                layoutParams.width = DensityUtil.dp2px(StatisticsActivity.this, 30) * (yAxisValue2.size());
                                chart2.setLayoutParams(layoutParams);
                                chart2.clear();

                                setBarChart(chart2, xAxisValue, yAxisValue2, "title", R.color.color_blue, tvChart2Top, tvChart2Center);


                                layoutParams = chart3.getLayoutParams();
                                layoutParams.width = DensityUtil.dp2px(StatisticsActivity.this, 30) * (yAxisValue3.size());
                                chart3.setLayoutParams(layoutParams);
                                chart3.clear();

                                setBarChart(chart3, xAxisValue, yAxisValue3, "title", R.color.color_blue, tvChart3Top, tvChart3Center);

                            }//for结束
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.getOutLogs(StatisticsActivity.this);
                        } else {
                            tvCount.setText("作业总量：" + 0 + "捆");
                            tvDay.setText("总作业天数：" + 0 + "天");
                            tvTime.setText("工作时间：" + 0 + "小时/天");
                            tvEfficiency.setText("工作效率：" + 0 + "捆/小时");
                            ToastUtil.showToast(StatisticsActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 单数据集。设置柱状图样式，X轴为字符串，Y轴为数值
     *
     * @param barChart
     * @param xAxisValue
     * @param yAxisValue
     * @param title      图例文字
     * @param barColor
     */
    public void setBarChart(BarChart barChart, List<String> xAxisValue, List<Float> yAxisValue, String title, Integer barColor, TextView tvChartTop, TextView tvChartCenter) {
        barChart.getDescription().setEnabled(false);//设置描述
        barChart.setPinchZoom(false);//设置按比例放缩柱状图
        barChart.setMaxVisibleValueCount(60);
        barChart.setLogEnabled(false);
        barChart.setMinimumHeight(0);
        barChart.setNoDataText("暂无数据");
        //设置自定义的markerView
//        MPChartMarkerView markerView = new MPChartMarkerView(barChart.getContext(), R.layout.custom_marker_view);
//        barChart.setMarker(markerView);

        //x坐标轴设置
        IAxisValueFormatter xAxisFormatter = new StringAxisValueFormatter(xAxisValue);//设置自定义的x轴值格式化器
        XAxis xAxis = barChart.getXAxis();//获取x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签显示位置
        xAxis.setDrawGridLines(false);//不绘制格网线
        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setTextSize(8);//设置标签字体大小
        xAxis.setTextColor(Color.parseColor("#666666"));//设置标签字体大小
        xAxis.setLabelCount(xAxisValue.size());//设置标签显示的个数
        if (R.color.my_message_item_tv_message_content_text_color != barColor)
            xAxis.setLabelRotationAngle(-60);
        //y轴设置
        YAxis leftAxis = barChart.getAxisLeft();//获取左侧y轴
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//设置y轴标签显示在外侧
        leftAxis.setAxisMinimum(0.0f);//设置Y轴最小值
        barChart.getAxisRight().setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(true);//禁止绘制y轴标签
        leftAxis.setDrawAxisLine(true);//禁止绘制y轴
        leftAxis.setLabelCount(yAxisValue.size() > 0 ? Collections.max(yAxisValue).equals("0") ? 0 : 4 : 0, false); //显示格数
        if (yAxisValue.size() == 0 || Collections.max(yAxisValue) == 0) {
            leftAxis.setAxisMaximum(5);
        } else {
            Float max = Collections.max(yAxisValue);
            int i = ((int) max.intValue());
            tvChartTop.setText((max < 0 ? max : i) + "");
            Float center = Collections.max(yAxisValue) / 2;
            i = (int) center.intValue();
            tvChartCenter.setText((center) + "");
            leftAxis.setAxisMaximum(Collections.max(yAxisValue));
        }
        leftAxis.setAxisLineColor(Color.parseColor("#000000"));//y轴线颜色
        leftAxis.setTextColor(Color.parseColor("#65859a"));//y轴字体颜色
        leftAxis.setSpaceBottom(0.2f);

        barChart.getAxisRight().setEnabled(false);//禁用右侧y轴
        barChart.getAxisLeft().setEnabled(false);//禁用左侧y轴
        //设置柱状图数据
        setBarChartData(barChart, yAxisValue, title, barColor);

        barChart.setExtraBottomOffset(1);//距视图窗口底部的偏移，类似与paddingbottom
        barChart.setExtraTopOffset(0);//距视图窗口顶部的偏移，类似与paddingtop
        barChart.setFitBars(false);//使两侧的柱图完全显示
        //barChart.animateX(1500);//数据显示动画，从左往右依次显示
        barChart.setBackgroundColor(Color.parseColor("#FFFFFF"));//背景颜色

    }

    /**
     * 设置柱图
     *
     * @param barChart
     * @param yAxisValue
     * @param title
     * @param barColor
     */
    private void setBarChartData(final BarChart barChart, List<Float> yAxisValue, String title, Integer barColor) {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0, n = yAxisValue.size(); i < n; ++i) {
            entries.add(new BarEntry(i, yAxisValue.get(i)));
        }

        BarDataSet set1;

        //背景颜色
//        set1.setBarBorderColor(ContextCompat.getColor(activity, R.color.appbgcolor));
//        BarDataSet的setBarSpacePercent(50f)方法非常好


        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(entries);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(entries, "");

            //允许选中
            // set1.setHighlightEnabled(true);
            //          if (barColor == null) {
            set1.setColor(Color.parseColor("#FFdde0"));//设置set1的柱的颜色
//            } else {
////                set1.setColor(Color.parseColor("#FFC0C6"));//设置set1的柱的颜色   #EDEBEB     #FBFAFA
//                set1.setGradientColor(Color.parseColor("#FBFAFA"), Color.parseColor("#ffefd4"));//#FFF7F8
//            }

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData barData = new BarData(dataSets);
            barData.setValueTextSize(7);
            barData.setValueTextColor(Color.parseColor("#999999"));
            barData.setBarWidth(0.7f);//设置柱形图柱子的大小  柱状图宽度
            barData.setValueFormatter(new MyValueFormatter());
            barChart.setData(barData);
            barChart.getData().setHighlightEnabled(
                    !barChart.getData().isHighlightEnabled());
            barChart.setScaleEnabled(false);

            Legend legend = barChart.getLegend();
            legend.setForm(Legend.LegendForm.NONE);
            legend.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
