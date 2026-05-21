package com.t20simulator.model;

/**
 * Represents one row of the INNINGS table.
 * One completed cricket match has two Innings objects.
 */
public class Innings {
    // IDs link this innings to the match, batting team, and bowling team.
    private int inningsId;
    private int matchId;
    private int battingTeamId;
    private int bowlingTeamId;
    // Scorecard values used later for NRR calculation.
    private int runsScored;
    private int wicketsLost;
    private double oversPlayed;
    private boolean allOut;

    public Innings() {
    }

    public Innings(int inningsId, int matchId, int battingTeamId, int bowlingTeamId, int runsScored,
                   int wicketsLost, double oversPlayed, boolean allOut) {
        this.inningsId = inningsId;
        this.matchId = matchId;
        this.battingTeamId = battingTeamId;
        this.bowlingTeamId = bowlingTeamId;
        this.runsScored = runsScored;
        this.wicketsLost = wicketsLost;
        this.oversPlayed = oversPlayed;
        this.allOut = allOut;
    }

    public int getInningsId() {
        return inningsId;
    }

    public void setInningsId(int inningsId) {
        this.inningsId = inningsId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getBattingTeamId() {
        return battingTeamId;
    }

    public void setBattingTeamId(int battingTeamId) {
        this.battingTeamId = battingTeamId;
    }

    public int getBowlingTeamId() {
        return bowlingTeamId;
    }

    public void setBowlingTeamId(int bowlingTeamId) {
        this.bowlingTeamId = bowlingTeamId;
    }

    public int getRunsScored() {
        return runsScored;
    }

    public void setRunsScored(int runsScored) {
        this.runsScored = runsScored;
    }

    public int getWicketsLost() {
        return wicketsLost;
    }

    public void setWicketsLost(int wicketsLost) {
        this.wicketsLost = wicketsLost;
    }

    public double getOversPlayed() {
        return oversPlayed;
    }

    public void setOversPlayed(double oversPlayed) {
        this.oversPlayed = oversPlayed;
    }

    public boolean isAllOut() {
        return allOut;
    }

    public void setAllOut(boolean allOut) {
        this.allOut = allOut;
    }
}
