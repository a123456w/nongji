package com.guo.qlzx.nongji.service.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.commen.util.ToolUtil;
import com.guo.qlzx.nongji.service.bean.RechargeRecordListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/6/4.
 */

public class RechargeRecordAdapter extends RecyclerViewAdapter<RechargeRecordListBean> {
    public RechargeRecordAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_recharge_record);
    }

    private int type=0;

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, RechargeRecordListBean model) {

        viewHolderHelper.setText(R.id.tv_time, ToolUtil.getStrTime(model.getCreate_time(),"yyyy年MM月dd日"));
        if ("0".equals(model.getPay_type())){
            viewHolderHelper.setText(R.id.tv_memory,"+"+model.getMoney());
        }else {
            viewHolderHelper.setText(R.id.tv_memory,"-"+model.getMoney());
        }


        if (type==0){
            viewHolderHelper.setText(R.id.tv_name,model.getType());
        }else {
            String mType=model.getType();
            //类型:1充值余额,2充值押金,3提现余额,4提现押金,5违规扣押金,6订单正常扣费，7充值优惠金
            if ("1".equals(mType)){
                viewHolderHelper.setText(R.id.tv_name,"充值余额");
            }else if ("2".equals(mType)){
                viewHolderHelper.setText(R.id.tv_name,"充值押金");
            }else if ("3".equals(mType)){
                viewHolderHelper.setText(R.id.tv_name,"提现余额");
            }else if ("4".equals(mType)){
                viewHolderHelper.setText(R.id.tv_name,"提现押金");
            }else if ("5".equals(mType)){
                viewHolderHelper.setText(R.id.tv_name,"违规扣押金");
            }else if ("6".equals(mType)){
                viewHolderHelper.setText(R.id.tv_name,"订单正常扣费");
            }else if ("7".equals(mType)){
                viewHolderHelper.setText(R.id.tv_name,"充值优惠金");
            }
        }

    }
}
