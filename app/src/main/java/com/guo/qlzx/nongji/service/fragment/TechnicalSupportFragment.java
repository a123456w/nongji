package com.guo.qlzx.nongji.service.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.activity.ManualActivity;
import com.guo.qlzx.nongji.client.activity.WebActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogsSrrvicesActivity;
import com.guo.qlzx.nongji.service.activity.MaintainingActivity;
import com.guo.qlzx.nongji.service.activity.MessageActivity;
import com.guo.qlzx.nongji.service.activity.PersonalCenterActivity;
import com.guo.qlzx.nongji.service.bean.LogsSrrvicesBean;
import com.guo.qlzx.nongji.service.bean.SendBean;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 技术支持
 */
public class TechnicalSupportFragment extends BaseFragment {
    @BindView(R.id.tv_servelog)
    LinearLayout tvServelog;
    @BindView(R.id.tv_maintain)
    LinearLayout tvMaintain;
    @BindView(R.id.tv_helps)
    LinearLayout tvHelps;
    Unbinder unbinder;
    @BindView(R.id.vi_a)
    View viA;
    @BindView(R.id.vi_b)
    View viB;
    @BindView(R.id.tv_imes)
    ImageView tvImes;
    private List<LogsSrrvicesBean> listdata=new ArrayList<>();

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_technical_support_fragment2, frameLayout, false);
        ButterKnife.bind(this, view);
        /**
         *  服务日志
         */
        tvServelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvImes.setVisibility(View.GONE);
                startActivity(new Intent(getActivity(), LogsSrrvicesActivity.class));
            }
        });
        /**
         *  维护保养
         */
        tvMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MaintainingActivity.class));

            }
        });
        /**
         *  使用帮助
         */
        tvHelps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WebActivity.startActivity(mContext, "使用帮助", Constants.HOST + "Article/article/type/8");
                ManualActivity.startMyActivity(mContext,"使用帮助","8");
            }
        });
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);

        titleBar.setTitleText("技术支持");
     //   titleBar.setRightImageRes(R.drawable.ic_xiaoxi);
        titleBar.setLeftImageRes(R.drawable.ic_geren);
        titleBar.setLeftImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PersonalCenterActivity.class));
            }
        });
    }

    @Override
    public void getData() {
        EventBusUtil.register(  this);
        PreferencesHelper helper = new PreferencesHelper(mContext);
        tvImes.setVisibility(View.GONE);
        getServiceJourDate( new PreferencesHelper(mContext).getToken());

        String userRole = helper.getUserRole();
        if (userRole.equals("5")) {
            tvServelog.setVisibility(View.GONE); // 隐藏
            tvMaintain.setVisibility(View.GONE); // 隐藏
            viB.setVisibility(View.GONE);
            viA.setVisibility(View.GONE);
        }
    }

    /**
     * 未完成案件点击已完成的回调
     *
     * @param evet
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SendBean(SendBean evet) {
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBusUtil.unregister(this);
    }

    /**
     * 日志列表方法
     */
    public void getServiceJourDate(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getServiceJour(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<LogsSrrvicesBean>>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<List<LogsSrrvicesBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            listdata = listBaseBean.data;
                            if (listdata != null && listdata.size() > 0) {
                                tvImes.setVisibility(View.VISIBLE);
                            } else {
                                //加载图片
                               // loadingLayout.setStatus(LoadingLayout.Empty);
                                tvImes.setVisibility(View.GONE);

                            }
                        }
                        else if (listBaseBean.code==4){
                            ToolUtil.getOutLogs(mContext);
                        }else {
                            ToastUtil.showToast(mContext,listBaseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });


    }
}
