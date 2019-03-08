package com.collekarry.finale;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddHistoryFragment.OnEntryCompleteListener} interface
 * to handle interaction events.
 * Use the {@link AddHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHistoryFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ward";
    private static final String ARG_PARAM2 = "intention";

    // TODO: Rename and change types of parameters
    private wardClass ward;
    private String intention;

    private ImageButton closeButton;
    private EditText titleView;
    private EditText descriptionView;
    private Button submitButton;

    private DatabaseReference mDatabaseReference;

    private OnEntryCompleteListener onEntryCompleteListener;

    public AddHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        closeButton = getView().findViewById(R.id.history_close_button);
        titleView = getView().findViewById(R.id.historyName);
        descriptionView = getView().findViewById(R.id.historyDescription);
        submitButton = getView().findViewById(R.id.hisSubmitButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleView.getText().toString();
                String description = descriptionView.getText().toString();

                if(title.equals("") || title.length() <= 3){
                    titleView.setError("Title too short");
                }
                else if(description.equals("") || description.length() <= 5){
                    descriptionView.setError("Description too short");
                }
                else{
                    History history = new History(title,description, new SimpleDateFormat("dd/MM/yyyy").format(new Date()));


                    if(intention.equals("direct_entry")){
                        ward.addHistory(history);

                        mDatabaseReference.child("Users").child("Patients").child(ward.getKey()).setValue(ward)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "History added", Toast.LENGTH_SHORT).show();
                                        onEntryCompleteListener.onEntryComplete();
                                        dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "history not added", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else{
                        onEntryCompleteListener.onEntryComplete(history);
                        dismiss();
                    }

                }
            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddHistoryFragment newInstance(wardClass param1, String param2) {
        AddHistoryFragment fragment = new AddHistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ward = (wardClass) getArguments().getSerializable(ARG_PARAM1);
            intention = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return inflater.inflate(R.layout.fragment_add_history, container, false);
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEntryCompleteListener) {
            onEntryCompleteListener = (OnEntryCompleteListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onEntryCompleteListener = null;
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
    public interface OnEntryCompleteListener {
        // TODO: Update argument type and name
        void onEntryComplete();
        void onEntryComplete(History history);
    }
}
