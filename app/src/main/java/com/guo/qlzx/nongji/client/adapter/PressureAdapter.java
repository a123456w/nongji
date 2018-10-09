package com.guo.qlzx.nongji.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.nongji.R;
import com.guo.qlzx.nongji.client.bean.DetailsInfoBean;
import com.guo.qlzx.nongji.client.bean.PressureTimeBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * create by xuxx on 2018/6/20
 * 日志详情 - 压力
 */
public class PressureAdapter extends RecyclerView.Adapter<PressureAdapter.ViewHolder> {
    private Context context;
    private List<PressureTimeBean> list;

    public PressureAdapter(Context context, List<PressureTimeBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_horizontal_layout, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PressureTimeBean pressureTimeBean = list.get(position);
       // addTextView(pressureTimeBean,holder.linearLayout);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        holder.linearLayout.removeAllViews();
        for (int i = 0; i < list.get(position).getTimeList().size(); i++) {
            TextView textView = new TextView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 20, 0);
            textView.setLayoutParams(layoutParams);
            textView.setText(simpleDateFormat.format(list.get(position).getTimeList().get(i)) + "");
            holder.linearLayout.addView(textView);
        }
       // addPressure(holder.tvFilePosition,pressureTimeBean);
        if (list.get(position).getBean() == null) {
            holder.tvFilePosition.setVisibility(View.INVISIBLE);
            return;
        }
        holder.tvFilePosition.setVisibility(View.VISIBLE);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(model.getTimeList().size()*20, ViewGroup.LayoutParams.WRAP_CONTENT);

        holder.tvFilePosition.setText(list.get(position).getBean().getBale_grade() + "档");
        //   textView.setLayoutParams(layoutParams);
        if (list.get(position).getBean().getBale_grade().equals("1")) {
            holder.tvFilePosition.setBackgroundResource(R.drawable.shap_green_three);

        } else if (list.get(position).getBean().getBale_grade().equals("2")) {
            holder.tvFilePosition.setBackgroundResource(R.drawable.shap_yellow_three);
        } else {
            holder.tvFilePosition.setBackgroundResource(R.drawable.shap_red_three);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView tvFilePosition;

        public ViewHolder(View view) {
            super(view);
            linearLayout = (LinearLayout) view.findViewById(R.id.rl_relative);
            tvFilePosition = (TextView) view.findViewById(R.id.tv_file_position);
        }
    }


    private void addPressure(TextView textView, PressureTimeBean model) {

    }

    private void addTextView(PressureTimeBean model, LinearLayout linearLayout) {

    }
}
