package com.collekarry.finale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MedicineDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MedicineDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicineDetailsFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "medicine";


    // TODO: Rename and change types of parameters
    private Medicine mParam1;


    private OnFragmentInteractionListener mListener;

    public MedicineDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment MedicineDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicineDetailsFragment newInstance(Medicine param1) {
        MedicineDetailsFragment fragment = new MedicineDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.mParam1 = param1;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Medicine) getArguments().getSerializable(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_medicine_details, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.timing_recycler_view);
        Context context = view.getContext();
        TextView titleView = view.findViewById(R.id.med_title);
        TextView manufacturerView = view.findViewById(R.id.med_manufacturer);
        ImageButton closeButton = view.findViewById(R.id.meddetails_close);
        TextView startDateView = view.findViewById(R.id.med_start_date);
        TextView endDateView = view.findViewById(R.id.med_end_date);
        TextView prescriptionView = view.findViewById(R.id.med_prescription);
        Button removeMedButton = view.findViewById(R.id.medRemoveButton);

        titleView.setText(mParam1.getName());
        manufacturerView.setText(mParam1.getBrandName());
//        startDateView.setText(new SimpleDateFormat("dd/MM/yyyy").format(mParam1.getDateStarted()));
//        endDateView.setText(new SimpleDateFormat("dd/MM/yyyy").format(mParam1.getDateStopped()));

        startDateView.setText(mParam1.getDateStarted());
        endDateView.setText(mParam1.getDateStopped());

        prescriptionView.setText(mParam1.getPrescriptionBy());

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MedicineTimingsRecyclerViewAdapter(context, mParam1.getConsumptionTimings()));


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        removeMedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMedRemoveAction(mParam1);
                dismiss();
            }
        });

        return view;


    }


    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnFragmentInteractionListener) context;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMedRemoveAction(Medicine m);
    }
}

