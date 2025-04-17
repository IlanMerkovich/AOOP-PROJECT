package Game.Characters;
import Game.Combat.*;
import Game.Map.Position;
import java.util.Random;
/**
 * Goblin enemy class — agile melee attacker.
 * Has a chance to evade based on agility. Uses melee attacks.
 */
public class Goblin extends Enemy implements PhysicalAttacker, MeleeFighter{
    private int agility;

    /**
     * Constructs a Goblin with random agility (0–80).
     * @param r row position
     * @param c column position
     */
    public Goblin(int r,int c){
        super(r,c);
        this.agility=new Random().nextInt(81);
    }
    /**
     * Calculates evasion chance based on agility and returns true if evasion succeeds.
     * @return true if evasion succeeds
     */
    public boolean tryEvade(Combatant attacker) {
        double baseEvasion = Math.min(0.8, agility / 100.0);
        double accuracyFactor = 1.0;
        accuracyFactor = 1.0 - attacker.getAccuracy();
        double finalEvasion = baseEvasion * accuracyFactor;
        double roll = new Random().nextDouble();
        return roll < finalEvasion;
    }
    /**
     * Textual representation of Goblin with agility and inherited info.
     */
    public String toString() {
        return String.format("👺 %s | Agility: %d", super.toString(), agility);
    }
    /**
     * Goblins have no magic element.
     * @return null
     */
    public MagicElement getElement() {
        return null;
    }
    /**
     * Goblins do not have accuracy.
     * @return 0
     */
    public double getAccuracy() {
        return 0;
    }
    /**
     * Performs melee attack if target is in range.
     * Applies critical hit with 10% chance.
     * @param target the combat target
     */
    public void fightClose(Combatant target){
        int totaldamage=this.getPower();
        if (isInMeleeRange(this.getPosition(),target.getPosition())){
            if (this.isCriticalHit()){
                totaldamage*=2;
            }
            target.receiveDamage(totaldamage,this);
        }
    }
    /**
     * Checks if target is in melee range (Manhattan distance == 1).
     */
    public boolean isInMeleeRange(Position self, Position target){
        return self.distanceTo(target)==1;
    }
    /**
     * Uses melee attack for goblin.
     */
    public void attack(Combatant target){
        fightClose(target);
    }
    /**
     * @return 10% critical hit chance.
     */
    public boolean isCriticalHit(){
        return new Random().nextDouble()<0.1;
    }
    /**
     * Display symbol for map rendering.
     * @return "GO"
     */
    public String getDisplaySymbol() {
        return "GO";
    }
}
