package Game.Characters;

import Game.Combat.*;
import Game.Items.GameItem;
import Game.Logs.LogManager;
import Game.Map.Position;
import java.util.Random;
import static java.lang.Math.min;
/**
 * Represents a Warrior character, a melee fighter with defensive capabilities.
 * Extends PlayerCharacter and implements MeleeFighter and PhysicalAttacker.
 * Warriors can perform close-range attacks and mitigate damage using their defense stat.
 */
public class Warrior extends PlayerCharacter implements MeleeFighter, PhysicalAttacker{
    /**
     * Constructs a Warrior with a random defense value between 0 and 120.
     *
     * @param r    the row position on the map
     * @param c    the column position on the map
     * @param name the name of the player
     */
    public Warrior(int r,int c,String name){
        super(r,c,name);
        defence=new Random().nextInt(121);
    }
    /**
     * Checks equality based on name, defense, and inherited fields.
     */
    public boolean equals(Object obj){
        if (!(obj instanceof Warrior) || !(super.equals(obj))){
            return false;
        }
        Warrior other=(Warrior) obj;
        return this.getName().equals(other.getName()) && this.defence==other.getDefence();
    }
    /**
     * Performs a melee attack on the given target.
     * Can result in critical hit (double damage) with 10% probability.
     */
    public void fightClose(Combatant target){
        int damage=this.getPower();
        if (isCriticalHit()){
            damage*=2;
        }
        target.receiveDamage(damage,this);
    }
    /**
     * Overrides receiveDamage to apply defense-based mitigation.
     * Reduces incoming damage up to 60% depending on defense stat.
     */
    public void receiveDamage(int amount, Combatant source){
        int totaldamage;
        if (tryEvade(source)){
            LogManager.addLog("Entity evaded attack at: "+source.getPosition());
        }
        else{
            totaldamage=(int)(amount*(1-min(0.6,defence/200.0)));
            this.getDamage(totaldamage);
        }
    }
    /**
     * Returns null as Warriors do not have a magic element.
     */
    public MagicElement getElement() {
        return null;
    }
    /**
     * Returns 0 since Warriors do not use accuracy.
     */
    public double getAccuracy() {
        return 0;
    }
    /**
     * Checks if a target is in melee range (distance = 1).
     */
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target)==1;
    }
    /**
     * Attacks the target if within melee range.
     */
    public void attack(Combatant target){
        if (isInMeleeRange(this.getPosition(),target.getPosition())){
            fightClose(target);
        }
        else {
            System.out.println("Target out of range");
        }
    }
    /**
     * Returns the warrior’s defense value.
     */
    public int getDefence() {
        return defence;
    }
    /**
     * Returns the symbol used to represent the warrior on the map.
     */
    public String getDisplaySymbol() {
        return "figther";
    }
    /**
     * Returns a string representation of the Warrior, including defense value.
     *
     * @return a formatted string with Warrior details
     */
    public String toString() {
        return String.format("🛡️%s | Defense: %d",
                super.toString(),
                defence);
    }
    public Warrior clone() {
        Warrior copy = new Warrior(getPosition().getRow(), getPosition().getCol(), getName());
        copy.addTreasurePoint(getTreasurePoints());
        copyFieldsTo(copy);
        copy.getInventory().getItems().clear();
        for (var item : getInventory().getItems()) {
            copy.addToInventory((GameItem) item.clone());
        }
        return copy;
    }

    private int defence;
}
