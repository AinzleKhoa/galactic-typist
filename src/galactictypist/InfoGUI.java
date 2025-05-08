package galactictypist;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import galactictypist.components.panels.info.BackgroundMenuPanel;
import galactictypist.components.panels.info.ButtonStoryPrevPanel;
import galactictypist.components.panels.info.ButtonStoryNextPanel;
import galactictypist.components.panels.info.ButtonStoryToMenuPanel;
import galactictypist.components.panels.info.DisplayImagePanel;
import galactictypist.components.panels.info.TextInfoPanel;
import javax.swing.SwingUtilities;

public class InfoGUI extends JFrame {

    private MainManager manager;
    private BackgroundMenuPanel backgroundMenuPanel;
    private DisplayImagePanel displayImagePanel;
    private TextInfoPanel textInfoPanel;

    private ButtonStoryPrevPanel buttonPrev;
    private ButtonStoryNextPanel buttonNext;
    private ButtonStoryToMenuPanel buttonStoryToMenuPanel;

    private int page = 0; //Track which story page the player is on

    private String story;

    int frameWidth = 1280;
    int frameHeight = 720;
    int buttonWidth = 300;
    int buttonHeight = 60;

    int centerX = (frameWidth - buttonWidth) / 2;
    int centerY = (frameHeight - buttonHeight) / 2;

    public InfoGUI(MainManager manager) {
        this.manager = manager;
        setupGUI();
    }

    private void setupGUI() {
        setTitle("Galactic Typist");
        setSize(frameWidth, frameHeight);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwingUtilities.invokeLater(() -> {
                    InfoGUI.this.dispose();
                    manager.backToMenu();
                });
            }
        });

        JLayeredPane jPane = getLayeredPane();

        backgroundMenuPanel = new BackgroundMenuPanel("/resources/images/info.png", frameWidth, frameHeight);
        backgroundMenuPanel.setBounds(0, 0, frameWidth, frameHeight);

        displayImagePanel = new DisplayImagePanel("/resources/images/info.png");
        displayImagePanel.setBounds(0, 0, frameWidth, frameHeight);
        displayImagePanel.setVisible(false);

        textInfoPanel = new TextInfoPanel("/resources/images/textbox.png");
        textInfoPanel.setBounds(0, 0, frameWidth, frameHeight);
        pageManager();

        buttonStoryToMenuPanel = new ButtonStoryToMenuPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", 150, 40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InfoGUI.this.dispose();
                manager.backToMenu();
            }
        });
        buttonStoryToMenuPanel.updateText("Menu");
        buttonStoryToMenuPanel.setBounds(1100, 30, 150, 40);
        buttonStoryToMenuPanel.setVisible(false);

        buttonPrev = new ButtonStoryPrevPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", buttonWidth, buttonHeight, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (page) {
                    case 0:
                        SwingUtilities.invokeLater(() -> {
                            InfoGUI.this.dispose();
                            manager.backToMenu();
                        });
                        break;
                    case 1:
                        page--;
                        pageManager();
                        break;
                    case 2:
                        page--;
                        pageManager();
                        break;
                    case 3:
                        page--;
                        pageManager();
                        break;
                    case 4:
                        page--;
                        pageManager();
                        break;
                    case 5:
                        page--;
                        pageManager();
                        break;
                    case 6:
                        page--;
                        pageManager();
                        break;
                    case 7:
                        page--;
                        pageManager();
                        break;
                    case 8:
                        page--;
                        pageManager();
                        break;
                    case 9:
                        page--;
                        pageManager();
                        break;
                    case 10:
                        page--;
                        pageManager();
                        break;
                    case 11:
                        page--;
                        pageManager();
                        break;
                    case 12:
                        page--;
                        pageManager();
                        break;
                    case 13:
                        page--;
                        pageManager();
                        break;
                    case 14:
                        page--;
                        pageManager();
                        break;
                    case 15:
                        page--;
                        pageManager();
                        break;
                    case 16:
                        page--;
                        pageManager();
                        break;
                    case 17:
                        page--;
                        pageManager();
                        break;
                    case 18:
                        page--;
                        pageManager();
                        break;
                    case 19:
                        page--;
                        pageManager();
                        break;
                    case 20:
                        page--;
                        pageManager();
                        break;
                    case 21:
                        page--;
                        pageManager();
                        break;
                    case 22:
                        page--;
                        pageManager();
                        break;
                    case 23:
                        page--;
                        pageManager();
                        break;
                    case 24:
                        page--;
                        pageManager();
                        break;
                    case 25:
                        page--;
                        pageManager();
                        break;
                    default:
                        MainManager.warningDisplayAndLog("Something wrong happen with the prev/next button in infoGUI");
                        SwingUtilities.invokeLater(() -> {
                            InfoGUI.this.dispose();
                            manager.backToMenu();
                        });
                        break;
                }
            }
        });
        buttonPrev.updateText("Go Back to menu");
        buttonPrev.setBounds(620, 590, buttonWidth, buttonHeight);

        buttonNext = new ButtonStoryNextPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", buttonWidth, buttonHeight, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (page) {
                    case 0:
                        page++;
                        pageManager();
                        break;
                    case 1:
                        page++;
                        pageManager();
                        break;
                    case 2:
                        page++;
                        pageManager();
                        break;
                    case 3:
                        page++;
                        pageManager();
                        break;
                    case 4:
                        page++;
                        pageManager();
                        break;
                    case 5:
                        page++;
                        pageManager();
                        break;
                    case 6:
                        page++;
                        pageManager();
                        break;
                    case 7:
                        page++;
                        pageManager();
                        break;
                    case 8:
                        page++;
                        pageManager();
                        break;
                    case 9:
                        page++;
                        pageManager();
                        break;
                    case 10:
                        page++;
                        pageManager();
                        break;
                    case 11:
                        page++;
                        pageManager();
                        break;
                    case 12:
                        page++;
                        pageManager();
                        break;
                    case 13:
                        page++;
                        pageManager();
                        break;
                    case 14:
                        page++;
                        pageManager();
                        break;
                    case 15:
                        page++;
                        pageManager();
                        break;
                    case 16:
                        page++;
                        pageManager();
                        break;
                    case 17:
                        page++;
                        pageManager();
                        break;
                    case 18:
                        page++;
                        pageManager();
                        break;
                    case 19:
                        page++;
                        pageManager();
                        break;
                    case 20:
                        page++;
                        pageManager();
                        break;
                    case 21:
                        page++;
                        pageManager();
                        break;
                    case 22:
                        page++;
                        pageManager();
                        break;
                    case 23:
                        page++;
                        pageManager();
                        break;
                    case 24:
                        page++;
                        pageManager();
                        break;
                    default:
                        MainManager.warningDisplayAndLog("Something wrong happen with the prev/next button in infoGUI");
                        SwingUtilities.invokeLater(() -> {
                            InfoGUI.this.dispose();
                            manager.backToMenu();
                        });
                        break;
                }
            }
        });
        buttonNext.updateText("Next page");
        buttonNext.setBounds(940, 590, buttonWidth, buttonHeight);

        jPane.add(backgroundMenuPanel, Integer.valueOf(0));
        jPane.add(textInfoPanel, Integer.valueOf(1));
        jPane.add(displayImagePanel, Integer.valueOf(2));
        jPane.add(buttonStoryToMenuPanel, Integer.valueOf(2));
        jPane.add(buttonPrev, Integer.valueOf(2));
        jPane.add(buttonNext, Integer.valueOf(2));
    }

    private void pageManager() {
        SwingUtilities.invokeLater(() -> {
            switch (page) {
                default:
                    break;
                case 0:
                    buttonStoryToMenuPanel.hideImage();
                    buttonPrev.updateText("Go Back to menu");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(22f);
                    displayImagePanel.setImage("/resources/images/creator.png");
                    displayImagePanel.showImage(820, 240, 240, 337);
                    story = "About me:\n"
                            + "Hi there! I’m Le Anh Khoa (Nickname: Ainzle), the creator of Galactic Typist. I’m currently a second-year Software Engineering student at FPT University in Can Tho, and this project was developed over ten intense days, from October 18 to October 27, 2024.\n"
                            + "\n"
                            + "My journey into game creation started at \n"
                            + "age six, and I’ve been passionate about \n"
                            + "sci-fi and space themes ever since. Working \n"
                            + "on Galactic Typist allowed me to channel that \n"
                            + "inspiration into a real, playable experience, \n"
                            + "blending creativity with the technical skills \n"
                            + "I’ve gained in my studies.";
                    textInfoPanel.updateText(story);
                    break;
                case 1:
                    buttonStoryToMenuPanel.showImage();
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(22f);
                    displayImagePanel.hideImage();
                    story = "About the game: \n"
                            + "Game Name: Galactic Typist\n"
                            + "Creator: Le Anh Khoa (Ainzle)\n"
                            + "Development Period: October 16, 2024 - October 26, 2024\n"
                            + "Programming Language: Java\n\n"
                            + "Galactic Typist was born from my love for immersive gaming, technology, and the thrill of space battles. Originally developed as a school assignment, it quickly evolved into a meaningful personal project. This game is my second solo Java coding project, where I poured countless hours, day and night, into creating my first large-scale work."
                            + "\n\n"
                            + "From designing gameplay mechanics to coding the details, I gained invaluable experience throughout this project. It deepened my knowledge of Java programming and taught me the ins and outs of managing a medium-scale project. My hope is that Galactic Typist brings players the same excitement and inspiration I felt while creating it—one keypress at a time.";
                    textInfoPanel.updateText(story);

                    break;
                case 2:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(22f);
                    displayImagePanel.hideImage();
                    story = "Game Features:\n"
                            + "Galactic Typist is a sci-fi-inspired typing game where enemies appear on screen with words displayed next to them. Your mission is simple: type the word exactly as shown and press Enter to defeat each enemy.\n"
                            + "\n"
                            + "The game includes an auto-save feature, so you can pick up right where you left off anytime.\n"
                            + "\n"
                            + "Choose between two modes:\n"
                            + "\n"
                            + "Story Mode: Dive into an immersive storyline as you progress through different challenges.\n"
                            + "Endless Mode: Test your endurance in an infinite wave of enemies—perfect for those seeking a challenge.\n\n"
                            + "Access the Scoreboard and Settings from the main menu. The scoreboard allows you to track high scores and manage player profiles. In Settings, you can customize the game to suit your style by adding, editing, or deleting words (DELETE BUTTON ON KEYBOARD ALSO WORKS).";
                    textInfoPanel.updateText(story);
                    break;
                case 3:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(24f);
                    displayImagePanel.hideImage();
                    story = "Game Modes:\n\n"
                            + "Story Mode: Embark on an immersive storyline and face progressively challenging enemies and bosses as you advance.\n"
                            + "\nEndless Mode: Test your endurance by facing an infinite wave of enemies, perfect for players seeking a continuous challenge.\n"
                            + "\n\nHow to Play: \n\n"
                            + "Enemy Attacks: When enemies appear, you have a limited amount of time to type their assigned word before they attack. The remaining time for each enemy’s attack varies based on the difficulty level.\n"
                            + "Warning: When an enemy is about to attack, its word will turn red as a final warning.";
                    textInfoPanel.updateText(story);
                    break;
                case 4:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(23f);
                    displayImagePanel.hideImage();
                    story = "Types of Enemies: \n\n"
                            + "You’ll encounter several types of enemies, each with unique characteristics:\n"
                            + "\n"
                            + "UFO:\n"
                            + "\n"
                            + "Description: This is the most common enemy, appearing frequently.\n"
                            + "Speed: Fast appearance with a moderate attack interval.\n"
                            + "Priority: Medium - Defeat quickly, but prioritize stronger enemies first.\n"
                            + "\nNight Stalker:\n"
                            + "\n"
                            + "Description: The strongest enemy with the fastest attack interval.\n"
                            + "Priority: Highest - Always prioritize eliminating this enemy first due to its fast and frequent attacks.\n";
                    textInfoPanel.updateText(story);
                    break;
                case 5:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(22f);
                    displayImagePanel.hideImage();
                    story = "Types of Enemies: \n\n"
                            + "Astral Raider:\n"
                            + "\n"
                            + "Description: The slowest in both appearance and attack interval but comes with a unique challenge. Defeating it will spawn more than two clones (based on game mode and difficulty).\n"
                            + "Priority: Lowest - Defeat last if possible, but be prepared for its clones in harder modes.\n"
                            + "\nLeviathan (Final Boss):\n"
                            + "\n"
                            + "Description: The ultimate challenge, Leviathan progresses through 4 phases, each representing 25% of its health. As its health decreases, it will spawn all types of enemies you've encountered previously.\n"
                            + "Damage Mechanic: Leviathan takes damage every 20 points scored, with each 20 points boosting the strength of smaller enemies.\n"
                            + "Last Phase (25% Health): During the final phase, Leviathan’s difficulty spikes, especially in Hard Mode, where it becomes extremely challenging.";
                    textInfoPanel.updateText(story);
                    break;
                case 6:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(25f);
                    displayImagePanel.hideImage();
                    story = "Scoreboard and Settings: \n\n"
                            + "From the main menu, you can access:\n"
                            + "\n"
                            + "Scoreboard: Track high scores, compare with previous attempts, and manage player profiles.\n"
                            + "\nSettings: Customize your experience by adjusting options to suit your style, including adding, editing, or deleting words. (The DELETE button on the keyboard can also be used for managing words.)";
                    textInfoPanel.updateText(story);
                    break;
                case 7:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(25f);
                    story = "History: (16/10/2024)\n\n"
                            + "My first ever \n"
                            + "game's menu screen. \n";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history0.png");
                    displayImagePanel.showImage(680, 130, 300, 450);
                    break;
                case 8:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(25f);
                    story = "History: (17/10/2024)\n\n"
                            + "My first concept for \n"
                            + "the game's main screen. \n"
                            + "It was too small, so I \n"
                            + "had to make it larger.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history1.png");
                    displayImagePanel.showImage(680, 130, 300, 450);
                    break;
                case 9:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(25f);
                    story = "History: (18/10/2024)\n\n"
                            + "Attempted to add \n"
                            + "explosions and enemies. \n"
                            + "It looked rough, but \n"
                            + "it worked well enough.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history2.png");
                    displayImagePanel.showImage(680, 130, 300, 450);
                    break;
                case 10:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(25f);
                    story = "History: (19/10/2024)\n\n"
                            + "The screen size \n"
                            + "was too small, \n"
                            + "so I expanded the \n"
                            + "resolution to 1280x720.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history3.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 11:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(23f);
                    story = "History: (19/10/2024)\n\n"
                            + "Added a damage indicator screen \n"
                            + "for when the player takes hits.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history4.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 12:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(25f);
                    story = "History: (19/10/2024)\n\n"
                            + "Initial design of the \n"
                            + "Night Stalker enemy,\n"
                            + "later changed to \n"
                            + "the current version.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history5.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 13:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(24f);
                    story = "History: (19/10/2024)\n\n"
                            + "Introduced the \n"
                            + "Astral Raider enemy. \n"
                            + "It was overpowered initially, \n"
                            + "so I nerfed it later.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history6.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 14:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(25f);
                    story = "History: (19/10/2024)\n\n"
                            + "Introduced level stage \n"
                            + "and warp jump";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history7.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 15:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(23f);
                    story = "History: (19/10/2024)\n\n"
                            + "First look at the \n"
                            + "scoreboard and settings menu.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history8.png");
                    displayImagePanel.showImage(680, 130, 300, 450);
                    break;
                case 16:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(23f);
                    story = "History: (20/10/2024)\n\n"
                            + "Introduced the Leviathan boss.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history9.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 17:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(25f);
                    story = "History: (22/10/2024)\n\n"
                            + "Added difficulty \n"
                            + "selection to the game.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history10.png");
                    displayImagePanel.showImage(680, 130, 300, 450);
                    break;
                case 18:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(25f);
                    story = "History: (22/10/2024)\n\n"
                            + "Implemented the \n"
                            + "health bar and\n"
                            + "game mechanics for \n"
                            + "the boss battles.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history11.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 19:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(25f);
                    story = "History: (23/10/2024)\n\n"
                            + "Finally chose \n"
                            + "a digital font—looks \n"
                            + "much better. \n"
                            + "(Fonts really do \n"
                            + "make a difference!)";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history12.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 20:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(23f);
                    story = "History: (24/10/2024)\n\n"
                            + "Added story mode to the game.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history13.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 21:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(24f);
                    story = "History: (26/10/2024)\n\n"
                            + "expanded the \n"
                            + "resolution to 1600x900.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history14.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 22:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(26f);
                    story = "History: (26/10/2024)\n\n"
                            + "Finalize the game";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history15.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 23:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(26f);
                    story = "History: (29/10/2024)\n\n"
                            + "First Update:"
                            + "\n\n"
                            + "Added a health bar\n"
                            + "for the player."
                            + "\nAlso improved\n"
                            + "text readability.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history16.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 24:
                    buttonPrev.updateText("Previous page");
                    buttonNext.showImage();
                    buttonNext.updateText("Next page");
                    textInfoPanel.setSizeText(26f);
                    story = "History: (02/11/2024)\n\n"
                            + "Second Update:"
                            + "\n\n"
                            + "Introduced the\n"
                            + "'How to Play'\n"
                            + "instructions.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history17.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
                case 25:
                    buttonPrev.updateText("Previous page");
                    buttonNext.hideImage();
                    textInfoPanel.setSizeText(26f);
                    story = "History: (03/02/2025)\n\n"
                            + "Third Update:"
                            + "\n\n"
                            + "Fixing some bugs\n"
                            + "in endless mode.";
                    textInfoPanel.updateText(story);
                    displayImagePanel.setImage("/resources/images/history18.png");
                    displayImagePanel.showImage(650, 180, 450, 300);
                    break;
            }
        });
    }
}
