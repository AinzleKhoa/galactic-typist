package galactictypist;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import galactictypist.components.panels.config.TextboxConfigPanel;
import galactictypist.components.panels.config.ButtonConfigPanel;
import galactictypist.components.panels.config.SmallButtonConfigPanel;
import galactictypist.components.panels.config.BackgroundConfigPanel;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class ConfigGUI extends JFrame {

    String[] columnNames = {"Rank", "Name", "Score", "Streak", "Accuracy", "Mode"};
    String[] wordColumns = {"Word"};

    private PlayerManager manager;
    private WordManager wordManager;
    private MainManager managerGUI;
    private BackgroundConfigPanel backgroundConfigPanel;
    private TextboxConfigPanel textboxConfigPanel;
    private JPanel menuButton;
    private JPanel addWordButton;
    private JPanel deleteWordButton;
    private JPanel deletePlayerButton;

    private JLayeredPane jPane;
    private JTextArea textAreaMessage; // Use JTextArea instead of JTextField

    private JTable playerTable;
    private DefaultTableModel playerTableModel;

    private JTable wordTable;
    private DefaultTableModel wordTableModel;

    int frameWidth = 700;
    int frameHeight = 1000;
    int buttonWidth = 400;
    int buttonHeight = 80;
    int smallButtonWidth = 200;
    int smallButtonHeight = 40;

    int centerX = (frameWidth - buttonWidth) / 2;
    int centerY = (frameHeight - buttonHeight) / 2;

    private Font customFont;

    public ConfigGUI(PlayerManager manager, WordManager wordManager, MainManager managerGUI) {
        this.wordManager = wordManager;
        this.manager = manager;
        this.managerGUI = managerGUI;

        textAreaMessage = new JTextArea();

        // Load custom font before setup
        loadCustomFont();  // Make sure to load the font first

        System.out.println("Players in configGUI constructor: " + manager.getPlayers().size());

        setupFrame();
        setupBackground();
        setupPlayerTable();  // Initializes playerTable and playerTableModel
        setupWordTable();    // Initializes wordTable and wordTableModel
        setupButtons();
        setupMessageBox();
        setupKeyListenerDelete();
    }

    //============================ Custom Font Loader ============================
    private void loadCustomFont() {
        try {
            // Load the custom font from a resource stream
            java.io.InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/01_DigiGraphics.TTF");

            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.BOLD, 14f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);  // Register the font
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();  // Log the error if any
            customFont = null;  // Set customFont to null to trigger fallback in table setup
        }
    }

    //=============================== Setup Methods ===============================
    private void setupFrame() {
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
                    ConfigGUI.this.dispose();
                    managerGUI.backToMenu();
                });
            }
        });
        jPane = getLayeredPane();  // Initialize the jPane instance variable
    }

    private void setupButtons() {
        setupDeletePlayerButton();
        setupAddWordButton();
        setupDeleteWordButton();
        setupMenuButton();
    }

    private void setupBackground() {
        backgroundConfigPanel = new BackgroundConfigPanel("/resources/images/configBackground.png", frameWidth, frameHeight);
        backgroundConfigPanel.setBounds(0, 0, frameWidth, frameHeight);
        jPane.add(backgroundConfigPanel, Integer.valueOf(0));
    }

    private void setupMessageBox() {
        textboxConfigPanel = new TextboxConfigPanel("/resources/images/textboxmessage.png", textAreaMessage);
        textboxConfigPanel.setBounds(centerX - 200, 670, 230, 130);
        textAreaMessage.setFont(customFont != null ? customFont.deriveFont(Font.PLAIN, 16) : new Font("Roboto", Font.BOLD, 18));
        textAreaMessage.setText("(*_*)     'Delete' on keyboard to delete  row");
        jPane.add(textboxConfigPanel, Integer.valueOf(1));  // Use the instance variable jPane
    }

    private void setupPlayerTable() {
        playerTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Disable editing of the table cells
            }
        };
        playerTable = new JTable(playerTableModel);
        setupTable(playerTable);
        addPlayersToTable(playerTableModel); // Fills the table with data

        // Apply the custom renderer to all columns to color the entire row
        ModeRowRenderer renderer = new ModeRowRenderer();
        for (int i = 0; i < playerTable.getColumnCount(); i++) {
            playerTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane playerScrollPane = new JScrollPane(playerTable);
        configureScrollPane(playerScrollPane, 50, 40, 600, 400);  // Use jPane inside this method
    }

    private void setupWordTable() {
        wordTableModel = new DefaultTableModel(wordColumns, 0);
        wordTable = new JTable(wordTableModel);
        setupTable(wordTable);
        addWordsToTable(wordTableModel);

        addWordTableModelListener();  // Call the new method to add the listener

        JScrollPane wordScrollPane = new JScrollPane(wordTable);
        configureScrollPane(wordScrollPane, 50, 460, 600, 200);  // Use jPane inside this method
    }

    //=============================== KEY LISTENER =================================
    public void setupKeyListenerDelete() {
        // Key listener for Delete key in playerTable
        playerTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                    int selectedRow = playerTable.getSelectedRow();
                    if (selectedRow != -1) {
                        playerTableModel.removeRow(selectedRow);
                        manager.deletePlayer(selectedRow);
                        textAreaMessage.setText("(!) Player Deleted From Row: " + selectedRow);
                        MainManager.logInfo("(!) Player Deleted From Row: " + selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a row to delete (Player scoreboard)", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Key listener for Delete key in wordTable
        wordTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
                    int selectedRow = wordTable.getSelectedRow();
                    if (selectedRow != -1) {
                        wordTableModel.removeRow(selectedRow);
                        wordManager.deleteWord(selectedRow);
                        textAreaMessage.setText("(!) Word Deleted From Row: " + selectedRow);
                        MainManager.logInfo("(!) Word Deleted From Row: " + selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a row to delete (Word table)", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    //=============================== Row Color Depend on Gamemode =================
    private class ModeRowRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Get the value of the "Mode" column (assume it's in the 6th column, index 5)
            String mode = (String) table.getValueAt(row, 5);

            // Set the background color for the entire row based on the mode
            switch (mode) {
                case "Endless":
                    cell.setBackground(Color.BLUE);
                    break;
                case "Easy":
                    cell.setBackground(new Color(0, 100, 0)); // Darker Green
                    break;
                case "Medium":
                    cell.setBackground(new Color(139, 0, 139)); // Darker Magenta
                    break;
                case "Hard":
                    cell.setBackground(new Color(139, 0, 0)); // Darker Red
                    break;
                default:
                    cell.setBackground(Color.DARK_GRAY);  // Default color for "None" or unknown modes
                    break;
            }

            // Keep the selection background color when row is selected
            if (isSelected) {
                cell.setBackground(Color.YELLOW);
            }

            return cell;
        }
    }

    //=============================== Helper Methods ===============================
    private void setupTable(JTable table) {
        table.setOpaque(false);  // Make the table background transparent
        table.setShowGrid(false);  // Remove cell grid lines
        table.setForeground(Color.WHITE);  // Set text color to white
        // Set the custom font for the table if loaded, else fallback
        table.setFont(customFont != null ? customFont.deriveFont(Font.PLAIN, 16) : new Font("Roboto", Font.BOLD, 18));  // Set custom font or default.
        table.setRowHeight(25);  // Set the row height
        table.setBorder(null);  // Remove table border
        table.setSelectionForeground(Color.RED);  // Set selected text color

        // Set up the default renderer to ensure transparency and white text
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setOpaque(false);  // Ensure cells are transparent
        renderer.setForeground(Color.WHITE);  // Set the text color of cells
        table.setDefaultRenderer(Object.class, renderer);  // Apply the custom renderer

        // Customize the table header
        table.getTableHeader().setOpaque(false);  // Make the table header transparent
        table.getTableHeader().setForeground(Color.BLUE);  // Set the header text color
        table.getTableHeader().setFont(customFont != null ? customFont.deriveFont(Font.BOLD, 18) : new Font("Roboto", Font.BOLD, 18));  // Set the custom font for header
        // Set the table to allow only one row to be selected at a time
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void configureScrollPane(JScrollPane scrollPane, int x, int y, int width, int height) {
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        jPane.add(scrollPane, Integer.valueOf(1));  // Use the instance variable jPane
        scrollPane.setBounds(x, y, width, height);
    }

    //=============================== Add To Table ===============================
    private void addPlayersToTable(DefaultTableModel model) {
        List<Player> sortedPlayers = manager.getPlayers()
                .stream()
                .sorted((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()))
                .collect(Collectors.toList());

        for (int i = 0; i < sortedPlayers.size(); i++) {
            Player p = sortedPlayers.get(i);
            Object[] row = {i + 1, p.getName(), p.getScore(), p.getStreak(), p.getFormattedAccuracy(), p.getCompletedGameModeInString()};
            model.addRow(row);
        }
    }

    private void addWordsToTable(DefaultTableModel wordModel) {
        List<Word> words = wordManager.getWords();
        for (Word word : words) {
            wordModel.addRow(new Object[]{word.getText()});
        }
    }

    private void addWordTableModelListener() {
        wordTableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column == 0) {  // Assuming column 0 is for word text
                String newWordText = (String) wordTableModel.getValueAt(row, column);
                if (row >= wordManager.getWords().size()) {
                    wordManager.addWord(newWordText);  // Add a new word if it’s a new row
                    textAreaMessage.setText("(+) New Word Added");  // Display feedback in the text area
                    MainManager.logInfo("(+) New Word Added");
                } else {
                    wordManager.updateWord(row, newWordText);  // Update an existing word
                    textAreaMessage.setText("(*) Word Updated");  // Display feedback in the text area
                    MainManager.logInfo("(*) Word Updated");
                }
            }
        });
    }

    //=============================== Set up Buttons ===============================
    private void setupDeletePlayerButton() {
        deletePlayerButton = new SmallButtonConfigPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "Delete Player", smallButtonWidth, smallButtonHeight, e -> {
            SwingUtilities.invokeLater(() -> {
                int selectedRow = playerTable.getSelectedRow();  // Use the playerTable instance variable
                if (selectedRow != -1) {
                    playerTableModel.removeRow(selectedRow);
                    manager.deletePlayer(selectedRow);
                    textAreaMessage.setText("(!) Player Deleted From Row: " + selectedRow);
                    MainManager.logInfo("(!) Player Deleted From Row: " + selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete (Player scoreboard)", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        });

        deletePlayerButton.setBounds(centerX + 100, 750, smallButtonWidth, smallButtonHeight);
        jPane.add(deletePlayerButton, Integer.valueOf(1));  // Use the instance variable jPane
    }

    private void setupAddWordButton() {
        addWordButton = new SmallButtonConfigPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "Add New Word", smallButtonWidth, smallButtonHeight, e -> {
            SwingUtilities.invokeLater(() -> {
                wordTableModel.insertRow(0, new Object[]{"New Word"});
                textAreaMessage.setText("(+) New Word added");
                MainManager.logInfo("(+) New Word added");
            });
        });

        addWordButton.setBounds(centerX, 800, smallButtonWidth, smallButtonHeight);
        jPane.add(addWordButton, Integer.valueOf(1));  // Use the instance variable jPane
    }

    private void setupDeleteWordButton() {
        deleteWordButton = new SmallButtonConfigPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "Delete Word", smallButtonWidth, smallButtonHeight, e -> {
            SwingUtilities.invokeLater(() -> {
                int selectedRow = wordTable.getSelectedRow();  // Use the wordTable instance variable
                if (selectedRow != -1) {
                    wordTableModel.removeRow(selectedRow);
                    wordManager.deleteWord(selectedRow);
                    textAreaMessage.setText("(!) Word Deleted From Row: " + selectedRow);
                    MainManager.logInfo("(!) Word Deleted From Row: " + selectedRow);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete (Word table)", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        });

        deleteWordButton.setBounds(centerX + 200, 800, smallButtonWidth, smallButtonHeight);
        jPane.add(deleteWordButton, Integer.valueOf(1));  // Use the instance variable jPane
    }

    private void setupMenuButton() {
        menuButton = new ButtonConfigPanel("/resources/images/button-frame.png", "/resources/images/button-frame-hover.png", "Back To Menu", buttonWidth, buttonHeight, e -> {
            SwingUtilities.invokeLater(() -> {
                ConfigGUI.this.dispose();
                managerGUI.backToMenu();
            });
        });
        menuButton.setBounds(centerX, 850, buttonWidth, buttonHeight);
        jPane.add(menuButton, Integer.valueOf(1));  // Use the instance variable jPane
    }
}
