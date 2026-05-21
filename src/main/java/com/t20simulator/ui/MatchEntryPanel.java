package com.t20simulator.ui;

import com.t20simulator.model.MatchSubmission;
import com.t20simulator.model.Team;
import com.t20simulator.model.Venue;
import com.t20simulator.service.MatchService;
import com.t20simulator.service.TeamService;
import com.t20simulator.service.VenueService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Match entry tab that inserts a match and both innings, then updates points and NRR.
 */
public class MatchEntryPanel extends JPanel {
    private final JComboBox<Team> team1ComboBox = new JComboBox<>();
    private final JComboBox<Team> team2ComboBox = new JComboBox<>();
    private final JComboBox<Venue> venueComboBox = new JComboBox<>();
    private final JComboBox<String> winnerComboBox = new JComboBox<>(new String[]{"Team 1", "Team 2", "Tie"});
    private final JTextField team1RunsField = new JTextField();
    private final JTextField team1WicketsField = new JTextField();
    private final JTextField team1OversField = new JTextField();
    private final JTextField team2RunsField = new JTextField();
    private final JTextField team2WicketsField = new JTextField();
    private final JTextField team2OversField = new JTextField();
    private final JLabel messageLabel = new JLabel("Enter a result and submit the match.");

    private final TeamService teamService = new TeamService();
    private final VenueService venueService = new VenueService();
    private final MatchService matchService = new MatchService();
    private final Runnable onMatchSaved;

    public MatchEntryPanel(Runnable onMatchSaved) {
        this.onMatchSaved = onMatchSaved;
        UITheme.stylePanel(this);
        setLayout(new BorderLayout(22, 22));
        setBorder(BorderFactory.createEmptyBorder(26, 28, 28, 28));

        styleInput(team1ComboBox);
        styleInput(team2ComboBox);
        styleInput(venueComboBox);
        styleInput(winnerComboBox);
        UITheme.styleField(team1RunsField);
        UITheme.styleField(team1WicketsField);
        UITheme.styleField(team1OversField);
        UITheme.styleField(team2RunsField);
        UITheme.styleField(team2WicketsField);
        UITheme.styleField(team2OversField);

        JLabel titleLabel = UITheme.createTitleLabel("Match Entry", 30f);
        JLabel subtitleLabel = UITheme.createSecondaryLabel("Record a completed match", 14f);

        JPanel container = UITheme.createPlainPanel();
        container.setLayout(new BorderLayout(0, 18));
        JPanel headerPanel = UITheme.createPlainPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        headerPanel.add(subtitleLabel);
        container.add(headerPanel, BorderLayout.NORTH);

        JPanel formCard = UITheme.createCardPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));

        JPanel topFields = new JPanel(new GridLayout(4, 2, 14, 14));
        topFields.setOpaque(false);
        topFields.add(UITheme.createSecondaryLabel("Team 1", 13f));
        topFields.add(team1ComboBox);
        topFields.add(UITheme.createSecondaryLabel("Team 2", 13f));
        topFields.add(team2ComboBox);
        topFields.add(UITheme.createSecondaryLabel("Venue", 13f));
        topFields.add(venueComboBox);
        topFields.add(UITheme.createSecondaryLabel("Winner", 13f));
        topFields.add(winnerComboBox);

        JLabel team1Header = UITheme.createSecondaryLabel("Team 1 Innings", 14f);
        team1Header.setForeground(UITheme.HIGHLIGHT);
        team1Header.setFont(UITheme.BASE_FONT.deriveFont(Font.BOLD, 14f));

        JPanel team1Panel = new JPanel(new GridLayout(3, 2, 14, 14));
        team1Panel.setOpaque(false);
        team1Panel.add(UITheme.createSecondaryLabel("Runs", 13f));
        team1Panel.add(team1RunsField);
        team1Panel.add(UITheme.createSecondaryLabel("Wickets", 13f));
        team1Panel.add(team1WicketsField);
        team1Panel.add(UITheme.createSecondaryLabel("Overs", 13f));
        team1Panel.add(team1OversField);

        JLabel team2Header = UITheme.createSecondaryLabel("Team 2 Innings", 14f);
        team2Header.setForeground(UITheme.HIGHLIGHT);
        team2Header.setFont(UITheme.BASE_FONT.deriveFont(Font.BOLD, 14f));

        JPanel team2Panel = new JPanel(new GridLayout(3, 2, 14, 14));
        team2Panel.setOpaque(false);
        team2Panel.add(UITheme.createSecondaryLabel("Runs", 13f));
        team2Panel.add(team2RunsField);
        team2Panel.add(UITheme.createSecondaryLabel("Wickets", 13f));
        team2Panel.add(team2WicketsField);
        team2Panel.add(UITheme.createSecondaryLabel("Overs", 13f));
        team2Panel.add(team2OversField);

        JPanel inningsPanel = new JPanel(new GridLayout(1, 2, 18, 0));
        inningsPanel.setOpaque(false);

        JPanel team1Section = new JPanel();
        team1Section.setOpaque(false);
        team1Section.setLayout(new BoxLayout(team1Section, BoxLayout.Y_AXIS));
        team1Section.add(team1Header);
        team1Section.add(Box.createRigidArea(new Dimension(0, 10)));
        team1Section.add(team1Panel);

        JPanel team2Section = new JPanel();
        team2Section.setOpaque(false);
        team2Section.setLayout(new BoxLayout(team2Section, BoxLayout.Y_AXIS));
        team2Section.add(team2Header);
        team2Section.add(Box.createRigidArea(new Dimension(0, 10)));
        team2Section.add(team2Panel);

        inningsPanel.add(team1Section);
        inningsPanel.add(team2Section);

        JButton submitButton = new JButton("Submit Match");
        UITheme.styleButton(submitButton, UITheme.HIGHLIGHT, UITheme.HIGHLIGHT_HOVER, UITheme.TEXT_PRIMARY);
        submitButton.setPreferredSize(new Dimension(180, 44));
        submitButton.addActionListener(event -> submitMatch());

        messageLabel.setFont(UITheme.BASE_FONT.deriveFont(Font.BOLD, 13f));
        messageLabel.setForeground(UITheme.SUCCESS);
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        formCard.add(topFields);
        formCard.add(Box.createRigidArea(new Dimension(0, 24)));
        formCard.add(inningsPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 24)));
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        footerPanel.setOpaque(false);
        footerPanel.add(submitButton);
        formCard.add(footerPanel);
        formCard.add(Box.createRigidArea(new Dimension(0, 16)));
        formCard.add(messageLabel);

        container.add(formCard, BorderLayout.CENTER);
        add(container, BorderLayout.CENTER);
        refreshData();
    }

    public void refreshData() {
        try {
            populateTeams(teamService.getAllTeams());
            populateVenues(venueService.getAllVenues());
        } catch (SQLException exception) {
            messageLabel.setForeground(UITheme.HIGHLIGHT);
            messageLabel.setText("Error loading dropdowns: " + exception.getMessage());
        }
    }

    private void populateTeams(List<Team> teams) {
        team1ComboBox.removeAllItems();
        team2ComboBox.removeAllItems();
        for (Team team : teams) {
            team1ComboBox.addItem(team);
            team2ComboBox.addItem(team);
        }
    }

    private void populateVenues(List<Venue> venues) {
        venueComboBox.removeAllItems();
        for (Venue venue : venues) {
            venueComboBox.addItem(venue);
        }
    }

    private void submitMatch() {
        try {
            Team team1 = (Team) team1ComboBox.getSelectedItem();
            Team team2 = (Team) team2ComboBox.getSelectedItem();
            Venue venue = (Venue) venueComboBox.getSelectedItem();
            if (team1 == null || team2 == null || venue == null) {
                throw new SQLException("Please add teams and venues before entering a match.");
            }

            Integer winnerTeamId = resolveWinner(team1, team2);
            MatchSubmission submission = new MatchSubmission(
                    team1.getTeamId(),
                    team2.getTeamId(),
                    venue.getVenueId(),
                    winnerTeamId,
                    LocalDate.now(),
                    "GROUP",
                    Integer.parseInt(team1RunsField.getText().trim()),
                    Integer.parseInt(team1WicketsField.getText().trim()),
                    Double.parseDouble(team1OversField.getText().trim()),
                    Integer.parseInt(team2RunsField.getText().trim()),
                    Integer.parseInt(team2WicketsField.getText().trim()),
                    Double.parseDouble(team2OversField.getText().trim())
            );

            matchService.submitMatch(submission);
            messageLabel.setForeground(UITheme.SUCCESS);
            messageLabel.setText("Match saved successfully.");
            clearForm();
            onMatchSaved.run();
        } catch (NumberFormatException exception) {
            messageLabel.setForeground(UITheme.HIGHLIGHT);
            messageLabel.setText("Enter valid numeric values for runs, wickets, and overs.");
        } catch (SQLException exception) {
            messageLabel.setForeground(UITheme.HIGHLIGHT);
            messageLabel.setText("Error: " + exception.getMessage());
        }
    }

    private Integer resolveWinner(Team team1, Team team2) {
        String winnerSelection = (String) winnerComboBox.getSelectedItem();
        if ("Team 1".equals(winnerSelection)) {
            return team1.getTeamId();
        }
        if ("Team 2".equals(winnerSelection)) {
            return team2.getTeamId();
        }
        return null;
    }

    private void clearForm() {
        team1RunsField.setText("");
        team1WicketsField.setText("");
        team1OversField.setText("");
        team2RunsField.setText("");
        team2WicketsField.setText("");
        team2OversField.setText("");
        winnerComboBox.setSelectedIndex(0);
    }

    private void styleInput(JComboBox<?> comboBox) {
        UITheme.styleField(comboBox);
    }
}
