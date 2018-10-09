package com.guo.qlzx.nongji.service.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.cretin.www.externalmaputilslibrary.OpenExternalMapAppUtils;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.activity.WebActivity;
import com.guo.qlzx.nongji.client.adapter.HomeArticleListAdapter;
import com.guo.qlzx.nongji.client.bean.ArticleListBean;
import com.guo.qlzx.nongji.client.bean.MachineInfo;
import com.guo.qlzx.nongji.client.bean.MapIndex;
import com.guo.qlzx.nongji.client.bean.WorkIndexBean;
import com.guo.qlzx.nongji.commen.activity.LoginActivity;
import com.guo.qlzx.nongji.commen.application.MyApplication;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogRecordDetailsActivity;
import com.guo.qlzx.nongji.service.activity.MessageActivity;
import com.guo.qlzx.nongji.service.activity.PersonalCenterActivity;
import com.guo.qlzx.nongji.service.bean.MessageListBean;
import com.guo.qlzx.nongji.service.costom.MyHorizontalScrollView;
import com.guo.qlzx.nongji.utils.MyValueFormatter;
import com.guo.qlzx.nongji.utils.ScreenUtil;
import com.guo.qlzx.nongji.utils.StringAxisValueFormatter;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.StringUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.yinglan.scrolllayout.ScrollLayout;
import com.yinglan.scrolllayout.content.ContentScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页
 * 胡亚婷 6-9 下午02:00:40
 * 服务端：17610084321         111111
 * 胡亚婷 6-9 下午02:00:54
 * 客户端  17316085996           123456
 * <p>
 * 服务端  17610084321
 */
public class HomePageFragment extends BaseFragment implements AMap.OnMarkerClickListener, RadioGroup.OnCheckedChangeListener, MyHorizontalScrollView.OnScrollChanged {
    @BindView(R.id.map)
    MapView map;
    Unbinder unbinder;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_shop_business)
    TextView tvShopBusiness;
    @BindView(R.id.chart1)
    BarChart chart1;
    @BindView(R.id.hv1)
    MyHorizontalScrollView hv1;
    @BindView(R.id.chart2)
    BarChart chart2;
    @BindView(R.id.hv2)
    MyHorizontalScrollView hv2;
    @BindView(R.id.chart3)
    BarChart chart3;
    @BindView(R.id.hv3)
    MyHorizontalScrollView hv3;

    @BindView(R.id.chart1_two)
    BarChart chart1Two;
    @BindView(R.id.hv1_two)
    MyHorizontalScrollView hv1Two;
    @BindView(R.id.chart2_two)
    BarChart chart2Two;
    @BindView(R.id.hv2_two)
    MyHorizontalScrollView hv2Two;
    @BindView(R.id.chart3_two)
    BarChart chart3Two;
    @BindView(R.id.hv3_two)
    MyHorizontalScrollView hv3Two;

    @BindView(R.id.wb_info)
    WebView wbInfo;
    /*@BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_last_money)
    TextView tvLastMoney;
    @BindView(R.id.tv_last_money_num)
    TextView tvLastMoneyNum;
    @BindView(R.id.iv_time)
    ImageView ivTime;
    @BindView(R.id.tv_last_day)
    TextView tvLastDay;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
    @BindView(R.id.tv_total_bale_num)
    TextView tvTotalBaleNum;
    @BindView(R.id.ll_title)*/
    LinearLayout llTitle;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.content_scroll_layout)
    ContentScrollView contentScrollLayout;
    @BindView(R.id.scroll_down_layout)
    ScrollLayout scrollDownLayout;
    @BindView(R.id.im_let)
    ImageView imLet;
    @BindView(R.id.im_rig)
    ImageView imRig;
    @BindView(R.id.rl_select)
    RelativeLayout rlSelect;
    @BindView(R.id.rg_radio)
    RadioGroup rgRadio;

    @BindView(R.id.im_mess)
    ImageView imMess;
    @BindView(R.id.rb_all)
    RadioButton rbAll;
    @BindView(R.id.rb_Baling)
    RadioButton rbBaling;
    @BindView(R.id.rv_view)
    RecyclerView rvView;
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
    @BindView(R.id.tv_chart1_two_top)
    TextView tvChart1TwoTop;
    @BindView(R.id.tv_chart1_two_center)
    TextView tvChart1TwoCenter;
    @BindView(R.id.tv_chart2_two_top)
    TextView tvChart2TwoTop;
    @BindView(R.id.tv_chart2_two_center)
    TextView tvChart2TwoCenter;
    @BindView(R.id.tv_chart3_two_top)
    TextView tvChart3TwoTop;
    @BindView(R.id.tv_chart3_two_center)
    TextView tvChart3TwoCenter;
    private PreferencesHelper helper;
    private AMap aMap;
    private AMapLocationClient mLocationClient;
    private Marker currentMarker;
    private PreferencesHelper preferencesHelper;
    private List<MapIndex> data;
    private TextView tv_content;
    private View rl_title;
    private View rl_into_gaode_map;
    private String token;

    private long startTime = 0;
    private long endTime = 0;
    private TextView tv_here;
    Runnable runnable;
    private Boolean tag;
    private Boolean isTouch = true;
    @BindView(R.id.ll_chart1)
    LinearLayout llChart1;
    @BindView(R.id.ll_chart2)
    LinearLayout llChart2;
    @BindView(R.id.ll_chart3)
    LinearLayout llChart3;
    //    @BindView(R.id.hv)
//    MyHorizontalScrollView hv;
    @BindView(R.id.ll_chart1_two)
    LinearLayout llChart1Two;
    @BindView(R.id.ll_chart2_two)
    LinearLayout llChart2Two;
    @BindView(R.id.ll_chart3_two)
    LinearLayout llChart3Two;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_home_page_fragment, frameLayout, false);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean hidden) {
        super.setUserVisibleHint(hidden);
        if (hidden && runnable != null)
            MyApplication.getMainHandler().postDelayed(runnable, 30000);
        else if (!hidden) {
            MyApplication.getMainHandler().removeCallbacks(runnable);
        }
    }

    /*
     * 文章列表接口
     * */
    private void getArticleListData() {
        final HomeArticleListAdapter adapter = new HomeArticleListAdapter(rvView);
        rvView.setFocusable(false);
        rvView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //添加Android自带的分割线
        rvView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvView.setAdapter(adapter);
        HttpHelp.getInstance().create(RemoteApi.class).articleList("1", 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ArticleListBean>>>(getActivity(), null) {
                    @Override
                    public void onNext(final BaseBean<List<ArticleListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0 && listBaseBean.data.size() != 0) {
                            adapter.setData(listBaseBean.data);
                            adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(ViewGroup parent, View childView, int position) {
                                    WebActivity.startActivity(mContext, "详情", listBaseBean.data.get(position).getUrl());
                                }
                            });
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }

    /**
     * 轮询更新数据
     * 30秒更新
     * create by xuxx
     */
    private void getPollingData() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (!tag) {
                    return;
                }
                MyApplication.getMainHandler().postDelayed(this, 30000);
                getMapData();
                homeChartData(token);
            }
        };
        MyApplication.getMainHandler().postDelayed(runnable, 30000);
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(mContext);
        imMess.setVisibility(View.GONE);
        titleBar.setTitleText("首页");
        titleBar.setRightImageRes(R.drawable.ic_xiaoxi);
        titleBar.setLeftImageRes(R.drawable.ic_geren);
        imLet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PersonalCenterActivity.class));
            }
        });
        imRig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MessageActivity.class));
                imMess.setVisibility(View.GONE);
            }
        });
        titleBar.setVisibility(View.GONE);

//        hv.setOnScrollChanged(new MyHorizontalScrollView.OnScrollChanged() {
//            @Override
//            public void onScroll(int l, int t, int oldl, int oldt,View view) {
//                hv1.smoothScrollTo(l, t);
//                hv2.smoothScrollTo(l, t);
//                hv3.smoothScrollTo(l, t);
//            }
//        });
        contentScrollLayout.setVisibility(View.GONE);
        contentScrollLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                contentScrollLayout.setVisibility(View.VISIBLE);
            }
        }, 500);
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                isTouch = false;
            }
        });
//        new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
////                if (!isHv2Scroll) {
////                    isHv1Scroll = true;
//                hv1.smoothScrollTo(i, i1);
//                hv2.smoothScrollTo(i, i1);
//                hv3.smoothScrollTo(i, i1);
////                    isHv1Scroll = false;
////                }
//
//            }
        rgRadio.setOnCheckedChangeListener(this);
        //设置webview的屏幕适配
        WebSettings settings = wbInfo.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);//开启DOM
        settings.getJavaScriptCanOpenWindowsAutomatically();
        wbInfo.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript: document.getElementsByClassName(\"iosHeader\")[0].style.display=\"none\";" +
                        "document.getElementsByClassName(\"iosText\")[0].style.paddingTop=\"0\";void(0)");
            }
        });

        wbInfo.loadUrl("http://test.qlzhx.cn/nongji/Api/Article/article/type/1");
        // llTitle.setVisibility(View.GONE);
        hv1.setOnScrollChanged(this);
        hv2.setOnScrollChanged(this);
        hv3.setOnScrollChanged(this);
        hv1Two.setOnScrollChanged(this);
        hv2Two.setOnScrollChanged(this);
        hv3Two.setOnScrollChanged(this);

        /**设置 setting*/
        scrollDownLayout.setMinOffset(0);
        scrollDownLayout.setMaxOffset((int) (ScreenUtil.getScreenHeight((Activity) mContext) * 0.5));
        scrollDownLayout.setExitOffset(ScreenUtil.dip2px(mContext, 160));
        scrollDownLayout.setOnScrollChangedListener(mOnScrollChangedListener);
        scrollDownLayout.setVerticalScrollBarEnabled(false);
        scrollDownLayout.setHorizontalScrollBarEnabled(false);
        //scrollDownLayout.setToExit();
        //scrollDownLayout.getBackground().setAlpha(0);
    }

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= 0) {
                float precent = 255 * currentProgress;
                if (precent > 255) {
                    precent = 255;
                } else if (precent < 0) {
                    precent = 0;
                }
                int i = (int) (ScreenUtil.getScreenHeight((Activity) mContext));
                //float ii=(i/(255 - (int) precent))*precent;
                //scrollDownLayout.setMaxOffset((int)ii );
                //scrollDownLayout.getBackground().setAlpha(255 - (int) precent);
            }
        }

        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if (currentStatus.equals(ScrollLayout.Status.EXIT)) {
                //当布局退出时
            }
        }

        @Override
        public void onChildScroll(int top) {
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        tag = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        tag = true;
        getPollingData();
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
    public void setBarChart(BarChart barChart, final List<String> xAxisValue, List<Float> yAxisValue, String title, Integer barColor, TextView tvChartTop, TextView tvChartCenter, LinearLayout linearLayout) {
        linearLayout.setVisibility(View.VISIBLE);
//        barChart.notifyDataSetChanged();
//        barChart.invalidate();
        barChart.setVisibility(View.VISIBLE);
        barChart.getDescription().setEnabled(false);//设置描述
        barChart.setPinchZoom(false);//设置按比例放缩柱状图
        //设置自定义的markerView
//        MPChartMarkerView markerView = new MPChartMarkerView(barChart.getContext(), R.layout.custom_marker_view);
//        barChart.setMarker(markerView);
        barChart.setLogEnabled(false);
        barChart.setMinimumHeight(0);
        //     barChart.setNoDataText("暂无数据");
        //x坐标轴设置
        final IAxisValueFormatter xAxisFormatter = new StringAxisValueFormatter(xAxisValue);//设置自定义的x轴值格式化器
        XAxis xAxis = barChart.getXAxis();//获取x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签显示位置
        xAxis.setDrawGridLines(false);//不绘制格网线
        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。
        barChart.getAxisRight().setAxisMinimum(0f);
        xAxis.setDrawLabels(true);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
                                     @Override
                                    public String getFormattedValue(float value, AxisBase axis) {
                                        return xAxisValue.get((int) value % xAxisValue.size());
                                    }
                                });


                xAxis.setTextSize(7);//设置标签字体大小
        xAxis.setTextColor(Color.parseColor("#666666"));//设置标签字体大小

        xAxis.setLabelCount(xAxisValue.size(), false);//设置标签显示的个数

        //y轴设置
        YAxis leftAxis = barChart.getAxisLeft();//获取左侧y轴
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//设置y轴标签显示在外侧
        //  leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(true);//禁止绘制y轴标签
        leftAxis.setDrawAxisLine(true);//禁止绘制y轴

        barChart.getAxisRight().setAxisMinimum(0f);
        leftAxis.setLabelCount(yAxisValue.size() > 0 ? (Collections.max(yAxisValue).equals("0") ? 0 : 4) : 0, false); //显示格数
        if (yAxisValue.size() == 0 || Collections.max(yAxisValue) < 5) {
            leftAxis.setAxisMaximum(5);
            tvChartTop.setText("5.0");
            tvChartCenter.setText("2.5");
        } else {
            Float max = Collections.max(yAxisValue);
            int i = ((int) max.intValue());
            tvChartTop.setText((max <= 1 ? max : i) + "");
            Float center = Collections.max(yAxisValue) / 2;
            tvChartCenter.setText(center + "");
            leftAxis.setAxisMaximum(max);
        }
        leftAxis.setAxisMinimum(0.01f);//设置Y轴最小值
        leftAxis.setAxisLineColor(Color.parseColor("#000000"));//y轴线颜色
        leftAxis.setTextColor(Color.parseColor("#65859a"));//y轴字体颜色
        leftAxis.setSpaceBottom(0.2f);
        //  leftAxis.setDrawZeroLine(true);
        barChart.getAxisRight().setEnabled(false);//禁用右侧y轴
        barChart.getAxisLeft().setEnabled(false);//禁用左侧y轴
        leftAxis.setAxisLineColor(Color.parseColor("#000000"));//y轴线颜色
        leftAxis.setTextColor(Color.parseColor("#65859a"));//y轴字体颜色

        barChart.getAxisRight().setEnabled(false);//禁用右侧y轴

        //设置柱状图数据
        setBarChartData(barChart, yAxisValue, title, barColor);

        barChart.setExtraBottomOffset(2);//距视图窗口底部的偏移，类似与paddingbottom
        barChart.setExtraTopOffset(0);//距视图窗口顶部的偏移，类似与paddingtop
        barChart.setFitBars(false);//使两侧的柱图完全显示
        barChart.animateXY(0, 1500);//数据显示动画，从左往右依次显示
        barChart.setScaleEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
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
    private static void setBarChartData(BarChart barChart, List<Float> yAxisValue, String title, Integer barColor) {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < yAxisValue.size(); i++) {
            entries.add(new BarEntry(i, yAxisValue.get(i)));
        }

        BarDataSet set1;

        //背景颜色
//        set1.setBarBorderColor(ContextCompat.getColor(activity, R.color.appbgcolor));
//        BarDataSet的setBarSpacePercent(50f)方法非常好

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {//不会走
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            // set1.clear();
            set1.setValues(entries);
//            barChart.getData().notifyDataChanged();
//            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(entries, "");

            //允许选中
            set1.setHighlightEnabled(false);
            if (barColor == R.color.gray) {
                set1.setGradientColor(Color.parseColor("#ffffff"), Color.parseColor("#FFdde0"));//设置set1的柱的颜色
            } else {
//                set1.setColor(Color.parseColor("#FFC0C6"));//设置set1的柱的颜色
//                int startColor, int endColor
                set1.setGradientColor(Color.parseColor("#FBFAFA"), Color.parseColor("#ffefd4"));//#FFF7F8
            }

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(11);
            data.setValueTextColor(Color.parseColor("#666666"));
            data.setBarWidth(0.7f);//设置柱形图柱子的大小  柱状图宽度
            data.setValueFormatter(new MyValueFormatter());
            barChart.setData(data);

            Legend legend = barChart.getLegend();
            legend.setForm(Legend.LegendForm.NONE);
            legend.setTextColor(Color.parseColor("#ffffff"));

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (map != null)
            map.onDestroy();

        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        if (runnable != null)
            MyApplication.getMainHandler().removeCallbacks(runnable);
        mLocationListener = null;
        mLocationClient = null;
    }

    @Override
    public void getData() {
//        LatLng latLng = new LatLng(39.903409, 116.427258);
//        initMap("39.903409", "116.427258", null);

        getMapData();
        homeChartData(token);

    }

    private void getMapData() {
        HttpHelp.getInstance().create(RemoteApi.class).mapIndex(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<MapIndex>>>(getActivity(), null) {
                    @Override
                    public void onNext(BaseBean<List<MapIndex>> investorBeanBaseBean) {
                        super.onNext(investorBeanBaseBean);

                        if (investorBeanBaseBean.code == 0 && (rgRadio.getCheckedRadioButtonId() == rbAll.getId() || rgRadio.getCheckedRadioButtonId() == rbBaling.getId())) {
                            data = investorBeanBaseBean.data;
                            if (aMap.getMapScreenMarkers() != null) {
                                List<Marker> list = aMap.getMapScreenMarkers();
                                for (Marker marker : list) {
                                    marker.remove();
                                    marker.destroy();
                                    marker = null;
                                }
                            }
                            if (null != data && data.size() != 0) {
                                LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度


                                for (int i = 0; i < data.size(); i++) {
                                    MapIndex mapIndex = data.get(i);
                                    MapIndex.LastCoordinateBean last_coordinate = mapIndex.getLast_coordinate();
                                    if (!StringUtil.isEmpty(last_coordinate.getLat())) {
                                        if (!(Double.parseDouble(last_coordinate.getLat()) <= 1 || Double.parseDouble(last_coordinate.getLng()) <= 1)) {
                                            initMap(last_coordinate.getLat(), last_coordinate.getLng(), mapIndex, i);
                                            LatLng latLng = new LatLng(Double.parseDouble(last_coordinate.getLat()), Double.parseDouble(last_coordinate.getLng()));
                                            boundsBuilder.include(latLng);//把所有点都include进去（LatLng类型）
                                        }

                                    }

                                }
                                if (isTouch)
                                    aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 200));//第二个参数为四周留空宽度
                            }
                        } else if (investorBeanBaseBean.code == 4) {
                            ToolUtil.getOutLogs(mContext);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    /**
     * 重新登录AlertDialog弹框
     */
    private void getOutLogs() {
        final Dialog bottomDialog = new Dialog(getActivity(), R.style.BottomDialog);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_write_logs, null);
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
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }

    private void homeChartData(String token) {
        //    首页数据统计内容
        HttpHelp.getInstance().create(RemoteApi.class).workIndex(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<WorkIndexBean>>(getActivity(), null) {
                    @Override
                    public void onNext(BaseBean<WorkIndexBean> investorBeanBaseBean) {
                        super.onNext(investorBeanBaseBean);

                        if (investorBeanBaseBean.code == 0) {
                            WorkIndexBean data1 = investorBeanBaseBean.data;
                            xAxisValue.clear();
                            baleNumData.clear();
                            efficiencyData.clear();
                            workHourData.clear();
                            zuoye_zongkunshu.clear();
                            pinjun_zuoye_xiaolv.clear();
                            pinjun_zuoye_shijian.clear();
                            if (null != data1) {
                                List<WorkIndexBean.DayWorkBean> day_work = data1.getDay_work();
                                List<WorkIndexBean.TotalWorkBean> totalWorkBean = data1.getTotal_work();

                                if (null != day_work) {
//                                    WorkIndexBean.DayWorkBean dayWorkBean = day_work.get(i);
                                    for (int i = 0; i < day_work.size(); i++) {
                                        WorkIndexBean.DayWorkBean dayWorkBean = day_work.get(i);

                                        String sn = dayWorkBean.getName();
                                        xAxisValue.add(sn == null ? "0" : sn);

                                        Float bale_num = dayWorkBean.getBale_num();//当日作业量（捆）
                                        baleNumData.add((bale_num));

                                        Float efficiency = dayWorkBean.getEfficiency();//当日作业效率（捆/小时）
                                        efficiencyData.add((efficiency));

                                        Float work_hour = dayWorkBean.getWork_hour();//当日作业时长（小时）
                                        workHourData.add((work_hour));
                                    }
//                                    if(totalWorkBean.size() == 0){
//                                        scrollDownLayout.setVisibility(View.GONE);
//                                        scrollDownLayout.setTag(View.GONE);
//                                    }else {
//                                        scrollDownLayout.setVisibility(View.VISIBLE);
//                                        scrollDownLayout.setTag(View.VISIBLE);
//                                    }
                                    for (int i = 0; i < totalWorkBean.size(); i++) {
                                        WorkIndexBean.TotalWorkBean dayWorkBean = totalWorkBean.get(i);

                                        Float bale_num = dayWorkBean.getBale_num();//作业总捆数（捆）
                                        zuoye_zongkunshu.add((bale_num));

                                        Float work_efficiency = dayWorkBean.getWork_efficiency();//平均作业效率（捆/天）
                                        pinjun_zuoye_xiaolv.add(work_efficiency);

                                        Float hour_average = dayWorkBean.getHour_average();//平均作业时间（小时/天）
                                        pinjun_zuoye_shijian.add(hour_average);
                                    }//for结束

                                    ViewGroup.LayoutParams layoutParams = chart1.getLayoutParams();
                                    layoutParams.width = DensityUtil.dp2px(getContext(), 25) * (baleNumData.size());
                                    chart1.setLayoutParams(layoutParams);

                                    layoutParams = chart2.getLayoutParams();
                                    layoutParams.width = DensityUtil.dp2px(getContext(), 25) * (efficiencyData.size());
                                    chart2.setLayoutParams(layoutParams);

                                    layoutParams = chart3.getLayoutParams();
                                    layoutParams.width = DensityUtil.dp2px(getContext(), 25) * (workHourData.size());
                                    chart3.setLayoutParams(layoutParams);

                                    layoutParams = chart1Two.getLayoutParams();
                                    layoutParams.width = DensityUtil.dp2px(getContext(), 25) * (zuoye_zongkunshu.size());
                                    chart1Two.setLayoutParams(layoutParams);

                                    layoutParams = chart2Two.getLayoutParams();
                                    layoutParams.width = DensityUtil.dp2px(getContext(), 25) * (pinjun_zuoye_xiaolv.size());
                                    chart2Two.setLayoutParams(layoutParams);

                                    layoutParams = chart3Two.getLayoutParams();
                                    layoutParams.width = DensityUtil.dp2px(getContext(), 25) * (pinjun_zuoye_shijian.size());
                                    chart3Two.setLayoutParams(layoutParams);
                                    // chart1.clear();
                                    if (baleNumData.size() != 0)
                                        setBarChart(chart1, xAxisValue, baleNumData, "title", R.color.gray, tvChart1Top, tvChart1Center, llChart1);
                                    else {
                                        chart1.setVisibility(View.GONE);
                                        llChart1.setVisibility(View.GONE);
                                    }
                                    if (efficiencyData.size() != 0)
                                        // chart2.clear();
                                        setBarChart(chart2, xAxisValue, efficiencyData, "title", R.color.gray, tvChart2Top, tvChart2Center, llChart2);
                                    else {
                                        chart2.setVisibility(View.GONE);
                                        llChart2.setVisibility(View.GONE);
                                    }
                                    if (workHourData.size() != 0)
                                        // chart3.clear();
                                        setBarChart(chart3, xAxisValue, workHourData, "title", R.color.gray, tvChart3Top, tvChart3Center, llChart3);
                                    else {
                                        chart3.setVisibility(View.GONE);
                                        llChart3.setVisibility(View.GONE);
                                    }

                                    if (zuoye_zongkunshu.size() != 0)
                                        // chart1Two.clear();
                                        setBarChart(chart1Two, xAxisValue, zuoye_zongkunshu, "title", R.color.color_blue, tvChart1TwoTop, tvChart1TwoCenter, llChart1Two);
                                    else {
                                        chart1Two.setVisibility(View.GONE);
                                        llChart1Two.setVisibility(View.GONE);
                                    }
                                    if (pinjun_zuoye_xiaolv.size() != 0)
                                        // chart2Two.clear();
                                        setBarChart(chart2Two, xAxisValue, pinjun_zuoye_xiaolv, "title", R.color.color_blue, tvChart2TwoTop, tvChart2TwoCenter, llChart2Two);
                                    else {
                                        chart2Two.setVisibility(View.GONE);
                                        llChart2Two.setVisibility(View.GONE);
                                    }
                                    if (pinjun_zuoye_shijian.size() != 0)
                                        //chart3Two.clear();
                                        setBarChart(chart3Two, xAxisValue, pinjun_zuoye_shijian, "title", R.color.color_blue, tvChart3TwoTop, tvChart3TwoCenter, llChart3Two);
                                    else {
                                        chart3Two.setVisibility(View.GONE);
                                        llChart3Two.setVisibility(View.GONE);
                                    }
                                    if (rbAll.isChecked()) {
                                        rbBaling.setChecked(true);
                                        rbAll.setChecked(true);
                                    }
                                }


                            } else {//null == data1
                                if (rbAll.isChecked()) {
                                    rbBaling.setChecked(true);
                                    rbAll.setChecked(true);
                                }
                            }
                        } else if (investorBeanBaseBean.code == 4) {//未登录
                            Toast.makeText(mContext, "未登录", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (rbAll.isChecked()) {
                            rbBaling.setChecked(true);
                            rbAll.setChecked(true);
                        }
                    }
                });
    }

    List<String> xAxisValue = new ArrayList<>();
    List<Float> baleNumData = new ArrayList<>();
    List<Float> efficiencyData = new ArrayList<>();
    List<Float> workHourData = new ArrayList<>();

    List<Float> zuoye_zongkunshu = new ArrayList<>();
    List<Float> pinjun_zuoye_xiaolv = new ArrayList<>();//平均作业效率
    List<Float> pinjun_zuoye_shijian = new ArrayList<>();//平均作业时间


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        map.onCreate(savedInstanceState);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        //显示地图
        if (aMap == null) {
            aMap = map.getMap();
        }

        aMap.getUiSettings().setZoomControlsEnabled(false);//缩放按钮
        aMap.getUiSettings().setScaleControlsEnabled(true);//比例尺
        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.blue_arrows));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.parseColor("#00ffffff"));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.parseColor("#00ffffff"));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);

        aMap.setMyLocationStyle(myLocationStyle);
//        aMap.setLocationSource(this);// 设置定位监听

        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);// 设置地图logo显示在右下方
        aMap.getUiSettings().setRotateGesturesEnabled(false);//禁止地图旋转手势
        aMap.getUiSettings().setTiltGesturesEnabled(false);//禁止地图旋转手势

        preferencesHelper = new PreferencesHelper(getActivity());
        token = preferencesHelper.getToken();

        return rootView;
    }

    private void initMap(String v, String v1, MapIndex last_coordinate, int i) {
        LatLng terminus = new LatLng(Double.valueOf(v), Double.valueOf(v1));
//        //移动展示
//        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(terminus, 10f), 4000, null);
//        LatLng latLng = new LatLng(39.903409, 116.427258);//纬度,精度   latitude,lng

        final View infoWindow = getLayoutInflater().inflate(R.layout.map_maintain_layout, null);//display为自定义layout文件
        ImageView iv_bg = (ImageView) infoWindow.findViewById(R.id.iv_bg);
        TextView tv_num = (TextView) infoWindow.findViewById(R.id.tv_num);
        ImageView iv_icon = (ImageView) infoWindow.findViewById(R.id.iv_icon);

        TextView tv_bale_num = (TextView) infoWindow.findViewById(R.id.tv_bale_num);

        tv_num.setText(last_coordinate.getName() + "");
//        status	int	机器状态 1正常绿色 2工作慢黄色 3未工作灰色

        int status = last_coordinate.getStatus();
        if (status == 1) {
            iv_bg.setBackground(getResources().getDrawable(R.drawable.luse_tankuang));
        } else if (status == 2) {
            iv_bg.setBackground(getResources().getDrawable(R.drawable.huangse_tankuang));
        } else {
            iv_bg.setBackground(getResources().getDrawable(R.drawable.huise_tupian));
        }

        String reason_id = last_coordinate.getReason_id();
//        reason_id="1";
        if (!StringUtil.isEmpty(reason_id)) {

            iv_icon.setVisibility(View.VISIBLE);
            tv_bale_num.setVisibility(View.GONE);

            if (reason_id.equals("1")) {
                iv_icon.setBackground(getResources().getDrawable(R.drawable.yun));
            } else if (reason_id.equals("2")) {
                iv_icon.setBackground(getResources().getDrawable(R.drawable.baise_banshou));
            } else if (reason_id.equals("3")) {
                iv_icon.setBackground(getResources().getDrawable(R.drawable.zhuangchao));
            } else {
                iv_icon.setBackground(getResources().getDrawable(R.drawable.feizhengchuang));
            }

        } else {
            iv_icon.setVisibility(View.GONE);//右边的图标
            if (status == 3) {
                tv_bale_num.setVisibility(View.GONE);//右边的文本（当日作业捆数）
                iv_icon.setVisibility(View.VISIBLE);
                iv_icon.setBackground(getResources().getDrawable(R.drawable.feizhengchuang));
            } else {
                tv_bale_num.setVisibility(View.VISIBLE);//右边的文本（当日作业捆数）
            }

            tv_bale_num.setText(last_coordinate.getBale_num() + "");
        }

        layoutView(infoWindow, 200, 400);
        Bitmap viewBitmap = getViewBitmap(infoWindow);

        //第一个点
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(terminus);
        markerOption.draggable(false);//设置Marker可拖动

        markerOption.icon(BitmapDescriptorFactory.fromBitmap(viewBitmap));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果

        Marker marker = aMap.addMarker(markerOption);

        marker.setTitle(i + "");

        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//&&(int)scrollDownLayout.getTag() == View.VISIBLE
                if (View.GONE == scrollDownLayout.getVisibility()) {
                    scrollDownLayout.setVisibility(View.VISIBLE);
                    rlSelect.setVisibility(View.VISIBLE);
                } else {
                    scrollDownLayout.setVisibility(View.GONE);
                    rlSelect.setVisibility(View.GONE);
                }
                if (aMap != null && currentMarker != null) {
                    if (currentMarker.isInfoWindowShown()) {
                        currentMarker.hideInfoWindow();
                    }
                }
            }
        });
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                isTouch = false;
            }
        });

        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                currentMarker = marker;
                String id = marker.getTitle();
                final MapIndex mapIndex = data.get(Integer.parseInt(id));

                View infoWindow1 = getLayoutInflater().inflate(
                        R.layout.map_popwindow_layout, null);
                tv_content = (TextView) infoWindow1.findViewById(R.id.tv_content);
                tv_here = (TextView) infoWindow1.findViewById(R.id.tv_here);
                rl_title = infoWindow1.findViewById(R.id.rl_title);//这个左边的布局
                rl_into_gaode_map = infoWindow1.findViewById(R.id.rl_into_gaode_map);//这个右边的布局


                rl_into_gaode_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OpenExternalMapAppUtils.openMapNavi(getActivity(),
                                mapIndex.getLast_coordinate().getLng(), mapIndex.getLast_coordinate().getLat(), "", "", "");
                    }
                });
                getMachineInfoData(mapIndex, mapIndex);

//                homeChartData(token);//从新请求数据
                return infoWindow1;
            }


            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });//AMap类中

    }

    /**
     * 机器详情
     *
     * @param index
     * @param mapIndex
     */
    private void getMachineInfoData(final MapIndex index, final MapIndex mapIndex) {

        HttpHelp.getInstance().create(RemoteApi.class).machineInfo(preferencesHelper.getToken(), mapIndex.getSn())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<MachineInfo>>(getActivity(), null) {


                    @Override
                    public void onNext(BaseBean<MachineInfo> investorBeanBaseBean) {
                        super.onNext(investorBeanBaseBean);

                        if (investorBeanBaseBean.code == 0) {
                            final MachineInfo data = investorBeanBaseBean.data;


                            String work_status1 = "";
                            String work_status = data.getWork_status();//工作状态 1正常 2工作慢 3没工作
                            if (work_status.equals("1")) {
                                work_status1 = "正常";
                            } else if (work_status.equals("2")) {
                                work_status1 = "工作慢";
                            } else {
                                work_status1 = "没工作";
                            }

                            String is_repair1 = "";
                            String is_repair = data.getIs_repair();//是否需要修理 1是 2否
                            if (is_repair.equals("1")) {
                                is_repair1 = "是";
                            } else if (is_repair.equals("2")) {
                                is_repair1 = "否";
                            }
//                            原因：在xml的处理中，这个\n的换行符解析出来时是“\\n”，前面多了一个“\”，所以起不到换行的效果。
                            String content = "机器编号：" + data.getSn() + "\n" +
                                    "当日作业报数：" + data.getBale_num() + "\n" + "工作状态：" + work_status1
                                    + "\n" + "是否需要修理：" + is_repair1 + "\n" + "距离下次保养还可工作：" + data.getEstimate() + "小时";
                            if (!TextUtils.isEmpty(content) && content.indexOf("\\n") >= 0)
                                content = content.replace("\\n", "\n");
                            tv_content.setText(content);
                            rl_title.setTag(mapIndex.getMachine_id());
                            rl_title.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Toast.makeText(getActivity(), "跳到详情", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mContext, LogRecordDetailsActivity.class);
                                    intent.putExtra("sn", (String) view.getTag());
                                    intent.putExtra("homepagesn", data.getName());
                                    startActivity(intent);
                                }
                            });
                            rl_into_gaode_map.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    OpenExternalMapAppUtils.openMapNavi(getActivity(),
                                            mapIndex.getLast_coordinate().getLng(), mapIndex.getLast_coordinate().getLat(), "", "", "");

//                                    OpenExternalMapAppUtils.openMapNavi(getActivity(),
//                                            "117.25589", "41.205284", "", "", "");
                                }
                            });


                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }

    private boolean isAlreadyLocation = false;
    private int ratio = 15;//地图缩放级别

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            //首先，可以判断AMapLocation对象不为空，当定位错误码类型为0时定位成功。
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。

                    double latitude = amapLocation.getLatitude();//获取纬度
                    double longitude = amapLocation.getLongitude();//获取经度

                    LatLng latLng = new LatLng(latitude, longitude);

                    Log.i("XXX", "latitude" + latitude + "longitude" + longitude);

                    if (!isAlreadyLocation) {
                        isAlreadyLocation = true;

                        //改变可视区域为指定位置
                        //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, ratio, 0, 0));
                        aMap.moveCamera(cameraUpdate);//地图移向指定区域

                        //然后可以移动到定位点,使用animateCamera就有动画效果
                        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ratio));

                    }

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    LogUtil.e("高德定位失败" + amapLocation.getErrorInfo());
                }
            }
        }
    };


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow(); // 显示改点对应 的infowindow
        return true;
    }

    private Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }


    //然后View和其内部的子View都具有了实际大小，也就是完成了布局，相当与添加到了界面上。接着就可以创建位图并在上面绘制了：
    private void layoutView(View v, int width, int height) {
        // 指定整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (R.id.rb_all == checkedId || R.id.rb_Baling == checkedId) {
            if (null != data && data.size() != 0 && aMap.getMapScreenMarkers() != null && aMap.getMapScreenMarkers().size() == 0) {
                for (int i = 0; i < data.size(); i++) {
                    MapIndex mapIndex = data.get(i);
                    MapIndex.LastCoordinateBean last_coordinate = mapIndex.getLast_coordinate();
                    if (!StringUtil.isEmpty(last_coordinate.getLat())) {
                        if (!(Double.parseDouble(last_coordinate.getLat()) <= 1 || Double.parseDouble(last_coordinate.getLng()) <= 1))
                            initMap(last_coordinate.getLat(), last_coordinate.getLng(), mapIndex, i);
                    }

                }
            }
            contentScrollLayout.setVisibility(View.VISIBLE);
        } else {
            List<Marker> markers = aMap.getMapScreenMarkers();
            for (int i = 0; i < markers.size(); i++) {
                Marker marker = markers.get(i);
                marker.remove();
            }
            contentScrollLayout.setVisibility(View.GONE);
        }
    }

    public void getdetermine(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).isHaveRedDot(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<String> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            if (listBaseBean.data != null) {
                                if (listBaseBean.data.equals("1")) {
                                    imMess.setVisibility(View.VISIBLE);
                                } else {
                                    //   loadingLayout.setStatus(LoadingLayout.Empty);
                                    imMess.setVisibility(View.GONE);
                                }
                            } else {
                                imMess.setVisibility(View.GONE);
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
            case R.id.hv1_two:
                hv2Two.scrollTo(l, t);
                hv3Two.scrollTo(l, t);
                break;
            case R.id.hv2_two:
                hv1Two.scrollTo(l, t);
                hv3Two.scrollTo(l, t);
                break;
            case R.id.hv3_two:
                hv1Two.scrollTo(l, t);
                hv2Two.scrollTo(l, t);
                break;
        }
    }
}
//LogRecordDetailsActivity