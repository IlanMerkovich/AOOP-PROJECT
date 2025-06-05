package Game.Engine;

import Game.Characters.*;
import Game.Combat.CombatSystem;
import Game.Core.GameEntity;
import Game.Items.*;
import Game.Logs.LogManager;
import Game.Map.Position;
import java.util.*;

public class GameWorld {
    private static final int ENEMY_SPAWN_CHANCE=30;
    private static final int WALL_SPAWN_CHANCE=40;
    private static final int POTION_SPAWN_CHANCE=60;
    private static final int POWER_POTION_CHANCE=25;

    public GameWorld(int rows, int cols, String name, String type) {
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.combatSystem = new CombatSystem();
        this.gameMap = GameMap.getInstance(rows, cols);
        this.random = new Random();

        Position playerPosition = getRandomEmptyPosition(rows, cols, random);
        PlayerCharacter playerCharacter = switch (type) {
            case "Warrior" -> new Warrior(playerPosition.getRow(), playerPosition.getCol(), name);
            case "Mage" -> new Mage(playerPosition.getRow(), playerPosition.getCol(), name);
            case "Archer" -> new Archer(playerPosition.getRow(), playerPosition.getCol(), name);
            default -> throw new IllegalArgumentException("Invalid type!");
        };
        players.add(playerCharacter);
        gameMap.placeEntity(playerPosition, playerCharacter);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Position newPos = new Position(i, j);
                if (!gameMap.getGrid().get(newPos).isEmpty())
                    continue;

                int probability = random.nextInt(100);
                GameEntity entity = null;

                if (probability < ENEMY_SPAWN_CHANCE) {
                    int enemyType = random.nextInt(3);
                    entity = switch (enemyType) {
                        case 0 -> new Goblin(i, j);
                        case 1 -> new Orc(i, j);
                        default -> new Dragon(i, j);
                    };
                    Enemy enemy = (Enemy) entity;
                    enemies.add(enemy);
                } else if (probability < WALL_SPAWN_CHANCE) {
                    entity = new Wall(i, j);
                } else if (probability < POTION_SPAWN_CHANCE) {
                    int chance = random.nextInt(100);
                    if (chance < POWER_POTION_CHANCE) {
                        entity = new PowerPotion(i, j);
                        items.add((GameItem) entity);
                    } else {
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
    private Position getRandomEmptyPosition(int rows, int cols, Random rand) {
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
    List<Position> getStepPositionNearPlayer(Position enemyPos) {
        int r = enemyPos.getRow();
        int c = enemyPos.getCol();
        ArrayList<Position> positionAvailable = new ArrayList<>(4);
        if (r > 0) positionAvailable.add(new Position(r - 1, c));
        if (r < rows - 1) positionAvailable.add(new Position(r + 1, c));
        if (c > 0) positionAvailable.add(new Position(r, c - 1));
        if (c < cols - 1) positionAvailable.add(new Position(r, c + 1));
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


    private Random random;
    private int rows = 10;
    private int cols = 10;
    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;
    private final GameMap gameMap;
    private final CombatSystem combatSystem;
    private final List<GameListener> listeners = new ArrayList<>();
}