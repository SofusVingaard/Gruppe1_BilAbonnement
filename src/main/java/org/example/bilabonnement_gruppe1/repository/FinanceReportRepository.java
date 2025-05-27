package org.example.bilabonnement_gruppe1.repository;

import org.example.bilabonnement_gruppe1.model.DamageReport;
import org.example.bilabonnement_gruppe1.model.FinanceReport;
import org.example.bilabonnement_gruppe1.model.RentalAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FinanceReportRepository {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RentalAgreementRepository rentalAgreementRepository;

    @Autowired
    private DamageReportRepository damageReportRepository;

    public void createFinanceReport(FinanceReport financeReport) {
        String sql = "INSERT INTO financeReport (rentalAgreementId, monthlyPrice, kmOverLimitCost, " +
                "repairCost, damageCost, totalCost, paid, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, financeReport.getRentalAgreementId());
            statement.setDouble(2, financeReport.getMonthlyPrice());
            statement.setDouble(3, financeReport.getKmOverLimitCost());
            statement.setDouble(4, financeReport.getRepairCost());
            statement.setDouble(5, financeReport.getDamageCost());
            statement.setDouble(6, financeReport.getTotalCost());
            statement.setBoolean(7, financeReport.isPaid());
            statement.setString(8, financeReport.getStatus());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error creating finance report:");
            e.printStackTrace();
        }
    }

    public FinanceReport generateFinanceReport(int rentalAgreementId) {
        RentalAgreement agreement = rentalAgreementRepository.getRentalAgreement(rentalAgreementId);
        if (agreement == null) {
            System.err.println("No rental agreement found for ID: " + rentalAgreementId);
            return null;
        }
        double allowedKmPrice=0;

        if (agreement.getAllowedKM()==1750){
            allowedKmPrice=250;
        }
        if (agreement.getAllowedKM()==2000){
            allowedKmPrice=450;
        }


        double monthlyPrice = agreement.getMonthlyCarPrice()+allowedKmPrice;
        double kmOverLimitCost = agreement.getKmOverLimit() * 0.75; // 0.75 kr per km

        double repairCost = 0.0;
        if (agreement.getDamageReport() != null) {
            repairCost = agreement.getDamageReport().getRepairCost();
            System.out.println("Repair Cost from Damage Report: " + repairCost);
        }

        double totalCost = (monthlyPrice*agreement.getMonthsRented()) + kmOverLimitCost + repairCost;


        FinanceReport report = new FinanceReport();
        report.setRentalAgreementId(rentalAgreementId);
        report.setMonthlyPrice(monthlyPrice);
        report.setKmOverLimitCost(kmOverLimitCost);
        report.setRepairCost(repairCost);
        report.setTotalCost(totalCost);
        report.setPaid(false);
        report.setStatus("Unpaid");

        return report;
    }

    public List<FinanceReport> getAllFinanceReports() {
        List<FinanceReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM financeReport";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                FinanceReport report = new FinanceReport();
                report.setId(resultSet.getInt("id"));
                report.setRentalAgreementId(resultSet.getInt("rentalAgreementId"));
                report.setMonthlyPrice(resultSet.getDouble("monthlyPrice"));
                report.setKmOverLimitCost(resultSet.getDouble("kmOverLimitCost"));
                report.setRepairCost(resultSet.getDouble("repairCost"));
                report.setDamageCost(resultSet.getDouble("damageCost"));
                report.setTotalCost(resultSet.getDouble("totalCost"));
                report.setPaid(resultSet.getBoolean("paid"));

                Date paymentDate = resultSet.getDate("paymentDate");
                if (paymentDate != null) {
                    report.setPaymentDate(paymentDate.toLocalDate());
                }

                report.setStatus(resultSet.getString("status"));
                reports.add(report);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    public FinanceReport getFinanceReportByRentalAgreementId(int rentalAgreementId) {
        String sql = "SELECT * FROM financeReport WHERE rentalAgreementId = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, rentalAgreementId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                FinanceReport report = new FinanceReport();
                report.setId(resultSet.getInt("id"));
                report.setRentalAgreementId(resultSet.getInt("rentalAgreementId"));
                report.setMonthlyPrice(resultSet.getDouble("monthlyPrice"));
                report.setKmOverLimitCost(resultSet.getDouble("kmOverLimitCost"));
                report.setRepairCost(resultSet.getDouble("repairCost"));
                report.setDamageCost(resultSet.getDouble("damageCost"));
                report.setTotalCost(resultSet.getDouble("totalCost"));
                report.setPaid(resultSet.getBoolean("paid"));

                Date paymentDate = resultSet.getDate("paymentDate");
                if (paymentDate != null) {
                    report.setPaymentDate(paymentDate.toLocalDate());
                }

                report.setStatus(resultSet.getString("status"));
                return report;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updatePaymentStatus(int financeReportId, boolean paid, LocalDate paymentDate) {
        String updateFinanceSql = "UPDATE financeReport SET paid = ?, paymentDate = ?, status = ? WHERE id = ?";
        String getRentalAgreementSql = "SELECT rentalAgreementId FROM financeReport WHERE id = ?";
        String deactivateRentalAgreementSql = "UPDATE rentalAgreement SET active = false WHERE id = ?";
        String getCarIdSql = "SELECT carId FROM rentalAgreement WHERE id = ?";
        String makeCarAvailableSql = "UPDATE car SET status = 'available' WHERE vehicleNumber = ?";

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            // 1. Opdater financeReport
            try (PreparedStatement financeStmt = connection.prepareStatement(updateFinanceSql)) {
                financeStmt.setBoolean(1, paid);
                if (paymentDate != null) {
                    financeStmt.setDate(2, Date.valueOf(paymentDate));
                } else {
                    financeStmt.setNull(2, Types.DATE);
                }
                financeStmt.setString(3, paid ? "Paid" : "Unpaid");
                financeStmt.setInt(4, financeReportId);
                financeStmt.executeUpdate();
            }

            if (paid) {
                int rentalAgreementId = -1;
                String carId = null;

                // 2. Find rentalAgreementId
                try (PreparedStatement getRentalStmt = connection.prepareStatement(getRentalAgreementSql)) {
                    getRentalStmt.setInt(1, financeReportId);
                    try (ResultSet rs = getRentalStmt.executeQuery()) {
                        if (rs.next()) {
                            rentalAgreementId = rs.getInt("rentalAgreementId");
                        }
                    }
                }

                // 3. Sæt rentalAgreement til inaktiv
                if (rentalAgreementId != -1) {
                    try (PreparedStatement deactivateStmt = connection.prepareStatement(deactivateRentalAgreementSql)) {
                        deactivateStmt.setInt(1, rentalAgreementId);
                        deactivateStmt.executeUpdate();
                    }

                    // 4. Find carId fra rentalAgreement
                    try (PreparedStatement getCarStmt = connection.prepareStatement(getCarIdSql)) {
                        getCarStmt.setInt(1, rentalAgreementId);
                        try (ResultSet rs = getCarStmt.executeQuery()) {
                            if (rs.next()) {
                                carId = rs.getString("carId");
                            }
                        }
                    }

                    // 5. Sæt bil til 'Ledig'
                    if (carId != null) {
                        try (PreparedStatement makeCarAvailableStmt = connection.prepareStatement(makeCarAvailableSql)) {
                            makeCarAvailableStmt.setString(1, carId);
                            makeCarAvailableStmt.executeUpdate();
                        }
                    }
                }
            }

            connection.commit(); // Alt lykkedes
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
