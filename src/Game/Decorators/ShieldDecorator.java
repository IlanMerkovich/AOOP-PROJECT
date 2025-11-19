package Game.Decorators;

import Game.Characters.PlayerCharacter;
import Game.Combat.Combatant;

public class ShieldDecorator extends PlayerCharacterDecorator{
    public ShieldDecorator(PlayerCharacter player){
        super(player);
    }

    @Override
    public void receiveDamage(int amount, Combatant source) {
        super.receiveDamage((int)(amount*0.95), source);
    }
}
