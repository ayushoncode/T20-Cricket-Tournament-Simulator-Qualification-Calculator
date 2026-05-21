package com.t20simulator.model;

/**
 * Simple read-only model used to move dashboard count values from service to UI.
 */
public class DashboardStats {
    // Values shown on Dashboard stat cards.
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
