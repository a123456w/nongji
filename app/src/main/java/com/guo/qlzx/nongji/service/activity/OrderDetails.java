package com.guo.qlzx.nongji.service.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.adapter.OrderDetailsNumberAdapter;
import com.guo.qlzx.nongji.service.adapter.OrderDetailsPieAdapter;
import com.guo.qlzx.nongji.service.adapter.OrderItemAdapter;
import com.guo.qlzx.nongji.service.bean.OrderDetailsBean;
import com.guo.qlzx.nongji.service.costom.MyHorizontalScrollView;
import com.guo.qlzx.nongji.utils.ColorUtils;
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
import com.qlzx.mylibrary.widget.MyLinearLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 订单详情
 */
public class OrderDetails extends BaseActivity implements MyHorizontalScrollView.OnScrollChanged {

    @BindView(R.id.but_recharge)
    Button butRecharge;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.gv_goods)
    GridView gvGoods;
    @BindView(R.id.tv_memory)
    TextView tvMemory;
    @BindView(R.id.gv_number)
    GridView gvNumber;
    @BindView(R.id.pic_sum)
    PieChart picSumChart;
    @BindView(R.id.line_sum)
    LineChart lineSumChart;
    @BindView(R.id.chart_sum)
    BarChart chartSum;
    @BindView(R.id.chart_efficiency)
    BarChart chartEfficiency;
    @BindView(R.id.chart_time)
    BarChart chartTime;
    @BindView(R.id.pic_list)
    ListView picList;
    @BindView(R.id.ll_liner)
    LinearLayout llLiner;
    @BindView(R.id.hv1)
    MyHorizontalScrollView hv1;
    @BindView(R.id.tv_chart1_top)
    TextView tvChart1Top;
    @BindView(R.id.tv_chart1_center)
    TextView tvChart1Center;
    @BindView(R.id.hv2)
    MyHorizontalScrollView hv2;
    @BindView(R.id.tv_chart2_top)
    TextView tvChart2Top;
    @BindView(R.id.tv_chart2_center)
    TextView tvChart2Center;
    @BindView(R.id.hv3)
    MyHorizontalScrollView hv3;
    @BindView(R.id.tv_chart3_top)
    TextView tvChart3Top;
    @BindView(R.id.tv_chart3_center)
    TextView tvChart3Center;
    @BindView(R.id.ll_chart1)
    LinearLayout llChart1;
    @BindView(R.id.ll_chart2)
    LinearLayout llChart2;
    @BindView(R.id.ll_chart3)
    LinearLayout llChart3;
    @BindView(R.id.ll_view)
    MyLinearLayout llView;
    private String orderId = "";
    private PreferencesHelper helper;

    private OrderItemAdapter orderItemAdapter;
    private OrderDetailsNumberAdapter adapter;

    private OrderDetailsPieAdapter pieAdapter;

    @Override
    public int getContentView() {
        return R.layout.activity_order_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("订单详情");
        orderId = getIntent().getStringExtra("ORDERID");
        helper = new PreferencesHelper(this);
        orderItemAdapter = new OrderItemAdapter(gvGoods);
        gvGoods.setAdapter(orderItemAdapter);
        adapter = new OrderDetailsNumberAdapter(gvNumber);
        gvNumber.setAdapter(adapter);

        pieAdapter = new OrderDetailsPieAdapter(picList);
        picList.setAdapter(pieAdapter);
        hv1.setOnScrollChanged(this);
        hv2.setOnScrollChanged(this);
        hv3.setOnScrollChanged(this);
        getUserType();

    }

    private void getUserType() {
        if (new PreferencesHelper(this).getUserRole().equals("2")) {
            gvGoods.setVisibility(View.GONE);
            llLiner.setVisibility(View.GONE);
        }
    }

    /**
     * 充值记录按钮
     */
    @OnClick(R.id.but_recharge)
    public void onViewClicked1() {
        //跳入充值记录
        Intent intent = new Intent(OrderDetails.this, RechargeRecordActivity.class);
        intent.putExtra("ORDERID", orderId);
        intent.putExtra("TYPE", 1);
        startActivity(intent);
    }

    @Override
    public void getData() {
        getOrderData(helper.getToken(), orderId);
    }

    private void getOrderData(String token, String id) {
        HttpHelp.getInstance().create(RemoteApi.class).getOrderDetailsData(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<OrderDetailsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<OrderDetailsBean> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            upDataUI(listBaseBean.data);
                            upDataChart(listBaseBean.data);
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.getOutLogs(OrderDetails.this);
                        } else {
                            ToastUtil.showToast(OrderDetails.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void upDataUI(OrderDetailsBean data) {
        OrderDetailsBean.OrderBean orderBean = data.getOrder();
        tvOrderId.setText("订单编号:" + orderBean.getOrder_sn());
        tvName.setText(orderBean.getUser_name());
        tvDate.setText(ToolUtil.getStrTime(orderBean.getCreate_time(), "yyyy-MM-dd"));
        tvTime.setText(ToolUtil.getStrTime(orderBean.getStart_time(), "yyyy-MM-dd") +
                "至" + ToolUtil.getStrTime(orderBean.getEnd_time(), "yyyy-MM-dd") + "(" + orderBean.getTotal_days() + "天)");
        tvMemory.setText(orderBean.getOrder_aumont());
        tvDay.setText(orderBean.getSurplus_days() + "天");
        orderItemAdapter.setData(orderBean.getOrder_mac());
        adapter.setData(data.getMac());
    }


    private List<Float> barSum = new ArrayList<>();
    private List<Float> barEfficiency = new ArrayList<>();
    private List<Float> barTime = new ArrayList<>();
    private List<String> barName = new ArrayList<>();
    private ArrayList<PieEntry> picSum = new ArrayList<>();
    private List<Entry> lineSum = new ArrayList<>();
    private ArrayList<String> lineName = new ArrayList<>();

    private void upDataChart(OrderDetailsBean data) {
        List<OrderDetailsBean.ContributionBean> picBeanSum = data.getContribution();
        List<OrderDetailsBean.TotalBean> lineBeanSum = data.getTotal();
        List<OrderDetailsBean.TotalWorkBean> barBeanSum = data.getTotal_work();
        //柱状图先行
        for (OrderDetailsBean.TotalWorkBean list : barBeanSum) {
            barSum.add(list.getBale_num());
            barEfficiency.add(list.getWork_efficiency());
            barTime.add(list.getHour_average());
            barName.add(list.getName());
        }
        for (OrderDetailsBean.ContributionBean list : picBeanSum) {
            picSum.add(new PieEntry(list.getScale1(), list.getName()));
        }
        if (null != lineBeanSum)
            for (int i = 0; i < lineBeanSum.size(); i++) {
                lineSum.add(new Entry(i, lineBeanSum.get(i).getNums()));
                lineName.add(ToolUtil.getStrTime(lineBeanSum.get(i).getTime(), "yyyy/MM/dd"));
            }
        picBeanSum.add(0, new OrderDetailsBean.ContributionBean("设备", "租金￥", "贡献%"));
        pieAdapter.setData(picBeanSum);
        ViewGroup.LayoutParams layoutParams = chartSum.getLayoutParams();
        layoutParams.width = DensityUtil.dp2px(this, 40) * (barSum.size());
        chartSum.setLayoutParams(layoutParams);
        layoutParams = chartEfficiency.getLayoutParams();
        layoutParams.width = DensityUtil.dp2px(this, 40) * (barEfficiency.size());
        chartEfficiency.setLayoutParams(layoutParams);
        layoutParams = chartTime.getLayoutParams();
        layoutParams.width = DensityUtil.dp2px(this, 40) * (barTime.size());
        chartTime.setLayoutParams(layoutParams);
        if (barName.size() == 0) {
            chartSum.setVisibility(View.GONE);
            chartEfficiency.setVisibility(View.GONE);
            chartTime.setVisibility(View.GONE);
            llChart1.setVisibility(View.GONE);
            llChart2.setVisibility(View.GONE);
            llChart3.setVisibility(View.GONE);
        } else {
            chartSum.setVisibility(View.VISIBLE);
            chartEfficiency.setVisibility(View.VISIBLE);
            chartTime.setVisibility(View.VISIBLE);
            llChart1.setVisibility(View.VISIBLE);
            llChart2.setVisibility(View.VISIBLE);
            llChart3.setVisibility(View.VISIBLE);
        }
//        if (barSum.size() != 0)
        setBarChart(chartSum, barName, barSum, "#ffd697", R.color.color_blue, tvChart1Top, tvChart1Center);
//        else   chartSum.setNoDataText("暂无数据");
//        if (barEfficiency.size() != 0)
        setBarChart(chartEfficiency, barName, barEfficiency, "#ffd697", R.color.color_blue, tvChart2Top, tvChart2Center);
//        else   chartEfficiency.setNoDataText("暂无数据");
//        if (barTime.size() != 0)
        setBarChart(chartTime, barName, barTime, "#ffd697", R.color.color_blue, tvChart3Top, tvChart3Center);
//        else   chartTime.setNoDataText("暂无数据");
        setPicChart();
//        if(lineName.size()>80){
//            for(int i = 0;i<80;){
//                if(lineName.size()>80){
//                    lineName.remove(0);
//                }else {
//                    break;
//                }
//            }
//        }
        layoutParams = lineSumChart.getLayoutParams();
        layoutParams.width = DensityUtil.dp2px(this, 20) * (lineName.size() + 1);
        setLineChart();
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
        barChart.setMaxVisibleValueCount(30);
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
        xAxis.setTextSize(13);//设置标签字体大小
        xAxis.setTextColor(Color.parseColor("#666666"));//设置标签字体大小
        xAxis.setLabelCount(xAxisValue.size());//设置标签显示的个数
//        if (R.color.my_message_item_tv_message_content_text_color != barColor)
//            xAxis.setLabelRotationAngle(-60);
        //y轴设置
        YAxis leftAxis = barChart.getAxisLeft();//获取左侧y轴
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//设置y轴标签显示在外侧
        leftAxis.setAxisMinimum(0.0f);//设置Y轴最小值
        barChart.getAxisRight().setAxisMinimum(0f);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(true);//禁止绘制y轴标签
        leftAxis.setDrawAxisLine(true);//禁止绘制y轴
        leftAxis.setLabelCount(yAxisValue.size() > 0 ? Collections.max(yAxisValue).equals("0") ? 0 : 4 : 0, false); //显示格数
        if (yAxisValue.size() == 0) {
            leftAxis.setAxisMaximum(5);
            tvChartTop.setText("");
            tvChartCenter.setText("");
        } else {
            Float max = Collections.max(yAxisValue);
            tvChartTop.setText(max + "");
            Float center = Collections.max(yAxisValue) / 2;
            tvChartCenter.setText(center + "");
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
        barChart.setScaleEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
    }

    //柱状图放置数据
    private static void setBarChartData(BarChart barChart, List<Float> yAxisValue, String color, Integer barColor) {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0, n = yAxisValue.size(); i < n; ++i) {
            entries.add(new BarEntry(i, yAxisValue.get(i)));
        }

        BarDataSet set1;

        //背景颜色
//        set1.setBarBorderColor(ContextCompat.getColor(activity, R.color.appbgcolor));
//        BarDataSet的setBarSpacePercent(50f)方法非常好

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {//不会走
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(entries);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(entries, "");

            //允许选中
            set1.setHighlightEnabled(false);
            if (barColor == null) {
                set1.setColor(Color.parseColor(color));//设置set1的柱的颜色
            } else {
//                set1.setColor(Color.parseColor("#FFC0C6"));//设置set1的柱的颜色
//                int startColor, int endColor
                set1.setGradientColor(Color.parseColor("#FFF7F8"), Color.parseColor(color));//#FFF7F8
            }

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(11);
            data.setValueTextColor(Color.parseColor("#666666"));
            data.setBarWidth(0.4f);//设置柱形图柱子的大小  柱状图宽度
            data.setValueFormatter(new MyValueFormatter());
            barChart.setData(data);

            Legend legend = barChart.getLegend();
            legend.setForm(Legend.LegendForm.NONE);
            legend.setTextColor(Color.parseColor("#ffffff"));

        }
    }

    //饼状图设置并放置数据
    private void setPicChart() {
        //设置X轴的动画
        picSumChart.animateX(1400);
        //使用百分比
        picSumChart.setUsePercentValues(false);
        //设置图列可见
        picSumChart.getLegend().setEnabled(false);
        //设置描述
        picSumChart.getDescription().setEnabled(false);
        //设置是否可转动
        picSumChart.setRotationEnabled(true);
        //设置圆孔半径
        picSumChart.setHoleRadius(20f);
        //Boolean类型    设置中心圆是否可以显示文字
        picSumChart.setDrawCenterText(false);
        //Boolean类型   设置是否隐藏饼图上文字，只显示百分比
        picSumChart.setDrawSliceText(false);
        //设置饼图没有数据时显示的文本
        picSumChart.setNoDataText("暂无数据展示");
        //设置环形图与中间空心圆之间圆环的的透明度
        picSumChart.setTransparentCircleAlpha(1);

        //饼图数据集
        PieDataSet dataset = new PieDataSet(picSum, "");
        //设置饼状图Item之间的间隙
        //dataset.setSlicespace(3f);
        //饼图Item被选中时变化的距离
        dataset.setSelectionShift(10f);
        //颜色填充

        //用 dataset.addColor(int)的方式 会出错
        int[] ints = new int[picSum.size()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = ColorUtils.getColor(i);
        }
        dataset.setColors(ints);
        //数据填充
        PieData piedata = new PieData(dataset);
        //设置饼图上显示数据的字体大小
        piedata.setValueTextSize(0f);
        picSumChart.setData(piedata);
    }

    /**
     *  折线图设置并放置数据
     */
    private void setLineChart() {
        // 不显示数据描述
        lineSumChart.getDescription().setEnabled(false);
        // 没有数据的时候，显示“暂无数据”
        lineSumChart.setNoDataText("暂无数据");
        lineSumChart.setDrawBorders(false); //在折线图上添加边框
        //lineChart.setDescription("时间/数据"); //数据描述
        lineSumChart.setDrawGridBackground(true); //表格颜色
        lineSumChart.setGridBackgroundColor(Color.WHITE); //表格的颜色，设置一个透明度
      //  lineSumChart.setTouchEnabled(false); //可点击
      //  lineSumChart.setDragDecelerationFrictionCoef(0.9f);
        //lineSumChart.setDragEnabled(false);  //可拖拽
        lineSumChart.setScaleEnabled(false);  //可缩放
        lineSumChart.setPinchZoom(false);
   //     lineSumChart.setHighlightPerDragEnabled(true);
        lineSumChart.setBackgroundColor(Color.WHITE); //设置背景颜色
      //  lineSumChart.setMaxVisibleValueCount(10);
//        float ratio = (float) lineSum.size()/(float) 6;
//        lineSumChart.zoom(ratio,1f,0,0);
        //设置背景颜色
//        lineSumChart.setBackgroundColor(ColorAndImgUtils.CHART_BACKGROUND_COLOR);
        YAxis leftAxis = lineSumChart.getAxisLeft();
        //重置所有限制线,以避免重叠线
        leftAxis.removeAllLimitLines();

        //折现图数据
        LineDataSet dataset = new LineDataSet(lineSum, "");
        //设置曲线颜色
        dataset.setColors(Color.parseColor("#ffb981"));
        dataset.setDrawFilled(true);
        dataset.setFillDrawable(getResources().getDrawable(R.drawable.order_gradient));//#FFF7F8
        dataset.setCircleColor(Color.parseColor("#ffb981"));
        // 设置平滑曲线
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        // 显示坐标点的小圆点
        dataset.setDrawCircles(true);
        // 显示坐标点的数据
        dataset.setDrawValues(false);
        // 显示定位线
        dataset.setHighlightEnabled(false);
        //数据填充
        LineData lineData = new LineData(dataset);
        //设置饼图上显示数据的字体大小
        lineData.setValueTextSize(8f);
        lineSumChart.setData(lineData);
      //  lineSumChart.setExtraBottomOffset(10);

        //继续设置
        Legend mLegend = lineSumChart.getLegend(); //设置标示，就是那个一组y的value的
        mLegend.setForm(Legend.LegendForm.NONE); //样式
        mLegend.setTextColor(Color.WHITE); //颜色
      //  lineSumChart.setVisibleXRange(1, lineName.size());   //x轴可显示的坐标范围
        IAxisValueFormatter xAxisFormatter = new StringAxisValueFormatter(lineName);//设置自定义的x轴值格式化器
        XAxis xAxis = lineSumChart.getXAxis();  //x轴的标示
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
        xAxis.setTextColor(Color.GRAY);    //字体的颜色
        xAxis.setTextSize(8f); //字体大小
        xAxis.setGridColor(Color.WHITE);//网格线颜色
        xAxis.setDrawGridLines(false); //不显示网格线
       // xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return lineName.get((int) value % lineName.size());
            }
        });
        xAxis.setLabelCount(lineName.size());//设置标签显示的个数
        //设置X轴上每个竖线是否显示
    //    xAxis.setDrawGridLines(true);
       // xAxis.setSpaceMin(1);
        //设置是否绘制X轴上的对应值(标签)
        xAxis.setDrawLabels(true);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签显示位置
        xAxis.setDrawGridLines(false);//不绘制格网线
       // xAxis.setGranularity(10f);//设置最小间隔，防止当放大时，出现重复标签。
        lineSumChart.getAxisRight().setAxisMinimum(0f);




       // xAxis.setLabelCount(lineName.size());
        // 横坐标文字
//        xAxis.setValueFormatter(xAxisFormatter);
        if (R.color.my_message_item_tv_message_content_text_color != R.color.color_blue)
            xAxis.setLabelRotationAngle(-60);
        YAxis axisLeft = lineSumChart.getAxisLeft(); //y轴左边标示
        YAxis axisRight = lineSumChart.getAxisRight(); //y轴右边标示
        axisLeft.setTextColor(Color.GRAY); //字体颜色
        axisLeft.setTextSize(8f); //字体大小
        axisLeft.setAxisMinimum(0.1f);
        //axisLeft.setAxisMaxValue(800f); //最大值
        axisLeft.setLabelCount(lineSum.size(), true); //显示格数
        axisLeft.setGridColor(Color.GRAY); //网格线颜色
        axisLeft.setLabelCount(3, false); //显示格数
//        if (axisLeft.size() == 0 || Double.parseDouble(Collections.max(yAxisValue)) < 5) {
//            axisLeft.setAxisMaximum(5);
//        }

        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

        //设置动画效果
        lineSumChart.animateY(2000, Easing.EasingOption.Linear);
        lineSumChart.animateX(2000, Easing.EasingOption.Linear);
        lineSumChart.invalidate();


    }


    @Override
    public void onScroll(int l, int t, int oldl, int oldt, View view) {
        switch (view.getId()) {
            case R.id.hv1:
                hv3.scrollTo(l, t);
                hv2.scrollTo(l, t);
                break;
            case R.id.hv2:
                hv1.scrollTo(l, t);
                hv3.scrollTo(l, t);
                break;
            case R.id.hv3:
                hv1.scrollTo(l, t);
                hv2.scrollTo(l, t);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
