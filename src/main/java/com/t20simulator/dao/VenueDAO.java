package com.t20simulator.dao;

import com.t20simulator.db.DBConnection;
import com.t20simulator.model.Venue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides CRUD-oriented access to venue data.
 */
public class VenueDAO {
    public void insert(Venue venue) throws SQLException {
        String sql = "INSERT INTO VENUE (name, city, capacity) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, venue.getName());
            preparedStatement.setString(2, venue.getCity());
            preparedStatement.setInt(3, venue.getCapacity());
            preparedStatement.executeUpdate();
        }
    }

    public List<Venue> getAll() throws SQLException {
        String sql = "SELECT venue_id, name, city, capacity FROM VENUE ORDER BY name";
        List<Venue> venues = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                venues.add(new Venue(
                        resultSet.getInt("venue_id"),
                        resultSet.getString("name"),
                        resultSet.getString("city"),
                        resultSet.getInt("capacity")
                ));
            }
        }
        return venues;
    }
}
