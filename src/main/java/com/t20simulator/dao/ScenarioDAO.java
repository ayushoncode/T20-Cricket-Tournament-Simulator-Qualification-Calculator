package com.t20simulator.dao;

import com.t20simulator.db.DBConnection;
import com.t20simulator.model.Scenario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores and retrieves saved qualification predictions.
 * Scenario rows are created whenever the user clicks Predict.
 */
public class ScenarioDAO {
    public void insert(Scenario scenario) throws SQLException {
        // Save prediction result so it can be reviewed later.
        String sql = "INSERT INTO SCENARIO (team_id, target_rank, required_runs, required_balls, projected_nrr, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, scenario.getTeamId());
            preparedStatement.setInt(2, scenario.getTargetRank());
            preparedStatement.setInt(3, scenario.getRequiredRuns());
            preparedStatement.setInt(4, scenario.getRequiredBalls());
            preparedStatement.setDouble(5, scenario.getProjectedNrr());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(scenario.getCreatedAt()));
            preparedStatement.executeUpdate();
        }
    }

    public List<Scenario> getByTeam(int teamId) throws SQLException {
        // Fetch previous prediction results for one team, newest first.
        String sql = "SELECT scenario_id, team_id, target_rank, required_runs, required_balls, projected_nrr, created_at "
                + "FROM SCENARIO WHERE team_id = ? ORDER BY created_at DESC";
        List<Scenario> scenarios = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, teamId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    scenarios.add(new Scenario(
                            resultSet.getInt("scenario_id"),
                            resultSet.getInt("team_id"),
                            resultSet.getInt("target_rank"),
                            resultSet.getInt("required_runs"),
                            resultSet.getInt("required_balls"),
                            resultSet.getDouble("projected_nrr"),
                            resultSet.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
            }
        }
        return scenarios;
    }
}
