package galactictypist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

class PlayerManager {

    private static final String FILE_NAME = "playerSaveFile.dat";
    private static final String BACKUP_FILE_NAME = "playerSaveFile_Backup.dat";

    private List<Player> players;
    private Player currentPlayer;  // This will store the active player
    private WordManager wordManager;
    private MainManager manager;

    // Constructor
    public PlayerManager(MainManager manager) {
        players = new ArrayList<>();
        this.manager = manager;
        initializeGameData();
    }

    private void initializeGameData() {
        // Check if the main game file exists
        if (isFileExists(FILE_NAME)) {
            try {
                // Attempt to load the main game file
                loadGame();
                MainManager.logInfo("Main game file loaded successfully.");
            } catch (IOException | ClassNotFoundException e) {
                MainManager.warningDisplayAndLog("Main game file corrupted. Attempting to load the backup file...");
                // If the main file is corrupted, attempt to load the backup
                handleBackupFile();
            }
        } else {
            MainManager.warningDisplayAndLog("Main game file not found. Creating new game file... Attempt to load from backup file...");
            try {
                // Create a new game file if the main one doesn't exist
                startCreateFile();
                handleBackupFile();
                loadGame();
            } catch (IOException | ClassNotFoundException e) {
                MainManager.errorDisplayAndLog("Error creating game data: " + e.getMessage(), e);
            }
        }
    }

    private void handleBackupFile() {
        // Check if the backup game file exists
        if (isFileExists(BACKUP_FILE_NAME)) {
            try {
                // Attempt to load the backup file
                LoadBackupFile();
                MainManager.logInfo("Backup file loaded successfully.");
            } catch (IOException e) {
                MainManager.warningDisplayAndLog("Backup file corrupted. Creating new game file...");
                try {
                    // If the backup file is corrupted, which meant the main file is corrupted. Attempt to reset and create new game file for both
                    startResetFile();
                } catch (IOException e1) {
                    MainManager.errorDisplayAndLog("Error resetting and creating game data: " + e1.getMessage(), e);
                }
            }
        } else {
            MainManager.warningDisplayAndLog("Backup file not found. Creating new game file...");
            try {
                // If the backup file is not found, attempt to create new game file for backup file
                startCreateFile();
            } catch (IOException e) {
                MainManager.errorDisplayAndLog("Error creating game data: " + e.getMessage(), e);
            }
        }
    }

    public int getDifficultyPlayerBeatenStoryModes(String name) {
        for (int gamemode : new int[]{-1, 1, 2, 3}) { // 0 is for endless mode. -1 is for not completing any difficulty
            if (hasPlayerAlreadyBeatenThisGameMode(name, gamemode)) {
                return gamemode;
            }
        }
        return -1;
    }

    public boolean hasPlayerAlreadyBeatenStoryModes(String name) {
        for (int gamemode : new int[]{-1, 1, 2, 3}) {
            if (hasPlayerAlreadyBeatenThisGameMode(name, gamemode)) {
                return true;
            }
        }
        return false;
    }

    public void deletePlayerAlreadyBeatenStoryModes(String name) {
        for (int gamemode : new int[]{-1, 1, 2, 3}) { //Loop through -1 1 2 3 and delete players
            deletePlayerAlreadyBeatenThisGameMode(name, gamemode);
        }
    }

    public boolean hasPlayerAlreadyBeatenThisGameMode(String name, int gamemode) {
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name) && player.getCompletedGameMode() == gamemode) {
                return true;
            }
        }
        return false;
    }

    public void deletePlayerAlreadyBeatenThisGameMode(String name, int gamemode) {
        players.removeIf(player -> player.getName().equalsIgnoreCase(name) && player.getCompletedGameMode() == gamemode);
    }

    public void setPlayerCompletedGameMode(int gamemode) {
        currentPlayer.setCompletedGameMode(gamemode);
    }

    public int getPlayerCompletedGameMode() {
        return currentPlayer.getCompletedGameMode();
    }

    public String getPlayerCompletedGameModeInString() {
        return currentPlayer.getCompletedGameModeInString();
    }

    public void setWordManager(WordManager wordManager) {
        this.wordManager = wordManager;
    }

    public String getPlayerName() {
        return currentPlayer.getName();
    }

    public int getPlayerScore() {
        return currentPlayer.getScore();
    }

    public int getPlayerLives() {
        return currentPlayer.getLives();
    }

    public int getPlayerLevel() {
        return currentPlayer.getLevel();
    }

    public int getPlayerMistakes() {
        return currentPlayer.getMistakes();
    }

    public int getPlayerStreak() {
        return currentPlayer.getStreak();
    }

    public String getPlayerAccuracy() {
        return currentPlayer.getFormattedAccuracy();
    }

    public void decrementPlayerLives() {
        currentPlayer.decrementLives(1);
    }

    public void incrementPlayerLevel() {
        currentPlayer.incrementLevel(1);
    }

    //debug
    public void printOut() {
        if (!players.isEmpty()) {
            for (Player player : players) {
                System.out.println(player.toString());
            }
        } else {
            System.out.println("Player empty");
        }
    }

    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
        currentPlayer = newPlayer;  // Set the current player when adding a new player
        // Debugging output
        MainManager.logInfo("Player added: " + newPlayer.getName()
                + ", Lives: " + newPlayer.getLives()
                + ", Score: " + newPlayer.getScore()
                + ", Level: " + newPlayer.getLevel()
                + ", Accuracy: " + newPlayer.getFormattedAccuracy() + "%"
                + ", Streak: " + newPlayer.getStreak());
    }

    public void deletePlayer(int index) {
        if (index >= 0 && index < players.size()) {
            players.remove(index);
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void correctInput() {
        if (currentPlayer == null) {
            MainManager.errorDisplayAndLog("Current player not initialized.", null);
            return;
        }
        manager.getGameGUI().updateInputValidationStatus(1);
        currentPlayer.incrementTotalWordTyped(1);
        currentPlayer.incrementScore(1);
        currentPlayer.incrementStreak(1);
        MainManager.logInfo("Correct!");
//                + ", Lives: " + currentPlayer.getLives()
//                + ", Score: " + currentPlayer.getScore()
//                + ", Level: " + currentPlayer.getLevel()
//                + ", Accuracy: " + currentPlayer.getFormattedAccuracy() + "%"
//                + ", Streak: " + currentPlayer.getStreak());
    }

    public void wrongInput() {
        if (currentPlayer == null) {
            MainManager.errorDisplayAndLog("Current player not initialized.", null);
            return;
        }
        manager.getGameGUI().updateInputValidationStatus(0);
        currentPlayer.incrementTotalWordTyped(1);
        currentPlayer.incrementMistakes(1);
        currentPlayer.setStreak(0);
        MainManager.logInfo("Mistakes!");
//                + ", Lives: " + currentPlayer.getLives()
//                + ", Score: " + currentPlayer.getScore()
//                + ", Level: " + currentPlayer.getLevel()
//                + ", Accuracy: " + currentPlayer.getFormattedAccuracy() + "%"
//                + ", Streak: " + currentPlayer.getStreak());
    }

    private boolean isFileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    private void LoadBackupFile() throws IOException {
        File gameFile = new File(FILE_NAME);
        File backupFile = new File(BACKUP_FILE_NAME);

        copyFile(backupFile, gameFile); //Exception could be thrown here
        MainManager.logInfo("Backup loaded.");
    }

    private void startCreateFile() throws IOException {
        //If main file haven't existed
        if (!isFileExists(FILE_NAME)) {
            createFile(FILE_NAME);
        }
        //If backup file haven't existed
        if (!isFileExists(BACKUP_FILE_NAME)) {
            createFile(BACKUP_FILE_NAME);
        }
    }

    private void startResetFile() throws IOException {
        resetFile(FILE_NAME);
        resetFile(BACKUP_FILE_NAME);

        // After resetting, create both files again
        createFile(FILE_NAME);
        createFile(BACKUP_FILE_NAME);
    }

    private void resetFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            if (file.delete()) {
                MainManager.logInfo("Deleted existing file: " + fileName);
            } else {
                throw new IOException();
            }
        }
    }

    private void createFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (file.createNewFile()) { //Exception could be thrown here
            MainManager.logInfo("Successfully create file: " + fileName);
        }
    }

    public void saveGame() throws IOException {
        File gameFile = new File(FILE_NAME);
        File backupFile = new File(BACKUP_FILE_NAME);

        if (gameFile.exists()) {
            copyFile(gameFile, backupFile); //Exception could be thrown here
            MainManager.logInfo("Backup created.");
        }

        //SaveGame
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) { //Exception could be thrown here
            oos.writeObject(players);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadGame() throws IOException, ClassNotFoundException {
        File gameFile = new File(FILE_NAME);

        // Check if the file is empty before attempting to read
        if (gameFile.length() == 0) {
            MainManager.warningDisplayAndLog("Game file is empty. Starting fresh.");
            return;  // Exit early since there is no valid data
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) { //Exception could be thrown here
            players = (List<Player>) ois.readObject();  // Load the list of players and cast it
            if (!players.isEmpty()) { //First time loading issue
                currentPlayer = players.get(0);  // Set the current player to the first player
            } else {
                currentPlayer = null;  // If the file is empty, avoid null issues later
                MainManager.warningDisplayAndLog("No players found in the file. Start fresh.");
            }
        }
    }

    // Helper method to copy a file
    private void copyFile(File source, File dest) throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
                FileOutputStream fos = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }
}
