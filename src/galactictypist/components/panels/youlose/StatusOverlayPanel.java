package galactictypist.components.panels.youlose;

import galactictypist.MainManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class StatusOverlayPanel extends JPanel {

    private Image textboxImage;
    private int locationX = 0;
    private int locationY = 160;
    private int widthImage = 400;
    private int heightImage = 460;

    private JLabel livesStatusLabel;
    private JLabel scoreStatusLabel;
    private JLabel mistakesStatusLabel;
    private JLabel streakStatusLabel;
    private JLabel accuracyStatusLabel;
    private JLabel difficultyStatusLabel;

    private Font customFont;

    public StatusOverlayPanel(String imagePath, JLabel livesStatusLabel, JLabel scoreStatusLabel, JLabel mistakesStatusLabel, JLabel streakStatusLabel, JLabel accuracyStatusLabel, JLabel difficultyStatusLabel) {
        setOpaque(false);
        setLayout(null);

        this.textboxImage = new ImageIcon(getClass().getResource(imagePath)).getImage();

        this.livesStatusLabel = livesStatusLabel;
        this.scoreStatusLabel = scoreStatusLabel;
        this.mistakesStatusLabel = mistakesStatusLabel;
        this.streakStatusLabel = streakStatusLabel;
        this.accuracyStatusLabel = accuracyStatusLabel;
        this.difficultyStatusLabel = difficultyStatusLabel;

        livesStatusLabel.setVisible(false);
        scoreStatusLabel.setVisible(false);
        mistakesStatusLabel.setVisible(false);
        streakStatusLabel.setVisible(false);
        accuracyStatusLabel.setVisible(false);
        difficultyStatusLabel.setVisible(false);

        add(livesStatusLabel);
        add(scoreStatusLabel);
        add(mistakesStatusLabel);
        add(streakStatusLabel);
        add(accuracyStatusLabel);
        add(difficultyStatusLabel);

        loadCustomFont();
        setFont(customFont != null ? customFont : new Font("Roboto", Font.BOLD, 16));
    }

    private void loadCustomFont() {
        try {
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/square_sans_serif_7.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 19f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
            }
        } catch (FontFormatException | IOException e) {
            MainManager.logError("Error with loading the font... StatusOverlayPanel YouLose", e);
            customFont = null;
        }
    }

    private BufferedImage createTextShadow(String text, Font font, Color shadowColor) {
        int width = getFontMetrics(font).stringWidth(text) + 4;
        int height = getFontMetrics(font).getHeight() + 4;
        BufferedImage shadowImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = shadowImage.createGraphics();
        g2d.setFont(font);
        g2d.setColor(shadowColor);

        // Draw shadow in multiple directions for a thicker effect
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

    public void updateStatus(String accuracy, int score, int lives, int mistakes, int streak, int difficulty) {
        accuracyStatusLabel.setText("Accuracy: " + accuracy);
        scoreStatusLabel.setText("Score: " + score);
        livesStatusLabel.setText("Lives: " + lives);
        mistakesStatusLabel.setText("Mistakes: " + mistakes);
        streakStatusLabel.setText("Streak: " + streak);

        String difficultyText;
        switch (difficulty) {
            case 1:
                difficultyText = "Easy";
                break;
            case 2:
                difficultyText = "Medium";
                break;
            case 3:
                difficultyText = "Hard";
                break;
            default:
                difficultyText = "Endless";
                break;
        }
        difficultyStatusLabel.setText("Difficulty: " + difficultyText);

        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (textboxImage != null) {
            g.drawImage(textboxImage, locationX, locationY, widthImage, heightImage, this);
        }

        int initialStartY = 135;
        int spacingY = 40;
        int marginX = 80;

        // Draw each label with shadow
        drawTextWithShadow(g, accuracyStatusLabel.getText(), locationX + marginX, locationY + initialStartY);
        drawTextWithShadow(g, scoreStatusLabel.getText(), locationX + marginX, locationY + initialStartY + spacingY * 1);
        drawTextWithShadow(g, livesStatusLabel.getText(), locationX + marginX, locationY + initialStartY + spacingY * 2);
        drawTextWithShadow(g, mistakesStatusLabel.getText(), locationX + marginX, locationY + initialStartY + spacingY * 3);
        drawTextWithShadow(g, streakStatusLabel.getText(), locationX + marginX, locationY + initialStartY + spacingY * 4);
        drawTextWithShadow(g, difficultyStatusLabel.getText(), locationX + marginX, locationY + initialStartY + spacingY * 5);
    }

    private void drawTextWithShadow(Graphics g, String text, int x, int y) {
        if (customFont == null) {
            customFont = new Font("Roboto", Font.BOLD, 16);  // Fallback font
        }

        // Create shadow image
        BufferedImage shadow = createTextShadow(text, customFont, Color.DARK_GRAY);

        // Draw shadow image
        g.drawImage(shadow, x - 2, y - shadow.getHeight() + 2, this);

        // Draw actual text on top
        g.setFont(customFont);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);
    }
}
