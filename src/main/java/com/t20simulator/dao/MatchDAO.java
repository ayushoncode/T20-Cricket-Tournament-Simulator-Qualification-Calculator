package com.t20simulator.dao;

import com.t20simulator.db.DBConnection;
import com.t20simulator.model.Match;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Performs CRUD-style database operations for match records.
 */
public class MatchDAO {
    public int insert(Match match) throws SQLException {
        String sql = "INSERT INTO MATCH_TABLE (team1_id, team2_id, venue_id, winner_team_id, match_date, match_type, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, match.getTeam1Id());
            preparedStatement.setInt(2, match.getTeam2Id());
            preparedStatement.setInt(3, match.getVenueId());
            if (match.getWinnerTeamId() == null) {
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(4, match.getWinnerTeamId());
            }
            preparedStatement.setDate(5, Date.valueOf(match.getMatchDate()));
            preparedStatement.setString(6, match.getMatchType());
            preparedStatement.setString(7, match.getStatus());
            preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert match record.");
    }

    public List<Match> getAll() throws SQLException {
        String sql = "SELECT match_id, team1_id, team2_id, venue_id, winner_team_id, match_date, match_type, status "
                + "FROM MATCH_TABLE ORDER BY match_date DESC, match_id DESC";
        List<Match> matches = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Integer winnerTeamId = resultSet.getObject("winner_team_id") == null
                        ? null : resultSet.getInt("winner_team_id");
                matches.add(new Match(
                        resultSet.getInt("match_id"),
                        resultSet.getInt("team1_id"),
                        resultSet.getInt("team2_id"),
                        resultSet.getInt("venue_id"),
                        winnerTeamId,
                        resultSet.getDate("match_date").toLocalDate(),
                        resultSet.getString("match_type"),
                        resultSet.getString("status")
                ));
            }
        }
        return matches;
    }

    public void updateWinner(int matchId, Integer winnerTeamId, String status) throws SQLException {
        String sql = "UPDATE MATCH_TABLE SET winner_team_id = ?, status = ? WHERE match_id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (winnerTeamId == null) {
                preparedStatement.setNull(1, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(1, winnerTeamId);
            }
            preparedStatement.setString(2, status);
            preparedStatement.setInt(3, matchId);
            preparedStatement.executeUpdate();
        }
    }

    public int countMatchesByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM MATCH_TABLE WHERE status = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, status);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1);
            }
        }
    }

    public int countRemainingMatchesForTeam(int teamId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM MATCH_TABLE WHERE status = ? AND (team1_id = ? OR team2_id = ?)";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "scheduled");
            preparedStatement.setInt(2, teamId);
            preparedStatement.setInt(3, teamId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1);
            }
        }
    }
}
