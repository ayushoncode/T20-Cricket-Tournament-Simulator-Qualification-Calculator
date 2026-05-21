package com.t20simulator.service;

import com.t20simulator.dao.MatchDAO;
import com.t20simulator.dao.PointsTableDAO;
import com.t20simulator.dao.ScenarioDAO;
import com.t20simulator.dao.TeamDAO;
import com.t20simulator.model.PointsTable;
import com.t20simulator.model.Scenario;
import com.t20simulator.model.Team;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * Estimates whether a team can still finish inside a target rank.
 */
public class ScenarioPredictorService {
    private final LeaderboardService leaderboardService = new LeaderboardService();
    private final MatchDAO matchDAO = new MatchDAO();
    private final PointsTableDAO pointsTableDAO = new PointsTableDAO();
    private final ScenarioDAO scenarioDAO = new ScenarioDAO();
    private final TeamDAO teamDAO = new TeamDAO();

    public Scenario evaluateQualification(int teamId, int targetRank) throws SQLException {
        pointsTableDAO.createEntryIfAbsent(teamId);
        PointsTable teamEntry = pointsTableDAO.getByTeamId(teamId)
                .orElseThrow(() -> new SQLException("Points entry not found for team " + teamId));
        Team team = teamDAO.getById(teamId)
                .orElseThrow(() -> new SQLException("Team not found for id " + teamId));

        int remainingMatches = matchDAO.countRemainingMatchesForTeam(teamId);
        int maximumReachablePoints = teamEntry.getPoints() + (remainingMatches * 2);

        List<PointsTable> leaderboard = leaderboardService.getLeaderboard();
        leaderboard.sort(Comparator.comparingInt(PointsTable::getPoints).reversed()
                .thenComparing(Comparator.comparingDouble(PointsTable::getNrr).reversed()));

        int cutoffPoints = 0;
        if (!leaderboard.isEmpty()) {
            int index = Math.min(Math.max(targetRank - 1, 0), leaderboard.size() - 1);
            cutoffPoints = leaderboard.get(index).getPoints();
        }

        boolean qualifiesOnPoints = maximumReachablePoints >= cutoffPoints;
        int pointsGap = Math.max(0, cutoffPoints - teamEntry.getPoints());
        int requiredWins = (int) Math.ceil(pointsGap / 2.0);
        int requiredRuns = qualifiesOnPoints ? 0 : pointsGap * 15;
        int requiredBalls = qualifiesOnPoints ? 0 : pointsGap * 6;
        double projectedNrr = qualifiesOnPoints
                ? Math.max(teamEntry.getNrr(), cutoffPoints == 0 ? teamEntry.getNrr() : teamEntry.getNrr() + 0.25)
                : teamEntry.getNrr() + (requiredWins * 0.15);

        Scenario scenario = new Scenario();
        scenario.setTeamId(teamId);
        scenario.setTargetRank(targetRank);
        scenario.setRequiredRuns(requiredRuns);
        scenario.setRequiredBalls(requiredBalls);
        scenario.setProjectedNrr(projectedNrr);
        scenario.setCreatedAt(LocalDateTime.now());

        scenarioDAO.insert(scenario);
        return scenario;
    }

    public String buildScenarioSummary(Scenario scenario) throws SQLException {
        Team team = teamDAO.getById(scenario.getTeamId())
                .orElseThrow(() -> new SQLException("Team not found for scenario summary."));
        if (scenario.getRequiredRuns() == 0 && scenario.getRequiredBalls() == 0) {
            return team.getTeamName() + " can still qualify for rank " + scenario.getTargetRank()
                    + " because their maximum reachable points still match or exceed the current top-four cutoff. Projected NRR: "
                    + String.format("%.3f", scenario.getProjectedNrr());
        }
        return team.getTeamName() + " need a stronger finish to target rank " + scenario.getTargetRank()
                + ". Suggested cushion: +" + scenario.getRequiredRuns() + " runs in the remaining fixtures over "
                + scenario.getRequiredBalls() + " balls. Projected NRR: "
                + String.format("%.3f", scenario.getProjectedNrr());
    }
}
