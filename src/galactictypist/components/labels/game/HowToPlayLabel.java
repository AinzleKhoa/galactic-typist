package galactictypist.components.labels.game;

import galactictypist.MainManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import javax.swing.JLabel;

/**
 * A simple JLabel that displays "To Be Continued..." text with a shadow effect.
 */
public class HowToPlayLabel extends JLabel {

    private Font customFont;

    // Constructor to set up the label properties
    public HowToPlayLabel() {
        loadCustomFont();  // Load the custom font
    }

    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/square_sans_serif_7.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 60f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);  // Register the font
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... YouLeviathanLabel", e);  // Log the error if any
            customFont = null;  // Set customFont to null to trigger fallback in table setup
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set the custom font (or fallback if customFont is null)
        g.setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 40));

        int shadowOffset = 2;
        int initialStartX = 100;
        int initialStartY = 120;
        int lineHeight = g.getFontMetrics().getHeight();

        // Define the lines of text
        String[] lines = {
            "Type the correct word,",
            "then press 'Enter'",
            "to defeat the enemies!"
        };

        // Draw each line of text with a shadow effect
        for (int i = 0; i < lines.length; i++) {
            int yPosition = initialStartY + (i * lineHeight);

            // Draw shadow
            g.setColor(Color.WHITE);
            g.drawString(lines[i], initialStartX + shadowOffset, yPosition + shadowOffset);

            // Draw main text
            g.setColor(Color.CYAN);
            g.drawString(lines[i], initialStartX, yPosition);
        }
    }
}
