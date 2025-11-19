package Game.Engine;

import Game.Characters.Enemy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class EnemyManager {
    private ExecutorService scheduler;
    private AtomicBoolean isRunning;
    private List<Enemy> enemies;

    public EnemyManager(ExecutorService scheduler, AtomicBoolean isRunning, List<Enemy> enemies) {
        this.scheduler = scheduler;
        this.isRunning = isRunning;
        this.enemies = enemies;
    }
    public void startAllEnemies() {
        for (Enemy enemy : enemies) {
            scheduleEnemy(enemy);
        }
    }
    void scheduleEnemy(Enemy enemy) {
        scheduler.submit(()->{
            if(isRunning.get()){
                enemy.run();
                if (!enemy.isDead()){
                    scheduleEnemy(enemy);
                }
            }
        });
    }
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }
    public void restart(ExecutorService scheduler, AtomicBoolean isRunning, List<Enemy> enemies) {
        shutdown();
        this.scheduler = scheduler;
        this.isRunning = isRunning;
        this.enemies = enemies;
    }
    public void addEnemyToList(Enemy enemy){
        this.enemies.add(enemy);
    }
    public AtomicBoolean getIsRunning() {
        return isRunning;
    }
    public void removeEnemyFromScheduling(Enemy enemy) {
        this.enemies.remove(enemy);
    }
}