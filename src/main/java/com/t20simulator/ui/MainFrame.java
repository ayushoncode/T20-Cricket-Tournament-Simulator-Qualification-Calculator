package com.t20simulator.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * Main Swing window that hosts all screens inside a custom top navigation bar.
 */
public class MainFrame extends JFrame {
    private static final String DASHBOARD = "dashboard";
    private static final String MATCH_ENTRY = "matchEntry";
    private static final String POINTS_TABLE = "pointsTable";

    private final DashboardPanel dashboardPanel;
    private final MatchEntryPanel matchEntryPanel;
    private final PointsTablePanel pointsTablePanel;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final JButton dashboardButton;
    private final JButton matchEntryButton;
    private final JButton pointsTableButton;

    public MainFrame() {
        super("T20 Cricket Tournament Simulator");
        UITheme.installLookAndFeelDefaults();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 760);
        setMinimumSize(new java.awt.Dimension(980, 640));
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.BACKGROUND);
        setLayout(new BorderLayout());

        dashboardPanel = new DashboardPanel(this::refreshAllPanels);
        matchEntryPanel = new MatchEntryPanel(this::refreshAllPanels);
        pointsTablePanel = new PointsTablePanel();
        dashboardButton = new JButton("Dashboard");
        matchEntryButton = new JButton("Match Entry");
        pointsTableButton = new JButton("Points Table");

        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(UITheme.SURFACE);
        navBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
                BorderFactory.createEmptyBorder(14, 22, 14, 22)));

        JLabel appTitle = new JLabel("T20 Tournament Simulator");
        appTitle.setForeground(UITheme.TEXT_PRIMARY);
        appTitle.setFont(UITheme.BASE_FONT.deriveFont(Font.BOLD, 20f));

        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        tabPanel.setOpaque(false);

        tabPanel.add(dashboardButton);
        tabPanel.add(matchEntryButton);
        tabPanel.add(pointsTableButton);
        navBar.add(appTitle, BorderLayout.WEST);
        navBar.add(tabPanel, BorderLayout.EAST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UITheme.BACKGROUND);
        contentPanel.add(dashboardPanel, DASHBOARD);
        contentPanel.add(matchEntryPanel, MATCH_ENTRY);
        contentPanel.add(pointsTablePanel, POINTS_TABLE);

        dashboardButton.addActionListener(event -> showTab(DASHBOARD));
        matchEntryButton.addActionListener(event -> showTab(MATCH_ENTRY));
        pointsTableButton.addActionListener(event -> showTab(POINTS_TABLE));

        add(navBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        showTab(DASHBOARD);
    }

    private void refreshAllPanels() {
        dashboardPanel.refreshData();
        matchEntryPanel.refreshData();
        pointsTablePanel.refreshData();
    }

    private void showTab(String tabName) {
        cardLayout.show(contentPanel, tabName);
        UITheme.styleTabButton(dashboardButton, DASHBOARD.equals(tabName));
        UITheme.styleTabButton(matchEntryButton, MATCH_ENTRY.equals(tabName));
        UITheme.styleTabButton(pointsTableButton, POINTS_TABLE.equals(tabName));

        if (DASHBOARD.equals(tabName)) {
            dashboardPanel.refreshData();
        } else if (MATCH_ENTRY.equals(tabName)) {
            matchEntryPanel.refreshData();
        } else if (POINTS_TABLE.equals(tabName)) {
            pointsTablePanel.refreshData();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
