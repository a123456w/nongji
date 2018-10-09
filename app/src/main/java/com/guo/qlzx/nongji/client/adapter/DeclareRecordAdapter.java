package com.guo.qlzx.nongji.client.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.DeclareRecordListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/5/31.
 */

public class DeclareRecordAdapter extends RecyclerViewAdapter<DeclareRecordListBean> {
    public DeclareRecordAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_declare_record);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, DeclareRecordListBean model) {

        viewHolderHelper.setText(R.id.tv_name,model.getContent());
        viewHolderHelper.setText(R.id.tv_content,"机器名称："+model.getMachine_name());
        //0未处理
        if ("0".equals(model.getIs_check())){
            viewHolderHelper.setText(R.id.tv_state,"未处理");
            viewHolderHelper.getTextView(R.id.tv_state).setTextColor(mContext.getResources().getColor(R.color.red_e7));
        }else {
            viewHolderHelper.setText(R.id.tv_state,"已处理");
            viewHolderHelper.getTextView(R.id.tv_state).setTextColor(mContext.getResources().getColor(R.color.textcolor6));
        }

    }
}
