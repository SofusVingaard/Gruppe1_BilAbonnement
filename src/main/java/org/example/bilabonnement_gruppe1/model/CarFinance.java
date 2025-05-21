package org.example.bilabonnement_gruppe1.model;

import java.time.LocalDate;

public class CarFinance {

    private int id;
    private double totalPrice;
    private int damageReportId;
    private LocalDate date;
    private double co2Emission;
    private double kmOverLimit;
    private double rentalFee;
    private int rentalAgreementId;

    private boolean paid;

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public int getRentalAgreementId(){
        return rentalAgreementId;
    }

    public int getId() {
        return id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getDamageReportId() {
        return damageReportId;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getCo2Emission() {
        return co2Emission;
    }

    public double getKmOverLimit() {
        return kmOverLimit;
    }

    public double getRentalFee() {
        return rentalFee;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDamageReportId(int damageReportId) {
        this.damageReportId = damageReportId;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCo2Emission(double Co2Emission) {
        this.co2Emission = Co2Emission;
    }

    public void setKmOverLimit(double kmOverLimit) {
        this.kmOverLimit = kmOverLimit;
    }

    public void setRentalFee(double rentalFee) {
        this.rentalFee = rentalFee;
    }

    public void setRentalAgreementId(int rentalAgreementId){
        this.rentalAgreementId = rentalAgreementId;
    }
}
