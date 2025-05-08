package galactictypist.components.panels.game;

import galactictypist.MainManager;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ainzle
 */
public class ButtonStoryAnswerPanel extends JPanel implements MouseListener, MouseMotionListener {

    private Image originalImage;
    private Image hoverImage;
    private Image currentImage;
    private JLabel textLabel; // JLabel for displaying text
    private boolean insideImageBounds;
    private ActionListener actionListener;

    int buttonWidth;
    int buttonHeight;

    private Font customFont;

    public ButtonStoryAnswerPanel(String imagePath, String hoverImagePath, int buttonWidth, int buttonHeight, ActionListener actionListener) {
        this.originalImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        this.hoverImage = new ImageIcon(getClass().getResource(hoverImagePath)).getImage();
        this.currentImage = originalImage;
        this.actionListener = actionListener;

        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;

        // Set the size of the panel to match the button's size
        this.setPreferredSize(new Dimension(buttonWidth, buttonHeight));

        addMouseListener(this);
        addMouseMotionListener(this);

        // Load the custom font
        loadCustomFont();

        // Initialize the JLabel for displaying text
        textLabel = new JLabel("", SwingConstants.CENTER);
        textLabel.setForeground(java.awt.Color.WHITE); // Set text color
        textLabel.setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 28));
        textLabel.setBounds(0, 0, buttonWidth, buttonHeight); // Set bounds for JLabel

        setLayout(null);
        add(textLabel);
    }

    public void hideImage() {
        setVisible(false);
        SwingUtilities.invokeLater(() -> repaint());
    }

    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/square_sans_serif_7.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 21f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);  // Register the font
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... ButtonStoryNextPanel Story", e);  // Log the error if any  // Log the error if any
            customFont = null;  // Set customFont to null to trigger fallback in table setup
        }
    }

    public void setSizeText(float fontSize) {
        if (customFont != null) {
            // Create a new font with the specified size and set it to textLabel
            Font resizedFont = customFont.deriveFont(Font.BOLD, fontSize);
            textLabel.setFont(resizedFont); // Apply the resized font to the JLabel
        } else {
            // Fallback if customFont is null (use default font)
            textLabel.setFont(new Font("Roboto", Font.BOLD, (int) fontSize));
        }
        SwingUtilities.invokeLater(() -> repaint()); // Refresh the component to apply the new font
    }

    public void updateText(String text) {
        // Wrapping the text in HTML tags with <br> for line breaks
        String htmlText = "<html><center>" + text.replace("\n", "<br>") + "</center></html>";
        textLabel.setText(htmlText); // Update text in JLabel
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the button image
        g.drawImage(currentImage, 0, 0, buttonWidth, buttonHeight, this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Check if the click is within the bounds of the button
        if (isInside(e.getX(), e.getY()) && actionListener != null) {
            actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        insideImageBounds = true;
        currentImage = hoverImage; // Change to hover image on mouse enter
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        insideImageBounds = false;
        currentImage = originalImage; // Revert to original image on mouse exit
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isInside(e.getX(), e.getY()) && !insideImageBounds) {
            mouseEntered(e);
        } else if (!isInside(e.getX(), e.getY()) && insideImageBounds) {
            mouseExited(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    // Helper method to check if the mouse is within the button bounds
    private boolean isInside(int mouseX, int mouseY) {
        int width = getWidth();
        int height = getHeight();
        return mouseX >= 0 && mouseX <= width && mouseY >= 0 && mouseY <= height;
    }
}
