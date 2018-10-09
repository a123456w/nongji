package com.guo.qlzx.nongji.service.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.activity.OrderDetails;
import com.guo.qlzx.nongji.service.bean.OrderListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/5/30.
 */

public class OrderAdapter extends RecyclerViewAdapter<OrderListBean> {
    public OrderAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_order);
    }

    private OrderItemAdapter adapter;
    public void setContext(Context mContext){
        this.mContext=mContext;
    }
    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, OrderListBean model) {
        viewHolderHelper.setText(R.id.tv_order_id,"订单号："+model.getOrder_sn());
        if ("1".equals(model.getOrder_status())){
            viewHolderHelper.setText(R.id.tv_state,"进行中");
            viewHolderHelper.getTextView(R.id.tv_state).setTextColor(mContext.getResources().getColor(R.color.red_e7));
        }else {
            viewHolderHelper.setText(R.id.tv_state,"已完成");
            viewHolderHelper.getTextView(R.id.tv_state).setTextColor(mContext.getResources().getColor(R.color.textcolor6));
        }
        viewHolderHelper.setText(R.id.tv_name,model.getUser_name());
        viewHolderHelper.setText(R.id.tv_date, ToolUtil.getStrTime(model.getCreate_time(),"yyyy-MM-dd"));
        viewHolderHelper.setText(R.id.tv_time,ToolUtil.getStrTime(model.getStart_time(),"yyyy-MM-dd")+
                "至"+ToolUtil.getStrTime(model.getEnd_time(),"yyyy-MM-dd")+"("+model.getTotal_days()+"天)");
        viewHolderHelper.setText(R.id.tv_lose_day,model.getSurplus_days()+"天");
        viewHolderHelper.setText(R.id.tv_price,model.getOrder_aumont());
        GridView gridView=viewHolderHelper.getView(R.id.gv_Goods);
        adapter=new OrderItemAdapter(gridView);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(mContext,OrderDetails.class);
                intent.putExtra("ORDERID",getItem(position).getId());
                mContext.startActivity(intent);
            }
        });
        adapter.setData(model.getMachine());
    }
}
