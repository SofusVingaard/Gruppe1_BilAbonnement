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
}
