package org.example.bilabonnement_gruppe1.repository;


import org.example.bilabonnement_gruppe1.model.Damage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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


    // Sofus
    public void createDamage(Damage damage) {
        String sql = "INSERT INTO damage (damageReportId, damageType, price) VALUES (?,?,?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, damage.getDamageReportId());
            statement.setString(2, damage.getDamageType());
            statement.setDouble(3, damage.getPrice());

            statement.executeUpdate();

            updateDamageReportRepairCost(damage.getDamageReportId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sofus
    public double getRepairCost(int damageReportId) {
        String sql = "SELECT price FROM damage WHERE damageReportId = ?";
        double totalPrice = 0;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, damageReportId);

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

    // Sofus
    public ArrayList<Damage> getDamageList(int damageReportId) {
        String sql = "SELECT id, damageType, price FROM damage WHERE damageReportId = ?";
        ArrayList<Damage> damageList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, damageReportId);

            ResultSet result = statement.executeQuery();

            while (result.next()){
                Damage damage = new Damage();
                damage.setId(result.getInt("id"));
                damage.setDamageType(result.getString("damageType"));
                damage.setPrice(result.getDouble("price"));
                damageList.add(damage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return damageList;
    }

    // Sofus
    public void deleteDamage(int damageId) {
        String getReportIdSql = "SELECT damageReportId FROM damage WHERE id = ?";
        String deleteSql = "DELETE FROM damage WHERE id = ?";

        int damageReportId = -1;

        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement getStmt = connection.prepareStatement(getReportIdSql)) {
                getStmt.setInt(1, damageId);
                try (ResultSet rs = getStmt.executeQuery()) {
                    if (rs.next()) {
                        damageReportId = rs.getInt("damageReportId");
                    }
                }
            }

            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, damageId);
                deleteStmt.executeUpdate();
            }

            if (damageReportId != -1) {
                updateDamageReportRepairCost(damageReportId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sofus
    public void updateDamageReportRepairCost(int damageReportId) {
        String sql = "UPDATE damageReport SET repairCost = ? WHERE id = ?";
        double totalRepairCost = getRepairCost(damageReportId);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setDouble(1, totalRepairCost);
            statement.setInt(2, damageReportId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
