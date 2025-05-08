/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galactictypist.components.panels.youwin;

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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ainzle
 */
public class ButtonMenuPanel extends JPanel implements MouseListener, MouseMotionListener {

    private Image originalImage;
    private Image hoverImage;
    private Image currentImage;
    private String text;
    private boolean insideImageBounds;
    private ActionListener actionListener;

    int buttonWidth;
    int buttonHeight;

    private Font customFont;

    public ButtonMenuPanel(String imagePath, String hoverImagePath, String text, int buttonWidth, int buttonHeight, ActionListener actionListener) {
        this.originalImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        this.hoverImage = new ImageIcon(getClass().getResource(hoverImagePath)).getImage();
        this.currentImage = originalImage;
        this.text = text;
        this.actionListener = actionListener;

        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;

        // Set the size of the panel to match the button's size
        this.setPreferredSize(new Dimension(buttonWidth, buttonHeight));

        addMouseListener(this);
        addMouseMotionListener(this);

        // Load the custom font
        loadCustomFont();

        // Set the font to the custom font if available, or fallback to Roboto
        setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 28));
    }

    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/square_sans_serif_7.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 23f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);  // Register the font
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... ButtonMenuPanel YouWin", e);  // Log the error if any  // Log the error if any
            customFont = null;  // Set customFont to null to trigger fallback in table setup
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the button image
        g.drawImage(currentImage, 0, 0, buttonWidth, buttonHeight, this);

        // Draw the text centered within the button
        g.setColor(java.awt.Color.WHITE);  // Ensure the text color is visible
        int textWidth = g.getFontMetrics().stringWidth(text);
        int textX = (this.getWidth() - textWidth) / 2; // Center the text horizontally within the button
        int textY = (this.getHeight() + g.getFontMetrics().getAscent()) / 2 - 5; // Center the text vertically within the button
        g.drawString(text, textX, textY);
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
        SwingUtilities.invokeLater(() -> repaint());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        insideImageBounds = false;
        currentImage = originalImage; // Revert to original image on mouse exit
        SwingUtilities.invokeLater(() -> repaint());
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
