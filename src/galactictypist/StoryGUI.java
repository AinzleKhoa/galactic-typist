package galactictypist;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import galactictypist.components.panels.story.BackgroundMenuPanel;
import galactictypist.components.panels.story.ButtonStoryPrevPanel;
import galactictypist.components.panels.story.ButtonStoryNextPanel;
import galactictypist.components.panels.story.TextStoryPanel;
import javax.swing.SwingUtilities;

public class StoryGUI extends JFrame {

    private MainManager manager;
    private BackgroundMenuPanel backgroundMenuPanel;
    private TextStoryPanel textStoryPanel;

    private ButtonStoryPrevPanel buttonPrev;
    private ButtonStoryNextPanel buttonNext;

    private int page = 0; //Track which story page the player is on

    private String story;

    int frameWidth = 1280;
    int frameHeight = 720;
    int buttonWidth = 300;
    int buttonHeight = 60;

    int centerX = (frameWidth - buttonWidth) / 2;
    int centerY = (frameHeight - buttonHeight) / 2;

    public StoryGUI(MainManager manager) {
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
                    StoryGUI.this.dispose();
                    manager.backToMenu();
                });
            }
        });

        JLayeredPane jPane = getLayeredPane();

        backgroundMenuPanel = new BackgroundMenuPanel("/resources/images/story.png", frameWidth, frameHeight);
        backgroundMenuPanel.setBounds(0, 0, frameWidth, frameHeight);

        textStoryPanel = new TextStoryPanel("/resources/images/textbox.png");
        textStoryPanel.setBounds(0, 0, frameWidth, frameHeight);
        story = "In the year 3025, Earth stood united under the banner of the Federation of Terran Defense (FTD), "
                + "a coalition dedicated to defending humanity from cosmic threats. "
                + "This newfound unity, however, was forged under the shadow of a formidable enemy: the Voidlord, "
                + "a ruthless alien empire that spanned across light-years, hungering for conquest. "
                + "Aboard their menacing ships, the Voidlord forces had one goal—complete domination over Earth and its surrounding colonies.\n"
                + "\n"
                + "Among Earth’s bravest defenders stood a new breed of pilot known as the \"Astroers.\" "
                + "Specially trained to operate the Federation’s most advanced combat ships, these soldiers were humanity’s best chance at fending off the invaders.";
        textStoryPanel.updateText(story);

        buttonPrev = new ButtonStoryPrevPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", buttonWidth, buttonHeight, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (page) {
                    case 0:
                        SwingUtilities.invokeLater(() -> {
                            StoryGUI.this.dispose();
                            manager.backToMenu();
                        });
                        break;
                    case 1:
                        page = 0;
                        pageManager();
                        break;
                    default:
                        page = 1;
                        pageManager();
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
                        page = 1;
                        pageManager();
                        break;
                    case 1:
                        page = 2;
                        pageManager();
                        break;
                    default:
                        manager.storyMode();
                        break;
                }
            }
        });
        buttonNext.updateText("Next page");
        buttonNext.setBounds(940, 590, buttonWidth, buttonHeight);

        jPane.add(backgroundMenuPanel, Integer.valueOf(0));
        jPane.add(textStoryPanel, Integer.valueOf(1));
        jPane.add(buttonPrev, Integer.valueOf(2));
        jPane.add(buttonNext, Integer.valueOf(2));
    }

    private void pageManager() {
        switch (page) {
            default:
                break;
            case 0:
                buttonPrev.updateText("Go Back to menu");
                buttonNext.updateText("Next page");
                story = "In the year 3025, Earth stood united under the banner of the Federation of Terran Defense (FTD), "
                        + "a coalition dedicated to defending humanity from cosmic threats. "
                        + "This newfound unity, however, was forged under the shadow of a formidable enemy: the Voidlord, "
                        + "a ruthless alien empire that spanned across light-years, hungering for conquest. "
                        + "Aboard their menacing ships, the Voidlord forces had one goal—complete domination over Earth and its surrounding colonies.\n"
                        + "\n"
                        + "Among Earth’s bravest defenders stood a new breed of pilot known as the \"Astroers.\" "
                        + "Specially trained to operate the Federation’s most advanced combat ships, these soldiers were humanity’s best chance at fending off the invaders.";
                textStoryPanel.updateText(story);
                break;
            case 1:
                buttonPrev.updateText("Previous page");
                buttonNext.updateText("Next page");
                story = "In the heart of the war-torn galaxy, the Astroers prepared for their most critical mission yet. "
                        + "The Voidlord’s power was concentrated within twelve colossal motherships, ominously named The Void Enforcers. "
                        + "Each one was a fortress in space, carrying enough firepower to decimate entire planets. To stand any chance of stopping the Voidlord, these twelve behemoths had to be destroyed.\n"
                        + "\n"
                        + "As the fleet of Astroers surged into battle, explosions lit up the void like distant supernovas. "
                        + "Among them, you, a freshly trained Astroer, found yourself face-to-face with one of The Void Enforcers, its silhouette massive against the stars. "
                        + "With a burst of speed, you pushed past enemy lines, dodging a barrage of laser fire. "
                        + "The chaos around you provided cover, allowing you to slip deep into the Voidlord’s territory.";
                textStoryPanel.updateText(story);
                break;
            case 2:
                buttonPrev.updateText("Previous page");
                buttonNext.updateText("Start Game");
                story = "Ahead lay the heart of enemy space—a vast network of defenses protecting the Enforcers. "
                        + "Alarms blared, and enemy fighters swarmed, but your training kept you sharp and steady. "
                        + "With each maneuver, you carved a path closer to your goal: "
                        + "a chance to eliminate one of the Voidlord’s greatest weapons and bring humanity a step closer to victory.\n"
                        + "\n"
                        + "This was no ordinary mission. This was the beginning of the end for the Voidlord—if you could survive.";
                textStoryPanel.updateText(story);
                break;
        }
    }
}
