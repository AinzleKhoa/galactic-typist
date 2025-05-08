package galactictypist.components.panels.game;

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
import javax.swing.SwingUtilities;

/**
 *
 * @author Ainzle
 */
// Textbox panel for multi-line input
public class TextStoryPanel extends JPanel {

    private Image textboxImage;
    private JTextArea textArea = new JTextArea();

    private int locationX = 1100;
    private int locationY = 200;
    private int widthImage = 450;
    private int heightImage = 200;

    private Font customFont;

    public TextStoryPanel(String ImagePath) {
        this.textboxImage = new ImageIcon(getClass().getResource(ImagePath)).getImage();
        setLayout(null);
        setOpaque(false); // Transparency for the textbox

        // Set size for the text area (widthImage - 100 = padding, height smaller than the image height)
        int textAreaWidth = widthImage - 60;
        int textAreaHeight = heightImage - 60; // Make the text area a bit smaller than the image

        // Calculate centered position for the text area
        int textAreaX = -15 + locationX + (widthImage - textAreaWidth) / 2;
        int textAreaY = 20 + locationY + (heightImage - textAreaHeight) / 2;

        // Set the bounds for the text area
        textArea.setBounds(textAreaX, textAreaY, textAreaWidth, textAreaHeight);

        // Customize the text area appearance
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        textArea.setOpaque(false);
        textArea.setBorder(null); // No borders
        textArea.setLineWrap(true); // Enable line wrap
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true); // Wrap by words for readability

        add(textArea);

        // Load the custom font
        loadCustomFont();

        // Set the font to the custom font if available, or fallback to Roboto
        textArea.setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 22));
    }

    public void hideImage() {
        SwingUtilities.invokeLater(() -> setVisible(false));
    }

    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/visitor1.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 24f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);  // Register the font
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... TextStoryPanel Story", e);  // Log the error if any
            customFont = null;  // Set customFont to null to trigger fallback in table setup
        }
    }

    public void setSizeText(float fontSize) {
        if (customFont != null) {
            textArea.setFont(customFont.deriveFont(Font.BOLD, fontSize));
        } else {
            textArea.setFont(new Font("Roboto", Font.BOLD, (int) fontSize));
        }
        SwingUtilities.invokeLater(() -> repaint());
    }

    public void setColorText(Color color) {
        textArea.setForeground(color);
        SwingUtilities.invokeLater(() -> repaint()); // Refresh the panel to apply the color change
    }

    public void setSizeImage(int widthImage, int heightImage) {
        this.widthImage = widthImage;
        this.heightImage = heightImage;
    }

    public void setLocationButton(int x, int y) {
        super.setLocation(x, y); // This will set the panel location within its container
        this.locationX = x;
        this.locationY = y;
    }

    public void updateText(String text) {
        textArea.setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(textboxImage, locationX, locationY, widthImage, heightImage, this);
    }
}
