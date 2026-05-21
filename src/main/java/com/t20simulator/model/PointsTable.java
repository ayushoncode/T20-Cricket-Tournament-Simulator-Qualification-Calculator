package com.t20simulator.model;

public class PointsTable {
    private int entryId;
    private int teamId;
    private String teamName;
    private int played;
    private int won;
    private int lost;
    private int tied;
    private double nrr;
    private int points;
    private int rank;

    public PointsTable() {
    }

    public PointsTable(int entryId, int teamId, String teamName, int played, int won, int lost,
                       int tied, double nrr, int points) {
        this.entryId = entryId;
        this.teamId = teamId;
        this.teamName = teamName;
        this.played = played;
        this.won = won;
        this.lost = lost;
        this.tied = tied;
        this.nrr = nrr;
        this.points = points;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public int getTied() {
        return tied;
    }

    public void setTied(int tied) {
        this.tied = tied;
    }

    public double getNrr() {
        return nrr;
    }

    public void setNrr(double nrr) {
        this.nrr = nrr;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
