/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galactictypist.threads;

import galactictypist.GameGUI;
import galactictypist.MainManager;
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
public class AstralRaiderThread implements Runnable {

    private Word currentWord;
    private final GameGUI game;
    private List<Word> activeWordsAR;

    public AstralRaiderThread(GameGUI game, List<Word> activeWordsAR) {
        this.game = game;
        this.activeWordsAR = activeWordsAR;
    }

    @Override
    public void run() {
        game.incrementActiveArThreads(); // Increase counter when this enemy type is generated
        try {
            if (Thread.currentThread().isInterrupted()) {
                return;  // Exit if the thread was interrupted
            }

            // Generate a word at a random time
            Thread.sleep(game.getMinAppearTimeAR() + new Random().nextInt(game.getMaxAppearTimeAR()));  // Random delay before showing the word

            if (Thread.currentThread().isInterrupted() || game.isGameOver()) {
                return;  // Exit if interrupted or game is over
            }

            currentWord = game.getMainManager().getWordManager().addRandomWordForAstralRaider();
            currentWord.setTextColor(Color.CYAN);

            synchronized (activeWordsAR) {
                activeWordsAR.add(currentWord);  // Add the new word to the active list
            }

            SwingUtilities.invokeLater(() -> game.getEnemyArPanel().repaint());  // Repaint the enemy ARs

            int sleepDuration = game.getAttackIntervalAR();
            int elapsed = 0;
            int interval = 100; // Check every 100ms (0.1 sec)
            boolean wordMatched = false;  // Track if the word is matched
            boolean alertYet = false;

            while (elapsed < sleepDuration && !Thread.currentThread().isInterrupted() && !game.isGameOver()) {
                if (currentWord.isMatched()) {
                    wordMatched = true;
                    spawnEnemyBasedOnState();  // Spawn based on the current state
                    handleExplosion();
                    break;
                }

                if (elapsed >= (2.0 / 3.0) * sleepDuration && !alertYet) {
                    currentWord.setTextColor(Color.RED);
                    SwingUtilities.invokeLater(() -> game.getEnemyUfoPanel().repaint());
                }
                Thread.sleep(interval);
                elapsed += interval;
            }

            handleEnemyRemoval(wordMatched);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Handle thread interruption properly
        } finally {
            game.decrementActiveArThreads(); // Decrease counter when this enemy type is done
        }
    }

    private void spawnEnemyBasedOnState() {
        // If the flag is set to spawn 2 enemies, do so
        if (game.isAllAstralRaidersDefeated()) { //Only spawn when 2 enemies has been defeated.
            if (game.isSpawnMoreAstralRaiders()) {
                synchronized (game) {  // Ensure that both enemies are spawned without interruption
                    MainManager.logInfo("Spawning " + game.getAstralRaidersSpawningNumber() + " additional Astral Raiders.");
                    for (int i = 0; i < game.getAstralRaidersSpawningNumber(); i++) {
                        game.startGenerateAR();
                    }
                }
                game.setAstralRaidersCount(game.getAstralRaidersSpawningNumber());
                game.setAllAstralRaidersDefeated(false); //Spawn and then reset the boolean
                game.setSpawnMoreAstralRaiders(false); //Next time spawn 1 enemy
            } else {
                MainManager.logInfo("Spawning 1 Astral Raider.");
                game.startGenerateAR();  // Spawn only 1 enemy
                game.setAstralRaidersCount(1);
                game.setSpawnMoreAstralRaiders(true); //Next time spawn 3 enemy
                game.setAllAstralRaidersDefeated(true); //Making sure the next time the first enemy defeated doesnt need to check 
                // (If set false, the next enemy can't be spawned)
            }
        }
    }

    private void handleExplosion() {
        // Show explosion in the explosion panel
        synchronized (this) {
            SwingUtilities.invokeLater(() -> {
                game.getExplosionPanel().showImage(currentWord.getX() - 40, currentWord.getY() - 130, 500, 500);
                Timer timer = new Timer(500, e -> game.getExplosionPanel().hideImage());
                timer.setRepeats(false);
                timer.start();
            });
        }
    }

    private void handleEnemyRemoval(boolean wordMatched) {
        synchronized (activeWordsAR) {
            if (activeWordsAR.contains(currentWord)) {
                activeWordsAR.remove(currentWord);
                if (!wordMatched) {
                    game.setEliminatedByWho(3);
                    game.receiveDamage();
                    showDamagePanel();
                }

                game.getMainManager().getWordManager().removeExpiredWord(currentWord.getId());
                game.difficultyManager();
            }
        }

        if (game.getAstralRaidersCount() > 0) {
            game.decrementAstralRaidersCount();
            if (game.getAstralRaidersCount() == 0) {
                // Once all enemies from the current batch are defeated, allow new ones to spawn
                game.setAllAstralRaidersDefeated(true);
            }
        }
        SwingUtilities.invokeLater(() -> game.getEnemyArPanel().repaint()); // Repaint after word removal
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
            timer.setRepeats(false);
            timer.start();
        });
    }
}
