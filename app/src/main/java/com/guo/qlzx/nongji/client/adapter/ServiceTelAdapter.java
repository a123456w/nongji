package com.guo.qlzx.nongji.client.adapter;

import android.widget.ListView;

import com.guo.qlzx.nongji.R;
import com.qlzx.mylibrary.base.BaseListAdapter;

/**
 * Created by 李 on 2018/6/5.
 */

public class ServiceTelAdapter extends BaseListAdapter<String> {
    public ServiceTelAdapter(ListView listView) {
        super(listView, R.layout.item_text_center);
    }

    @Override
    public void fillData(ViewHolder holder, int position, String model) {
        holder.setText(R.id.tv_name,model);
    }
}
