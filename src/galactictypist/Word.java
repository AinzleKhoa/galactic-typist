package galactictypist;

import java.awt.Color;
import java.io.Serializable;
import java.util.UUID;

public class Word implements Serializable {

    private static final long serialVersionUID = 1L; // Add this line for version control
    private String text;  // Word text
    private int x;        // x coordinate
    private int y;        // y coordinate
    private Color textColor; // New field for color

    private volatile boolean isMatched = false; // Flag to indicate if the word is matched
    private final String id;

    // Constructor to initialize the word with random position
    public Word(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.id = UUID.randomUUID().toString();
        this.textColor = Color.WHITE; // Default color
    }

    // Constructor for word without coordinates
    public Word(String text) {
        this.text = text;
        this.id = UUID.randomUUID().toString();
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public String getText() {
        return text;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setMatched(boolean matched) {
        this.isMatched = matched;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public String getId() {
        return id;  // Return the unique identifier
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // If they are the same object
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // If null or not the same class
        }
        Word word = (Word) obj; // Cast to Word
        return id == word.id && text.equals(word.text); // Compare based on fields (id, text, etc.)
    }

    // Override toString method
    @Override
    public String toString() {
        return text; // Change this to return what you want (e.g., wordText + ": " + definition)
    }
}
