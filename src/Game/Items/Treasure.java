package Game.Items;
import Game.Characters.PlayerCharacter;
import java.util.Random;
/**
 * Represents a treasure chest in the game.
 * When interacted with, the player has a chance to gain treasure points or a potion.
 */
public class Treasure extends GameItem implements Interactable{
    /**
     * Constructs a Treasure object at the specified position with a given value.
     *
     * @param r    the row index of the treasure's position
     * @param c    the column index of the treasure's position
     * @param loot the value of the treasure
     */
    public Treasure(int r, int c, int loot) {
        super(r, c);
        this.value = loot;
        this.collected = false;
        this.setDescription("This is a treasure chest. Using it gives: 1/3 chance for a potion, 1/2 for treasure points (100–300), 1/6 for a power potion. Can be used only once.");
    }
    /**
     * Performs an interaction with the player character.
     * Depending on a random roll, the player may receive a potion, power potion, or treasure points.
     *
     * @param c the player character interacting with the treasure
     */
    public void interact(PlayerCharacter c){
        if (collected){
            return;
        }
        int probability=1+new Random().nextInt(7);
        if (probability==1){
            c.addToInventory(new PowerPotion(c.getPosition().getRow(),c.getPosition().getCol()));
        }
        else if (probability>=2 && probability<=4){
            c.addTreasurePoint(this.value);
        }
        else{
            c.addToInventory(new Potion(c.getPosition().getRow(),c.getPosition().getCol()));
        }
        this.collected=true;
        this.removeBlock();
    }
    /**
     * Returns a string representation of the treasure.
     *
     * @return a string containing type, base details, value, and collection status
     */
    public String toString() {
        return String.format("💰 Treasure | %s | Value: %d | Collected: %s",
                super.toString(), value, collected);
    }
    /**
     * Returns the display symbol for this treasure on the map.
     *
     * @return the string \"TR\"
     */
    public String getDisplaySymbol() {
        return "TR";
    }
    /**
     * Checks if this treasure is equal to another object.
     * Two treasures are considered equal if they share the same position,
     * value, and collected status.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal treasures, false otherwise
     */
    public boolean equals(Object obj){
        if (!(obj instanceof Treasure))
            return false;
        Treasure other = (Treasure) obj;
        return super.equals(other) && this.value == other.value && this.collected == other.collected;
    }

    private int value;
    private boolean collected;
}
