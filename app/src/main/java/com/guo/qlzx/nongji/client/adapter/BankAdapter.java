package com.guo.qlzx.nongji.client.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.BankListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by 李 on 2018/6/4.
 */

public class BankAdapter extends RecyclerViewAdapter<BankListBean> {
    public BankAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_bank_card);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, BankListBean model) {
        GlideUtil.displayAvatar(mContext, Constants.IMG_HOST+model.getPic(),viewHolderHelper.getImageView(R.id.iv_img));
        viewHolderHelper.setText(R.id.tv_name,model.getBank());
        viewHolderHelper.setText(R.id.tv_card,model.getCard_type());
        viewHolderHelper.setText(R.id.tv_number,model.getCard_number());
    }
}
