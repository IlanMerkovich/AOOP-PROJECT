package Game.Combat;

import Game.Map.Position;
/**
 * Represents an entity that can perform ranged attacks.
 * A ranged fighter can attack from a distance using the fightRanged method,
 * and provides logic to determine attack range and range validation.
 */
public interface RangedFighter {
    /**
     * Performs a ranged attack on the given target.
     * @param target the target to attack
     */
    public void fightRanged(Combatant target);
    /**
     * Returns the maximum range this fighter can attack from.
     * @return the attack range (usually measured in Manhattan distance)
     */
    public int getRange();
    /**
     * Checks whether the target is within the allowed range from the fighter's position.
     * @param self the position of the fighter
     * @param target the position of the target
     * @return true if within range, false otherwise
     */
    public boolean isInRange(Position self,Position target);
}
