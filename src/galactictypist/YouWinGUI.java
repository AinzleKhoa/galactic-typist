package galactictypist;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;

import galactictypist.components.panels.youwin.BackgroundMenuPanel;
import galactictypist.components.labels.youwin.CongratulationLabel;
import galactictypist.components.panels.youwin.StatusOverlayPanel;
import galactictypist.components.labels.youwin.ToBeContinuedLabel;
import galactictypist.components.labels.youwin.YouWinLabel;
import galactictypist.components.panels.youwin.ButtonMenuPanel;
import javax.swing.SwingUtilities;

public class YouWinGUI extends JFrame {

    private MainManager manager;
    private BackgroundMenuPanel backgroundMenuPanel;
    private ToBeContinuedLabel toBeContinuedLabel;
    private CongratulationLabel congratulationLabel;
    private YouWinLabel youWinLabel;

    private StatusOverlayPanel statusOverlayPanel;
    private JPanel exitButton;

    private final int frameWidth = 1280;
    private final int frameHeight = 720;
    private final int buttonWidth = 300;
    private final int buttonHeight = 60;

    private JLabel livesStatusLabel = new JLabel();
    private JLabel scoreStatusLabel = new JLabel();
    private JLabel mistakesStatusLabel = new JLabel();
    private JLabel streakStatusLabel = new JLabel();
    private JLabel accuracyStatusLabel = new JLabel();
    private JLabel difficultyLabel = new JLabel();

    public YouWinGUI(MainManager manager) {
        this.manager = manager;
        
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

        // Load background with error handling
        try {
            backgroundMenuPanel = new BackgroundMenuPanel("/resources/images/youwin.png", frameWidth, frameHeight);
            backgroundMenuPanel.setBounds(0, 0, frameWidth, frameHeight);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading background image.");
        }

        // Initialize labels and add error handling
        try {
            youWinLabel = new YouWinLabel();
            youWinLabel.setBounds(520, 30, 600, 200);

            String playerName = manager.getPlayerManager().getPlayerName();
            congratulationLabel = new CongratulationLabel(playerName);
            congratulationLabel.setBounds(350, 80, 1000, 200);

            toBeContinuedLabel = new ToBeContinuedLabel();
            toBeContinuedLabel.setBounds(530, 120, 600, 200);
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
        jPane.add(youWinLabel, Integer.valueOf(1));
        jPane.add(congratulationLabel, Integer.valueOf(1));
        jPane.add(toBeContinuedLabel, Integer.valueOf(1));
        jPane.add(statusOverlayPanel, Integer.valueOf(1));
        jPane.add(exitButton, Integer.valueOf(1));

        // Make the window visible after setting up all components
        setVisible(true);
    }

    // Method to handle returning to the main menu safely
    private void disposeAndReturnToMenu() {
        SwingUtilities.invokeLater(() -> {
            YouWinGUI.this.dispose();
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
