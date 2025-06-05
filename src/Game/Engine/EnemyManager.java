package Game.Engine;

import Game.Characters.Enemy;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class EnemyManager {
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean isRunning;
    private final List<Enemy> enemies;

    public EnemyManager(ScheduledExecutorService scheduler, AtomicBoolean isRunning, List<Enemy> enemies) {
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
        scheduler.schedule(() -> {
            if (isRunning.get()) {
                enemy.run();
                scheduleEnemy(enemy);
            }
        }, 100, TimeUnit.MILLISECONDS);
    }
    public void shutdown() {
        isRunning.set(false);
        scheduler.shutdownNow();
    }
}
