package Game.GUI;
import Game.Audio.SoundManager;
import Game.Characters.Enemy;
import Game.Characters.PlayerCharacter;
import Game.Decorators.ExplodingEnemyDecorator;
import Game.Decorators.TeleportingEnemyDecorator;
import Game.Decorators.VampireEnemyDecorator;
import Game.Engine.*;
import Game.Core.GameEntity;
import Game.Items.GameItem;
import Game.Items.Wall;
import Game.Logs.LogManager;
import Game.Map.Position;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class MapPanel extends JPanel implements GameListener {
    private final GameController gameController;
    private final JLabel[][] cells;
    private final int iconSize = 64;
    private final Map<String, ImageIcon> iconCache = new ConcurrentHashMap<>();
    private static final Color color1=new Color(0x3A, 0x3A, 0x3A);
    private final Set<JLabel> flashingLabels = Collections.synchronizedSet(new HashSet<>()); //in order to not flash the same cell twice or more at a time and not corrupt the border


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
                                        flashOnce(cells[playerPos.getRow()][playerPos.getCol()],200,Color.GRAY);
                                    }
                                    else{
                                        flashOnce(cells[playerPos.getRow()][playerPos.getCol()],200,Color.RED);
                                    }

                                    if (clickResult.getDamageToEnemy()<=0){
                                        flashOnce(lbl,200,Color.GRAY);
                                    }
                                    else {
                                        flashOnce(lbl,200,Color.RED);
                                    }
                                    break;
                                case PICKUP:
                                    flashOnce(cells[pos.getRow()][pos.getCol()],200,Color.GREEN);
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
                                JLabel typeLbl = new JLabel("Type: " + enemy.getType());
                                typeLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                                info.add(typeLbl);
                                if (enemy instanceof ExplodingEnemyDecorator) {
                                    JLabel decoratedLbl = new JLabel("Enhanced: Explosive");
                                    decoratedLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                                    info.add(decoratedLbl);
                                } else if (enemy instanceof VampireEnemyDecorator) {
                                    JLabel decoratedLbl = new JLabel("Enhanced: Vampire");
                                    decoratedLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                                    info.add(decoratedLbl);
                                } else if (enemy instanceof TeleportingEnemyDecorator) {
                                    JLabel decoratedLbl = new JLabel("Enhanced: Teleporting");
                                    decoratedLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                                    info.add(decoratedLbl);
                                }
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

    public void playerAttack(PlayerCharacter playerCharacter, Enemy enemy) {
        Position playerPos = playerCharacter.getPosition();
        Position enemyPos = enemy.getPosition();

        flashOnce(cells[playerPos.getRow()][playerPos.getCol()], 200, Color.RED);
        refresh();
        flashOnce(cells[enemyPos.getRow()][enemyPos.getCol()], 200, Color.RED);
        refresh();
    }

    private void flashOnce(JLabel label, int holdMs, Color color) {
        if (flashingLabels.contains(label))
            return;

        flashingLabels.add(label);
        final Border original = label.getBorder();
        final Border flashBorder = new LineBorder(color, 2);

        label.setBorder(flashBorder);

        new Timer(holdMs, evt -> {
            ((Timer) evt.getSource()).stop();
            label.setBorder(original);
            flashingLabels.remove(label);
        }).start();
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
            synchronized (list) {
                if (!list.isEmpty()) {
                    GameEntity ent = list.get(0);
                    if (ent != null) {
                        boolean shouldShow = false;
                        if (ent instanceof PlayerCharacter) {
                            shouldShow = true;
                        } else {
                            Position playerPos = gameController.getPlayer().getPosition();
                            if (playerPos != null) {
                                int distance = playerPos.distanceTo(pos);
                                shouldShow = distance <= 2 && ent.getVisibility();
                            }
                        }
                        if (shouldShow) {
                            String file = ent.getDisplaySymbol() + ".png";
                            ImageIcon icon = iconCache.computeIfAbsent(file, f -> ImageLoader.load(f, iconSize, iconSize));
                            if (ent instanceof Enemy enemy && (enemy instanceof ExplodingEnemyDecorator || enemy instanceof VampireEnemyDecorator || enemy instanceof TeleportingEnemyDecorator)) {

                                JPanel centerPanel = new JPanel();
                                centerPanel.setLayout(new OverlayLayout(centerPanel));
                                centerPanel.setOpaque(false);

                                JLabel pic = new JLabel(icon);
                                pic.setHorizontalAlignment(SwingConstants.CENTER);
                                pic.setAlignmentX(Component.CENTER_ALIGNMENT);
                                pic.setAlignmentY(Component.CENTER_ALIGNMENT);

                                JLabel indicator = new JLabel("⚡");
                                indicator.setFont(new Font("Dialog", Font.BOLD, 32));
                                indicator.setForeground(Color.YELLOW);
                                indicator.setHorizontalAlignment(SwingConstants.RIGHT);
                                indicator.setVerticalAlignment(SwingConstants.TOP);

                                centerPanel.add(indicator);
                                centerPanel.add(pic);

                                lbl.add(centerPanel, BorderLayout.CENTER);
                                lbl.add(new HealthBar(enemy.getHealth(), 50), BorderLayout.NORTH);
                            }
                            else {
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
                    }
                }
            }
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
}