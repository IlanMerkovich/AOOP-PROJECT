package Game.Combat;

import Game.Map.Position;
/**
 * Represents an entity capable of performing close-range (melee) combat.
 * Provides methods to execute an attack and check if the target is within melee range.
 */
public interface MeleeFighter{
    /**
     * Performs a melee attack on the specified target.
     * Typically used when the target is adjacent to the attacker.
     * @param target the combatant to attack at close range
     */
    public void fightClose(Combatant target);
    /**
     * Checks if the target is within valid melee range.
     * The standard melee range is a Manhattan distance of 1.
     * @param self the position of the attacker
     * @param target the position of the target
     * @return true if the target is within melee range, false otherwise
     */
    public boolean isInMeleeRange(Position self,Position target);
}
