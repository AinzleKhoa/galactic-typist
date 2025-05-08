/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galactictypist.components.panels.game;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ainzle
 */
public class ExplosionBossDefeatedPanel5 extends JPanel {

    private Image theImage;
    private int centerX;
    private int centerY;
    private int widthImage;
    private int heightImage;

    public ExplosionBossDefeatedPanel5(String ImagePath) {
        this.theImage = new ImageIcon(getClass().getResource(ImagePath)).getImage();
        setOpaque(false);
    }

    //If the boss is set to center at default
    public void showImage(int widthImage, int heightImage) {
        this.widthImage = widthImage;
        this.heightImage = heightImage;

        this.centerX = (1600 - widthImage) / 2;
        this.centerY = (900 - heightImage) / 2;
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    public void hideImage() {
        SwingUtilities.invokeLater(() -> setVisible(false));
    }

    public void showImage() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isVisible()) {
            g.drawImage(theImage, centerX, centerY, widthImage, heightImage, this);
        }
    }
}
