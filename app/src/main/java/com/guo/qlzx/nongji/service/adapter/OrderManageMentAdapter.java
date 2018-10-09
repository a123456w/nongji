package com.guo.qlzx.nongji.service.adapter;
import android.support.v7.widget.RecyclerView;
import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.service.bean.OrderManageMentBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;


/**管理系统详情适配器
 * Created by Administrator on 2018/5/24.
 */

public class OrderManageMentAdapter extends RecyclerViewAdapter<OrderManageMentBean> {
    public OrderManageMentAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.ordermanagement_item);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, OrderManageMentBean model) {
        viewHolderHelper.setText(R.id.tv_machinedetails, model.getMachine_name());
        viewHolderHelper.setText(R.id.tv_machinistdetails, "机    手："+model.getOperator_name());
        viewHolderHelper.setText(R.id.tv_number,           "作业捆数："+ model.getBalenum());
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getUrl(),viewHolderHelper.getImageView(R.id.machineimages));
        viewHolderHelper.setItemChildClickListener(R.id.im_delete);
        viewHolderHelper.setItemChildClickListener(R.id.tv_machinedetails);
    }
}
