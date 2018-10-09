package com.guo.qlzx.nongji.client.adapter;

import android.widget.ListView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.FailureDeclareListBean;
import com.qlzx.mylibrary.base.BaseListAdapter;

/**
 * Created by 李 on 2018/5/31.
 */

public class FailureDeclareAdapter extends BaseListAdapter<String> {
    public FailureDeclareAdapter(ListView listView) {
        super(listView, R.layout.item_text);
    }

    @Override
    public void fillData(ViewHolder holder, int position, String model) {
            holder.setText(R.id.tv_name,model);
    }
}
