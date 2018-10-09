package com.guo.qlzx.nongji.client.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.activity.SPersonalCentercActivity;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.LogRecordDetailsActivity;
import com.guo.qlzx.nongji.service.activity.StatisticsActivity;
import com.guo.qlzx.nongji.service.adapter.LogrecordAdapter;
import com.guo.qlzx.nongji.service.bean.LogrecordBean;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 客户端日志
 */
public class SLogRecordFragment extends BaseFragment implements OnRVItemClickListener{


    @BindView(R.id.re_review)
    RecyclerView reReview;
    private PreferencesHelper helper;
    private LogrecordAdapter adapter;
    private List<LogrecordBean> date1;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_slog_record_fragment, frameLayout, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void initView() {
        ButterKnife.bind(getActivity());
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(getActivity());
        titleBar.setTitleText("日志");
        titleBar.setRightImageRes(R.drawable.ic_statistics);
        titleBar.setLeftImageRes(R.drawable.ic_geren);
        titleBar.setLeftImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SPersonalCentercActivity.class));
            }
        });
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),StatisticsActivity.class));
            }
        });
        adapter = new LogrecordAdapter(reReview);
        reReview.setLayoutManager(new LinearLayoutManager(getActivity()));
        reReview.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);
        reReview.addItemDecoration(new RecycleViewDivider(getActivity()));

    }

    @Override
    public void getData() {
        String token = helper.getToken();
        //日志详情
        getJour(new PreferencesHelper(mContext).getToken());
    }
    /**
     * 日志详情
     * @param token
     */
    private void getJour(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getjournal(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<LogrecordBean>>>(getActivity(), null) {
                    @Override
                    public void onNext(BaseBean<List<LogrecordBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            date1 = listBaseBean.data;
                            adapter.setData(date1);
                        }else if (listBaseBean.code==4){
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
    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(getActivity(),LogRecordDetailsActivity.class);
        intent.putExtra("sn",adapter.getItem(position).getMachine_id());
        intent.putExtra("homepagesn",adapter.getItem(position).getName());
        startActivity(intent);
    }
}
