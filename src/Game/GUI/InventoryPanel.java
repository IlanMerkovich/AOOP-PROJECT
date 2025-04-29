package Game.GUI;

import Game.Characters.PlayerCharacter;
import Game.Engine.GameWorld;
import Game.Items.GameItem;
import Game.Items.Potion;
import Game.Items.PowerPotion;
import Game.Items.Interactable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;
import java.util.List;

/**
 * Shows the player’s current items; click to view/use.
 */
public class InventoryPanel extends JPanel {
    private final GameWorld world;
    private StatusPanel statusPanel;
    private final int iconSize = 32;

    public InventoryPanel(GameWorld world,StatusPanel statusPanel) {
        super(new FlowLayout(FlowLayout.LEFT, 5, 5));
        this.world = world;
        this.statusPanel=statusPanel;
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
        } else {
            for (GameItem item : inv) {
                ImageIcon icon = ImageLoader.load(item.getDisplaySymbol() + ".png", iconSize, iconSize);
                JLabel lbl;
                if (icon != null) {
                    lbl = new JLabel(icon);
                } else {
                    lbl = new JLabel(item.getDisplaySymbol());
                }
                lbl.setToolTipText(item.getDescription());
                lbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                lbl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        boolean used = false;

                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (item instanceof Potion && !(item instanceof PowerPotion)) {
                                used = p.usePotion();
                                statusPanel.rebuild();
                            }
                            else if (item instanceof PowerPotion) {
                                used = p.usePowerPotion();
                                statusPanel.rebuild();
                            }
                            if (!used) {
                                // אפשר להוסיף הודעה או Beep
                                Toolkit.getDefaultToolkit().beep();
                            }
                        } else {
                            // קליק ימני: תיאור
                            JOptionPane.showMessageDialog(
                                    InventoryPanel.this,
                                    item.getDescription(),
                                    item.getDisplaySymbol(),
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                        rebuild();
                    }
                });

                add(lbl);
            }
        }

        revalidate();
        repaint();
    }
}
