package com.t20simulator.service;

import com.t20simulator.dao.PointsTableDAO;
import com.t20simulator.model.PointsTable;

import java.sql.SQLException;

/**
 * Applies win-loss-tie rules and updates points table rows.
 */
public class PointsCalculatorService {
    private final PointsTableDAO pointsTableDAO = new PointsTableDAO();

    public void updatePointsForMatch(int team1Id, int team2Id, Integer winnerTeamId) throws SQLException {
        pointsTableDAO.createEntryIfAbsent(team1Id);
        pointsTableDAO.createEntryIfAbsent(team2Id);

        PointsTable team1Entry = pointsTableDAO.getByTeamId(team1Id)
                .orElseThrow(() -> new SQLException("Points table entry missing for team " + team1Id));
        PointsTable team2Entry = pointsTableDAO.getByTeamId(team2Id)
                .orElseThrow(() -> new SQLException("Points table entry missing for team " + team2Id));

        team1Entry.setPlayed(team1Entry.getPlayed() + 1);
        team2Entry.setPlayed(team2Entry.getPlayed() + 1);

        if (winnerTeamId == null) {
            team1Entry.setTied(team1Entry.getTied() + 1);
            team2Entry.setTied(team2Entry.getTied() + 1);
            team1Entry.setPoints(team1Entry.getPoints() + 1);
            team2Entry.setPoints(team2Entry.getPoints() + 1);
        } else if (winnerTeamId == team1Id) {
            team1Entry.setWon(team1Entry.getWon() + 1);
            team2Entry.setLost(team2Entry.getLost() + 1);
            team1Entry.setPoints(team1Entry.getPoints() + 2);
        } else if (winnerTeamId == team2Id) {
            team2Entry.setWon(team2Entry.getWon() + 1);
            team1Entry.setLost(team1Entry.getLost() + 1);
            team2Entry.setPoints(team2Entry.getPoints() + 2);
        } else {
            throw new SQLException("Winner team id does not belong to the match.");
        }

        pointsTableDAO.update(team1Entry);
        pointsTableDAO.update(team2Entry);
    }
}
