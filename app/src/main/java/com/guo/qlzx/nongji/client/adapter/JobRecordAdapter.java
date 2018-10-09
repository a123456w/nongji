package com.guo.qlzx.nongji.client.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.JobRecordListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by 李 on 2018/5/31.
 */

public class JobRecordAdapter extends RecyclerViewAdapter<JobRecordListBean> {
    public JobRecordAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_job_record);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, JobRecordListBean model) {
        viewHolderHelper.setText(R.id.tv_name,model.getName());
        viewHolderHelper.setText(R.id.tv_count,model.getBalenum()+"捆");
        viewHolderHelper.setText(R.id.tv_time,model.getHour()+"小时");
        viewHolderHelper.setText(R.id.tv_speed,model.getEfficiency()+"捆/小时");
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getPic(),viewHolderHelper.getImageView(R.id.iv_img));
    }
}
