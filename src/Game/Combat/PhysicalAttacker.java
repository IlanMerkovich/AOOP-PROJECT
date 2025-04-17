package Game.Combat;
/**
 * Represents an entity capable of performing physical attacks.
 * Provides a method to perform the attack and a chance-based check
 * for executing a critical hit.
 */
public interface PhysicalAttacker{
    /**
     * Performs a physical attack on the given target.
     * @param target the target to attack
     */
    public void attack(Combatant target);
    /**
     * Determines if the current attack is a critical hit.
     * Critical hits do double damage.
     * This should return true with a probability of 10%.
     * @return true if the attack is critical, false otherwise
     */
    public boolean isCriticalHit();//10% for double damage//
}
