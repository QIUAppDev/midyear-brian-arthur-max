package com.example.brian.subwaytime;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by brian on 1/15/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private List<derpwork> network_list;
    private View.OnClickListener onClickListener;

    public RecyclerViewAdapter(List<derpwork> derp_list, View.OnClickListener listener){
        network_list=derp_list;
        onClickListener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        derpwork network= network_list.get(position);
        holder.itemTextView.setText(network.getName());
        holder.nameTextView.setText(network.getSsid());
        holder.dateTextView.setText(network.getMac());
        holder.itemView.setTag(network);
        holder.itemView.setOnClickListener(onClickListener);
    }
    @Override
    public int getItemCount() {
        return network_list.size();
    }

    public void addItems(List<derpwork> new_list) {
        network_list = new_list;
        notifyDataSetChanged();
    }


    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTextView;
        private TextView nameTextView;
        private TextView dateTextView;

        RecyclerViewHolder(View view) {
            super(view);
            itemTextView = (TextView) view.findViewById(R.id.itemTextView);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        }
    }
}
