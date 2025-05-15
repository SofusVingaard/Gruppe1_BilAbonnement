package org.example.bilabonnement_gruppe1.repository;

import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

@Repository
public class RentalAgreementRepository {

    @Autowired
    private DataSource dataSource;

    public void createRentalAgreement(RentalAgreement agreement) {
        String rentalSql = "INSERT INTO rentalAgreement " +
                "(carId, customerPhoneNumber, userLogin, startDate, endDate, active, allowedKM) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String damageSql = "INSERT INTO damageReport (rentalAgreementId, repairCost, note) VALUES (?, ?, ?)";

        String updateCar = "UPDATE car SET status = 'Udlejet' WHERE vehicleNumber = ?";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement rentalStmt = connection.prepareStatement(rentalSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement damageStmt = connection.prepareStatement(damageSql);
             PreparedStatement updateCarStmt = connection.prepareStatement(updateCar)

        )

        {

            rentalStmt.setString(1, agreement.getCar().getVehicleNumber());
            rentalStmt.setInt(2, agreement.getCustomerPhoneNumber());
            rentalStmt.setString(3, agreement.getUser().getUserLogin());
            rentalStmt.setDate(4, java.sql.Date.valueOf(agreement.getStartDate()));
            rentalStmt.setDate(5, java.sql.Date.valueOf(agreement.getEndDate()));
            rentalStmt.setBoolean(6, agreement.isActive());
            rentalStmt.setDouble(7, agreement.getAllowedKM());

            rentalStmt.executeUpdate();

            ResultSet generatedKeys = rentalStmt.getGeneratedKeys();



            if (generatedKeys.next()) {
                int rentalAgreementId = generatedKeys.getInt(1);

                damageStmt.setInt(1, rentalAgreementId);
                damageStmt.setDouble(2, 0.0);
                damageStmt.setString(3, "");
                damageStmt.executeUpdate();

                updateCarStmt.setString(1, agreement.getCar().getVehicleNumber());
                updateCarStmt.executeUpdate();


            }




        } catch (SQLException e) {
            System.err.println("Fejl ved oprettelse af lejeaftale og damageReport: " + e.getMessage());
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

            statement.setBoolean(6, agreement.isActive());
            statement.setDouble(7, agreement.getAllowedKM());
            statement.setDouble(8, agreement.getKmOverLimit());
            statement.setInt(9, agreement.getId());

            // Udfør opdateringen
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


    public ArrayList<RentalAgreement> getActiveRentalAgreements() {
        return getRentalAgreementsByActiveStatus(true);
    }

    public ArrayList<RentalAgreement> getInactiveRentalAgreements() {
        return getRentalAgreementsByActiveStatus(false);
    }

        public ArrayList<RentalAgreement> getRentalAgreementByPhoneNumber(int customerPhoneNumber)  {
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

    public RentalAgreement getRentalAgreement(int id){
        String sql = "SELECT * FROM rentalAgreement WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                RentalAgreement agreement = new RentalAgreement();
                agreement.setId(resultSet.getInt("id"));
                agreement.setCarId(resultSet.getString("carId"));
                agreement.setCustomerPhoneNumber(resultSet.getInt("customerPhoneNumber"));
                agreement.setDamageReportId(resultSet.getInt("damageReportId"));
                agreement.setStartDate(resultSet.getDate("startDate").toLocalDate());
                agreement.setEndDate(resultSet.getDate("endDate").toLocalDate());
                agreement.setActive(resultSet.getBoolean("active"));
                agreement.setAllowedKM(resultSet.getDouble("allowedKM"));
                agreement.setKmOverLimit(resultSet.getDouble("kmOverLimit"));
                return agreement;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }















































    public ArrayList<RentalAgreement> getAllRentalAgreements()  {
        ArrayList<RentalAgreement> agreements = new ArrayList<>();
        String sql = "SELECT * FROM rentalAgreement";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                RentalAgreement agreement = new RentalAgreement();
                agreement.setId(resultSet.getInt("id"));
                agreement.setCarId(resultSet.getString("carId"));
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
            e.printStackTrace();
        }
        return agreements;
    }

    /*public void updateRentalAgreementDamageReport(int rentalAgreementId, DamageReport damageReport) {
        String sql = "UPDATE rentalAgreement SET damageReportId = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, damageReport.getId());
            statement.setInt(2, rentalAgreementId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/


}

