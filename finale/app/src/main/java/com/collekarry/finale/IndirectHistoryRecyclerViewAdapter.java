package com.collekarry.finale;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class IndirectHistoryRecyclerViewAdapter extends android.support.v7.widget.RecyclerView.Adapter<IndirectHistoryRecyclerViewAdapter.ViewHolder> {

    private List<History> histories;
    private Context context;

    public IndirectHistoryRecyclerViewAdapter(Context context, List<History> histories) {
        this.histories = histories;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item_layout, parent, false);

        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nameView.setText(histories.get(position).getTitle());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                histories.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(histories==null){
            return 0;
        }
        else{
            return histories.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView nameView;
        public ImageButton imageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            nameView = itemView.findViewById(R.id.medTextViewMain);
            imageButton = itemView.findViewById(R.id.action_remove_med);

        }
    }
}
