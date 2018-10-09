package com.guo.qlzx.nongji.client.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.ArticleListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * create by xuxx on 2018/9/15
 */
public class ManualListAdapter extends RecyclerViewAdapter<ArticleListBean> {
    public ManualListAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.manual_page_list_view);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, ArticleListBean model) {
      viewHolderHelper.getTextView(R.id.tv_text).setText(model.getTitle());
        viewHolderHelper.setItemChildClickListener(R.id.ll_liner);
    }
}
