package org.example.bilabonnement_gruppe1.repository;


import org.example.bilabonnement_gruppe1.model.Damage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class DamageRepository {

    @Autowired
    DataSource dataSource;

    public void createDamage(Damage damage){
        String sql="INSERT INTO damageReport (damageReportId, damageType, price) VALUES (?,?,?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, damage.getDamageReportId());
            statement.setString(2,damage.getDamageType());
            statement.setDouble(3, damage.getPrice());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
