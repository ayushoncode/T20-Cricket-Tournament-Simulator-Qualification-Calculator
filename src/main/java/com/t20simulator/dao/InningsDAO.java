package com.t20simulator.dao;

import com.t20simulator.db.DBConnection;
import com.t20simulator.model.Innings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence and retrieval of innings rows used by NRR calculations.
 */
public class InningsDAO {
    public void insert(Innings innings) throws SQLException {
        String sql = "INSERT INTO INNINGS (match_id, batting_team_id, bowling_team_id, runs_scored, wickets_lost, overs_played, all_out) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, innings.getMatchId());
            preparedStatement.setInt(2, innings.getBattingTeamId());
            preparedStatement.setInt(3, innings.getBowlingTeamId());
            preparedStatement.setInt(4, innings.getRunsScored());
            preparedStatement.setInt(5, innings.getWicketsLost());
            preparedStatement.setDouble(6, innings.getOversPlayed());
            preparedStatement.setBoolean(7, innings.isAllOut());
            preparedStatement.executeUpdate();
        }
    }

    public List<Innings> getByMatch(int matchId) throws SQLException {
        String sql = "SELECT innings_id, match_id, batting_team_id, bowling_team_id, runs_scored, wickets_lost, overs_played, all_out "
                + "FROM INNINGS WHERE match_id = ? ORDER BY innings_id";
        List<Innings> inningsList = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, matchId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    inningsList.add(new Innings(
                            resultSet.getInt("innings_id"),
                            resultSet.getInt("match_id"),
                            resultSet.getInt("batting_team_id"),
                            resultSet.getInt("bowling_team_id"),
                            resultSet.getInt("runs_scored"),
                            resultSet.getInt("wickets_lost"),
                            resultSet.getDouble("overs_played"),
                            resultSet.getBoolean("all_out")
                    ));
                }
            }
        }
        return inningsList;
    }

    public List<Innings> getByBattingTeam(int teamId) throws SQLException {
        String sql = "SELECT innings_id, match_id, batting_team_id, bowling_team_id, runs_scored, wickets_lost, overs_played, all_out "
                + "FROM INNINGS WHERE batting_team_id = ? ORDER BY innings_id";
        return getByTeamQuery(sql, teamId);
    }

    public List<Innings> getByBowlingTeam(int teamId) throws SQLException {
        String sql = "SELECT innings_id, match_id, batting_team_id, bowling_team_id, runs_scored, wickets_lost, overs_played, all_out "
                + "FROM INNINGS WHERE bowling_team_id = ? ORDER BY innings_id";
        return getByTeamQuery(sql, teamId);
    }

    private List<Innings> getByTeamQuery(String sql, int teamId) throws SQLException {
        List<Innings> inningsList = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, teamId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    inningsList.add(new Innings(
                            resultSet.getInt("innings_id"),
                            resultSet.getInt("match_id"),
                            resultSet.getInt("batting_team_id"),
                            resultSet.getInt("bowling_team_id"),
                            resultSet.getInt("runs_scored"),
                            resultSet.getInt("wickets_lost"),
                            resultSet.getDouble("overs_played"),
                            resultSet.getBoolean("all_out")
                    ));
                }
            }
        }
        return inningsList;
    }
}
