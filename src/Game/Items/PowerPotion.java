package Game.Items;

import Game.Characters.PlayerCharacter;

import java.util.Random;

/**
 * Represents a power potion in the game.
 * When used, it increases the player's power by a small randomized amount (1–5).
 */
public class PowerPotion extends Potion{
    /**
     * Constructs a PowerPotion at the specified position.
     * Randomizes the amount to be added to power between 1 and 5.
     *
     * @param r the row position on the map
     * @param c the column position on the map
     */
    public PowerPotion(int r, int c) {
        super(r,c);
        this.setAmount(new Random().nextInt(4)+1);
        this.setDescription("This is a power Potion.By using this potion,you can add to yourself between 1 and 5 power points.limited for 1 use only");;
    }
    /**
     * Returns a formatted string representation of this power potion.
     *
     * @return string with icon and base description
     */
    public String toString() {
        return String.format("💥 PowerPotion | %s", super.toString());
    }
    /**
     * Returns the display symbol used on the map for this potion.
     *
     * @return the string "PP"
     */
    public String getDisplaySymbol() {
        return "power_potion";
    }
    /**
     * Checks whether another object is equal to this power potion.
     * Equality includes type and base potion properties.
     *
     * @param obj the object to compare
     * @return true if both are equal PowerPotion instances
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof PowerPotion)||!super.equals(obj)){
            return false;
        }
        return true;
    }
    /**
     * Interacts with the player by using the potion and adding power to the player
     *
     * @param c the player character
     */
    public void interact(PlayerCharacter c) {
        c.addPower(this.getAmount());
        this.setUsed();
    }
    /**
     * Picks up the potion from the game board and adds him to the player inventory
     * @param c player who is picking the potion
     */
    public void pickup(PlayerCharacter c) {
        c.addToInventory(this);
        System.out.println("You picked up a power potion!");
    }
}
