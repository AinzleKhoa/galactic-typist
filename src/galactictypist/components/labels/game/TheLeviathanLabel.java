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
public class TheLeviathanLabel extends JLabel {

    private Font customFont;

    // Constructor to set up the label properties
    public TheLeviathanLabel() {
        loadCustomFont();  // Load the custom font
    }

    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/square_sans_serif_7.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 80f);
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
        g.setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 80));

        int shadowOffset = 2;
        int initialStartX = 100;  // Adjust based on your layout
        int initialStartY = 120;   // Adjust based on your layout

            // Draw the shadow in dark gray
            g.setColor(Color.DARK_GRAY);
            g.drawString("! The Leviathan !", initialStartX + shadowOffset, initialStartY + shadowOffset);

            // Now draw the main text in white
            g.setColor(Color.RED);
            g.drawString("! The Leviathan !", initialStartX, initialStartY);
    }
}
