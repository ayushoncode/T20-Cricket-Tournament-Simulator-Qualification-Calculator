package com.t20simulator.service;

import com.t20simulator.dao.TeamDAO;
import com.t20simulator.model.Team;

import java.sql.SQLException;
import java.util.List;

/**
 * Coordinates team-related actions for the Swing UI.
 * UI calls this class instead of directly calling TeamDAO.
 */
public class TeamService {
    private final TeamDAO teamDAO = new TeamDAO();

    public void addTeam(Team team) throws SQLException {
        // Forward validated Team object to DAO for INSERT query.
        teamDAO.insert(team);
    }

    public List<Team> getAllTeams() throws SQLException {
        // Used by dropdowns and qualification predictor.
        return teamDAO.getAll();
    }
}
