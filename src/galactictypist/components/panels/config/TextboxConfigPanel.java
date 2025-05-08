/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galactictypist.components.panels.config;

import galactictypist.MainManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Ainzle
 */
public class TextboxConfigPanel extends JPanel {

    private Image textboxImage;
    private JTextArea textAreaMessage;
    private Font customFont;

    public TextboxConfigPanel(String ImagePath, JTextArea textAreaMessage) {
        this.textboxImage = new ImageIcon(getClass().getResource(ImagePath)).getImage();
        this.textAreaMessage = textAreaMessage; // Keep the passed instance

        setOpaque(false); // This allows the background to be transparent
        setLayout(null);

        // DO NOT reinitialize textAreaMessage here
        // Add the passed textAreaMessage directly to the panel
        textAreaMessage.setBounds(65, 25, 120, 100); // Adjust as needed
        textAreaMessage.setEditable(false); // Make the text field uneditable
        textAreaMessage.setForeground(Color.WHITE);
        textAreaMessage.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        textAreaMessage.setOpaque(false); // Keep the background transparent
        textAreaMessage.setBorder(null); // Border transparent
        textAreaMessage.setWrapStyleWord(true); // Enable word wrapping
        textAreaMessage.setLineWrap(true); // Wrap lines when they are too long

        // Set text alignment to center
        textAreaMessage.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        textAreaMessage.setAlignmentY(JTextArea.CENTER_ALIGNMENT);

        add(textAreaMessage); // Add the passed instance, not a new one
        // Load the custom fon
        loadCustomFont();

        // Set the font to the custom font if available, or fallback to Roboto
        setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 15));
    }

    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/square_sans_serif_7.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 15f);
                // Register the font in the GraphicsEnvironment so it can be used
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
            } else {
                System.err.println("Font file not found. Using fallback font.");
                customFont = new Font("Roboto", Font.BOLD, 15);  // Fallback font
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... TextboxConfigPanel", e);  // Log the error if any
            customFont = new Font("Roboto", Font.BOLD, 15);  // Fallback font
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the spaceship image with transparency
        g.drawImage(textboxImage, 0, 0, getWidth(), getHeight(), this);
    }
}
