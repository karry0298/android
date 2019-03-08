package com.collekarry.finale;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class Medicine implements Serializable {

    private String name;
    private String brandName;
    private String dateStarted;
    private String dateStopped;
    private int count;
    private long dueEventID;
    private long dailyEventID;

    private List<String> consumptionTimings;
    private String prescriptionBy;

    public void removeTiming(String timing){
        consumptionTimings.remove(timing);
    }

    public String getNextConsumptionTime(LocalTime t){
        if(consumptionTimings == null || consumptionTimings.size() == 0){
            return null;
        }
        LocalTime x = new LocalTime(23,59);
        LocalTime tomoTime = new LocalTime(23,59);

        for(int i = 0; i < consumptionTimings.size(); i++){

            LocalTime a = LocalTime.parse(consumptionTimings.get(i).replace(".","") , DateTimeFormat.forPattern("hh:mm a").withLocale(Locale.ENGLISH));

            if( a.isBefore(x) && (a.isAfter(t) || a.isEqual(t))  ){
                x = a;
            }

            if(a.isBefore(tomoTime)){
                tomoTime = a;
            }
        }

        if(x.isEqual(new LocalTime(23,59))){
//            System.out.println("\t"+ name+"\t"+x.toString("hh:mm a"));
            return "Tomorrow, " + tomoTime.toString("hh:mm a");
        }
        else{
            return "Today, " + x.toString("hh:mm a");
        }
    }

    public DateTime getNextConsumptionDateTime(LocalTime t){
        if(consumptionTimings == null || consumptionTimings.size() == 0){
            return new DateTime();
        }
        LocalTime x = new LocalTime(23,59);
        LocalTime tomoTime = new LocalTime(23,59);

        for(int i = 0; i < consumptionTimings.size(); i++){
            Log.e("Edwin", consumptionTimings.get(i).replace(".",""));
            LocalTime a = LocalTime.parse(consumptionTimings.get(i).replace(".","") ,
                    DateTimeFormat.forPattern("hh:mm a").withLocale(Locale.ENGLISH));
            Log.e("Edwin_END", " ---");
            if( a.isBefore(x) && (a.isAfter(t) || a.isEqual(t))  ){
                x = a;
            }

            if(a.isBefore(tomoTime)){
                tomoTime = a;
            }
        }

        if(x.isEqual(new LocalTime(23,59))){
//            System.out.println("\t"+ name+"\t"+x.toString("hh:mm a"));
            return tomoTime.toDateTimeToday().plusDays(1);
        }
        else{
            return x.toDateTimeToday();
        }
    }

    public DateTime dueDate() {
        System.out.println();
        DateTime d = null;
        try {
            d = new DateTime(new SimpleDateFormat("dd/MM/yyyy").parse(dateStarted) ).withTime(23,59,59,999);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int pillsPerDay = consumptionTimings.size();
        int days = Integer.valueOf(count/pillsPerDay) - 3;

        try {
            if(d.plusDays(days).isAfter(new DateTime(new SimpleDateFormat("dd/MM/yyyy").parse(dateStopped)))){
                return null;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d.plusDays(days);
    }

    public long setReminder(Context context, Medicine newMed, String wardName){
        if(newMed.dueDate()==null){return 0;}

        DateTime due = newMed.dueDate();
        long startMillis = due.getMillis();
        DateTime dueEnd = due.plusDays(3);
        long endMillis = dueEnd.getMillis();

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();

        eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);
        eventValues.put(CalendarContract.Events.TITLE, "Buy Medicine");
        eventValues.put(CalendarContract.Events.DESCRIPTION, wardName + " : " + newMed.getName() + " ["+ newMed.getBrandName() + "] "  + " almost over");
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, due.getZone().toTimeZone().getDisplayName());
        eventValues.put(CalendarContract.Events.DTSTART, startMillis);
        eventValues.put(CalendarContract.Events.DTEND, endMillis);
        eventValues.put(CalendarContract.Events.STATUS, 0);
        eventValues.put(CalendarContract.Events.ALL_DAY, 1);

        Uri eventUri = context.getContentResolver().insert(Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());


        String reminderUriString = "content://com.android.calendar/reminders";
        ContentValues reminderValues = new ContentValues();
        reminderValues.put("event_id", eventID);
        reminderValues.put("minutes", 12*60);
        reminderValues.put("method", 1);
        Uri reminderUri = context.getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);

        return eventID;
    }

    public Medicine() { }

    public Medicine(String name, String brandName, int count, String dateStarted, String dateStopped, List<String> consumptionTimings, String prescriptionBy) {
        this.name = name;
        this.brandName = brandName;
        this.count = count;
        this.dateStarted = dateStarted;
        this.dateStopped = dateStopped;
        this.consumptionTimings = consumptionTimings;
        this.prescriptionBy = prescriptionBy;
        this.dailyEventID = -1;
    }

    public long getDailyEventID() {
        return dailyEventID;
    }
    public void setDailyEventID(long dailyEventID) {
        this.dailyEventID = dailyEventID;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public String getDateStarted() {
        return dateStarted;
    }
    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getDateStopped() {
        return dateStopped;
    }
    public void setDateStopped(String dateStopped) {
        this.dateStopped = dateStopped;
    }

    public List<String> getConsumptionTimings() {
        return consumptionTimings;
    }
    public void setConsumptionTimings(List<String> consumptionTimings) {
        this.consumptionTimings = consumptionTimings;
    }

    public String getPrescriptionBy() {
        return prescriptionBy;
    }
    public void setPrescriptionBy(String prescriptionBy) {
        this.prescriptionBy = prescriptionBy;
    }

    public long getDueEventID() {
        return dueEventID;
    }
    public void setDueEventID(long dueEventID) {
        this.dueEventID = dueEventID;
    }
}

