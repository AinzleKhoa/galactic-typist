package galactictypist;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import galactictypist.components.panels.game.AlertBossPanel;
import galactictypist.components.panels.game.BackgroundBossPanel1;
import galactictypist.components.panels.game.BackgroundBossPanel2;
import galactictypist.components.panels.game.BackgroundBossPanel3;
import galactictypist.components.panels.game.BackgroundFinalePanel;
import galactictypist.components.panels.game.BackgroundLevel1Panel;
import galactictypist.components.panels.game.BackgroundLevel2Panel;
import galactictypist.components.panels.game.BackgroundLevel3Panel;
import galactictypist.components.panels.game.BackgroundLevel4Panel;
import galactictypist.components.panels.game.BackgroundLevel5Panel;
import galactictypist.components.panels.game.BackgroundTransitionPanel;
import galactictypist.components.panels.game.BossDefeatedFinalePanel1;
import galactictypist.components.panels.game.BossDefeatedFinalePanel2;
import galactictypist.components.panels.game.BossDefeatedFinalePanel3;
import galactictypist.components.panels.game.BossDefeatedFinalePanel4;
import galactictypist.components.panels.game.DamageAlertPanel;
import galactictypist.components.panels.game.DamagePanel1;
import galactictypist.components.panels.game.DamagePanel2;
import galactictypist.components.panels.game.DamagePanel3;
import galactictypist.components.panels.game.DamagePanel4;
import galactictypist.components.panels.game.DamageReceivedPanel;
import galactictypist.components.panels.game.EnemyArPanel;
import galactictypist.components.panels.game.EnemyLeviathanPanel;
import galactictypist.components.panels.game.EnemyNsPanel;
import galactictypist.components.panels.game.EnemyUfoPanel;
import galactictypist.components.panels.game.ExplosionBossDefeatedPanel0;
import galactictypist.components.panels.game.ExplosionBossDefeatedPanel1;
import galactictypist.components.panels.game.ExplosionBossDefeatedPanel2;
import galactictypist.components.panels.game.ExplosionBossDefeatedPanel3;
import galactictypist.components.panels.game.ExplosionBossDefeatedPanel4;
import galactictypist.components.panels.game.ExplosionBossDefeatedPanel5;
import galactictypist.components.panels.game.ExplosionBossPanel1;
import galactictypist.components.panels.game.ExplosionBossPanel2;
import galactictypist.components.panels.game.ExplosionPanel;
import galactictypist.components.panels.game.PortalBlackPanel;
import galactictypist.components.panels.game.PortalSpacePanel;
import galactictypist.components.panels.game.SpaceshipBadConditionPanel;
import galactictypist.components.panels.game.SpaceshipPanel;
import galactictypist.components.panels.game.SpeedTravelEnemyPanel1;
import galactictypist.components.panels.game.SpeedTravelEnemyPanel2;
import galactictypist.components.panels.game.SpeedTravelPlayerPanel1;
import galactictypist.components.panels.game.SpeedTravelPlayerPanel2;
import galactictypist.components.panels.game.StatusOverlayPanel;
import galactictypist.components.panels.game.TextboxPanel;

import galactictypist.components.CustomHealthBarProgressBar;
import galactictypist.components.PlayerHealthBarProgressBar;

import galactictypist.components.labels.game.DifficultyAndGamemodeLabel;
import galactictypist.components.labels.game.HowToPlayLabel;
import galactictypist.components.labels.game.PlayerNameLabel;
import galactictypist.components.labels.game.TheLeviathanLabel;
import galactictypist.components.panels.game.ButtonStoryAnswerPanel;
import galactictypist.components.panels.game.ButtonStorySkipPanel;
import galactictypist.components.panels.game.DisplayTabletEnemyPanel;
import galactictypist.components.panels.game.DisplayTabletPanel;
import galactictypist.components.panels.game.TextStoryPanel;

import galactictypist.threads.UfoThread;
import galactictypist.threads.NightStalkerThread;
import galactictypist.threads.AstralRaiderThread;
import java.awt.Color;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main Game GUI for "Galactic Typist"
 */
public class GameGUI extends JFrame {

    private int storyPhase = 0; //This is where the story phase is keep tracked
    private ButtonStoryAnswerPanel buttonStoryAnswerPanel;
    private ButtonStorySkipPanel buttonStorySkipPanel;

    private int difficultyMode = 1; // The difficulty will be passed from MainManager which is from difficultyGUI

    private int endlessModeScoreToTravel = 20; //All this is for endless mode. Basically the EndlessModeDifficultyDirector.
    private boolean endlessModeSpawnNS = false;
    private boolean endlessModeSpawnAR = false;
    private int endlessModeSpawnCountAR = 2;

    private int eliminatedByWho = 0;

    private final MainManager manager;
    private YouWinGUI youWinScreen;
    private YouLoseGUI youLoseScreen;

    private SpeedTravelEnemyPanel1 speedTravelEnemyPanel1;
    private SpeedTravelEnemyPanel2 speedTravelEnemyPanel2;
    private SpeedTravelPlayerPanel1 speedTravelPlayerPanel1;
    private SpeedTravelPlayerPanel2 speedTravelPlayerPanel2;

    private BackgroundTransitionPanel transitionBackgroundPanel;

    private BackgroundBossPanel1 backgroundBossPanel1;
    private BackgroundBossPanel2 backgroundBossPanel2;
    private BackgroundBossPanel3 backgroundBossPanel3;

    private BackgroundFinalePanel backgroundFinalePanel;

    private BackgroundLevel1Panel backgroundLevel1Panel;
    private BackgroundLevel2Panel backgroundLevel2Panel;
    private BackgroundLevel3Panel backgroundLevel3Panel;
    private BackgroundLevel4Panel backgroundLevel4Panel;
    private BackgroundLevel5Panel backgroundLevel5Panel;

    private SpaceshipPanel spaceshipPanel;
    private SpaceshipBadConditionPanel spaceshipBadConditionPanel;

    private TextboxPanel textboxPanel;

    private TextStoryPanel textStoryPanel;
    private String textStory;
    private DisplayTabletPanel displayTabletPanel;
    private DisplayTabletEnemyPanel displayTabletEnemyPanel;

    private CustomHealthBarProgressBar bossHealthBar;
    private PlayerHealthBarProgressBar playerHealthBar;

    private StatusOverlayPanel statusOverlayPanel;
    private JLabel livesStatusLabel = new JLabel();
    private JLabel scoreStatusLabel = new JLabel();
    private JLabel levelStatusLabel = new JLabel();
    private JLabel streakStatusLabel = new JLabel();
    private JLabel accuracyStatusLabel = new JLabel();

    private DifficultyAndGamemodeLabel difficultyAndGamemodeLabel;
    private HowToPlayLabel howToPlayLabel;

    private EnemyUfoPanel enemyUfoPanel;
    private EnemyNsPanel enemyNsPanel;
    private EnemyArPanel enemyArPanel;
    private boolean spawnMoreAstralRaiders = false;
    private boolean allAstralRaidersDefeated = true;
    private int astralRaidersSpawningNumber;
    private int astralRaidersCount = 0;

    private AlertBossPanel alertBossPanel;
    private EnemyLeviathanPanel enemyLeviathanPanel;

    private PortalBlackPanel portalBlackPanel;
    private PortalSpacePanel portalSpacePanel;

    private ExplosionPanel explosionPanel;

    private ExplosionBossPanel1 explosionBossPanel1;
    private ExplosionBossPanel2 explosionBossPanel2;

    private ExplosionBossDefeatedPanel0 explosionBossDefeatedPanel0;
    private ExplosionBossDefeatedPanel1 explosionBossDefeatedPanel1;
    private ExplosionBossDefeatedPanel2 explosionBossDefeatedPanel2;
    private ExplosionBossDefeatedPanel3 explosionBossDefeatedPanel3;
    private ExplosionBossDefeatedPanel4 explosionBossDefeatedPanel4;
    private ExplosionBossDefeatedPanel5 explosionBossDefeatedPanel5;

    private BossDefeatedFinalePanel1 bossDefeatedFinalePanel1;
    private BossDefeatedFinalePanel2 bossDefeatedFinalePanel2;
    private BossDefeatedFinalePanel3 bossDefeatedFinalePanel3;
    private BossDefeatedFinalePanel4 bossDefeatedFinalePanel4;

    private DamagePanel1 damagePanel1;
    private DamagePanel2 damagePanel2;
    private DamagePanel3 damagePanel3;
    private DamagePanel4 damagePanel4;
    private DamageAlertPanel damageAlertPanel;
    private DamageReceivedPanel damageReceivedPanel;

    private final List<Word> activeWordsUFO = new ArrayList<>();  // List of active for UFO enemy's words generation
    private final List<Word> activeWordsNS = new ArrayList<>();  // List of active for NightStalker enemy's words generation
    private final List<Word> activeWordsAR = new ArrayList<>();  // List of active for AstralRaider enemy's words generation

    private boolean enemyUfoAppear = true;
    private boolean enemyNsAppear = false; //this enemy appear after level 2
    private boolean enemyArAppear = false; //this enemy appear after level 4
    private boolean enemyLeviathanAppear = false; //The boss at level 5

    private int currentBossState = 1;

    private TheLeviathanLabel theLeviathanLabel;
    private PlayerNameLabel playerNameLabel;

    private int initialScoreForBossPhase = 0;

    private AtomicInteger activeUfoThreads = new AtomicInteger(0); //AtomicInteger to ensure atomic operations are used for thread-safe increments and decrements
    private AtomicInteger activeNsThreads = new AtomicInteger(0);
    private AtomicInteger activeArThreads = new AtomicInteger(0);

    private volatile boolean gameOver = false;  // Use volatile to ensure proper synchronization across threads
    private boolean isInitialStart = true;  // Flag to check if it's the initial start

    private int maxEnemyUFO;
    private int attackIntervalUFO;
    private int minAppearTimeUFO;
    private int maxAppearTimeUFO;

    private int maxEnemyNS;
    private int attackIntervalNS;
    private int minAppearTimeNS;
    private int maxAppearTimeNS;

    private int maxEnemyAR;
    private int attackIntervalAR;
    private int minAppearTimeAR;
    private int maxAppearTimeAR;

    private Timer warningBossTimer;
    private Timer emergenceBossTimer;
    private Timer travelSpeedBossTimer;
    private Timer travelSpeedPlayerTimer;
    private Timer portalBlackBiggerTimer;
    private Timer portalSpaceBiggerTimer;
    private Timer bossGetHurtTimer;
    private Timer bossGetHurtExplosionTimer;
    private Timer bossExplosionShockwaveTimer;
    private Timer bossExplosionBiggerTimer;
    private Timer endlessModeDifficultyDirectorTimer;
    private Timer repeatEmergingIntoVortexPortalTimer;
    private Timer repeatAlertLeviathanLabelTimer;

    private Timer updateInputValidationStatusTimer;

    private ExecutorService enemyExecutor;  // Thread pool for enemies

    int frameWidth = 1600;
    int frameHeight = 900;

    public GameGUI(MainManager manager, int difficultyMode) {
        enemyExecutor = Executors.newFixedThreadPool(10);  // Adjust pool size as necessary
        this.manager = manager;
        this.difficultyMode = difficultyMode;
        initialDifficulty();
        initilizeFocusOnInputTextbox();
        setupGUI();
        if (manager.getPlayerManager().getPlayerLevel() == 0) { // IF ENDLESS, THE STORY MODE WILL NOT START
            EndlessModeRandomStageTravel();
            howToPlayDisplayOn();

            //Start spawning enemy after the warp jump is completed.
            Timer phase3 = new Timer(10110, e3 -> {
                SwingUtilities.invokeLater(() -> {
                    startGenerateUFO();
                    howToPlayDisplayOff();
                });
            });
            phase3.setRepeats(false); // This timer will not repeat
            phase3.start();
        } else {
            initializeStoryMode();
        }
//        startGenerateUFO();  // Start generatin
//        startGenerateNS();  // Start generating
//        startGenerateAR();  // Start generating
//        startBossOnsetPhases();
//        startBossAttackPhases();
    }

    private void initilizeFocusOnInputTextbox() {
        Timer phase1 = new Timer(100, e1 -> {
            SwingUtilities.invokeLater(() -> {
                textboxPanel.setFocusOnTheInputTextbox();
            });
        });
        phase1.setRepeats(false); // This timer will not repeat
        phase1.start();
    }

    private void initialDifficulty() {
        switch (difficultyMode) {
            case 1:
                astralRaidersSpawningNumber = 3;
                //Easy mode, astral raider at level 4 will spawn 3 enemies
                setUfoStrength(2, 20000, 3000, 5000);
                //Initial start will be 2 enemy, before become 1 enemy again for difficulty balance
                //Ufo Enemy will be the first enemy and will attack after 20 second
                //enemy will take 3 - 5 seconds before appear again

                setNsStrength(1, 18000, 6000, 8000);
                //Initial start will be 1 enemy for this enemy
                //Nightstalker Enemy will be the second enemy and will attack after 16 second
                //enemy will take 6 - 8 seconds before appear again

                setArStrength(1, 28000, 3000, 6000);
                //Initial start will be 2 enemy for this enemy
                //Astral Raider Enemy will be the third enemy and will attack after 26 second
                //enemy will take 3 - 6 seconds before appear again
                break;
            case 2:
                astralRaidersSpawningNumber = 4; //Medium mode, astral raider at level 4 will spawn 4 enemies
                setUfoStrength(2, 12000, 3000, 5000);
                setNsStrength(1, 9000, 6000, 8000);
                setArStrength(1, 18000, 2000, 3000);
                break;
            case 3:
                astralRaidersSpawningNumber = 5; //Hard mode, astral raider at level 4 will spawn 5 enemies
                setUfoStrength(2, 6000, 2000, 4000);
                setNsStrength(1, 4000, 5000, 7000);
                setArStrength(1, 10000, 1000, 2000);
                break;
            //ENDLESS MODE
            default:
                astralRaidersSpawningNumber = 1; //For Endless, first time defeated will be 2 enemy clone
                endlessModeSpawnCountAR = 1;
                manager.getPlayerManager().setPlayerCompletedGameMode(0); //Set gamemode to 0 meant endless mode for scoreboard to display accordingly afterward.

                setUfoStrength(2, 12000, 3000, 5000);
                setNsStrength(1, 8000, 6000, 8000);
                setArStrength(1, 14000, 6000, 9000);

                endlessModeDifficultyDirectorTimer = EndlessModeDifficultyDirector(20000); //start the timer to make the enemy stronger

                MainManager.logInfo("Endless Mode initialized");
                break;
        }
    }

    private void setUfoStrength(int maxEnemy, int attackInterval, int minAppearTime, int maxAppearTime) {
        maxEnemyUFO = maxEnemy;
        attackIntervalUFO = attackInterval;
        minAppearTimeUFO = minAppearTime;
        maxAppearTimeUFO = maxAppearTime - minAppearTime;
    }

    private void setNsStrength(int maxEnemy, int attackInterval, int minAppearTime, int maxAppearTime) {
        maxEnemyNS = maxEnemy;
        attackIntervalNS = attackInterval;
        minAppearTimeNS = minAppearTime;
        maxAppearTimeNS = maxAppearTime - minAppearTime;
    }

    private void setArStrength(int maxEnemy, int attackInterval, int minAppearTime, int maxAppearTime) {
        maxEnemyAR = maxEnemy;
        attackIntervalAR = attackInterval;
        minAppearTimeAR = minAppearTime;
        maxAppearTimeAR = maxAppearTime - minAppearTime;
    }

    private void setEnemyAppear(boolean UFO, boolean NS, boolean AR, boolean BOSS) {
        enemyUfoAppear = UFO;
        enemyNsAppear = NS;
        enemyArAppear = AR;
        enemyLeviathanAppear = BOSS;
    }

    private void clearAllActiveWords() {
        manager.getWordManager().clearCurrentWords(); //Clear the active words in wordmanager
        activeWordsUFO.clear();  // Clear the active words after game is disposed
        activeWordsNS.clear();  // Clear the active words after game is disposed
        activeWordsAR.clear();  // Clear the active words after game is disposed
    }

    private void stopAllTimer() {
        // Stop all active timers if they are not null
        if (warningBossTimer != null) {
            warningBossTimer.stop();
        }
        if (emergenceBossTimer != null) {
            emergenceBossTimer.stop();
        }
        if (travelSpeedBossTimer != null) {
            travelSpeedBossTimer.stop();
        }
        if (travelSpeedPlayerTimer != null) {
            travelSpeedPlayerTimer.stop();
        }
        if (portalBlackBiggerTimer != null) {
            portalBlackBiggerTimer.stop();
        }
        if (portalSpaceBiggerTimer != null) {
            portalSpaceBiggerTimer.stop();
        }
        if (bossGetHurtTimer != null) {
            bossGetHurtTimer.stop();
        }
        if (bossGetHurtExplosionTimer != null) {
            bossGetHurtExplosionTimer.stop();
        }
        if (bossExplosionShockwaveTimer != null) {
            bossExplosionShockwaveTimer.stop();
        }
        if (bossExplosionBiggerTimer != null) {
            bossExplosionBiggerTimer.stop();
        }
        if (endlessModeDifficultyDirectorTimer != null) {
            endlessModeDifficultyDirectorTimer.stop();
        }
        if (repeatEmergingIntoVortexPortalTimer != null) {
            repeatEmergingIntoVortexPortalTimer.stop();
        }
        if (repeatAlertLeviathanLabelTimer != null) {
            repeatAlertLeviathanLabelTimer.stop();
        }
        if (updateInputValidationStatusTimer != null) {
            SwingUtilities.invokeLater(() -> playerNameLabel.updateValidationStatus(-1));
            updateInputValidationStatusTimer.stop();
        }
    }

    private void initializeStoryMode() {
        //phase 0
        SwingUtilities.invokeLater(() -> {
            transitionBackgroundPanel.setVisible(true);
            travelSpeedBossTimer = repeatBossSpeedTravel(80);
            //phase 1
            Timer phase1 = new Timer(4000, e1 -> {
                SwingUtilities.invokeLater(() -> {
                    dialogueResponseManager();
                });
            });
            phase1.setRepeats(false); // This timer will not repeat
            phase1.start();
        });
    }

    private void dialogueResponseManager() {
        SwingUtilities.invokeLater(() -> {
            switch (storyPhase) {
                case 0:
                    buttonStorySkipPanel.setVisible(true);
                    textStoryPanel.setVisible(true);
                    textStory = "This is your Overseer speaking. Astroer! Do you copy?";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("Copy, sir!");
                    break;
                case 1:
                    textStoryPanel.setVisible(true);
                    textStory = "Report confirms you’ve broken through the enemy’s frontline. Good work, Astroer.";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("Thank you, sir.");
                    break;
                case 2:
                    displayTabletPanel.showImage(10, 220, 470, 352); //Display the tablet
                    textStoryPanel.setVisible(true);
                    textStory = "Astroer, intel incoming. Threat profiles will display on your tablet—study them well..";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("Yes sir!");
                    break;
                case 3:
                    displayTabletEnemyPanel.setImage("/resources/images/enemy.png"); //Display the enemy
                    displayTabletEnemyPanel.showImage(60, 325, 350, 151);
                    textStoryPanel.setVisible(true);
                    textStoryPanel.setSizeText(21f);
                    textStory = "First, the UFO. Once a harmless myth, now a common threat. It’s basic but frequent. Take them out as they appear.";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("...");
                    break;
                case 4:
                    displayTabletEnemyPanel.setImage("/resources/images/nightstalker.png");
                    displayTabletEnemyPanel.showImage(75, 250, 320, 320);
                    textStoryPanel.setVisible(true);
                    textStoryPanel.setSizeText(22f);
                    textStory = "Next, the Night Stalker. It’s fast and lethal—prioritize it as soon as you spot one.";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("...");
                    break;
                case 5:
                    displayTabletEnemyPanel.setImage("/resources/images/astralraider.png");
                    displayTabletEnemyPanel.showImage(45, 335, 380, 180);
                    textStoryPanel.setVisible(true);
                    textStoryPanel.setSizeText(21f);
                    textStory = "Finally, the Astral Raider. Destroying it will cause it to clone itself into multiple targets. Stay sharp once you take one down.";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("...");
                    break;
                case 6:
                    displayTabletEnemyPanel.setImage("/resources/images/leviathan.png");
                    displayTabletEnemyPanel.showImage(60, 320, 370, 195);
                    textStoryPanel.setVisible(true);
                    textStoryPanel.setSizeText(20f);
                    textStory = "Your mission: eliminate the twelve Void Enforcers—colossal motherships. "
                            + "You’re approaching one now. It’s known as the Leviathan.";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("Understood, sir.");
                    break;
                case 7:
                    textStoryPanel.setVisible(true);
                    textStoryPanel.setSizeText(21f);
                    textStory = "Leviathan has the ability to summon squadrons—think of it as the core of their defense. "
                            + "Your orders are clear: destroy it.";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("Mission confirmed, sir.");
                    break;
                case 8:
                    displayTabletPanel.hideImage();
                    displayTabletEnemyPanel.hideImage();
                    textStoryPanel.setVisible(true);
                    textStoryPanel.setSizeText(22f);
                    textStory = "Heads up—you’re deep in enemy territory. "
                            + "Multiple hostiles inbound. Maintain focus, Astroer; this is hostile ground.";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("Understood, sir. Ready for orders.");
                    break;
                case 9:
                    buttonStorySkipPanel.setVisible(false);
                    textStoryPanel.setVisible(true);
                    textStoryPanel.setSizeText(22f);
                    textStory = "Prepare for warp jump. Comms may weaken after the jump, but I’ll reconnect if possible. Engage when ready. Good luck out there.";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("*Initilize the warp jump*");
                    break;
                case 10:
                    howToPlayDisplayOn();
                    displayTabletEnemyPanel.hideImage();
                    displayTabletPanel.hideImage();
                    textStoryPanel.hideImage();
                    buttonStoryAnswerPanel.hideImage();
                    travelSpeedBossTimer.stop();
                    travelToLevel(1); // Start the warp jump

                    //make sure the image is hide completely
                    Timer phase2 = new Timer(110, e2 -> {
                        SwingUtilities.invokeLater(() -> {
                            speedTravelEnemyPanel1.hideImage();
                            speedTravelEnemyPanel2.hideImage();

                            //Start spawning enemy after the warp jump is completed.
                            Timer phase3 = new Timer(10110, e3 -> {
                                SwingUtilities.invokeLater(() -> {
                                    startGenerateUFO();
                                    howToPlayDisplayOff();
                                });
                            });
                            phase3.setRepeats(false); // This timer will not repeat
                            phase3.start();
                        });
                    });
                    phase2.setRepeats(false); // This timer will not repeat
                    phase2.start();
                    break;

                // The second interactions (After boss is defeated)
                case 11:
                    buttonStorySkipPanel.setVisible(true);
                    textStoryPanel.setSizeText(24f);
                    textStoryPanel.setVisible(true);
                    textStory = "This is your Overseer speaking. Astroer! Do you copy?";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("Copy, sir!");
                    break;
                case 12:
                    textStoryPanel.setSizeText(23f);
                    textStoryPanel.setVisible(true);
                    textStory = "Report confirms the Leviathan has been eliminated. Excellent work, Astroer!";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("Thank you, sir!");
                    break;
                case 13:
                    textStoryPanel.setSizeText(22f);
                    textStoryPanel.setVisible(true);
                    textStory = "Listen closely—when a Void Enforcer is destroyed, it spawns a vortex portal that links to our next target’s vicinity.";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("Understood, sir. Ready for orders.");
                    break;
                case 14:
                    textStoryPanel.setSizeText(23f);
                    textStoryPanel.setVisible(true);
                    textStory = "Enter the vortex portal and continue the mission. We’re counting on you. Over and out.";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("Alright. Lets go-");
                    break;
                case 15:
                    textStoryPanel.setSizeText(24f);
                    textStoryPanel.setColorText(Color.red);
                    textStoryPanel.setVisible(true);
                    textStory = "Astroer... do you hear me?";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setSizeText(21f);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("Who is this? Identify yourself!");
                    break;
                case 16:
                    textStoryPanel.setSizeText(23f);
                    textStoryPanel.setVisible(true);
                    textStory = "That’s not important. But listen closely—you cannot trust the Overseer.";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setSizeText(20f);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("What? How did you get on this channel?");
                    break;
                case 17:
                    textStoryPanel.setSizeText(23f);
                    textStoryPanel.setColorText(Color.gray);
                    textStoryPanel.setVisible(true);
                    textStory = "(The comms go silent)";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setSizeText(22f);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("Hey wait!...");
                    break;
                case 18:
                    textStoryPanel.setSizeText(23f);
                    textStoryPanel.setColorText(Color.gray);
                    textStoryPanel.setVisible(true);
                    textStory = "(No one response...)";
                    textStoryPanel.updateText(textStory);
                    buttonStoryAnswerPanel.setSizeText(22f);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("...");
                    break;
                case 19:
                    buttonStorySkipPanel.setVisible(false);
                    textStoryPanel.hideImage();
                    buttonStoryAnswerPanel.setSizeText(21f);
                    buttonStoryAnswerPanel.setVisible(true);
                    buttonStoryAnswerPanel.updateText("*Fly into the vortex portal*");
                    break;
                case 20:
                    textStoryPanel.hideImage();
                    buttonStoryAnswerPanel.hideImage();
                    repeatEmergingIntoVortexPortalTimer = repeatEmergingIntoVortexPortal(80);
                    travelSpeedBossTimer = repeatBossSpeedTravel(80);

                    // Approach the strange hole
                    Timer phase3 = new Timer(3000, e3 -> {
                        SwingUtilities.invokeLater(() -> {
                            repeatEmergingIntoVortexPortalTimer.stop();
                            travelSpeedBossTimer.stop();
                            speedTravelEnemyPanel1.hideImage();
                            speedTravelEnemyPanel2.hideImage();
                            bossDefeatedFinalePanel4.hideImage();

                            //make sure the image is hide completely and end game
                            Timer phase4 = new Timer(110, e4 -> {
                                SwingUtilities.invokeLater(() -> {
                                    speedTravelEnemyPanel1.hideImage();
                                    speedTravelEnemyPanel2.hideImage();
                                    bossDefeatedFinalePanel4.hideImage();

                                    MainManager.logInfo("You win!");
                                    switch (difficultyMode) { //Based on what difficulty, the scoreboard will display accordingly
                                        case 1:
                                            manager.getPlayerManager().setPlayerCompletedGameMode(1);
                                            break;
                                        case 2:
                                            manager.getPlayerManager().setPlayerCompletedGameMode(2);
                                            break;
                                        case 3:
                                            manager.getPlayerManager().setPlayerCompletedGameMode(3);
                                            break;
                                        default:
                                            MainManager.warningDisplayAndLog("The difficulty wasnt set properly!");
                                            break;
                                    }
                                    SwingUtilities.invokeLater(() -> {
                                        GameGUI.this.dispose();  // Dispose the current game window
                                        youWinScreen = new YouWinGUI(manager);  // Create and display the "You Win" screen
                                        manager.saveGame();  // Save the game data
                                    });

                                });
                            });
                            phase4.setRepeats(false); // This timer will not repeat
                            phase4.start();
                        });
                    });
                    phase3.setRepeats(false); // This timer will not repeat
                    phase3.start();
                    break;
                default:
                    break;
            }
        });
    }

    private void howToPlayDisplayOn() {
        SwingUtilities.invokeLater(() -> howToPlayLabel.setVisible(true));
    }

    private void howToPlayDisplayOff() {
        SwingUtilities.invokeLater(() -> howToPlayLabel.setVisible(false));
    }

    private void setupGUI() {
        // Set up JFrame
        setTitle("Galactic Typist");
        setSize(frameWidth, frameHeight);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
        // Set the default close operation to call backToMenu
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwingUtilities.invokeLater(() -> {
                    GameGUI.this.dispose();  // Dispose the current game window
                    manager.backToMenu();
                    gameOver();
                });
            }
        });

        //JPane for control over layer of panels
        JLayeredPane jPane = getLayeredPane();

        //Answer the call, this will link very closely to the dialogueResponseManager, e.g: case 9 = case 10 on that method
        buttonStoryAnswerPanel = new ButtonStoryAnswerPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", 300, 80, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    switch (storyPhase) {
                        case 0:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 1:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 2:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 3:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 4:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 5:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 6:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 7:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 8:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 9:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;

                        // The second interactions (After boss is defeated)
                        case 10:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 11:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 12:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 13:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 14:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 15:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 16:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 17:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 18:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        case 19:
                            buttonStoryAnswerPanel.setVisible(false);
                            storyPhase++;
                            dialogueResponseManager();
                            break;
                        default:
                            break;
                    }
                });
            }
        });
        //Answer the call, this will link very closely to the dialogueResponseManager, e.g: case 9 = case 10 on that method
        buttonStorySkipPanel = new ButtonStorySkipPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", 150, 40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (storyPhase < 9) {
                        storyPhase = 10;
                        buttonStorySkipPanel.hideImage();
                        dialogueResponseManager();
                    } else {
                        storyPhase = 20;
                        buttonStorySkipPanel.hideImage();
                        dialogueResponseManager();
                    }
                });
            }
        });
        SwingUtilities.invokeLater(() -> {
            buttonStoryAnswerPanel.updateText("");
            buttonStoryAnswerPanel.setVisible(false);
            buttonStoryAnswerPanel.setBounds(648, 680, 300, 80);

            buttonStorySkipPanel.updateText("Skip");
            buttonStorySkipPanel.setVisible(false);
            buttonStorySkipPanel.setBounds(730, 120, 150, 40);

            //Text for story
            textStoryPanel = new TextStoryPanel("/resources/images/textboxright.png");
            textStoryPanel.setBounds(0, 0, frameWidth, frameHeight);
            textStoryPanel.setVisible(false);

            //Display Tablet
            displayTabletPanel = new DisplayTabletPanel("/resources/images/displaytablet.png");
            displayTabletPanel.setBounds(0, 0, frameWidth, frameHeight);
            displayTabletPanel.setVisible(false);

            //Display Tablet for enemies
            displayTabletEnemyPanel = new DisplayTabletEnemyPanel("/resources/images/enemy.png");
            displayTabletEnemyPanel.setBounds(0, 0, frameWidth, frameHeight);
            displayTabletEnemyPanel.setVisible(false);

            //Label for the leviathan boss showcase
            theLeviathanLabel = new TheLeviathanLabel();
            theLeviathanLabel.setBounds(220, 340, frameWidth, frameHeight); // Adjust position if needed
            theLeviathanLabel.setVisible(false);

            //Label for the player name (Name is for player name, streak is for input correct or wrong, If it is 0 streak it is wrong, if not, it is correct
            playerNameLabel = new PlayerNameLabel(manager.getPlayerManager().getPlayerName());
            playerNameLabel.setBounds(10, 10, frameWidth, frameHeight); // Adjust position if needed
            playerNameLabel.setVisible(true);

            //Label for difficulty
            difficultyAndGamemodeLabel = new DifficultyAndGamemodeLabel(manager.getPlayerManager().getPlayerLevel(), difficultyMode);
            difficultyAndGamemodeLabel.setBounds(670, 720, 300, 300); // Adjust position if needed

            //Label for how to play
            howToPlayLabel = new HowToPlayLabel();
            howToPlayLabel.setBounds(180, 50, frameWidth, frameHeight); // Adjust position if needed
            howToPlayLabel.setVisible(false);

            //Health bar for boss
            bossHealthBar = new CustomHealthBarProgressBar(0, 100);
            bossHealthBar.setValue(100); // Start with full health (100)
            bossHealthBar.setBounds(620, 30, 380, 50);
            bossHealthBar.setVisible(false);

            //Health bar for player
            playerHealthBar = new PlayerHealthBarProgressBar(0, 100);
            playerHealthBar.setValue(100); // Start with full health (100)
            playerHealthBar.setBounds(30, 230, 50, 250);
            playerHealthBar.setVisible(true);

            //Speed Travel
            speedTravelEnemyPanel1 = new SpeedTravelEnemyPanel1("/resources/images/speedTravelEnemy1.png");
            speedTravelEnemyPanel1.setBounds(0, 0, frameWidth, frameHeight);
            speedTravelEnemyPanel1.setVisible(false);

            speedTravelEnemyPanel2 = new SpeedTravelEnemyPanel2("/resources/images/speedTravelEnemy2.png");
            speedTravelEnemyPanel2.setBounds(0, 0, frameWidth, frameHeight);
            speedTravelEnemyPanel2.setVisible(false);

            speedTravelPlayerPanel1 = new SpeedTravelPlayerPanel1("/resources/images/speedTravelPlayer1.png");
            speedTravelPlayerPanel1.setBounds(0, 0, frameWidth, frameHeight);
            speedTravelPlayerPanel1.setVisible(false);

            speedTravelPlayerPanel2 = new SpeedTravelPlayerPanel2("/resources/images/speedTravelPlayer2.png");
            speedTravelPlayerPanel2.setBounds(0, 0, frameWidth, frameHeight);
            speedTravelPlayerPanel2.setVisible(false);

            //Background Boss Stage
            backgroundBossPanel1 = new BackgroundBossPanel1("/resources/images/backgroundboss1.png");
            backgroundBossPanel1.setBounds(0, 0, frameWidth, frameHeight);
            backgroundBossPanel1.setVisible(false);

            backgroundBossPanel2 = new BackgroundBossPanel2("/resources/images/backgroundboss2.png");
            backgroundBossPanel2.setBounds(0, 0, frameWidth, frameHeight);
            backgroundBossPanel2.setVisible(false);

            backgroundBossPanel3 = new BackgroundBossPanel3("/resources/images/backgroundboss3.png");
            backgroundBossPanel3.setBounds(0, 0, frameWidth, frameHeight);
            backgroundBossPanel3.setVisible(false);

            //Background Finale
            backgroundFinalePanel = new BackgroundFinalePanel("/resources/images/backgroundFinale.png");
            backgroundFinalePanel.setBounds(0, 0, frameWidth, frameHeight);
            backgroundFinalePanel.setVisible(false);

            //Background
            transitionBackgroundPanel = new BackgroundTransitionPanel("/resources/images/spacetravel.png");
            transitionBackgroundPanel.setBounds(0, 0, frameWidth, frameHeight);
            transitionBackgroundPanel.setVisible(true);

            backgroundLevel1Panel = new BackgroundLevel1Panel("/resources/images/backgroundlevel1.png");
            backgroundLevel1Panel.setBounds(0, 0, frameWidth, frameHeight);
            backgroundLevel1Panel.setVisible(false);

            backgroundLevel2Panel = new BackgroundLevel2Panel("/resources/images/backgroundlevel2.png");
            backgroundLevel2Panel.setBounds(0, 0, frameWidth, frameHeight);
            backgroundLevel2Panel.setVisible(false);

            backgroundLevel3Panel = new BackgroundLevel3Panel("/resources/images/backgroundlevel3.png");
            backgroundLevel3Panel.setBounds(0, 0, frameWidth, frameHeight);
            backgroundLevel3Panel.setVisible(false);

            backgroundLevel4Panel = new BackgroundLevel4Panel("/resources/images/backgroundlevel4.png");
            backgroundLevel4Panel.setBounds(0, 0, frameWidth, frameHeight);
            backgroundLevel4Panel.setVisible(false);

            backgroundLevel5Panel = new BackgroundLevel5Panel("/resources/images/backgroundlevel5.png");
            backgroundLevel5Panel.setBounds(0, 0, frameWidth, frameHeight);
            backgroundLevel5Panel.setVisible(false);

            //Spaceship hud
            spaceshipPanel = new SpaceshipPanel("/resources/images/spaceship.png");
            spaceshipPanel.setBounds(0, 0, frameWidth, frameHeight);

            spaceshipBadConditionPanel = new SpaceshipBadConditionPanel("/resources/images/spaceshipbad.png");
            spaceshipBadConditionPanel.setBounds(0, 0, frameWidth, frameHeight);
            spaceshipBadConditionPanel.setVisible(false);

            //Textbox input
            textboxPanel = new TextboxPanel("/resources/images/textboxinput.png", this);
            textboxPanel.setBounds(0, 0, frameWidth, frameHeight);

            //Status overlay
            statusOverlayPanel = new StatusOverlayPanel(
                    "/resources/images/statusOverlay.png",
                    livesStatusLabel,
                    scoreStatusLabel,
                    levelStatusLabel,
                    streakStatusLabel,
                    accuracyStatusLabel
            );
            statusOverlayPanel.setBounds(0, 0, frameWidth, frameHeight);
            updateStatusOverlay();

            //Enemy
            enemyUfoPanel = new EnemyUfoPanel("/resources/images/enemy.png", activeWordsUFO);
            enemyUfoPanel.setBounds(0, 0, frameWidth, frameHeight);

            enemyNsPanel = new EnemyNsPanel("/resources/images/nightstalker.png", activeWordsNS);
            enemyNsPanel.setBounds(0, 0, frameWidth, frameHeight);

            enemyArPanel = new EnemyArPanel("/resources/images/astralraider.png", activeWordsAR);
            enemyArPanel.setBounds(0, 0, frameWidth, frameHeight);

            //Boss
            alertBossPanel = new AlertBossPanel("/resources/images/alertBoss.png");
            alertBossPanel.setBounds(0, 0, frameWidth, frameHeight);
            alertBossPanel.setVisible(false);  // Hide panel initially

            enemyLeviathanPanel = new EnemyLeviathanPanel("/resources/images/leviathan.png");
            enemyLeviathanPanel.setBounds(0, 0, frameWidth, frameHeight);
            enemyLeviathanPanel.setVisible(false);  // Hide panel initially

            portalBlackPanel = new PortalBlackPanel("/resources/images/portalBlack.png");
            portalBlackPanel.setBounds(0, 0, frameWidth, frameHeight);
            portalBlackPanel.setVisible(false);  // Hide panel initially

            portalSpacePanel = new PortalSpacePanel("/resources/images/portalSpace.png");
            portalSpacePanel.setBounds(0, 0, frameWidth, frameHeight);
            portalSpacePanel.setVisible(false);  // Hide panel initially

            //Explosion Boss Defeated
            explosionBossDefeatedPanel0 = new ExplosionBossDefeatedPanel0("/resources/images/bossDefeatedExplosion0.png");
            explosionBossDefeatedPanel0.setBounds(0, 0, 2000, 2000);
            explosionBossDefeatedPanel0.setVisible(false);  // Hide panel initially

            explosionBossDefeatedPanel1 = new ExplosionBossDefeatedPanel1("/resources/images/bossDefeatedExplosion1.png");
            explosionBossDefeatedPanel1.setBounds(0, 0, 2000, 2000);
            explosionBossDefeatedPanel1.setVisible(false);  // Hide panel initially

            explosionBossDefeatedPanel2 = new ExplosionBossDefeatedPanel2("/resources/images/bossDefeatedExplosion2.png");
            explosionBossDefeatedPanel2.setBounds(0, 0, 2000, 2000);
            explosionBossDefeatedPanel2.setVisible(false);  // Hide panel initially

            explosionBossDefeatedPanel3 = new ExplosionBossDefeatedPanel3("/resources/images/bossDefeatedExplosion3.png");
            explosionBossDefeatedPanel3.setBounds(0, 0, 2000, 2000);
            explosionBossDefeatedPanel3.setVisible(false);  // Hide panel initially

            explosionBossDefeatedPanel4 = new ExplosionBossDefeatedPanel4("/resources/images/bossDefeatedExplosion4.png");
            explosionBossDefeatedPanel4.setBounds(0, 0, 2000, 2000);
            explosionBossDefeatedPanel4.setVisible(false);  // Hide panel initially

            explosionBossDefeatedPanel5 = new ExplosionBossDefeatedPanel5("/resources/images/bossDefeatedExplosion5.png");
            explosionBossDefeatedPanel5.setBounds(0, 0, 2000, 2000);
            explosionBossDefeatedPanel5.setVisible(false);  // Hide panel initially

            //Boss Defeated Finale
            bossDefeatedFinalePanel1 = new BossDefeatedFinalePanel1("/resources/images/bossDefeated1.png");
            bossDefeatedFinalePanel1.setBounds(0, 0, 2000, 2000);
            bossDefeatedFinalePanel1.setVisible(false);  // Hide panel initially

            bossDefeatedFinalePanel2 = new BossDefeatedFinalePanel2("/resources/images/bossDefeated2.png");
            bossDefeatedFinalePanel2.setBounds(0, 0, 2000, 2000);
            bossDefeatedFinalePanel2.setVisible(false);  // Hide panel initially

            bossDefeatedFinalePanel3 = new BossDefeatedFinalePanel3("/resources/images/bossDefeated3.png");
            bossDefeatedFinalePanel3.setBounds(0, 0, 2000, 2000);
            bossDefeatedFinalePanel3.setVisible(false);  // Hide panel initially

            bossDefeatedFinalePanel4 = new BossDefeatedFinalePanel4("/resources/images/bossDefeated4.png");
            bossDefeatedFinalePanel4.setBounds(0, 0, 2000, 2000);
            bossDefeatedFinalePanel4.setVisible(false);  // Hide panel initially

            //Explosion Eliminated
            explosionPanel = new ExplosionPanel("/resources/images/explosion.png");
            explosionPanel.setBounds(0, 0, frameWidth, frameHeight);
            explosionPanel.setVisible(false);  // Hide panel initially

            //Explosion Boss get hurt
            explosionBossPanel1 = new ExplosionBossPanel1("/resources/images/explosionBoss1.png");
            explosionBossPanel1.setBounds(0, 0, frameWidth, frameHeight);
            explosionBossPanel1.setVisible(false);  // Hide panel initially

            explosionBossPanel2 = new ExplosionBossPanel2("/resources/images/explosionBoss2.png");
            explosionBossPanel2.setBounds(0, 0, frameWidth, frameHeight);
            explosionBossPanel2.setVisible(false);  // Hide panel initially

            //Damage Received
            damagePanel1 = new DamagePanel1("/resources/images/damage1.png");
            damagePanel1.setBounds(0, 0, frameWidth, frameHeight);
            damagePanel1.setVisible(false);  // Hide panel initially

            damagePanel2 = new DamagePanel2("/resources/images/damage2.png");
            damagePanel2.setBounds(0, 0, frameWidth, frameHeight);
            damagePanel2.setVisible(false);  // Hide panel initially

            damagePanel3 = new DamagePanel3("/resources/images/damage3.png");
            damagePanel3.setBounds(0, 0, frameWidth, frameHeight);
            damagePanel3.setVisible(false);  // Hide panel initially

            damagePanel4 = new DamagePanel4("/resources/images/damage2.png");
            damagePanel4.setBounds(0, 0, frameWidth, frameHeight);
            damagePanel4.setVisible(false);  // Hide panel initially

            damageReceivedPanel = new DamageReceivedPanel("/resources/images/receivedamage.png");
            damageReceivedPanel.setBounds(0, 0, frameWidth, frameHeight);
            damageReceivedPanel.setVisible(false);  // Hide panel initially

            damageAlertPanel = new DamageAlertPanel("/resources/images/damagewarning.png");
            damageAlertPanel.setBounds(0, 0, frameWidth, frameHeight);
            damageAlertPanel.setVisible(false);  // Hide panel initially

            // Add panels to the JLayeredPane in correct order
            jPane.add(transitionBackgroundPanel, Integer.valueOf(0));
            jPane.add(backgroundBossPanel1, Integer.valueOf(0));
            jPane.add(backgroundBossPanel2, Integer.valueOf(0));
            jPane.add(backgroundBossPanel3, Integer.valueOf(0));
            jPane.add(backgroundFinalePanel, Integer.valueOf(0)); //Finale background
            jPane.add(backgroundLevel1Panel, Integer.valueOf(0));
            jPane.add(backgroundLevel2Panel, Integer.valueOf(0));
            jPane.add(backgroundLevel3Panel, Integer.valueOf(0));
            jPane.add(backgroundLevel4Panel, Integer.valueOf(0));
            jPane.add(backgroundLevel5Panel, Integer.valueOf(0));
            jPane.add(portalSpacePanel, Integer.valueOf(1));
            jPane.add(speedTravelEnemyPanel1, Integer.valueOf(2));
            jPane.add(speedTravelEnemyPanel2, Integer.valueOf(2));
            jPane.add(speedTravelPlayerPanel1, Integer.valueOf(2));
            jPane.add(speedTravelPlayerPanel2, Integer.valueOf(2));
            jPane.add(portalBlackPanel, Integer.valueOf(3));
            jPane.add(enemyLeviathanPanel, Integer.valueOf(4));
            jPane.add(explosionBossDefeatedPanel0, Integer.valueOf(5));
            jPane.add(explosionBossDefeatedPanel1, Integer.valueOf(5));
            jPane.add(explosionBossDefeatedPanel2, Integer.valueOf(5));
            jPane.add(explosionBossDefeatedPanel3, Integer.valueOf(5));
            jPane.add(explosionBossDefeatedPanel4, Integer.valueOf(5));
            jPane.add(explosionBossDefeatedPanel5, Integer.valueOf(5));
            jPane.add(bossDefeatedFinalePanel1, Integer.valueOf(6));
            jPane.add(bossDefeatedFinalePanel2, Integer.valueOf(6));
            jPane.add(bossDefeatedFinalePanel3, Integer.valueOf(6));
            jPane.add(bossDefeatedFinalePanel4, Integer.valueOf(6));
            jPane.add(explosionBossPanel1, Integer.valueOf(7));
            jPane.add(explosionBossPanel2, Integer.valueOf(7));
            jPane.add(enemyArPanel, Integer.valueOf(8));
            jPane.add(enemyUfoPanel, Integer.valueOf(9));
            jPane.add(enemyNsPanel, Integer.valueOf(10));
            jPane.add(explosionPanel, Integer.valueOf(11));  // This is where explosions happen
            jPane.add(damagePanel1, Integer.valueOf(12));
            jPane.add(damagePanel2, Integer.valueOf(12));
            jPane.add(damagePanel3, Integer.valueOf(12));
            jPane.add(damagePanel4, Integer.valueOf(12));
            jPane.add(damageReceivedPanel, Integer.valueOf(13));
            jPane.add(spaceshipPanel, Integer.valueOf(14));
            jPane.add(spaceshipBadConditionPanel, Integer.valueOf(14));
            jPane.add(playerHealthBar, Integer.valueOf(15));
            jPane.add(textStoryPanel, Integer.valueOf(16));  // This is where story text happen
            jPane.add(buttonStoryAnswerPanel, Integer.valueOf(16));  // This is where story text happen
            jPane.add(buttonStorySkipPanel, Integer.valueOf(16));  // This is where story skipper happen
            jPane.add(displayTabletPanel, Integer.valueOf(16));  // This is where story text happen
            jPane.add(displayTabletEnemyPanel, Integer.valueOf(17));  // This is where story text happen
            jPane.add(difficultyAndGamemodeLabel, Integer.valueOf(17));
            jPane.add(playerNameLabel, Integer.valueOf(18));
            jPane.add(damageAlertPanel, Integer.valueOf(19));
            jPane.add(alertBossPanel, Integer.valueOf(20));
            jPane.add(textboxPanel, Integer.valueOf(211));
            jPane.add(statusOverlayPanel, Integer.valueOf(22));
            jPane.add(bossHealthBar, Integer.valueOf(23));
            jPane.add(theLeviathanLabel, Integer.valueOf(24));
            jPane.add(howToPlayLabel, Integer.valueOf(25));
        });
    }

    public MainManager getMainManager() {
        return manager;
    }

// Pass the update logic
    public void updateStatusOverlay() {
        statusOverlayPanel.updateStatus(manager.getPlayerManager().getPlayerAccuracy(),
                manager.getPlayerManager().getPlayerScore(),
                manager.getPlayerManager().getPlayerLives(),
                manager.getPlayerManager().getPlayerLevel(),
                manager.getPlayerManager().getPlayerStreak());
    }

    public void startGenerateUFO() {
        if (enemyUfoAppear) {  // Only generate UFO if allowed
            for (int i = 0; i < maxEnemyUFO; i++) {
                if (!enemyExecutor.isShutdown()) {
                    enemyExecutor.submit(new UfoThread(this, activeWordsUFO));  // Submit tasks to thread pool
                } else {
                    MainManager.logWarning("Attempted to submit a task (GenerateUFO) after the executor has been shut down.");
                }
            }
        }
    }

    public void startGenerateNS() {
        if (enemyNsAppear) {  // Only generate Night Stalker if allowed
            for (int i = 0; i < maxEnemyNS; i++) {
                if (!enemyExecutor.isShutdown()) {
                    enemyExecutor.submit(new NightStalkerThread(this, activeWordsNS));  // Submit tasks to thread pool

                } else {
                    MainManager.logWarning("Attempted to submit a task (GenerateNS) after the executor has been shut down.");
                }
            }
        }
    }

    public void startGenerateAR() {
        if (enemyArAppear) {  // Only generate Astral Raider if allowed
            for (int i = 0; i < maxEnemyAR; i++) {
                if (!enemyExecutor.isShutdown()) {
                    enemyExecutor.submit(new AstralRaiderThread(this, activeWordsAR));  // Submit tasks to thread pool
                } else {
                    MainManager.logWarning("Attempted to submit a task (GenerateAR) after the executor has been shut down.");
                }
            }
        }
    }

    private void gameOver() {
        MainManager.logInfo("**The game has stopped");
        gameOver = true;
        setEnemyAppear(false, false, false, false); //Stop generating enemy

        stopAllTimer();

        if (enemyExecutor != null && !enemyExecutor.isShutdown()) {
            enemyExecutor.shutdownNow();  // Interrupt all running threads
        }

        clearAllActiveWords();
    }

    @Override
    public void dispose() {
        // Ensure that everything is disposed on the EDT
        SwingUtilities.invokeLater(() -> {
            // Shut down the thread pool if it’s still running
            if (!enemyExecutor.isShutdown()) {
                enemyExecutor.shutdownNow();  // Shutdown threads on dispose
            }

            // Call the superclass dispose method to close the window
            super.dispose();

            // Perform cleanup on game data and lists
            manager.getWordManager().clearCurrentWords(); // Clear the active words in word manager
            activeWordsUFO.clear();  // Clear active UFO words
            activeWordsNS.clear();   // Clear active Night Stalker words
            activeWordsAR.clear();   // Clear active Astral Raider words
        });
    }

    public Timer repeatAlertBossPanel(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    alertBossPanel.hideImage();
                    if (count % 2 == 0) {
                        alertBossPanel.showImage(500, 500);
                    } else {
                        alertBossPanel.hideImage();
                    }
                    count++;
                });
            }
        });
        repeatingTimer.setRepeats(true);
        repeatingTimer.start();
        return repeatingTimer; // Return the Timer instance so it can be stopped later if needed
    }

    public Timer repeatAlertLeviathanLabel(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            int count = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    theLeviathanLabel.setVisible(false);
                    if (count % 2 == 0) {
                        theLeviathanLabel.setVisible(true);
                    } else {
                        theLeviathanLabel.setVisible(false);
                    }
                    count++;
                });
            }
        });
        repeatingTimer.setRepeats(true);
        repeatingTimer.start();
        return repeatingTimer; // Return the Timer instance so it can be stopped later if needed
    }

    public Timer repeatBossEmerge(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            int imageWidth = 300; //Initial size when boss appear
            int imageHeight = 167; //Initial size when boss appear

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    enemyLeviathanPanel.showImage(imageWidth, imageHeight);
                    imageWidth += 50;
                    imageHeight += 28;
                });
            }
        });
        repeatingTimer.setRepeats(true);
        repeatingTimer.start();
        return repeatingTimer; // Return the Timer instance so it can be stopped later if needed
    }

    public Timer repeatBossSpeedTravel(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            int count = 0; // Counter to alternate between images

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    // Force both images to hide before toggling visibility
                    speedTravelEnemyPanel1.hideImage();
                    speedTravelEnemyPanel2.hideImage();

                    if (count % 2 == 0) {
                        // Show speedTravelEnemyPanel1, ensure speedTravelEnemyPanel2 is hidden
                        speedTravelEnemyPanel1.showImage();
                    } else {
                        // Show speedTravelEnemyPanel2, ensure speedTravelEnemyPanel1 is hidden
                        speedTravelEnemyPanel2.showImage();
                    }
                    count++;
                });
            }
        });
        repeatingTimer.setRepeats(true);  // Repeat the timer
        repeatingTimer.start();           // Start the timer
        return repeatingTimer;            // Return the timer instance for future control
    }

    public Timer repeatPlayerSpeedTravel(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            int count = 0; // Counter to alternate between images

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    // Force both images to hide before toggling visibility
                    speedTravelPlayerPanel1.hideImage();
                    speedTravelPlayerPanel2.hideImage();

                    if (count % 2 == 0) {
                        speedTravelPlayerPanel1.showImage();
                    } else {
                        speedTravelPlayerPanel2.showImage();
                    }
                    count++;
                });
            }
        });
        repeatingTimer.setRepeats(true);  // Repeat the timer
        repeatingTimer.start();           // Start the timer
        return repeatingTimer;            // Return the timer instance for future control
    }

    public Timer repeatPortalBlackBigger(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            int imageWidth = 400; //Initial size when boss appear
            int imageHeight = 400; //Initial size when boss appear

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    portalBlackPanel.hideImage();
                    portalBlackPanel.showImage(imageWidth, imageHeight);
                    imageWidth += 50;
                    imageHeight += 50;
                });
            }
        });
        repeatingTimer.setRepeats(true);
        repeatingTimer.start();
        return repeatingTimer; // Return the Timer instance so it can be stopped later if needed
    }

    public Timer repeatPortalSpaceBigger(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            int imageWidth = 500; //Initial size when boss appear
            int imageHeight = 500; //Initial size when boss appear

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    portalSpacePanel.hideImage();
                    portalSpacePanel.showImage(imageWidth, imageHeight);
                    imageWidth += 50;
                    imageHeight += 50;
                });
            }
        });
        repeatingTimer.setRepeats(true);
        repeatingTimer.start();
        return repeatingTimer; // Return the Timer instance so it can be stopped later if needed
    }

    public Timer repeatEmergingIntoVortexPortal(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            int imageWidth = 800; //Initial size when boss appear
            int imageHeight = 800; //Initial size when boss appear

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    bossDefeatedFinalePanel4.showImage(imageWidth, imageHeight);
                    imageWidth += 50;
                    imageHeight += 50;
                });
            }
        });
        repeatingTimer.setRepeats(true);
        repeatingTimer.start();
        return repeatingTimer; // Return the Timer instance so it can be stopped later if needed
    }

    public void repeatCheckingListEmptyAndStartBoss(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve the current counts safely using AtomicInteger
                int ufoCount = activeUfoThreads.get();
                int nsCount = activeNsThreads.get();
                int arCount = activeArThreads.get();

                // Log the current thread counts
                MainManager.logInfo("UFO Threads: " + ufoCount + ", NS Threads: " + nsCount + ", AR Threads: " + arCount);

                // Check if all enemy threads have finished
                if (ufoCount == 0 && nsCount == 0 && arCount == 0) {
                    startBossOnsetPhases();  // Trigger the boss phase
                    ((Timer) e.getSource()).stop();  // Stop the timer once boss phase starts
                }
            }
        });
        repeatingTimer.setRepeats(true);  // Ensure the timer repeats until all conditions are met
        repeatingTimer.start();  // Start the timer
    }

    public Timer repeatBossGetHurt(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            int count = 0; // Counter to alternate between images

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    enemyLeviathanPanel.showImageCenter();
                    Random random = new Random();
                    int randomX = 1 + random.nextInt(1);
                    int randomY = 1 + random.nextInt(1);

                    if (count % 2 == 0) {
                        enemyLeviathanPanel.showImageAt(randomX, randomY);
                    } else {
                        enemyLeviathanPanel.showImageCenter();
                    }
                    count++;
                });
            }
        });
        repeatingTimer.setRepeats(true);  // Repeat the timer
        repeatingTimer.start();           // Start the timer
        return repeatingTimer;            // Return the timer instance for future control
    }

    public Timer repeatBossGetHurtExplosion(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            int count = 0; // Counter to alternate between images

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    explosionBossPanel1.hideImage();
                    explosionBossPanel2.hideImage();

                    Random random = new Random();
                    int randomX = 100 + random.nextInt(600);
                    int randomY = 30 + random.nextInt(100);
                    int randomSizeXY = 300 + random.nextInt(300);

                    if (count % 2 == 0) {
                        explosionBossPanel1.showImage(randomX, randomY, randomSizeXY, randomSizeXY);
                        explosionBossPanel2.hideImage();
                    } else {
                        explosionBossPanel1.hideImage();
                        explosionBossPanel2.showImage(randomX, randomY, randomSizeXY, randomSizeXY);
                    }
                    count++;
                });
            }
        });
        repeatingTimer.setRepeats(true);  // Repeat the timer
        repeatingTimer.start();           // Start the timer
        return repeatingTimer;            // Return the timer instance for future control
    }

    public Timer repeatBossDefeatedShockwave(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            int count = 0; // Counter to alternate between images

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    explosionBossDefeatedPanel0.hideImage();
                    explosionBossDefeatedPanel1.hideImage();

                    if (count % 2 == 0) {
                        explosionBossDefeatedPanel0.showImage(2000, 2000);
                        explosionBossDefeatedPanel1.hideImage();
                    } else {
                        explosionBossDefeatedPanel0.hideImage();
                        explosionBossDefeatedPanel1.showImage(2000, 2000);
                    }
                    count++;
                });
            }
        });
        repeatingTimer.setRepeats(true);  // Repeat the timer
        repeatingTimer.start();           // Start the timer
        return repeatingTimer;            // Return the timer instance for future control
    }

    public void startBossGetHurt() {
        //phase 1
        SwingUtilities.invokeLater(() -> {
            bossGetHurtTimer = repeatBossGetHurt(200);
            bossGetHurtExplosionTimer = repeatBossGetHurtExplosion(100);

            //phase 2
            Timer phase1 = new Timer(1000, e1 -> {
                SwingUtilities.invokeLater(() -> {
                    bossGetHurtTimer.stop();
                    bossGetHurtExplosionTimer.stop();

                    //make sure the image is back to its location and hide the explosion completely
                    Timer phase2 = new Timer(200, e2 -> {
                        SwingUtilities.invokeLater(() -> {
                            enemyLeviathanPanel.showImageCenter();
                            explosionBossPanel1.hideImage();
                            explosionBossPanel2.hideImage();
                        });
                    });
                    phase2.setRepeats(false); // This timer will not repeat
                    phase2.start();

                });
            });
            phase1.setRepeats(false); // This timer will not repeat
            phase1.start();
        });
    }

    public Timer repeatBossExplosionBigger(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {
            int imageWidth = 2500; //Initial size when boss appear
            int imageHeight = 2500; //Initial size when boss appear

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    explosionBossDefeatedPanel5.hideImage();
                    explosionBossDefeatedPanel5.showImage(imageWidth, imageHeight);
                    imageWidth += 120;
                    imageHeight += 120;
                });
            }
        });
        repeatingTimer.setRepeats(true);
        repeatingTimer.start();
        return repeatingTimer; // Return the Timer instance so it can be stopped later if needed
    }

    public void startBossExplosion() {
        //phase 1
        SwingUtilities.invokeLater(() -> {
            explosionBossDefeatedPanel2.showImage(500, 500);
            explosionBossDefeatedPanel3.showImage(400, 400);

            //phase 2
            Timer phase1 = new Timer(400, e1 -> {
                SwingUtilities.invokeLater(() -> {
                    explosionBossDefeatedPanel2.showImage(800, 800);
                    explosionBossDefeatedPanel3.hideImage();
                    explosionBossDefeatedPanel4.showImage(1000, 1000);
                    bossExplosionShockwaveTimer = repeatBossDefeatedShockwave(100);

                    //phase 3
                    Timer phase2 = new Timer(400, e2 -> {
                        SwingUtilities.invokeLater(() -> {
                            explosionBossDefeatedPanel2.hideImage();
                            explosionBossDefeatedPanel4.hideImage();
                            enemyLeviathanPanel.hideImage();
                            bossHealthBar.setVisible(false);
                            explosionBossDefeatedPanel5.showImage(2500, 2500);
                            bossExplosionBiggerTimer = repeatBossExplosionBigger(30);

                            //phase 4 - makse sure the image is hide
                            Timer phase3 = new Timer(2000, e3 -> {
                                SwingUtilities.invokeLater(() -> {
                                    bossExplosionBiggerTimer.stop();

                                    //phase 5 - makse sure the image is hide
                                    Timer phase4 = new Timer(150, e4 -> {
                                        SwingUtilities.invokeLater(() -> {
                                            explosionBossDefeatedPanel5.hideImage();
                                            bossDefeatedFinalePanel1.showImage(1800, 1800);
                                            enemyLeviathanPanel.hideImage();
                                        });
                                    });
                                    phase4.setRepeats(false); // This timer will not repeat
                                    phase4.start();
                                });
                            });
                            phase3.setRepeats(false); // This timer will not repeat
                            phase3.start();
                        });
                    });
                    phase2.setRepeats(false); // This timer will not repeat
                    phase2.start();
                });
            });
            phase1.setRepeats(false); // This timer will not repeat
            phase1.start();
        });
    }

    public void startBossFinaleDefeated() {
        //phase 1
        SwingUtilities.invokeLater(() -> {
            explosionBossDefeatedPanel5.hideImage();
            //This is just for sure the panel is finale
            backgroundLevel5Panel.hideImage();
            backgroundBossPanel1.hideImage();
            backgroundBossPanel2.hideImage();
            backgroundBossPanel3.hideImage();
            backgroundFinalePanel.showImage();

            //phase 2
            Timer phase1 = new Timer(600, e1 -> {
                SwingUtilities.invokeLater(() -> {
                    bossDefeatedFinalePanel1.hideImage();
                    bossDefeatedFinalePanel2.showImage(1000, 1000);
                    bossExplosionShockwaveTimer.stop();
                    explosionBossDefeatedPanel1.hideImage();
                    explosionBossDefeatedPanel2.hideImage();

                    //phase 3
                    Timer phase2 = new Timer(500, e2 -> {
                        SwingUtilities.invokeLater(() -> {
                            explosionBossDefeatedPanel1.hideImage();
                            explosionBossDefeatedPanel2.hideImage();
                            explosionBossDefeatedPanel0.hideImage();
                            bossDefeatedFinalePanel1.hideImage();
                            bossDefeatedFinalePanel2.hideImage();
                            bossDefeatedFinalePanel3.showImage(600, 600);

                            //phase 3
                            Timer phase3 = new Timer(800, e3 -> {
                                SwingUtilities.invokeLater(() -> {
                                    bossDefeatedFinalePanel3.hideImage();
                                    bossDefeatedFinalePanel4.showImage(800, 800);

                                    //phase 4
                                    Timer phase4 = new Timer(2000, e4 -> {
                                        SwingUtilities.invokeLater(() -> {
                                            storyPhase = 11; // Increment because the last phase doesnt
                                            dialogueResponseManager();
                                        });
                                    });
                                    phase4.setRepeats(false); // This timer will not repeat
                                    phase4.start();
                                });
                            });
                            phase3.setRepeats(false); // This timer will not repeat
                            phase3.start();
                        });
                    });
                    phase2.setRepeats(false); // This timer will not repeat
                    phase2.start();
                });
            });
            phase1.setRepeats(false); // This timer will not repeat
            phase1.start();
        });
    }

    public void startBossGetDefeated() {
        //phase 1
        SwingUtilities.invokeLater(() -> {
            bossGetHurtTimer = repeatBossGetHurt(100);
            bossGetHurtExplosionTimer = repeatBossGetHurtExplosion(100);

            //phase 2, boss stop explode, wait for a bigger explosion
            Timer phase1 = new Timer(4000, e1 -> {
                SwingUtilities.invokeLater(() -> {
                    bossGetHurtTimer.stop();
                    bossGetHurtExplosionTimer.stop();

                    //make sure the image is back to its location and hide the explosion completely
                    Timer phase2 = new Timer(200, e2 -> {
                        SwingUtilities.invokeLater(() -> {
                            enemyLeviathanPanel.showImageCenter();
                            explosionBossPanel1.hideImage();
                            explosionBossPanel2.hideImage();

                            //phase 3
                            Timer phase3 = new Timer(50, e3 -> {
                                SwingUtilities.invokeLater(() -> {
                                    startBossExplosion(); //Start the method that make the boss explode

                                    //phase 4 - finale stage
                                    Timer phase4 = new Timer(2600, e4 -> {
                                        SwingUtilities.invokeLater(() -> {
                                            startBossFinaleDefeated();
                                        });
                                    });
                                    phase4.setRepeats(false); // This timer will not repeat
                                    phase4.start();
                                });
                            });
                            phase3.setRepeats(false); // This timer will not repeat
                            phase3.start();
                        });
                    });
                    phase2.setRepeats(false); // This timer will not repeat
                    phase2.start();
                });
            });
            phase1.setRepeats(false); // This timer will not repeat
            phase1.start();
        });
    }

    //==========================================================================
    public void travelToLevel(int level) {
        // phase 1
        SwingUtilities.invokeLater(() -> {
            portalBlackBiggerTimer = repeatPortalBlackBigger(50);
            travelSpeedPlayerTimer = repeatPlayerSpeedTravel(80);

            // phase 2: initial delay before starting the transition
            Timer phase1 = new Timer(3000, e1 -> {
                SwingUtilities.invokeLater(() -> {
                    portalBlackBiggerTimer.stop();
                    // Make every background panel hide
                    hideAllBackgrounds();

                    transitionBackgroundPanel.showImage();
                    portalBlackPanel.hideImage();

                    // phase: hide the portal black panel after a short delay
                    Timer phase2 = new Timer(200, e2 -> {
                        SwingUtilities.invokeLater(() -> {
                            portalBlackPanel.hideImage();

                            // phase 3: delay for transition to the next background
                            Timer phase3 = new Timer(5000, e3 -> {
                                SwingUtilities.invokeLater(() -> {
                                    transitionBackgroundPanel.hideImage();

                                    // Show the appropriate level background
                                    showLevelBackground(level);

                                    portalSpaceBiggerTimer = repeatPortalSpaceBigger(30);

                                    // phase 4: manage the final steps of this transition
                                    Timer phase4 = new Timer(2000, e4 -> {
                                        SwingUtilities.invokeLater(() -> {
                                            portalSpaceBiggerTimer.stop();
                                            portalSpacePanel.hideImage();
                                            travelSpeedPlayerTimer.stop();
                                            speedTravelPlayerPanel1.hideImage();
                                            speedTravelPlayerPanel2.hideImage();

                                            if (level == 5 && manager.getPlayerManager().getPlayerLevel() != 0 && (difficultyMode == 1 || difficultyMode == 2 || difficultyMode == 3)) {
                                                // Getlevel to make sure it is not endless mode (0)
                                                // DifficultyMode range from 1 - 3 is story mode
                                                repeatCheckingListEmptyAndStartBoss(500); //check every 500ms
                                            }

                                            // make sure the image is hide completely
                                            Timer phase5 = new Timer(200, e5 -> {
                                                SwingUtilities.invokeLater(() -> {
                                                    portalSpacePanel.hideImage();
                                                    speedTravelPlayerPanel1.hideImage();
                                                    speedTravelPlayerPanel2.hideImage();
                                                });
                                            });
                                            phase5.setRepeats(false); // This timer will not repeat
                                            phase5.start();
                                        });
                                    });
                                    phase4.setRepeats(false); // This timer will not repeat
                                    phase4.start();
                                });
                            });
                            phase3.setRepeats(false); // This timer will not repeat
                            phase3.start();
                        });
                    });
                    phase2.setRepeats(false); // This timer will not repeat
                    phase2.start();
                });
            });
            phase1.setRepeats(false); // This timer will not repeat
            phase1.start();
        });
    }

// Method to hide all background panels
    private void hideAllBackgrounds() {
        backgroundLevel1Panel.hideImage();
        backgroundLevel2Panel.hideImage();
        backgroundLevel3Panel.hideImage();
        backgroundLevel4Panel.hideImage();
        backgroundLevel5Panel.hideImage();
        backgroundBossPanel1.hideImage();
        backgroundBossPanel2.hideImage();
        backgroundBossPanel3.hideImage();
    }

// Method to show the appropriate level background based on the level
    private void showLevelBackground(int level) {
        SwingUtilities.invokeLater(() -> {
            switch (level) {
                case 1:
                    backgroundLevel1Panel.showImage();
                    break;
                case 2:
                    backgroundLevel2Panel.showImage();
                    break;
                case 3:
                    backgroundLevel3Panel.showImage();
                    break;
                case 4:
                    backgroundLevel4Panel.showImage();
                    break;
                case 5:
                    backgroundLevel5Panel.showImage();
                    break;
                case 6: // Assuming this is for the boss stage
                    backgroundBossPanel1.showImage(); // or whichever boss stage you want to display
                    break;
                case 7:
                    backgroundBossPanel2.showImage();
                    break;
                case 8:
                    backgroundBossPanel3.showImage();
                    break;
                default:
                    MainManager.warningDisplayAndLog("The background didn't show");
                    // Handle invalid level case if necessary
                    break;
            }
        });
    }

    //==========================================================================
    public void startBossOnsetPhases() {
        // phase 1
        Timer phase1 = new Timer(50, e -> {
            SwingUtilities.invokeLater(() -> {
                MainManager.logInfo("Boss about to appear");

                // phase 2
                Timer phase2 = new Timer(100, e2 -> {
                    SwingUtilities.invokeLater(() -> {
                        warningBossTimer = repeatAlertBossPanel(300);
                        MainManager.logInfo("Warning Boss panel is now showing");

                        // phase 3
                        Timer phase3 = new Timer(3000, e3 -> {
                            SwingUtilities.invokeLater(() -> {
                                portalBlackPanel.showImage(550, 550);
                                warningBossTimer.stop();
                                alertBossPanel.hideImage();
                                emergenceBossTimer = repeatBossEmerge(150);
                                travelSpeedBossTimer = repeatBossSpeedTravel(80);
                                MainManager.logInfo("Boss is now emerging from the void");
                                // You can do more here, such as showing the boss panel

                                // phase 4
                                Timer phase4 = new Timer(2500, e4 -> {
                                    SwingUtilities.invokeLater(() -> {
                                        portalBlackPanel.hideImage();
                                        emergenceBossTimer.stop();
                                        travelSpeedBossTimer.stop();
                                        speedTravelEnemyPanel1.hideImage();
                                        speedTravelEnemyPanel2.hideImage();
                                        MainManager.logInfo("Boss is here");

                                        //make sure the image is hide completely
                                        Timer phase5 = new Timer(200, e5 -> {
                                            SwingUtilities.invokeLater(() -> {
                                                portalSpacePanel.hideImage();
                                                speedTravelEnemyPanel1.hideImage();
                                                speedTravelEnemyPanel2.hideImage();
                                                repeatAlertLeviathanLabelTimer = repeatAlertLeviathanLabel(100);

                                                //phase 5
                                                Timer phase6 = new Timer(2000, e6 -> {
                                                    SwingUtilities.invokeLater(() -> {
                                                        repeatAlertLeviathanLabelTimer.stop();
                                                        startBossAttackPhases();
                                                        MainManager.logInfo("Initilize boss attack phases");

                                                        //make sure it is hide
                                                        Timer phase7 = new Timer(200, e7 -> {
                                                            SwingUtilities.invokeLater(() -> {
                                                                theLeviathanLabel.setVisible(false);
                                                            });
                                                        });
                                                        phase7.setRepeats(false); // This timer will not repeat
                                                        phase7.start();
                                                    });
                                                });
                                                phase6.setRepeats(false); // This timer will not repeat
                                                phase6.start();
                                            });
                                        });
                                        phase5.setRepeats(false); // This timer will not repeat
                                        phase5.start();
                                    });
                                });
                                phase4.setRepeats(false); // This timer will not repeat
                                phase4.start();
                            });
                        });
                        phase3.setRepeats(false); // This timer will not repeat
                        phase3.start();
                    });
                });
                phase2.setRepeats(false); // This timer will not repeat
                phase2.start();
            });
        });
        phase1.setRepeats(false); // This timer will not repeat
        phase1.start();
    }

    public void startBossAttackPhases() {
        initialScoreForBossPhase = manager.getPlayerManager().getPlayerScore();

        bossHealthBar.setVisible(true);

        astralRaidersSpawningNumber = 2; //For balancing purpose

        switch (difficultyMode) {
            case 1:
                //UFO Balance:
                setUfoStrength(maxEnemyUFO, 19000, 2000, 3500);
                //Night Stalker:
                setNsStrength(maxEnemyNS, 17000, 5000, 6500);
                //Astral Raider Balance
                setArStrength(maxEnemyAR, 27000, 4000, 5000);
                break;
            case 2:
                //UFO Balance:
                setUfoStrength(maxEnemyUFO, 10000, 1500, 3000);
                //Night Stalker:
                setNsStrength(maxEnemyNS, 8000, 5000, 6000);
                //Astral Raider Balance
                setArStrength(maxEnemyAR, 16000, 3000, 4000);
                break;
            case 3:
                //UFO Balance:
                setUfoStrength(maxEnemyUFO, 6000, 1000, 2000);
                //Night Stalker:
                setNsStrength(maxEnemyNS, 3800, 3500, 5000);
                //Astral Raider Balance
                setArStrength(maxEnemyAR, 8000, 4000, 6000);
                break;
            default:
                MainManager.warningDisplayAndLog("The difficulty wasnt set properly!");
                break;
        }

        setEnemyAppear(true, true, true, true);

        startGenerateUFO();
        startGenerateUFO(); //generate 2 times
        startGenerateNS();
        startGenerateAR();
    }

    private void decreaseBossHealthBar(int value) {
        int currentHealth = bossHealthBar.getValue();
        int newHealth = currentHealth - value;

        // Ensure health doesn’t drop below 0
        if (newHealth < 0) {
            newHealth = 0;
        }

        // Use a final variable to satisfy the lambda requirements
        final int finalNewHealth = newHealth;
        SwingUtilities.invokeLater(() -> bossHealthBar.setValue(finalNewHealth));
    }

    private void decreasePlayerHealthBar(int value) {
        int currentHealth = playerHealthBar.getValue();
        int newHealth = currentHealth - value;

        // Ensure health doesn’t drop below 0
        if (newHealth < 0) {
            newHealth = 0;
        }

        // Use a final variable to satisfy the lambda requirements
        final int finalNewHealth = newHealth;
        SwingUtilities.invokeLater(() -> playerHealthBar.setValue(finalNewHealth));
    }

    private Timer EndlessModeDifficultyDirector(int delayBetween) {
        Timer repeatingTimer = new Timer(delayBetween, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    // Safeguard to ensure values do not go below minimum thresholds
                    attackIntervalUFO = Math.max(100, attackIntervalUFO - 300); //Safe guard to 100 max
                    minAppearTimeUFO = Math.max(200, minAppearTimeUFO - 150); //Safeguard to 1000 max
                    maxAppearTimeUFO = Math.max(minAppearTimeUFO + 300, maxAppearTimeUFO - 150); // Ensure maxAppearTime stays larger than minAppearTime, safeguard to 2500 max
                    // Ensure when the safeguard is triggered on 3 status, it wont print out again
                    if (attackIntervalUFO == 100 && minAppearTimeUFO == 200 && maxAppearTimeUFO == 500) {
                        //Do nothing when the max is hit
                    } else {
                        MainManager.logInfo("Enemy UFO getting stronger");
                    }

                    if (manager.getPlayerManager().getPlayerScore() >= 20) {
                        if (!endlessModeSpawnNS) {
                            endlessModeSpawnNS = true;
                            MainManager.logInfo("Start generating NS");
                            enemyNsAppear = true;
                            startGenerateNS();
                        }
                        attackIntervalNS = Math.max(100, attackIntervalNS - 400);
                        minAppearTimeNS = Math.max(800, minAppearTimeNS - 400);
                        maxAppearTimeNS = Math.max(minAppearTimeNS + 500, maxAppearTimeNS - 400);
                        // Ensure when the safeguard is triggered on 3 status, it wont print out again
                        if (attackIntervalNS == 100 && minAppearTimeNS == 500 && maxAppearTimeNS == 1000) {
                            //Do nothing when the max is hit
                        } else {
                            MainManager.logInfo("Enemy NS getting stronger");
                        }
                    }

                    if (manager.getPlayerManager().getPlayerScore() >= 40) {
                        if (!endlessModeSpawnAR) {
                            endlessModeSpawnAR = true;
                            MainManager.logInfo("Start generating AR");
                            enemyArAppear = true;
                            startGenerateAR();
                        }
                        attackIntervalAR = Math.max(100, attackIntervalAR - 400);
                        minAppearTimeAR = Math.max(500, minAppearTimeAR - 500);
                        maxAppearTimeAR = Math.max(minAppearTimeAR + 1000, maxAppearTimeAR - 500);
                        // Ensure when the safeguard is triggered on 3 status, it wont print out again
                        if (attackIntervalAR == 100 && minAppearTimeAR == 500 && maxAppearTimeAR == 1500) {
                            //Do nothing when the max is hit
                        } else {
                            MainManager.logInfo("Enemy AR getting stronger");
                        }
                    }

                    if (manager.getPlayerManager().getPlayerScore() >= 60 && endlessModeSpawnCountAR == 1) {
                        endlessModeSpawnCountAR = 2; //Increase the flag so the next level spawning of AR can be triggered without issues
                        astralRaidersSpawningNumber = 2;
                        MainManager.logInfo("Enemy AR each time defeated now spawning 2 clones");
                    }

                    if (manager.getPlayerManager().getPlayerScore() >= 80 && endlessModeSpawnCountAR == 2) {
                        endlessModeSpawnCountAR = 3; //Increase the flag so the next level spawning of AR can be triggered without issues
                        astralRaidersSpawningNumber = 3;
                        MainManager.logInfo("Enemy AR each time defeated now spawning 3 clones");
                    }

                    if (manager.getPlayerManager().getPlayerScore() >= 150 && endlessModeSpawnCountAR == 3) {
                        endlessModeSpawnCountAR = 4; //Increase the flag so the next level spawning of AR can be triggered without issues
                        astralRaidersSpawningNumber = 4;
                        MainManager.logInfo("Enemy AR each time defeated now spawning 4 clones");
                    }

                    if (manager.getPlayerManager().getPlayerScore() >= 300 && endlessModeSpawnCountAR == 4) {
                        endlessModeSpawnCountAR = 5;
                        astralRaidersSpawningNumber = 5;
                        MainManager.logInfo("Enemy AR each time defeated now spawning 5 clones");
                    }
                });
            }
        });
        repeatingTimer.setRepeats(true);
        repeatingTimer.start();
        return repeatingTimer; // Return the Timer instance so it can be stopped later if needed
    }

    private void EndlessModeRandomStageTravel() {
        SwingUtilities.invokeLater(() -> {
            Random random = new Random();
            int stage = 1 + random.nextInt(8);

            switch (stage) {
                case 1:
                    travelToLevel(1);
                    break;
                case 2:
                    travelToLevel(2);
                    break;
                case 3:
                    travelToLevel(3);
                    break;
                case 4:
                    travelToLevel(4);
                    break;
                case 5:
                    travelToLevel(5);
                    break;
                case 6:
                    travelToLevel(6);
                    break;
                case 7:
                    travelToLevel(7);
                    break;
                case 8:
                    travelToLevel(8);
                    break;
            }
        });
    }

    public void levelManager() {
        SwingUtilities.invokeLater(() -> {
            int currentLevel = manager.getPlayerManager().getPlayerLevel();

            if (currentLevel == 2) {
                travelToLevel(2);
            }
            if (currentLevel == 3) {
                travelToLevel(3);
            }
            if (currentLevel == 4) {
                setEnemyAppear(false, false, true, enemyLeviathanAppear);

                MainManager.logInfo("Starting Astral Raider generation");
                startGenerateAR();
                travelToLevel(4);
            }
            if (currentLevel == 5) {
                setEnemyAppear(false, false, false, enemyLeviathanAppear);
                travelToLevel(5);
            }
        });
    }

    public void difficultyManager() { //DifficultyManager wil always be called after an enemy is defeated or hurt the player (all enemy - threads)
        SwingUtilities.invokeLater(() -> {
            int playerScore = manager.getPlayerManager().getPlayerScore();
            int currentLevel = manager.getPlayerManager().getPlayerLevel();

            //ENDLESS MODE
            if (currentLevel == 0 && playerScore >= endlessModeScoreToTravel) {
                endlessModeScoreToTravel += 20;
                EndlessModeRandomStageTravel();
            }

            //need 10 more score to hit level 2
            if (currentLevel == 1 && playerScore >= 10) {
                MainManager.logInfo("Level 2");
                //Start level 2
                switch (difficultyMode) {
                    case 1:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, attackIntervalUFO, 2000, 5000);
                        break;
                    case 2:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, attackIntervalUFO, 2000, 4000);
                        break;
                    case 3:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, attackIntervalUFO, 2000, 3000);
                        break;
                }
                MainManager.logInfo("Starting Night Stalker generation");
                enemyNsAppear = true;
                startGenerateNS();

                manager.getPlayerManager().incrementPlayerLevel();
                levelManager();
            }

            //need 15 more score to hit level 3
            if (currentLevel == 2 && playerScore >= 25) {
                MainManager.logInfo("Level 3");
                switch (difficultyMode) {
                    case 1:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, attackIntervalUFO, 2000, 4000);
                        //Night Stalker:
                        setNsStrength(maxEnemyNS, attackIntervalNS, 5000, 7000);
                        break;
                    case 2:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, attackIntervalUFO, 1500, 4000);
                        //Night Stalker:
                        setNsStrength(maxEnemyNS, attackIntervalNS, 5000, 6000);
                        break;
                    case 3:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, attackIntervalUFO, 1000, 3000);
                        //Night Stalker:
                        setNsStrength(maxEnemyNS, attackIntervalNS, 4000, 5000);
                        break;
                }

                manager.getPlayerManager().incrementPlayerLevel();
                levelManager();
            }

            //need 20 more score to hit level 4
            if (currentLevel == 3 && playerScore >= 45) {
                MainManager.logInfo("Level 4");
                manager.getPlayerManager().incrementPlayerLevel();
                levelManager();
            }

            //need 25 more score to hit level 4
            if (currentLevel == 4 && playerScore >= 70) {
                MainManager.logInfo("Level 5");
                manager.getPlayerManager().incrementPlayerLevel();
                levelManager();
            }

            int hurtScore = 20;

            //Boss phase: Leviathan
            if (currentLevel == 5 && enemyLeviathanAppear && playerScore >= initialScoreForBossPhase + hurtScore * 1 && currentBossState
                    == 1) {
                MainManager.logInfo("Travel to boss stage 1, current boss health = 75%");
                decreaseBossHealthBar(25);
                travelToLevel(6);
                startBossGetHurt();
                currentBossState = 2;

                switch (difficultyMode) {
                    case 1:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, 18000, 2000, 3000);
                        //Night Stalker:
                        setNsStrength(maxEnemyNS, 16000, 5000, 8000);
                        //Astral Raider Balance
                        setArStrength(maxEnemyAR, 26000, 4000, 6000);
                        break;
                    case 2:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, 10000, 1000, 3000);
                        //Night Stalker:
                        setNsStrength(maxEnemyNS, 7000, 4500, 5500);
                        //Astral Raider Balance
                        setArStrength(maxEnemyAR, 14000, 3000, 4000);
                        break;
                    case 3:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, 6000, 800, 1500);
                        //Night Stalker:
                        setNsStrength(maxEnemyNS, 3500, 3000, 5000);
                        //Astral Raider Balance
                        setArStrength(maxEnemyAR, 8000, 3000, 6000);
                        astralRaidersSpawningNumber = 3;
                        break;
                    default:
                        MainManager.warningDisplayAndLog("The difficulty wasnt set properly!");
                        break;
                }

            }
            if (currentLevel == 5 && enemyLeviathanAppear && playerScore >= initialScoreForBossPhase + hurtScore * 2 && currentBossState
                    == 2) {
                MainManager.logInfo("Travel to boss stage 2, current boss health = 50%");
                decreaseBossHealthBar(25);
                travelToLevel(7);
                startBossGetHurt();
                currentBossState = 3;

                switch (difficultyMode) {
                    case 1:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, 17000, 2000, 2500);
                        //Night Stalker:
                        setNsStrength(maxEnemyNS, 15000, 4000, 5000);
                        //Astral Raider Balance
                        setArStrength(maxEnemyAR, 25000, 4000, 5000);
                        break;
                    case 2:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, 9000, 1000, 2000);
                        //Night Stalker:
                        setNsStrength(maxEnemyNS, 7000, 4000, 5000);
                        //Astral Raider Balance
                        setArStrength(maxEnemyAR, 12000, 2000, 4000);
                        astralRaidersSpawningNumber = 3;
                        break;
                    case 3:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, 6000, 800, 1500);
                        //Night Stalker:
                        setNsStrength(maxEnemyNS, 3500, 3000, 5000);
                        //Astral Raider Balance
                        setArStrength(maxEnemyAR, 8000, 2000, 4000);
                        break;
                    default:
                        MainManager.warningDisplayAndLog("The difficulty wasnt set properly!");
                        break;
                }

            }
            if (currentLevel == 5 && enemyLeviathanAppear && playerScore >= initialScoreForBossPhase + hurtScore * 3 && currentBossState
                    == 3) {
                MainManager.logInfo("Travel to boss stage 3, current boss health = 25%");
                decreaseBossHealthBar(25);
                travelToLevel(8);
                startBossGetHurt();
                currentBossState = 4;

                switch (difficultyMode) {
                    case 1:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, 16000, 1000, 2000);
                        //Night Stalker:
                        setNsStrength(maxEnemyNS, 14000, 3000, 4000);
                        //Astral Raider Balance
                        setArStrength(maxEnemyAR, 24000, 2000, 3000);
                        break;
                    case 2:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, 8000, 500, 2000);
                        //Night Stalker:
                        setNsStrength(maxEnemyNS, 6000, 2000, 4000);
                        //Astral Raider Balance
                        setArStrength(maxEnemyAR, 10000, 800, 2000);
                        break;
                    case 3:
                        //UFO Balance:
                        setUfoStrength(maxEnemyUFO, 6000, 500, 1000);
                        //Night Stalker:
                        setNsStrength(maxEnemyNS, 3000, 2000, 4000);
                        //Astral Raider Balance
                        setArStrength(maxEnemyAR, 8000, 1000, 4000);
                        astralRaidersSpawningNumber = 2; //For balancing purpose
                        startGenerateNS(); //Spawn another NS
                        break;
                }
            }
            if (currentLevel == 5 && enemyLeviathanAppear && playerScore >= initialScoreForBossPhase + hurtScore * 4 && currentBossState
                    == 4) {
                MainManager.logInfo("Boss health depleted! Boss has been defeated!");
                decreaseBossHealthBar(25);
                startBossGetDefeated();
                currentBossState = 5;

                gameOver();
            }
        });
    }

    public void receiveDamage() {
        SwingUtilities.invokeLater(() -> {
            int playerLives = manager.getPlayerManager().getPlayerLives();

            if (playerLives == 5) {
                manager.getPlayerManager().decrementPlayerLives();
                MainManager.logInfo("Receive Damage! Lives: 4");
                // Ensure the image is updated on the EDT
                damagePanel1.showImage(1380, 80, 300, 350);
                decreasePlayerHealthBar(20);
                updateStatusOverlay();
            }
            if (playerLives == 4) {
                manager.getPlayerManager().decrementPlayerLives();
                MainManager.logInfo("Receive Damage! Lives: 3");
                // Ensure the image is updated on the EDT
                damagePanel2.showImage(1, 450, 250, 300);
                decreasePlayerHealthBar(20);
                updateStatusOverlay();
            }
            if (playerLives == 3) {
                manager.getPlayerManager().decrementPlayerLives();
                MainManager.logInfo("Receive Damage! Lives: 2");
                // Ensure the image is updated on the EDT
                damagePanel3.showImage(120, 50, 350, 200);
                decreasePlayerHealthBar(20);
                updateStatusOverlay();
            }
            if (playerLives == 2) {
                manager.getPlayerManager().decrementPlayerLives();
                MainManager.logInfo("Receive Damage! Lives: 1");
                // Ensure the image is updated on the EDT
                damagePanel4.showImage(700, 10, 250, 300);
                decreasePlayerHealthBar(20);
                spaceshipPanel.hideImage();
                spaceshipBadConditionPanel.showImage();
                updateStatusOverlay();
            }
            if (playerLives == 1) {
                manager.getPlayerManager().decrementPlayerLives();
                MainManager.logInfo("Receive Damage! Lives: 0");
                MainManager.logInfo("You Lose!");
                GameGUI.this.dispose();  // Ensure the JFrame is disposed safely
                decreasePlayerHealthBar(20);
                youLoseScreen = new YouLoseGUI(manager, eliminatedByWho);
                gameOver();
            }
        });
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void incrementActiveUfoThreads() {
        activeUfoThreads.incrementAndGet();
    }

    public void decrementActiveUfoThreads() {
        if (activeUfoThreads.get() > 0) {
            activeUfoThreads.decrementAndGet();
        } else {
            MainManager.logWarning("Active UFO threads cannot be decremented below 0.");
        }
    }

    public void incrementActiveNsThreads() {
        activeNsThreads.incrementAndGet();
    }

    public void decrementActiveNsThreads() {
        if (activeNsThreads.get() > 0) {
            activeNsThreads.decrementAndGet();
        } else {
            MainManager.logWarning("Active NS threads cannot be decremented below 0.");
        }
    }

    public void incrementActiveArThreads() {
        activeArThreads.incrementAndGet();
    }

    public void decrementActiveArThreads() {
        if (activeArThreads.get() > 0) {
            activeArThreads.decrementAndGet();
        } else {
            MainManager.logWarning("Active AR threads cannot be decremented below 0.");
        }
    }

    public int getMinAppearTimeUFO() {
        return minAppearTimeUFO;
    }

    public int getMinAppearTimeNS() {
        return minAppearTimeNS;
    }

    public int getMinAppearTimeAR() {
        return minAppearTimeAR;
    }

    public int getMaxAppearTimeUFO() {
        return maxAppearTimeUFO;
    }

    public int getMaxAppearTimeNS() {
        return maxAppearTimeNS;
    }

    public int getMaxAppearTimeAR() {
        return maxAppearTimeAR;
    }

    public int getAttackIntervalUFO() {
        return attackIntervalUFO;
    }

    public int getAttackIntervalNS() {
        return attackIntervalNS;
    }

    public int getAttackIntervalAR() {
        return attackIntervalAR;
    }

    public boolean isInitialStart() {
        return isInitialStart;
    }

    public void setInitialStart(boolean state) {
        isInitialStart = state;
    }

    public void setMaxEnemyUFO(int number) {
        maxEnemyUFO = number;
    }

    public EnemyUfoPanel getEnemyUfoPanel() {
        return enemyUfoPanel;
    }

    public EnemyNsPanel getEnemyNsPanel() {
        return enemyNsPanel;
    }

    public EnemyArPanel getEnemyArPanel() {
        return enemyArPanel;
    }

    public ExplosionPanel getExplosionPanel() {
        return explosionPanel;
    }

    public DamageReceivedPanel getDamageReceivedPanel() {
        return damageReceivedPanel;
    }

    public DamageAlertPanel getDamageAlertPanel() {
        return damageAlertPanel;
    }

    public int getEliminatedByWho() {
        return eliminatedByWho;
    }

    public void setEliminatedByWho(int eliminatedByWho) {
        this.eliminatedByWho = eliminatedByWho;
    }

    public boolean isSpawnMoreAstralRaiders() {
        return spawnMoreAstralRaiders;
    }

    public void setSpawnMoreAstralRaiders(boolean count) {
        spawnMoreAstralRaiders = count;
    }

    public int getAstralRaidersCount() {
        return astralRaidersCount;
    }

    public void setAstralRaidersCount(int count) {
        astralRaidersCount = count;
    }

    public boolean isAllAstralRaidersDefeated() {
        return allAstralRaidersDefeated;
    }

    public void setAllAstralRaidersDefeated(boolean count) {
        allAstralRaidersDefeated = count;
    }

    public void decrementAstralRaidersCount() {
        astralRaidersCount -= 1;
    }

    public int getAstralRaidersSpawningNumber() {
        return astralRaidersSpawningNumber;
    }

    public void setAstralRaidersSpawningNumber(int number) {
        astralRaidersSpawningNumber = number;
    }

    public void updateInputValidationStatus(int validateInput) {
        // Update the validation status immediately
        playerNameLabel.updateValidationStatus(validateInput);

        // If the timer is running, stop it
        if (updateInputValidationStatusTimer != null && updateInputValidationStatusTimer.isRunning()) {
            updateInputValidationStatusTimer.stop(); // Stop the previous timer
        }

        // Create a new timer with an ActionListener
        updateInputValidationStatusTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the validation status to default after 1 second
                SwingUtilities.invokeLater(() -> playerNameLabel.updateValidationStatus(-1));
                updateInputValidationStatusTimer.stop(); // Stop the timer after executing
            }
        });

        // Start or restart the timer
        updateInputValidationStatusTimer.start(); // Start the timer
    }
}
