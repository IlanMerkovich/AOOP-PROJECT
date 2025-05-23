package Game.GUI;

import Game.Characters.PlayerCharacter;
import Game.Engine.GameWorld;
import Game.Engine.GameListener;
import Game.Items.GameItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Shows the player’s current items; click to view/use.
 */
public class InventoryPanel extends JPanel implements GameListener {
    private final GameWorld world;
    private final int iconSize = 32;
    private static final Color color1 = new Color(0xF5, 0xE6, 0xC8);

    public InventoryPanel(GameWorld world,StatusPanel statusPanel) {
        super(new FlowLayout(FlowLayout.LEFT, 5, 5));
        this.world = world;
        world.addListener(this);
        this.setBackground(color1);
        setBorder(BorderFactory.createTitledBorder("Inventory"));
        setPreferredSize(new Dimension(300, 80));
        rebuild();
    }

    public void rebuild() {
        removeAll();
        PlayerCharacter p = world.getPlayer();
        List<GameItem> inv = p.getInventory().getItems();

        if (inv.isEmpty()) {
            add(new JLabel("(No items)"));
        }
        else {
            for (GameItem item : inv) {
                ImageIcon icon = ImageLoader.load(item.getDisplaySymbol() + ".png", iconSize, iconSize);
                JLabel lbl;
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
                            world.useItem(item);
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