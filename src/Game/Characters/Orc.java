package Game.Characters;

import Game.Combat.*;
import Game.Map.Position;

import java.util.Random;
/**
 * Represents an Orc enemy character.
 * Orcs are melee fighters with resistance against magic attacks.
 */
public class Orc extends Enemy implements PhysicalAttacker, MeleeFighter{
    /**
     * Constructs a new Orc at the given row and column with randomized resistance.
     *
     * @param r the row position of the Orc
     * @param c the column position of the Orc
     */
    public Orc(int r,int c){
        super(r,c);
        this.resistance=new Random().nextDouble(0.5);
    }
    /**
     * Gets the magic element of the Orc (always null since Orcs don't use magic).
     *
     * @return null
     */
    public MagicElement getElement() {
        return null;
    }
    /**
     * Returns the accuracy of the Orc (always 0 since Orcs don't use ranged attacks).
     *
     * @return 0
     */
    public double getAccuracy() {
        return 0;
    }
    /**
     * Performs a melee attack on a target if it is within range.
     *
     * @param target the combatant to attack
     */
    public void fightClose(Combatant target){
        int totaldamage=getPower();
        if (this.isInMeleeRange(this.getPosition(),target.getPosition())){
            if (isCriticalHit()){
                totaldamage*=2;
            }
            target.receiveDamage(totaldamage,this);
        }
    }
    /**
     * Checks if the target is within melee range (distance <= 1).
     *
     * @param self   the Orc's position
     * @param target the target's position
     * @return true if in melee range, false otherwise
     */
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target)<=1;
    }
    /**
     * Attacks a target using melee combat.
     *
     * @param target the combatant to attack
     */
    public void attack(Combatant target){
        fightClose(target);
    }
    /**
     * Returns true with a 10% chance to simulate a critical hit.
     *
     * @return true if it's a critical hit, false otherwise
     */
    public boolean isCriticalHit() {
        return new Random().nextDouble()<0.1;
    }
    /**
     * Applies damage to the Orc after checking for evasion and magical resistance.
     *
     * @param amount the amount of damage to apply
     * @param source the attacker
     */
    public void receiveDamage(int amount, Combatant source) {
        int damageReceived=amount;
        if (tryEvade(source)){
            return;
        }
        if (source.getElement()!=null){
            damageReceived*=(1-this.resistance);
        }
        this.getDamage(damageReceived);
    }
    /**
     * Returns a string representation of the Orc including resistance and inherited info.
     *
     * @return string with Orc details
     */
    public String toString() {
        return String.format("👹 %s | Resistance: %.2f",
                super.toString(),
                resistance);
    }
    /**
     * Returns a display symbol representing the Orc.
     *
     * @return the display symbol "OR"
     */
    public String getDisplaySymbol() {
        return "orc";
    }
    /**
     * Checks equality between this Orc and another object.
     * Equality is based on resistance and inherited properties.
     *
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Orc other)) return false;
        return super.equals(obj) && Double.compare(this.resistance, other.resistance) == 0;
    }

    private final double resistance;
}
