package com.guo.qlzx.nongji.service.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.service.bean.CommitAddListBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by Administrator on 2018/5/28.
 */

public class AddMachinistAdapter extends RecyclerViewAdapter<CommitAddListBean>{

    public AddMachinistAdapter(RecyclerView recyclerView) {
        super(recyclerView,R.layout.add_item);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, CommitAddListBean model) {
        viewHolderHelper.setText(R.id.tv_add,"机手:"+model.getMachine_name());
    }
}
