package org.example.bilabonnement_gruppe1.repository;


import org.example.bilabonnement_gruppe1.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class CarRepository {

    @Autowired
    DataSource dataSource;

    public ArrayList<Car> showAvailableCars(String available) {
        String sql = "select * from car WHERE status=?";
        ArrayList<Car> carList = new ArrayList<>();


        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, available);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                Car car=new Car();
                car.setVehicleNumber(result.getString(1));
                car.setChassisnumber(result.getString(2));
                car.setModel(result.getString(3));
                car.setEquipment(result.getString(4));
                car.setKmDriven(result.getInt(5));
                car.setCo2Emission(result.getInt(6));
                car.setImage(result.getString(7));
                car.setStatus(result.getString(8));

                carList.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carList;
    }
}
