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
    limited boolean DEFAULT FALSE,
    monthlyFee int
);


CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(250),
    email VARCHAR(250),
    phoneNumber INT UNIQUE
);

CREATE TABLE damageReport(
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             note TEXT,
                             repairCost DOUBLE
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
    monthsRented INT,
    active boolean DEFAULT TRUE,
    allowedKM double,
    kmOverLimit double,
    monthlyCarPrice INT,
    totalPrice DECIMAL(10,2),




    FOREIGN KEY (carId) REFERENCES car (vehicleNumber),
    FOREIGN KEY (customerPhoneNumber) REFERENCES customer (phoneNumber),
    FOREIGN KEY (userLogin) REFERENCES user (userLogin),
    FOREIGN KEY (damageReportId) REFERENCES damageReport (id)
);



CREATE TABLE IF NOT EXISTS financeReport (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rentalAgreementId INT NOT NULL,
    monthlyPrice DECIMAL(10,2) NOT NULL,
    kmOverLimitCost DECIMAL(10,2) NOT NULL,
    repairCost DECIMAL(10,2) NOT NULL,
    damageCost DECIMAL(10,2) NOT NULL,
    totalCost DECIMAL(10,2) NOT NULL,
    paid BOOLEAN DEFAULT FALSE,
    paymentDate DATE,
    status VARCHAR(20) DEFAULT 'Unpaid',
    FOREIGN KEY (rentalAgreementId) REFERENCES rentalAgreement(id)
    );






