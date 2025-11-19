package Game.Builders;

import Game.Characters.Dragon;
import Game.Characters.Enemy;
import Game.Characters.Goblin;
import Game.Characters.Orc;

public class EnemyBuilder {
    private Enemy enemy;
    private String enemyType;

    public EnemyBuilder(String type){
        this.enemyType = type;
        this.reset();
    }

    public void reset() {
        switch (enemyType) {
            case "Goblin" -> this.enemy = new Goblin(0, 0);
            case "Orc" -> this.enemy = new Orc(0, 0);
            case "Dragon" -> this.enemy = new Dragon(0, 0);
            default -> throw new IllegalArgumentException("Unknown enemy type: " + enemyType);
        }
    }

    public void setHealth(int i){
        enemy.setHealth(50);
    }

    public void setPower(int power){
        enemy.addPower(power - enemy.getPower());
    }

    public void setLoot(int loot){
        enemy.setLoot(loot);
    }

    public Enemy getProduct() {
        Enemy result = enemy.clone();
        this.reset();
        return result;
    }
}