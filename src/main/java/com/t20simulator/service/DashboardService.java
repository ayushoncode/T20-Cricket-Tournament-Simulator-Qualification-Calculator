package com.t20simulator.service;

import com.t20simulator.dao.MatchDAO;
import com.t20simulator.dao.TeamDAO;
import com.t20simulator.model.DashboardStats;

import java.sql.SQLException;

/**
 * Aggregates lightweight metrics for the dashboard tab.
 */
public class DashboardService {
    private final TeamDAO teamDAO = new TeamDAO();
    private final MatchDAO matchDAO = new MatchDAO();

    public DashboardStats getStats() throws SQLException {
        int totalTeams = teamDAO.countTeams();
        int matchesPlayed = matchDAO.countMatchesByStatus("completed");
        int matchesRemaining = matchDAO.countMatchesByStatus("scheduled");
        return new DashboardStats(totalTeams, matchesPlayed, matchesRemaining);
    }
}
