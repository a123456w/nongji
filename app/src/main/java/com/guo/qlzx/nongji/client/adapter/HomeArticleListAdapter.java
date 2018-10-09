package com.guo.qlzx.nongji.client.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.ArticleListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

import java.util.List;

/**
 * create by xuxx on 2018/9/15
 */
public class HomeArticleListAdapter extends RecyclerViewAdapter<ArticleListBean> {
    public HomeArticleListAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.home_page_list_view);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, ArticleListBean model) {
        viewHolderHelper.getTextView(R.id.tv_title).setText(model.getTitle());
        viewHolderHelper.getTextView(R.id.tv_details).setText(model.getDescribe());
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPic(), viewHolderHelper.getImageView(R.id.iv_img));
        viewHolderHelper.setItemChildClickListener(R.id.ll_liner);
    }
}
