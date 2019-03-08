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

public class IndirectMedicineRecyclerViewAdapter extends RecyclerView.Adapter<IndirectMedicineRecyclerViewAdapter.ViewHolder> {

    private List<Medicine> meds;
    private Context context;

    public IndirectMedicineRecyclerViewAdapter(Context context, List<Medicine> meds) {
        this.context = context;
        this.meds = meds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_item_layout, parent, false);
//        TextView tv = row.findViewById(R.id.medTextViewMain);
//        ImageButton ib = row.findViewById(R.id.action_remove_med);
        ViewHolder vh = new ViewHolder(row);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nameView.setText(meds.get(position).getName());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meds.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(meds == null){
            return 0;
        }
        else{
            return meds.size();
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
