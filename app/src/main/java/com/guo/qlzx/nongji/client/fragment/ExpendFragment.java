package com.guo.qlzx.nongji.client.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.adapter.ExpendAdapter;
import com.guo.qlzx.nongji.client.bean.AddExpendBean;
import com.guo.qlzx.nongji.client.bean.ExpendBean;
import com.guo.qlzx.nongji.client.event.OnShowDataDialog;
import com.guo.qlzx.nongji.client.event.TagForStart;
import com.guo.qlzx.nongji.commen.http.RemoteApi;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 随手记支出
 */
public class ExpendFragment extends BaseFragment {

    @BindView(R.id.re_cle)
    RecyclerView reCle;
    @BindView(R.id.tv_rili)
    TextView tvRili;
    @BindView(R.id.ed_money)
    EditText edMoney;
    @BindView(R.id.but_com)
    Button butCom;
    Unbinder unbinder1;
    Unbinder unbinder;
    @BindView(R.id.ed_comment)
    EditText edComment;
    private ExpendAdapter adapter;
    private PreferencesHelper helper;
    private List<ExpendBean> data;
    private String name;
    private List<AddExpendBean> listdata = new ArrayList<>();
    private Object data1;
    private String id;
    private boolean isFirst = true;
    private int pos = 0;
    private String id2;
    private EditText edad;
    private OnShowDataDialog onShowDataDialog;
    private ExpendBean expendBean;
    //是否选择标签
    private Boolean flag = false;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_expend_fragment, frameLayout, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setVisibility(View.GONE);
        adapter = new ExpendAdapter(reCle);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tvRili.setText(simpleDateFormat.format(Calendar.getInstance().getTimeInMillis()));
        reCle.setLayoutManager(new GridLayoutManager(getContext(), 4));
        reCle.setAdapter(adapter);
        adapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                for (int i = 0; i < adapter.getData().size(); i++) {
                    ExpendBean expendBean = adapter.getData().get(i);
                    expendBean.setChecked(false);
                }
                expendBean = adapter.getData().get(position);
                expendBean.setChecked(true);
                if ("-1".equals(adapter.getItem(position).getId())) {
                    inputTitleDialog();
                    id = expendBean.getId();
                } else {
                    ExpendBean expendBean = adapter.getData().get(position);
                    expendBean.setChecked(true);
                    id = expendBean.getId();
                }
                adapter.notifyDataSetChanged();
            }
        });
        butCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = edMoney.getText().toString();
                if (id == null || id.equals("")) {
                    ToastUtil.showToast(getActivity(), "请选择标签");
                    return;
                }
                if (money == null || money.equals("")) {
                    ToastUtil.showToast(getActivity(), "请填写金额");
                    return;
                }
                String comment = edComment.getText().toString();
                getCommitAddLabelDate(new PreferencesHelper(mContext).getToken(), id, money, ToolUtil.getTimeStamp(tvRili.getText().toString(), "yyyy-MM-dd"), 1, comment);
                Log.d("ExpendFragment", tvRili.getText().toString() + "++++" + ToolUtil.getTimeStamp(tvRili.getText().toString(), "yyyy-MM-dd HH:mm"));
                getCommitAddLabelDate(new PreferencesHelper(mContext).getToken(), id, money, ToolUtil.getTimeStamp(tvRili.getText().toString(), "yyyy-MM-dd HH"), 1, comment);

            }
        });

    }

    @Override
    public void getData() {
        getLabelDate(new PreferencesHelper(mContext).getToken(), 1);
    }

    /**
     * 获取标签
     */
    private void getLabelDate(String token, int type) {
        HttpHelp.getInstance().create(RemoteApi.class).getLabel(token, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ExpendBean>>>(getContext(), null) {
                    @Override
                    public void onNext(BaseBean<List<ExpendBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            data = listBaseBean.data;
                            name = listBaseBean.getClass().getName();
                            if (flag && data.size() > 0)
                                data.get(data.size() - 1).setChecked(true);
                            else if (data.size() > 0)
                                data.get(data.size() - 1).setChecked(false);
                            data.add(new ExpendBean("-1", "点击添加"));
                            if (null != data && data.size() > 0) {
                                adapter.setData(data);
                                reCle.setVisibility(View.VISIBLE);
                            }
                        } else if (listBaseBean.code == 4) {
                            ToolUtil.getOutLogs(mContext);
                        } else {
                            ToastUtil.showToast(mContext, listBaseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 添加标签
     */
    private void getAddLabelDate(String token, int type, String label_name) {
        HttpHelp.getInstance().create(RemoteApi.class).getAddLabel(token, type, label_name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<AddExpendBean>>>(getContext(), null) {
                    @Override
                    public void onNext(BaseBean<List<AddExpendBean>> listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            listdata = listBaseBean.data;
                            flag = true;
                            getLabelDate(new PreferencesHelper(mContext).getToken(), 1);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 提交
     */
    private void getCommitAddLabelDate(String token, String label_id, String money, long create_time, int type, String content) {
        HttpHelp.getInstance().create(RemoteApi.class).getRecord(token, label_id, create_time, money, type, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(getContext(), null) {
                    @Override
                    public void onNext(BaseBean listBaseBean) {
                        super.onNext(listBaseBean);
                        if (listBaseBean.code == 0) {
                            data1 = listBaseBean.data;
                            ToastUtil.showToast(getContext(), "提交成功");
                            getActivity().setResult(TagForStart.Notewithhand);
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(getContext(), "提交失败");
                    }
                });
    }

    /**
     * 添加按钮弹框方法
     */
    private void inputTitleDialog() {

        final Dialog bottomDialog = new Dialog(getActivity(), R.style.BottomDialog);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add, null);
        final Button com = contentView.findViewById(R.id.but_cans);
        final Button coms = contentView.findViewById(R.id.but_cants);
        edad = contentView.findViewById(R.id.ed_ad);
        coms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.cancel();
                if (data.size() > 0) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setChecked(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = edad.getText().toString();
                getAddLabelDate(new PreferencesHelper(getActivity()).getToken(), 1, inputName);

                bottomDialog.cancel();
            }
        });
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        contentView.setLayoutParams(layoutParams);
        bottomDialog.show();
    }

    public void setOnShowDataDialog(OnShowDataDialog onShowDataDialog) {
        this.onShowDataDialog = onShowDataDialog;
    }

    public void setDataText(String data) {
        tvRili.setText(data);
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
    }

    @OnClick(R.id.tv_rili)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_rili: //显示日期选择
                if (onShowDataDialog != null) {
                    onShowDataDialog.onShowDialog(tvRili.getText().toString());
                }
                break;
            case R.id.iv_picc: //显示日期选择
                if (onShowDataDialog != null) {
                    onShowDataDialog.onShowDialog(tvRili.getText().toString());
                }
                break;
        }
    }
}
