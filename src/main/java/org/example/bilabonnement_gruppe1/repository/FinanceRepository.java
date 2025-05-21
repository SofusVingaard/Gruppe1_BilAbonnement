package org.example.bilabonnement_gruppe1.repository;

import org.example.bilabonnement_gruppe1.model.CarFinance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FinanceRepository {

    @Autowired
    private DataSource dataSource;

    public List<CarFinance> findAll() {
        List<CarFinance> reports = new ArrayList<>();
        String sql = "SELECT * FROM CarFinance";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                CarFinance report = new CarFinance();
                report.setId(rs.getInt("id"));
                report.setTotalPrice(rs.getDouble("totalPrice"));
                report.setDamageReportId(rs.getInt("damageReportId"));
                report.setDate(rs.getDate("date").toLocalDate());
                report.setCo2Emission(rs.getDouble("co2Emission"));
                report.setKmOverLimit(rs.getDouble("kmOverLimit"));
                report.setRentalFee(rs.getDouble("rentalFee"));
                reports.add(report);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }


    public void createFinanceReport(CarFinance report){
        String sql = "INSERT INTO carFinance (totalPrice, rentalAgreementId, damagePrice, co2Emission, kmOverLimit, date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";



        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, report.getTotalPrice());
            statement.setInt(2, report.getRentalAgreementId());
            statement.setDouble(3, report.getDamagePrice());
            statement.setDouble(4, report.getCo2Emission());
            statement.setDouble(5, report.getKmOverLimit());
            statement.setDate(6, java.sql.Date.valueOf(report.getDate()));


            statement.executeUpdate();


        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void markAsPaid(int id) {
        String sql = "UPDATE CarFinance SET paid = true WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
