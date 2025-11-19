package Game.Decorators;

import Game.Characters.Enemy;
import Game.Combat.Combatant;

public class ExplodingEnemyDecorator extends EnemyDecorator{
    public ExplodingEnemyDecorator(Enemy enemy){
        super(enemy);
    }

    @Override
    public void receiveDamage(int amount, Combatant source) {
        super.receiveDamage(amount, source);
        if (getDecoratedEnemy().isDead()){
            source.receiveDamage((int) (source.getHealth()*0.10),this);
        }
    }
}