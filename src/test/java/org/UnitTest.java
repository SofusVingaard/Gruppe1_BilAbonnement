package org;


import jakarta.servlet.http.HttpSession;
import org.example.bilabonnement_gruppe1.controller.CarController;
import org.example.bilabonnement_gruppe1.controller.RentalAgreementController;
import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.example.bilabonnement_gruppe1.repository.CarRepository;
import org.example.bilabonnement_gruppe1.repository.RentalAgreementRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;



@ExtendWith(MockitoExtension.class)
public class UnitTest {


    @Mock
    RentalAgreementRepository rentalAgreementRepository;

    @Mock
    Model model;

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarController carController;

    @InjectMocks
    RentalAgreementController rentalAgreementController;


    // Happy flow test
    @Test
    public void findRentalAgreementsHappyFlow() {
        // Assumption

        ArrayList<RentalAgreement> agreements = new ArrayList<>();
        RentalAgreement rentalAgreement = new RentalAgreement();
        rentalAgreement.setId(1);
        agreements.add(rentalAgreement);
        given(rentalAgreementRepository.getAllRentalAgreements()).willReturn(agreements);

        // Execution
        HttpSession session= Mockito.mock(HttpSession.class);
        Mockito.when(session.getAttribute("currentUser")).thenReturn(new Object());
        String viewName = rentalAgreementController.rentalAgreement(model, session);

        // Validation
        assertEquals("rentalAgreement", viewName);
        verify(model).addAttribute("agreements", agreements);
    }

    //Exception flow test
    @Test
    public void showCarsExceptionFlow() {
        // Assumption
        String status = "test";
        RuntimeException mockException = new RuntimeException("Database fejl");

        // Execution
        given(carRepository.getCarsByStatus(status)).willThrow(mockException);
        HttpSession session= Mockito.mock(HttpSession.class);
        Mockito.when(session.getAttribute("currentUser")).thenReturn(new Object());
        String result = carController.filterCars(status, model, session);

        // Validation
        assertEquals("carList", result);
        verify(model).addAttribute("error", "Kunne ikke hente biler.");
        verify(model).addAttribute(eq("carList"), any(ArrayList.class));
        verify(model).addAttribute("selectedStatus", status);
    }
}

