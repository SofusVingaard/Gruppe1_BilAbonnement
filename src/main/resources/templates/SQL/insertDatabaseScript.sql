USE testdb;
USE bilabonnement;

    INSERT INTO car (chassisNumber, model, equipment, kmDriven, co2Emission, image, status)
                VALUES (CONCAT('fck-', FLOOR(RAND() * 100000)), 'Kia Picanto 2014', 'standard', 150000, 1.4, 'kiaPicanto.jpg', 'available');

INSERT INTO car (chassisNumber, model, equipment, kmDriven, co2Emission, image, status)
VALUES (CONCAT('fck-', FLOOR(RAND() * 100000)), 'BMW E6 2024', 'standard', 150000, 1.4, 'kiaPicanto.jpg', 'available');

INSERT INTO customer (id, name, email, phoneNumber)
values (2,'jens','jensjensen@gmail.com',87654321);

INSERT INTO rentalAgreement (carId, customerPhoneNumber, userLogin, startDate, endDate, active,allowedKM)
values (1,12345678,'demo','2025-08-20', '2026-08-20', true,20000) ;

INSERT INTO rentalAgreement (carId, customerPhoneNumber, userLogin, startDate, endDate, active,allowedKM)
values(2,87654321,'demo','2025-08-20', '2026-08-20', false,20000) ;

INSERT INTO damagereport (rentalAgreementId, note, repairCost)
VALUES (10, 'nej', 1500)





