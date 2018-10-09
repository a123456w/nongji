package com.guo.qlzx.nongji.service.adapter;

import android.view.View;
import android.widget.ListView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.service.bean.OrderDetailsBean;
import com.guo.qlzx.nongji.utils.ColorUtils;
import com.qlzx.mylibrary.base.BaseListAdapter;

/**
 * Created by 李 on 2018/6/14.
 */

public class OrderDetailsPieAdapter extends BaseListAdapter<OrderDetailsBean.ContributionBean> {
    public OrderDetailsPieAdapter(ListView listView) {
        super(listView, R.layout.item_order_pic);
    }

    @Override
    public void fillData(ViewHolder holder, int position, OrderDetailsBean.ContributionBean model) {
        holder.setText(R.id.tv_mac,model.getName());
        holder.setText(R.id.tv_percent,model.getScale2());
        holder.setText(R.id.tv_rental,model.getMoney()+"");

        if (0==position){
            holder.setVisibility(R.id.iv_color, View.GONE);
            holder.getTextView(R.id.tv_mac).setTextSize(13);
            holder.getTextView(R.id.tv_percent).setTextSize(13);
            holder.getTextView(R.id.tv_rental).setTextSize(13);
        }else {
            holder.getTextView(R.id.tv_mac).setTextSize(12);
            holder.getTextView(R.id.tv_percent).setTextSize(12);
            holder.getTextView(R.id.tv_rental).setTextSize(12);
            holder.setVisibility(R.id.iv_color, View.VISIBLE);
            holder.setBackgroundColor(R.id.iv_color, ColorUtils.getColor(position-1));
        }

    }
}
