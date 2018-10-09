package com.guo.qlzx.nongji.service.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.bean.LogrecordBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by Administrator on 2018/6/1.
 * 日志适配器
 */

public class LogrecordAdapter extends RecyclerViewAdapter<LogrecordBean> {
    public LogrecordAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_logfragment);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, LogrecordBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getPic(),viewHolderHelper.getImageView(R.id.ic_machineimage));
        viewHolderHelper.setText(R.id.tv_robot,model.getName());
        viewHolderHelper.setText(R.id.tv_owner,"机主："+model.getUser_name());
        viewHolderHelper.setText(R.id.tv_serve,"服务："+model.getService_name());
        viewHolderHelper.setText(R.id.tv_tractordriver,"机手："+model.getOperator_name());
        if ("".equals(model.getLast_time())){
            viewHolderHelper.setText(R.id.servetrim,"暂无服务");
        }else {
            viewHolderHelper.setText(R.id.servetrim,"上次服务："+ToolUtil.getStrTime(model.getLast_time(),"yyyy.MM.dd"));
        }

    }
}
