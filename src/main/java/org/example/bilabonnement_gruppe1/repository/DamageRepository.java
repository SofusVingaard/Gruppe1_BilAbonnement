package org.example.bilabonnement_gruppe1.repository;


import org.example.bilabonnement_gruppe1.model.Damage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class DamageRepository {

    @Autowired
    DataSource dataSource;

    public void createDamage(Damage damage) {
        String sql = "INSERT INTO damage (damageReportId, damageType, price) VALUES (?,?,?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, damage.getDamageReportId());
            statement.setString(2, damage.getDamageType());
            statement.setDouble(3, damage.getPrice());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public double getRepairCost(int damageReportId) {
        String sql = "SELECT price FROM damage WHERE damageReportId = ?";
        Damage damage = null;
        double totalPrice = 0;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, damage.getDamageReportId());

            statement.executeUpdate();

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    totalPrice += resultSet.getDouble("price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalPrice;
    }
}
