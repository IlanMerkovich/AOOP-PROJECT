package Game.Combat;
/**
 * Interface representing a character that can perform magical attacks.
 */
public interface MagicAttacker{
    /**
     * Calculates and applies magic damage to the given target,
     * based on the caster's magic power and elemental advantages.
     *
     * @param target the combatant receiving the damage
     */
    public void calculateMagicDamage(Combatant target);
    /**
     * Casts a spell on the given target. May apply magic effects or damage.
     *
     * @param target the combatant to cast the spell on
     */
    public void castSpell(Combatant target);
    /**
     * Gets the magic element associated with this attacker.
     *
     * @return the attacker's magic element
     */
    public MagicElement getElement();
    /**
     * Compares elemental strengths and determines if this attacker's element
     * is stronger than the other's.
     *
     * @param other another magic attacker to compare elements with
     * @return true if this element is stronger, false otherwise
     */
    public boolean isElementStrongerThan(MagicAttacker other);
}
