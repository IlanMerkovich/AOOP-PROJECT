package Game.GUI;
import Game.Audio.SoundManager;
import Game.Characters.PlayerCharacter;
import Game.Engine.GameController;
import Game.Engine.GameWorld;
import Game.Logs.LogManager;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private PlayerCharacter playerCharacter;

    public MainFrame(GameWorld gameWorld, GameController gameController) {
        super("D&D Based Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        createMenuBar(gameWorld, gameController);
        playerCharacter=gameController.getPlayer();

        StatusPanel statusPanel = new StatusPanel(gameWorld);
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));

        InventoryPanel inventoryPanel = new InventoryPanel(gameController, gameWorld);
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));

        MapPanel mapPanel = new MapPanel(gameController, gameWorld);
        mapPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        add(mapPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(5, 5));

        bottom.add(statusPanel, BorderLayout.WEST);
        bottom.add(inventoryPanel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        SoundManager.playLoop("background.wav", 0.2f);
        setVisible(true);
    }
    private void createMenuBar(GameWorld gameWorld, GameController gameController) {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");

        JMenuItem saveGameItem = new JMenuItem("Save Game");
        saveGameItem.addActionListener(e -> {
            gameWorld.save();
            showStatusMessage("Game saved successfully!");
        });

        JMenuItem loadGameItem = new JMenuItem("Load Game");
        loadGameItem.addActionListener(e -> gameWorld.restore());

        gameMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");

        exitItem.addActionListener(e -> exitGame(gameController));


        gameMenu.add(saveGameItem);
        gameMenu.add(loadGameItem);
        gameMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");

        JMenuItem playerStatsItem = new JMenuItem("Player Stats");
        playerStatsItem.addActionListener(e -> showPlayerStats(gameWorld));

        JMenuItem playerLogs=new JMenuItem("Player Logs");
        playerLogs.addActionListener(e->showLogs());

        JMenuItem scores=new JMenuItem("Player Scores");
        scores.addActionListener(e->showScores());


        viewMenu.add(playerLogs);
        viewMenu.add(playerStatsItem);
        viewMenu.add(scores);

        JMenu audioMenu = new JMenu("Audio");

        JCheckBoxMenuItem muteItem = new JCheckBoxMenuItem("Mute");
        muteItem.addActionListener(e -> {
            if (muteItem.isSelected()) {
                SoundManager.setMasterVolume(0.0f);
            } else {
                SoundManager.setMasterVolume(0.2f);
            }
        });

        audioMenu.add(muteItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem controlsItem = new JMenuItem("Controls");
        controlsItem.addActionListener(e -> showControls());
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAbout());

        helpMenu.add(controlsItem);
        helpMenu.add(aboutItem);

        menuBar.add(gameMenu);
        menuBar.add(viewMenu);
        menuBar.add(audioMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void showPlayerStats(GameWorld gameWorld) {
        PlayerCharacter player = gameWorld.getPlayer();
        String stats = String.format("""
                === PLAYER STATISTICS ===
                
                Name: %s
                Class: %s
                
                Health: %d / 100
                Power: %d
                Treasure Points: %d
                
                Position: %s
                
                Inventory Items: %d
                """,
                player.getName(),
                player.getClass().getSimpleName(),
                player.getHealth(),
                player.getPower(),
                player.getTreasurePoints(),
                player.getPosition(),
                player.getInventory().getItems().size()
        );
        JOptionPane.showMessageDialog(this, stats, "Player Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showControls() {
        String controls = """
                === GAME CONTROLS ===
                
                🖱️ Mouse Controls:
                • Left Click: Move/Attack/Pickup
                • Right Click: Inspect item/enemy
                
                🎮 Gameplay:
                • Click adjacent cells to move
                • Click enemies to attack
                • Click potions to pick them up
                • Click inventory items to use them
                """;

        JOptionPane.showMessageDialog(this, controls, "Game Controls", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAbout() {
        String about = """
                🏰 D&D Based Game
                
                A fantasy RPG adventure game built with Java Swing.
                
                Features:
                • Multiple character classes (Warrior, Archer, Mage)
                • Various enemies (Goblins, Orcs, Dragons)
                • Magic system with elemental combat
                • Inventory and potion system
                • Save/Load functionality
                
                Enjoy your adventure!
                """;

        JOptionPane.showMessageDialog(this, about, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitGame(GameController gameController) {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit? Unsaved progress will be lost.",
                "Exit Game",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            String score=""+gameController.getPlayer().getName()+" Scored "+gameController.getPlayer().getTreasurePoints()+" Points!";
            LogManager.writeScore(score);
            gameController.shutdownEnemies();
            SoundManager.stopAll();
            LogManager.stop();
            System.exit(0);
        }
    }

    private void showStatusMessage(String message) {
        JLabel statusLabel = new JLabel(message);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(144, 238, 144));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(statusLabel, BorderLayout.NORTH);
        revalidate();
        repaint();

        Timer timer = new Timer(1000, e -> {
            remove(statusLabel);
            revalidate();
            repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }
    private void showLogs() {
        JFrame logsFrame = new JFrame("Game Logs");
        logsFrame.setSize(600, 400);
        logsFrame.setLocationRelativeTo(this);

        JTextArea logsArea = new JTextArea();
        logsArea.setEditable(false);
        logsArea.setBackground(new Color(248, 248, 248));
        logsArea.setText(LogManager.getLogs());

        JScrollPane scrollPane = new JScrollPane(logsArea);
        logsFrame.add(scrollPane);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        logsFrame.add(buttonPanel, BorderLayout.SOUTH);
        logsFrame.setVisible(true);
    }

    private void showScores() {
        JFrame logsFrame = new JFrame("Game Scores");
        logsFrame.setSize(400, 200);
        logsFrame.setLocationRelativeTo(this);

        JTextArea logsArea = new JTextArea();
        logsArea.setEditable(false);
        logsArea.setBackground(new Color(248, 248, 248));
        logsArea.setText(LogManager.getPlayerScores());
        JScrollPane scrollPane = new JScrollPane(logsArea);
        logsFrame.add(scrollPane);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        logsFrame.add(buttonPanel, BorderLayout.SOUTH);
        logsFrame.setVisible(true);
    }

}