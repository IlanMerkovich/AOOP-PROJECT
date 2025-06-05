package Game.GUI;

import Game.Characters.PlayerCharacter;
import Game.Engine.GameController;
import Game.Engine.GameWorld;
import Game.Engine.GameListener;
import Game.Items.GameItem;
import Game.Items.PowerPotion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Shows the player’s current items; click to view/use.
 */
public class InventoryPanel extends JPanel implements GameListener {
    private final GameController gameController;
    private final int iconSize = 48;
    private static final Color color1 = new Color(0xF5, 0xE6, 0xC8);
    private final ImageIcon potionIcon;
    private final ImageIcon powerPotionIcon;

    public InventoryPanel(GameController gameController,GameWorld gameWorld){
        super(new FlowLayout(FlowLayout.LEFT, 5, 5));
        this.gameController = gameController;
        gameWorld.addListener(this);
        this.setBackground(color1);
        setBorder(BorderFactory.createTitledBorder("Inventory"));
        setPreferredSize(new Dimension(300, 80));
        potionIcon=ImageLoader.load("life_potion.png",iconSize,iconSize);
        powerPotionIcon=ImageLoader.load("power_potion.png",iconSize,iconSize);
        rebuild();
    }

    public void rebuild() {
        removeAll();
        PlayerCharacter p = gameController.getPlayer();
        List<GameItem> inv = p.getInventory().getItems();

        if (inv.isEmpty()) {
            add(new JLabel("(No items)"));
        }
        else {
            for (GameItem item : inv){
                JLabel lbl;
                ImageIcon icon;
                if (item instanceof PowerPotion){
                    icon=powerPotionIcon;
                }
                else {
                    icon=potionIcon;
                }
                if (icon != null) {
                    lbl = new JLabel(icon);
                }
                else {
                    lbl = new JLabel(item.getDisplaySymbol());
                }
                lbl.setToolTipText(item.getDescription());
                lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                lbl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            gameController.useItem(item);
                        }
                        else {
                            JOptionPane.showMessageDialog(InventoryPanel.this, item.getDescription(), item.getDisplaySymbol(), JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                });
                add(lbl);
            }
        }
        revalidate();
        repaint();
    }

    public void changeDetected() {
        rebuild();
    }
    public void onMapChange() {
        return;
    }
}