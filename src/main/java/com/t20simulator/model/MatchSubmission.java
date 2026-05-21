package com.t20simulator.model;

import java.time.LocalDate;

/**
 * Carries all input values collected from the Swing match entry form.
 * It is not a database table directly; it is a helper object between UI and MatchService.
 */
public class MatchSubmission {
    // Match-level values selected from combo boxes.
    private final int team1Id;
    private final int team2Id;
    private final int venueId;
    private final Integer winnerTeamId;
    private final LocalDate matchDate;
    private final String matchType;
    // Scorecard values typed into the form.
    private final int team1Runs;
    private final int team1Wickets;
    private final double team1Overs;
    private final int team2Runs;
    private final int team2Wickets;
    private final double team2Overs;

    public MatchSubmission(int team1Id, int team2Id, int venueId, Integer winnerTeamId,
                           LocalDate matchDate, String matchType, int team1Runs, int team1Wickets,
                           double team1Overs, int team2Runs, int team2Wickets, double team2Overs) {
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.venueId = venueId;
        this.winnerTeamId = winnerTeamId;
        this.matchDate = matchDate;
        this.matchType = matchType;
        this.team1Runs = team1Runs;
        this.team1Wickets = team1Wickets;
        this.team1Overs = team1Overs;
        this.team2Runs = team2Runs;
        this.team2Wickets = team2Wickets;
        this.team2Overs = team2Overs;
    }

    public int getTeam1Id() {
        return team1Id;
    }

    public int getTeam2Id() {
        return team2Id;
    }

    public int getVenueId() {
        return venueId;
    }

    public Integer getWinnerTeamId() {
        return winnerTeamId;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public String getMatchType() {
        return matchType;
    }

    public int getTeam1Runs() {
        return team1Runs;
    }

    public int getTeam1Wickets() {
        return team1Wickets;
    }

    public double getTeam1Overs() {
        return team1Overs;
    }

    public int getTeam2Runs() {
        return team2Runs;
    }

    public int getTeam2Wickets() {
        return team2Wickets;
    }

    public double getTeam2Overs() {
        return team2Overs;
    }
}
