package Game.Engine;
import Game.Core.GameEntity;
import Game.Items.GameItem;
import Game.Map.Position;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * GameMap represents the 2D game board that holds all entities in the world,
 * including players, enemies, and items.
 * Each cell in the grid can contain multiple entities.
 * The map supports operations such as placing entities, displaying the map,
 * and checking for movement blockage.
 */
public class GameMap{
    private int rows,cols;
    private static volatile GameMap instance=null;
    /**
     * Constructs a new GameMap with the given number of rows and columns.
     * Initializes each cell in the grid as an empty list.
     *
     * @param rows number of rows (height of the map)
     * @param cols number of columns (width of the map)
     */
    public static GameMap getInstance(int rows, int cols){
        if (instance==null){
            instance=new GameMap(rows,cols);
        }
        return instance;
    }
    public static GameMap getInstance(){
        if (instance==null){
            throw new IllegalStateException("Map not created yet,Please create map first.");
        }
        return instance;
    }
    private GameMap(int rows, int cols) {
        this.rows=rows;
        this.cols=cols;
        this.grid = new HashMap<>();
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                grid.put(new Position(i,j),new ArrayList<>());
            }
        }
        cellLocks = new ReentrantLock[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cellLocks[i][j] = new ReentrantLock(true);
            }
        }
    }
    /**
     * Places a game entity on the map at a given position.
     * If the position does not yet exist in the grid, it is initialized.
     *
     * @param position the position to place the entity
     * @param entity   the game entity to place
     */
    public void placeEntity(Position position,GameEntity entity){
        if (position==null || entity==null){
            return;
        }
        ReentrantLock lock=new ReentrantLock(true);
        lock.lock();
        try {
            if (!grid.containsKey(position)) {
                grid.put(position, new ArrayList<>());
            }
            entity.setPosition(position);
            grid.get(position).add(entity);
        }
        finally {
            lock.unlock();
        }
    }
    /**
     * Returns the full map grid containing all entities on the board.
     *
     * @return a map from Position to a list of GameEntities
     */
    public Map<Position,List<GameEntity>>getGrid(){
        return grid;
    }

    /**
     * Checks whether the specified position contains any entity
     * that blocks movement (such as walls or uncollected items).
     *
     * @param pos the position to check
     * @return true if movement is blocked, false otherwise
     */
    public boolean isBlocked(Position pos) {
        ReentrantLock lock=new ReentrantLock(true);
        lock.lock();
        try {
            List<GameEntity> entities = grid.get(pos);
            for (GameEntity entity : entities) {
                if (entity instanceof GameItem item && item.isBlocksMovement()) {
                    return true;
                }
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }
    /**
     * Removes the specified entity from the given position on the map.
     *
     * @param pos    the position from which to remove the entity
     * @param entity the entity to remove
     * @return true if the entity was found and removed, false otherwise
     */
    public boolean removeEntity(Position pos, GameEntity entity) {
        ReentrantLock lock=new ReentrantLock(true);
        lock.lock();
        try{
            List<GameEntity> cell = grid.get(pos);
            if (cell != null && cell.contains(entity)) {
                return cell.remove(entity);
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }
    public boolean tryLockCell(Position pos, long timeoutMillis) {
        ReentrantLock lock = cellLocks[pos.getRow()][pos.getCol()];
        try {
            return lock.tryLock(timeoutMillis, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
    public void unlockCell(Position pos) {
        ReentrantLock lock = cellLocks[pos.getRow()][pos.getCol()];
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
    private Map<Position,List<GameEntity>>grid;
    private ReentrantLock[][] cellLocks;
}
