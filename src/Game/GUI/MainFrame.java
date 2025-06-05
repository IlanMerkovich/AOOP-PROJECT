package Game.GUI;

import Game.Audio.SoundManager;
import Game.Engine.GameController;
import Game.Engine.GameWorld;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(GameWorld gameWorld, GameController gameController) {
        super("D&D Based Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        StatusPanel statusPanel = new StatusPanel(gameWorld);
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));

        InventoryPanel inventoryPanel = new InventoryPanel(gameController,gameWorld);
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));

        MapPanel mapPanel = new MapPanel(gameController,gameWorld);
        mapPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        add(mapPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(5, 5));

        bottom.add(statusPanel, BorderLayout.WEST);
        bottom.add(inventoryPanel, BorderLayout.CENTER);

        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        SoundManager.playLoop("background.wav", 0.3f);
        setVisible(true);
    }
}
