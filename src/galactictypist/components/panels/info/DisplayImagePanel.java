/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galactictypist.components.panels.info;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ainzle
 */
public class DisplayImagePanel extends JPanel {

    private Image theImage;
    private int locationX;
    private int locationY;
    private int widthImage;
    private int heightImage;

    public DisplayImagePanel(String ImagePath) {
        setImage(ImagePath);
    }

    public void setImage(String ImagePath) {
        this.theImage = new ImageIcon(getClass().getResource(ImagePath)).getImage();
        setOpaque(false);
    }

    public void showImage(int x, int y, int widthImage, int heightImage) {
        this.locationX = x;
        this.locationY = y;
        this.widthImage = widthImage;
        this.heightImage = heightImage;

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
            g.drawImage(theImage, locationX, locationY, widthImage, heightImage, this);
        }
    }
}
