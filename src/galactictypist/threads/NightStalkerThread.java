package galactictypist.threads;

import galactictypist.GameGUI;
import galactictypist.Word;
import java.awt.Color;
import java.util.List;
import java.util.Random;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Ainzle
 */
public class NightStalkerThread implements Runnable {

    private Word currentWord;
    private final GameGUI game;
    private List<Word> activeWordsNS;

    public NightStalkerThread(GameGUI game, List<Word> activeWordsNS) {
        this.game = game;
        this.activeWordsNS = activeWordsNS;
    }

    @Override
    public void run() {
        try {
            game.incrementActiveNsThreads(); //Increase counter when this enemy type is generated
            if (Thread.currentThread().isInterrupted()) {
                return;  // Exit if the thread was interrupted
            }

            // Generate a word at a random time
            Thread.sleep(game.getMinAppearTimeNS() + new Random().nextInt(game.getMaxAppearTimeNS()));  // Random delay before showing the word

            // If the game is over or interrupted, exit the thread
            if (Thread.currentThread().isInterrupted() || game.isGameOver()) {
                return;  // Exit if interrupted or game is over
            }

            currentWord = game.getMainManager().getWordManager().addRandomWordForNightStalker();
            currentWord.setTextColor(Color.MAGENTA); // Set color for NS

            synchronized (activeWordsNS) {
                activeWordsNS.add(currentWord);  // Add the new word to the active list
            }

            //Ensure that all calls to repaint() and UI updates happen on the EDT to avoid any Swing-related thread issues. 
            SwingUtilities.invokeLater(() -> game.getEnemyNsPanel().repaint());  // Repaint the enemy UFOs

            int sleepDuration = game.getAttackIntervalNS();
            int elapsed = 0;
            int interval = 100; //check every 100ms (0.1 sec)
            boolean wordMatched = false;  // Track if the word is matched
            while (elapsed < sleepDuration && !Thread.currentThread().isInterrupted() && !game.isGameOver()) {
                if (currentWord.isMatched()) {
                    wordMatched = true;
                    handleExplosion(); // Show explosion in the explosion panel
                    break;
                }
                //If the time attack is already 2/3
                if (elapsed >= (2.0 / 3.0) * sleepDuration) {
                    currentWord.setTextColor(Color.RED);
                    SwingUtilities.invokeLater(() -> game.getEnemyUfoPanel().repaint());
                }
                Thread.sleep(interval);
                elapsed += interval;
            }

            handleEnemyRemoval(wordMatched);

            if (!game.isGameOver()) {
                game.startGenerateNS();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Handle thread interruption properly
        } finally {
            game.decrementActiveNsThreads(); //Decrease counter when this enemy type is done
        }
    }

    private void handleExplosion() {
        // Show explosion in the explosion panel
        synchronized (this) {
            // Show explosion panel and hide it after 500ms using a Timer
            SwingUtilities.invokeLater(() -> {
                game.getExplosionPanel().showImage(currentWord.getX() - 120, currentWord.getY() - 80, 600, 600);
                Timer timer = new Timer(500, e -> {
                    game.getExplosionPanel().hideImage();  // Hide explosion panel after 500ms
                });
                timer.setRepeats(false);  // Only run the timer once
                timer.start();  // Start the timer
            });
        }
    }

    private void handleEnemyRemoval(boolean wordMatched) {
        synchronized (activeWordsNS) {
            SwingUtilities.invokeLater(() -> {
                if (activeWordsNS.contains(currentWord)) {
                    activeWordsNS.remove(currentWord);
                    if (!wordMatched) {
                        game.setEliminatedByWho(2);
                        game.receiveDamage();
                        showDamagePanel();
                    }
                    game.getMainManager().getWordManager().removeExpiredWord(currentWord.getId());
                    game.difficultyManager();
                }
                SwingUtilities.invokeLater(() -> game.getEnemyNsPanel().repaint());  // Repaint the enemy NS after word removal
            });
        }
    }

    private void showDamagePanel() {
        // Show damage panel and hide it after 200ms using a Timer
        SwingUtilities.invokeLater(() -> {
            game.getDamageReceivedPanel().showImage(0, 0, 1600, 900);  // Show damage panel
            game.getDamageAlertPanel().showImage(740, 350, 120, 120);
            Timer timer = new Timer(200, e -> {
                game.getDamageReceivedPanel().hideImage();  // Hide damage panel after 200ms
                game.getDamageAlertPanel().hideImage();
            });
            timer.setRepeats(false);  // Only run the timer once
            timer.start();  // Start the timer
        });
    }
}
