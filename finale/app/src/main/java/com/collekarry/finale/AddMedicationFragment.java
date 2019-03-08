package com.collekarry.finale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddMedicationFragment.OnEntryComplete} interface
 * to handle interaction events.
 * Use the {@link AddMedicationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMedicationFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ward";
    private static final String ARG_PARAM2 = "intention";

    private OnEntryComplete entryCompleteListener;

    private EditText medName;
    private EditText medManufacturer;
    private TextView dateTextView;
    private TextView dateTextView2;
    private LinearLayout datePickView;
    private LinearLayout datePickView2;
    private ImageButton addTimingButton;
    private LinearLayout consumptionTimingLayout;
    private TextView timeTextViewMain;
    private EditText medPrescriptionBy;
    private EditText medCount;
    private ImageButton closeButton;


    private DatabaseReference mDatabaseReference;

    private Button medSubmitButton;



    // TODO: Rename and change types of parameters
    private wardClass ward;
    private String intention;


    private OnEntryComplete medListner;

    public AddMedicationFragment() {

        // Required empty public constructor
    }


    public void dateSelectClicked(View v){

        DatePickerDialog.OnDateSetListener mDatesetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String d = String.valueOf(dayOfMonth)+"/"+ String.valueOf(month+1)+"/"+ String.valueOf(year);
                dateTextView.setText(d);
            }
        };

        Calendar calender = Calendar.getInstance();
        Dialog mDialog = new DatePickerDialog(getContext(),
                mDatesetListener, calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH), calender
                .get(Calendar.DAY_OF_MONTH));

        mDialog.show();


    }

    public void requestPermissions(){

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
//            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CALENDAR)) {
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
//                alertBuilder.setCancelable(true);
//                alertBuilder.setWriter("Permission necessary");
//                alertBuilder.setMessage("Calendar permission is necessary.");
//                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                    public void onClick(DialogInterface dialog, int which) {
//                        requestPermissions(new String[]{android.Manifest.permission.WRITE_CALENDAR, android.Manifest.permission.READ_CALENDAR},
//                                66);
//                    }
//                });
//                AlertDialog alert = alertBuilder.create();
//                alert.show();
//            } else {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_CALENDAR},
                    66);
            requestPermissions(new String[]{android.Manifest.permission.WRITE_CALENDAR},
                    66);
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 66){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
            else{
                requestPermissions();
            }
        }
//        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        medName = getView().findViewById(R.id.medName);
        medManufacturer = getView().findViewById(R.id.medManufacturer);
        dateTextView = getView().findViewById(R.id.dateText);
        dateTextView2 = getView().findViewById(R.id.dateText2);
        datePickView = getView().findViewById(R.id.date_pick_view);
        datePickView2 = getView().findViewById(R.id.date_pick_view2);
        addTimingButton = getView().findViewById(R.id.action_add_timing);
        consumptionTimingLayout = getView().findViewById(R.id.consumption_timings);
        timeTextViewMain = getView().findViewById(R.id.timeTextViewMain);
        medPrescriptionBy = getView().findViewById(R.id.medprescriptionBy);
        medCount = getView().findViewById(R.id.medCount);
        medSubmitButton = getView().findViewById(R.id.medSubmitButton);
        closeButton = getView().findViewById(R.id.med_close_button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        datePickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelectClicked(v);
            }
        });

        datePickView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener mDatesetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String d = String.valueOf(dayOfMonth)+"/"+ String.valueOf(month+1)+"/"+ String.valueOf(year);
                        dateTextView2.setText(d);
                    }
                };

                Calendar calender = Calendar.getInstance();
                Dialog mDialog = new DatePickerDialog(getContext(),
                        mDatesetListener, calender.get(Calendar.YEAR),
                        calender.get(Calendar.MONTH), calender
                        .get(Calendar.DAY_OF_MONTH));

                mDialog.show();
            }
        });

        timeTextViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener mTimesetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String time = String.format("%02d", hourOfDay) + ":" + String.format("%02d",minute);

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        try {
                            Date d = sdf.parse(time);
                            timeTextViewMain.setText(new SimpleDateFormat("hh:mm aa").format(d));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                };

                Calendar calender = Calendar.getInstance();
                Dialog mDialog = new TimePickerDialog(getContext(),
                        mTimesetListener, calender.get(Calendar.HOUR_OF_DAY),
                        calender.get(Calendar.MINUTE),
                        false);

                mDialog.show();
            }
        });

        addTimingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addTimingButton.getTag().equals("add")) {

                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View row = vi.inflate(R.layout.layout_time_row, null);

                    final ImageButton newAddButton = row.findViewById(R.id.add_timing);
                    final TextView timeTextView = row.findViewById(R.id.timeTextView);


                    timeTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            TimePickerDialog.OnTimeSetListener mTimesetListener = new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    String time = String.format("%02d", hourOfDay) + ":" + String.format("%02d",minute);

                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                    try {
                                        Date d = sdf.parse(time);
                                        timeTextView.setText(new SimpleDateFormat("hh:mm aa").format(d));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }


                                }

                            };

                            Calendar calender = Calendar.getInstance();
                            Dialog mDialog = new TimePickerDialog(getContext(),
                                    mTimesetListener, calender.get(Calendar.HOUR_OF_DAY),
                                    calender.get(Calendar.MINUTE),
                                    false);

                            mDialog.show();
                        }
                    });



                    newAddButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            consumptionTimingLayout.removeView((View) v.getParent());
                        }
                    });


//                    LinearLayout vg = getView().findViewById(R.id.action_add_timing);
                    consumptionTimingLayout.addView(row, consumptionTimingLayout.getChildCount() , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//                    addTimingButton.setImageResource(R.drawable.ic_remove);
//                    addTimingButton.setTag("remove");
                }
                else{
                    consumptionTimingLayout.removeView((View) v.getParent());
                }
            }
        });


        medSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = medName.getText().toString();
                String manufacturer = medManufacturer.getText().toString();
                String dateText = dateTextView.getText().toString().trim();
                String dateText2 = dateTextView2.getText().toString().trim();
                String prescriptionBy = medPrescriptionBy.getText().toString();
                int count;
                if(medCount.getText().toString().equals("")){
                    count = 0;
                }
                else{
                    count = Integer.parseInt(medCount.getText().toString());
                }
                int noOfTimings = consumptionTimingLayout.getChildCount();

                if(name.length() <= 3){
                    medName.setError("Too short");
                }
                else if(manufacturer.length() <= 3){
                    medManufacturer.setError("Too short");
                }
                else if(prescriptionBy.length() <= 3){
                    medPrescriptionBy.setError("Too Short");
                }
                else if(count < 3*noOfTimings){
                    medCount.setError("More pills required");
                }
                else if(dateText.equals("dd/MM/yyyy")){

                    dateTextView.setError("Please select a date.");
                }
                else if(dateText2.equals("dd/MM/yyyy")){

                    dateTextView2.setError("Please select a date.");
                }
                else{

                    List<String> timings = new ArrayList<>();

                    for(int i = 0; i < noOfTimings; i++){
                        View t = consumptionTimingLayout.getChildAt(i);
                        TextView tv = t.findViewWithTag("time");
                        timings.add(tv.getText().toString());

                    }
                    System.out.println(timings);
                    Medicine newMed = new Medicine(name,manufacturer,count,dateText
                            ,dateText2 ,timings,prescriptionBy);

                    if(intention.equals("direct_entry")){

                        while (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_CALENDAR)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions();
                        }

                        long eventID = newMed.setReminder(getActivity(), newMed, ward.getName());
                        System.out.println(ward);
                        newMed.setDueEventID(eventID);

                        ward.addMedicine(newMed);

                        mDatabaseReference.child("Users").child("Patients").child(ward.getKey()).setValue(ward)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Med added", Toast.LENGTH_SHORT).show();
                                        entryCompleteListener.onEntryComplete();
                                        dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Med not added", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else{
                        entryCompleteListener.onEntryComplete(newMed);
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

     * @return A new instance of fragment AddMedicationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMedicationFragment newInstance(wardClass param1, String param2) {
        AddMedicationFragment fragment = new AddMedicationFragment();
        Bundle args = new Bundle();
        fragment.ward = param1;
        fragment.intention = param2;
        args.putSerializable(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
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
        return inflater.inflate(R.layout.fragment_add_medication, container, false);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        medListner = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        entryCompleteListener = (OnEntryComplete) context;

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
    public interface OnEntryComplete {
        // TODO: Update argument type and name
        void onEntryComplete(Medicine med);
        void onEntryComplete();
    }
}
