package com.t20simulator.model;

import java.time.LocalDateTime;

/**
 * Represents one qualification prediction saved in SCENARIO table.
 */
public class Scenario {
    // Prediction identity and team being evaluated.
    private int scenarioId;
    private int teamId;
    private int targetRank;
    // Suggested extra performance needed in future matches.
    private int requiredRuns;
    private int requiredBalls;
    private double projectedNrr;
    private LocalDateTime createdAt;

    public Scenario() {
    }

    public Scenario(int scenarioId, int teamId, int targetRank, int requiredRuns, int requiredBalls,
                    double projectedNrr, LocalDateTime createdAt) {
        this.scenarioId = scenarioId;
        this.teamId = teamId;
        this.targetRank = targetRank;
        this.requiredRuns = requiredRuns;
        this.requiredBalls = requiredBalls;
        this.projectedNrr = projectedNrr;
        this.createdAt = createdAt;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getTargetRank() {
        return targetRank;
    }

    public void setTargetRank(int targetRank) {
        this.targetRank = targetRank;
    }

    public int getRequiredRuns() {
        return requiredRuns;
    }

    public void setRequiredRuns(int requiredRuns) {
        this.requiredRuns = requiredRuns;
    }

    public int getRequiredBalls() {
        return requiredBalls;
    }

    public void setRequiredBalls(int requiredBalls) {
        this.requiredBalls = requiredBalls;
    }

    public double getProjectedNrr() {
        return projectedNrr;
    }

    public void setProjectedNrr(double projectedNrr) {
        this.projectedNrr = projectedNrr;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
