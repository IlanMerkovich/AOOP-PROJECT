package Game.Combat;

import Game.Map.Position;

public interface RangedFighter {
    public void fightRanged(Combatant target);
    public int getRange();
    public boolean isInRange(Position self,Position target);
}
