package org.example.bilabonnement_gruppe1.model;

import java.time.LocalDate;

public class RentalAgreement {
    private int id;
    private Car car;
    private Customer customer;
    private String carId;
    private int customerPhoneNumber;
    private String userLogin;
    private User user;
    private DamageReport damageReport;
    private int damageReportId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private double allowedKM;

    public RentalAgreement(int customerPhoneNumber, String carId, String userLogin, DamageReport damageReport, LocalDate startDate, LocalDate endDate, boolean active, double kmOverLimit) {
        this.customerPhoneNumber = customerPhoneNumber;
        this.carId = carId;
        this.userLogin = userLogin;
        this.damageReport = damageReport;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.kmOverLimit = kmOverLimit;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public int getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(int customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public double getAllowedKM() {
        return allowedKM;
    }

    public void setAllowedKM(double allowedKM) {
        this.allowedKM = allowedKM;
    }

    public double getKmOverLimit() {
        return kmOverLimit;
    }

    public void setKmOverLimit(double kmOverLimit) {
        this.kmOverLimit = kmOverLimit;
    }

    public void setId(int id) {
        this.id = id;
    }

    private double kmOverLimit;


    //uden ID
    public RentalAgreement(Car car, Customer customer, User user,
                           LocalDate startDate, LocalDate endDate, boolean active, double allowedKM, double kmOverLimit) {
        this.car = car;
        this.customer = customer;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.allowedKM = allowedKM;
        this.kmOverLimit = kmOverLimit;
    }
    public RentalAgreement(){

    }

    public int getId() {
        return id;}

    public Car getCar() {
        return car;}

    public void setCar(Car car) {
        this.car = car;}

    public Customer getCustomer() {
        return customer;}

    public void setCustomer(Customer customer) {
        this.customer = customer;}

    public User getUser() {
        return user;}

    public void setUser(User user) {
        this.user = user;}

    public DamageReport getDamageReport() {
        return damageReport;}

    public void setDamageReport(DamageReport damageReport) {
        this.damageReport = damageReport;}

    public LocalDate getStartDate() {
        return startDate;}

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;}

    public LocalDate getEndDate() {
        return endDate;}

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;}

    public boolean isActive() {
        return active;}

    public void setActive(boolean active) {
        this.active = active;}

    public int getDamageReportId() {
        return damageReportId;
    }

    public void setDamageReportId(int damageReportId) {
        this.damageReportId = damageReportId;
    }
}