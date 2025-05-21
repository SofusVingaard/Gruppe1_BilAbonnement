package org;


import org.example.bilabonnement_gruppe1.controller.RentalAgreementController;
import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.example.bilabonnement_gruppe1.repository.RentalAgreementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class Show_RentalAgreement_UnitTest {

    @Mock
    RentalAgreementRepository rentalAgreementRepository;

    @Mock
    Model model;

    @InjectMocks
    RentalAgreementController rentalAgreementController;


    // Happy flow test
    @Test
    public void findRentalAgreements_shouldAddAgreementsToModel() {
        // Assumption
        ArrayList<RentalAgreement> agreements = new ArrayList<>();
        RentalAgreement rentalAgreement = new RentalAgreement();
        rentalAgreement.setId(1);
        agreements.add(rentalAgreement);
        given(rentalAgreementRepository.getAllRentalAgreements()).willReturn(agreements);

        // Execution
        String viewName = rentalAgreementController.rentalAgreement(model);

        // Validation
        assertEquals("rentalAgreement", viewName);
        verify(model).addAttribute("agreements", agreements);
    }
}

