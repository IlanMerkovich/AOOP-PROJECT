package Game.Builders;

import java.util.Random;

public class EnemyBuildDirector {
    public void constructRandomEnemy(EnemyBuilder builder) {
        Random random = new Random();
        builder.reset();
        builder.setHealth(80);
        builder.setPower(random.nextInt(4, 14));
        builder.setLoot(random.nextInt(100, 301));
    }
}