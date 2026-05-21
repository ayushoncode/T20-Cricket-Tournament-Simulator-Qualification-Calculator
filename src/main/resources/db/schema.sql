CREATE DATABASE IF NOT EXISTS t20_tournament;
USE t20_tournament;

CREATE TABLE IF NOT EXISTS TEAM (
  team_id INT PRIMARY KEY AUTO_INCREMENT,
  team_name VARCHAR(100),
  group_name VARCHAR(50),
  captain VARCHAR(100),
  home_city VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS VENUE (
  venue_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  city VARCHAR(100),
  capacity INT
);

CREATE TABLE IF NOT EXISTS MATCH_TABLE (
  match_id INT PRIMARY KEY AUTO_INCREMENT,
  team1_id INT,
  team2_id INT,
  venue_id INT,
  winner_team_id INT,
  match_date DATE,
  match_type VARCHAR(50),
  status VARCHAR(50),
  FOREIGN KEY (team1_id) REFERENCES TEAM(team_id),
  FOREIGN KEY (team2_id) REFERENCES TEAM(team_id),
  FOREIGN KEY (venue_id) REFERENCES VENUE(venue_id),
  FOREIGN KEY (winner_team_id) REFERENCES TEAM(team_id)
);

CREATE TABLE IF NOT EXISTS INNINGS (
  innings_id INT PRIMARY KEY AUTO_INCREMENT,
  match_id INT,
  batting_team_id INT,
  bowling_team_id INT,
  runs_scored INT,
  wickets_lost INT,
  overs_played FLOAT,
  all_out BOOLEAN,
  FOREIGN KEY (match_id) REFERENCES MATCH_TABLE(match_id),
  FOREIGN KEY (batting_team_id) REFERENCES TEAM(team_id),
  FOREIGN KEY (bowling_team_id) REFERENCES TEAM(team_id)
);

CREATE TABLE IF NOT EXISTS POINTS_TABLE (
  entry_id INT PRIMARY KEY AUTO_INCREMENT,
  team_id INT UNIQUE,
  played INT DEFAULT 0,
  won INT DEFAULT 0,
  lost INT DEFAULT 0,
  tied INT DEFAULT 0,
  nrr FLOAT DEFAULT 0.0,
  points INT DEFAULT 0,
  FOREIGN KEY (team_id) REFERENCES TEAM(team_id)
);

CREATE TABLE IF NOT EXISTS SCENARIO (
  scenario_id INT PRIMARY KEY AUTO_INCREMENT,
  team_id INT,
  target_rank INT,
  required_runs INT,
  required_balls INT,
  projected_nrr FLOAT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (team_id) REFERENCES TEAM(team_id)
);
