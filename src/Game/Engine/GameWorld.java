package Game.Engine;
import Game.Items.Interactable;
import Game.Characters.*;
import Game.Combat.CombatSystem;
import Game.Core.GameEntity;
import Game.Items.*;
import Game.Logs.LogManager;
import Game.Map.Position;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * GameWorld is the central controller of the turn-based RPG game.
 * It initializes the game map, generates all entities (player, enemies, and items),
 * handles user input, movement, combat, item interaction, and the main game loop.
 * This class manages:
 * - Initial setup of the world and user input
 * - Populating the grid with enemies and items
 * - Processing turns and actions from the player
 * - Combat resolution using CombatSystem
 * - Visibility logic for entities based on player's position
 */
public class GameWorld {
    private static final int ENEMY_SPAWN_CHANCE=30;
    private static final int WALL_SPAWN_CHANCE=40;
    private static final int POTION_SPAWN_CHANCE=60;
    private static final int POWER_POTION_CHANCE=25;
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

                if (probability < ENEMY_SPAWN_CHANCE) {
                    int enemyType = random.nextInt(3);
                    entity = switch (enemyType) {
                        case 0 -> new Goblin(i, j);
                        case 1 -> new Orc(i, j);
                        default -> new Dragon(i, j);
                    };
                    Enemy enemy=(Enemy) entity;
                    enemy.init(this,isRunning);
                    enemies.add((Enemy) entity);
                }
                else if (probability < WALL_SPAWN_CHANCE) {
                    entity = new Wall(i, j);
                }
                else if (probability < POTION_SPAWN_CHANCE) {
                    int chance = random.nextInt(100);
                    if (chance < POWER_POTION_CHANCE) {
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
        LogManager.startLogger();
        LogManager.addLog("==============Game Started==============");
        LogManager.addLog("Player is :" + getPlayer().getName() + " type: "+getPlayer().getClass().getSimpleName());
        updateVisibility(playerPosition);
        notifyListeners();
        this.scheduler = Executors.newScheduledThreadPool(10);
        scheduleAllEnemies();
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
    private void ManageFight(PlayerCharacter player, Enemy enemy){
        Position playerPos=player.getPosition();
        Position enemyPos=enemy.getPosition();
        if(gameMap.tryLockCell(enemyPos,50)){
            try {
                if(gameMap.tryLockCell(playerPos,50)){
                    try {
                        combatSystem.resolveCombat(player,enemy);
                        placeTreasure(enemy);
                    }
                    finally {
                        gameMap.unlockCell(playerPos);
                    }
                }
            }
            finally {
                gameMap.unlockCell(enemyPos);
            }
        }
    }
    /**
     * Returns the first (and currently only) player in the world.
     *
     * @return the main player character
     */
    public PlayerCharacter getPlayer(){
        return players.getFirst();
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
    private void placeTreasure(Enemy enemy){
        if(gameMap.tryLockCell(enemy.getPosition(),50))
            try{
                if (enemy.isDead()){
                    Treasure treasure = enemy.Defeat();
                   this.addItem(treasure);
                   LogManager.addLog("Treasure was created at: " + treasure.getPosition());
                   gameMap.removeEntity(enemy.getPosition(), enemy);
                   gameMap.placeEntity(enemy.getPosition(), treasure);
            }
        }
        finally {
            gameMap.unlockCell(enemy.getPosition());
            notifyMapChange();
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
        Position src=getPlayer().getPosition();

        if (!gameMap.isBlocked(dest)){
            try{
                if (gameMap.tryLockCell(dest,50) && gameMap.tryLockCell(src,50)){
                    if (gameMap.getGrid().get(dest).isEmpty() && !gameMap.isBlocked(dest)){
                        gameMap.removeEntity(src,p);
                        p.setPosition(dest);
                        gameMap.placeEntity(dest,p);
                        LogManager.addLog("Player moved from "+src +" to "+dest);
                    }
                }
            updateVisibility(dest);
            notifyListeners();
            }
            finally {
                gameMap.unlockCell(dest);
                gameMap.unlockCell(src);
            }
        }
    }
    public void attackEnemyAt(Position pos) {
        if (gameMap.tryLockCell(pos, 50)) {
            try {
                List<GameEntity> list = gameMap.getGrid().get(pos);
                if (list != null && !list.isEmpty() && list.get(0) instanceof Enemy e) {
                    ManageFight(getPlayer(),e);
                    updateVisibility(getPlayer().getPosition());
                }
            }
            finally {
                gameMap.unlockCell(pos);
                notifyListeners();
            }
        }
    }
    public void pickupItemAt(Position pos) {
        if (gameMap.tryLockCell(pos, 50)) {
            try {
                List<GameEntity> list = gameMap.getGrid().get(pos);
                if (list != null && !list.isEmpty() && list.get(0) instanceof Pickupable pick) {
                    pick.pickup(getPlayer());
                    list.remove(0);
                    updateVisibility(getPlayer().getPosition());
                    LogManager.addLog("Player picked up an item at: " + pos);
                }
            }
            finally {
                gameMap.unlockCell(pos);
                notifyListeners();
            }
        }

    }
    public void interactWithItemAt(Position pos) {
        if (gameMap.tryLockCell(pos, 100)) {
            try {
                List<GameEntity> cell = gameMap.getGrid().get(pos);
                if (cell == null)
                    return;
                Iterator<GameEntity> it = cell.iterator();
                while (it.hasNext()) {
                    GameEntity ent = it.next();
                    if (ent instanceof Interactable inter) {
                        inter.interact(getPlayer());
                        LogManager.addLog("Player interacted with a treasure at: " + pos);
                        it.remove();
                        updateVisibility(getPlayer().getPosition());

                        return;
                    }
                }
            } finally {
                gameMap.unlockCell(pos);
                notifyListeners();
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
    public void useItem(GameItem item) {
        PlayerCharacter p = getPlayer();
        boolean used;
        if (item instanceof PowerPotion) {
            used = p.usePowerPotion();
        } else {
            used = p.usePotion();
        }
        if (used) {
            notifyListeners();
        }
    }
    public void addListener(GameListener l){
        listeners.add(l);
    }
    public void removeListener(GameListener l){
        listeners.remove(l);
    }
    private void notifyListeners(){
        for (GameListener l:listeners){
            l.changeDetected();
        }
    }
    private void notifyMapChange(){
        for (GameListener l:listeners){
            l.onMapChange();
        }
    }
    private List<Position> getStepPositionNearPlayer(Position enemyPos){
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
        return positionAvailable;
    }
    public void attemptToMove(Enemy enemy) {
        if (enemy.isDead()) {
            return;
        }
        Position enemyPos = enemy.getPosition();
        Position playerPos = getPlayer().getPosition();
        double distToPlayer = enemyPos.distanceTo(playerPos);

        if (distToPlayer <= 2) {
            for (Position nextStep:getStepPositionNearPlayer(playerPos)){
                if (nextStep.equals(enemyPos)) {
                    continue;
                }
                if (tryMoveto(enemyPos, nextStep)) {
                    if (gameMap.tryLockCell(enemyPos, 50)) {
                        try {
                            if (gameMap.tryLockCell(nextStep,50)){
                                try {
                                    moveEntity(enemy, nextStep);
                                    updateVisibility(playerPos);
                                    notifyMapChange();
                                }
                                finally {
                                    gameMap.unlockCell(nextStep);
                                }
                            }
                        }
                        finally {
                            gameMap.unlockCell(enemyPos);
                        }
                    }
                    return;
                }
            }
        }
        else {
            List<Position> neighbors = getPositionsShuffled(enemyPos);
            for (Position target : neighbors){
                if (tryMoveto(enemyPos,target) && gameMap.tryLockCell(enemyPos, 50)){
                    try{
                        if (gameMap.tryLockCell(target,50)){
                            try {
                                moveEntity(enemy, target);
                                if (enemy.getVisibility()) {
                                    notifyMapChange();
                                    updateVisibility(playerPos);
                                }
                            }
                            finally {
                                gameMap.unlockCell(target);
                            }
                        }
                    }
                    finally{
                        gameMap.unlockCell(enemyPos);
                    }
                    return;
                }
            }
        }
        notifyMapChange();
    }
    private List<Position> getPositionsShuffled(Position enemyPos) {
        List<Position> neighboringPos = new ArrayList<>();
        neighboringPos.add(new Position(enemyPos.getRow()+ 1,enemyPos.getCol()));
        neighboringPos.add(new Position(enemyPos.getRow()- 1,enemyPos.getCol()));
        neighboringPos.add(new Position(enemyPos.getRow(),enemyPos.getCol()+1));
        neighboringPos.add(new Position(enemyPos.getRow(),enemyPos.getCol()-1));
        Collections.shuffle(neighboringPos);
        return neighboringPos;
    }
    private boolean tryMoveto(Position src,Position dest){
        if (src.distanceTo(dest)==1 && !gameMap.isBlocked(dest) && gameMap.getGrid().get(dest).isEmpty()){
            return true;
        }
        else
            return false;
    }
    private void moveEntity(GameEntity entity,Position newPos){
        //no need to lock,place entity and remove entity methods lock
        Position oldPos=entity.getPosition();
        gameMap.removeEntity(oldPos,entity);
        entity.setPosition(newPos);
        gameMap.placeEntity(newPos,entity);
        LogManager.addLog("Enemy moved from "+oldPos+" to "+newPos);
    }
    private void scheduleOneEnemy(Enemy enemy) {
        scheduler.schedule(() -> {
            enemy.run();
            scheduleOneEnemy(enemy);
            },100,TimeUnit.MILLISECONDS);
    }
    private void scheduleAllEnemies(){
        for (Enemy enemy : enemies){
            scheduleOneEnemy(enemy);
        }
    }
    public void shutdown() {
        isRunning.set(false);
        scheduler.shutdownNow();

    }


    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private Random random;
    private final int rows;
    private final int cols;
    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;
    private final GameMap gameMap;
    private final CombatSystem combatSystem;
    private final List<GameListener> listeners = new ArrayList<>();

}