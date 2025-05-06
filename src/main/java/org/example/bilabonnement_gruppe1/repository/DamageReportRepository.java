package org.example.bilabonnement_gruppe1.repository;


import org.example.bilabonnement_gruppe1.model.DamageReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class DamageReportRepository {

    @Autowired
    private DataSource dataSource;

    public void createDamageReport(DamageReport damageReport) {
        String sql ="INSERT INTO damageReport (userID, kmOverLimit) VALUES (?, ?)";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

        statement.setInt(1,damageReport.getUserID());
        statement.setDouble(2,damageReport.getKmOverLimit());
        statement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
