package com.collekarry.finale;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MedicineTimingsRecyclerViewAdapter extends RecyclerView.Adapter<MedicineTimingsRecyclerViewAdapter.ViewHolder>{


    private final List<String> timings;
    private Context context;

    public MedicineTimingsRecyclerViewAdapter(Context context, List<String> timings) {
        this.timings = timings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.nameView.setText(timings.get(position));

    }

    @Override
    public int getItemCount() {
        if(timings==null){
            return 0;
        }
        return timings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.item_number);

        }
    }

}
