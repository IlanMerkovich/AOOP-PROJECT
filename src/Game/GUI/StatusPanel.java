package Game.GUI;

import Game.Characters.PlayerCharacter;
import Game.Combat.MagicElement;
import Game.Engine.GameWorld;
import Game.Engine.GameListener;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class StatusPanel extends JPanel implements GameListener {
    private final GameWorld world;
    private final int elementIconSize=16;
    private final JLabel nameLabel;
    private final JLabel hpLabel;
    private final JLabel powerLabel;
    private final JLabel treasureLabel;
    private final JLabel elementLabel;

    public StatusPanel(GameWorld world) {
        this.world = world;
        world.addListener(this);
        setPreferredSize(new Dimension(200, 140));

        setBackground(new Color(252, 255, 250));
        setLayout(new GridLayout(5, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        nameLabel = createStatLabel();
        hpLabel = createStatLabel();
        powerLabel = createStatLabel();
        treasureLabel = createStatLabel();
        elementLabel = createStatLabel();

        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));

        add(nameLabel);
        add(hpLabel);
        add(powerLabel);
        add(treasureLabel);
        add(elementLabel);

        rebuild();
    }

    private JLabel createStatLabel() {
        JLabel lbl = new JLabel("", SwingConstants.LEFT);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(200, 200, 200));
        MatteBorder border = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(180, 180, 180));
        lbl.setBorder(border);
        lbl.setForeground(Color.DARK_GRAY);
        lbl.setPreferredSize(new Dimension(100, 24));
        return lbl;
    }

    public void rebuild() {
        PlayerCharacter p = world.getPlayer();
        nameLabel.setText("Name: " + p.getName());
        hpLabel.setText("HP:       " + p.getHealth() + " / " + "100");
        powerLabel.setText("Power:    " + p.getPower());
        treasureLabel.setText("Treasure: " + p.getTreasurePoints());
        treasureLabel.setIcon(ImageLoader.load("treasure.png",elementIconSize,elementIconSize));
        MagicElement el = p.getElement();
        if (el != null) {
            elementLabel.setText("Element: " + el);
            ImageIcon icon = ImageLoader.load(el.toString().toLowerCase() + ".png", elementIconSize, elementIconSize);
            elementLabel.setIcon(icon);
        }
        else {
            elementLabel.setText("Element: -");
            elementLabel.setIcon(null);
        }
    }

    public void changeDetected() {
        rebuild();
    }
}
