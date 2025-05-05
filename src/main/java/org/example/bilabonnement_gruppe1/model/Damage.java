package org.example.bilabonnement_gruppe1.model;

public class Damage {
    private int id;
    private int damageReportId;
    private String damageType;
    private double price;

    public Damage(String damageType, double price) {
        this.damageType = damageType;
        this.price = price;
    }

    public Damage() {
    }

    public Damage(int damageReportId, String damageType, double price) {
        this.damageReportId = damageReportId;
        this.damageType = damageType;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDamageReportId() {
        return damageReportId;
    }

    public void setDamageReportId(int damageReportId) {
        this.damageReportId = damageReportId;
    }

    public String getDamageType() {
        return damageType;
    }

    public void setDamageType(String damageType) {
        this.damageType = damageType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
