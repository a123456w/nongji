package com.guo.qlzx.nongji.service.adapter;

import android.widget.GridView;
import android.widget.ListView;

import com.guo.qlzx.nongji.R;
import com.qlzx.mylibrary.base.BaseListAdapter;

public class LogRecordDetailsPartAdapter extends BaseListAdapter<String> {
    public LogRecordDetailsPartAdapter(GridView listView) {
        super(listView, R.layout.item_logrecord_part);
    }

    @Override
    public void fillData(ViewHolder holder, int position, String model) {
        holder.setText(R.id.tv_text,model);
    }
}
