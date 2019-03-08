package com.collekarry.finale;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;


public class listOfPeople extends AppCompatActivity
{
    DatabaseReference nameList, keyReference ,timeRef;
    List<wardClass> wardList;
    RecyclerView list;
    List<wardClass> uploadList;
    SwipeRefreshLayout srl;
    MyAdapter adapter;

    List<String> timeStor;

    List<StorageReference> Fimages;
    List<String> dsList;
    NotificationCompat.Builder notification;
    private static final int notId = 12321;

    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menubar, menu);
        return true;
    }


    public void addWard(View v){
        startActivity(new Intent(listOfPeople.this, addPersonActivity.class));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout)
        {
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(listOfPeople.this, MainActivity.class));
        }

        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_people);
//        String base = "";
//        Log.i("name:","");
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setElevation(0);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();


        if(FirebaseAuth.getInstance().getUid() == null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        wardList = new ArrayList<>();
        final List<StorageReference> dpList = new ArrayList<>();

        keyReference = mDatabaseReference.child("LinksCaretakersPatients");
        keyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                wardList.clear();
                dpList.clear();
                String temp_uid = FirebaseAuth.getInstance().getUid();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    if(ds.hasChild(temp_uid)){
                        String PKey = ds.child(temp_uid).getValue(String.class);
                        DatabaseReference patientRef = mDatabaseReference.child("Users").child("Patients").child(PKey);
                        final StorageReference patientImageRef = mStorageReference.child("Display Pictures").child(PKey + ".jpg");
                        patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                wardClass w = dataSnapshot.getValue(wardClass.class);
                                if(!wardList.contains(w)){
                                    wardList.add(w);
                                    dpList.add(patientImageRef);
                                    adapter.notifyDataSetChanged();
                                }
                                updateNotifications();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("db error", databaseError.getMessage());
            }
        });

         timeStor = new ArrayList<>();
        timeStor.clear();





        System.out.println("finalskfmkladf "+timeStor+" timeakzflgd");

//        for(int wz = 0 ; wz < timeStor.size() ; wz++)
//            Log.i("free",timeStor.get(wz));

        nameList = FirebaseDatabase.getInstance().getReference("wards").child(FirebaseAuth.getInstance().getUid());
        nameList.keepSynced(true);


        uploadList = new ArrayList<>();

        Fimages = new ArrayList<>();
        dsList = new ArrayList<>();



        list = (RecyclerView) findViewById(R.id.peopleListView);

        srl = findViewById(R.id.swipe_container);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                startActivity(new Intent(getApplicationContext() , listOfPeople.class));
            }
        });

        
//       displayNotification();
//        aList = new ArrayList<>();

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationManager mNotificationManager =
//                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
//            mChannel.setDescription(CHANNEL_DESCRIPTION);
//            mChannel.enableLights(true);
//            mChannel.setLightColor(Color.RED);
//            mChannel.enableVibration(true);
//            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            mNotificationManager.createNotificationChannel(mChannel);
//        }
//
//        /*
//         * Displaying a notification locally
//         */
//        MyNotificationManager.getInstance(this).displayNotification("Greetings", "Hello how are you?");


//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
//        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.SECOND, 5);
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

//
//        AlarmManager[] alarmManager=new AlarmManager[24];
//        ArrayList intentArray = new ArrayList<PendingIntent>();
//        for(int f=0;f<arr2.length;f++){
//            Intent intent = new Intent(this, AlarmReceiver.class);
//            PendingIntent pi = PendingIntent.getBroadcast(this, f, intent, 0);
//
//            alarmManager[f] = (AlarmManager) getSystemService(ALARM_SERVICE);
//            alarmManager[f].set(AlarmManager.RTC_WAKEUP,arr2[f] ,pi);
//
//
//
//            intentArray.add(pi);
//        }



//        nameList.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot)
//            {
//
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    HashMap value = (HashMap) postSnapshot.getValue();
////                    final String uid = (String) value.get("uid");
//                    wardClass ward = postSnapshot.getValue(wardClass.class);
////                    wardClass ward = new wardClass(
////                            value.get("key").toString(),
////                            value.get("name").toString(),
////                            Integer.valueOf(Long.toString((Long) value.get("age"))),
////                            value.get("gender").toString(),
////                            value.get("uid").toString()
////                    );
//
//
//
//                    if (ward.getUid() != null && FirebaseAuth.getInstance().getUid() != null) {
//                        if (FirebaseAuth.getInstance().getUid().equals(ward.getUid())) {
//
//                            if (!dsList.contains(value.get("key").toString() )) {
//
//                                dsList.add(value.get("key").toString());
////                        System.out.println(dsList);
//
//
//                                uploadList.add(ward);
////                                Fnames.add((String) value.get("name"));
////                                Fages.add(Integer.valueOf(Long.toString((Long) value.get("age"))));
//
//
//                                final String key = (String) postSnapshot.getKey();
////                                System.out.println("here : " + uid + "/displayPictures/" + key + ".jpg");
//                                StorageReference imageRef = mStorageReference
//                                        .child(ward.getUid() + "/displayPictures/" + ward.getKey() + ".jpg");
//                                Fimages.add(imageRef);
//
////                                System.out.println("key :" + key);
////                                System.out.println("uid :" + uid);
////
////                            mStorageReference.child(uid + "/displayPictures/" + key +".jpg").getDownloadUrl()
////                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
////                                        @Override
////                                        public void onSuccess(Uri uri) {
////                                            System.out.println("here : " + uid + "/displayPictures/" + key +".jpg");
////                                            StorageReference imageRef = mStorageReference.child(uid + "/displayPictures/" + key +".jpg");
////
////                                            Fimages.add(imageRef);
////                                        }
////                                    })
////                                    .addOnFailureListener(new OnFailureListener() {
////                                        @Override
////                                        public void onFailure(@NonNull Exception e) {
////                                            Fimages.add(mStorageReference.child("dpic.jpg"));
////                                        }
////                                    });
//                            }
//
//
////                        Toast.makeText(listOfPeople.this, value.get("name") + " added", Toast.LENGTH_SHORT).show();
//
//
//                            MyAdapter adapter = new MyAdapter(listOfPeople.this,
//                                    wardList,
//                                    dpList);
//
//
//                            list.setAdapter(adapter);
//
//                            RecyclerView.LayoutManager lm = new LinearLayoutManager(listOfPeople.this);
//                            list.setLayoutManager(lm);
//                        }
//                    }
//
//
//                    //addition :
////                    System.out.println(upload.getName());
////                    HashMap<String, String> hm = new HashMap<>();
////                    hm.put("name", upload.getName());
////                    hm.put("age", ""+upload.getAge());
////                    hm.put("imageId", ""+upload.getImageId());
////                    aList.add(hm);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });






//        HashMap<String,String> h1 = new HashMap<>();
//        h1.put("name","Nicola Tesla");
//        h1.put("age", "98");
//        h1.put("imageId", Integer.toString(R.mipmap.ic_launcher_round));
//        aList.add(h1);
//        String[] from = { "name", "age", "imageId"};
//        int[] to = { R.id.name, R.id.age, R.id.displayPicture};
//
//        SimpleAdapter sAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.people_listview_layout, from, to);



//        MyAdapter adapter = new MyAdapter(this, names,ages, images);

//        list = (RecyclerView) findViewById(R.id.peopleListView);

        adapter = new MyAdapter(this,
                wardList,
                dpList);

        list.setAdapter(adapter);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(listOfPeople.this);
        list.setLayoutManager(lm);
        adapter.notifyDataSetChanged();

        FloatingActionButton fab = findViewById(R.id.ambulance_call_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 102));
                startActivity(i);
            }
        });



    }

    public void updateNotifications(){
        System.out.println(wardList.size());
        if(wardList == null){return;}
        for (ListIterator<wardClass> it = wardList.listIterator(); it.hasNext(); ) {
            wardClass x = it.next();

            long eventID = -1;
            if(x == null){return;}
            if(x.getMedicines() == null){return;}

            for (ListIterator<Medicine> it1 = x.getMedicines().listIterator(); it1.hasNext(); ) {
                Medicine m = it1.next();
                if(m == null){return;}


                try {
                    Date ds = new SimpleDateFormat("dd/MM/yyyy").parse(m.getDateStopped());
                    if(ds.before(new Date())){
                        History h = new History(m.getName() + " stopped",
                                "Date started : "+ m.getDateStarted()
                                        + "\n Date Stopped : " + m.getDateStopped()
                                        + "\n Manufacturer : " + m.getBrandName()
                                        + "\n Consumption timings : " + Arrays.toString(m.getConsumptionTimings().toArray())
                                        + "\n Prescribed by : " + m.getPrescriptionBy()
                                ,new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

                        List<Medicine> tm = x.getMedicines()==null ? new ArrayList<Medicine>() : x.getMedicines();
                        tm.remove(m);
                        x.setMedicines(tm);

                        List<History> th = x.getHistories()==null ? new ArrayList<History>() : x.getHistories();
                        th.add(h);
                        x.setHistories(th);

                        mDatabaseReference.child("Users").child("Patients").child(x.getKey()).setValue(x).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Med to History successful", Toast.LENGTH_LONG).show();
                            }
                        });

                        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, m.getDueEventID());
                        int rows = getContentResolver().delete(deleteUri, null, null);

                        if(rows>0){
                            Toast.makeText(getApplicationContext(), "deleted rows : "+ rows,  Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } //Convert medicine to history

                if(m.getDailyEventID()==-1 || m.getDailyEventID() == 0){
                    DateTime d = DateTimeFormat.forPattern("dd/MM/yyyy").parseDateTime(m.getDateStarted());
                    System.out.println(d.toString());
                    DateTime dStart = d.withTime(0,0,0,0);
                    long startMillis = dStart.getMillis();
                    DateTime dEnd = DateTimeFormat.forPattern("dd/MM/yyyy").parseDateTime(m.getDateStopped())
                            .withTime(0,0,0,0);
                    long endMillis = dEnd.getMillis();

                    String eventUriString = "content://com.android.calendar/events";
                    ContentValues eventValues = new ContentValues();

                    eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);
                    eventValues.put(CalendarContract.Events.TITLE, x.getName() + "'s Medicine time");
                    eventValues.put(CalendarContract.Events.DESCRIPTION, x.getName() + " : " + m.getName() + " dosage time now");
                    eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, dStart.getZone().toTimeZone().getDisplayName());
                    eventValues.put(CalendarContract.Events.DTSTART, startMillis);
                    eventValues.put(CalendarContract.Events.DTEND, endMillis);
                    eventValues.put(CalendarContract.Events.ALL_DAY, 0);

                    System.out.println(eventID);
                    Uri eventUri = getApplicationContext().getContentResolver().insert(Uri.parse(eventUriString), eventValues);
                    eventID = Long.parseLong(eventUri.getLastPathSegment());

                    String reminderUriString = "content://com.android.calendar/reminders";
                    for(String t: m.getConsumptionTimings()){
                        DateTime dt = DateTimeFormat.forPattern("hh:mm a").withLocale(Locale.ENGLISH).parseDateTime(t.replace(".", "").toUpperCase());
                        long millis = dt.getMillis();
                        long startMilli = dt.withTime(0, 0, 0, 0).getMillis();
                        int min = (int) ((millis - startMilli)/60000);

//                        min = 24*60 - min;
                        ContentValues reminderValues = new ContentValues();
                        reminderValues.put("event_id", eventID);
                        reminderValues.put(CalendarContract.Reminders.MINUTES, min);
                        reminderValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                        Uri reminderUri = getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
                    }
                    List<Medicine> meds = x.getMedicines();
                    Medicine tm = m;

                    meds.remove(tm);
                    tm.setDailyEventID(eventID);
                    meds.add(tm);
                    x.setMedicines(meds);
                    mDatabaseReference.child("Users").child("Patients").child(x.getKey()).setValue(x).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

            }

        }
    }

    public void displayNotification()
    {
        String cId = "abcabc";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager notificationManager = (NotificationManager)
                                                    getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

            CharSequence name = "Chnnel Name";
            String description = "Channel Descp";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(cId, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.WHITE);
            channel.enableVibration(false);

            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, cId)
                .setSmallIcon(R.drawable.appointments_ic)
                .setContentTitle("title")
                .setContentText("kdfhjkgasfbsdjfbdgbjhbsg")
                .setLights(Color.WHITE,500,500)
                .setColor(Color.RED)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(12312, mBuilder.build());


    }

    boolean doubleBackToExitPressedOnce;
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        private final Context context;
        private final List<wardClass> listElemets;
        private final List<StorageReference> imageReferences;

        public MyAdapter(@NonNull Context context, List<wardClass> listElements, List<StorageReference> imageReferences) {
            this.listElemets = listElements;
            this.imageReferences = imageReferences;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_listview_layout, parent, false);
            ViewHolder vh = new ViewHolder(row);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            Glide.with(listOfPeople.this)
                    .using(new FirebaseImageLoader())
                    .load(imageReferences.get(position))
                    .centerCrop()
                    .into(holder.iv1);

//            final wardClass[] currentWard = {new wardClass()};


            holder.name.setText(listElemets.get(position).getName());
            holder.age.setText(String.valueOf(listElemets.get(position).getAge()));
            holder.gender.setText(listElemets.get(position).getGender());

            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), WardDetailsActivity.class);
                    intent.putExtra("key", listElemets.get(position).getKey());
                    startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            if (listElemets == null){
                return 0;
            }
            else{
                return listElemets.size();
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView iv1 ;
            View row;
            TextView name ;
            TextView age ;
            TextView gender;

            public ViewHolder(View rowView) {
                super(rowView);
                row = rowView;
                iv1 = (ImageView) rowView.findViewById(R.id.displayPicture);
                name = (TextView) rowView.findViewById(R.id.name);
                age = (TextView) rowView.findViewById(R.id.age);
                gender = (TextView) rowView.findViewById(R.id.gender);
            }
        }
    }
}
