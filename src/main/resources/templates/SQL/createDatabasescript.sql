CREATE DATABASE IF NOT EXISTS bilabonnement;
USE bilabonnement;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userLogin VARCHAR(100) UNIQUE,
    name VARCHAR(50),
    password VARCHAR(100)
);

CREATE TABLE car (
    vehicleNumber INT AUTO_INCREMENT PRIMARY KEY,
    chassisNumber VARCHAR(250) UNIQUE,
    model VARCHAR(250) NOT NULL,
    equipment VARCHAR(250),
    kmDriven INT,
    co2Emission INT,
    image VARCHAR(250),
    status VARCHAR(50)
);

CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250),
    email VARCHAR(250),
    phoneNumber VARCHAR(250)
);

CREATE TABLE damageReport(
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             userId INT,
                             kmOverLimit DOUBLE,
                             repairCost DOUBLE,

                             FOREIGN KEY (userId) REFERENCES user (id)
);

CREATE TABLE damage (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        damageReportId INT,
                        damageType VARCHAR(500),
                        price DOUBLE,

                        FOREIGN KEY (damageReportId) REFERENCES damageReport(id)
);

CREATE TABLE rentalAgreement (
    id INT AUTO_INCREMENT PRIMARY KEY,
    carId INT,
    customerId int,
    userId int,
    damageReportId INT,
    startDate date,
    endDate date,
    active boolean DEFAULT TRUE,


    FOREIGN KEY (carId) REFERENCES car (vehicleNumber),
    FOREIGN KEY (customerId) REFERENCES customer (id),
    FOREIGN KEY (userId) REFERENCES user (id),
    FOREIGN KEY (damageReportId) REFERENCES damageReport (id)
);


CREATE TABLE carFinance(
    id INT AUTO_INCREMENT PRIMARY KEY,
    totalPrice DOUBLE
)