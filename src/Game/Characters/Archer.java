package Game.Characters;

import Game.Combat.*;
import Game.Items.GameItem;
import Game.Map.Position;

import java.util.Random;
/**
 * Represents a ranged physical attacker (archer) in the game.
 * Inherits from PlayerCharacter and implements RangedFighter and PhysicalAttacker.
 * Has a unique accuracy value that affects enemy evasion.
 */
public class Archer extends PlayerCharacter implements RangedFighter, PhysicalAttacker{
    /**
     * Constructs a new Archer with a randomized accuracy value between 0.0 and 0.8.
     *
     * @param r    row position
     * @param c    column position
     * @param name the name of the archer
     */
    public Archer(int r,int c,String name){
        super(r,c,name);
        accuracy=new Random().nextDouble()*0.8;
    }
    /**
     * Checks equality with another Archer based on accuracy and inherited fields.
     *
     * @param obj object to compare
     * @return true if the archers are equal, false otherwise
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Archer) || !super.equals(obj)) return false;
        Archer other = (Archer) obj;
        return this.accuracy==other.getAccuracy();
    }
    /**
     * Returns the accuracy of the archer (0.0 to 0.8).
     *
     * @return accuracy value
     */
    public double getAccuracy() {
        return accuracy;
    }
    /**
     * Performs an attack using ranged logic.
     *
     * @param target the combatant being attacked
     */
    public void attack(Combatant target){
        fightRanged(target);
    }
    /**
     * Executes a ranged attack if the target is within range.
     *
     * @param target the target to attack
     */
    public void fightRanged(Combatant target){
        if (this.isInRange(this.getPosition(),target.getPosition())){
            int damage=this.getPower();
            if (isCriticalHit()){
                damage*=2;
            }
            target.receiveDamage(damage,this);
        }
        else{
            System.out.println("Target out of range");
        }
    }
    /**
     * Returns the attack range of the archer.
     *
     * @return fixed range value (2)
     */
    public int getRange() {
        return 2;
    }
    /**
     * Returns a display symbol representing the archer for map visualization.
     *
     * @return "AR" (Archer)
     */
    public String getDisplaySymbol() {
        return "archer";
    }
    /**
     * Archers do not use magic; returns null.
     *
     * @return null
     */
    public MagicElement getElement() {
        return null;
    }
    /**
     * Checks if the target is within the archer's attack range.
     *
     * @param self   position of the archer
     * @param target position of the target
     * @return true if within range, false otherwise
     */
    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target)<=getRange();
    }
    /**
     * Returns a formatted string representation of the archer.
     *
     * @return string including symbol and accuracy
     */
    public String toString() {
        return String.format("🏹 %s | Accuracy: %.2f",
                super.toString(),
                getAccuracy());
    }
    private double accuracy;
    public Archer clone() {
        Archer copy = new Archer(getPosition().getRow(), getPosition().getCol(), getName());
        copy.addTreasurePoint(getTreasurePoints());
        copyFieldsTo(copy);
        copy.accuracy = this.accuracy;
        copy.getInventory().getItems().clear();
        for (GameItem item : getInventory().getItems()) {
            copy.addToInventory((GameItem) item.clone());
        }
        return copy;
    }
}
