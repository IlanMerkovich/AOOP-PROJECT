package Game.Decorators;

import Game.Characters.Enemy;
import Game.Combat.Combatant;

public class TeleportingEnemyDecorator extends EnemyDecorator{
    public TeleportingEnemyDecorator(Enemy enemy){
        super(enemy);
    }

    @Override
    public void receiveDamage(int amount, Combatant source) {
        super.receiveDamage(amount, source);
        if (this.getHealth()<=20){
            getGameController().enemyTeleport(this);
        }
    }
}