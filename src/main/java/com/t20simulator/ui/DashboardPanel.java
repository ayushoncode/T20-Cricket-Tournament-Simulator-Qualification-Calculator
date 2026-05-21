package com.t20simulator.ui;

import com.t20simulator.model.DashboardStats;
import com.t20simulator.model.Team;
import com.t20simulator.model.Venue;
import com.t20simulator.service.DashboardService;
import com.t20simulator.service.TeamService;
import com.t20simulator.service.VenueService;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

/**
 * Dashboard tab that shows tournament totals and lets the user add teams and venues.
 * It is the first screen the user sees after the application opens.
 */
public class DashboardPanel extends JPanel {
    // These labels are updated whenever refreshData() reads latest counts from the database.
    private final JLabel totalTeamsValue = new JLabel("0");
    private final JLabel matchesPlayedValue = new JLabel("0");
    private final JLabel matchesRemainingValue = new JLabel("0");

    // Services hide database details from the UI layer.
    private final DashboardService dashboardService = new DashboardService();
    private final TeamService teamService = new TeamService();
    private final VenueService venueService = new VenueService();
    // Callback supplied by MainFrame; used to refresh other panels after inserting data.
    private final Runnable onDataChanged;

    public DashboardPanel(Runnable onDataChanged) {
        this.onDataChanged = onDataChanged;
        UITheme.stylePanel(this);
        setLayout(new BorderLayout(22, 22));
        setBorder(BorderFactory.createEmptyBorder(26, 28, 28, 28));

        // Top cards show live tournament summary values.
        JPanel statsPanel = UITheme.createPlainPanel();
        statsPanel.setLayout(new GridLayout(1, 3, 16, 16));
        statsPanel.add(createStatCard("Total Teams", totalTeamsValue));
        statsPanel.add(createStatCard("Matches Played", matchesPlayedValue));
        statsPanel.add(createStatCard("Matches Remaining", matchesRemainingValue));

        JPanel buttonPanel = UITheme.createCardPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));
        JButton addTeamButton = new JButton("Add Team");
        JButton addVenueButton = new JButton("Add Venue");
        JButton refreshButton = new JButton("Refresh");
        UITheme.styleButton(addTeamButton, UITheme.HIGHLIGHT, UITheme.HIGHLIGHT_HOVER, UITheme.TEXT_PRIMARY);
        UITheme.styleButton(addVenueButton, UITheme.ACCENT, new java.awt.Color(0x0F3028), UITheme.TEXT_PRIMARY);
        UITheme.styleButton(refreshButton, UITheme.SURFACE, UITheme.ACCENT_SOFT, UITheme.TEXT_PRIMARY);
        refreshButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                BorderFactory.createEmptyBorder(9, 18, 9, 18)));

        // Button actions open input dialogs or reload stats from the database.
        addTeamButton.addActionListener(event -> openAddTeamDialog());
        addVenueButton.addActionListener(event -> openAddVenueDialog());
        refreshButton.addActionListener(event -> refreshData());

        buttonPanel.add(addTeamButton);
        buttonPanel.add(addVenueButton);
        buttonPanel.add(refreshButton);

        JPanel headerPanel = UITheme.createPlainPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = UITheme.createTitleLabel("Dashboard", 30f);
        JLabel subtitleLabel = UITheme.createSecondaryLabel("Tournament overview", 14f);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        headerPanel.add(subtitleLabel);

        JPanel bodyPanel = UITheme.createPlainPanel();
        bodyPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 0, 16, 0);
        bodyPanel.add(statsPanel, constraints);

        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        bodyPanel.add(buttonPanel, constraints);

        add(headerPanel, BorderLayout.NORTH);
        add(bodyPanel, BorderLayout.CENTER);
        refreshData();
    }

    public void refreshData() {
        try {
            // Service calculates totals using TEAM and MATCH_TABLE queries.
            DashboardStats stats = dashboardService.getStats();
            totalTeamsValue.setText(String.valueOf(stats.getTotalTeams()));
            matchesPlayedValue.setText(String.valueOf(stats.getMatchesPlayed()));
            matchesRemainingValue.setText(String.valueOf(stats.getMatchesRemaining()));
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(), "Dashboard Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        // Reusable card builder for Total Teams, Matches Played, and Matches Remaining.
        JPanel panel = UITheme.createCardPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 132));
        JLabel titleLabel = UITheme.createSecondaryLabel(title, 12f);
        titleLabel.setFont(UITheme.BASE_FONT.deriveFont(Font.BOLD, 12f));
        valueLabel.setHorizontalAlignment(JLabel.LEFT);
        valueLabel.setForeground(UITheme.TEXT_PRIMARY);
        valueLabel.setFont(UITheme.BASE_FONT.deriveFont(Font.BOLD, 44f));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private void openAddTeamDialog() {
        // Modal dialog blocks the main window until Save or close is pressed.
        JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(this), "Add Team", true);
        dialog.setLayout(new BorderLayout(12, 12));
        dialog.setSize(420, 300);
        dialog.setLocationRelativeTo(this);
        UITheme.styleDialog(dialog);

        JTextField nameField = new JTextField();
        JTextField groupField = new JTextField();
        JTextField captainField = new JTextField();
        JTextField cityField = new JTextField();
        UITheme.styleField(nameField);
        UITheme.styleField(groupField);
        UITheme.styleField(captainField);
        UITheme.styleField(cityField);

        JPanel formPanel = UITheme.createCardPanel();
        formPanel.setLayout(new GridLayout(4, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));
        formPanel.add(UITheme.createSecondaryLabel("Team Name", 13f));
        formPanel.add(nameField);
        formPanel.add(UITheme.createSecondaryLabel("Group", 13f));
        formPanel.add(groupField);
        formPanel.add(UITheme.createSecondaryLabel("Captain", 13f));
        formPanel.add(captainField);
        formPanel.add(UITheme.createSecondaryLabel("Home City", 13f));
        formPanel.add(cityField);

        JButton saveButton = new JButton("Save Team");
        UITheme.styleButton(saveButton, UITheme.HIGHLIGHT, UITheme.HIGHLIGHT_HOVER, UITheme.TEXT_PRIMARY);
        saveButton.addActionListener(event -> {
            try {
                // Read form fields, build a Team model object, then pass it to the service layer.
                Team team = new Team(0, nameField.getText().trim(), groupField.getText().trim(),
                        captainField.getText().trim(), cityField.getText().trim());
                teamService.addTeam(team);
                dialog.dispose();
                // Refresh dashboard, dropdowns, and points table after a new team is inserted.
                onDataChanged.run();
                UITheme.showStyledMessage(this, "Success", "Team added successfully.", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException exception) {
                UITheme.showStyledMessage(dialog, "Save Error", exception.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel footer = UITheme.createPlainPanel();
        footer.setLayout(new FlowLayout(FlowLayout.RIGHT));
        footer.add(saveButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void openAddVenueDialog() {
        // Venue dialog collects stadium details used later while entering a match.
        JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(this), "Add Venue", true);
        dialog.setLayout(new BorderLayout(12, 12));
        dialog.setSize(420, 260);
        dialog.setLocationRelativeTo(this);
        UITheme.styleDialog(dialog);

        JTextField nameField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField capacityField = new JTextField();
        UITheme.styleField(nameField);
        UITheme.styleField(cityField);
        UITheme.styleField(capacityField);

        JPanel formPanel = UITheme.createCardPanel();
        formPanel.setLayout(new GridLayout(3, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 8, 18));
        formPanel.add(UITheme.createSecondaryLabel("Venue Name", 13f));
        formPanel.add(nameField);
        formPanel.add(UITheme.createSecondaryLabel("City", 13f));
        formPanel.add(cityField);
        formPanel.add(UITheme.createSecondaryLabel("Capacity", 13f));
        formPanel.add(capacityField);

        JButton saveButton = new JButton("Save Venue");
        UITheme.styleButton(saveButton, UITheme.HIGHLIGHT, UITheme.HIGHLIGHT_HOVER, UITheme.TEXT_PRIMARY);
        saveButton.addActionListener(event -> {
            try {
                // Capacity must be numeric because database column VENUE.capacity is INT.
                Venue venue = new Venue(0, nameField.getText().trim(), cityField.getText().trim(),
                        Integer.parseInt(capacityField.getText().trim()));
                venueService.addVenue(venue);
                dialog.dispose();
                // Refresh all screens so Match Entry venue dropdown immediately gets the new venue.
                onDataChanged.run();
                UITheme.showStyledMessage(this, "Success", "Venue added successfully.", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException exception) {
                UITheme.showStyledMessage(dialog, "Input Error", "Capacity must be a valid number.", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException exception) {
                UITheme.showStyledMessage(dialog, "Save Error", exception.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel footer = UITheme.createPlainPanel();
        footer.setLayout(new FlowLayout(FlowLayout.RIGHT));
        footer.add(saveButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
