package com.t20simulator.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Shared dark-theme styling helpers for all Swing UI components.
 * Keeping styles here prevents repeated color/font code in every panel.
 */
public final class UITheme {
    // Common color palette used by all screens.
    public static final Color BACKGROUND = new Color(0xF4F7F2);
    public static final Color SURFACE = new Color(0xFFFFFF);
    public static final Color CARD = new Color(0xFFFFFF);
    public static final Color BORDER = new Color(0xDCE4DA);
    public static final Color ACCENT = new Color(0x173F35);
    public static final Color ACCENT_SOFT = new Color(0xE6F0EA);
    public static final Color HIGHLIGHT = new Color(0xC65D2E);
    public static final Color HIGHLIGHT_HOVER = new Color(0xA94D25);
    public static final Color TEXT_PRIMARY = new Color(0x1F2A24);
    public static final Color TEXT_SECONDARY = new Color(0x64706A);
    public static final Color SUCCESS = new Color(0x14835F);
    public static final Color WARNING = new Color(0xB7791F);

    public static final Font BASE_FONT = new Font("SansSerif", Font.PLAIN, 14);

    private UITheme() {
        // Utility class: no object should be created.
    }

    public static void installLookAndFeelDefaults() {
        // Configure default Swing component styles before panels are built.
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", SURFACE);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("Button.font", BASE_FONT);
        UIManager.put("Label.font", BASE_FONT);
        UIManager.put("TextField.font", BASE_FONT);
        UIManager.put("ComboBox.font", BASE_FONT);
        UIManager.put("Table.font", BASE_FONT);
        UIManager.put("TableHeader.font", BASE_FONT.deriveFont(Font.BOLD, 13f));
    }

    public static void stylePanel(JPanel panel) {
        // Base style for full-screen panels.
        panel.setBackground(BACKGROUND);
        panel.setOpaque(true);
    }

    public static RoundedPanel createCardPanel() {
        // Card panel is used for grouped UI blocks like forms and stat cards.
        RoundedPanel panel = new RoundedPanel(CARD, 8, BORDER);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    public static JPanel createPlainPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND);
        panel.setOpaque(true);
        return panel;
    }

    public static JLabel createTitleLabel(String text, float size) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_PRIMARY);
        label.setFont(BASE_FONT.deriveFont(Font.BOLD, size));
        return label;
    }

    public static JLabel createSecondaryLabel(String text, float size) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_SECONDARY);
        label.setFont(BASE_FONT.deriveFont(Font.PLAIN, size));
        return label;
    }

    public static void styleField(JComponent component) {
        // Shared field style for JTextField and JComboBox inputs.
        component.setFont(BASE_FONT.deriveFont(13f));
        component.setForeground(TEXT_PRIMARY);
        component.setBackground(SURFACE);
        component.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        component.setPreferredSize(new Dimension(220, 38));
        component.setOpaque(true);
        if (component instanceof JComboBox) {
            ((JComboBox<?>) component).setFocusable(false);
        }
    }

    public static void styleButton(JButton button, Color baseColor, Color hoverColor, Color textColor) {
        // Button receives normal color and hover color for better UI feedback.
        button.setBackground(baseColor);
        button.setForeground(textColor);
        button.setFont(BASE_FONT.deriveFont(Font.BOLD, 13f));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent event) {
                button.setBackground(baseColor);
            }
        });
    }

    public static void styleTabButton(JButton button, boolean active) {
        // Navigation buttons look different when their tab is active.
        button.setFont(BASE_FONT.deriveFont(Font.BOLD, 14f));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(active ? ACCENT : BORDER, 1),
                BorderFactory.createEmptyBorder(9, 18, 9, 18)));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setBackground(active ? ACCENT : SURFACE);
        button.setForeground(active ? Color.WHITE : TEXT_SECONDARY);
    }

    public static Border createCardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(18, 18, 18, 18));
    }

    public static void styleDialog(JDialog dialog) {
        dialog.getContentPane().setBackground(BACKGROUND);
    }

    public static void showStyledMessage(Component parent, String title, String message, int messageType) {
        // Wrapper around JOptionPane so all popups follow the same design.
        JLabel label = new JLabel("<html><body style='width:280px;'>" + message + "</body></html>");
        label.setForeground(TEXT_PRIMARY);
        label.setFont(BASE_FONT);
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        panel.add(label);
        JOptionPane optionPane = new JOptionPane(panel, messageType);
        JDialog dialog = optionPane.createDialog(parent, title);
        styleDialog(dialog);
        dialog.setVisible(true);
    }

    /**
     * Anti-aliased rounded panel used for cards and form containers.
     */
    public static class RoundedPanel extends JPanel {
        private final Color backgroundColor;
        private final Color borderColor;
        private final int arc;

        public RoundedPanel(Color backgroundColor, int arc) {
            this(backgroundColor, arc, BORDER);
        }

        public RoundedPanel(Color backgroundColor, int arc, Color borderColor) {
            this.backgroundColor = backgroundColor;
            this.arc = arc;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            // Custom painting draws anti-aliased rounded background and border.
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(backgroundColor);
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            graphics2D.setColor(borderColor);
            graphics2D.setStroke(new BasicStroke(1f));
            graphics2D.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            graphics2D.dispose();
            super.paintComponent(graphics);
        }
    }
}
