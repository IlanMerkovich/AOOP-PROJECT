package Game.Engine;

import Game.Characters.*;
import Game.Combat.CombatSystem;
import Game.Combat.Combatant;
import Game.Core.GameEntity;
import Game.Factory.EnemyFactory;
import Game.Items.*;
import Game.Logs.LogManager;
import Game.Map.Position;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class GameWorld {
    private static final int ENEMY_SPAWN_CHANCE=30;
    private static final int WALL_SPAWN_CHANCE=40;
    private static final int POTION_SPAWN_CHANCE=60;
    private static final int POWER_POTION_CHANCE=25;

    public GameWorld(int rows, int cols, PlayerCharacter combatant){
        PlayerCharacter playerCharacter=combatant;
        try {
            GameMap existingMap = GameMap.getInstance();
            if (existingMap != null) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        Position pos = new Position(i, j);
                        if (existingMap.getGrid().containsKey(pos)) {
                            existingMap.clearCell(pos);
                        }
                    }
                }
                existingMap.getGrid().clear();
            }
        }
        catch (IllegalStateException e) {
        }

        this.rows = rows;
        this.cols = cols;
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.combatSystem = new CombatSystem();
        this.gameMap = GameMap.getInstance(rows, cols);
        this.random = new Random();
        this.careTaker = new GameWorldCareTaker();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Position pos = new Position(i, j);
                if (!gameMap.getGrid().containsKey(pos)) {
                    gameMap.getGrid().put(pos, Collections.synchronizedList(new ArrayList<>()));
                }
            }
        }

        Position position = getRandomEmptyPosition(rows, cols, random);
        players.add(playerCharacter);
        gameMap.placeEntity(position, playerCharacter);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Position newPos = new Position(i, j);
                if (!gameMap.getGrid().get(newPos).isEmpty())
                    continue;

                int probability = random.nextInt(100);
                GameEntity entity = null;

                if (probability < ENEMY_SPAWN_CHANCE) {
                    entity = enemyFactory.createCharacter(enemies,rows,cols);
                    enemies.add((Enemy)entity);
                }
                else if (probability < WALL_SPAWN_CHANCE) {
                    entity = new Wall(i, j);
                }
                else if (probability < POTION_SPAWN_CHANCE) {
                    int chance = random.nextInt(100);

                    if (chance < POWER_POTION_CHANCE) {
                        entity = new PowerPotion(i, j);
                        items.add((GameItem) entity);
                    }
                    else {
                        entity = new Potion(i, j);
                        items.add((GameItem) entity);
                    }
                }

                if (entity != null) {
                    gameMap.placeEntity(newPos, entity);
                }
            }
        }

        LogManager.startLogger();
        LogManager.addLog("==============Game Started==============");
        LogManager.addLog("Player is :" + getPlayer().getName() + " type: " + getPlayer().getClass().getSimpleName());
        notifyListeners();
        notifyMapChange();
    }

    public Position getRandomEmptyPosition(int rows, int cols, Random rand) {
        while (true) {
            Position pos = new Position(rand.nextInt(rows), rand.nextInt(cols));
            if (gameMap.getGrid().get(pos).isEmpty()) {
                return pos;
            }
        }
    }
    public PlayerCharacter getPlayer() {
        return players.getFirst();
    }
    public boolean addItem(GameEntity entity) {
        if (entity instanceof GameItem e) {
            items.add(e);
            return true;
        }
        return false;
    }
    public GameMap getGameMap() {
        return gameMap;
    }
    public int getCols() {
        return cols;
    }
    public int getRows() {
        return rows;
    }
    public CombatSystem getCombatSystem() {
        return combatSystem;
    }
    public void addListener(GameListener l) {
        listeners.add(l);
    }
    public void removeListener(GameListener l) {
        listeners.remove(l);
    }
    void notifyListeners() {
        for (GameListener l : listeners) {
            l.changeDetected();
        }
    }
    void notifyMapChange() {
        for (GameListener l : listeners) {
            l.onMapChange();
        }
    }
    void notifyAttack(PlayerCharacter playerCharacter,Enemy enemy){
        for (GameListener l : listeners) {
            l.playerAttack(playerCharacter,enemy);
        }
    }
    List<Position> getStepPositionNearPlayer(Position enemyPos) {
        int r = enemyPos.getRow();
        int c = enemyPos.getCol();
        ArrayList<Position> positionAvailable = new ArrayList<>(4);
        if (r > 0)
            positionAvailable.add(new Position(r - 1, c));
        if (r < rows - 1)
            positionAvailable.add(new Position(r + 1, c));
        if (c > 0)
            positionAvailable.add(new Position(r, c - 1));
        if (c < cols - 1)
            positionAvailable.add(new Position(r, c + 1));
        return positionAvailable.isEmpty() ? null : positionAvailable;
    }
    List<Position> getPositionsShuffled(Position enemyPos) {
        List<Position> neighboringPos = new ArrayList<>();
        neighboringPos.add(new Position(enemyPos.getRow() + 1, enemyPos.getCol()));
        neighboringPos.add(new Position(enemyPos.getRow() - 1, enemyPos.getCol()));
        neighboringPos.add(new Position(enemyPos.getRow(), enemyPos.getCol() + 1));
        neighboringPos.add(new Position(enemyPos.getRow(), enemyPos.getCol() - 1));
        Collections.shuffle(neighboringPos);
        return neighboringPos;
    }
    List<Enemy> getEnemies() {
        return enemies;
    }
    public GameWorldMemento createMemento(){
        return new GameWorldMemento(players, enemies, items, gameMap.getGrid());
    }
    private void restoreMemento(GameWorldMemento memento){
        this.players = new ArrayList<>(memento.getPlayers());
        this.enemies = new ArrayList<>(memento.getEnemies());
        this.items = new ArrayList<>(memento.getItems());
        this.gameMap.setGrid(memento.getGrid());
        LogManager.addLog("Game was restored to last saving point");

    }
    public void restore() {
        if (!careTaker.previousLoads()) {
            lock.lock();
            clearGameWorld();
            restoreMemento(careTaker.loadMemento());
            GameController controller = new GameController(this);
            controller.restoreEnemyThreads();

            PlayerCharacter restoredPlayer = getPlayer();
            Position playerPos = restoredPlayer.getPosition();

            for (List<GameEntity> cell : getGameMap().getGrid().values()) {
                cell.removeIf(e -> e instanceof PlayerCharacter);
            }

            getGameMap().getGrid().computeIfAbsent(playerPos,p -> new ArrayList<>()).add(restoredPlayer);
            notifyListeners();
            lock.unlock();
        }
        else {
            Toolkit.getDefaultToolkit().beep();
            notifyListeners();
        }
    }
    private void clearGameWorld() {
        this.players.clear();
        this.enemies.clear();
        this.items.clear();
        gameMap.getGrid().clear();
    }
    public void save(){
        lock.lock();
        careTaker.saveMemento(createMemento());
        lock.unlock();
    }
    public void placeNewEnemy(Enemy enemy,Position position){
        System.out.println("new enemy set");
        gameMap.placeEntity(position,enemy);
    }
    public void addEnemy(Enemy enemy){
        this.enemies.add(enemy);
    }
    protected void hello(){
        System.out.println("hello");
    }
    private final ReentrantLock lock=new ReentrantLock(true);
    private GameWorldCareTaker careTaker;
    private Random random;
    private int rows;
    private int cols;
    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;
    private GameMap gameMap;
    private final CombatSystem combatSystem;
    private final List<GameListener> listeners = new ArrayList<>();
    private final EnemyFactory enemyFactory=new EnemyFactory();
}