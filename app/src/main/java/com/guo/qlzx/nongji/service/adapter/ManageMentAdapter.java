package com.guo.qlzx.nongji.service.adapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.service.bean.ManageMentBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;


/**
 * 管理系统适配器
 * Created by Administrator on 2018/5/24.
 */

public class ManageMentAdapter extends RecyclerViewAdapter<ManageMentBean>{
    public ManageMentAdapter(RecyclerView recyclerView) {
        super(recyclerView,R.layout.ment_item);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, ManageMentBean model) {
        viewHolderHelper.setText(R.id.tv_owner,"机主:"+model.getUser_name());
        Log.i("TAG","走"+position+"次"+model.getOrder_mac().size());
        if (model.getOrder_mac().size()==0){
        }else if (model.getOrder_mac().size()==1){
            viewHolderHelper.setText(R.id.tv_tractor,"机手:"+model.getOrder_mac().get(0).getOperator_name());
            viewHolderHelper.setText(R.id.tv_machine,model.getOrder_mac().get(0).getMachine_name());
        }else if (model.getOrder_mac().size()==2){
            viewHolderHelper.setText(R.id.tv_tractor,"机手:"+model.getOrder_mac().get(0).getOperator_name());
            viewHolderHelper.setText(R.id.tv_machine,model.getOrder_mac().get(0).getMachine_name());
            viewHolderHelper.setText(R.id.tv_tractor1,"机手:"+model.getOrder_mac().get(1).getOperator_name());
            viewHolderHelper.setText(R.id.tv_machine1,model.getOrder_mac().get(1).getMachine_name());
        }else if (model.getOrder_mac().size()>=3){
            viewHolderHelper.setText(R.id.tv_tractor,"机手:"+model.getOrder_mac().get(0).getOperator_name());
            viewHolderHelper.setText(R.id.tv_machine,model.getOrder_mac().get(0).getMachine_name());
            viewHolderHelper.setText(R.id.tv_tractor1,"机手:"+model.getOrder_mac().get(1).getOperator_name());
            viewHolderHelper.setText(R.id.tv_machine1,model.getOrder_mac().get(1).getMachine_name());
            viewHolderHelper.setText(R.id.tv_tractor2,"机手:"+model.getOrder_mac().get(2).getOperator_name());
            viewHolderHelper.setText(R.id.tv_machine2,model.getOrder_mac().get(2).getMachine_name());
        }
    }


}
