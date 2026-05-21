package com.t20simulator.service;

import com.t20simulator.dao.InningsDAO;
import com.t20simulator.dao.MatchDAO;
import com.t20simulator.model.Innings;
import com.t20simulator.model.Match;
import com.t20simulator.model.MatchSubmission;

import java.sql.SQLException;

/**
 * Validates and stores match submissions, then triggers points and NRR updates.
 * This is the central service called when the user presses Submit Match.
 */
public class MatchService {
    private final MatchDAO matchDAO = new MatchDAO();
    private final InningsDAO inningsDAO = new InningsDAO();
    private final PointsCalculatorService pointsCalculatorService = new PointsCalculatorService();
    private final NRRCalculatorService nrrCalculatorService = new NRRCalculatorService();

    public void submitMatch(MatchSubmission submission) throws SQLException {
        // Stop invalid cricket data before any database insert happens.
        validateSubmission(submission);

        Integer winnerTeamId = submission.getWinnerTeamId();
        String status = "completed";

        // Convert form submission into a Match model for MATCH_TABLE.
        Match match = new Match();
        match.setTeam1Id(submission.getTeam1Id());
        match.setTeam2Id(submission.getTeam2Id());
        match.setVenueId(submission.getVenueId());
        match.setWinnerTeamId(winnerTeamId);
        match.setMatchDate(submission.getMatchDate());
        match.setMatchType(submission.getMatchType());
        match.setStatus(status);

        // Insert match first so generated match_id can be used in innings rows.
        int matchId = matchDAO.insert(match);

        // Insert first innings: team1 bats, team2 bowls.
        inningsDAO.insert(new Innings(0, matchId, submission.getTeam1Id(), submission.getTeam2Id(),
                submission.getTeam1Runs(), submission.getTeam1Wickets(), submission.getTeam1Overs(), submission.getTeam1Wickets() == 10));
        // Insert second innings: team2 bats, team1 bowls.
        inningsDAO.insert(new Innings(0, matchId, submission.getTeam2Id(), submission.getTeam1Id(),
                submission.getTeam2Runs(), submission.getTeam2Wickets(), submission.getTeam2Overs(), submission.getTeam2Wickets() == 10));

        // After saving scorecard, update result, points table, and NRR.
        matchDAO.updateWinner(matchId, winnerTeamId, status);
        pointsCalculatorService.updatePointsForMatch(submission.getTeam1Id(), submission.getTeam2Id(), winnerTeamId);
        nrrCalculatorService.updateTeamNrr(submission.getTeam1Id());
        nrrCalculatorService.updateTeamNrr(submission.getTeam2Id());
    }

    private void validateSubmission(MatchSubmission submission) throws SQLException {
        // Same team cannot play against itself.
        if (submission.getTeam1Id() == submission.getTeam2Id()) {
            throw new SQLException("Team 1 and Team 2 must be different.");
        }
        if (submission.getMatchDate() == null) {
            throw new SQLException("Match date is required.");
        }
        // A T20 innings must have valid overs and wickets.
        if (submission.getTeam1Overs() <= 0 || submission.getTeam2Overs() <= 0) {
            throw new SQLException("Overs played must be greater than zero.");
        }
        if (submission.getTeam1Overs() > 20 || submission.getTeam2Overs() > 20) {
            throw new SQLException("Overs played cannot exceed 20 in a T20 match.");
        }
        if (submission.getTeam1Wickets() < 0 || submission.getTeam1Wickets() > 10
                || submission.getTeam2Wickets() < 0 || submission.getTeam2Wickets() > 10) {
            throw new SQLException("Wickets lost must be between 0 and 10.");
        }
        // Winner selection must match the actual scores entered by the user.
        if (submission.getWinnerTeamId() == null && submission.getTeam1Runs() != submission.getTeam2Runs()) {
            throw new SQLException("Select Tie only when both teams have the same runs.");
        }
        if (submission.getWinnerTeamId() != null
                && submission.getWinnerTeamId() == submission.getTeam1Id()
                && submission.getTeam1Runs() < submission.getTeam2Runs()) {
            throw new SQLException("Selected winner does not match the score entered.");
        }
        if (submission.getWinnerTeamId() != null
                && submission.getWinnerTeamId() == submission.getTeam2Id()
                && submission.getTeam2Runs() < submission.getTeam1Runs()) {
            throw new SQLException("Selected winner does not match the score entered.");
        }
    }
}
