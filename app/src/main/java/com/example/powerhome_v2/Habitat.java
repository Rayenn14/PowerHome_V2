package com.example.powerhome_v2;

import java.util.List;

public class Habitat {
    int id;
    String residentName;
    int floor;
    double area;
    List<Appliance> appliances;

    public Habitat(int id, String residentName, int floor, double area, List<Appliance> appliances) {
        this.id = id;
        this.residentName = residentName;
        this.floor = floor;
        this.area = area;
        this.appliances = appliances;
    }

    public double getArea() {
        return area;
    }

    public int getFloor() {
        return floor;
    }

    public int getId() {
        return id;
    }

    public List<Appliance> getAppliances() {
        return appliances;
    }

    public String getResidentName() {
        return residentName;
    }

    public void AddAppliance(Appliance a){
        appliances.add(a);
    }

    public int nbAppliance(){
        return appliances.size();
    }

}
