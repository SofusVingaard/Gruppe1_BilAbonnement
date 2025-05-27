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


        double monthlyPrice = agreement.getMonthlyCarPrice() * agreement.getMonthsRented();
        double kmOverLimitCost = agreement.getKmOverLimit() * 0.75; // 0.75 kr per km

        double repairCost = 0.0;
        if (agreement.getDamageReport() != null) {
            repairCost = agreement.getDamageReport().getRepairCost();
            System.out.println("Repair Cost from Damage Report: " + repairCost);
        }

        double totalCost = monthlyPrice + kmOverLimitCost + repairCost;


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
        String sql = "UPDATE financeReport SET paid = ?, paymentDate = ?, status = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, paid);

            if (paymentDate != null) {
                statement.setDate(2, Date.valueOf(paymentDate));
            } else {
                statement.setNull(2, Types.DATE);
            }

            statement.setString(3, paid ? "Paid" : "Unpaid");
            statement.setInt(4, financeReportId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
