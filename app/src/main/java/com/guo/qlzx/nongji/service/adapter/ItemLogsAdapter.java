package com.guo.qlzx.nongji.service.adapter;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.service.bean.AddLayoutBean;
import com.qlzx.mylibrary.base.BaseListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/6.
 * 增加明细
 */

public class ItemLogsAdapter extends BaseListAdapter<AddLayoutBean> {
    private List<AddLayoutBean> listData;
    private EditText buwei;
    private EditText edModel;
    private EditText edNum;

    OnEditAssignment onEditAssignment;

    public ItemLogsAdapter(ListView listView, List<AddLayoutBean> listData) {
        super(listView, R.layout.item_logs_asrvices);
        this.listData = listData;
    }

    @Override
    public void fillData(ViewHolder holder, final int position, final AddLayoutBean model) {
        final AddLayoutBean addLayoutBean = new AddLayoutBean();
        TextView dele = holder.getView(R.id.tv_dele);
        buwei = holder.getView(R.id.ed_buwei);
        edModel = holder.getView(R.id.ed_model);
        edNum = holder.getView(R.id.ed_num);
        dele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AddLayoutBean> data = getData();
                data.remove(position);
                listData.remove(position);
                notifyDataSetChanged();
                if (onEditAssignment != null)
                    onEditAssignment.onRemoveLayout();
            }
        });

        TextWatcher buweiWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    addLayoutBean.setStrBuwei(s.toString());
                    addLayoutBean.setId(position);
                }
                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(addLayoutBean.getStrEdModel()) && !TextUtils.isEmpty(addLayoutBean.getStrEdNum())) {
                    onEditAssignment.onEditAssignment(addLayoutBean);
                }else {
                    onEditAssignment.onEditUnAssignment(addLayoutBean);
                }
            }
        };
        buwei.addTextChangedListener(buweiWatcher);
        TextWatcher edModelWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    addLayoutBean.setStrEdModel(s.toString());
                    addLayoutBean.setId(position);
                }
                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(addLayoutBean.getStrEdModel()) && !TextUtils.isEmpty(addLayoutBean.getStrEdNum())) {
                    onEditAssignment.onEditAssignment(addLayoutBean);
                }else {
                    onEditAssignment.onEditUnAssignment(addLayoutBean);
                }
            }
        };
        edModel.addTextChangedListener(edModelWatcher);
        TextWatcher edNumWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    addLayoutBean.setStrEdNum(s.toString());
                    addLayoutBean.setId(position);
                }
                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(addLayoutBean.getStrEdModel()) && !TextUtils.isEmpty(addLayoutBean.getStrEdNum())) {
                    onEditAssignment.onEditAssignment(addLayoutBean);
                }else {
                    onEditAssignment.onEditUnAssignment(addLayoutBean);
                }
            }
        };
        edNum.addTextChangedListener(edNumWatcher);
    }


    private boolean isAssignment(String s) {
        boolean b = false;

        if (s.toString().trim() != null && !s.toString().trim().equals("")) {
            b = true;
        }
        return b;
    }

//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//    }

    //    @Override
//    public void afterTextChanged(Editable s) {
//
//        if (isAssignment(buwei) && isAssignment(edModel) && isAssignment(edNum) && onEditAssignment != null) {
//
//            addLayoutBean.setStrBuwei(buwei.getText().toString());
//            addLayoutBean.setStrEdModel(edModel.getText().toString());
//            addLayoutBean.setStrEdNum(edNum.getText().toString());
//            if (addLayoutBean.getId() == 0) {
//                addLayoutBean.setId(System.currentTimeMillis());
//            }
//            onEditAssignment.onEditAssignment(addLayoutBean);
//        } else if (onEditAssignment != null) {
//            onEditAssignment.onEditUnAssignment(addLayoutBean);
//        }
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    public interface OnEditAssignment {
        //填写完毕回调
        void onEditAssignment(AddLayoutBean addLayoutBean);

        //删除一项回调
        void onEditUnAssignment(AddLayoutBean addLayoutBean);

        void onRemoveLayout();
    }

    public void setOnEditAssignment(OnEditAssignment onEditAssignment) {
        this.onEditAssignment = onEditAssignment;
    }
}
