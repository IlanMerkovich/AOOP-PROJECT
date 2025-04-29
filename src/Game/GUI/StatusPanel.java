package Game.GUI;

import Game.Characters.PlayerCharacter;
import Game.Engine.GameWorld;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * מציג סטטוס עדכני: שם, HP, כוח ו־Treasure Points.
 * משתמש בצבעים אחידים כמו MapPanel.
 */
public class StatusPanel extends JPanel {
    private final GameWorld world;

    private final JLabel nameLabel;
    private final JLabel hpLabel;
    private final JLabel powerLabel;
    private final JLabel treasureLabel;

    public StatusPanel(GameWorld world) {
        this.world = world;

        // soft ivory background (same as MapPanel background)
        setBackground(new Color(255, 255, 250));
        setLayout(new GridLayout(4, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // create labels
        nameLabel     = createStatLabel();
        hpLabel       = createStatLabel();
        powerLabel    = createStatLabel();
        treasureLabel = createStatLabel();

        // bold title font for name
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));

        // add in order
        add(nameLabel);
        add(hpLabel);
        add(powerLabel);
        add(treasureLabel);

        rebuild();
    }

    /** helper to create a label with the same light-gray cell style */
    private JLabel createStatLabel() {
        JLabel lbl = new JLabel("", SwingConstants.LEFT);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(200, 200, 200));               // same as MapPanel cells
        MatteBorder border = BorderFactory.createMatteBorder(
                1, 1, 1, 1, new Color(180, 180, 180)                   // same border color
        );
        lbl.setBorder(border);
        lbl.setForeground(Color.DARK_GRAY);
        lbl.setPreferredSize(new Dimension(100, 24));
        return lbl;
    }

    /** קורא למודל ומשליך את ערכי הסטטוס על התוויות. */
    public void rebuild() {
        PlayerCharacter p = world.getPlayer();
        nameLabel    .setText("Name:     " + p.getName());
        hpLabel      .setText("HP:       " + p.getHealth() + " / " + "100");
        powerLabel   .setText("Power:    " + p.getPower());
        treasureLabel.setText("Treasure: " + p.getTreasurePoints());
    }
}
