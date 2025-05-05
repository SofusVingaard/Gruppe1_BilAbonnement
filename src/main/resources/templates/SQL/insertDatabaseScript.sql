USE bilabonnement;

    INSERT INTO car (chassisNumber, model, equipment, kmDriven, co2Emission, image, status)
                VALUES (CONCAT('ch-', FLOOR(RAND() * 100000)), 'Kia Picanto 2014', 'standard', 15000, 1.4, 'kiaPicanto.jpg', 'available');
