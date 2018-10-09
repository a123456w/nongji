package com.guo.qlzx.nongji.service.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.adapter.CommentPicListAdapter;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ActionItem;
import com.guo.qlzx.nongji.commen.util.TitlePopup;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.adapter.ItemLogsAdapter;
import com.guo.qlzx.nongji.service.bean.AddLayoutBean;
import com.guo.qlzx.nongji.service.bean.LogsAsrvicesDetailsBean;
import com.guo.qlzx.nongji.utils.LuBanUtils;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TakePhotoUtils;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 报修处理
 */
public class LogsAsrvicesDetailsActivity extends BaseActivity implements ItemLogsAdapter.OnEditAssignment, AMapLocationListener {

    @BindView(R.id.but_zhengchang)
    RadioButton butZhengchang;
    @BindView(R.id.but_feizhengchang)
    RadioButton butFeizhengchang;
    @BindView(R.id.RadioGroup1)
    RadioGroup RadioGroup1;
    @BindView(R.id.tv_jiqi)
    TextView tvJiqi;
    @BindView(R.id.iv_picc)
    ImageView ivPicc;
    private final int REQUIRES_PERMISSION = 0;//申请权限
    private static final int REQUEST_CODE_SETTING = 300;
    @BindView(R.id.but_onesumit)
    Button butOnesumit;
    @BindView(R.id.gv_pic)
    NoScrollGridView gvPic;
    @BindView(R.id.gv_pic1)
    NoScrollGridView gvPic1;
    @BindView(R.id.gv_pic2)
    NoScrollGridView gvPic2;
    @BindView(R.id.rc_rate)
    RatingBar rcRate;
    @BindView(R.id.tv_adds)
    TextView tvAdds;
    @BindView(R.id.re_cy1)
    NoScrollListView reCy1;
    @BindView(R.id.but_twosumit)
    Button butTwosumit;
    @BindView(R.id.tv_content)
    EditText tvContent;
    private PreferencesHelper helper;
    private List<LogsAsrvicesDetailsBean> listdata;
    private String dizhi_id;
    private TitlePopup titlePopup;
    private String orderId = "";
    private String machine_id = "";
    private Dialog mDialog;
    private File takePhotoFile;
    private List<String> imageUrl3List = new ArrayList<>();
    private List<String> imageUrl1List = new ArrayList<>();
    private List<String> imageUrl2List = new ArrayList<>();
    List<AddLayoutBean> listData = new ArrayList<>();
    //图片
    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_CHOICE_PHOTO = 200;
    private Object data;
    private String status = "0";
    private CommentPicListAdapter adapter;
    private ItemLogsAdapter adapter1;
    private EditText ed_buwei;
    private List<AddLayoutBean> assignmentList = new ArrayList<>();
    int numStars = 5;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    public int getContentView() {
        return R.layout.activity_logs_asrvices_details;
    }

    @Override
    public void initView() {


        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        initMaplocation();
        rcRate.setNumStars(5);
        //第一次提交上传图片
        initPic();
        //第二次提交问题图片
        initPics();
        //第二次提交更换配件图片
        initPicsc();
        final EditText ed_buwei = (EditText) findViewById(R.id.ed_buwei);
        adapter1 = new ItemLogsAdapter(reCy1, listData);
        reCy1.setAdapter(adapter1);
        adapter1.setOnEditAssignment(this);
        String name = getIntent().getStringExtra("ORDERID1");
        dizhi_id = getIntent().getStringExtra("ORDERID2");
        rcRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                numStars = ratingBar.getNumStars();
            }
        });
        titleBar.setTitleText(name);
        //实例化popwi
        titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                tvJiqi.setText(listdata.get(position).getReason());
                machine_id = listdata.get(position).getId();
            }
        });
        orderId = getIntent().getStringExtra("ORDERID");
        ivPicc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titlePopup.show(tvJiqi);
            }
        });
        helper = new PreferencesHelper(this);
        RadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (butZhengchang.isChecked()) {
                    tvJiqi.setText("");
                    ivPicc.setClickable(false);
                    dizhi_id = "1";
                    machine_id = "";
                } else {
                    ivPicc.setClickable(true);
                    dizhi_id = "2";
                }

            }
        });
        final String journal_id = getIntent().getStringExtra("ORDERID2");
        //第一次提交
        butOnesumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = helper.getToken();
                if (ivPicc.isClickable() ? (machine_id.equals("") ? true : false) : !dizhi_id.equals("1")
                        || ivPicc.isClickable() ?tvContent.getText().toString().trim().equals(""):false) {
                    ToastUtil.showToast(context, "请填写完整信息");
                    return;
                }
                getFirstSubmitDate(new PreferencesHelper(LogsAsrvicesDetailsActivity.this).getToken(), journal_id, dizhi_id, machine_id,
                        imageUrl1List, tvContent.getText().toString().trim(), numStars);
            }
        });

        //点击增加明细
        tvAdds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.add(new AddLayoutBean());
                adapter1.setData(listData);
                adapter1.notifyDataSetChanged();
                if (listData.size() == 10) {
                    tvAdds.setVisibility(View.GONE);
                }
            }
        });
        butTwosumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assignmentList.size() != listData.size()) {
                    ToastUtil.showToast(context, "请填写完整信息");
                    return;
                }
                JSONArray detailedJson = new JSONArray();
                for (int i = 0; i < assignmentList.size(); i++) {
                    AddLayoutBean addLayoutBean = assignmentList.get(i);
                    JSONObject detailed = new JSONObject();
                    try {
                        detailed.put("part", addLayoutBean.getStrBuwei());
                        detailed.put("num", addLayoutBean.getStrEdNum());
                        detailed.put("model", addLayoutBean.getStrEdModel());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    detailedJson.put(detailed);
                }


                getSecondSubmitDate(new PreferencesHelper(LogsAsrvicesDetailsActivity.this).getToken(), journal_id, detailedJson.toString(), imageUrl2List, imageUrl3List);
            }
        });

    }

    private void initMaplocation() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setOnceLocation(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        //启动定位
        mlocationClient.startLocation();
    }

    private void initPicsc() {
        adapter = new CommentPicListAdapter(this);
        gvPic2.setAdapter(adapter);
        adapter.setList(imageUrl3List);
        gvPic2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imageUrl3List != null) {
                    RadioGroup1.setTag(gvPic2.getId());
                    if (imageUrl3List.size() <= position) {
                        DialogPics(6 - imageUrl3List.size());
                    }
                } else {

                    DialogPicc(6);
                }
            }
        });
    }

    private void initPics() {
        adapter = new CommentPicListAdapter(this);
        gvPic1.setAdapter(adapter);
        adapter.setList(imageUrl2List);
        gvPic1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imageUrl2List != null) {
                    RadioGroup1.setTag(gvPic1.getId());
                    if (imageUrl2List.size() <= position) {
                        DialogPicc(6 - imageUrl2List.size());
                    }
                } else {

                    DialogPicc(6);
                }
            }
        });
    }

    private void initPic() {
        //提交照片
        adapter = new CommentPicListAdapter(this);
        gvPic.setAdapter(adapter);
        adapter.setList(imageUrl1List);
        gvPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (imageUrl1List != null) {
                    RadioGroup1.setTag(gvPic.getId());
                    if (imageUrl1List.size() <= position) {
                        DialogPic(6 - imageUrl1List.size());
                    }
                } else {

                    DialogPic(6);
                }

            }
        });
    }


    /**
     * 第一次提交
     *
     * @param token
     * @param journal_id
     * @param machine_id
     * @param reason_id
     * @param pic
     * @param content
     * @param score
     */
    private void getFirstSubmitDate(String token, String journal_id, String machine_id, String reason_id, final List<String> pic, String content, int score) {
        loadingLayout.setStatus(LoadingLayout.Loading);
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", token)
                .addFormDataPart("journal_id", journal_id)
                .addFormDataPart("status", machine_id)
                .addFormDataPart("reason_id", reason_id)
                .addFormDataPart("content", content)
                .addFormDataPart("score", score + "");
        if(pic.size()!=0)
        new LuBanUtils().getFileList(context, pic, new LuBanUtils.OnCompressSuccessListener() {

            @Override
            public void onSuccess(List<File> list) {
                for (int i = 0; i < list.size(); i++) {
                    File file = list.get(i);
                    RequestBody body = RequestBody.create(MediaType.parse("image/png"), file);
                    builder.addFormDataPart("pic[]", file.getName(), body);
                }
                getFirstSubmitDate(builder);
            }
        });
        else {
            getFirstSubmitDate(builder);
        }

    }

    /*
     *
     * */
    private void getFirstSubmitDate(MultipartBody.Builder builder) {
        HttpHelp.getInstance().create(RemoteApi.class).getFirstSubmit(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        loadingLayout.setStatus(LoadingLayout.Success);
                        if (listBaseBean.code == 0) {
                            butOnesumit.setClickable(false);
                            imageUrl1List.clear();
                            setStringList(gvPic, imageUrl1List);
                            rcRate.setRating(0);
                            butZhengchang.setChecked(false);
                            butFeizhengchang.setChecked(false);
                            tvContent.setText("");
                            numStars = 0;
                            butOnesumit.setText("已提交");
                            if (!butOnesumit.isClickable() && !butTwosumit.isClickable()) {
                                finish();
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        loadingLayout.setStatus(LoadingLayout.Success);
                    }
                });
    }

    /**
     * 第二次提交
     *
     * @param token
     * @param journal_id
     * @param detailed
     * @param fault_pic
     * @param solve_pic
     */
    private void getSecondSubmitDate(String token, String journal_id, String detailed, final List<String> fault_pic, final List<String> solve_pic) {
        loadingLayout.setStatus(LoadingLayout.Loading);
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", token)
                .addFormDataPart("journal_id", journal_id)
                .addFormDataPart("detailed", detailed);
        if(fault_pic.size()!=0)
        new LuBanUtils().getFileList(context, fault_pic, new LuBanUtils.OnCompressSuccessListener() {
            @Override
            public void onSuccess(List<File> list) {
                for (int i = 0; i < list.size(); i++) {
                    File f = list.get(i);
                    RequestBody IDFrontBody = RequestBody.create(MediaType.parse("image/png"), list.get(i));
                    builder.addFormDataPart("fault_pic[]", list.get(i).getName(), IDFrontBody);
                }
                if(solve_pic.size()!=0)
                new LuBanUtils().getFileList(context, solve_pic, new LuBanUtils.OnCompressSuccessListener() {

                    @Override
                    public void onSuccess(List<File> list) {
                        for (int i = 0; i < list.size(); i++) {
                            File f = list.get(i);
                            RequestBody IDFrontBody = RequestBody.create(MediaType.parse("image/png"), list.get(i));
                            builder.addFormDataPart("solve_pic[]", list.get(i).getName(), IDFrontBody);
                        }
                        getSecondSubmitDate(builder);
                    }
                });
                else getSecondSubmitDate(builder);
            }
        });
        else  getSecondSubmitDate(builder);
//        for (int i = 0; i < fault_pic.size(); i++) {
//            File file = new File(fault_pic.get(i));
//            RequestBody body = RequestBody.create(MediaType.parse("image/png"), file);
//            builder.addFormDataPart("fault_pic[]", file.getName(), body);
//        }
//        for (int i = 0; i < solve_pic.size(); i++) {
//            File file = new File(solve_pic.get(i));
//            RequestBody body = RequestBody.create(MediaType.parse("image/png"), file);
//            builder.addFormDataPart("solve_pic[]", file.getName(), body);
//        }
//        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
//                , detailed);
//        builder.addFormDataPart("detailed",requestBody);

    }

    private void getSecondSubmitDate(MultipartBody.Builder builder) {
        HttpHelp.getInstance().create(RemoteApi.class).getSecondSubmit(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        loadingLayout.setStatus(LoadingLayout.Success);
                        if (listBaseBean.code == 0) {
                            butTwosumit.setClickable(false);
                            imageUrl2List.clear();
                            setStringList(gvPic1, imageUrl2List);
                            imageUrl3List.clear();
                            setStringList(gvPic2, imageUrl3List);
                            listData.clear();
                            listData.add(new AddLayoutBean());
                            adapter1.setData(listData);
                            adapter1.notifyDataSetChanged();
                            tvAdds.setVisibility(View.VISIBLE);
                            assignmentList.clear();
                            butTwosumit.setText("已提交");
                            if (!butOnesumit.isClickable() && !butTwosumit.isClickable()) {
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        loadingLayout.setStatus(LoadingLayout.Success);
                    }
                });
    }

    @Override
    public void getData() {
        listData.add(new AddLayoutBean());
        adapter1.setData(listData);
        adapter1.notifyDataSetChanged();
        String token = helper.getToken();
        getShowRepairDate(new PreferencesHelper(LogsAsrvicesDetailsActivity.this).getToken(), dizhi_id);
    }

    /**
     * 报修处理方法
     */
    private void getShowRepairDate(String token, String journal_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getShowRepair(token, journal_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<LogsAsrvicesDetailsBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<LogsAsrvicesDetailsBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            listdata = listBaseBean.data;

                            if (listdata != null && listdata.size() != 0) {

                                for (int i = 0; i < listdata.size(); i++) {
                                    titlePopup.addAction(new ActionItem(LogsAsrvicesDetailsActivity.this, listdata.get(i).getReason(), R.drawable.ic_about));
                                }

                            }
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.getOutLogs(LogsAsrvicesDetailsActivity.this);
                        } else {
                            ToastUtil.showToast(LogsAsrvicesDetailsActivity.this, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 弹框
     */
    public void DialogPic(final int count) {


        if (!AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            requestPermission();
            return;
        }


        mDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_pic_gain, null);

        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();

        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.dialog_animation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialogwindow.setAttributes(lp);
        mDialog.show();

        TextView tv_close, pat_pic, photo_select;
        tv_close = inflate.findViewById(R.id.tv_close);
        photo_select = inflate.findViewById(R.id.photo_select);
        pat_pic = inflate.findViewById(R.id.pat_pic);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        pat_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//照片
                mDialog.dismiss();
                try {
                    takePhotoFile = TakePhotoUtils.takePhoto(LogsAsrvicesDetailsActivity.this, "nongjiPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(LogsAsrvicesDetailsActivity.this, "拍照失败");
                    e.printStackTrace();
                }
            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//相册
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(LogsAsrvicesDetailsActivity.this, null, count, null, false), REQUEST_CODE_CHOICE_PHOTO);
            }
        });

    }

    public void setStringList(NoScrollGridView gridView, List<String> list) {
        CommentPicListAdapter adapter = (CommentPicListAdapter) gridView.getAdapter();
        adapter.setList(list);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("回调成功  onActivityResult");
        String path;
        //mBaseDialog.dismiss();

        //拍照回调
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            LogUtil.d("拍照回调成功  onActivityResult");
            if (takePhotoFile != null) {

                List<String> list = new ArrayList<>();
                path = takePhotoFile.getAbsolutePath();
                list.add(path);
                switch ((int) RadioGroup1.getTag()) {
                    case R.id.gv_pic:

                        if (imageUrl1List != null) {
                            list.addAll(imageUrl1List);
                        }
                        imageUrl1List = list;
                        setStringList(gvPic, list);
                        break;
                    case R.id.gv_pic1:

                        if (imageUrl2List != null) {
                            list.addAll(imageUrl2List);
                        }
                        imageUrl2List = list;
                        setStringList(gvPic1, list);
                        break;
                    case R.id.gv_pic2:

                        if (imageUrl3List != null) {
                            list.addAll(imageUrl3List);
                        }
                        imageUrl3List = list;
                        setStringList(gvPic2, list);
                        break;
//            }
                }
            }
        }
        //选择图片回调
        if (requestCode == REQUEST_CODE_CHOICE_PHOTO && resultCode == RESULT_OK) {
            LogUtil.d("选择回调成功  onActivityResult");

            switch ((int) RadioGroup1.getTag()) {
                case R.id.gv_pic:
                    List<String> listImg = BGAPhotoPickerActivity.getSelectedImages(data);
                    if (imageUrl1List != null) {
                        listImg.addAll(imageUrl1List);
                    }
                    imageUrl1List = listImg;
                    setStringList(gvPic, listImg);
                    break;
                case R.id.gv_pic1:
                    listImg = BGAPhotoPickerActivity.getSelectedImages(data);
                    if (imageUrl2List != null) {
                        listImg.addAll(imageUrl2List);
                    }
                    imageUrl2List = listImg;
                    setStringList(gvPic1, listImg);
                    break;
                case R.id.gv_pic2:
                    listImg = BGAPhotoPickerActivity.getSelectedImages(data);
                    if (imageUrl3List != null) {
                        listImg.addAll(imageUrl3List);
                    }
                    imageUrl3List = listImg;
                    setStringList(gvPic2, listImg);
                    break;
            }
        }
    }

    // 申请多个权限。
    private void requestPermission() {
        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(REQUIRES_PERMISSION)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(rationaleListener)
                .start();
    }

    /**
     * 回调监听。
     */
    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantPermissions) {
            LogUtil.d("权限申请成功  onSucceed");
            switch (requestCode) {
                case REQUIRES_PERMISSION: {
                    ToastUtil.showToast(LogsAsrvicesDetailsActivity.this, "权限申请成功");
                    break;
                }
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            LogUtil.d("权限申请成功  onFailed");
            switch (requestCode) {

                case REQUIRES_PERMISSION: {
                    // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                    if (AndPermission.hasAlwaysDeniedPermission(LogsAsrvicesDetailsActivity.this, deniedPermissions)) {
                        //用自定义的提示语。
                        AndPermission.defaultSettingDialog(LogsAsrvicesDetailsActivity.this, REQUEST_CODE_SETTING)
                                .setTitle("权限申请失败")
                                .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                                .setPositiveButton("好，去设置")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtil.showToast(LogsAsrvicesDetailsActivity.this, "权限申请失败，无法评论");
                                    }
                                })
                                .show();
                    } else {
                        ToastUtil.showToast(LogsAsrvicesDetailsActivity.this, "权限申请失败，无法评论");
                    }

                    break;
                }
            }


        }
    };
    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            AlertDialog.newBuilder(LogsAsrvicesDetailsActivity.this)
                    .setTitle("友好提醒")
                    .setMessage("为保证APP的正常运行，需要以下权限:\n1.访问SD卡（选择图片等功能）\n2.调用摄像头（拍照功能）")
                    .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rationale.resume();
                        }
                    })
                    .setNegativeButton("依然拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rationale.cancel();
                        }
                    }).show();
        }
    };


    /**
     * 弹框c
     */
    public void DialogPicc(final int count) {


        if (!AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            requestPermission();
            return;
        }
        mDialog = new Dialog(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_pic_gain, null);
        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.dialog_animation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        mDialog.show();
        TextView tv_close, pat_pic, photo_select;
        tv_close = inflate.findViewById(R.id.tv_close);
        photo_select = inflate.findViewById(R.id.photo_select);
        pat_pic = inflate.findViewById(R.id.pat_pic);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        pat_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//照片
                mDialog.dismiss();
                try {
                    takePhotoFile = TakePhotoUtils.takePhoto(LogsAsrvicesDetailsActivity.this, "nongjiPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(LogsAsrvicesDetailsActivity.this, "拍照失败");
                    e.printStackTrace();
                }
            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//相册
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(LogsAsrvicesDetailsActivity.this, null, count, null, false), REQUEST_CODE_CHOICE_PHOTO);
            }
        });

    }

    /**
     * 弹框s
     */
    public void DialogPics(final int count) {


        if (!AndPermission.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            requestPermission();
            return;
        }


        mDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_pic_gain, null);

        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();

        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.dialog_animation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialogwindow.setAttributes(lp);
        mDialog.show();

        TextView tv_close, pat_pic, photo_select;
        tv_close = inflate.findViewById(R.id.tv_close);
        photo_select = inflate.findViewById(R.id.photo_select);
        pat_pic = inflate.findViewById(R.id.pat_pic);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        pat_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//照片
                mDialog.dismiss();
                try {
                    takePhotoFile = TakePhotoUtils.takePhoto(LogsAsrvicesDetailsActivity.this, "nongjiPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(LogsAsrvicesDetailsActivity.this, "拍照失败");
                    e.printStackTrace();
                }
            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//相册
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(LogsAsrvicesDetailsActivity.this, null, count, null, false), REQUEST_CODE_CHOICE_PHOTO);
            }
        });

    }


    @Override
    public void onEditAssignment(AddLayoutBean addLayoutBean) {
        for (int i = 0; i < assignmentList.size(); i++) {
            if (addLayoutBean.getId() == assignmentList.get(i).getId()) {
                assignmentList.remove(i);
            }
        }
        assignmentList.add(addLayoutBean);
    }

    @Override
    public void onEditUnAssignment(AddLayoutBean addLayoutBean) {
        for (int i = 0; i < assignmentList.size(); i++) {
            if (addLayoutBean.getId() == assignmentList.get(i).getId()) {
                assignmentList.remove(i);
            }
        }
    }

    @Override
    public void onRemoveLayout() {
        if (listData.size() < 10) {
            tvAdds.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationOption.clone();
        mlocationClient.onDestroy();
        mLocationOption = null;
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                titleBar.setRightTextRes(aMapLocation.getCity());
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                LogUtil.e("高德定位失败" + aMapLocation.getErrorInfo());
            }
        }
    }
}
