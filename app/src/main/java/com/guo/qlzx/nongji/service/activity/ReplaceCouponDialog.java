package com.guo.qlzx.nongji.service.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.service.adapter.ReplaceCouponDialogAdapter;
import com.guo.qlzx.nongji.service.bean.ReplaceListBean;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * 管理系统替换弹出框类
 */
public class ReplaceCouponDialog extends Dialog {

    private final Context mContext;
    private final String name;
    @BindView(R.id.tv_rema)
    TextView tvRema;
    @BindView(R.id.rv_view)
    RecyclerView rvView;
    @BindView(R.id.but_cancel)
    Button butCancel;
    @BindView(R.id.determine)
    Button determine;
    private List<ReplaceListBean> beanList=new ArrayList<>();
    private ReplaceCouponDialogAdapter adapter;
    private OnCancelClick onCancelClick;
    private OnSubmitClick onSubmitClick;
    private boolean isFirst=true;
    private int pos=0;
    public ReplaceCouponDialog(@NonNull Context context, String tvname) {
        super(context);
        this.mContext=context;
        this.name=tvname;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_coupon_dialog);
        ButterKnife.bind(this);
        adapter = new ReplaceCouponDialogAdapter(rvView);
        rvView.setLayoutManager(new GridLayoutManager(mContext,4));
        rvView.setAdapter(adapter);
        adapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                for (int i = 0; i < beanList.size(); i++) {
                    beanList.get(i).setChecked(false);
                }
                List<ReplaceListBean> data = adapter.getData();
                data.get(position).setChecked(true);
                pos=position;
                adapter.notifyDataSetChanged();
            }
        });

        //需要获取Token
        //调用替换框方法
        getReplaceDate(new PreferencesHelper(mContext).getToken(),5);
        initEvent();
        tvRema.setText(name);
    }
    private void initEvent() {
        butCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancelClick!=null){
                    onCancelClick.onCancel();
                }
            }
        });
        determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSubmitClick!=null){
                 onSubmitClick.onSubmit(adapter.getItem(pos).getId());
                }
            }
        });
    }

    /**
     * 弹出替换框方法
     * @param token
     * @param machine_id
     */
    private void getReplaceDate(String token,int machine_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getreplace(token,machine_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ReplaceListBean>>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<List<ReplaceListBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            beanList = listBaseBean.data;
                            if (beanList!=null&&beanList.size()>0) {
                                if (isFirst){
                                    beanList.get(0).setChecked(true);
                                    isFirst=false;
                                }
                                adapter.setData(beanList);
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    public void setOnSubmitClick(OnSubmitClick onSubmitClick) {
        this.onSubmitClick = onSubmitClick;
    }

    public void setOnCancelClick(OnCancelClick onCancelClick) {
        this.onCancelClick = onCancelClick;
    }

    interface OnSubmitClick{
        void onSubmit(String id);
    }
    interface OnCancelClick{
        void onCancel();
    }
}
