package Game.GUI;

import Game.Engine.GameWorld;
import Game.GUI.InventoryPanel;
import Game.GUI.MapPanel;
import Game.GUI.StatusPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(GameWorld gameWorld) {
        super("D&D Based Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(5,5));


        // Status panel as before
        StatusPanel statusPanel = new StatusPanel(gameWorld);
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status"));

        InventoryPanel inventoryPanel = new InventoryPanel(gameWorld,statusPanel);
        inventoryPanel.setBorder(BorderFactory.createTitledBorder("Inventory"));

        MapPanel mapPanel = new MapPanel(gameWorld);
        mapPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        add(mapPanel, BorderLayout.CENTER);

        // Bottom container
        JPanel bottom = new JPanel(new BorderLayout(5,5));
        bottom.add(statusPanel,    BorderLayout.WEST);
        bottom.add(inventoryPanel, BorderLayout.CENTER);

        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
