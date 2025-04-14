package Game.Engine;

import Game.Characters.*;
import Game.Combat.CombatSystem;
import Game.Core.GameEntity;
import Game.Items.*;
import Game.Map.Position;

import java.util.*;

public class GameWorld {
    private GameWorld(int rows, int cols, String name, int type){
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.gameMap = GameMap.getInstance(rows, cols);
        this.combatSystem = new CombatSystem();
        this.rows=rows;
        this.cols=cols;
        Random random = new Random();

        Position playerPosition = getRandomEmptyPosition(rows, cols, random);
        PlayerCharacter playerCharacter = switch (type) {
            case 1 -> new Warrior(playerPosition.getRow(), playerPosition.getCol(), name);
            case 2 -> new Mage(playerPosition.getRow(), playerPosition.getCol(), name);
            case 3 -> new Archer(playerPosition.getRow(), playerPosition.getCol(), name);
            default -> throw new IllegalArgumentException("Invalid type!");
        };
        players.add(playerCharacter);
        gameMap.placeEntity(playerPosition, playerCharacter);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Position newPos = new Position(i, j);
                if (!gameMap.getGrid().get(newPos).isEmpty()) continue;

                int probability = random.nextInt(100);
                GameEntity entity = null;

                if (probability < 30) {
                    int enemyType = random.nextInt(3);
                    entity = switch (enemyType) {
                        case 0 -> new Goblin(i, j);
                        case 1 -> new Orc(i, j);
                        default -> new Dragon(i, j);
                    };
                    enemies.add((Enemy) entity);
                } else if (probability < 40) {
                    entity = new Wall(i, j);
                } else if (probability < 60) {
                    entity = random.nextInt(100) < 25 ? new PowerPotion(i, j) : new Potion(i, j);
                    items.add((GameItem) entity);
                }

                if (entity != null) {
                    gameMap.placeEntity(newPos, entity);
                }
            }
        }
    }
    private Position getRandomEmptyPosition(int rows, int cols, Random rand) {
        while (true) {
            Position pos = new Position(rand.nextInt(rows), rand.nextInt(cols));
            if (gameMap.getGrid().get(pos).isEmpty()) {
                return pos;
            }
        }
    }
    public List<GameItem> getItems() {
        return items;
    }
    public GameMap getGameMap() {
        return gameMap;
    }
    public List<Enemy> getEnemies() {
        return enemies;
    }
    public List<PlayerCharacter> getPlayers() {
        return players;
    }
    public void addTreasure(GameEntity entity){
        if (entity instanceof GameItem e){
            items.add(e);
        }
    }
    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;
    private GameMap gameMap;
    private CombatSystem combatSystem;
    private int rows,cols;
}
