package com.t20simulator.service;

import com.t20simulator.dao.PointsTableDAO;
import com.t20simulator.model.PointsTable;

import java.sql.SQLException;
import java.util.List;

/**
 * Produces the ranked leaderboard for the points table tab.
 */
public class LeaderboardService {
    private final PointsTableDAO pointsTableDAO = new PointsTableDAO();

    public List<PointsTable> getLeaderboard() throws SQLException {
        List<PointsTable> leaderboard = pointsTableDAO.getLeaderboard();
        for (int index = 0; index < leaderboard.size(); index++) {
            leaderboard.get(index).setRank(index + 1);
        }
        return leaderboard;
    }
}
