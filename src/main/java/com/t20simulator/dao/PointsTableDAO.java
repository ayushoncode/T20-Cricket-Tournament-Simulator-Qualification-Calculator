package com.t20simulator.dao;

import com.t20simulator.db.DBConnection;
import com.t20simulator.model.PointsTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Reads and updates the leaderboard table without embedding business rules.
 */
public class PointsTableDAO {
    public void createEntryIfAbsent(int teamId) throws SQLException {
        String sql = "INSERT INTO POINTS_TABLE (team_id, played, won, lost, tied, nrr, points) "
                + "SELECT ?, 0, 0, 0, 0, 0.0, 0 WHERE NOT EXISTS "
                + "(SELECT 1 FROM POINTS_TABLE WHERE team_id = ?)";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, teamId);
            preparedStatement.setInt(2, teamId);
            preparedStatement.executeUpdate();
        }
    }

    public Optional<PointsTable> getByTeamId(int teamId) throws SQLException {
        String sql = "SELECT entry_id, team_id, played, won, lost, tied, nrr, points FROM POINTS_TABLE WHERE team_id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, teamId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    PointsTable pointsTable = new PointsTable();
                    pointsTable.setEntryId(resultSet.getInt("entry_id"));
                    pointsTable.setTeamId(resultSet.getInt("team_id"));
                    pointsTable.setPlayed(resultSet.getInt("played"));
                    pointsTable.setWon(resultSet.getInt("won"));
                    pointsTable.setLost(resultSet.getInt("lost"));
                    pointsTable.setTied(resultSet.getInt("tied"));
                    pointsTable.setNrr(resultSet.getDouble("nrr"));
                    pointsTable.setPoints(resultSet.getInt("points"));
                    return Optional.of(pointsTable);
                }
            }
        }
        return Optional.empty();
    }

    public void update(PointsTable pointsTable) throws SQLException {
        String sql = "UPDATE POINTS_TABLE SET played = ?, won = ?, lost = ?, tied = ?, nrr = ?, points = ? WHERE team_id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, pointsTable.getPlayed());
            preparedStatement.setInt(2, pointsTable.getWon());
            preparedStatement.setInt(3, pointsTable.getLost());
            preparedStatement.setInt(4, pointsTable.getTied());
            preparedStatement.setDouble(5, pointsTable.getNrr());
            preparedStatement.setInt(6, pointsTable.getPoints());
            preparedStatement.setInt(7, pointsTable.getTeamId());
            preparedStatement.executeUpdate();
        }
    }

    public List<PointsTable> getLeaderboard() throws SQLException {
        String sql = "SELECT pt.entry_id, pt.team_id, t.team_name, pt.played, pt.won, pt.lost, pt.tied, pt.nrr, pt.points "
                + "FROM POINTS_TABLE pt JOIN TEAM t ON pt.team_id = t.team_id "
                + "ORDER BY pt.points DESC, pt.nrr DESC, t.team_name ASC";
        List<PointsTable> leaderboard = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                leaderboard.add(new PointsTable(
                        resultSet.getInt("entry_id"),
                        resultSet.getInt("team_id"),
                        resultSet.getString("team_name"),
                        resultSet.getInt("played"),
                        resultSet.getInt("won"),
                        resultSet.getInt("lost"),
                        resultSet.getInt("tied"),
                        resultSet.getDouble("nrr"),
                        resultSet.getInt("points")
                ));
            }
        }
        return leaderboard;
    }
}
