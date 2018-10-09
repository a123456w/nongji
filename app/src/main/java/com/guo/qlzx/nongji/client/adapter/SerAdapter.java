package com.guo.qlzx.nongji.client.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.SerBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by Administrator on 2018/6/4.
 */

public class SerAdapter extends RecyclerViewAdapter<SerBean> {
    public SerAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_ser);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, SerBean model) {
    viewHolderHelper.setText(R.id.tv_kefu,model.getPhone_number());
    }
}
