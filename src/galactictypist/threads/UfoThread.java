/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class UfoThread implements Runnable {

    private Word currentWord;
    private final GameGUI game;
    private List<Word> activeWordsUFO;

    public UfoThread(GameGUI game, List<Word> activeWordsUFO) {
        this.game = game;
        this.activeWordsUFO = activeWordsUFO;
    }

    @Override
    public void run() {
        try {
            game.incrementActiveUfoThreads(); //Increase counter when this enemy type is generated
            if (Thread.currentThread().isInterrupted()) {
                return;  // Exit if the thread was interrupted
            }

            // Generate a word at a random time
            Thread.sleep(game.getMinAppearTimeUFO() + new Random().nextInt(game.getMaxAppearTimeUFO()));  // Random delay before showing the word

            // If the game is over or interrupted, exit the thread
            if (Thread.currentThread().isInterrupted() || game.isGameOver()) {
                return;  // Exit if interrupted or game is over
            }

            currentWord = game.getMainManager().getWordManager().addRandomWordForUFO();
            currentWord.setTextColor(Color.WHITE); // Set initial color for UFO

            synchronized (activeWordsUFO) {
                activeWordsUFO.add(currentWord);  // Add the new word to the active list
            }

            //Ensure that all calls to repaint() and UI updates happen on the EDT to avoid any Swing-related thread issues. 
            SwingUtilities.invokeLater(() -> game.getEnemyUfoPanel().repaint());  // Repaint the enemy UFOs

            int sleepDuration = game.getAttackIntervalUFO();
            int elapsed = 0;
            int interval = 100; //check every 100ms (0.1 sec)
            boolean wordMatched = false;  // Track if the word is matched
            while (elapsed < sleepDuration && !Thread.currentThread().isInterrupted() && !game.isGameOver()) {
                if (currentWord.isMatched()) {
                    handleExplosion();
                    wordMatched = true;
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

            // Start the next word after a delay (if game isn't over)
            if (!game.isGameOver()) {
                if (game.isInitialStart() == true) {
                    game.setInitialStart(false);
                    game.setMaxEnemyUFO(1);
                }
                game.startGenerateUFO();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Handle thread interruption properly
        } finally {
            game.decrementActiveUfoThreads(); //Decrease counter when this enemy type is done
        }
    }

    private void handleExplosion() {
        // Show explosion in the explosion panel
        synchronized (this) {
            // Show explosion panel and hide it after 500ms using a Timer
            SwingUtilities.invokeLater(() -> {
                game.getExplosionPanel().showImage(currentWord.getX() - 40, currentWord.getY() - 110, 350, 350);
                Timer timer = new Timer(500, e -> {
                    game.getExplosionPanel().hideImage();  // Hide explosion panel after 500ms
                });
                timer.setRepeats(false);  // Only run the timer once
                timer.start();  // Start the timer
            });
        }
    }

    private void handleEnemyRemoval(boolean wordMatched) {
        // Remove word from the active list after it has expired or matched
        synchronized (activeWordsUFO) {
            SwingUtilities.invokeLater(() -> {
                if (activeWordsUFO.contains(currentWord)) {
                    activeWordsUFO.remove(currentWord);
                    if (!wordMatched) {
                        game.setEliminatedByWho(1);
                        game.receiveDamage();
                        showDamagePanel();
                    }
                    game.getMainManager().getWordManager().removeExpiredWord(currentWord.getId());
                    game.difficultyManager();
                }
                //Ensure that all calls to repaint() and UI updates happen on the EDT to avoid any Swing-related thread issues. 
                game.getEnemyUfoPanel().repaint();  // Repaint the enemy UFOs after word removal
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
