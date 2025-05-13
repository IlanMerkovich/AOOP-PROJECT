package Game.Engine;
import Game.GUI.MapPanel;
import Game.Items.Interactable;

import Game.Characters.*;
import Game.Combat.CombatSystem;
import Game.Core.GameEntity;
import Game.Items.*;
import Game.Map.Position;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

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
        this.rows=rows;
        this.cols=cols;
        this.gameMap = new GameMap(rows,cols);
        this.worldLock=new ReentrantLock(true);
        this.random=new Random();

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
                if (!gameMap.getGrid().get(newPos).isEmpty())
                    continue;

                int probability = random.nextInt(100);
                GameEntity entity = null;

                if (probability < 30) {
                    int enemyType = random.nextInt(3);
                    entity = switch (enemyType) {
                        case 0 -> new Goblin(i, j);
                        case 1 -> new Orc(i, j);
                        default -> new Dragon(i, j);
                    };
                    Enemy enemy=(Enemy) entity;
                    enemy.init(this);
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
        this.scheduler = Executors.newScheduledThreadPool(10);
        scheduleAllEnemies();
        updateVisibility(playerPosition);
        notifyListeners();
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
        combatSystem.resolveCombat(player,enemy);
        if (enemy.isDead()){
            Treasure treasure=enemy.Defeat();
            this.addItem(treasure);
            gameMap.placeEntity(enemy.getPosition(),treasure);
            gameMap.removeEntity(enemy.getPosition(),enemy);
        }
    }
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
    public void movePlayerTo(Position dest){
        PlayerCharacter p = players.get(0);
        if (!gameMap.isBlocked(dest)) {
            Position src = p.getPosition();
            gameMap.removeEntity(src, p);
            p.setPosition(dest);
            gameMap.placeEntity(dest, p);
            updateVisibility(dest);
            notifyListeners();
        }
    }
    public void attackEnemyAt(Position pos) {
        List<GameEntity> list = gameMap.getGrid().get(pos);
        if (list != null && !list.isEmpty() && list.get(0) instanceof Enemy e) {
            ManageFight(getPlayer(),e);
            updateVisibility(getPlayer().getPosition());
            notifyListeners();
        }
    }
    public void pickupItemAt(Position pos) {
        List<GameEntity> list = gameMap.getGrid().get(pos);
        if (list != null && !list.isEmpty() && list.get(0) instanceof Pickupable pick) {
            pick.pickup(getPlayer());
            list.remove(0);
            updateVisibility(getPlayer().getPosition());
            notifyListeners();
        }
    }
    public void interactWithItemAt(Position pos) {
        List<GameEntity> cell = gameMap.getGrid().get(pos);
        if (cell == null)
            return;

        Iterator<GameEntity> it = cell.iterator();
        while (it.hasNext()) {
            GameEntity ent = it.next();
            if (ent instanceof Interactable inter) {
                inter.interact(getPlayer());
                it.remove();
                updateVisibility(getPlayer().getPosition());
                notifyListeners();
                return;
            }
        }
    }
    public boolean areAllEnemiesDead() {
        for (Enemy e : enemies) {
            if (!e.isDead())
                return false;
        }
        return true;
    }
    public void addListener(GameWorldListener l){
        listeners.add(l);
    }
    public void removeListener(GameWorldListener l){
        listeners.remove(l);
    }
    private void notifyListeners(){
        for (GameWorldListener l:listeners){
            l.worldChanged();
        }
    }
    private void notifyMapChange(){
        for (GameWorldListener l:listeners){
            if (listeners instanceof MapPanel mapPanel){
                mapPanel.worldChanged();
            }
        }
    }
    private Position getStepPosition(Position enemyPos){
        int r=enemyPos.getRow();
        int c=enemyPos.getCol();
        ArrayList<Position>positionAvailable=new ArrayList<>(4);
        if (r>0){
            positionAvailable.add(new Position(r-1,c));
        }
        if (r<rows-1){
            positionAvailable.add(new Position(r+1,c));
        }
        if (c>0){
            positionAvailable.add(new Position(r,c-1));
        }
        if (c<cols-1){
            positionAvailable.add(new Position(r,c+1));
        }
        if (positionAvailable.isEmpty()){
            return null;
        }
        return positionAvailable.get(random.nextInt(positionAvailable.size()));
    }
    public void attemptToMove(Enemy enemy){
        Position enemyPos=enemy.getPosition();
        Position targetPos=getStepPosition(enemyPos);
        try {
            if (gameMap.tryLockCell(targetPos, 50)) ;
            try {
                if (gameMap.getGrid().get(targetPos).isEmpty() && !gameMap.isBlocked(targetPos)) {
                    gameMap.removeEntity(enemyPos, enemy);
                    enemy.setPosition(targetPos);
                    gameMap.placeEntity(targetPos, enemy);
                }
            } finally {
                gameMap.unlockCell(targetPos);
            }
        }
        finally {
            updateVisibility(getPlayer().getPosition());
            if (enemy.getVisibility()){
                notifyMapChange();
            }
        }
    }
    private void scheduleOneEnemy(Enemy enemy) {
        long delay = 500 + random.nextInt(1501);
        scheduler.schedule(() -> {
            enemy.run();
            scheduleOneEnemy(enemy);
        }, delay, TimeUnit.MILLISECONDS);
    }
    private void scheduleAllEnemies() {
        for (Enemy enemy : enemies) {
            scheduleOneEnemy(enemy);
        }
    }
    public void shutdown() {
        scheduler.shutdownNow();
    }

    private ReentrantLock worldLock;
    private ScheduledExecutorService scheduler;
    private Random random;
    private int rows,cols;
    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;
    private GameMap gameMap;
    private CombatSystem combatSystem;
    private final List<GameWorldListener> listeners = new ArrayList<>();
}