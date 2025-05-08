/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galactictypist.components.panels.difficulty;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Ainzle
 */
public class BackgroundMenuPanel extends JPanel {

    private Image backgroundImage;
    private int frameWidth;
    private int frameHeight;

    public BackgroundMenuPanel(String backgroundImagePath, int frameWidth, int frameHeight) {
        this.backgroundImage = new ImageIcon(getClass().getResource(backgroundImagePath)).getImage();
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        this.setPreferredSize(new Dimension(frameWidth, frameHeight));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
