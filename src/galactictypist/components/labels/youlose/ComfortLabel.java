package galactictypist.components.labels.youlose;

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

public class ComfortLabel extends JLabel {

    private String playerName;
    private Font customFont;

    // Constructor to set up the label properties
    public ComfortLabel(String playerName) {
        this.playerName = playerName;
        setupTextLabel();
        loadCustomFont();  // Load the custom font
    }

    private void setupTextLabel() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/square_sans_serif_7.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 18f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);  // Register the font
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... ComfortLabel", e);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        String message = "Stay strong, Astroer '" + playerName + "'! The stars will guide your way.";
        int initialStartX = 40;  // Adjust based on your layout
        int initialStartY = 80;   // Adjust based on your layout

        // Create and draw the shadow image
        BufferedImage shadow = createTextShadow(message, customFont != null ? customFont : new Font("Roboto", Font.BOLD, 25), Color.DARK_GRAY);
        g.drawImage(shadow, initialStartX - 2, initialStartY - shadow.getHeight() + 2, this);

        // Draw the main text in white on top
        g.setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 25));
        g.setColor(Color.WHITE);
        g.drawString(message, initialStartX, initialStartY);
    }
}
