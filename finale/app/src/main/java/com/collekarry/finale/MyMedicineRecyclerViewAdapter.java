package com.collekarry.finale;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collekarry.finale.MedicationFragment.OnListFragmentInteractionListener;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMedicineRecyclerViewAdapter extends RecyclerView.Adapter<MyMedicineRecyclerViewAdapter.ViewHolder> {

    private final List<Medicine> mValues;
    private final OnMedClickedListener mListener;



    public MyMedicineRecyclerViewAdapter(Context context, List<Medicine> items) {
        mListener = (OnMedClickedListener) context;

        List<Medicine> m = new ArrayList<>();
        if(items == null || items.size() == 0){
            mValues = m;
            return;
        }
        for(Medicine x: items){
            m.add(x);

        }

        Collections.sort(m, new Comparator<Medicine>() {
            @Override
            public int compare(Medicine o1, Medicine o2) {
                    return  o1.getNextConsumptionDateTime(LocalTime.now()).compareTo(o2.getNextConsumptionDateTime(LocalTime.now()));
                }
        });

        mValues = m;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());
        holder.mContentView.setText(mValues.get(position).getNextConsumptionTime(LocalTime.now()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.medClicked(holder.mItem);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if(mValues == null){
            return 0;
        }
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Medicine mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface OnMedClickedListener{
        void medClicked(Medicine m);
    }
}
