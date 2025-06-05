package Game.GUI;

import Game.Characters.PlayerCharacter;
import Game.Combat.MagicElement;
import Game.Engine.GameWorld;
import Game.Engine.GameListener;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class StatusPanel extends JPanel implements GameListener {
    private static final Color color1 = new Color(0xF5, 0xE6, 0xC8);
    private static final Color color2 = new Color(0xA8, 0xA8, 0xA8);
    private static final Color color3 = new Color(0x3A, 0x3A, 0x3A);
    private static final Color color4 = new Color(0x5C, 0x3A, 0x21);

    private final GameWorld world;
    private final int elementIconSize = 16;
    private final JLabel nameLabel;
    private final JLabel hpLabel;
    private final JLabel powerLabel;
    private final JLabel treasureLabel;
    private final JLabel elementLabel;
    private final JButton saveBtn;
    private final JButton loadBtn;

    public StatusPanel(GameWorld world) {
        this.world = world;
        world.addListener(this);

        setBackground(color1);
        setPreferredSize(new Dimension(220, 180));
        setLayout(new GridLayout(7, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        nameLabel = createStatLabel();
        hpLabel = createStatLabel();
        powerLabel = createStatLabel();
        treasureLabel = createStatLabel();
        elementLabel  = createStatLabel();
        saveBtn=new JButton("Save Game");
        loadBtn=new JButton("Load Last Save");
        loadBtn.setBackground(Color.ORANGE);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 14f));
        nameLabel.setForeground(color3);

        add(nameLabel);
        add(hpLabel);
        add(powerLabel);
        add(treasureLabel);
        add(elementLabel);
        add(saveBtn);
        add(loadBtn);

        rebuild();
    }

    private JLabel createStatLabel() {
        JLabel lbl = new JLabel("", SwingConstants.LEFT);
        lbl.setOpaque(true);
        lbl.setBackground(color2);
        MatteBorder border = BorderFactory.createMatteBorder(1, 1, 1, 1, color4);
        lbl.setBorder(border);
        lbl.setForeground(color3);
        lbl.setPreferredSize(new Dimension(100, 24));
        return lbl;
    }

    public void rebuild() {
        PlayerCharacter p = world.getPlayer();

        nameLabel.setText("Name: "     + p.getName());
        hpLabel.setText("HP:       "   + p.getHealth() + " / " + "100");
        powerLabel.setText("Power:    " + p.getPower());
        treasureLabel.setText("Treasure: " + p.getTreasurePoints());
        treasureLabel.setIcon(ImageLoader.load("treasure.png", elementIconSize, elementIconSize));

        MagicElement el = p.getElement();
        if (el != null) {
            elementLabel.setText("Element: " + el);
            ImageIcon icon = ImageLoader.load(el.toString().toLowerCase() + ".png", elementIconSize,elementIconSize);
            elementLabel.setIcon(icon);
        }
        else{
            this.remove(elementLabel);
        }
    }

    public void changeDetected() {
        rebuild();
    }

    public void onMapChange() {
        return;
    }
}

