package com.guo.qlzx.nongji.client.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.BankListBean;
import com.guo.qlzx.nongji.client.bean.ExpendBean;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.ToastUtil;

/**
 * Created by 李 on 2018/6/4.
 */

public class ExpendAdapter extends RecyclerViewAdapter<ExpendBean> {
    public ExpendAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_ewmember);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, final ExpendBean model) {
        final Button checkBox = viewHolderHelper.getView(R.id.but_xiaofei);
        viewHolderHelper.setText(R.id.but_xiaofei,model.getName());
        if (model.isChecked()){
            checkBox.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gray_red));
        }else {
            checkBox.setBackground(mContext.getResources().getDrawable(R.drawable.loginandregister_bg));

        }
    }
}
