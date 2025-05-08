package Game.GUI;

import Game.Characters.Enemy;
import Game.Characters.PlayerCharacter;
import Game.Combat.RangedFighter;
import Game.Core.GameEntity;
import Game.Engine.GameWorld;
import Game.Engine.GameWorldListener;
import Game.Items.GameItem;
import Game.Items.Potion;
import Game.Items.Treasure;
import Game.Map.Position;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;
import java.util.List;


public class MapPanel extends JPanel implements GameWorldListener {
    private final GameWorld world;
    private final JLabel[][] cells;
    private final int iconSize = 64;

    public MapPanel(GameWorld world) {
        this.world = world;
        world.addListener(this);
        int rows = world.getRows();
        int cols = world.getCols();
        setLayout(new GridLayout(rows, cols));
        setBackground(new Color(255, 255, 250));

        cells = new JLabel[rows][cols];
        Color borderColor = new Color(180, 180, 180);
        MatteBorder cellBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JLabel lbl = new JLabel();
                lbl.setOpaque(true);
                lbl.setBackground(new Color(200, 200, 200));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setVerticalAlignment(SwingConstants.CENTER);
                lbl.setPreferredSize(new Dimension(iconSize, iconSize));
                lbl.setBorder(cellBorder);

                Position pos = new Position(r, c);
                lbl.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        PlayerCharacter player = findPlayer();
                        int dist = player.getPosition().distanceTo(pos);
                        List<GameEntity> list = world.getGameMap().getGrid().get(pos);

                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (list == null || list.isEmpty()) {
                                if (dist == 1) {
                                    world.movePlayerTo(pos);
                                }
                                else {
                                    Toolkit.getDefaultToolkit().beep();
                                }
                            }

                            else {
                                GameEntity ent = list.get(0);
                                if (ent instanceof Enemy) {
                                    if (player instanceof RangedFighter) {
                                        if (dist <= 2){
                                            world.attackEnemyAt(pos);
                                        }
                                        else
                                            Toolkit.getDefaultToolkit().beep();
                                    }
                                    else
                                    {
                                        if (dist == 1){
                                            world.attackEnemyAt(pos);
                                        }
                                        else
                                            Toolkit.getDefaultToolkit().beep();
                                    }
                                }
                                else if (ent instanceof GameItem) {
                                    if (dist == 1){
                                        if (ent instanceof Potion){
                                            world.pickupItemAt(pos);
                                        }
                                        else if(ent instanceof Treasure){
                                            world.interactWithItemAt(pos);
                                        }
                                    }
                                    else {
                                        Toolkit.getDefaultToolkit().beep();
                                    }
                                }
                                updateCell(ent.getPosition().getRow(),ent.getPosition().getCol());
                            }
                        }

                        else if (SwingUtilities.isRightMouseButton(e)) {
                            JPopupMenu menu = new JPopupMenu();
                            menu.setBackground(Color.WHITE);
                            menu.setBorder(BorderFactory.createMatteBorder(1,1,1,1,borderColor));

                            if (list == null || list.isEmpty()) {
                                if (world.getGameMap().isBlocked(pos)) {
                                    menu.add(new JMenuItem("Wall – blocks movement"));
                                }
                                else {
                                    menu.add(new JMenuItem("Empty cell"));
                                }
                            }
                            else {
                                GameEntity ent = list.get(0);
                                if (ent instanceof Enemy en) {
                                    menu.add(new JMenuItem(en.getDisplaySymbol() + " – HP: " + en.getHealth()));
                                }
                                else if (ent instanceof GameItem item) {
                                    menu.add(new JMenuItem(item.getDisplaySymbol() + " – " + item.getDescription()));
                                }
                                else {
                                    menu.add(new JMenuItem(ent.getDisplaySymbol()));
                                }
                            }
                            menu.show(lbl, e.getX(), e.getY());
                        }
                    }
                });
                cells[r][c] = lbl;
                add(lbl);
            }
        }
        refresh();
        checkGameEnd();
    }

    public void worldChanged(){
        refresh();
        checkGameEnd();
    }
    /** Redraws the entire grid with up-to-date icons. */
    private void refresh() {
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                updateCell(r, c);
            }
        }
    }

    private void updateCell(int r, int c) {
        Position pos = new Position(r, c);
        List<GameEntity> list = world.getGameMap().getGrid().get(pos);
        JLabel lbl = cells[r][c];
        ImageIcon icon = null;

        if (list != null && !list.isEmpty()) {
            GameEntity ent = list.get(0);
            if (ent instanceof PlayerCharacter || ent.getVisibility()) {
                String file = ent.getDisplaySymbol() + ".png";
                icon = ImageLoader.load(file, iconSize, iconSize);
            }
        }
        else {
            icon = ImageLoader.load("empty.png", iconSize, iconSize);
        }
        lbl.setIcon(icon);
    }

    /** Finds the current player object on the map. */
    private PlayerCharacter findPlayer() {
        for (List<GameEntity> cell : world.getGameMap().getGrid().values()) {
            for (GameEntity ent : cell) {
                if (ent instanceof PlayerCharacter pc) {
                    return pc;
                }
            }
        }
        throw new IllegalStateException("No player on map!");
    }

    private void checkGameEnd() {
        PlayerCharacter p = world.getPlayer();
        if (p.isDead()){
            JOptionPane.showMessageDialog(
                    this, "Game Over – You have died!\nTreasure Points: " + p.getTreasurePoints(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        if (world.areAllEnemiesDead()) {
            JOptionPane.showMessageDialog(
                    this, "Congratulations! All enemies have been defeated.\nTreasure Points: " + p.getTreasurePoints(), "Victory!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

}
