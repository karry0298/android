package com.collekarry.finale;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class wardClass implements Serializable
{
    private String key;
    private String name;
    private int age;
    private String imp;
    private String gender;

    private List<Medicine> medicines;
    private List<History> histories;



    public wardClass() { }

    public wardClass(String key, String name, int age, String gender, String uid , String imp) {
        this.key = key;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.imp = imp;
        this.medicines = new ArrayList<>();
        this.histories = new ArrayList<>();
    }

    public void addMedicine(Medicine e){
        if(medicines == null){
            medicines = new ArrayList<>();
        }
        this.medicines.add(e);
        System.out.println(e.getName());
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }
    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    public void addHistory(History history)
    {
        if(histories == null)
        {
            histories = new ArrayList<>();
        }
        this.histories.add(history);
    }

    public String getImp() {
        return imp;
    }

    public void setImp() {
        this.imp = imp;
    }

}
