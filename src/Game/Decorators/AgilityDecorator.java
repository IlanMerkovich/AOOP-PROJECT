package Game.Decorators;

import Game.Characters.PlayerCharacter;
import Game.Combat.Combatant;

import java.util.Random;

public class AgilityDecorator extends PlayerCharacterDecorator{
    public AgilityDecorator(PlayerCharacter player){
        super(player);
    }
    public void receiveDamage(int amount, Combatant source) {
        if (new Random().nextBoolean())
            return;
        getBasePlayer().receiveDamage(amount, source);
    }

}
