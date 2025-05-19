package org.example.bilabonnement_gruppe1.repository;

import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public class RentalAgreementRepository {

    @Autowired
    private DataSource dataSource;

    public void createRentalAgreement(RentalAgreement agreement) {
        String damageSql = "INSERT INTO damageReport (repairCost, note) VALUES (?, ?)";

        String rentalSql = "INSERT INTO rentalAgreement " +
                "(carId, customerPhoneNumber, userLogin, startDate, endDate, active, allowedKM, kmOverLimit, totalPrice, monthsRented, monthlyCarPrice, damageReportId) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String updateCarSql = "UPDATE car SET status = 'Udlejet' WHERE vehicleNumber = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement damageStmt = connection.prepareStatement(damageSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement rentalStmt = connection.prepareStatement(rentalSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement updateCarStmt = connection.prepareStatement(updateCarSql)) {

            // 1. Opret damageReport
            damageStmt.setDouble(1, 0.0);
            damageStmt.setString(2, "");
            damageStmt.executeUpdate();

            ResultSet damageKeys = damageStmt.getGeneratedKeys();
            int damageReportId = 0;
            if (damageKeys.next()) {
                damageReportId = damageKeys.getInt(1);
            }

            // 2. Opret rentalAgreement med damageReportId
            rentalStmt.setString(1, agreement.getCar().getVehicleNumber());
            rentalStmt.setInt(2, agreement.getCustomerPhoneNumber());
            rentalStmt.setString(3, agreement.getUser().getUserLogin());
            rentalStmt.setDate(4, java.sql.Date.valueOf(agreement.getStartDate()));
            rentalStmt.setDate(5, java.sql.Date.valueOf(agreement.getEndDate()));
            rentalStmt.setBoolean(6, agreement.isActive());
            rentalStmt.setDouble(7, agreement.getAllowedKM());
            rentalStmt.setDouble(8, agreement.getKmOverLimit()); // eller 0.0 hvis ukendt
            rentalStmt.setInt(9, agreement.getTotalPrice());
            rentalStmt.setInt(10, agreement.getMonthsRented());
            rentalStmt.setInt(11, agreement.getMonthlyCarPrice());
            rentalStmt.setInt(12, damageReportId);

            rentalStmt.executeUpdate();

            // 3. Opdater bilens status
            updateCarStmt.setString(1, agreement.getCar().getVehicleNumber());
            updateCarStmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Fejl ved oprettelse af lejeaftale og damageReport: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void updateRentalAgreement(RentalAgreement agreement) {
        String sql = "UPDATE rentalAgreement SET " +
                "carId = ?, " +
                "customerPhoneNumber = ?, " +
                "active = ?, " +
                "allowedKM = ?, " +
                "kmOverLimit = ? " +
                "WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, agreement.getCarId());
            statement.setInt(2, agreement.getCustomerPhoneNumber());
            statement.setBoolean(3, agreement.isActive());
            statement.setDouble(4, agreement.getAllowedKM());
            statement.setDouble(5, agreement.getKmOverLimit());
            statement.setInt(6, agreement.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Fejl ved opdatering af lejeaftale: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void deleteRentalAgreement(int id) {
        String sql = "DELETE FROM rentalAgreement WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
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

                Date sqlStartDate = resultSet.getDate("startDate");
                LocalDate startDate = (sqlStartDate != null) ? sqlStartDate.toLocalDate() : null;
                agreement.setStartDate(startDate);


                Date sqlEndDate = resultSet.getDate("endDate");
                LocalDate endDate = (sqlEndDate != null) ? sqlEndDate.toLocalDate() : null;
                agreement.setEndDate(endDate);

                agreement.setActive(resultSet.getBoolean("active"));
                agreement.setAllowedKM(resultSet.getDouble("allowedKM"));
                agreement.setKmOverLimit(resultSet.getDouble("kmOverLimit"));
                int monthlyPrice=resultSet.getInt("monthlyCarPrice");
                int allowedKM=resultSet.getInt("allowedKM");
                int allowedKMPrice=0;
                if (allowedKM==1750){
                    allowedKMPrice=250;
                }if (allowedKM==2000){
                    allowedKMPrice=450;
                }
                monthlyPrice+=allowedKMPrice;
                agreement.setMonthlyCarPrice(monthlyPrice);
                System.out.println("Lejeaftale ID: " + agreement.getId() + ", Pris/måned: " + agreement.getMonthlyCarPrice());

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

    public ArrayList<RentalAgreement> getRentalAgreementByPhoneNumber(int customerPhoneNumber) {
        ArrayList<RentalAgreement> agreements = new ArrayList<>();
        String sql = "SELECT * FROM rentalAgreement WHERE customerPhoneNumber = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerPhoneNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                RentalAgreement agreement = new RentalAgreement();
                agreement.setId(resultSet.getInt("id"));
                agreement.setCustomerPhoneNumber(resultSet.getInt("customerPhoneNumber"));
                agreement.setUserLogin(resultSet.getString("userLogin"));

                Date sqlStartDate = resultSet.getDate("startDate");
                LocalDate startDate = (sqlStartDate != null) ? sqlStartDate.toLocalDate() : null;
                agreement.setStartDate(startDate);


                Date sqlEndDate = resultSet.getDate("endDate");
                LocalDate endDate = (sqlEndDate != null) ? sqlEndDate.toLocalDate() : null;
                agreement.setEndDate(endDate);

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

    public RentalAgreement getRentalAgreement(int id) {
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

                Date sqlStartDate = resultSet.getDate("startDate");
                LocalDate startDate = (sqlStartDate != null) ? sqlStartDate.toLocalDate() : null;
                agreement.setStartDate(startDate);


                Date sqlEndDate = resultSet.getDate("endDate");
                LocalDate endDate = (sqlEndDate != null) ? sqlEndDate.toLocalDate() : null;
                agreement.setEndDate(endDate);

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


    public ArrayList<RentalAgreement> getAllRentalAgreements() {
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

                Date sqlStartDate = resultSet.getDate("startDate");
                LocalDate startDate = (sqlStartDate != null) ? sqlStartDate.toLocalDate() : null;
                agreement.setStartDate(startDate);

                Date sqlEndDate = resultSet.getDate("endDate");
                LocalDate endDate = (sqlEndDate != null) ? sqlEndDate.toLocalDate() : null;
                agreement.setEndDate(endDate);
                agreement.setActive(resultSet.getBoolean("active"));
                agreement.setAllowedKM(resultSet.getDouble("allowedKM"));
                agreement.setKmOverLimit(resultSet.getDouble("kmOverLimit"));

                int monthlyPrice=resultSet.getInt("monthlyCarPrice");
                int allowedKM=resultSet.getInt("allowedKM");
                int allowedKMPrice=0;
                if (allowedKM==1750){
                    allowedKMPrice=250;
                }if (allowedKM==2000){
                    allowedKMPrice=450;
                }
                monthlyPrice+=allowedKMPrice;
                agreement.setMonthlyCarPrice(monthlyPrice);


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


    public RentalAgreement findById(int id) {
        return null;
    }
}