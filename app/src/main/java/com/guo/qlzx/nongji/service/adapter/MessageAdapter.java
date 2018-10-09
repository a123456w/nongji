package com.guo.qlzx.nongji.service.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.service.bean.MessageListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/5/29.
 */

public class MessageAdapter extends RecyclerViewAdapter<MessageListBean> {
    public MessageAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_message);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, MessageListBean model) {
        viewHolderHelper.setText(R.id.tv_name,model.getContent());
        if ("1".equals(model.getIs_read())){
            //已读
            viewHolderHelper.getTextView(R.id.tv_name).setTextColor(mContext.getResources().getColor(R.color.textcolor9));
        }else {
            viewHolderHelper.getTextView(R.id.tv_name).setTextColor(mContext.getResources().getColor(R.color.textcolor3));
        }
        viewHolderHelper.setItemChildClickListener(R.id.btn_delete);
        viewHolderHelper.setItemChildClickListener(R.id.ll_content);
    }
}
