package com.guo.qlzx.nongji.client.Costom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.adapter.FailureDeclareAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李 on 2018/5/31.
 */

public class DeclarePopWindow extends PopupWindow {

    private List<String> list=new ArrayList<>();
    private Context mContext;
    private View llMac;
    private setOnPopWindowItemClick onPopWindowItemClick;
    public DeclarePopWindow(Context mContext, List<String> list,View mac){
        this.list=list;
        this.mContext=mContext;
        this.llMac=mac;
        initData();
    }

    private void initData() {
        View contentView=null;
        if (contentView==null) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_listview, null, false);
        }
        ListView listView = (ListView) contentView.findViewById(R.id.lv_list);
        FailureDeclareAdapter adapter=new FailureDeclareAdapter(listView);
        listView.setAdapter(adapter);
        adapter.setData(list);
        final PopupWindow window=new PopupWindow(contentView, 100, 100, true);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        ViewGroup.LayoutParams params=  llMac.getLayoutParams();
        window.setWidth(params.width);
        window.setHeight(params.height * 3);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                if (window != null) {
                    window.dismiss();
                    if (onPopWindowItemClick!=null){
                        onPopWindowItemClick.onItem(position);
                    }
                }
            }
        });
        window.showAsDropDown(llMac, 0,0);

    }

    public void setOnPopWindowItemClick(setOnPopWindowItemClick onPopWindowItemClick) {
        this.onPopWindowItemClick = onPopWindowItemClick;
    }

    public interface setOnPopWindowItemClick{
        void onItem(int pos);
    }
}
