package com.t20simulator.model;

import java.time.LocalDate;

public class Match {
    private int matchId;
    private int team1Id;
    private int team2Id;
    private int venueId;
    private Integer winnerTeamId;
    private LocalDate matchDate;
    private String matchType;
    private String status;

    public Match() {
    }

    public Match(int matchId, int team1Id, int team2Id, int venueId, Integer winnerTeamId,
                 LocalDate matchDate, String matchType, String status) {
        this.matchId = matchId;
        this.team1Id = team1Id;
        this.team2Id = team2Id;
        this.venueId = venueId;
        this.winnerTeamId = winnerTeamId;
        this.matchDate = matchDate;
        this.matchType = matchType;
        this.status = status;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(int team1Id) {
        this.team1Id = team1Id;
    }

    public int getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(int team2Id) {
        this.team2Id = team2Id;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public Integer getWinnerTeamId() {
        return winnerTeamId;
    }

    public void setWinnerTeamId(Integer winnerTeamId) {
        this.winnerTeamId = winnerTeamId;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
