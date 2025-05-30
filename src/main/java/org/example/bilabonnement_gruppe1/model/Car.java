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
    private boolean limited;
    private Integer monthlyFee;

    public Car() {
    }

    public Car(String vehicleNumber, String chassisnumber, String model, String status, String equipment, String image, double co2Emission, double kmDriven) {
        this.vehicleNumber = vehicleNumber;
        this.chassisnumber = chassisnumber;
        this.model = model;
        this.status = status;
        this.equipment = equipment;
        this.image = image;
        this.co2Emission = co2Emission;
        this.kmDriven = kmDriven;
    }

    public Car(String registrationNumber, String chassisnumber, String model, String equipment, double kmDriven, double co2Emission, String image, String status) {
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Car(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getChassisnumber() {
        return chassisnumber;
    }

    public void setChassisnumber(String chassisnumber) {
        this.chassisnumber = chassisnumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getCo2Emission() {
        return (double) co2Emission;
    }

    public void setCo2Emission(double co2Emission) {
        this.co2Emission = co2Emission;
    }

    public double getSteelPrice() {
        return steelPrice;
    }

    public void setSteelPrice(double steelPrice) {
        this.steelPrice = steelPrice;
    }

    public double getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(double registrationFee) {
        this.registrationFee = registrationFee;
    }

    public double getKmDriven() {
        return (double) kmDriven;
    }
    public void setKmDriven(double kmDriven) {
        this.kmDriven = kmDriven;
    }

    public boolean isLimited() {
        return limited;
    }

    public void setLimited(boolean limited) {
        this.limited = limited;
    }

    public Integer getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(Integer monthlyFee) {
        this.monthlyFee = monthlyFee;
    }
}
