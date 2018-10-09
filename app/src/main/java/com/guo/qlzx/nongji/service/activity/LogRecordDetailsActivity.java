package com.guo.qlzx.nongji.service.activity;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.DetailsInfoBean;
import com.guo.qlzx.nongji.client.bean.JournalInfoBean;
import com.guo.qlzx.nongji.client.bean.PressureTimeBean;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.adapter.LogRecordDetailsPartAdapter;
import com.guo.qlzx.nongji.utils.MyValueFormatter;
import com.guo.qlzx.nongji.utils.StringAxisValueFormatter;
import com.qlzx.mylibrary.base.BaseActivityTwo;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.GlideUtil;
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
 * 日志列表详情 -未完成 接口有争议
 */

public class LogRecordDetailsActivity extends BaseActivityTwo implements GeocodeSearch.OnGeocodeSearchListener, OnChartValueSelectedListener {
    @BindView(R.id.tv_period)
    TextView tvPeriod;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_count_work)
    TextView tvCountWork;
    @BindView(R.id.tv_count_day)
    TextView tvCountDay;
    @BindView(R.id.chart1)
    BarChart chart1;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_time_unit)
    TextView tvTimeUnit;
    @BindView(R.id.tv_efficiency)
    TextView tvEfficiency;
    @BindView(R.id.tv_efficiency_unit)
    TextView tvEfficiencyUnit;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.tv_speed_unit)
    TextView tvSpeedUnit;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.tv_no_normal_explain)
    TextView tvNoNormalExplain;
    @BindView(R.id.describe_content)
    TextView describeContent;
    @BindView(R.id.gv_picture)
    GridView gvPicture;
    @BindView(R.id.gv_problem_picture)
    GridView gvProblemPicture;
    @BindView(R.id.ll_change_accessories_info)
    LinearLayout llChangeAccessoriesInfo;
    @BindView(R.id.gv_change_accessories_pic)
    GridView gvChangeAccessoriesPic;
    @BindView(R.id.gv_part_details)
    GridView gvPartDetails;
    @BindView(R.id.tv_end_period)
    TextView tvEndPeriod;
    @BindView(R.id.ll_pressure)
    LinearLayout llPressure;
    @BindView(R.id.ll_service)
    LinearLayout llService;
    @BindView(R.id.ll_journal)
    LinearLayout llJournal;
    @BindView(R.id.ll_picture)
    LinearLayout llPicture;
    @BindView(R.id.ll_problem_picture)
    LinearLayout llProblemPicture;
    @BindView(R.id.ll_part_details)
    LinearLayout llPartDetails;
    @BindView(R.id.ll_change_accessories_pic)
    LinearLayout llChangeAccessoriesPic;
    @BindView(R.id.hs_view)
    HorizontalScrollView hsView;
    @BindView(R.id.tv_top)
    TextView tvTop;
    @BindView(R.id.tv_center)
    TextView tvCenter;
    //初始化偏移量
    private int offset = 0;
    private String id;
    private List<String> xAxisValue = new ArrayList<>();
    private List<String> xPressureAxisValue = new ArrayList<>();
    private List pinjun_zuoye_shijian = new ArrayList();
    private List pinjunPressureTime = new ArrayList();
    private AMap aMap;
    private PreferencesHelper preferencesHelper;
    private String token;

    private String titleName = "";
    private String startTime = "";
    private String endTime = "";
    private LogRecordDetailsPartAdapter adapter;
    //private PressureAdapter pressureAdapter;
    private DatePickerDialog dp;
    JournalInfoBean data;
    private List<PressureTimeBean> pressureTimeBeans = new ArrayList<>();
    @BindView(R.id.rl_relative)
    LinearLayout rlRelative;
    @BindView(R.id.ll_describe)
    LinearLayout llDescribe;
    @BindView(R.id.ll_liner)
    LinearLayout llLiner;

    @Override
    public int getContentView() {
        return R.layout.activity_logrecord_details;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        id = getIntent().getStringExtra("sn");
        titleName = getIntent().getStringExtra("homepagesn");

        if (!TextUtils.isEmpty(titleName)) {
            titleBar.setTitleText(titleName);
        }
        map.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = map.getMap();
        }
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.getStrokeWidth();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setScrollGesturesEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);//缩放按钮
        aMap.getUiSettings().setScaleControlsEnabled(true);//比例尺

        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setRotateGesturesEnabled(false);//禁止地图旋转手势
        aMap.getUiSettings().setTiltGesturesEnabled(false);//禁止地图旋转手势

        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);// 设置地图logo显示在右下方

//        for (int i = 0; i < 12; i++) {
//            xAxisValue.add(i + "");
//            pinjun_zuoye_shijian.add(i + "");
//        }
//
//        setBarChart(chart1, xAxisValue, pinjun_zuoye_shijian, "title", R.color.color_blue);
        final List<Date> timeList = getTimeList(Calendar.getInstance().getTime());
        if (rlRelative.getChildCount() == 0)
            addTextView(timeList, rlRelative);
        preferencesHelper = new PreferencesHelper(LogRecordDetailsActivity.this);
        token = preferencesHelper.getToken();

        adapter = new LogRecordDetailsPartAdapter(gvPartDetails);
        gvPartDetails.setAdapter(adapter);
//        pressureAdapter = new PressureAdapter(context, pressureTimeBeans);
//        LinearLayoutManager ms = new LinearLayoutManager(this);
//        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
//        rvHorizontal.setLayoutManager(ms);
//        rvHorizontal.setAdapter(pressureAdapter);
    }

    List<LatLng> latLngs = new ArrayList<LatLng>();

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
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        tvPeriod.setText(startTime);
        tvEndPeriod.setText(" - " + endTime);
        journalInfo(String.valueOf(ToolUtil.getTimeStamp(startTime, "yyyy年MM月dd日")), String.valueOf(ToolUtil.getTimeStamp(endTime, "yyyy年MM月dd日")));
    }

    private void journalInfo(String token, String id, final String time) {
        HttpHelp.getInstance().create(RemoteApi.class).detailsInfo(token, id, time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<DetailsInfoBean>>(getApplicationContext(), null) {
                    @Override
                    public void onNext(BaseBean<DetailsInfoBean> investorBeanBaseBean) {
                        super.onNext(investorBeanBaseBean);

                        if (investorBeanBaseBean.code == 0) {
                            final DetailsInfoBean data = investorBeanBaseBean.data;
                            List<DetailsInfoBean.TrajectoryBean> trajectory = data.getTrajectory();//经纬度

                            //地图
                            if (null != data.getTrajectory()) {
                                latLngs.clear();
                                for (int i = 0; i < trajectory.size(); i++) {

                                    DetailsInfoBean.TrajectoryBean trajectoryBean = trajectory.get(i);
                                    latLngs.add(new LatLng(Double.parseDouble(trajectoryBean.getLat()),
                                            Double.parseDouble(trajectoryBean.getLng())));
                                }
                                //划线
                                Polyline polyline = aMap.addPolyline(new PolylineOptions().
                                        addAll(latLngs).width(10).color(Color.parseColor("#0000ED")));
                                //划一个机器图标
                                if (trajectory.size() != 0) {
                                    DetailsInfoBean.TrajectoryBean trajectoryBean = trajectory.get(trajectory.size() - 1);
                                    initMap(trajectoryBean.getLat(),
                                            trajectoryBean.getLng());
                                }
                                //上边详情
                                tvTime.setText(data.getWork_hours() + "");
                                tvEfficiency.setText(data.getEfficiency() + "");
                                tvSpeed.setText(data.getSpeed() + "");

                                tvNoNormalExplain.setText(data.getJournal().getReason());
                                describeContent.setText(data.getJournal().getContent());
                                llService.setVisibility(View.VISIBLE);
                                //具体描述
                                if (null != data.getJournal().getPic() && data.getJournal().getPic().size() != 0) {
                                    MyAdapter myAdapter1 = new MyAdapter(gvPicture);
                                    gvPicture.setAdapter(myAdapter1);
                                    myAdapter1.setData(data.getJournal().getPic());

                                    MyAdapter myAdapter2 = new MyAdapter(gvProblemPicture);
                                    gvProblemPicture.setAdapter(myAdapter2);
                                    myAdapter2.setData(data.getJournal().getFault_pic());

                                    MyAdapter myAdapter3 = new MyAdapter(gvChangeAccessoriesPic);
                                    gvChangeAccessoriesPic.setAdapter(myAdapter3);
                                    myAdapter3.setData(data.getJournal().getSolve_pic());
                                } else if ((null == data.getJournal().getContent() || data.getJournal().getContent().equals("")) &&
                                        data.getJournal().getFault_pic().size() == 0 && data.getJournal().getDetailed().size() == 0
                                        && (null == data.getJournal().getReason() || data.getJournal().getReason().equals(""))
                                        && data.getJournal().getDetailed().size() == 0 && data.getJournal().getSolve_pic().size() == 0) {
                                    llService.setVisibility(View.GONE);
                                }
                                visibilityView(data.getJournal());
                                List<DetailsInfoBean.JournalBean.DetailedBean> beans = data.getJournal().getDetailed();
                                List<String> stringList = new ArrayList<>();
                                for (DetailsInfoBean.JournalBean.DetailedBean list : beans) {
                                    stringList.add(list.getName() + ":" + list.getNum());
                                }
                                adapter.setData(stringList);
                                xPressureAxisValue.clear();
                                pinjunPressureTime.clear();
                                final List<Date> timeList = getTimeList(new Date((data.getTime() * 1000)));
                                llPressure.removeAllViews();

                                if (rlRelative.getChildCount() == 0)
                                    addTextView(timeList, rlRelative);
                                if (null != data.getGrade() && data.getGrade().size() != 0) {
                                    rlRelative.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                        @Override
                                        public void onGlobalLayout() {
                                            rlRelative.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                            lastEndWidth = 0;
                                            for (int i = 0; i < data.getGrade().size(); i++) {
                                                DetailsInfoBean.GradeBean gradeBean = data.getGrade().get(i);
                                                addPressureTextView(getWidth(gradeBean.getStart(), rlRelative.getWidth()),
                                                        gradeBean.getBale_grade(),
                                                        getWidth(gradeBean.getEnd(), rlRelative.getWidth()));
                                            }
                                        }
                                    });
                                }
                            }
                        } else {
                            ToastUtil.showToast(LogRecordDetailsActivity.this, investorBeanBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    private void visibilityView(DetailsInfoBean.JournalBean journalBean) {
        if (null == journalBean.getReason() && journalBean.getReason().equals("")) {
            llJournal.setVisibility(View.GONE);
        } else {
            llJournal.setVisibility(View.VISIBLE);
        }
        if (null == journalBean.getContent() && journalBean.getContent().equals("")) {
            llDescribe.setVisibility(View.GONE);
        } else {
            llDescribe.setVisibility(View.VISIBLE);
        }
        if (journalBean.getPic().size() == 0) {
            llPicture.setVisibility(View.GONE);
        } else llPicture.setVisibility(View.VISIBLE);
        if (journalBean.getFault_pic().size() == 0) {
            llProblemPicture.setVisibility(View.GONE);
        } else llProblemPicture.setVisibility(View.VISIBLE);
        if (journalBean.getDetailed().size() == 0) llPartDetails.setVisibility(View.GONE);
        else llPartDetails.setVisibility(View.VISIBLE);
        if (journalBean.getFault_pic().size() == 0) llChangeAccessoriesPic.setVisibility(View.GONE);
        else llChangeAccessoriesPic.setVisibility(View.VISIBLE);
    }

    private int getWidth(long startTime, int rlWidth) {
        Date date = new Date(startTime * 1000);
        double startF = ((double) rlWidth / (double) 24) * date.getHours() + ((double) rlWidth / (double) 24) / 60 * date.getMinutes();

        return (int) startF;
    }

    int lastEndWidth = 0;

    private void addPressureTextView(int startWidth, String position, int endWidth) {
        if (endWidth - startWidth == 0 || position.equals("0")) {
            return;
        }

        TextView textView = new TextView(context);
        textView.setWidth(endWidth - startWidth);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(endWidth - startWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        textView.setText(position + "档");
        textView.setTextSize(10);
        textView.setId(0);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));

        if (position.equals("1")) {
            textView.setBackgroundResource(R.drawable.shap_green_three);
            layoutParams.setMargins(startWidth - lastEndWidth, 2, 0, 2);
            textView.setLayoutParams(layoutParams);
        } else if (position.equals("2")) {
            textView.setBackgroundResource(R.drawable.shap_yellow_three);
            layoutParams.setMargins(startWidth - lastEndWidth, 1, 0, 1);
            textView.setLayoutParams(layoutParams);
        } else if (position.equals("3")) {
            textView.setBackgroundResource(R.drawable.shap_red_three);
            layoutParams.setMargins(startWidth - lastEndWidth, 0, 0, 0);
            textView.setLayoutParams(layoutParams);
        }
//        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(0, 0);
//        //添加规则  1为tv1的id
//        params.addRule(RelativeLayout.LEFT_OF,0);
//        TextView tv=new TextView(this);
//        //将tv2加入布局 传入参数params
//        llPressure.addView(tv,params);
        llPressure.addView(textView);
        lastEndWidth = endWidth;
    }

    private void addTextView(List<Date> dateList, LinearLayout linearLayout) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        for (int i = 0; i < dateList.size(); i++) {
            TextView textView = new TextView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 100, 0);
            textView.setLayoutParams(layoutParams);
            textView.setText(simpleDateFormat.format(dateList.get(i)) + "");
            linearLayout.addView(textView);
        }
    }

    private List<Date> getTimeList(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Date> timeList = new ArrayList<>();
        try {
            for (int i = 0; i < 24; i = i + 1) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, 1900 + date.getYear());
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MONTH, date.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, date.getDate());
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                String strDate = simpleDateFormat.format(new Date(calendar.getTimeInMillis()));
                Date dateTime = simpleDateFormat.parse(strDate);
                timeList.add(dateTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeList;
    }

    private List<Date> removeTimeList(List<Date> timeList, List<Long> pressureList) {
        for (int i = 0; i < pressureList.size(); i++) {
            for (int k = 0; k < timeList.size(); k++) {
                if (pressureList.get(i) == timeList.get(k).getTime()) {
                    timeList.remove(k);
                    k--;
                }
            }
        }
        return timeList;
    }

    public static long getTodayZero() {
        Date date = new Date();
        long l = 24 * 60 * 60 * 1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
    }

    //获得当天24点时间
    public static long getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() / 1000);
    }

    private void journalInfo(String start, String end) {
        HttpHelp.getInstance().create(RemoteApi.class).journalInfo(token, id, start, end)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<JournalInfoBean>>(getApplicationContext(), null) {
                    @Override
                    public void onNext(BaseBean<JournalInfoBean> investorBeanBaseBean) {
                        super.onNext(investorBeanBaseBean);

                        if (investorBeanBaseBean.code == 0) {
                            data = investorBeanBaseBean.data;
                            if (!(null == investorBeanBaseBean.message || "".equals(investorBeanBaseBean.message)))
                                ToastUtil.showToast(context, investorBeanBaseBean.message);
                            // tvCountWork.setText("总工作捆数   " + data.getTotal_nums() + "捆");
                            tvCountDay.setText("总工作时长   " + data.getTotal_days() + "天");
                            //获取位置
                            setAddressText(data.getPosition());
                            if (null != data.getPosition() && null != data.getPosition().getLat()
                                    && null != data.getPosition().getLng() && (!data.getPosition().getLat().equals("") || !data.getPosition().getLng().equals(""))) {
                                LatLng latLng = new LatLng(Double.valueOf(data.getPosition().getLat()), Double.valueOf(data.getPosition().getLng()));
                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 15, 0, 0));
                                aMap.moveCamera(cameraUpdate);//地图移向指定区域

                                //然后可以移动到定位点,使用animateCamera就有动画效果
                                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                            }
                            xAxisValue.clear();
                            pinjun_zuoye_shijian.clear();
                            if (null != data.getWork()) {
                                for (int i = 0; i < data.getWork().size(); i++) {
                                    xAxisValue.add((new SimpleDateFormat("yyyy-MM-dd").format(new Date((data.getWork().get(i).getTime() * 1000)))));
                                    pinjun_zuoye_shijian.add(data.getWork().get(i).getNums());
                                }
                                ViewGroup.LayoutParams layoutParams = chart1.getLayoutParams();
                                layoutParams.width = DensityUtil.dp2px(LogRecordDetailsActivity.this, 40) * (pinjun_zuoye_shijian.size());
                                chart1.setLayoutParams(layoutParams);
//                                if (pinjun_zuoye_shijian.size() != 0)
                                setBarChart(chart1, xAxisValue, pinjun_zuoye_shijian, "title", R.color.white,tvTop,tvCenter);
//                                else chart1.setNoDataText("暂无数据");
                            }//for结束
                            else {
                                llLiner.setVisibility(View.GONE);
                                chart1.setVisibility(View.GONE);
                            }


                        } else if (investorBeanBaseBean.code == 4) {
                            ToolUtil.getOutLogs(LogRecordDetailsActivity.this);
                        } else {
                            ToastUtil.showToast(LogRecordDetailsActivity.this, investorBeanBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * create by xuxx 2018-6-19
     *
     * @param bean 位置实体
     */
    private void setAddressText(JournalInfoBean.PositionBean bean) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        if (null == bean.getLat() || bean.getLat().equals("") || null == bean.getLng() || bean.getLng().equals("")) {
            tvAddress.setText("");
            return;
        }
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(Double.valueOf(bean.getLat()), Double.valueOf(bean.getLng())), 0, GeocodeSearch.AMAP);

        geocoderSearch.getFromLocationAsyn(query);
    }

    private void initMap(String v, String v1) {
        LatLng terminus = new LatLng(Double.valueOf(v), Double.valueOf(v1));
//        //移动展示
//        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(terminus, 10f), 4000, null);
//        LatLng latLng = new LatLng(39.903409, 116.427258);//纬度,精度   latitude,lng

        final View infoWindow = getLayoutInflater().inflate(R.layout.map_maintain, null);//display为自定义layout文件
//        status	int	机器状态 1正常绿色 2工作慢黄色 3未工作灰色

        layoutView(infoWindow, 200, 400);
        Bitmap viewBitmap = getViewBitmap(infoWindow);
        for (int i = 0; i < aMap.getMapScreenMarkers().size(); i++) {
            Marker marker = aMap.getMapScreenMarkers().get(i);
            marker.remove();
        }
        //第一个点
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(terminus);
        markerOption.draggable(false);//设置Marker可拖动

        markerOption.icon(BitmapDescriptorFactory.fromBitmap(viewBitmap));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        Marker marker = aMap.addMarker(markerOption);
        //改变可视区域为指定位置
        //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）

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

    /**
     * 单数据集。设置柱状图样式，X轴为字符串，Y轴为数值
     *
     * @param barChart
     * @param xAxisValue
     * @param yAxisValue
     * @param title      图例文字
     * @param barColor
     */
    public void setBarChart(BarChart barChart, List<String> xAxisValue, List<Float> yAxisValue, String title, Integer barColor,TextView tvTop,TextView tvCenter) {

        barChart.getDescription().setEnabled(false);//设置描述
        barChart.setPinchZoom(false);//设置按比例放缩柱状图
        barChart.setMaxVisibleValueCount(60);
        barChart.setLogEnabled(false);
        barChart.setMinimumHeight(0);
        // barChart.setNoDataText("暂无数据");
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
        if (yAxisValue.size() == 0 ) {
            leftAxis.setAxisMaximum(5);
        } else {
            Float max = Collections.max(yAxisValue);
            int i = ((int) max.intValue());
            tvTop.setText((max <= 0 ? 0 : i) + "");
            Float center = Collections.max(yAxisValue) / 2;
            tvCenter.setText((center) + "");
            leftAxis.setAxisMaximum(max);
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
            set1.setHighlightEnabled(false);
//            if (barColor == null) {
            set1.setColor(Color.parseColor("#FFFFFF"));//设置set1的柱的颜色
//            } else {
//                set1.setColor(Color.parseColor("#FFC0C6"));//设置set1的柱的颜色   #EDEBEB     #FBFAFA
            set1.setGradientColor(Color.parseColor("#FBFAFA"), Color.parseColor("#ffefd4"));//#FFF7F8
            //          }

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(11);
            data.setValueTextColor(Color.parseColor("#666666"));
            data.setBarWidth(0.7f);//设置柱形图柱子的大小  柱状图宽度
            data.setValueFormatter(new MyValueFormatter());
            barChart.setData(data);
            barChart.getData().setHighlightEnabled(
                    !barChart.getData().isHighlightEnabled());
            barChart.setScaleEnabled(false);
            barChart.setOnChartValueSelectedListener(this);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == 1000) {
            tvAddress.setText(regeocodeResult.getRegeocodeAddress().getCity() + regeocodeResult.getRegeocodeAddress().getDistrict());
        } else {
            tvAddress.setText("区域获取失败");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

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
                        ToastUtil.showToast(LogRecordDetailsActivity.this, "不能大于当前时间");
                        return;
                    }


                    if (compareDay(pickerDate.getTime(), date.getTime(), tag) && tag ? true : startDate.getTime() < pickerDate.getTime()) {


                        if (tag) {
                            startTime = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                            tvPeriod.setText(startTime);
                        } else {
                            endTime = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                            tvEndPeriod.setText("-" + endTime);
                        }

                        journalInfo(String.valueOf(ToolUtil.getTimeStamp(startTime, "yyyy年MM月dd日")), String.valueOf(ToolUtil.getTimeStamp(endTime, "yyyy年MM月dd日")));
                    } else if (tag) {
                        ToastUtil.showToast(LogRecordDetailsActivity.this, "起始时间不能大于结束时间");
                    } else {
                        ToastUtil.showToast(LogRecordDetailsActivity.this, "结束时间不能小于起始时间");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), 1);
        dp.show();

    }

    private boolean compareDay(long fist, long end, boolean tag) {
        boolean b = false;
        if (tag ? fist <= end : fist <= Calendar.getInstance().getTime().getTime()) {
            b = true;
        }
        return b;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (data != null && (!data.getPosition().getLat().equals("") || !data.getPosition().getLng().equals(""))) {
            LatLng latLng = new LatLng(Double.valueOf(data.getPosition().getLat()), Double.valueOf(data.getPosition().getLng()));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 15, 0, 0));
            aMap.moveCamera(cameraUpdate);//地图移向指定区域

            //然后可以移动到定位点,使用animateCamera就有动画效果
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        }
        journalInfo(token, id, String.valueOf(data.getWork().get((int) e.getX()).getTime()));
    }

    @Override
    public void onNothingSelected() {
        tvTime.setText("0");
        tvEfficiency.setText("0");
        tvSpeed.setText("0");
        llPressure.removeAllViews();
        if (((MyAdapter) gvPicture.getAdapter()) != null)
            ((MyAdapter) gvPicture.getAdapter()).setData(null);
        if (((MyAdapter) gvProblemPicture.getAdapter()) != null)
            ((MyAdapter) gvProblemPicture.getAdapter()).setData(null);
        if (((MyAdapter) gvChangeAccessoriesPic.getAdapter()) != null)
            ((MyAdapter) gvChangeAccessoriesPic.getAdapter()).setData(null);
        tvNoNormalExplain.setText("");
        describeContent.setText("");

    }

    class MyAdapter extends BaseListAdapter<String> {
        public MyAdapter(GridView listView) {
            super(listView, R.layout.list_item_log_details_pic);
        }

        @Override
        public void fillData(ViewHolder holder, int position, String model) {
//            holder.setText(R.id.iv_pic, model);

            ImageView view = (ImageView) holder.getView(R.id.iv_pic);
            GlideUtil.display(mContext, Constants.IMG_HOST + model, view);

        }
    }

}
