package org.example.bilabonnement_gruppe1.repository;

import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public ArrayList<RentalAgreement> getRentalAgreementsByActiveStatus(boolean active) {
        ArrayList<RentalAgreement> agreements = new ArrayList<>();
        String sql = "SELECT * FROM rentalAgreement WHERE active = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, active);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                RentalAgreement agreement = new RentalAgreement();
                agreement.setId(resultSet.getInt("id"));
                agreement.setStartDate(resultSet.getDate("startDate").toLocalDate());
                agreement.setEndDate(resultSet.getDate("endDate").toLocalDate());
                agreement.setActive(resultSet.getBoolean("active"));

                // Du kan fylde resten ud med fx: carId, customerId, userId osv.
                // Eller lave joins senere hvis du vil vise fx navn i stedet for ID

                agreements.add(agreement);
            }

        } catch (SQLException e) {
            System.err.println("Fejl ved hentning af lejeaftaler: " + e.getMessage());
            e.printStackTrace();
        }

        return agreements;
    }


    public ArrayList<RentalAgreement> getActiveRentalAgreements() {
        return getRentalAgreementsByActiveStatus(true);
    }

    public ArrayList<RentalAgreement> getInactiveRentalAgreements() {
        return getRentalAgreementsByActiveStatus(false);
    }

    public ArrayList<RentalAgreement> getRentalAgreementByPhoneNumber(int customerPhoneNumber) throws SQLException {
        ArrayList<RentalAgreement> agreements = new ArrayList<>();
        String sql = "SELECT * FROM rentalAgreement WHERE customerPhoneNumber = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){
                 statement.setInt(1, customerPhoneNumber);
                 ResultSet resultSet = statement.executeQuery();
                 while (resultSet.next()) {
                     RentalAgreement agreement = new RentalAgreement();
                     agreement.setId(resultSet.getInt("id"));
                     agreement.setCustomerPhoneNumber(resultSet.getInt("customerPhoneNumber"));
                     agreement.setUserLogin(resultSet.getString("userLogin"));
                     agreement.setStartDate(resultSet.getDate("startDate").toLocalDate());
                     agreement.setEndDate(resultSet.getDate("endDate").toLocalDate());
                     agreement.setActive(resultSet.getBoolean("active"));
                     agreement.setAllowedKM(resultSet.getDouble("allowedKM"));
                     agreement.setKmOverLimit(resultSet.getDouble("kmOverLimit"));


                     agreements.add(agreement);
                 }

        } catch (SQLException e) {
            System.err.println("Fejl ved hentning af lejeaftaler: " + e.getMessage());
            e.printStackTrace();
        }

        return agreements;
    }



    public int countActiveAgreements() {
        String sql = "SELECT COUNT(*) FROM rentalAgreement WHERE active = TRUE";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            var result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double averageRentalPeriodLength() {
        String sql = "SELECT AVG(DATEDIFF(endDate, startDate)) FROM rentalAgreement";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            var result = statement.executeQuery();
            if (result.next()) {
                return result.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}

