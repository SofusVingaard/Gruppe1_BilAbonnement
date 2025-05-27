package org.example.bilabonnement_gruppe1.repository;


import org.example.bilabonnement_gruppe1.model.DamageReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

@Repository
public class DamageReportRepository {

    @Autowired
    private DataSource dataSource;

    public void createDamageReport(DamageReport damageReport) {
        String sql = "INSERT INTO damageReport (repairCost, note, rentalAgreementId) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setDouble(1, damageReport.getRepairCost());
            statement.setString(2, damageReport.getNote());
            statement.setInt(3, damageReport.getRentalAgreementID());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    damageReport.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getRepairCost(int damageReportId) {
        String sql = "SELECT repairCost FROM damageReport WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, damageReportId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("repairCost");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public void updateRepairCost(int damageReportId, double repairCost) {
        String sql = "UPDATE damageReport SET repairCost = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, repairCost);
            statement.setInt(2, damageReportId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DamageReport getDamageReportById(int damageReportId) {
        String sql = "SELECT * FROM damageReport WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, damageReportId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                DamageReport report = new DamageReport();
                report.setId(resultSet.getInt("id"));
                report.setRepairCost(resultSet.getDouble("repairCost"));
                report.setNote(resultSet.getString("note"));  // Now this will work
                report.setRentalAgreementID(resultSet.getInt("rentalAgreementId"));
                return report;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public double getTotalRepairCost(int damageReportId) {
        String sql = "SELECT SUM(price) AS total FROM damage WHERE damageReportId = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, damageReportId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}

