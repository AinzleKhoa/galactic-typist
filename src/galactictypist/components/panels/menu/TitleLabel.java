package galactictypist.components.panels.menu;

import galactictypist.MainManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * A simple JLabel that displays "To Be Continued..." text with a shadow effect.
 */
public class TitleLabel extends JLabel {

    private Font customFont;

    // Constructor to set up the label properties
    public TitleLabel() {
        setupTextLabel();
        // Load the custom font
        loadCustomFont();

        // Set the font to the custom font if available, or fallback to Roboto
        setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 50));
    }

    private void setupTextLabel() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/square_sans_serif_7.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 48f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);  // Register the font
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... YouWinLabel", e);  // Log the error if any
            customFont = null;  // Set customFont to null to trigger fallback in table setup
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int shadowOffset = 2;
        int initialStartX = 12;  // Adjust based on your layout
        int initialStartY = 80;   // Adjust based on your layout

        // Draw the shadow in dark gray
        g.setColor(Color.DARK_GRAY);
        g.drawString("Galactic Typist", initialStartX + shadowOffset, initialStartY + shadowOffset);

        // Now draw the main text in white
        g.setColor(Color.WHITE);
        g.drawString("Galactic Typist", initialStartX, initialStartY);
    }
}
