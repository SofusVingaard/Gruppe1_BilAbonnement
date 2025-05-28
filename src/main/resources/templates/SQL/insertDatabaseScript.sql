USE bilabonnement;

-- Indsæt 1 bruger
INSERT INTO user (userLogin, name, password) VALUES
    ('demo', 'demo', 'demo');

-- Indsæt 5 kunder
INSERT INTO customer (name, email, phoneNumber) VALUES
('Kunde A', 'kundeA@example.com', 11111111),
('Kunde B', 'kundeB@example.com', 22222222),
('Kunde C', 'kundeC@example.com', 33333333),
('Kunde D', 'kundeD@example.com', 44444444),
('Kunde E', 'kundeE@example.com', 55555555);

-- Indsæt 5 ledige biler
INSERT INTO car (vehicleNumber, chassisNumber, model, equipment, kmDriven, co2Emission, image, status, limited, monthlyFee) VALUES
('CAR001', 'CHASSIS001', 'Tesla Model 3', 'Standard', 10000, 0, 'image1.jpg', 'available', FALSE, 4500),
('CAR002', 'CHASSIS002', 'BMW i3', 'Premium', 15000, 0, 'image2.jpg', 'available', TRUE, 4000),
('CAR003', 'CHASSIS003', 'Audi e-tron', 'Advanced', 12000, 0, 'image3.jpg', 'available', FALSE, 4700),
('CAR004', 'CHASSIS004', 'Hyundai Kona EV', 'Standard+', 9000, 0, 'image4.jpg', 'available', FALSE, 3900),
('CAR005', 'CHASSIS005', 'Renault Zoe', 'Comfort', 8000, 0, 'image5.jpg', 'available', TRUE, 3700);

-- Indsæt 5 brugte biler (til lejeaftaler)
INSERT INTO car (vehicleNumber, chassisNumber, model, equipment, kmDriven, co2Emission, image, status, limited, monthlyFee) VALUES
('CAR006', 'CHASSIS006', 'Peugeot e-208', 'GT', 20000, 0, 'image6.jpg', 'rented', FALSE, 4100),
('CAR007', 'CHASSIS007', 'VW ID.4', 'Life', 25000, 0, 'image7.jpg', 'rented', FALSE, 4600),
('CAR008', 'CHASSIS008', 'Skoda Enyaq', 'Sportline', 23000, 0, 'image8.jpg', 'rented', FALSE, 4500),
('CAR009', 'CHASSIS009', 'Kia EV6', 'GT-Line', 19000, 0, 'image9.jpg', 'rented', FALSE, 4900),
('CAR010', 'CHASSIS010', 'Ford Mustang Mach-E', 'Premium AWD', 22000, 0, 'image10.jpg', 'rented', FALSE, 5000);

-- Indsæt 5 skadesrapporter
INSERT INTO damageReport (note, repairCost) VALUES
('Skade på front', 2000),
('ridset dør', 1500),
('Knust bagrude', 3000),
('Lakskade', 1000),
('Punkteret dæk', 700);

-- Indsæt 5 skader
INSERT INTO damage (damageReportId, damageType, price) VALUES
(1,'Skade på front', 2000),
(2,'ridset dør', 1500),
(3,'Knust bagrude', 3000),
(4,'Lakskade', 1000),
(5,'Punkteret dæk', 700);


-- Indsæt 5 lejeaftaler
INSERT INTO rentalAgreement (carId, customerPhoneNumber, userLogin, damageReportId, startDate, endDate, monthsRented, active, allowedKM, kmOverLimit, monthlyCarPrice, totalPrice) VALUES
('CAR006', 11111111, 'demo', 1, '2024-01-01', '2024-04-01', 3, FALSE, 1500, 300, 4100, 13800.00),
('CAR007', 22222222, 'demo', 2, '2024-02-01', '2024-05-01', 3, FALSE, 1500, 100, 4600, 14100.00),
('CAR008', 33333333, 'demo', 3, '2024-03-01', '2024-06-01', 3, FALSE, 1500, 200, 4500, 14400.00),
('CAR009', 44444444, 'demo', 4, '2024-01-15', '2024-04-15', 3, FALSE, 1500, 150, 4900, 15000.00),
('CAR010', 55555555, 'demo', 5, '2024-02-10', '2024-05-10', 3, FALSE, 1500, 250, 5000, 15750.00);


INSERT INTO financeReport (rentalAgreementId, monthlyPrice, kmOverLimitCost, repairCost, damageCost, totalCost, paid, paymentDate, status) VALUES
(1, 4100.00, 900.00, 2000.00, 0.00, 13800.00, TRUE, '2024-04-02', 'Paid'),
(2, 4600.00, 300.00, 1500.00, 0.00, 14100.00, TRUE, '2024-05-03', 'Paid'),
(3, 4500.00, 600.00, 3000.00, 0.00, 14400.00, TRUE, '2024-06-04', 'Paid'),
(4, 4900.00, 450.00, 1000.00, 0.00, 15000.00, TRUE, '2024-04-16', 'Paid'),
(5, 5000.00, 750.00, 700.00, 0.00, 15750.00, TRUE, '2024-05-11', 'Paid');


