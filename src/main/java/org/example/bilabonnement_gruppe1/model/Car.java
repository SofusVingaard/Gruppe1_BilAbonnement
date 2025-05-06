package org.example.bilabonnement_gruppe1.model;

public class Car {


    private String vehicleNumber;
    private String chassisnumber;
    private String brand;
    private String model;
    private int year;
    private String color;
    private String licensePlate;
    private double price;
    private String status;
    private String equipment;
    private String image;
    private double co2Emission;
    private double steelPrice;
    private double registrationFee;
    private double kmDriven;

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Car(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

}
