package Game.Engine;
import Game.Core.GameEntity;
import Game.Items.GameItem;
import Game.Map.Position;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class GameMap {
    private final int rows, cols;
    private final Map<Position, List<GameEntity>> grid;
    private final ReentrantLock[][] cellLocks;
    private static volatile GameMap instance = null;

    public static GameMap getInstance(int rows, int cols) {
        if (instance == null) {
            synchronized (GameMap.class) {
                if (instance == null) {
                    instance = new GameMap(rows, cols);
                }
            }
        }
        return instance;
    }

    public static GameMap getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Map not created yet, please create map first.");
        }
        return instance;
    }

    public static void resetInstance() {
        synchronized (GameMap.class) {
            instance = null;
        }
    }

    private GameMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new ConcurrentHashMap<>();
        this.cellLocks = new ReentrantLock[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Position pos = new Position(i, j);
                grid.put(pos,Collections.synchronizedList(new ArrayList<>()));
                cellLocks[i][j] = new ReentrantLock(true);
            }
        }
    }

    public void placeEntity(Position pos, GameEntity entity) {
        if (pos == null || entity == null) return;
        ReentrantLock lock = cellLocks[pos.getRow()][pos.getCol()];
        lock.lock();
        try {
            List<GameEntity> list = grid.get(pos);
            if (!list.contains(entity)) {
                list.add(entity);
                entity.setPosition(pos);
            }
        } finally {
            lock.unlock();
        }
    }

    public void moveEntity(Position from, Position to, GameEntity entity) {
        if (from == null || to == null || entity == null) return;
        ReentrantLock fromLock = cellLocks[from.getRow()][from.getCol()];
        ReentrantLock toLock = cellLocks[to.getRow()][to.getCol()];

        ReentrantLock firstLock = from.hashCode() < to.hashCode() ? fromLock : toLock;
        ReentrantLock secondLock = from.hashCode() < to.hashCode() ? toLock : fromLock;

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                grid.get(from).remove(entity);
                entity.setPosition(to);
                List<GameEntity> toList = grid.get(to);
                if (!toList.contains(entity)) {
                    toList.add(entity);
                }
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }

    public boolean removeEntity(Position pos, GameEntity entity) {
        if (pos == null || entity == null) return false;
        ReentrantLock lock = cellLocks[pos.getRow()][pos.getCol()];
        lock.lock();
        try {
            return grid.get(pos).remove(entity);
        }
        finally {
            lock.unlock();
        }
    }

    public boolean isBlocked(Position pos) {
        ReentrantLock lock = cellLocks[pos.getRow()][pos.getCol()];
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

    public boolean tryLockCell(Position pos, long timeoutMillis) {
        try {
            return cellLocks[pos.getRow()][pos.getCol()].tryLock(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
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

    public List<GameEntity> getEntitiesAt(Position pos) {
        return grid.get(pos);
    }

    public void clearCell(Position pos) {
        ReentrantLock lock = cellLocks[pos.getRow()][pos.getCol()];
        lock.lock();
        try {
            grid.get(pos).clear();
        } finally {
            lock.unlock();
        }
    }

    public Map<Position, List<GameEntity>> getGrid() {
        return grid;
    }

    public void setGrid(Map<Position, List<GameEntity>> grid) {
        this.grid.clear();
        this.grid.putAll(grid);
    }
}