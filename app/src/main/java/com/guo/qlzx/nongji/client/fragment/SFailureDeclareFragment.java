package com.guo.qlzx.nongji.client.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.Costom.DeclarePopWindow;
import com.guo.qlzx.nongji.client.activity.DeclareRecordActivity;
import com.guo.qlzx.nongji.client.activity.ManualActivity;
import com.guo.qlzx.nongji.client.activity.SPersonalCentercActivity;
import com.guo.qlzx.nongji.client.activity.WebActivity;
import com.guo.qlzx.nongji.client.adapter.CommentPicListAdapter;
import com.guo.qlzx.nongji.client.adapter.FailureDeclareAdapter;
import com.guo.qlzx.nongji.client.bean.FailureDeclareListBean;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.utils.LuBanUtils;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TakePhotoUtils;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * 客户端故障申报
 */
public class SFailureDeclareFragment extends BaseFragment  {

    @BindView(R.id.tv_mac)
    TextView tvMac;
    @BindView(R.id.tv_mac_name)
    TextView tvMacName;
    @BindView(R.id.iv_mac)
    ImageView ivMac;
    @BindView(R.id.ll_mac)
    LinearLayout llMac;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_mac_num)
    TextView tvMacNum;
    @BindView(R.id.iv_mac_num)
    ImageView ivMacNum;
    @BindView(R.id.ll_mac_number)
    LinearLayout llMacNumber;
    @BindView(R.id.tv_mac_part)
    TextView tvMacPart;
    @BindView(R.id.iv_mac_part)
    ImageView ivMacPart;
    @BindView(R.id.ll_mac_part)
    LinearLayout llMacPart;
    @BindView(R.id.gv_pic)
    GridView gvPic;
    @BindView(R.id.tv_content)
    EditText tvContent;
    @BindView(R.id.tv_issue)
    TextView tvIssue;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    Unbinder unbinder;
    private PreferencesHelper helper;
    private List<FailureDeclareListBean.PositionBean> list=new ArrayList<>();
    private List<FailureDeclareListBean> data=new ArrayList<>();

    private String machine_id="";
    private String cate_id="";
    private String position_id="";

    //图片
    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_CHOICE_PHOTO = 200;

    private final int REQUIRES_PERMISSION = 0;//申请权限
    private static final int REQUEST_CODE_SETTING = 300;
    private List<String> stringList = new ArrayList<>();
    private Map<String, List<File>> listFile = new LinkedHashMap<>();
    private Dialog mDialog;
    private File takePhotoFile;
    private CommentPicListAdapter adapter;
    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_sfailure_declare_fragment, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(getActivity());
        titleBar.setTitleText("故障申报");
        titleBar.setRightImageRes(R.drawable.ic_ordernumber);
        titleBar.setLeftImageRes(R.drawable.ic_geren);
        titleBar.setLeftImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SPersonalCentercActivity.class));
            }
        });
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DeclareRecordActivity.class));
            }
        });
        initPic();
    }
    private void initPic() {
        //提交照片
        adapter = new CommentPicListAdapter(mContext);
        gvPic.setAdapter(adapter);
        adapter.setList(stringList);
        gvPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (stringList != null) {
                    if (stringList.size() <= position) {
                        DialogPic(6 - stringList.size());
                    }
                } else {
                    DialogPic(6);
                }

            }
        });
    }

    @Override
    public void getData() {
        getDatas(helper.getToken());
    }

    private void getDatas(String token){
            HttpHelp.getInstance().create(RemoteApi.class).getFailureDeclareListData(token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<BaseBean<List<FailureDeclareListBean>>>(mContext, null) {
                        @Override
                        public void onNext(BaseBean<List<FailureDeclareListBean>> listBaseBean) {
                            super.onNext(listBaseBean);
                            if (listBaseBean.code == 0) {
                               data=listBaseBean.data;
                                if (data!=null&&data.size()!=0){
                                    tvMacName.setText(data.get(0).getName());
                                    tvMacNum.setText(data.get(0).getSn());
                                    list=data.get(0).getPosition();
                                    tvMacPart.setText(list.get(0).getName());
                                    tvNumber.setText(data.get(0).getBalenum());
                                    machine_id=data.get(0).getId();
                                    cate_id=data.get(0).getCate_id();
                                    position_id=list.get(0).getId();
                                }
                            } else if (listBaseBean.code==4){
                                ToolUtil.getOutLogs(mContext);
                            }
                                else {
                                ToastUtil.showToast(mContext,listBaseBean.message);
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                        }
                    });
    }
    private void submit(MultipartBody.Builder builder){
        btnSubmit.setEnabled(false);
        btnSubmit.setClickable(false);
        HttpHelp.getInstance().create(RemoteApi.class).submitFailureDeclareListData(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(mContext, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        btnSubmit.setClickable(true);
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            tvContent.setText("");
                            adapter.onClear();
                            ToastUtil.showToast(mContext,listBaseBean.message);
                        } else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(mContext);
                        }else {
                            ToastUtil.showToast(mContext,listBaseBean.message);
                        }
                        btnSubmit.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        btnSubmit.setClickable(true);
                        super.onError(throwable);
                        btnSubmit.setEnabled(true);
                    }
                });
    }
    private void initMac() {
        final List<String> arrayList=new ArrayList<>();
        if(data==null||data.size()==0){
            ToastUtil.showToast(getContext(),"暂无可选机器");
            return;
        }
        for (FailureDeclareListBean datas:data){
            arrayList.add(datas.getName());
        }
        DeclarePopWindow popWindow=new DeclarePopWindow(mContext,arrayList,llMac);
        popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_red_line));
        popWindow.setOnPopWindowItemClick(new DeclarePopWindow.setOnPopWindowItemClick() {
            @Override
            public void onItem(int pos) {
                tvMacName.setText(data.get(pos).getName());
                tvMacNum.setText(data.get(pos).getSn());
                list=data.get(pos).getPosition();
                tvNumber.setText(data.get(pos).getBalenum());
                machine_id=data.get(pos).getId();
                cate_id=data.get(pos).getCate_id();
                if (list!=null&&list.size()!=0)
                tvMacPart.setText(list.get(0).getName());
            }
        });
    }
    private void initMacPart(){
        List<String> arrayList=new ArrayList<>();
        for (FailureDeclareListBean.PositionBean datas:this.list){
            arrayList.add(datas.getName());
        }
        DeclarePopWindow popWindow=new DeclarePopWindow(mContext,arrayList,llMacPart);
        popWindow.setOnPopWindowItemClick(new DeclarePopWindow.setOnPopWindowItemClick() {
            @Override
            public void onItem(int pos) {
                tvMacPart.setText(list.get(pos).getName());
                position_id=list.get(pos).getId();
            }
        });
    }

    @OnClick({R.id.ll_mac, R.id.ll_mac_part,R.id.btn_submit,R.id.tv_issue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_mac:
                //机器
                initMac();
                break;
            case R.id.ll_mac_part:
                //故障部件
                if (list==null||list.size()==0){
                    ToastUtil.showToast(mContext,"请先选择故障机器");
                    return;
                }
                initMacPart();
                break;
            case R.id.btn_submit :
                if(TextUtils.isEmpty(tvContent.getText().toString())){
                ToastUtil.showToast(mContext,"请填写完整信息");
                return;
                }
                //提交
                String sn=tvMacNum.getText().toString();
                String content=tvContent.getText().toString();

                //上传图片不知所谓
                final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                builder.addFormDataPart("token", new PreferencesHelper(mContext).getToken())
                        .addFormDataPart("sn", sn)
                        .addFormDataPart("machine_id", machine_id)
                        .addFormDataPart("position_id", position_id)
                        .addFormDataPart("content", content)
                        .addFormDataPart("cate_id", cate_id);
                if (stringList.size() > 0 && stringList != null) {
                    new LuBanUtils().getFileList(getContext(), stringList, new LuBanUtils.OnCompressSuccessListener() {
                        @Override
                        public void onSuccess(List<File> list) {
                            for (int i = 0; i < list.size(); i++) {
                                File f = list.get(i);
                                RequestBody IDFrontBody = RequestBody.create(MediaType.parse("image/png"), f);
                                builder.addFormDataPart("upload[]", f.getName(), IDFrontBody);
                            }
                            submit(builder);
                        }
                    });

                }else {
                    submit(builder);
                }


                break;
            case R.id.tv_issue:
                //WebActivity.startActivity(mContext,"常见故障解决", Constants.HOST+"Article/article/type/2");
                ManualActivity.startMyActivity(mContext,"常见故障解决","2");
                break;
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 弹框
     */
    public void DialogPic(final int count) {
        if (!AndPermission.hasPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            requestPermission();
            return;
        }


        mDialog = new Dialog(mContext);
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_pic_gain, null);

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
                    takePhotoFile = TakePhotoUtils.takePhoto((Activity) mContext, "nongjiPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(mContext, "拍照失败");
                    e.printStackTrace();
                }
            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//相册
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(mContext, null, count, null, false), REQUEST_CODE_CHOICE_PHOTO);
            }
        });

    }

    public void setStringList(List<String> stringList) {
        if (stringList != null) {
            this.stringList = stringList;
        }
        adapter.setList(stringList);
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

                if (stringList != null) {
                    list.addAll(stringList);
                }

                path = takePhotoFile.getAbsolutePath();
                list.add(path);
                setStringList(list);
                /*listMap.put(goods_id, list);
                setListMap(listMap);*/

            }
        }

        //选择图片回调
        if (requestCode == REQUEST_CODE_CHOICE_PHOTO && resultCode == RESULT_OK) {
            LogUtil.d("选择回调成功  onActivityResult");
            try {

                List<String> lists = new ArrayList<>();

                List<String> listImg = BGAPhotoPickerActivity.getSelectedImages(data);
                if (stringList != null) {
                    lists.addAll(stringList);
                }

                lists.addAll(listImg);
                setStringList(lists);
              /*  listMap.put(goods_id + "", lists);

                setListMap(listMap);*/

            } catch (Exception e) {
                e.printStackTrace();
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
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantPermissions) {
            LogUtil.d("权限申请成功  onSucceed");
            switch (requestCode) {
                case REQUIRES_PERMISSION: {
                    ToastUtil.showToast(mContext, "权限申请成功");
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
                    if (AndPermission.hasAlwaysDeniedPermission(getActivity(), deniedPermissions)) {
                        //用自定义的提示语。
                        AndPermission.defaultSettingDialog(getActivity(), REQUEST_CODE_SETTING)
                                .setTitle("权限申请失败")
                                .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                                .setPositiveButton("好，去设置")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtil.showToast(mContext, "权限申请失败，无法评论");
                                    }
                                })
                                .show();
                    } else {
                        ToastUtil.showToast(mContext, "权限申请失败，无法评论");
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
            AlertDialog.newBuilder(mContext)
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

}
