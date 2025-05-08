package galactictypist;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;

import galactictypist.components.panels.youlose.BackgroundMenuPanel;
import galactictypist.components.labels.youlose.ComfortLabel;
import galactictypist.components.panels.youlose.StatusOverlayPanel;
import galactictypist.components.labels.youlose.EliminatedByWhoLabel;
import galactictypist.components.labels.youlose.YouLoseLabel;
import galactictypist.components.panels.youlose.ButtonMenuPanel;
import javax.swing.SwingUtilities;

public class YouLoseGUI extends JFrame {

    private MainManager manager;
    private BackgroundMenuPanel backgroundMenuPanel;
    private EliminatedByWhoLabel eliminatedByWhoLabel;
    private ComfortLabel comfortLabel;
    private YouLoseLabel youLoseLabel;

    private StatusOverlayPanel statusOverlayPanel;
    private JPanel exitButton;

    private final int frameWidth = 1280;
    private final int frameHeight = 720;
    private final int buttonWidth = 300;
    private final int buttonHeight = 60;

    private final int centerX = (frameWidth - buttonWidth) / 2;
    private final int centerY = (frameHeight - buttonHeight) / 2;

    private int eliminatedByWho;

    private JLabel livesStatusLabel = new JLabel();
    private JLabel scoreStatusLabel = new JLabel();
    private JLabel mistakesStatusLabel = new JLabel();
    private JLabel streakStatusLabel = new JLabel();
    private JLabel accuracyStatusLabel = new JLabel();
    private JLabel difficultyLabel = new JLabel();

    public YouLoseGUI(MainManager manager, int eliminatedByWho) {
        this.manager = manager;
        this.eliminatedByWho = eliminatedByWho;

        // Initialize the GUI in a separate thread to avoid UI freeze
        SwingUtilities.invokeLater(this::setupGUI);
    }

    private void setupGUI() {
        setTitle("Galactic Typist");
        setSize(frameWidth, frameHeight);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disposeAndReturnToMenu();
            }
        });

        JLayeredPane jPane = getLayeredPane();

        // Load background based on the player's level with error handling
        try {
            if (manager.getPlayerManager().getPlayerLevel() != 0) {
                backgroundMenuPanel = new BackgroundMenuPanel("/resources/images/youlose.png", frameWidth, frameHeight);
            } else {
                backgroundMenuPanel = new BackgroundMenuPanel("/resources/images/gameover.png", frameWidth, frameHeight);
            }
            backgroundMenuPanel.setBounds(0, 0, frameWidth, frameHeight);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading background image.");
        }

        // Initialize labels and add error handling
        try {
            youLoseLabel = new YouLoseLabel(manager.getPlayerManager().getPlayerLevel());
            youLoseLabel.setBounds(510, 30, frameWidth, frameHeight);

            String playerName = manager.getPlayerManager().getPlayerName();
            comfortLabel = new ComfortLabel(playerName);
            comfortLabel.setBounds(350, 80, frameWidth, frameHeight);

            eliminatedByWhoLabel = new EliminatedByWhoLabel(eliminatedByWho);
            eliminatedByWhoLabel.setBounds(470, 120, frameWidth, frameHeight);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error initializing labels.");
        }

        // Initialize the status overlay panel
        try {
            statusOverlayPanel = new StatusOverlayPanel(
                    "/resources/images/statusOverlay.png",
                    livesStatusLabel,
                    scoreStatusLabel,
                    mistakesStatusLabel,
                    streakStatusLabel,
                    accuracyStatusLabel,
                    difficultyLabel
            );
            statusOverlayPanel.setBounds(0, 0, frameWidth, frameHeight);
            updateStatusOverlay();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error initializing status overlay.");
        }

        // Setup the exit button with error handling
        try {
            exitButton = new ButtonMenuPanel(
                    "/resources/images/button-frame.png",
                    "/resources/images/button-frame-hover.png",
                    "Back to menu",
                    buttonWidth,
                    buttonHeight,
                    e -> disposeAndReturnToMenu()
            );
            exitButton.setBounds(620, 600, buttonWidth, buttonHeight);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error setting up exit button.");
        }

        // Add all components to the layered pane
        jPane.add(backgroundMenuPanel, Integer.valueOf(0));
        jPane.add(youLoseLabel, Integer.valueOf(1));
        jPane.add(comfortLabel, Integer.valueOf(1));
        jPane.add(eliminatedByWhoLabel, Integer.valueOf(1));
        jPane.add(statusOverlayPanel, Integer.valueOf(1));
        jPane.add(exitButton, Integer.valueOf(1));

        // Make the window visible after setting up all components
        setVisible(true);
    }

    // Method to handle returning to the main menu safely
    private void disposeAndReturnToMenu() {
        SwingUtilities.invokeLater(() -> {
            YouLoseGUI.this.dispose();
            manager.backToMenu();
        });
    }

    // Pass the update logic
    public void updateStatusOverlay() {
        try {
            statusOverlayPanel.updateStatus(
                    manager.getPlayerManager().getPlayerAccuracy(),
                    manager.getPlayerManager().getPlayerScore(),
                    manager.getPlayerManager().getPlayerLives(),
                    manager.getPlayerManager().getPlayerMistakes(),
                    manager.getPlayerManager().getPlayerStreak(),
                    manager.getDifficultyMode()
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating status overlay.");
        }
    }
}
