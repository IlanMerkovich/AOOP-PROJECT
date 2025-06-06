package Game.GUI;
import Game.Audio.SoundManager;
import Game.Characters.Enemy;
import Game.Characters.PlayerCharacter;
import Game.Engine.*;
import Game.Core.GameEntity;
import Game.Items.GameItem;
import Game.Items.Wall;
import Game.Logs.LogManager;
import Game.Map.Position;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MapPanel extends JPanel implements GameListener {
    private final GameController gameController;
    private final JLabel[][] cells;
    private final int iconSize = 64;
    private final Map<String, ImageIcon> iconCache = new ConcurrentHashMap<>();
    private static final Color color1=new Color(0x3A, 0x3A, 0x3A);

    public MapPanel(GameController gameController,GameWorld world) {
        this.gameController = gameController;
        world.addListener(this);
        int rows = world.getRows();
        int cols = world.getCols();
        setLayout(new GridLayout(rows, cols));

        cells = new JLabel[rows][cols];
        Color borderColor = new Color(255, 255, 255);

        Border cellBorder = BorderFactory.createLineBorder(color1, 2);
        ClickerHandler clickerHandler=new ClickerHandler(gameController);
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
                                    if (clickResult.getDamageToPlayer()<=0){
                                        addClickFlash(cells[playerPos.getRow()][playerPos.getCol()],200,Color.GRAY);
                                    }
                                    else{
                                        addClickFlash(cells[playerPos.getRow()][playerPos.getCol()],200,Color.RED);
                                    }

                                    if (clickResult.getDamageToEnemy()<=0){
                                        addClickFlash(lbl,200,Color.GRAY);
                                    }
                                    else {
                                        addClickFlash(lbl,200,Color.RED);
                                    }
                                    break;
                                case PICKUP:
                                    addClickFlash(cells[pos.getRow()][pos.getCol()],200,Color.GREEN);
                                    break;
                            }
                        }
                        else if (SwingUtilities.isRightMouseButton(e)) {
                            if (list == null || list.isEmpty()) {
                                JOptionPane.showMessageDialog(MapPanel.this, "Empty cell", "Info", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                            GameEntity ent = list.get(0);
                            if (ent instanceof Wall) {
                                JOptionPane.showMessageDialog(MapPanel.this, "Wall Is Blocking You! You Must Bypass It!", "Blocked", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            if (ent instanceof Enemy enemy){
                                JPanel info = new JPanel();
                                info.setBackground(Color.WHITE);
                                info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
                                ImageIcon icon = ImageLoader.load(enemy.getDisplaySymbol() + ".png", iconSize, iconSize);
                                info.add(new JLabel(icon));
                                JProgressBar hpBar = new JProgressBar(0, 50);
                                hpBar.setValue(enemy.getHealth());
                                hpBar.setStringPainted(true);
                                hpBar.setString(enemy.getHealth() + " / " + 50);
                                info.add(hpBar);
                                JLabel powerLbl = new JLabel("Power: " + enemy.getPower());
                                powerLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                                info.add(powerLbl);
                                JPopupMenu menu = new JPopupMenu();
                                menu.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                                menu.add(info);
                                menu.show(lbl, e.getX(), e.getY());
                                return;
                            }
                            if (ent instanceof GameItem item) {
                                JPanel info = new JPanel();
                                info.setBackground(Color.WHITE);
                                info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
                                ImageIcon icon = ImageLoader.load(item.getDisplaySymbol() + ".png", iconSize, iconSize);
                                info.add(new JLabel(icon));
                                JLabel effect = new JLabel(item.getDescription());
                                effect.setAlignmentX(Component.CENTER_ALIGNMENT);
                                info.add(effect);
                                JPopupMenu menu = new JPopupMenu();
                                menu.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                                menu.add(info);
                                menu.show(lbl, e.getX(), e.getY());
                                return;
                            }
                            if(ent instanceof PlayerCharacter){
                                return;
                            }
                            JOptionPane.showMessageDialog(MapPanel.this, ent.getDisplaySymbol(), "Info", JOptionPane.INFORMATION_MESSAGE);
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
            for (int c = 0; c < cells[r].length; c++){
                updateCell(r, c);
            }
        }
    }
    private void updateCell(int r, int c) {
        Position pos = new Position(r, c);
        List<GameEntity> list = gameController.getGameMap().getGrid().get(pos);
        JLabel lbl = cells[r][c];
        lbl.removeAll();
        lbl.setLayout(new BorderLayout());

        if (list != null && !list.isEmpty()){
            GameEntity ent = list.get(0);
            if (ent instanceof PlayerCharacter || ent.getVisibility()) {
                String file = ent.getDisplaySymbol() + ".png";
                ImageIcon icon = iconCache.computeIfAbsent(file, f -> ImageLoader.load(f, iconSize, iconSize));
                JLabel pic = new JLabel(icon);
                pic.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.add(pic, BorderLayout.CENTER);

                if (ent instanceof PlayerCharacter player){
                    lbl.add(new HealthBar(player.getHealth(), 100), BorderLayout.NORTH);
                }
                else if (ent instanceof Enemy enemy) {
                    lbl.add(new HealthBar(enemy.getHealth(), 50), BorderLayout.NORTH);
                }
            }
        }
        else {
        }
        lbl.revalidate();
        lbl.repaint();
    }
    private void checkGameEnd() {
        PlayerCharacter p = gameController.getPlayer();
        if (p.isDead()){
            gameController.shutdownEnemies();
            SoundManager.playEffect("playerdead.wav");
            JOptionPane.showMessageDialog(this, "Game Over – You have died!\nTreasure Points: " + p.getTreasurePoints(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
            LogManager.stop();
            System.exit(0);
        }
        if (gameController.areAllEnemiesDead()) {
            gameController.shutdownEnemies();
            SoundManager.playEffect("victory.wav");
            JOptionPane.showMessageDialog(this, "Congratulations! All enemies have been defeated.\nTreasure Points: " + p.getTreasurePoints(), "Victory!", JOptionPane.INFORMATION_MESSAGE);
            LogManager.stop();
            System.exit(0);
        }
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
