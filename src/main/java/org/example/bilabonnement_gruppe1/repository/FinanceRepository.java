package org.example.bilabonnement_gruppe1.repository;

import org.example.bilabonnement_gruppe1.model.CarFinance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

@Repository
public class FinanceRepository {

    @Autowired
    private DataSource dataSource;

    public void createFinanceReport(CarFinance report){
        String sql = "INSERT INTO CarFinance (totalPrice, damageReportId, date, co2Emission, kmOverLimit, rentalFee)" +
                "VALUES(?,?,?,?,?,?)";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setDouble(1, report.getTotalPrice());
        statement.setInt(2, report.getDamageReportId());
        statement.setDate(3, java.sql.Date.valueOf(report.getDate()));
        statement.setDouble(4, report.getCo2Emissinon());
        statement.setDouble(5, report.getKmOverLimit());
        statement.setDouble(6, report.getRentalFee());

        statement.executeUpdate();


        } catch (SQLException e){
            System.err.println("Fejl ved oprettelse af finans rapport; " + e.getMessage());
            e.printStackTrace();
        }
    }




}
