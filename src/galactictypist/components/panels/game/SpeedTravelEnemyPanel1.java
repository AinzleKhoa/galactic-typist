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
public class SpeedTravelEnemyPanel1 extends JPanel {

    private Image theImage;

    public SpeedTravelEnemyPanel1(String ImagePath) {
        this.theImage = new ImageIcon(getClass().getResource(ImagePath)).getImage();
        setOpaque(false); // Transparency for spaceship
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
        g.drawImage(theImage, 0, 0, getWidth(), getHeight(), this);
    }
}
