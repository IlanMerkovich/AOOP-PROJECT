package Game.Decorators;

import Game.Characters.PlayerCharacter;
import Game.Combat.Combatant;

public class RageDecorator extends PlayerCharacterDecorator {
    public RageDecorator(PlayerCharacter player){
        super(player);
    }
    @Override
    public void attack(Combatant target) {
        getBasePlayer().setPower(getPower()*2);
        super.attack(target);
        getBasePlayer().setPower(getPower()/2);
    }
}
