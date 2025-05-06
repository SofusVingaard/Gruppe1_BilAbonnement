package org.example.bilabonnement_gruppe1.model;

import java.util.List;

public class DamageReport {
    private int id;
    private int userID;
    private double kmOverLimit;
    private double repairCost;
    private List<Damage> damage;

    public DamageReport() {
    }

    public DamageReport(int userID, double kmOverLimit) {
        this.userID = userID;
        this.kmOverLimit = kmOverLimit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Damage> getDamage() {
        return damage;
    }

    public void setDamage(List<Damage> damage) {
        this.damage = damage;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
