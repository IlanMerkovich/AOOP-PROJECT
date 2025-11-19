package Game.Decorators;

import Game.Characters.Enemy;
import Game.Combat.Combatant;

public class VampireEnemyDecorator extends EnemyDecorator{
    public VampireEnemyDecorator(Enemy enemy){
        super(enemy);
    }

    @Override
    public void attack(Combatant target) {
        super.attack(target);
        getDecoratedEnemy().setHealth((int) (getHealth()+target.getHealth()*0.10));
    }
}