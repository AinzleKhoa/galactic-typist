package galactictypist.components.panels.game;

import galactictypist.MainManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class StatusOverlayPanel extends JPanel {

    private Image textboxImage;
    private int locationX = 1200;
    private int locationY = 500;
    private int widthImage = 400;
    private int heightImage = 400;

    private JLabel livesStatusLabel;
    private JLabel scoreStatusLabel;
    private JLabel levelStatusLabel;
    private JLabel streakStatusLabel;
    private JLabel accuracyStatusLabel;

    private Font customFont;

    public StatusOverlayPanel(String imagePath, JLabel livesStatusLabel, JLabel scoreStatusLabel, JLabel levelStatusLabel, JLabel streakStatusLabel, JLabel accuracyStatusLabel) {
        setOpaque(false);
        setLayout(null);

        this.textboxImage = new ImageIcon(getClass().getResource(imagePath)).getImage();

        this.livesStatusLabel = livesStatusLabel;
        this.scoreStatusLabel = scoreStatusLabel;
        this.levelStatusLabel = levelStatusLabel;
        this.streakStatusLabel = streakStatusLabel;
        this.accuracyStatusLabel = accuracyStatusLabel;

        livesStatusLabel.setVisible(false);
        scoreStatusLabel.setVisible(false);
        levelStatusLabel.setVisible(false);
        streakStatusLabel.setVisible(false);
        accuracyStatusLabel.setVisible(false);

        livesStatusLabel.setBounds(locationX + 60, locationY + 50, 200, 30);
        scoreStatusLabel.setBounds(locationX + 60, locationY + 90, 200, 30);
        levelStatusLabel.setBounds(locationX + 60, locationY + 130, 200, 30);
        streakStatusLabel.setBounds(locationX + 60, locationY + 170, 200, 30);
        accuracyStatusLabel.setBounds(locationX + 60, locationY + 210, 200, 30);

        add(livesStatusLabel);
        add(scoreStatusLabel);
        add(levelStatusLabel);
        add(streakStatusLabel);
        add(accuracyStatusLabel);

        loadCustomFont();
        setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 18));
    }

    private void loadCustomFont() {
        try {
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/visitor1.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 23f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... StatusOverlayPanel Game", e);
            customFont = null;
        }
    }

    private BufferedImage createTextShadow(String text, Font font, Color shadowColor) {
        int width = getFontMetrics(font).stringWidth(text) + 4;
        int height = getFontMetrics(font).getHeight() + 4;
        BufferedImage shadowImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = shadowImage.createGraphics();
        g2d.setFont(font);
        g2d.setColor(shadowColor);

        // Draw shadow in multiple directions for a thicker effect
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) {
                    g2d.drawString(text, 2 + dx, height - 2 + dy);
                }
            }
        }
        g2d.dispose();
        return shadowImage;
    }

    public void updateStatus(String accuracy, int score, int lives, int level, int streak) {
        accuracyStatusLabel.setText("Accuracy: " + accuracy);
        scoreStatusLabel.setText("Score: " + score);
        livesStatusLabel.setText("Lives: " + lives);
        levelStatusLabel.setText("Level: " + level);
        streakStatusLabel.setText("Streak: " + streak);

        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (textboxImage != null) {
            g.drawImage(textboxImage, locationX, locationY, widthImage, heightImage, this);
        }

        int initialStartY = 110;
        int initialStartX = 80;
        int spacingY = 40;

        // Draw each label with shadow
        drawTextWithShadow(g, accuracyStatusLabel.getText(), locationX + initialStartX, locationY + initialStartY);
        drawTextWithShadow(g, scoreStatusLabel.getText(), locationX + initialStartX, locationY + initialStartY + spacingY * 1);
        drawTextWithShadow(g, livesStatusLabel.getText(), locationX + initialStartX, locationY + initialStartY + spacingY * 2);
        drawTextWithShadow(g, levelStatusLabel.getText(), locationX + initialStartX, locationY + initialStartY + spacingY * 3);
        drawTextWithShadow(g, streakStatusLabel.getText(), locationX + initialStartX, locationY + initialStartY + spacingY * 4);
    }

    private void drawTextWithShadow(Graphics g, String text, int x, int y) {
        if (customFont == null) {
            customFont = new Font("Roboto", Font.BOLD, 18);  // Fallback font
        }

        // Create shadow image
        BufferedImage shadow = createTextShadow(text, customFont, Color.DARK_GRAY);

        // Draw shadow image
        g.drawImage(shadow, x - 2, y - shadow.getHeight() + 2, this);

        // Draw actual text on top
        g.setFont(customFont);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }
}
