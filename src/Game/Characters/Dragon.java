package Game.Characters;

import Game.Combat.*;
import Game.Map.Position;

import java.util.Random;
/**
 * Represents a Dragon enemy in the game.
 * The Dragon can perform both melee and magical ranged attacks,
 * and has an associated magic element which affects damage calculation.
 */
public class Dragon extends Enemy implements PhysicalAttacker,MeleeFighter, MagicAttacker, RangedFighter{
    /**
     * Constructs a Dragon at a specific position with a random magic element.
     *
     * @param r row position
     * @param c column position
     */
    public Dragon(int r,int c){
        super(r,c);
        this.element=MagicElement.getRandomElement();
    }
    public Dragon(int r, int c, MagicElement element) {
        super(r, c);
        this.element = element;
    }
    /**
     * Calculates and applies magic damage to a target.
     * Takes into account elemental advantage or disadvantage.
     *
     * @param target the combatant being attacked
     */
    public void calculateMagicDamage(Combatant target){
        double damage= (this.getPower()*1.5);
        MagicElement otherelement=target.getElement();
        if (otherelement!=null){
            if (this.element.isStrongerThan(otherelement)){
                damage*=1.2;
            }
            else if (otherelement.isStrongerThan(this.element)){
                damage*=0.8;
            }
        }
        target.receiveDamage((int) damage,this);
    }
    /**
     * Casts a magic spell on the target using elemental logic.
     *
     * @param target the combatant to cast on
     */
    public void castSpell(Combatant target) {
        calculateMagicDamage(target);
    }
    /**
     * Returns the dragon's magic element.
     *
     * @return the element
     */
    public MagicElement getElement() {
        return this.element;
    }
    /**
     * Determines whether this dragon has a stronger element than the given attacker.
     *
     * @param other another magic attacker
     * @return true if stronger, false otherwise
     */
    public boolean isElementStrongerThan(MagicAttacker other) {
        if (other.getElement()!=null){
            return this.element.isStrongerThan(other.getElement());
        }
        else{
            return false;
        }
    }
    /**
     * Dragons do not have accuracy; always returns 0.
     *
     * @return 0
     */
    public double getAccuracy() {
        return 0;
    }
    /**
     * Performs a melee attack on the target with a chance for critical hit.
     *
     * @param target the combatant to attack
     */
    public void fightClose(Combatant target) {
        int damage = getPower();
        if (isCriticalHit()) {
            damage *= 2;
        }
        target.receiveDamage(damage, this);
    }
    /**
     * Checks if target is in melee range (distance = 1).
     *
     * @param self   dragon's position
     * @param target target's position
     * @return true if in range
     */
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target) == 1;
    }
    /**
     * Performs a ranged spell attack if target is in range.
     *
     * @param target the combatant to attack
     */
    public void fightRanged(Combatant target) {
        if (isInRange(this.getPosition(),target.getPosition())) {
            castSpell(target);
        }
    }
    /**
     * Returns the dragon's ranged attack range (fixed at 2).
     *
     * @return range
     */
    public int getRange() {
        return 2;
    }
    /**
     * Checks if a target is within ranged attack distance.
     *
     * @param self   dragon's position
     * @param target target's position
     * @return true if in range
     */
    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target) <= getRange();
    }
    /**
     * Executes an attack. Chooses melee if in range, otherwise casts a spell.
     *
     * @param target the combatant to attack
     */
    public void attack(Combatant target){
        if (isInMeleeRange(getPosition(), target.getPosition())) {
            fightClose(target);
        }
        else {
            fightRanged(target);
        }
    }
    /**
     * Returns the map symbol representing the dragon.
     *
     * @return "DR"
     */
    public String getDisplaySymbol() {
        return "dragon";
    }
    /**
     * Returns a string representation of the dragon, including its element type and loot value.
     *
     * @return a formatted string with dragon details
     */
    public String toString() {
        return String.format("🐉 Dragon | Element: %s | %s",
                element.toString(),
                super.toString());
    }
    /**
     * Checks if this dragon is equal to another object.
     * Equality is based on superclass fields and magic element.
     *
     * @param obj the object to compare
     * @return true if the dragons are equal, false otherwise
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Dragon other))
            return false;
        return super.equals(obj) && this.element == other.getElement();
    }
    private MagicElement element;

    public Dragon clone() {
        Dragon copy = new Dragon(getPosition().getRow(), getPosition().getCol(), this.element);
        copyEnemyFieldsTo(copy);
        return copy;
    }
}
