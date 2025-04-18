package Game.Characters;

import Game.Combat.Combatant;
import Game.Combat.MagicElement;
import Game.Items.Treasure;
import java.util.Random;
/**
 * Abstract class representing an enemy character in the game.
 * Each enemy has a fixed max health of 50 and contains loot that becomes treasure upon defeat.
 * Inherits general character properties from {@link AbstractCharacter}.
 */
public abstract class Enemy extends AbstractCharacter{
    private int loot;
    /**
     * Constructs an enemy at a given position with a randomized loot value between 100 and 300.
     *
     * @param r the row position
     * @param c the column position
     */
    public Enemy(int r,int c){
        super(r,c);
        this.setHealth(50);
        this.loot= new Random().nextInt(201) + 100;
    }
    /**
     * Sets the health of the enemy.
     * If the value is above 50, it's capped to 50.
     * If the value is below 0, it is set to 0 (dead).
     *
     * @param health the new health value
     * @return true if health was updated, false otherwise
     */
    public boolean setHealth(int health) {
        if (health < 0) {
            return super.setHealth(0);
        }
        if (health > 50) {
            return super.setHealth(50);
        }
        return super.setHealth(health);
    }
    /**
     * Heals the enemy by a specified amount, up to a maximum of 50.
     *
     * @param amount the amount to heal
     */
    public void Heal(int amount){
        if (this.getHealth()+amount>50){
            this.setHealth(50);
        }
        else{
            this.setHealth(this.getHealth()+amount);
        }
    }
    /**
     * Returns a string representation of the enemy including inherited data and loot amount.
     *
     * @return a formatted string
     */
    public String toString() {
        return String.format("👾 %s | Loot: %d",
                super.toString(),
                loot);
    }
    /**
     * Returns a treasure item representing the loot dropped by the enemy upon defeat.
     * The treasure appears at the same position the enemy occupied.
     *
     * @return a {@link Treasure} item with the enemy's loot value
     */
    public Treasure Defeat(){
        return new Treasure(this.getPosition().getRow(),this.getPosition().getCol(),loot);
    }
    /**
     * Checks if two Enemy objects are equal.
     * Equality is based on position, health, power, and loot value.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Enemy)) {
            return false;
        }
        Enemy other = (Enemy) obj;
        return this.getPosition().equals(other.getPosition()) &&
                this.getHealth() == other.getHealth() &&
                this.getPower() == other.getPower() &&
                this.loot == other.loot;
    }
}
