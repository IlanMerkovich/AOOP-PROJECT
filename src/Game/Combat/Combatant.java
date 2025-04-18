package Game.Combat;

import Game.Map.Position;
/**
 * Represents any game entity that can participate in combat,
 * including both player characters and enemies.
 */
public interface Combatant{
    /**
     * Gets the current health of the combatant.
     *
     * @return the health value
     */
    public int getHealth();
    /**
     * Sets the health of the combatant.
     * Should ensure health is within valid bounds.
     *
     * @param health the new health value
     * @return true if the health was set successfully, false otherwise
     */
    public boolean setHealth(int health);
    /**
     * Applies damage to the combatant from another source.
     *
     * @param amount the damage amount
     * @param source the attacker
     */
    public void receiveDamage(int amount,Combatant source);
    /**
     * Checks if the combatant is dead (health is zero or less).
     *
     * @return true if dead, false otherwise
     */
    public boolean isDead();
    /**
     * Returns the combat power of the combatant.
     *
     * @return the power value
     */
    public int getPower();
    /**
     * Attempts to evade an attack from another combatant.
     *
     * @param attacker the attacker
     * @return true if the evasion was successful, false otherwise
     */
    public boolean tryEvade(Combatant attacker);
    /**
     * Returns the current position of the combatant on the game map.
     *
     * @return the position
     */
    public Position getPosition();
    /**
     * Returns the magic element of the combatant, if applicable.
     *
     * @return the magic element, or null if not relevant
     */
    public MagicElement getElement();
    /**
     * Returns the accuracy of the combatant's attacks.
     * Used to affect opponent's evasion chance.
     *
     * @return accuracy value (0.0–1.0)
     */
    public double getAccuracy();
    /**
     * Performs an attack on the given target.
     *
     * @param target the target to attack
     */
    void attack(Combatant target);

}
