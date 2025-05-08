package galactictypist;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import galactictypist.components.panels.menu.ButtonMenuPanel;
import galactictypist.components.panels.menu.BackgroundMenuPanel;
import galactictypist.components.panels.menu.SmallButtonMenuPanel;
import galactictypist.components.panels.menu.TitleLabel;
import javax.swing.SwingUtilities;

public class MenuGUI extends JFrame {

    private MainManager manager;
    private TitleLabel titleLabel;
    private BackgroundMenuPanel backgroundMenuPanel;
    private ButtonMenuPanel storyModeButton;
    private ButtonMenuPanel endlessModeButton;
    private ButtonMenuPanel exitGameButton;
    private ButtonMenuPanel scoreboardButton;
    private SmallButtonMenuPanel infoButton;

    int frameWidth = 600;
    int frameHeight = 800;
    int buttonWidth = 400;
    int buttonHeight = 80;

    int smallButtonWidth = 50;
    int smallButtonHeight = 50;

    int centerX = (frameWidth - buttonWidth) / 2;
    int centerY = (frameHeight - buttonHeight) / 2;

    public MenuGUI(MainManager manager) {
        this.manager = manager;
        setupGUI();
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
                manager.saveAndExit();
            }
        });

        JLayeredPane jPane = getLayeredPane();

        //Label for the leviathan boss showcase
        titleLabel = new TitleLabel();
        titleLabel.setBounds(10, 10, frameWidth, frameHeight); // Adjust position if needed

        backgroundMenuPanel = new BackgroundMenuPanel("/resources/images/background.png", frameWidth, frameHeight);
        backgroundMenuPanel.setBounds(0, 0, frameWidth, frameHeight);

        storyModeButton = new ButtonMenuPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "Story Mode", buttonWidth, buttonHeight, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> manager.difficultyChoose());
            }
        });
        storyModeButton.setBounds(100, 150, buttonWidth, buttonHeight);

        endlessModeButton = new ButtonMenuPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "Endless Mode", buttonWidth, buttonHeight, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> manager.endlessMode());
            }
        });
        endlessModeButton.setBounds(100, 250, buttonWidth, buttonHeight);

        scoreboardButton = new ButtonMenuPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "Scoreboard/Settings", buttonWidth, buttonHeight, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> manager.scoreboardAndConfig());
            }
        });
        scoreboardButton.setBounds(100, 350, buttonWidth, buttonHeight);

        infoButton = new SmallButtonMenuPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "!", smallButtonWidth, smallButtonHeight, e -> {
            SwingUtilities.invokeLater(() -> {
                MenuGUI.this.dispose();
                manager.info();
            });
        });
        infoButton.setBounds(520, 655, smallButtonWidth, smallButtonHeight);

        exitGameButton = new ButtonMenuPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "Save And Exit", buttonWidth, buttonHeight, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> manager.saveAndExit());
            }
        });
        exitGameButton.setBounds(100, 640, buttonWidth, buttonHeight);

        jPane.add(backgroundMenuPanel, Integer.valueOf(0));
        jPane.add(titleLabel, Integer.valueOf(1));
        jPane.add(storyModeButton, Integer.valueOf(1));
        jPane.add(endlessModeButton, Integer.valueOf(1));
        jPane.add(scoreboardButton, Integer.valueOf(1));
        jPane.add(infoButton, Integer.valueOf(1));
        jPane.add(exitGameButton, Integer.valueOf(1));
    }

}
