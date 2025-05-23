package Game.GUI;
import Game.Audio.SoundManager;
import Game.Characters.Enemy;
import Game.Characters.PlayerCharacter;
import Game.Combat.MagicAttacker;
import Game.Combat.RangedFighter;
import Game.Core.GameEntity;
import Game.Engine.GameWorld;
import Game.Engine.GameListener;
import Game.Items.GameItem;
import Game.Items.Potion;
import Game.Items.Treasure;
import Game.Logs.LogManager;
import Game.Map.Position;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


public class MapPanel extends JPanel implements GameListener {
    private final GameWorld world;
    private final JLabel[][] cells;
    private final int iconSize = 64;
    private static final Color color1=new Color(0x3A, 0x3A, 0x3A);

    public MapPanel(GameWorld world) {
        this.world = world;
        world.addListener(this);
        int rows = world.getRows();
        int cols = world.getCols();
        setLayout(new GridLayout(rows, cols));

        cells = new JLabel[rows][cols];
        Color borderColor = new Color(255, 255, 255);
        Border cellBorder = BorderFactory.createLineBorder(color1, 2);

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
                lbl.addMouseListener(new MouseAdapter() {
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
                                    Enemy enemy = (Enemy) list.get(0);
                                    int beforeE = enemy.getHealth();
                                    int beforeP = player.getHealth();

                                    if (player instanceof RangedFighter) {
                                        if (dist <= 2) {
                                            world.attackEnemyAt(pos);
                                            addClickFlash(cells[player.getPosition().getRow()][player.getPosition().getCol()],250,Color.red);
                                            addClickFlash(lbl,250,Color.red);
                                            int afterE = enemy.getHealth();
                                            int afterP = player.getHealth();
                                            int dmgE = beforeE - afterE;
                                            int dmgP = beforeP - afterP;
                                            if (dmgE > 0) {
                                                showDamagePopup(lbl, dmgE, Color.GREEN);
                                            }
                                            if (dmgP > 0) {
                                                Position playerpos = player.getPosition();
                                                JLabel pcell = cells[playerpos.getRow()][playerpos.getCol()];
                                                showDamagePopup(pcell, dmgP, Color.RED);
                                            }
                                        }
                                        else
                                            Toolkit.getDefaultToolkit().beep();
                                    }
                                    else {
                                        if (dist == 1) {
                                            world.attackEnemyAt(pos);
                                            addClickFlash(lbl,250,Color.red);
                                            addClickFlash(cells[player.getPosition().getRow()][player.getPosition().getCol()],250,Color.red);
                                            int afterE = enemy.getHealth();
                                            int afterP = player.getHealth();
                                            int dmgE = beforeE - afterE;
                                            int dmgP = beforeP - afterP;

                                            if (dmgE > 0) {
                                                showDamagePopup(lbl, dmgE, Color.GREEN);
                                            }
                                            if (dmgP > 0) {
                                                Position playerpos = player.getPosition();
                                                JLabel pcell = cells[playerpos.getRow()][playerpos.getCol()];
                                                showDamagePopup(pcell, dmgP, Color.RED);
                                            }
                                        }
                                        else
                                            Toolkit.getDefaultToolkit().beep();
                                    }
                                }
                                else if (ent instanceof GameItem) {
                                    if (dist == 1) {
                                        if (ent instanceof Potion) {
                                            world.pickupItemAt(pos);
                                            addClickFlash(lbl,100,Color.green);
                                        }
                                        else if (ent instanceof Treasure) {
                                            SoundManager.playEffect("point.wav");
                                            world.interactWithItemAt(pos);
                                            addClickFlash(lbl,100,Color.green);
                                        }
                                    }
                                    else {
                                        Toolkit.getDefaultToolkit().beep();
                                    }
                                }
                                updateCell(ent.getPosition().getRow(), ent.getPosition().getCol());
                            }
                        }
                        else if (SwingUtilities.isRightMouseButton(e)) {
                            leftClickPopMenu(e,list,borderColor,world,pos,lbl);
                        }
                    }
                });
                lbl.setBackground(new Color(100, 100, 110));
                cells[r][c] = lbl;
                add(lbl);
            }
        }
        refresh();
        int fps = 1;
        int delay = 1000 / fps;
        new javax.swing.Timer(delay, e -> {
            this.refresh();}).start();
        changeDetected();
        SoundManager.playEffect("welcome.wav");
    }

    private void leftClickPopMenu(MouseEvent e, List<GameEntity> list, Color borderColor, GameWorld world, Position pos, JLabel lbl) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(Color.WHITE);
        menu.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, borderColor));

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
                if (en instanceof MagicAttacker){
                    menu.add(new JMenuItem(en.getDisplaySymbol() + " – HP: " + en.getHealth() + " ,Element: " +en.getElement()));
                }
                else{
                    menu.add(new JMenuItem(en.getDisplaySymbol()+ " - HP: "+en.getHealth()));
                }
            }
            else if (ent instanceof GameItem item) {
                menu.add(new JMenuItem(item.getDisplaySymbol() + " – " + item.getDescription()));
            }
            else if (ent instanceof PlayerCharacter pl){
                menu.add(new JMenuItem(pl.getName()));
            }
        }
        menu.show(lbl, e.getX(), e.getY());
    }

    public void onMapChange() {
        refresh();
        checkGameEnd();
    }
    public void changeDetected() {
        refresh();
        checkGameEnd();
    }
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
        lbl.setIcon(icon);
    }
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
            world.shutdown();
            SoundManager.playEffect("playerdead.wav");
            JOptionPane.showMessageDialog(this, "Game Over – You have died!\nTreasure Points: " + p.getTreasurePoints(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
            LogManager.stop();
            System.exit(0);
        }
        if (world.areAllEnemiesDead()) {
            world.shutdown();
            SoundManager.playEffect("victory.wav");
            JOptionPane.showMessageDialog(this, "Congratulations! All enemies have been defeated.\nTreasure Points: " + p.getTreasurePoints(), "Victory!", JOptionPane.INFORMATION_MESSAGE);
            LogManager.stop();
            System.exit(0);
        }
    }
    private void showDamagePopup(JLabel cell, int damage, Color color) {
        cell.setText("-" + damage);
        cell.setHorizontalTextPosition(SwingConstants.CENTER);
        cell.setVerticalTextPosition(SwingConstants.CENTER);
        cell.setFont(new Font("Arial Black", Font.BOLD, 20));
        cell.setForeground(color);

        Timer t = new Timer(600, e -> {
            ((Timer)e.getSource()).stop();
            cell.setText("");
        });
        t.setRepeats(false);
        t.start();
    }
    private void addClickFlash(final JLabel label, final int holdMs,Color color) {
        final Border original = label.getBorder();
        final Border redBorder = new LineBorder(color, 2);

        label.setBorder(redBorder);
         new Timer(holdMs, evt -> {
             ((Timer) evt.getSource()).stop();
             label.setBorder(original);
         }).start();
    }

}