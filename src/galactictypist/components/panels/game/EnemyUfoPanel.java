/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galactictypist.components.panels.game;

import galactictypist.MainManager;
import galactictypist.Word;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Ainzle
 */
public class EnemyUfoPanel extends JPanel {

    private Image enemyImage;
    private List<Word> activeWordsUFO;
    private Font customFont;

    public EnemyUfoPanel(String ImagePath, List<Word> activeWordsUFO) {
        this.enemyImage = new ImageIcon(getClass().getResource(ImagePath)).getImage();
        this.activeWordsUFO = activeWordsUFO;
        setOpaque(false); // Transparency for enemy UFO.
        // Load the custom font
        loadCustomFont();
        setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 35));
    }

    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/visitor1.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 40f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);  // Register the font
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... EnemyArPanel Game", e);  // Log the error if any  // Log the error if any
            customFont = null;  // Set customFont to null to trigger fallback in table setup
        }
    }

    private BufferedImage createTextShadow(String text, Font font, Color shadowColor) {
        int width = getFontMetrics(font).stringWidth(text) + 4;
        int height = getFontMetrics(font).getHeight() + 4;
        BufferedImage shadowImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = shadowImage.createGraphics();
        g2d.setFont(font);
        g2d.setColor(shadowColor);

        // Draw shadow in multiple directions for thicker effect
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) {
                    g2d.drawString(text, 2 + dx, height - 2 + dy);
                }
            }
        }
        g2d.dispose();
        return shadowImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        synchronized (activeWordsUFO) {
            for (Word word : activeWordsUFO) {
                // Draw the enemy image for each word
                g.drawImage(enemyImage, word.getX() - 10, word.getY() - 10, 300, 200, this); // Draw enemy image with some offset

                // Draw the shadow image
                BufferedImage shadow = createTextShadow(word.getText(), customFont, Color.DARK_GRAY);
                g.drawImage(shadow, word.getX() - 2, word.getY() - shadow.getHeight() + 2, this);

                // Draw the main text
                g.setColor(word.getTextColor());
                g.drawString(word.getText(), word.getX(), word.getY());
            }
        }
    }
}
