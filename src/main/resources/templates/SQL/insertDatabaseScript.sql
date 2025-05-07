USE bilabonnement;

    INSERT INTO car (chassisNumber, model, equipment, kmDriven, co2Emission, image, status)
                VALUES (CONCAT('fck-', FLOOR(RAND() * 100000)), 'Kia Picanto 2014', 'standard', 150000, 1.4, 'kiaPicanto.jpg', 'available');

INSERT INTO car (chassisNumber, model, equipment, kmDriven, co2Emission, image, status)
VALUES (CONCAT('fck-', FLOOR(RAND() * 100000)), 'BMW E6 2024', 'standard', 150000, 1.4, 'kiaPicanto.jpg', 'available');