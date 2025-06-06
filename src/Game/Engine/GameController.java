package Game.Engine;

import Game.Characters.*;
import Game.Core.GameEntity;
import Game.Factory.EnemyFactory;
import Game.Factory.Factory;
import Game.Items.*;
import Game.Logs.LogManager;
import Game.Map.Position;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameController {
    private static GameWorld world;
    private final EnemyManager enemyManager;
    private final Factory enemyFactory=new EnemyFactory();

    public GameController(GameWorld world) {
        this.world = world;
        int r=world.getRows();
        int c=world.getCols();
        System.out.println("created "+calculateNumOfThreads(r,c)+" threads.");
        ExecutorService scheduler = Executors.newFixedThreadPool(calculateNumOfThreads(r,c));
        AtomicBoolean isRunning = new AtomicBoolean(true);
        this.enemyManager = new EnemyManager(scheduler, isRunning, world.getEnemies());
        this.enemyManager.startAllEnemies();
        for (Enemy enemy:world.getEnemies()){
            enemy.init(this,isRunning);
        }
        updateVisibility(getPlayer().getPosition());
        world.notifyListeners();
        world.notifyMapChange();
    }
    private int calculateNumOfThreads(int r,int c){
        int numOfThreads= (int) ((r*c)*0.03);
        return numOfThreads = Math.max(1, Math.min(10, numOfThreads));
    }
    public void shutdownEnemies() {
        enemyManager.shutdown();
    }
    public void movePlayerTo(Position dest){
        PlayerCharacter p = world.getPlayer();
        Position src = p.getPosition();

        if (!world.getGameMap().isBlocked(dest)){
            try{
                if (world.getGameMap().tryLockCell(dest,50) && world.getGameMap().tryLockCell(src,50)){
                    if (world.getGameMap().getGrid().get(dest).isEmpty() && !world.getGameMap().isBlocked(dest)){
                        world.getGameMap().removeEntity(src, p);
                        p.setPosition(dest);
                        world.getGameMap().placeEntity(dest, p);
                        LogManager.addLog("Player moved from "+src +" to "+dest);
                    }
                }
                updateVisibility(dest);
                world.notifyListeners();
            }
            finally {
                world.getGameMap().unlockCell(dest);
                world.getGameMap().unlockCell(src);
            }
        }
    }
    public void attackEnemyAt(Position pos) {
        if (world.getGameMap().tryLockCell(pos, 50)) {
            try {
                List<GameEntity> list = world.getGameMap().getGrid().get(pos);
                if (list != null && !list.isEmpty() && list.get(0) instanceof Enemy e) {
                    manageFight(world.getPlayer(),e);
                    updateVisibility(world.getPlayer().getPosition());
                }
            }
            finally {
                world.getGameMap().unlockCell(pos);
                world.notifyListeners();
            }
        }
    }
    public void pickupItemAt(Position pos) {
        if (world.getGameMap().tryLockCell(pos, 50)) {
            try {
                List<GameEntity> list = world.getGameMap().getGrid().get(pos);
                if (list != null && !list.isEmpty() && list.get(0) instanceof Pickupable pick) {
                    pick.pickup(world.getPlayer());
                    list.remove(0);
                    updateVisibility(world.getPlayer().getPosition());
                    LogManager.addLog("Player picked up an item at: " + pos);
                }
            }
            finally {
                world.getGameMap().unlockCell(pos);
                world.notifyListeners();
            }
        }
    }
    public void interactWithItemAt(Position pos) {
        if (world.getGameMap().tryLockCell(pos, 100)) {
            try {
                List<GameEntity> cell = world.getGameMap().getGrid().get(pos);
                if (cell == null)
                    return;
                Iterator<GameEntity> it = cell.iterator();
                while (it.hasNext()) {
                    GameEntity ent = it.next();
                    if (ent instanceof Interactable inter) {
                        inter.interact(world.getPlayer());
                        LogManager.addLog("Player interacted with a treasure at: " + pos);
                        it.remove();
                        updateVisibility(world.getPlayer().getPosition());
                        return;
                    }
                }
            } finally {
                world.getGameMap().unlockCell(pos);
                world.notifyListeners();
            }
        }
    }
    public void useItem(GameItem item) {
        PlayerCharacter p = world.getPlayer();
        boolean used;
        if (item instanceof PowerPotion) {
            used = p.usePowerPotion();
        } else {
            used = p.usePotion();
        }
        if (used) {
            world.notifyListeners();
        }
    }
    public void attemptToMove(Enemy enemy) {
        if (enemy.isDead()) {
            return;
        }
        Position enemyPos = enemy.getPosition();
        Position playerPos = world.getPlayer().getPosition();
        double distToPlayer = enemyPos.distanceTo(playerPos);

        if (distToPlayer <= 2) {
            for (Position nextStep : world.getStepPositionNearPlayer(playerPos)) {
                if (nextStep.equals(enemyPos)) {
                    continue;
                }
                if (tryMoveto(enemyPos,nextStep)) {
                    if (world.getGameMap().tryLockCell(enemyPos, 50)) {
                        try {
                            if (world.getGameMap().tryLockCell(nextStep, 50)) {
                                try {
                                    moveEntity(enemy, nextStep);
                                    updateVisibility(playerPos);
                                    world.notifyMapChange();
                                } finally {
                                    world.getGameMap().unlockCell(nextStep);
                                }
                            }
                        } finally {
                            world.getGameMap().unlockCell(enemyPos);
                        }
                    }
                    return;
                }
            }
        } else {
            List<Position> neighbors = world.getPositionsShuffled(enemyPos);
            for (Position target : neighbors) {
                if (tryMoveto(enemyPos, target) && world.getGameMap().tryLockCell(enemyPos, 50)) {
                    try {
                        if (world.getGameMap().tryLockCell(target, 50)) {
                            try {
                                moveEntity(enemy, target);
                                if (enemy.getVisibility()) {
                                    world.notifyMapChange();
                                    updateVisibility(playerPos);
                                }
                            } finally {
                                world.getGameMap().unlockCell(target);
                            }
                        }
                    } finally {
                        world.getGameMap().unlockCell(enemyPos);
                    }
                    return;
                }
            }
        }
        world.notifyMapChange();
    }
    private boolean tryMoveto(Position src, Position dest){
        return src.distanceTo(dest) == 1 && !world.getGameMap().isBlocked(dest) && world.getGameMap().getGrid().get(dest).isEmpty();
    }
    private void moveEntity(GameEntity entity, Position newPos) {
        Position oldPos = entity.getPosition();
        world.getGameMap().removeEntity(oldPos, entity);
        entity.setPosition(newPos);
        world.getGameMap().placeEntity(newPos, entity);
        LogManager.addLog("Enemy moved from " + oldPos + " to " + newPos);
    }
    private void updateVisibility(Position playerPos) {
        for (List<GameEntity> cell : world.getGameMap().getGrid().values()) {
            for (GameEntity entity : cell) {
                boolean visible = playerPos.distanceTo(entity.getPosition()) <= 2;
                entity.setVisible(visible);
            }
        }
    }
    private void manageFight(PlayerCharacter player, Enemy enemy){
        Position playerPos = player.getPosition();
        Position enemyPos = enemy.getPosition();
        if (world.getGameMap().tryLockCell(enemyPos, 50)) {
            try {
                if (world.getGameMap().tryLockCell(playerPos, 50)) {
                    try {
                        world.getCombatSystem().resolveCombat(player, enemy);
                        placeTreasure(enemy);
                        placeNewEnemy(enemy,world.getEnemies());
                        world.getEnemies().remove(enemy);

                    } finally {
                        world.getGameMap().unlockCell(playerPos);
                    }
                }
            } finally {
                world.getGameMap().unlockCell(enemyPos);
            }
        }
    }
    private void placeTreasure(Enemy enemy){
        if (world.getGameMap().tryLockCell(enemy.getPosition(), 50)) {
            try {
                if (enemy.isDead()) {
                    Treasure treasure = enemy.Defeat();
                    world.addItem(treasure);
                    LogManager.addLog("Treasure was created at: " + treasure.getPosition());
                    world.getGameMap().removeEntity(enemy.getPosition(), enemy);
                    world.getGameMap().placeEntity(enemy.getPosition(), treasure);
                }
            } finally {
                world.getGameMap().unlockCell(enemy.getPosition());
                world.notifyMapChange();
            }
        }
    }
    private void placeNewEnemy(Enemy enemy, List<Enemy> enemies) {
        if (!enemy.isDead())
            return;
        if (world.getGameMap().tryLockCell(enemy.getPosition(), 50)) {
            try {
                Position newPos = world.getRandomEmptyPosition(world.getRows(), world.getCols(), new Random());
                if (newPos == null)
                    return;
                if (world.getGameMap().tryLockCell(newPos, 50)) {
                    try {
                        Enemy newEnemy = enemyFactory.createCharacter(enemies, newPos.getRow(), newPos.getCol());
                        enemyManager.scheduleEnemy(newEnemy);
                        world.placeNewEnemy(newEnemy, newPos);
                    } finally {
                        world.getGameMap().unlockCell(newPos);
                    }
                }
            } finally {
                world.getGameMap().unlockCell(enemy.getPosition());
            }
        }
    }
    public GameMap getGameMap(){
        return world.getGameMap();
    }
    public PlayerCharacter getPlayer(){
        return world.getPlayer();
    }
    public boolean areAllEnemiesDead() {
        for (Enemy e : world.getEnemies()) {
            if (!e.isDead())
                return false;
        }
        return true;
    }
    public static void setNewWorld(GameWorld gameWorld){
        world=gameWorld;
    }
    public void restoreEnemyThreads() {
        int r = world.getRows();
        int c = world.getCols();
        ExecutorService scheduler = Executors.newFixedThreadPool(calculateNumOfThreads(r, c));
        AtomicBoolean isRunning = new AtomicBoolean(true);
        this.enemyManager.restart(scheduler, isRunning, world.getEnemies());
        for (Enemy enemy : world.getEnemies()) {
            if (!enemy.isDead()) {
                enemy.init(this, isRunning);
            }
        }
        this.enemyManager.startAllEnemies();
    }
}
