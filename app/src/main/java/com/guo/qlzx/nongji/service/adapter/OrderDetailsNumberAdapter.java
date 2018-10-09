package com.guo.qlzx.nongji.service.adapter;

import android.widget.GridView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.service.bean.OrderDetailsBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by 李 on 2018/5/31.
 */

public class OrderDetailsNumberAdapter extends BaseListAdapter<OrderDetailsBean.MacBean> {
    public OrderDetailsNumberAdapter(GridView listView) {
        super(listView, R.layout.item_order_details_number);
    }

    @Override
    public void fillData(ViewHolder holder, int position, OrderDetailsBean.MacBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getPic(),holder.getImageView(R.id.iv_img));
        holder.setText(R.id.tv_name,model.getName()+"");
    }
}
