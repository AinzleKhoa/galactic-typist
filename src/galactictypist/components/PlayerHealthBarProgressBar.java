package galactictypist.components;

import galactictypist.MainManager;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JProgressBar;

public class PlayerHealthBarProgressBar extends JProgressBar {

    public PlayerHealthBarProgressBar(int min, int max) {
        super(JProgressBar.VERTICAL, min, max);  // Set orientation to vertical
        setBackground(Color.DARK_GRAY);  // Background color for the health bar
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));  // Remove default border for custom drawing
    }

    @Override
    protected void paintComponent(Graphics g) {
        int health = getValue();
        int maxHealth = getMaximum();

        // Create a 2D Graphics object for custom painting
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Neon blue color similar to the uploaded image
        Color neonBlue = new Color(0, 255, 255); // Bright neon blue
        Color darkBlue = new Color(0, 51, 102);  // Dark blue at low health

        // GradientPaint from bottom to top
        GradientPaint gradient = new GradientPaint(
                0, getHeight(), darkBlue,      // Dark blue at the bottom
                0, 0, neonBlue                 // Neon blue at the top
        );

        // Background for rounded border with increased radius
        g2d.setColor(Color.DARK_GRAY);
        RoundRectangle2D roundedBorder = new RoundRectangle2D.Double(2, 2, getWidth() - 4, getHeight() - 4, 30, 30); // Increased radius
        g2d.fill(roundedBorder);

        // Calculate fill height and position to fit neatly inside the border
        int fillHeight = (int) ((getHeight() - 10) * ((float) health / maxHealth)); // Slight adjustment to stay inside border
        int fillY = getHeight() - fillHeight - 5; // Adjust position to start slightly within border

        // Draw gradient-filled health bar inside rounded border
        g2d.setPaint(gradient);
        RoundRectangle2D innerFill = new RoundRectangle2D.Double(5, fillY, getWidth() - 10, fillHeight, 24, 24); // Smaller radius and position adjustments
        g2d.fill(innerFill);

        // Draw rounded border outline with neon blue
        g2d.setColor(neonBlue);
        g2d.setStroke(new BasicStroke(3)); // Thicker border for visibility
        g2d.draw(roundedBorder);

        // Draw dividing lines for sections at 100, 80, 60, 40, 20, and 0 for visual health checkpoints
        int[] markers = {80, 60, 40, 20};
        g2d.setColor(neonBlue.brighter()); // Slightly brighter neon blue for markers
        g2d.setStroke(new BasicStroke(0.75f));  // Thin line for markers

        for (int marker : markers) {
            int markerPosition = getHeight() - (getHeight() * marker / maxHealth);
            g2d.drawLine(6, markerPosition, getWidth() - 6, markerPosition); // Moved lines slightly inward to avoid border overlap
        }
    }
}
