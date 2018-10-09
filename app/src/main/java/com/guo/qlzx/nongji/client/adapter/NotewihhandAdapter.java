package com.guo.qlzx.nongji.client.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.NotewithhandListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/6/4.
 * 钱包-随手记
 */

public class NotewihhandAdapter extends RecyclerViewAdapter<NotewithhandListBean.ListBean> {
    public NotewihhandAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_notewithhand);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, NotewithhandListBean.ListBean model) {
        viewHolderHelper.setText(R.id.tv_refuel,model.getLabel());
        viewHolderHelper.setText(R.id.tv_times, new SimpleDateFormat("yyyy-MM-dd").format(Long.parseLong(model.getCreate_time())*1000));
        viewHolderHelper.setText(R.id.tv_money,(model.getType().equals("1")?"-":"+")+model.getMoney());
        viewHolderHelper.setText(R.id.tv_remark,model.getContent());

    }
}
