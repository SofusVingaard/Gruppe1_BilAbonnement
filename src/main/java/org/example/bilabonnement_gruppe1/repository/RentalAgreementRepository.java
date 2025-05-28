package org.example.bilabonnement_gruppe1.repository;

import org.example.bilabonnement_gruppe1.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public class RentalAgreementRepository {

    @Autowired
    private DataSource dataSource;

    // Sofus
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

            // Opretter damageReport først
            damageStmt.setDouble(1, 0.0);
            damageStmt.setString(2, "");
            damageStmt.executeUpdate();

            ResultSet damageKeys = damageStmt.getGeneratedKeys();
            int damageReportId = 0;
            if (damageKeys.next()) {
                damageReportId = damageKeys.getInt(1);
            }

            // Oprettelse af rentalAgreement inklusiv damageReportId
            rentalStmt.setString(1, agreement.getCar().getVehicleNumber());
            rentalStmt.setInt(2, agreement.getCustomerPhoneNumber());
            rentalStmt.setString(3, agreement.getUser().getUserLogin());
            rentalStmt.setDate(4, java.sql.Date.valueOf(agreement.getStartDate()));
            rentalStmt.setDate(5, java.sql.Date.valueOf(agreement.getEndDate()));
            rentalStmt.setBoolean(6, agreement.isActive());
            rentalStmt.setDouble(7, agreement.getAllowedKM());
            rentalStmt.setDouble(8, agreement.getKmOverLimit());
            rentalStmt.setDouble(9, agreement.getTotalPrice());
            rentalStmt.setInt(10, agreement.getMonthsRented());
            rentalStmt.setInt(11, agreement.getMonthlyCarPrice());
            rentalStmt.setInt(12, damageReportId);

            rentalStmt.executeUpdate();

            // Den tilknyttede bil får opdateret sin status
            updateCarStmt.setString(1, agreement.getCar().getVehicleNumber());
            updateCarStmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Fejl ved oprettelse af lejeaftale og damageReport: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Thomas
    public void updateRentalAgreement(RentalAgreement agreement) {
        String sql = "UPDATE rentalAgreement SET " +
                "carId = ?, " +
                "customerPhoneNumber = ?, " +
                "active = ?, " +
                "allowedKM = ?, " +
                "kmOverLimit = ?, " +
                "totalPrice = totalPrice + ? " +
                "WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            double kmPenalty = agreement.getKmOverLimit()*0.75; // 0.75 kr per kmOverLimit

            statement.setString(1, agreement.getCarId());
            statement.setInt(2, agreement.getCustomerPhoneNumber());
            statement.setBoolean(3, agreement.isActive());
            statement.setDouble(4, agreement.getAllowedKM());
            statement.setDouble(5, agreement.getKmOverLimit());
            statement.setDouble(6, kmPenalty);
            statement.setInt(7, agreement.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Fejl ved opdatering af lejeaftale: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Thomas
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

    // Thomas
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

    //Christoffer
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

    //Christoffer
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

    //Christoffer
    public RentalAgreement getRentalAgreement(int id) {
        String sql = "SELECT ra.*, dr.repairCost FROM rentalAgreement ra " +
                "LEFT JOIN damageReport dr ON ra.damageReportId = dr.id " +
                "WHERE ra.id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                RentalAgreement agreement = new RentalAgreement();

                agreement.setId(resultSet.getInt("id"));
                agreement.setCarId(resultSet.getString("carId"));
                agreement.setCustomerPhoneNumber(resultSet.getInt("customerPhoneNumber"));
                agreement.setUserLogin(resultSet.getString("userLogin"));
                agreement.setDamageReportId(resultSet.getInt("damageReportId"));
                agreement.setMonthlyCarPrice(resultSet.getInt("monthlyCarPrice"));
                agreement.setMonthsRented(resultSet.getInt("monthsRented"));
                agreement.setKmOverLimit(resultSet.getDouble("kmOverLimit"));
                agreement.setTotalPrice(resultSet.getDouble("totalPrice"));

                Date sqlStartDate = resultSet.getDate("startDate");
                if (sqlStartDate != null) {
                    agreement.setStartDate(sqlStartDate.toLocalDate());
                }

                Date sqlEndDate = resultSet.getDate("endDate");
                if (sqlEndDate != null) {
                    agreement.setEndDate(sqlEndDate.toLocalDate());
                }

                agreement.setActive(resultSet.getBoolean("active"));
                agreement.setAllowedKM(resultSet.getDouble("allowedKM"));

                if (resultSet.getInt("damageReportId") > 0) {
                    DamageReport damageReport = new DamageReport();
                    damageReport.setId(resultSet.getInt("damageReportId"));
                    damageReport.setRepairCost(resultSet.getDouble("repairCost"));
                    agreement.setDamageReport(damageReport);
                }

                return agreement;
            }
        } catch (SQLException e) {
            System.err.println("Error getting rental agreement: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Denne metode bruges til at indhente alle informationer fra rentalAgreement
    // Inklusiv de tilknyttede objekter som Car, User, Customer og DamageReport
    // Sofus og Gustav
    public RentalAgreement getActiveRentalAgreementById(int id) {
        String sql = """
        SELECT ra.id, ra.carId, ra.customerPhoneNumber, ra.userLogin, ra.damageReportId,
        ra.startDate, ra.endDate, ra.monthsRented, ra.active,
        ra.allowedKM, ra.kmOverLimit, ra.monthlyCarPrice, ra.totalPrice,
        
               c.vehicleNumber, c.model, c.monthlyFee, c.limited,c.co2Emission,
               dr.id as dr_id, dr.note as dr_note, dr.repairCost as dr_repairCost,
               cu.id as cu_id, cu.name as cu_name, cu.email as cu_email, cu.phoneNumber as cu_phoneNumber,
               u.userLogin, u.name as u_name
        FROM rentalAgreement ra
        JOIN car c ON ra.carId = c.vehicleNumber
        JOIN customer cu ON ra.customerPhoneNumber = cu.phoneNumber
        LEFT JOIN user u ON ra.userLogin = u.userLogin
        LEFT JOIN damageReport dr ON ra.damageReportId = dr.id
        WHERE ra.id = ? AND ra.active = TRUE
        LIMIT 1;
    """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                RentalAgreement agreement = new RentalAgreement();


                agreement.setId(resultSet.getInt("id"));
                agreement.setCarId(resultSet.getString("carId"));
                agreement.setCustomerPhoneNumber(resultSet.getInt("customerPhoneNumber"));
                agreement.setUserLogin(resultSet.getString("userLogin"));
                agreement.setDamageReportId(resultSet.getInt("damageReportId"));
                agreement.setStartDate(resultSet.getDate("startDate").toLocalDate());

                Date sqlEndDate = resultSet.getDate("endDate");
                if (sqlEndDate != null) {
                    agreement.setEndDate(sqlEndDate.toLocalDate());
                }

                // Neden for gemmes data for all objekterne og gemmes i en liste til rentalAgreement

                agreement.setActive(resultSet.getBoolean("active"));
                agreement.setAllowedKM(resultSet.getDouble("allowedKM"));
                agreement.setKmOverLimit(resultSet.getDouble("kmOverLimit"));
                agreement.setMonthlyCarPrice(resultSet.getInt("monthlyCarPrice"));
                agreement.setTotalPrice(resultSet.getDouble("totalPrice"));
                agreement.setMonthsRented(resultSet.getInt("monthsRented"));

                Car car = new Car();
                car.setVehicleNumber(resultSet.getString("vehicleNumber"));
                car.setModel(resultSet.getString("model"));
                car.setMonthlyFee(resultSet.getInt("monthlyFee"));
                car.setLimited(resultSet.getBoolean("limited"));
                car.setCo2Emission(resultSet.getDouble("co2Emission"));
                agreement.setCar(car);

                Customer customer = new Customer();
                customer.setId(resultSet.getInt("cu_id"));
                customer.setName(resultSet.getString("cu_name"));
                customer.setEmail(resultSet.getString("cu_email"));
                customer.setPhoneNumber(resultSet.getInt("cu_phoneNumber"));
                agreement.setCustomer(customer);

                User user = new User();
                user.setUserLogin(resultSet.getString("userLogin"));
                user.setName(resultSet.getString("u_name"));
                agreement.setUser(user);

                if (resultSet.getInt("dr_id") > 0) {
                    DamageReport damageReport = new DamageReport();
                    damageReport.setId(resultSet.getInt("dr_id"));
                    damageReport.setRepairCost(resultSet.getDouble("dr_repairCost"));
                    agreement.setDamageReport(damageReport);
                }

                return agreement;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Christoffer
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
}