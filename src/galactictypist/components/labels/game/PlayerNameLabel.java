package galactictypist.components.labels.game;

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
 * A simple JLabel that displays the player's name and input validation status.
 */
public class PlayerNameLabel extends JLabel {

    private Font customFont;
    private String name;
    private int validateInput; // 0 for wrong, 1 for correct
    private boolean initialValidateInput = true; // Display "WAITING..." initially

    // Constructor to set up the label properties
    public PlayerNameLabel(String name) {
        loadCustomFont();  // Load the custom font
        this.name = name;
        this.validateInput = -1; // Initialize to -1 to represent "waiting" state
    }

    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/square_sans_serif_7.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 20f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);  // Register the font
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... PlayerNameLabel", e);  // Log the error if any
            customFont = null;  // Set customFont to null to trigger fallback in table setup
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set the custom font (or fallback if customFont is null)
        g.setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 20));

        int shadowOffset = 2;
        int initialStartX = 120;  // Adjust based on your layout
        int initialStartY = 10;   // Adjust based on your layout

        // Draw the player's name
        g.setColor(Color.DARK_GRAY);
        g.drawString("User: " + name, initialStartX + shadowOffset, initialStartY + shadowOffset);
        g.setColor(Color.WHITE);
        g.drawString("User: " + name, initialStartX, initialStartY);

        // Draw validation status
        drawValidationStatus(g, initialStartX, initialStartY + 20); // Draw below the name
    }

    private void drawValidationStatus(Graphics g, int x, int y) {
        g.setColor(Color.DARK_GRAY);
        String validationMessage;

        if (initialValidateInput) {
            validationMessage = "Input: WAITING...";
            g.drawString(validationMessage, 100 + x + 2, 3 + y + 2);
            g.setColor(Color.GRAY);
            g.drawString(validationMessage, 100 + x, 3 + y);
        } else {
            // Set the message based on validation input
            switch (validateInput) {
                case 0:
                    validationMessage = "Input: WRONG!";
                    g.drawString(validationMessage, 100 + x + 2, 3 + y + 2);
                    g.setColor(Color.RED);
                    g.drawString(validationMessage, 100 + x, 3 + y);
                    break;
                case 1:
                    validationMessage = "Input: CORRECT!";
                    g.drawString(validationMessage, 100 + x + 2, 3 + y + 2);
                    g.setColor(Color.GREEN);
                    g.drawString(validationMessage, 100 + x, 3 + y);
                    break;
                case -1:
                    validationMessage = "Input: WAITING...";
                    g.drawString(validationMessage, 100 + x + 2, 3 + y + 2);
                    g.setColor(Color.GRAY);
                    g.drawString(validationMessage, 100 + x, 3 + y);
                    break;
                default:
                    break;
            }
        }
    }

    // Update the validation status based on user input
    public void updateValidationStatus(int validateInput) {
        this.validateInput = validateInput;
        this.initialValidateInput = false; // Update the state to no longer be waiting
        SwingUtilities.invokeLater(() -> repaint()); // Refresh the label to show the updated status
    }

    public void updateName(String name) {
        this.name = name; // Update the player's name
        SwingUtilities.invokeLater(() -> repaint()); // Refresh the label to show the updated name
    }
}
