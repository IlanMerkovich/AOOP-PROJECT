package Game.Engine;
import Game.Items.Interactable;

import Game.Characters.*;
import Game.Combat.CombatSystem;
import Game.Combat.RangedFighter;
import Game.Core.GameEntity;
import Game.Items.*;
import Game.Map.Position;

import java.util.*;
/**
 * GameWorld is the central controller of the turn-based RPG game.
 * It initializes the game map, generates all entities (player, enemies, and items),
 * handles user input, movement, combat, item interaction, and the main game loop.
 *
 * This class manages:
 * - Initial setup of the world and user input
 * - Populating the grid with enemies and items
 * - Processing turns and actions from the player
 * - Combat resolution using CombatSystem
 * - Visibility logic for entities based on player's position
 */
public class GameWorld {
    /**
     * Constructs the game world by:
     * - Collecting input from the user for map size and character choice
     * - Initializing the game map
     * - Placing the player at a random empty position
     * - Randomly populating the map with enemies, walls, and potions
     */
    public GameWorld(int rows,int cols,String name,String type){
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.combatSystem = new CombatSystem();
        Scanner sc=new Scanner(System.in);
        Random random = new Random();
        this.rows=rows;
        this.cols=cols;
        this.gameMap = new GameMap(rows,cols);


        Position playerPosition = getRandomEmptyPosition(rows, cols, random);
        PlayerCharacter playerCharacter = switch (type) {
            case "Warrior" -> new Warrior(playerPosition.getRow(), playerPosition.getCol(), name);
            case "Mage" -> new Mage(playerPosition.getRow(), playerPosition.getCol(), name);
            case "Archer"-> new Archer(playerPosition.getRow(), playerPosition.getCol(), name);
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
                }
                else if (probability < 40) {
                    entity = new Wall(i, j);
                }
                else if (probability < 60) {
                    int chance = random.nextInt(100);
                    if (chance < 25) {
                        entity=new PowerPotion(i,j);
                        items.add((GameItem) entity);
                    }
                    else {
                        entity=new Potion(i,j);
                        items.add((GameItem) entity);
                    }
                }

                if (entity != null) {
                    gameMap.placeEntity(newPos, entity);
                }
            }
        }
        updateVisibility(playerPosition);
    }
    /**
     * Picks a random empty cell from the map for placing entities (like player).
     *
     * @param rows total number of rows on the map
     * @param cols total number of columns on the map
     * @param rand Random instance
     * @return a Position on the map that is currently empty
     */
    private Position getRandomEmptyPosition(int rows, int cols, Random rand) {
        while (true){
            Position pos = new Position(rand.nextInt(rows), rand.nextInt(cols));
            if (gameMap.getGrid().get(pos).isEmpty()) {
                return pos;
            }
        }
    }

    /**
     * Handles the combat loop between a player character and an enemy.
     * The method continues to call the combat system until one of the combatants dies.
     * If the enemy is defeated, it is removed from the map and replaced with a treasure.
     *
     * @param player the player character participating in the fight
     * @param enemy  the enemy the player is fighting against
     */
    private void ManageFight(PlayerCharacter player, Enemy enemy) {
        while (!player.isDead() && !enemy.isDead()){
            combatSystem.resolveCombat(player,enemy);
        }
        if (enemy.isDead()){
            Treasure treasure=enemy.Defeat();
            this.addItem(treasure);
            gameMap.placeEntity(enemy.getPosition(),treasure);
            gameMap.removeEntity(enemy.getPosition(),enemy);
        }
    }

    /**
     * Main game loop.
     * Repeats until the player dies or all enemies are defeated.
     * On each turn:
     * - Checks victory/defeat conditions
     * - Updates visibility around player
     * - Displays the map and player info
     * - Lets the player perform an action (movement, item usage, combat)
     */

    /**
     * Returns the first (and currently only) player in the world.
     *
     * @return the main player character
     */
    public PlayerCharacter getPlayer(){
        return players.get(0);
    }
    /**
     * Adds a game item to the world's item list.
     *
     * @param entity the GameEntity to add (must be GameItem)
     * @return true if the item was added successfully, false otherwise
     */
    private boolean addItem(GameEntity entity){
        if (entity instanceof GameItem e){
            items.add(e);
            return true;
        }
        return false;
    }
    /**
     * Updates visibility of entities on the map.
     * Entities within a Manhattan distance of 2 from the player are visible.
     *
     * @param playerPos the current position of the player
     */
    private void updateVisibility(Position playerPos) {
        for (List<GameEntity> cell : gameMap.getGrid().values()) {
            for (GameEntity entity : cell) {
                boolean visible = playerPos.distanceTo(entity.getPosition()) <= 2;
                entity.setVisible(visible);
            }
        }
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

    // inside GameWorld

    /** Attempt to move the player to `dest`, respecting walls and visibility. */
    public void movePlayerTo(Position dest) {
        PlayerCharacter p = players.get(0);
        // only if not blocked:
        if (!gameMap.isBlocked(dest)) {
            Position src = p.getPosition();
            gameMap.removeEntity(src, p);
            p.setPosition(dest);
            gameMap.placeEntity(dest, p);
            updateVisibility(dest);
        }
    }

    /** If there’s an enemy at `pos`, run the fight to completion. */
    public void attackEnemyAt(Position pos) {
        List<GameEntity> list = gameMap.getGrid().get(pos);
        if (list != null && !list.isEmpty() && list.get(0) instanceof Enemy e) {
            ManageFight(getPlayer(), e);
            updateVisibility(getPlayer().getPosition());
        }
    }

    /** If there’s a pickupable item at `pos`, pick it up and remove from map. */
    public void pickupItemAt(Position pos) {
        List<GameEntity> list = gameMap.getGrid().get(pos);
        if (list != null && !list.isEmpty() && list.get(0) instanceof Pickupable pick) {
            pick.pickup(getPlayer());
            list.remove(0);
            updateVisibility(getPlayer().getPosition());
        }
    }
    public void interactWithItemAt(Position pos) {
        List<GameEntity> cell = gameMap.getGrid().get(pos);
        if (cell == null) return;

        Iterator<GameEntity> it = cell.iterator();
        while (it.hasNext()) {
            GameEntity ent = it.next();
            if (ent instanceof Interactable inter) {
                inter.interact(getPlayer());
                it.remove();
                updateVisibility(getPlayer().getPosition());
                return;
            }
        }
    }
    public boolean areAllEnemiesDead() {
        for (Enemy e : enemies) {
            if (!e.isDead()) return false;
        }
        return true;
    }


    private int rows,cols;
    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;
    private GameMap gameMap;
    private CombatSystem combatSystem;
}
