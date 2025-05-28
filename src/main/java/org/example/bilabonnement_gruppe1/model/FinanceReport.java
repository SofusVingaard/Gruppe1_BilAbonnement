package org.example.bilabonnement_gruppe1.model;

import java.time.LocalDate;

//Christoffer, Gustav, Sofus

public class FinanceReport {
    private int id;
    private int rentalAgreementId;
    private double monthlyPrice;
    private double kmOverLimitCost;
    private double repairCost;
    private double damageCost;
    private double totalCost;
    private boolean paid;
    private LocalDate paymentDate;
    private String status;

    public FinanceReport() {
    }

    public FinanceReport(int rentalAgreementId, double monthlyPrice, double kmOverLimitCost,
                         double repairCost, double damageCost, double totalCost) {
        this.rentalAgreementId = rentalAgreementId;
        this.monthlyPrice = monthlyPrice;
        this.kmOverLimitCost = kmOverLimitCost;
        this.repairCost = repairCost;
        this.damageCost = damageCost;
        this.totalCost = totalCost;
        this.paid = false;
        this.status = "Unpaid";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRentalAgreementId() {
        return rentalAgreementId;
    }

    public void setRentalAgreementId(int rentalAgreementId) {
        this.rentalAgreementId = rentalAgreementId;
    }

    public double getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(double monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public double getKmOverLimitCost() {
        return kmOverLimitCost;
    }

    public void setKmOverLimitCost(double kmOverLimitCost) {
        this.kmOverLimitCost = kmOverLimitCost;
    }

    public double getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(double repairCost) {
        this.repairCost = repairCost;
    }

    public double getDamageCost() {
        return damageCost;
    }

    public void setDamageCost(double damageCost) {
        this.damageCost = damageCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}