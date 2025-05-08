package galactictypist.components.labels.game;

import galactictypist.MainManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class DifficultyAndGamemodeLabel extends JLabel {

    private int currentLevel = 1; // RANGE FROM ENDLESS MODE 0 - STORY MODE 1-5
    private int currentDifficulty = 1; // RANGE FROM EASY 1 - MEDIUM 2 - HARD 3
    private Font customFont;

    public DifficultyAndGamemodeLabel(int currentLevel, int currentDifficulty) {
        this.currentLevel = currentLevel;
        this.currentDifficulty = currentDifficulty;
        setupTextLabel();
        loadCustomFont();
        setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 20));
    }

    private void loadCustomFont() {
        try {
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/square_sans_serif_7.TTF");
            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 18f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... DifficultyAndGamemodeLabel", e);
            customFont = null;
        }
    }

    private void setupTextLabel() {
        SwingUtilities.invokeLater(() -> setVisible(true));
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int initialStartX = 50;  // Adjust based on your layout
        int initialStartY = 80;   // Adjust based on your layout
        int spacingY = 25;
        int marginX = -25;

        // Draw the "Endless Mode" or "Story Mode" text with a shadow
        if (currentLevel == 0) {
            drawTextWithShadow(g, "Endless Mode", initialStartX - 7, initialStartY);
        } else {
            drawTextWithShadow(g, "Story Mode", initialStartX + 1, initialStartY);
        }

        // Draw the difficulty text with shadow
        switch (currentDifficulty) {
            case 1:
                drawTextWithShadow(g, "Difficulty: Easy", -5 + marginX + initialStartX, initialStartY + spacingY);
                break;
            case 2:
                drawTextWithShadow(g, "Difficulty: Medium", -10 + marginX + initialStartX, initialStartY + spacingY);
                break;
            case 3:
                drawTextWithShadow(g, "Difficulty: Hard", -5 + marginX + initialStartX, initialStartY + spacingY);
                break;
            default:
                drawTextWithShadow(g, "Difficulty: Unknown", marginX + initialStartX - 25, initialStartY + spacingY);
                break;
        }
    }

    private void drawTextWithShadow(Graphics g, String text, int x, int y) {
        if (customFont == null) {
            customFont = new Font("Roboto", Font.BOLD, 18);  // Fallback font
        }

        // Create shadow image
        BufferedImage shadow = createTextShadow(text, customFont, Color.DARK_GRAY);

        // Draw shadow image at the specified location
        g.drawImage(shadow, x - 2, y - shadow.getHeight() + 2, this);

        // Draw actual text on top
        g.setFont(customFont);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }
}
