package com.t20simulator.ui;

import com.t20simulator.model.PointsTable;
import com.t20simulator.model.Scenario;
import com.t20simulator.model.Team;
import com.t20simulator.service.LeaderboardService;
import com.t20simulator.service.ScenarioPredictorService;
import com.t20simulator.service.TeamService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.List;

/**
 * Points table tab that displays the leaderboard and checks qualification scenarios.
 * It is used after matches are entered to inspect rankings and top-four chances.
 */
public class PointsTablePanel extends JPanel {
    // Table model defines columns shown in the leaderboard.
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Rank", "Team Name", "Played", "Won", "Lost", "Tied", "NRR", "Points"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            // User should not edit calculated leaderboard values directly.
            return false;
        }
    };

    // JTable displays the points table; combo box selects team for prediction.
    private final JTable pointsTable = new JTable(tableModel);
    private final JComboBox<Team> teamComboBox = new JComboBox<>();
    private final JLabel resultLabel = new JLabel("Choose a team and predict qualification.");

    private final LeaderboardService leaderboardService = new LeaderboardService();
    private final TeamService teamService = new TeamService();
    private final ScenarioPredictorService scenarioPredictorService = new ScenarioPredictorService();

    public PointsTablePanel() {
        UITheme.stylePanel(this);
        setLayout(new BorderLayout(22, 22));
        setBorder(BorderFactory.createEmptyBorder(26, 28, 28, 28));

        JLabel titleLabel = UITheme.createTitleLabel("Points Table", 30f);
        JLabel subtitleLabel = UITheme.createSecondaryLabel("Leaderboard and qualification check", 14f);

        JButton refreshButton = new JButton("Refresh");
        UITheme.styleButton(refreshButton, UITheme.SURFACE, UITheme.ACCENT_SOFT, UITheme.TEXT_PRIMARY);
        refreshButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1),
                BorderFactory.createEmptyBorder(9, 18, 9, 18)));
        // Refresh reloads leaderboard and team dropdown from database.
        refreshButton.addActionListener(event -> refreshData());

        JButton checkQualificationButton = new JButton("Predict");
        UITheme.styleButton(checkQualificationButton, UITheme.SUCCESS, new java.awt.Color(0x009E7D), UITheme.TEXT_PRIMARY);
        // Predict checks whether selected team can still reach rank 4.
        checkQualificationButton.addActionListener(event -> checkQualification());

        styleInput(teamComboBox);
        styleTable();

        JPanel topPanel = UITheme.createPlainPanel();
        topPanel.setLayout(new BorderLayout());
        JPanel headingPanel = UITheme.createPlainPanel();
        headingPanel.setLayout(new javax.swing.BoxLayout(headingPanel, javax.swing.BoxLayout.Y_AXIS));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headingPanel.add(titleLabel);
        headingPanel.add(javax.swing.Box.createRigidArea(new Dimension(0, 4)));
        headingPanel.add(subtitleLabel);
        topPanel.add(headingPanel, BorderLayout.CENTER);

        JPanel buttonPanel = UITheme.createPlainPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.add(refreshButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        JPanel bottomPanel = UITheme.createCardPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 2));
        JLabel predictorLabel = new JLabel("Check Qualification");
        predictorLabel.setForeground(UITheme.TEXT_PRIMARY);
        predictorLabel.setFont(UITheme.BASE_FONT.deriveFont(Font.BOLD, 14f));
        resultLabel.setForeground(UITheme.TEXT_SECONDARY);
        resultLabel.setFont(UITheme.BASE_FONT);

        bottomPanel.add(predictorLabel);
        bottomPanel.add(teamComboBox);
        bottomPanel.add(checkQualificationButton);
        bottomPanel.add(resultLabel);

        JScrollPane scrollPane = new JScrollPane(pointsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scrollPane.getViewport().setBackground(UITheme.SURFACE);
        scrollPane.setBackground(UITheme.SURFACE);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        refreshData();
    }

    public void refreshData() {
        // Both table and dropdown depend on database values, so refresh both together.
        loadLeaderboard();
        loadTeams();
    }

    private void loadLeaderboard() {
        tableModel.setRowCount(0);
        try {
            // LeaderboardService sorts teams by points, then NRR, then name.
            List<PointsTable> leaderboard = leaderboardService.getLeaderboard();
            for (PointsTable entry : leaderboard) {
                tableModel.addRow(new Object[]{
                        entry.getRank(),
                        entry.getTeamName(),
                        entry.getPlayed(),
                        entry.getWon(),
                        entry.getLost(),
                        entry.getTied(),
                        String.format("%.3f", entry.getNrr()),
                        entry.getPoints()
                });
            }
        } catch (SQLException exception) {
            UITheme.showStyledMessage(this, "Leaderboard Error", exception.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTeams() {
        try {
            // Team list is needed for the qualification predictor combo box.
            List<Team> teams = teamService.getAllTeams();
            teamComboBox.removeAllItems();
            for (Team team : teams) {
                teamComboBox.addItem(team);
            }
        } catch (SQLException exception) {
            UITheme.showStyledMessage(this, "Team Load Error", exception.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkQualification() {
        // No prediction can run until a team is selected.
        Team selectedTeam = (Team) teamComboBox.getSelectedItem();
        if (selectedTeam == null) {
            UITheme.showStyledMessage(this, "Input Required", "Please select a team first.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Target rank 4 means top-four qualification.
            Scenario scenario = scenarioPredictorService.evaluateQualification(selectedTeam.getTeamId(), 4);
            String result = scenarioPredictorService.buildScenarioSummary(scenario);
            resultLabel.setForeground(UITheme.SUCCESS);
            resultLabel.setText(result);
            UITheme.showStyledMessage(this, "Qualification Result", result, JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException exception) {
            resultLabel.setForeground(UITheme.HIGHLIGHT);
            resultLabel.setText(exception.getMessage());
            UITheme.showStyledMessage(this, "Scenario Error", exception.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleInput(JComboBox<?> comboBox) {
        UITheme.styleField(comboBox);
    }

    private void styleTable() {
        // Centralized table styling for readable rows and a clear header.
        pointsTable.setBackground(UITheme.CARD);
        pointsTable.setForeground(UITheme.TEXT_PRIMARY);
        pointsTable.setGridColor(UITheme.BORDER);
        pointsTable.setRowHeight(42);
        pointsTable.setSelectionBackground(UITheme.ACCENT_SOFT);
        pointsTable.setSelectionForeground(UITheme.TEXT_PRIMARY);
        pointsTable.setFont(UITheme.BASE_FONT);
        pointsTable.setShowVerticalLines(false);
        pointsTable.setShowHorizontalLines(true);
        pointsTable.setFillsViewportHeight(true);

        JTableHeader header = pointsTable.getTableHeader();
        header.setBackground(UITheme.ACCENT);
        header.setForeground(java.awt.Color.WHITE);
        header.setFont(UITheme.BASE_FONT.deriveFont(Font.BOLD, 13f));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 42));

        // Custom renderer centers every cell and alternates row background colors.
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    component.setBackground(row % 2 == 0 ? UITheme.SURFACE : UITheme.ACCENT_SOFT);
                    component.setForeground(UITheme.TEXT_PRIMARY);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                setHorizontalAlignment(SwingConstants.CENTER);
                return component;
            }
        };

        for (int columnIndex = 0; columnIndex < pointsTable.getColumnModel().getColumnCount(); columnIndex++) {
            pointsTable.getColumnModel().getColumn(columnIndex).setCellRenderer(centerRenderer);
        }
    }
}
