package com.t20simulator.model;

public class DashboardStats {
    private final int totalTeams;
    private final int matchesPlayed;
    private final int matchesRemaining;

    public DashboardStats(int totalTeams, int matchesPlayed, int matchesRemaining) {
        this.totalTeams = totalTeams;
        this.matchesPlayed = matchesPlayed;
        this.matchesRemaining = matchesRemaining;
    }

    public int getTotalTeams() {
        return totalTeams;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getMatchesRemaining() {
        return matchesRemaining;
    }
}
