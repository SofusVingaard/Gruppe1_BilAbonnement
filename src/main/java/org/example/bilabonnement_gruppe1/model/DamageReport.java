package org.example.bilabonnement_gruppe1.model;

import java.util.List;

public class DamageReport {
    private int id;
    private int userID;
    private int rentalAgreementID;  // Add this field
    private double kmOverLimit;
    private double repairCost;
    private String note;           // Add this field
    private List<Damage> damage;

    public DamageReport() {
    }

    public DamageReport(int id) {
        this.id = id;
    }

    public DamageReport(int userID, double kmOverLimit) {
        this.userID = userID;
        this.kmOverLimit = kmOverLimit;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getRentalAgreementID() {
        return rentalAgreementID;
    }

    public void setRentalAgreementID(int rentalAgreementID) {
        this.rentalAgreementID = rentalAgreementID;
    }

    public double getKmOverLimit() {
        return kmOverLimit;
    }

    public void setKmOverLimit(double kmOverLimit) {
        this.kmOverLimit = kmOverLimit;
    }

    public double getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(double repairCost) {
        this.repairCost = repairCost;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Damage> getDamage() {
        return damage;
    }

    public void setDamage(List<Damage> damage) {
        this.damage = damage;
    }
}