package org.example.bilabonnement_gruppe1.repository;

import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class RentalAgreementRepository {

    @Autowired
    private DataSource dataSource;

    public void createRentalAgreement(RentalAgreement agreement) {
        String sql = "INSERT INTO rentalAgreement " +
                "(carId, customerId, userLogin, damageReportId, startDate, endDate, active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, agreement.getCar().getVehicleNumber());
            statement.setInt(2, agreement.getCustomer().getId());
            statement.setString(3, agreement.getUser().getUserLogin());
            statement.setInt(4, agreement.getDamageReport().getId());
            statement.setDate(5, java.sql.Date.valueOf(agreement.getStartDate()));
            statement.setDate(6, java.sql.Date.valueOf(agreement.getEndDate()));
            statement.setBoolean(7, agreement.isActive());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Fejl ved oprettelse af lejeaftale: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateRentalAgreement(RentalAgreement agreement) {
        String sql = "UPDATE rentalAgreement SET " + "carId = ?, " + "customerId = ?, " + "userId = ?, " +
                "damageReportId = ?, " + "startDate = ?, " + "endDate = ?, " + "active = ? " + "WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, agreement.getCar().getVehicleNumber());
            statement.setInt(2, agreement.getCustomer().getId());
            statement.setInt(3, agreement.getUser().getId());
            statement.setInt(4, agreement.getDamageReport().getId());
            statement.setDate(5, java.sql.Date.valueOf(agreement.getStartDate()));
            statement.setDate(6, java.sql.Date.valueOf(agreement.getEndDate()));
            statement.setBoolean(7, agreement.isActive());

            statement.setInt(8, agreement.getId()); // WHERE id = ?

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Fejl ved opdatering af lejeaftale: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteRentalAgreement(int id) {
        String sql = "DELETE FROM rentalAgreement WHERE id = ?";

        try( Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){

        statement.setInt(1,id);
        statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

