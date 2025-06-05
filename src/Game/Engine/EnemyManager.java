package Game.Engine;

import Game.Characters.Enemy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class EnemyManager {
    private final ExecutorService scheduler;
    private final AtomicBoolean isRunning;
    private final List<Enemy> enemies;

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
    private void scheduleEnemy(Enemy enemy) {
        scheduler.submit(()->{
            if(isRunning.get()){
                enemy.run();
                scheduleEnemy(enemy);
            }
        });
    }
    public void shutdown() {
        isRunning.set(false);
        scheduler.shutdownNow();
    }
}
