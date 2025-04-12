package Game.Combat;

import Game.Map.Position;

public interface MeleeFighter{
    public void fightClose(Combatant target);
    public boolean isInMeleeRange(Position self,Position target);
}
