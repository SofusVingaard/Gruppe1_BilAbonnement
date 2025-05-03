package org.example.bilabonnement_gruppe1.repository;

import org.example.bilabonnement_gruppe1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class UserRepository {

    @Autowired
    private DataSource dataSource;

    public void createUser(User user) {
        String sql = "INSERT INTO user (userLogin, name, password) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUserLogin());
            statement.setString(2, user.getName());
            statement.setString(3, user.getPassword());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
