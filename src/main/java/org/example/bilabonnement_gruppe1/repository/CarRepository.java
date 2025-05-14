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
                car.setKmDriven(result.getDouble(5));
                car.setCo2Emission(result.getDouble(6));
                car.setImage(result.getString(7));
                car.setStatus(result.getString(8));
                car.setLimited(result.getBoolean(9));
                car.setMonthlyFee(result.getInt(10));

                carList.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carList;
    }

    public void createCar(Car car) {
        String sql = "INSERT INTO car (vehicleNumber, chassisnumber, model, equipment, kmDriven, co2Emission, image, status, limited, monthlyFee) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, car.getVehicleNumber());
            stmt.setString(2, car.getChassisnumber());
            stmt.setString(3, car.getModel());
            stmt.setString(4, car.getEquipment());
            stmt.setDouble(5, car.getKmDriven());
            stmt.setDouble(6, car.getCo2Emission());
            stmt.setString(7, car.getImage());
            stmt.setString(8, car.getStatus());
            stmt.setBoolean(9,car.isLimited());
            stmt.setInt(10, car.getMonthlyFee());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countAvailableCars() {
        String sql = "SELECT COUNT(*) FROM car WHERE status = 'available'";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            var result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}


