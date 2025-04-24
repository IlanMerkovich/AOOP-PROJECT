package Game.Characters;

import Game.Combat.Combatant;
import Game.Core.GameEntity;
import Game.Map.Position;
import java.util.Random;
/**
 * Abstract base class for all characters (players and enemies) in the game.
 * Implements both GameEntity and Combatant interfaces, and contains shared logic
 * for position, health, evasion, visibility, and basic combat behavior.
 */
public abstract class AbstractCharacter implements Combatant, GameEntity{
    /**
     * Constructs a character at the specified position with random power and default values.
     *
     * @param r the row position
     * @param c the column position
     */
    public AbstractCharacter(int r,int c){
        position=new Position(r,c);
        Health=100;
        Power=new Random().nextInt(4,14);
        evasionChance=0.25;
        visibility=false;

    }
    /**
     * Checks equality by comparing position, health, and power.
     *
     * @param obj the object to compare with
     * @return true if equal, false otherwise
     */
    public boolean equals(Object obj){
        if (!(obj instanceof AbstractCharacter)){
            return false;
        }
        AbstractCharacter other=(AbstractCharacter) obj;
        return this.getPosition().equals(other.getPosition()) && this.getHealth()==other.getHealth() && this.Power==other.getPower();
    }
    /**
     * Sets the visibility state of the character (for rendering).
     *
     * @param visible true to make visible, false to hide
     */
    public void setVisible(boolean visible) {
        this.visibility=visible;
    }
    /**
     * Heals the character by the specified amount up to a maximum of 100.
     *
     * @param amount the amount of health to restore
     */
    public void Heal(int amount){
        if (this.Health+amount>100){
            this.Health=100;
        }
        else{
            this.Health+=amount;
        }
    }
    /**
     * Increases the character's power up to a maximum of 14.
     *
     * @param amount the amount of power to add
     */
    public void addPower(int amount){
        if (this.Power+amount>14){
            this.Power=14;
        }
        else{
            this.Power+=amount;
        }
    }
    /**
     * Attempts to set the character's health.
     *
     * @param health the health value to set
     * @return true if successful, false if the value is out of bounds
     */
    public boolean setHealth(int health) {
        if (health>100 || health<0){
            return false;
        }
        this.Health=health;
        return true;
    }
    /**
     *@return players health
     */
    public int getHealth(){
        return this.Health;
    }
    /**
     * @return players power
     */
    public int getPower() {
        return Power;
    }
    /**
     * cheack if the character is dead
     * @return true if dead ,false if not
     */
    public boolean isDead() {
        if (Health<=0){
            return true;
        }
        return false;
    }
    /**
     * @return player position
     */
    public Position getPosition(){
        return position;
    }
    /**
     * sets player position
     * @param position the new position to set
     */
    public void setPosition(Position position) {
        this.position = position; //position is immutable
    }
    /**
     * Attempts to evade an incoming attack. If the attacker is an Archer,
     * the evasion chance is reduced by their accuracy.
     *
     * @param attacker the attacker
     * @return true if the character successfully evades, false otherwise
     */
    public boolean tryEvade(Combatant attacker){
        double accuracyFactor = 1.0;
        accuracyFactor = 1 - attacker.getAccuracy();
        double evadeChance = this.evasionChance * accuracyFactor;
        double roll = new Random().nextDouble();
        return roll < evadeChance;
    }
    /**
     * Handles incoming damage from a source. Attempts evasion before reducing health.
     *
     * @param amount the amount of damage
     * @param source the attacker
     */
    public void receiveDamage(int amount, Combatant source){
        if (tryEvade(source)){
            System.out.println("You have evaded the strike");
        } else {
            this.getDamage(amount);
        }
    }
    /**
     * Returns the character's evasion chance.
     *
     * @return the evasion chance (0.0–1.0)
     */
    protected double getEvasionChance(){
        return evasionChance;
    }
    /**
     * Returns a formatted string representing the character.
     *
     * @return string summary of the character's state
     */
    public String toString() {
        return String.format("%s | HP: %d | Power: %d | Evasion: %.2f | Position: (%d,%d)",
                getClass().getSimpleName(),
                getHealth(),
                getPower(),
                evasionChance,
                getPosition().getRow(),
                getPosition().getCol());
    }
    /**
     * Returns whether this character is currently visible.
     *
     * @return true if visible, false otherwise
     */
    public boolean getVisibility() {
        return visibility;
    }
    /**
     * sets player new health after getting damaged
     * @param amount of damage
     */
    protected void getDamage(int amount){
        this.Health-=amount;
    }

    private Position position;
    private int Health,Power;
    private double evasionChance;
    private boolean visibility;
}
