CREATE DATABASE IF NOT EXISTS bilabonnement;
USE bilabonnement;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userLogin VARCHAR(100) UNIQUE,
    name VARCHAR(50),
    password VARCHAR(100)
);

CREATE TABLE car (
    vehicleNumber VARCHAR(50) PRIMARY KEY,
    chassisNumber VARCHAR(250) UNIQUE,
    model VARCHAR(250) NOT NULL,
    equipment VARCHAR(250),
    kmDriven INT,
    co2Emission INT,
    image VARCHAR(250),
    status VARCHAR(50),
    limited boolean,
    monthlyFee int
);

ALTER TABLE car ADD COLUMN limited BOOLEAN DEFAULT FALSE;
ALTER TABLE car ADD COLUMN monthlyFee INT;

CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250),
    email VARCHAR(250),
    phoneNumber INT UNIQUE
);

CREATE TABLE damageReport(
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             rentalAgreementId INT,
                             note TEXT,
                             repairCost DOUBLE,

                             FOREIGN KEY (rentalAgreementId) REFERENCES rentalAgreement (id)
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
    carId VARCHAR(50),
    customerPhoneNumber int,
    userLogin VARCHAR(100),
    damageReportId INT,
    startDate date,
    endDate date,
    active boolean DEFAULT TRUE,
    allowedKM double,
    kmOverLimit double,
    totalPrice INT,



    FOREIGN KEY (carId) REFERENCES car (vehicleNumber),
    FOREIGN KEY (customerPhoneNumber) REFERENCES customer (phoneNumber),
    FOREIGN KEY (userLogin) REFERENCES user (userLogin),
    FOREIGN KEY (damageReportId) REFERENCES damageReport (id)
);


CREATE TABLE carFinance(
    id INT AUTO_INCREMENT PRIMARY KEY,
    totalPrice DOUBLE
);