/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galactictypist.components.panels.game;

import galactictypist.GameGUI;
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
import javax.swing.JTextField;

/**
 *
 * @author Ainzle
 */
// Textbox panel for input
public class TextboxPanel extends JPanel {

    private Image textboxImage;
    private JTextField textField;
    private GameGUI gameGUI;

    private int locationX = 0;
    private int locationY = 760;
    private int widthImage = 250;
    private int heightImage = 130;

    private Font customFont;

    public TextboxPanel(String ImagePath, GameGUI gameGUI) {
        this.textboxImage = new ImageIcon(getClass().getResource(ImagePath)).getImage();
        this.gameGUI = gameGUI;
        setLayout(null);
        setOpaque(false); // Transparency for the textbox

        // Create and position the text field
        textField = new JTextField();

        // Set size for the text field (widthImage - 100 = padding, height smaller than the image height)
        int textFieldWidth = widthImage - 100;
        int textFieldHeight = heightImage - 40; // Make the text field a bit smaller than the image

        // Calculate centered position for the text field
        int textFieldX = locationX + (widthImage - textFieldWidth) / 2;
        int textFieldY = locationY + (heightImage - textFieldHeight) / 2;

        // Set the bounds for the text field
        textField.setBounds(textFieldX, textFieldY, textFieldWidth, textFieldHeight);

        // Customize the text field appearance
        textField.setForeground(Color.WHITE);
        textField.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        textField.setOpaque(false);
        textField.setBorder(null); // No borders

        add(textField);

        // Load the custom font
        loadCustomFont();

        // Set the font to the custom font if available, or fallback to Roboto
        textField.setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 26));

        textField.addActionListener(e
                -> {
            gameGUI.getMainManager().inputHandler(textField.getText());
            textField.setText(""); // Clear the text field after submission
            gameGUI.updateStatusOverlay();
        });
        // Request focus for the text field when the TextboxPanel is created
        setFocusOnTheInputTextbox(); // Set focus to the text field immediately
    }
    
    public void setFocusOnTheInputTextbox() {
        textField.requestFocusInWindow();
    }

    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/visitor1.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 29f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);  // Register the font
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... TextboxPanel Game", e);  // Log the error if any
            customFont = null;  // Set customFont to null to trigger fallback in table setup
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(textboxImage, locationX, locationY, widthImage, heightImage, this);
    }
}
