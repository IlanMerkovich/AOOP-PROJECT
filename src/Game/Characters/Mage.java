package Game.Characters;

import Game.Combat.*;
import Game.Map.Position;

/**
 * Represents a Mage character that can perform ranged magic attacks
 * using a specific magic element. Mages use elemental strengths and weaknesses
 * to enhance or reduce damage.
 */
public class Mage extends PlayerCharacter implements MagicAttacker, RangedFighter{
    private final MagicElement element;
    /**
     * Constructs a Mage at a specific position with a given name.
     * @param r the row position
     * @param c the column position
     * @param name the character name
     */
    public Mage(int r,int c,String name){
        super(r,c,name);
        this.element=MagicElement.getRandomElement();
    }
    /**
     * Compares this mage with another object.
     * @param obj the other object
     * @return true if equal in type and content, false otherwise
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Mage) || !super.equals(obj))
            return false;
        Mage other = (Mage) obj;
        return this.element == other.getElement();
    }
    /**
     * Calculates and applies magic damage to a target,
     * considering elemental strength and weakness.
     * @param target the target receiving damage
     */
    public void calculateMagicDamage(Combatant target) {
        double totaldamage = this.getPower() * 1.5;
        MagicElement targetElement = target.getElement();
        if (targetElement != null) {
            if (this.element.isStrongerThan(targetElement)) {
                totaldamage *= 1.2;
            } else if (targetElement.isStrongerThan(this.element)) {
                totaldamage *= 0.8;
            }
        }
        target.receiveDamage((int) totaldamage, this);
    }
    /**
     * Casts a spell on the target.
     * @param target the target of the spell
     */
    public void castSpell(Combatant target) {
        calculateMagicDamage(target);
    }
    /**
     * Gets the mage's magic element.
     * @return the element
     */
    public MagicElement getElement(){
        return element;
    }
    /**
     * Mages don't use accuracy; returns 0.
     * @return 0
     */
    public double getAccuracy() {
        return 0;
    }
    /**
     * Returns true if this mage's element is stronger than the other's.
     * @param other another magic attacker
     * @return true if stronger, false otherwise
     */
    public boolean isElementStrongerThan(MagicAttacker other){
        return element.isStrongerThan(other.getElement());
    }
    /**
     * Executes a ranged magical attack on the target.
     * @param target the enemy
     */
    public void fightRanged(Combatant target){
        if (this.isInRange(this.getPosition(),target.getPosition())){
            calculateMagicDamage(target);
        }
    }
    /**
     * Gets the range of the mage's attack.
     * @return range value
     */
    public int getRange(){
        return 2;
    }
    /**
     * Determines if the target is within range.
     * @param self the mage's position
     * @param target the target's position
     * @return true if in range, false otherwise
     */
    public boolean isInRange(Position self, Position target){
        return self.distanceTo(target)<=getRange();
    }
    /**
     * Gets the string representation of the mage on the map.
     * @return "MA"
     */
    public String getDisplaySymbol() {
        return "mage";
    }
    /**
     * Performs an attack on a target using magic.
     * @param target the enemy to attack
     */
    public void attack(Combatant target) {
        castSpell(target);
    }
    /**
     * Returns a string with all relevant mage info for debug or display.
     * @return formatted string with stats
     */
    public String toString() {
        return String.format("🧙 %s | Element: %s", super.toString(), element);
    }
}
