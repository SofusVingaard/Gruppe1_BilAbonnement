package org.example.bilabonnement_gruppe1.model;

import java.time.LocalDate;

public class RentalAgreement {
    private int id;
    private Car car;
    private Customer customer;
    private User user;
    private DamageReport damageReport;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;


    //uden ID
    public RentalAgreement(Car car, Customer customer, User user,
                           LocalDate startDate, LocalDate endDate, boolean active) {
        this.car = car;
        this.customer = customer;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }

    // med id
    public RentalAgreement(int id, Car car, Customer customer, User user,
                           DamageReport damageReport, LocalDate startDate, LocalDate endDate, boolean active) {
        this(car, customer, user, startDate, endDate, active);
        this.id = id;
        this.damageReport = damageReport;
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
}