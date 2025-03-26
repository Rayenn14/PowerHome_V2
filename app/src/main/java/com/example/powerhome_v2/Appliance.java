package com.example.powerhome_v2;

public class Appliance {
    int id;
    String name;
    String references;
    int wattage;

    public Appliance(int id, String name, String references, int wattage) {
        this.id = id;
        this.name = name;
        this.references = references;
        this.wattage = wattage;
    }
}
