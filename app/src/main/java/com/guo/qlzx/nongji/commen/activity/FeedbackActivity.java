package com.guo.qlzx.nongji.commen.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.adapter.CommentPicListAdapter;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
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
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ctreate by Xuxx
 *  意见反馈
 * @date 2018/06/26
 */
public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.ed_content)
    EditText edContent;
    @BindView(R.id.gv_pic)
    GridView gvPic;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    CommentPicListAdapter adapter;
    private List<String> stringList = new ArrayList<>();
    private final int REQUIRES_PERMISSION = 0;//申请权限
    private static final int REQUEST_CODE_SETTING = 300;
    private Dialog mDialog;
    private File takePhotoFile;
    //图片
    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_CHOICE_PHOTO = 200;

    @Override
    public int getContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        initPic();
        titleBar.setTitleText("意见反馈");
    }

    private void initPic() {
        //提交照片
        adapter = new CommentPicListAdapter(context);
        gvPic.setAdapter(adapter);
        adapter.setMaxNumber(4);
        adapter.setList(stringList);
        gvPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (stringList != null) {
                    if (stringList.size() <= position) {
                        DialogPic(4 - stringList.size());
                    }
                } else {
                    DialogPic(4);
                }

            }
        });
    }

    /**
     * 弹框
     */
    public void DialogPic(final int count) {
        if (!AndPermission.hasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            requestPermission();
            return;
        }


        mDialog = new Dialog(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_pic_gain, null);

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
                    takePhotoFile = TakePhotoUtils.takePhoto((Activity) context, "nongjiPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(context, "拍照失败");
                    e.printStackTrace();
                }
            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//相册
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(context, null, count, null, false), REQUEST_CODE_CHOICE_PHOTO);
            }
        });

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

    @Override
    public void getData() {

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
                    ToastUtil.showToast(context, "权限申请成功");
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
                    if (AndPermission.hasAlwaysDeniedPermission((Activity) context, deniedPermissions)) {
                        //用自定义的提示语。
                        AndPermission.defaultSettingDialog((Activity) context, REQUEST_CODE_SETTING)
                                .setTitle("权限申请失败")
                                .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                                .setPositiveButton("好，去设置")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtil.showToast(context, "权限申请失败，无法评论");
                                    }
                                })
                                .show();
                    } else {
                        ToastUtil.showToast(context, "权限申请失败，无法评论");
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
            AlertDialog.newBuilder(context)
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

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setStringList(List<String> stringList) {
        if (stringList != null) {
            this.stringList = stringList;
        }
        adapter.setList(stringList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation

    }

    @OnClick(R.id.btn_submit)
    public void onClick() {
        if (null == edContent.getText().toString().trim() || edContent.getText().toString().trim().equals("")) {
            ToastUtil.showToast(context, "请输入文字");
            return;
        }
        final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", new PreferencesHelper(context).getToken())
                .addFormDataPart("content", edContent.toString().trim());
        if (stringList.size() > 0 && stringList != null) {
            new LuBanUtils().getFileList(context, stringList, new LuBanUtils.OnCompressSuccessListener() {
                @Override
                public void onSuccess(List<File> list) {
                    for (int i = 0; i < list.size(); i++) {
                        File f = list.get(i);
                        RequestBody IDFrontBody = RequestBody.create(MediaType.parse("image/png"), list.get(i));
                        builder.addFormDataPart("upload[]", list.get(i).getName(), IDFrontBody);
                    }
                    getData(builder.build());
                }
            });


        }else {
            getData(builder.build());
        }

    }

    private void getData(RequestBody body){
        btnSubmit.setClickable(false);
        HttpHelp.getInstance().create(RemoteApi.class).getSubmit(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(context, null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        btnSubmit.setClickable(true);
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                           ToastUtil.showToast(context,"提交成功");
                           finish();
                        } else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(context);
                        }else {
                            ToastUtil.showToast(context,listBaseBean.message);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        btnSubmit.setClickable(true);
                        super.onError(throwable);
                    }
                });
    }
}
