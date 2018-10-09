package com.guo.qlzx.nongji.service.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.bean.MaintainingBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by Administrator on 2018/6/1.
 * 维护保养适配器
 */

public class MaintainingAdapter extends RecyclerViewAdapter<MaintainingBean> {
    public MaintainingAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_maintaining);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, MaintainingBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getPic(),viewHolderHelper.getImageView(R.id.im_jiqi));
        viewHolderHelper.setText(R.id.tv_robot,model.getName());
        if ("".equals(model.getCreate_time())){
            viewHolderHelper.setText(R.id.tv_time,"上次保养时间：暂未保养");
        }else {
            viewHolderHelper.setText(R.id.tv_time, "上次保养时间：" +
                    ToolUtil.getStrTime(model.getCreate_time(), "yyyy年MM月dd日"));
        }
    }
}
