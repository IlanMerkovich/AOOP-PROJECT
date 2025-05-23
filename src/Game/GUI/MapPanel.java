package Game.GUI;
import Game.Audio.SoundManager;
import Game.Characters.Enemy;
import Game.Characters.PlayerCharacter;
import Game.Combat.MagicAttacker;
import Game.Engine.ClickResult;
import Game.Engine.ClickerHandler;
import Game.Core.GameEntity;
import Game.Engine.GameWorld;
import Game.Engine.GameListener;
import Game.Items.GameItem;
import Game.Logs.LogManager;
import Game.Map.Position;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapPanel extends JPanel implements GameListener {
    private final GameWorld world;
    private final JLabel[][] cells;
    private final int iconSize = 64;
    private final Map<String, ImageIcon> iconCache = new HashMap<>();
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
        ClickerHandler clickerHandler=new ClickerHandler(world);
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
                        Position playerPos=world.getPlayer().getPosition();
                        List<GameEntity> list = world.getGameMap().getGrid().get(pos);
                        if (SwingUtilities.isLeftMouseButton(e)){
                            ClickResult clickResult=clickerHandler.handleLeftClickMap(pos);
                            switch (clickResult.getType()){
                                case MOVE, NONE:
                                    break;
                                case ATTACK:
                                    showDamagePopup(cells[playerPos.getRow()][playerPos.getCol()], clickResult.getDamageToPlayer(),Color.RED);
                                    showDamagePopup(lbl,clickResult.getDamageToEnemy(),Color.RED);
                                    addClickFlash(lbl,100,Color.RED);
                                    addClickFlash(cells[playerPos.getRow()][playerPos.getCol()],100,Color.red);
                                    break;
                                case PICKUP:
                                    addClickFlash(cells[pos.getRow()][pos.getCol()],100,Color.GREEN);
                                    break;
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
        changeDetected();
        SoundManager.playEffect("welcome.wav");
    }
    private void leftClickPopMenu(MouseEvent e, List<GameEntity> list, Color borderColor, GameWorld world, Position pos, JLabel lbl) {
        JPopupMenu menu = new JPopupMenu();
        menu.setBackground(Color.WHITE);
        menu.setBorder(BorderFactory.createMatteBorder(7, 7, 7, 7, borderColor));

        if (list == null || list.isEmpty()) {
            if (world.getGameMap().isBlocked(pos)) {
                menu.add(new JMenuItem("Wall – blocks movement"));
            }
            else {
                menu.add(new JMenuItem("Empty cell"));
            }
        }
        else {
            GameEntity ent = list.getFirst();
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
            GameEntity ent = list.getFirst();
            if (ent instanceof PlayerCharacter || ent.getVisibility()) {
                String file = ent.getDisplaySymbol() + ".png";
                icon = getIcon(file);
            }
        }
        lbl.setIcon(icon);
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
        Position pos = world.getPlayer().getPosition();
        if (damage<=0){
            cell.setText("EVADE!");
            cell.setFont(new Font("Arial Black", Font.BOLD, 12));
        }
        else{
            cell.setText("-" + damage);
            cell.setFont(new Font("Arial Black", Font.BOLD, 18));
        }
        cell.setHorizontalTextPosition(SwingConstants.CENTER);
        cell.setVerticalTextPosition(SwingConstants.CENTER);
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
    private ImageIcon getIcon(String filename){
        ImageIcon img = ImageLoader.load(filename, iconSize, iconSize);
        if (img != null) {
            iconCache.put(filename, img);
        }
        else {
            System.err.println("error loading image");
        }
        return iconCache.get(filename);
    }
}