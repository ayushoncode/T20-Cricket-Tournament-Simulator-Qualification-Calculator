package com.t20simulator.dao;

import com.t20simulator.db.DBConnection;
import com.t20simulator.model.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Provides CRUD-oriented access to team data.
 */
public class TeamDAO {
    public void insert(Team team) throws SQLException {
        String sql = "INSERT INTO TEAM (team_name, group_name, captain, home_city) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, team.getTeamName());
            preparedStatement.setString(2, team.getGroupName());
            preparedStatement.setString(3, team.getCaptain());
            preparedStatement.setString(4, team.getHomeCity());
            preparedStatement.executeUpdate();
        }
    }

    public List<Team> getAll() throws SQLException {
        String sql = "SELECT team_id, team_name, group_name, captain, home_city FROM TEAM ORDER BY team_name";
        List<Team> teams = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                teams.add(mapRow(resultSet));
            }
        }
        return teams;
    }

    public Optional<Team> getById(int teamId) throws SQLException {
        String sql = "SELECT team_id, team_name, group_name, captain, home_city FROM TEAM WHERE team_id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, teamId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    public int countTeams() throws SQLException {
        String sql = "SELECT COUNT(*) FROM TEAM";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    private Team mapRow(ResultSet resultSet) throws SQLException {
        return new Team(
                resultSet.getInt("team_id"),
                resultSet.getString("team_name"),
                resultSet.getString("group_name"),
                resultSet.getString("captain"),
                resultSet.getString("home_city")
        );
    }
}
