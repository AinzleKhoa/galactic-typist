package galactictypist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Ainzle
 */
public class WordManager {

    private PlayerManager playerManager;

    private int x, y;

    private final List<Word> words;
    private final List<Word> currentWordsUFO;
    private final List<Word> currentWordsNS;
    private final List<Word> currentWordsAR;
    private final String FILE_NAME = "wordSaveFile.txt";

    // Constructor to inject the PlayerManager instance
    public WordManager() {
        this.words = new ArrayList<>();  // Initialize the words list
        this.currentWordsUFO = new ArrayList<>();  // Initialize the words list
        this.currentWordsNS = new ArrayList<>();  // Initialize the words list
        this.currentWordsAR = new ArrayList<>();  // Initialize the words list
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void clearCurrentWords() {
        this.currentWordsUFO.clear();
        this.currentWordsNS.clear();
        this.currentWordsAR.clear();
    }

    public void removeExpiredWord(String wordText) {
        synchronized (currentWordsUFO) {
            Iterator<Word> iterator = currentWordsUFO.iterator();
            while (iterator.hasNext()) {
                Word word = iterator.next();
                if (wordText.equalsIgnoreCase(word.getId())) {
                    iterator.remove();  // Use iterator.remove() to avoid ConcurrentModificationException
                    break;
                }
            }
        }
        synchronized (currentWordsNS) {
            Iterator<Word> iterator = currentWordsNS.iterator();
            while (iterator.hasNext()) {
                Word word = iterator.next();
                if (wordText.equalsIgnoreCase(word.getId())) {
                    iterator.remove();  // Use iterator.remove() to avoid ConcurrentModificationException
                    break;
                }
            }
        }
        synchronized (currentWordsAR) {
            Iterator<Word> iterator = currentWordsAR.iterator();
            while (iterator.hasNext()) {
                Word word = iterator.next();
                if (wordText.equalsIgnoreCase(word.getId())) {
                    iterator.remove();  // Use iterator.remove() to avoid ConcurrentModificationException
                    break;
                }
            }
        }
    }

    public void processTextInput(String input) {
        boolean wordMatched = false;  // Track if the word is matched for any enemy

        // Check UFO words
        synchronized (currentWordsUFO) {
            Iterator<Word> iterator = currentWordsUFO.iterator();
            while (iterator.hasNext()) {
                Word word = iterator.next();
                if (input.equalsIgnoreCase(word.getText())) {
                    playerManager.correctInput();
                    word.setMatched(true);
                    iterator.remove();
                    wordMatched = true;
                    break;
                }
            }
        }

        // Check Night Stalker words
        synchronized (currentWordsNS) {
            Iterator<Word> iterator = currentWordsNS.iterator();
            while (iterator.hasNext()) {
                Word word = iterator.next();
                if (input.equalsIgnoreCase(word.getText())) {
                    playerManager.correctInput();
                    word.setMatched(true);
                    iterator.remove();
                    wordMatched = true;
                    break;
                }
            }
        }

        // Check Astral Raider words
        synchronized (currentWordsAR) {
            Iterator<Word> iterator = currentWordsAR.iterator();
            while (iterator.hasNext()) {
                Word word = iterator.next();
                if (input.equalsIgnoreCase(word.getText())) {
                    playerManager.correctInput();
                    word.setMatched(true);
                    iterator.remove();
                    wordMatched = true;
                    break;
                }
            }
        }

        if (!wordMatched) {
            playerManager.wrongInput();
        }
    }

    public Word getRandomWord() {
        if (words.isEmpty()) {
            MainManager.warningDisplayAndLog("The List Words is Empty. Attempt to add predefined words in...");
            insertWordList();
        }

        Random random = new Random();
        int randomIndex = random.nextInt(words.size());
        return words.get(randomIndex);
    }

    public Word addRandomWordForUFO() {
        Random random = new Random();
        x = 150 + random.nextInt(850);  // Random x position
        y = 250 + random.nextInt(250);   // Random y position

        Word randomWord = getRandomWord();  // Get a random word
        Word newWord = new Word(randomWord.getText(), x, y);

        currentWordsUFO.add(newWord);  // Add to UFO words list
        return newWord;
    }

    public Word addRandomWordForNightStalker() {
        Random random = new Random();
        x = 150 + random.nextInt(850);  // Random x position
        y = 250 + random.nextInt(250);   // Random y position

        Word randomWord = getRandomWord();  // Get a random word
        Word newWord = new Word(randomWord.getText(), x, y);

        currentWordsNS.add(newWord);  // Add to Night Stalker words list
        return newWord;
    }

    public Word addRandomWordForAstralRaider() {
        Random random = new Random();
        x = 150 + random.nextInt(850);  // Random x position
        y = 250 + random.nextInt(250);   // Random y position

        Word randomWord = getRandomWord();  // Get a random word
        Word newWord = new Word(randomWord.getText(), x, y);

        currentWordsAR.add(newWord);  // Add to Night Stalker words list
        return newWord;
    }

    // Add new word to the list
    public void addWord(String newWordText) {
        words.add(new Word(newWordText));  // Add the new word to the list
        try {
            saveWordFile();  // Save the updated list
        } catch (IOException e) {
            MainManager.errorDisplayAndLog("Error saving word file: " + e.getMessage(), e);
        }
    }

    // Update an existing word
    public void updateWord(int index, String newWordText) {
        if (index >= 0 && index < words.size()) {
            words.set(index, new Word(newWordText));  // Update the word
            try {
                saveWordFile();  // Save the updated list
            } catch (IOException e) {
                MainManager.errorDisplayAndLog("Error saving word file: " + e.getMessage(), e);
            }
        }
    }

    // Method to delete a word
    public void deleteWord(int index) {
        if (index >= 0 && index < words.size()) {
            words.remove(index); // Remove the word at the specified index
            try {
                saveWordFile();  // Save the updated list
            } catch (IOException e) {
                MainManager.errorDisplayAndLog("Error saving word file: " + e.getMessage(), e);
            }
        }
    }

    public List<Word> getWords() {
        return words;
    }

    public void createSaveLoadWordFile() {
        try {
            File file = new File(FILE_NAME);

            // If the file doesn't exist, create it and insert predefined words
            if (file.createNewFile()) {
                MainManager.warningDisplayAndLog("Word save file doesn't exist, attempt to create new Word file");
                insertWordList();
                saveWordFile();
            } else {
                loadWordFile();  // Load words from the file into the list
            }
        } catch (IOException e) {
            MainManager.errorDisplayAndLog("Failed to create the file: " + FILE_NAME + ". " + e.getMessage(), e);
        }
    }

    public void saveWordFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Word word : words) {
                writer.write(word.toString());
                writer.newLine();
            }
        }
    }

    void loadWordFile() {
        File file = new File(FILE_NAME);
        File tempFile = new File("temp_" + FILE_NAME); // Create a temporary file name
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            words.clear(); // Clear existing words

            boolean isFileEmpty = true; // To check if file is empty

            while ((line = reader.readLine()) != null) {
                isFileEmpty = false; // File is not empty if at least one line is found
                try {
                    Word word = parseWordFromString(line); // Parse word from line
                    words.add(word); // Add the word to the list
                    writer.write(line); // Write to temp file
                    writer.newLine();
                } catch (Exception e) {
                    MainManager.warningDisplayAndLog("Error parsing line: " + line + ". Skipping the invalid line.");
                }
            }
            // If the file is empty, insert predefined words and save them
            if (isFileEmpty) {
                MainManager.warningDisplayAndLog("Word file is empty. Inserting predefined words.");
                insertWordList(); // Insert predefined words
                saveWordFile(); // Save the words to the file
            }
        } catch (IOException e) {
            MainManager.errorDisplayAndLog("Error reading or writing file: " + e.getMessage(), e);
        }

        // Handle file renaming and cleanup
        if (file.delete()) {
            if (!tempFile.renameTo(file)) {
                MainManager.errorDisplayAndLog("Could not rename temp file to original file: " + FILE_NAME, null);
            }
        } else {
            MainManager.errorDisplayAndLog("Could not delete original file: " + FILE_NAME, null);
        }
    }

    private Word parseWordFromString(String line) throws Exception {
        if (line == null || line.trim().isEmpty()) {
            throw new Exception("Malformed data: Empty or null line.");
        }

        // Return a new Word object with just the text
        return new Word(line.trim());
    }

    private void insertWordList() {
        words.add(new Word("air"));
        words.add(new Word("bat"));
        words.add(new Word("cat"));
        words.add(new Word("dog"));
        words.add(new Word("egg"));
        words.add(new Word("fan"));
        words.add(new Word("gas"));
        words.add(new Word("hat"));
        words.add(new Word("ice"));
        words.add(new Word("jar"));
        words.add(new Word("key"));
        words.add(new Word("lip"));
        words.add(new Word("map"));
        words.add(new Word("net"));
        words.add(new Word("owl"));
        words.add(new Word("pan"));
        words.add(new Word("rat"));
        words.add(new Word("sun"));
        words.add(new Word("tea"));
        words.add(new Word("van"));
        words.add(new Word("web"));
        words.add(new Word("yarn"));
        words.add(new Word("zone"));
        words.add(new Word("back"));
        words.add(new Word("card"));
        words.add(new Word("desk"));
        words.add(new Word("exit"));
        words.add(new Word("fork"));
        words.add(new Word("gift"));
        words.add(new Word("hair"));
        words.add(new Word("iron"));
        words.add(new Word("joke"));
        words.add(new Word("kite"));
        words.add(new Word("lamp"));
        words.add(new Word("milk"));
        words.add(new Word("name"));
        words.add(new Word("open"));
        words.add(new Word("quiz"));
        words.add(new Word("rope"));
        words.add(new Word("soap"));
        words.add(new Word("tent"));
        words.add(new Word("unit"));
        words.add(new Word("vest"));
        words.add(new Word("wind"));
        words.add(new Word("yard"));
        words.add(new Word("zone"));
        words.add(new Word("aim"));
        words.add(new Word("cap"));
        words.add(new Word("dot"));
        words.add(new Word("ear"));
        words.add(new Word("gap"));
        words.add(new Word("hit"));
        words.add(new Word("ink"));
        words.add(new Word("job"));
        words.add(new Word("log"));
        words.add(new Word("mud"));
        words.add(new Word("oak"));
        words.add(new Word("pen"));
        words.add(new Word("run"));
        words.add(new Word("sip"));
        words.add(new Word("tag"));
        words.add(new Word("vet"));
        words.add(new Word("win"));
        words.add(new Word("zap"));
        words.add(new Word("bell"));
        words.add(new Word("corn"));
        words.add(new Word("dust"));
        words.add(new Word("fish"));
        words.add(new Word("gold"));
        words.add(new Word("heat"));
        words.add(new Word("jump"));
        words.add(new Word("kite"));
        words.add(new Word("leaf"));
        words.add(new Word("moon"));
        words.add(new Word("note"));
        words.add(new Word("pipe"));
        words.add(new Word("seed"));
        words.add(new Word("time"));
        words.add(new Word("vote"));
        words.add(new Word("walk"));
        words.add(new Word("yard"));
        words.add(new Word("zone"));
        words.add(new Word("apple"));
        words.add(new Word("subject"));
        words.add(new Word("ship"));
        words.add(new Word("space"));
        words.add(new Word("cloud"));
        words.add(new Word("plant"));
        words.add(new Word("chair"));
        words.add(new Word("glass"));
        words.add(new Word("smile"));
        words.add(new Word("light"));
        words.add(new Word("train"));
        words.add(new Word("bread"));
        words.add(new Word("clock"));
        words.add(new Word("fruit"));
        words.add(new Word("stone"));
        words.add(new Word("brick"));
        words.add(new Word("river"));
        words.add(new Word("storm"));
        words.add(new Word("beach"));
        words.add(new Word("plant"));
        words.add(new Word("piano"));
        words.add(new Word("flame"));
        words.add(new Word("heart"));
        words.add(new Word("house"));
        words.add(new Word("water"));
        words.add(new Word("music"));
        words.add(new Word("candy"));
        words.add(new Word("phone"));
        words.add(new Word("table"));
        words.add(new Word("paper"));
        words.add(new Word("brush"));
    }

}
