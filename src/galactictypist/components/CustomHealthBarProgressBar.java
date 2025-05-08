package galactictypist.components;

import galactictypist.MainManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;

public class CustomHealthBarProgressBar extends JProgressBar {

    private Font customFont;

    public CustomHealthBarProgressBar(int min, int max) {
        super(min, max);
        setForeground(new Color(0, 200, 0));  // Green for full health
        setBackground(Color.GRAY);  // Background color for the health bar
        setStringPainted(true);  // Display percentage on the bar
        setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));  // Set a nice border
        // Load the custom font
        loadCustomFont();

        // Set the font to the custom font if available, or fallback to Roboto
        setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 18));
    }

    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/01_DigiGraphics.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 18f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);  // Register the font
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... CustomHealthBarProgressBar", e);  // Log the error if any  // Log the error if any
            customFont = null;  // Set customFont to null to trigger fallback in table setup
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Call the super method to make sure the bar is drawn
        super.paintComponent(g);

        // Get current health percentage
        int health = getValue();

        // Create a 2D Graphics object for custom painting
        Graphics2D g2d = (Graphics2D) g;

        // Enable anti-aliasing for smoother graphics and text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gradient transition: Green (100%) -> Yellow (50%) -> Red (0%)
        Color startColor = (health > 50) ? new Color(0, 200, 0) : new Color(255, 255, 0);  // Green or Yellow
        Color endColor = (health > 50) ? new Color(255, 255, 0) : new Color(255, 0, 0);    // Yellow or Red

        // GradientPaint from left to right
        GradientPaint gradient = new GradientPaint(
                0, 0, startColor, // Start color at left
                getWidth(), 0, endColor // End color at right
        );

        // Set the paint to gradient
        g2d.setPaint(gradient);

        // Fill the progress area
        g2d.fillRect(2, 2, (int) (getWidth() * ((float) health / getMaximum())) - 4, getHeight() - 4);

        // Now we handle drawing the text with a drop shadow
        String text = getString();

        // Drop shadow properties
        int shadowOffset = 2;  // Offset for the shadow
        Color shadowColor = new Color(50, 50, 50, 150);  // Slightly transparent dark shadow

        // Font metrics for positioning text
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();  // Baseline to top distance

        // Calculate the position of the text (centered)
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight) / 2 - fm.getDescent();

        // Draw shadow (first draw the text slightly offset for the shadow effect)
        g2d.setColor(shadowColor);
        g2d.drawString(text, x + shadowOffset, y + shadowOffset);

        // Draw the main white text
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
    }
}
