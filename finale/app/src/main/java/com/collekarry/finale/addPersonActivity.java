package com.collekarry.finale;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class addPersonActivity extends AppCompatActivity implements AddMedicationFragment.OnEntryComplete, AddHistoryFragment.OnEntryCompleteListener,AdapterView.OnItemSelectedListener{

    private Button submitButton ;
    private EditText nameView;
    private EditText ageView;
    private Switch genderView;
    private List<Medicine> meds;
    private List<History> histories;
    private Spinner docSpin;

    private String patkey;

    private DatabaseReference mDatabase,docRefBase;
    private StorageReference mStorageRef;
    private com.mikhaellopez.circularimageview.CircularImageView imageView;
    private RecyclerView medRecyclerView;
    private RecyclerView historyRecyclerView;
    private RecyclerView.Adapter mAdapter, mAdapterB;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<String> docterDet;
    private List<String> docKeys;
    private List<String> docUps;

    private String userChosenTask;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    int baka=1;
    String name="",gender="";
    int age =-99;
    int flag;

    int pos[] = new int[100];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        if(FirebaseAuth.getInstance().getUid() == null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        flag=1;

        nameView = findViewById(R.id.nameInput);
        ageView = findViewById(R.id.ageInput);
        genderView = findViewById(R.id.genderSwitch);
        submitButton = findViewById(R.id.submitButton);
        imageView = findViewById(R.id.imageButton);
        meds = new ArrayList<>();
        histories = new ArrayList<>();
        docterDet = new ArrayList<>() ;
        docSpin = (Spinner) findViewById(R.id.docSpin);
        docKeys = new ArrayList<>();
        docUps = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        medRecyclerView = findViewById(R.id.med_recycler_view);
        medRecyclerView.setNestedScrollingEnabled(false);
        medRecyclerView.setHasFixedSize(true);

        historyRecyclerView = findViewById(R.id.history_recycler_view);

      //  docSpin.setOnItemSelectedListener(this);
        submitButton.setClickable(true);


        docRefBase = FirebaseDatabase.getInstance().getReference();


        docRefBase = docRefBase.child("Users").child("Docters");
        docRefBase.addValueEventListener(new ValueEventListener() {
                  @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                            String PKey = ds.child("name").getValue(String.class);
                            docterDet.add(PKey);
                            PKey = ds.child("key").getValue(String.class);
                            docKeys.add(PKey);
                    }


                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(addPersonActivity.this, android.R.layout.simple_spinner_item, docterDet);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    docSpin.setAdapter(areasAdapter);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("db error", databaseError.getMessage());
                }
        });


//        docSpin.setOnTouchListener(Spinner_OnTouch);
  //      docSpin.setOnItemSelectedListener(ItemSelectedListener);

        docSpin.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (flag != 1)
        {
//            String abc = (String) parent.getItemAtPosition(position);
//            //abc = docKeys.get(position) ;
//            abc = Integer.toString(position);
//            Toast.makeText(this, abc, Toast.LENGTH_SHORT).show();
//            Log.i("abcbca", abc);

            if(docUps.size() == 0)
            {
                String abc = (String) docKeys.get(position);
                docUps.add(abc);
                pos[position] = 1;
                Toast.makeText(this, parent.getItemAtPosition(position)+" was added!!!", Toast.LENGTH_SHORT).show();
            }
            else if(pos[position] == 0)
            {
                String abc = (String) docKeys.get(position);
                docUps.add(abc);
                pos[position] = 1;
                Toast.makeText(this, parent.getItemAtPosition(position)+" was added!!!", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "cannot add 2 same docs", Toast.LENGTH_SHORT).show();

        } else
            flag = 12494;

        System.out.println(docUps);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public void submitButtonClicked(View v)
    {
        name = nameView.getText().toString().trim();
        age = ageView.getText().toString().equals("") ? 0 : Integer.valueOf(ageView.getText().toString());

        baka =0;

        if(!name.matches("^[a-zA-Z\\s]{3,20}$")){
            nameView.setError("Enter a valid name");
        }
        else if( age<30 || age>200 ){
            ageView.setError("Enter a valid age");
        }
        else{
            if(genderView.isChecked()){
                gender = "Female";
            }
            else{
                gender = "Male";
            }

            final String uid = FirebaseAuth.getInstance().getUid();

            final String key = mDatabase.child("wards").child(uid).push().getKey();
            String imp = "false";

            final wardClass ward = new wardClass(key,name,age,gender,uid,imp);

            for(Medicine m: meds){
                m.setDueEventID(m.setReminder(getApplicationContext(), m,name));
            }

            ward.setMedicines(meds);
            ward.setHistories(histories);

//            List<Medicine> tempMed = new ArrayList<>();
//            List<String> t = new ArrayList<>();
//            t.add("2:00 am");
//            t.add("11:30 pm");
//
//            for(int i = 0; i < 10; i++){
//                tempMed.add(new Medicine("MedName","BrandName",100, new Date(), t, "Dr. Kannaswmi") );
//            }
//            ward.setMedicines(tempMed);


        submitButton.setClickable(false);


            mDatabase.child("Users").child("Patients").child(key).setValue(ward)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            final DatabaseReference CPLinkRef = mDatabase.child("LinksCaretakersPatients");

                            CPLinkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Map<String, String> s = new HashMap<>();
                                    s.put(uid, ward.getKey());
                                    CPLinkRef.push().setValue(s);
//
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            for(int z=0 ; z<docUps.size() ; z++)
                            {
                                final DatabaseReference DPLinkRef = mDatabase.child("LinksDocterPatients");

                                final int finalZ = z;
                                DPLinkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Map<String, String> s = new HashMap<>();
                                        s.put( ward.getKey() , docUps.get(finalZ));
                                        DPLinkRef.push().setValue(s);
//
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addPersonActivity.this, "Failed successfully", Toast.LENGTH_SHORT).show();
                        }
                    });


//        for(int z=0 ; z<docUps.size() ; z++)
//        {
//            mDatabase.child("LinksDocterPatient").child(key).child(patkey).setValue(docUps.get(z))
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(addPersonActivity.this, "Failed successfully", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }










            ProgressDialog dialog = ProgressDialog.show(addPersonActivity.this, "",
                    "Loading. Please wait...", true);



            StorageReference fileRef = mStorageRef.child("Display Pictures").child(key + ".jpg");

            Drawable img;
            System.out.println("\n\n\n tadada: " + imageView.getTag() + "\n\n\n");
            if(imageView.getTag().equals("R.drawable.dpic.jpg")){
                img = getDrawable(R.drawable.default_dp);
            }
            else{
                img = imageView.getDrawable();
            }

            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) img).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = fileRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(addPersonActivity.this, "Image upload failed", Toast.LENGTH_SHORT);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    startActivity(new Intent(addPersonActivity.this, listOfPeople.class));
                }
            });


//            Map<String, Object> wardValues = ward.toMap();
//
//            Map<String, Object> childUpdates = new HashMap<>();
//            childUpdates.put("/wards/" + key, wardValues);
//
//            mDatabase.updateChildren(childUpdates);

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void imageClicked(View v){

        final CharSequence[] items = { "Camera", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(addPersonActivity.this);
        if(imageView.getDrawable().equals(getDrawable(R.drawable.dpic))){
            builder.setTitle("Select Display picture :");
        }
        else{
            builder.setTitle("Change Display picture :");
        }

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Camera")) {
                    userChosenTask = "Camera";
                    boolean result = checkPermissions(addPersonActivity.this);
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChosenTask = "Choose from Library";
                    boolean result = checkPermissions(addPersonActivity.this);
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }
    public void galleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select photo"),1);
    }

    public boolean checkPermissions(final Context context){
        if(userChosenTask.equals("Choose from Library")) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        }
        else{
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions((Activity) context, new String[] {android.Manifest.permission.CAMERA}, 124);

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Camera permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }

                return false;
            }
            else
            {
                return true;
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChosenTask.equals("Camera"))
                        cameraIntent();
                    else if(userChosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    Toast.makeText(this, "Need permissions", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            imageView.setTag("");
            if (requestCode == 1)               //1 for gallery
                onSelectFromGalleryResult(data);
            else if (requestCode == 0)          //0 for camera
                onCaptureImageResult(data);
        }
    }

    void onSelectFromGalleryResult(Intent data){
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageView.setImageBitmap(bm);
        }
    }

    void onCaptureImageResult(Intent data){

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Objects.requireNonNull(thumbnail).compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(thumbnail);
    }

    public void addMed(View v){

        AddMedicationFragment.newInstance(new wardClass(), "indirect_entry")
                .show(getSupportFragmentManager(), "add_med_indirect");

//
//        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View row = vi.inflate(R.layout.new_medicine_layout, null);

    }

    public void addHistory(View v){

        AddHistoryFragment.newInstance(new wardClass(), "indirect_entry")
                .show(getSupportFragmentManager(), "add_history_indirect");
    }

    @Override
    public void onEntryComplete(Medicine med) {
        meds.add(med);
        System.out.println(meds);
        mAdapter = new IndirectMedicineRecyclerViewAdapter(getApplicationContext(), meds);
        medRecyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(addPersonActivity.this);
        medRecyclerView.setLayoutManager(layoutManager);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEntryComplete() {

    }

    @Override
    public void onBackPressed()
    {
        if(baka != 0 || age == -99 || gender.equals("") || name.equals(""))
        {
            startActivity(new Intent(this,listOfPeople.class));
        }
    }



    @Override
    public void onEntryComplete(History history) {
        histories.add(history);

        mAdapterB = new IndirectHistoryRecyclerViewAdapter(getApplicationContext(), histories);
        historyRecyclerView.setAdapter(mAdapterB);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(addPersonActivity.this);
        historyRecyclerView.setLayoutManager(layoutManager);

        mAdapterB.notifyDataSetChanged();
    }

}
