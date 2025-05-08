package galactictypist;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;

import galactictypist.components.panels.difficulty.ButtonMenuPanel;
import galactictypist.components.panels.difficulty.BackgroundMenuPanel;
import javax.swing.SwingUtilities;

public class DifficultyGUI extends JFrame {

    private MainManager manager;
    private BackgroundMenuPanel backgroundMenuPanel;
    private JPanel easyModeButton;
    private JPanel mediumModeButton;
    private JPanel hardModeButton;
    private JPanel exitButton;

    int frameWidth = 600;
    int frameHeight = 800;
    int buttonWidth = 400;
    int buttonHeight = 80;

    int centerX = (frameWidth - buttonWidth) / 2;
    int centerY = (frameHeight - buttonHeight) / 2;

    public DifficultyGUI(MainManager manager) {
        this.manager = manager;
        setupGUI();
    }

    public MainManager getMainManager() {
        return manager;
    }

    private void setupGUI() {
        setTitle("Galactic Typist");
        setSize(600, 800);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwingUtilities.invokeLater(() -> {
                    DifficultyGUI.this.dispose();
                    manager.backToMenu();
                });
            }
        });

        JLayeredPane jPane = getLayeredPane();

        backgroundMenuPanel = new BackgroundMenuPanel("/resources/images/difficultybackground.png", frameWidth, frameHeight);
        backgroundMenuPanel.setBounds(0, 0, frameWidth, frameHeight);

        easyModeButton = new ButtonMenuPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "Easy Mode", buttonWidth, buttonHeight, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    manager.setDifficultyMode(1);
                    manager.storyBeforeStart();
                });
            }
        });
        easyModeButton.setBounds(100, 100, buttonWidth, buttonHeight);

        mediumModeButton = new ButtonMenuPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "Medium Mode", buttonWidth, buttonHeight, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    manager.setDifficultyMode(2);
                    manager.storyBeforeStart();
                });
            }
        });
        mediumModeButton.setBounds(100, 200, buttonWidth, buttonHeight);

        hardModeButton = new ButtonMenuPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "Hard Mode", buttonWidth, buttonHeight, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    manager.setDifficultyMode(3);
                    manager.storyBeforeStart();
                });
            }
        });
        hardModeButton.setBounds(100, 300, buttonWidth, buttonHeight);

        exitButton = new ButtonMenuPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "Back to menu", buttonWidth, buttonHeight, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    DifficultyGUI.this.dispose();
                    manager.backToMenu();
                });
            }
        });
        exitButton.setBounds(100, 640, buttonWidth, buttonHeight);

        jPane.add(backgroundMenuPanel, Integer.valueOf(0));
        jPane.add(easyModeButton, Integer.valueOf(1));
        jPane.add(mediumModeButton, Integer.valueOf(1));
        jPane.add(hardModeButton, Integer.valueOf(1));
        jPane.add(exitButton, Integer.valueOf(1));
    }

}
