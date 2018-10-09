package com.guo.qlzx.nongji.service.adapter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.CheckBox;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.service.bean.ReplaceListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import butterknife.BindView;

/**
 * 管理系统替换框适配器类
 * Created by Administrator on 2018/5/25.
 */

public class ReplaceCouponDialogAdapter extends RecyclerViewAdapter<ReplaceListBean> {


    public ReplaceCouponDialogAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_delete_coupon_dialog);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, ReplaceListBean model) {
        Button checkBox = viewHolderHelper.getView(R.id.but_jiqi);
        viewHolderHelper.setText(R.id.but_jiqi, model.getSn() + "机器");
        if (model.isChecked()){
            checkBox.setBackground(mContext.getResources().getDrawable(R.drawable.shape_red_three));
            checkBox.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            checkBox.setBackground(mContext.getResources().getDrawable(R.drawable.shape_gary_three));
            checkBox.setTextColor(mContext.getResources().getColor(R.color.textcolor3));
        }
    }

}
