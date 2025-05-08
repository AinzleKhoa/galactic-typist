package galactictypist;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MainManager extends JFrame {

    private static Logger logger = Logger.getLogger(MainManager.class.getName());

    private PlayerManager playerManager;
    private WordManager wordManager;

    private MenuGUI menu;
    private GameGUI game;
    private ConfigGUI config;
    private DifficultyGUI difficulty;
    private StoryGUI story;
    private InfoGUI info;

    private Player playerData;

    private int difficultyMode;  // Store the selected difficulty mode

    public MainManager() {
        setupLogger();
        // Initialize the PlayerManager first
        playerManager = new PlayerManager(this);
        wordManager = new WordManager();
        playerManager.setWordManager(wordManager);
        wordManager.setPlayerManager(playerManager);

        // Create the menu GUI
        menu = new MenuGUI(this);
        wordManager.createSaveLoadWordFile();
    }

    public WordManager getWordManager() {
        return wordManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public GameGUI getGameGUI() {
        return game;
    }

    public void inputHandler(String input) {
        wordManager.processTextInput(input);
    }

    //============================== LOG =======================================
    // Method to set up the logger
    private void setupLogger() {
        try {
            // Create a FileHandler that writes to "app.log"
            FileHandler fileHandler = new FileHandler("app.log", 1000000, 3, true); // Limit to 1MB per file, 3 backups
            fileHandler.setFormatter(new SimpleFormatter()); // Set a simple format

            // Attach the handler to the logger
            logger.addHandler(fileHandler);

            // Optionally, you can set the logging level here (INFO, WARNING, etc.)
            logger.setLevel(Level.ALL); // Log all levels (fine-grained to severe)
        } catch (IOException e) {
            errorDisplayAndLog("Error setting up logger: " + e.getMessage(), e);
        }
    }

    // Static methods for logging from other classes
    public static void logInfo(String message) {
        logger.log(Level.INFO, message);
    }

    public static void logWarning(String message) {
        logger.log(Level.WARNING, message);
    }

    public static void logError(String message, Exception e) {
        logger.log(Level.SEVERE, message, e);
    }

    public static void infoDisplayAndLog(String message) {
        logInfo(message); // Log info message
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "New Message", JOptionPane.INFORMATION_MESSAGE));
    }

    public static void warningDisplayAndLog(String message) {
        logWarning(message); // Log info message
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE));
    }

    public static void errorDisplayAndLog(String message, Exception e) {
        if (e != null) {
            logger.log(Level.SEVERE, message, e);
        } else {
            logger.log(Level.SEVERE, message);
        }
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE));
    }

    //===================================================== STORY MODE =========
    public void difficultyChoose() {
        SwingUtilities.invokeLater(() -> {
            menu.dispose();
            difficulty = new DifficultyGUI(this);
        });
    }

    //Will be called from difficutlyGUI
    public void storyBeforeStart() {
        SwingUtilities.invokeLater(() -> {
            difficulty.dispose();
            story = new StoryGUI(this);
        });
    }

    //WIll be called from storyGUI
    public void storyMode() {
        SwingUtilities.invokeLater(() -> {
            JTextField nameField = new JTextField();

            Object[] fields = {
                "Name:", nameField
            };

            int option;
            do {
                option = JOptionPane.showConfirmDialog(this, fields, "Player Name", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String name = nameField.getText().trim().toUpperCase();
                    if (name.isEmpty()) { // If left empty
                        JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
                    } else if (name.contains(" ")) {
                        JOptionPane.showMessageDialog(null, " No spaces are allowed in the name.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
                    } else if (name.length() > 6) {
                        JOptionPane.showMessageDialog(null, "Name must be 6 characters or fewer.", "Empty Field", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // Check if player has completed any of the gamemodes 1, 2, or 3
                        if (playerManager.hasPlayerAlreadyBeatenStoryModes(name)) {
                            handlePlayerCompletedStoryModes(name);
                            break;
                        } else {
                            Player newPlayer = new Player(name);
                            playerManager.addPlayer(newPlayer);
                            // Create and open a new game window (for the actual gameplay)
                            story.dispose();
                            game = new GameGUI(this, difficultyMode);

                            break;
                        }
                    }
                } else {
                    //Do nothing since the story page is still there
                    break;
                }
            } while (true);
        });
    }
    //================================================ ENDLESS MODE ============

    public void endlessMode() {
        SwingUtilities.invokeLater(() -> {
            JTextField nameField = new JTextField();

            Object[] fields = {
                "Name:", nameField
            };

            difficultyMode = 0; //Initilize to endless mode difficulty
            int option;
            do {
                option = JOptionPane.showConfirmDialog(this, fields, "Player Name", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String name = nameField.getText().trim().toUpperCase();
                    if (name.isEmpty()) { // If left empty
                        JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Incomplete Form", JOptionPane.WARNING_MESSAGE);
                    } else if (name.contains(" ")) {
                        JOptionPane.showMessageDialog(null, " No spaces are allowed in the name.", "Invalid Name", JOptionPane.ERROR_MESSAGE);
                    } else if (name.length() > 6) {
                        JOptionPane.showMessageDialog(null, "Name must be 6 characters or fewer.", "Empty Field", JOptionPane.WARNING_MESSAGE);
                    } else {
                        if (playerManager.hasPlayerAlreadyBeatenThisGameMode(name, 0)) { // If player existed, 0 is for ENDLESS
                            handlePlayerCompletedEndlessGameMode(name);
                            break;
                        } else {
                            Player newPlayer = new Player(name, 0);
                            playerManager.addPlayer(newPlayer);
//                         Create and open a new game window (for the actual gameplay)
                            menu.dispose();
                            game = new GameGUI(this, difficultyMode);
                            break;
                        }
                    }
                } else {
                    //Do nothing since the menu is still there
                    break;
                }
            } while (true);
        });
    }

    //=====================================================SAVE & EXIT==========
    public void saveAndExit() {
        SwingUtilities.invokeLater(() -> {
            saveGame();
            playerManager.printOut();
            System.exit(0);
        });
    }

    //===================================================SCOREBOARD & SETTINGS==
    public void scoreboardAndConfig() {
        SwingUtilities.invokeLater(() -> {
            menu.dispose();
            config = new ConfigGUI(playerManager, wordManager, this);
        });
    }

    //===================================================INFO====================
    public void info() {
        SwingUtilities.invokeLater(() -> {
            menu.dispose();
            info = new InfoGUI(this);
        });
    }

    //=====================================================BACK TO MENU=========
    public void backToMenu() {
        SwingUtilities.invokeLater(() -> menu = new MenuGUI(this));
    }

    //=================================================SAVE GAME================
    public void saveGame() {
        try {
            playerManager.saveGame();
            wordManager.saveWordFile();
        } catch (IOException e) {
            // Handle IOExceptions such as issues in file creation, writing, or copying
            errorDisplayAndLog("Error saving game data: " + e.getMessage(), e);
        }
    }

    //================ Existed Player for the game mode handler ================
    public void handlePlayerCompletedEndlessGameMode(String name) {
        SwingUtilities.invokeLater(() -> {
            int option = JOptionPane.showConfirmDialog(this, "This player has already completed the endless game mode\n"
                    + "Do you want to overwrite this player's save file and start again for endless mode?",
                    "Game Mode Already Beaten", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                playerManager.deletePlayerAlreadyBeatenThisGameMode(name, 0);
                Player newPlayer = new Player(name, 0);
                playerManager.addPlayer(newPlayer);
                menu.dispose();
                game = new GameGUI(this, difficultyMode); // Start game after deleting old data
            } else {
                //Do nothing, since the JOption Enter Name is still there
            }
        });
    }

    public void handlePlayerCompletedStoryModes(String name) {
        SwingUtilities.invokeLater(() -> {
            String storyModeDifficulty;
            switch (playerManager.getDifficultyPlayerBeatenStoryModes(name)) {
                case -1:
                    storyModeDifficulty = "None";
                    break;
                case 1:
                    storyModeDifficulty = "Easy";
                    break;
                case 2:
                    storyModeDifficulty = "Medium";
                    break;
                case 3:
                    storyModeDifficulty = "Hard";
                    break;
                default:
                    storyModeDifficulty = "Unknown";
                    break;
            }
            int option;
            if (playerManager.getDifficultyPlayerBeatenStoryModes(name) == -1) {
                option = JOptionPane.showConfirmDialog(this,
                        "This player has not yet completed any of the story modes difficulty: " + storyModeDifficulty + ".\n"
                        + "Do you want to overwrite this player's save file and start again for story mode?",
                        "Story Mode Haven't Beaten",
                        JOptionPane.OK_CANCEL_OPTION);
            } else {
                option = JOptionPane.showConfirmDialog(this,
                        "This player has already completed one of the story modes difficulty: " + storyModeDifficulty + ".\n"
                        + "Do you want to overwrite this player's save file and start again for story mode?",
                        "Story Mode Already Beaten",
                        JOptionPane.OK_CANCEL_OPTION);
            }
            if (option == JOptionPane.OK_OPTION) {
                playerManager.deletePlayerAlreadyBeatenStoryModes(name);
                Player newPlayer = new Player(name);
                playerManager.addPlayer(newPlayer);
                story.dispose();
                game = new GameGUI(this, difficultyMode); // Start the game after overwriting
            } else {
                //Do nothing, since the JOption Enter Name is still there
            }
        });
    }

    public void setDifficultyMode(int difficultyMode) {
        this.difficultyMode = difficultyMode;
        logInfo("Current Difficulty: " + difficultyMode);
    }

    public int getDifficultyMode() {
        return difficultyMode;
    }
}
