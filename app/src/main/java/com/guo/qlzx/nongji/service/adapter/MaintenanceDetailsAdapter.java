package com.guo.qlzx.nongji.service.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.bean.MaintenanceDetailsBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by Administrator on 2018/6/1.
 */

public class MaintenanceDetailsAdapter extends RecyclerViewAdapter<MaintenanceDetailsBean> {

    public MaintenanceDetailsAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_maintenancedetails);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, MaintenanceDetailsBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getPic(),viewHolderHelper.getImageView(R.id.im_ji));
        viewHolderHelper.setText(R.id.tv_model,model.getModel());
        viewHolderHelper.setText(R.id.tv_tim, "预计剩余"+model.getNext_maintime()+"小时需要保养");
    }
}
