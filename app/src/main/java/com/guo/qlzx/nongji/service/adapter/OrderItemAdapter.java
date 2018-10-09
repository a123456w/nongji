package com.guo.qlzx.nongji.service.adapter;

import android.widget.GridView;
import android.widget.ListView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.service.bean.OrderListBean;
import com.guo.qlzx.nongji.service.bean.OrderMacBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by 李 on 2018/5/30.
 */

public class OrderItemAdapter extends BaseListAdapter<OrderMacBean> {
    public OrderItemAdapter(GridView listView) {
        super(listView, R.layout.item_order_grid);
    }

    @Override
    public void fillData(ViewHolder holder, int position, OrderMacBean model) {
        holder.setText(R.id.tv_name,"x"+model.getNum()+"("+model.getWorkunit()+"/"+model.getUnit()+")");
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getPic(),holder.getImageView(R.id.iv_img));
    }
}
