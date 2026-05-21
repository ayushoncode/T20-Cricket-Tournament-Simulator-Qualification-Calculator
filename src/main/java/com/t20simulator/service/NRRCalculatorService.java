package com.t20simulator.service;

import com.t20simulator.dao.InningsDAO;
import com.t20simulator.dao.PointsTableDAO;
import com.t20simulator.model.Innings;
import com.t20simulator.model.PointsTable;

import java.sql.SQLException;
import java.util.List;

/**
 * Recomputes NRR for a team from all batting and bowling innings records.
 * NRR = batting run rate - bowling/conceded run rate.
 */
public class NRRCalculatorService {
    private final PointsTableDAO pointsTableDAO = new PointsTableDAO();
    private final InningsDAO inningsDAO = new InningsDAO();

    public void updateTeamNrr(int teamId) throws SQLException {
        // Ensure team has points-table row where NRR can be stored.
        pointsTableDAO.createEntryIfAbsent(teamId);
        PointsTable entry = pointsTableDAO.getByTeamId(teamId)
                .orElseThrow(() -> new SQLException("Points table entry missing for team " + teamId));

        double runsScored = 0;
        double oversFaced = 0;
        double runsConceded = 0;
        double oversBowled = 0;

        // Sum all runs and overs while this team was batting.
        List<Innings> battingInnings = inningsDAO.getByBattingTeam(teamId);
        for (Innings innings : battingInnings) {
            runsScored += innings.getRunsScored();
            oversFaced += innings.getOversPlayed();
        }

        // Sum all runs and overs while this team was bowling.
        List<Innings> bowlingInnings = inningsDAO.getByBowlingTeam(teamId);
        for (Innings innings : bowlingInnings) {
            runsConceded += innings.getRunsScored();
            oversBowled += innings.getOversPlayed();
        }

        double projectedNrr = 0.0;
        if (oversFaced > 0 && oversBowled > 0) {
            // Net Run Rate formula.
            projectedNrr = (runsScored / oversFaced) - (runsConceded / oversBowled);
        }

        // Store recalculated NRR in POINTS_TABLE.
        entry.setNrr(projectedNrr);
        pointsTableDAO.update(entry);
    }
}
