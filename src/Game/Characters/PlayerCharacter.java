package Game.Characters;
import Game.Combat.MagicElement;
import Game.Core.Inventory;
import Game.Items.GameItem;
import Game.Items.Potion;
import Game.Items.PowerPotion;

/**
 * Represents a player-controlled character in the game.
 * Stores the player's name, inventory, and treasure points.
 * Handles interactions with potions and inventory management.
 */
public abstract class PlayerCharacter extends AbstractCharacter{
    /**
     * Constructs a new player character with default stats and an empty inventory.
     *
     * @param r    the row position
     * @param c    the column position
     * @param name the character's name
     */
    public PlayerCharacter(int r,int c,String name){
        super(r,c);
        this.name=name;
        this.treasurePoints=0;
        inventory=new Inventory();
    }
    /**
     * Checks if this player is equal to another object.
     * Equality is based on name, treasure points, and inherited properties.
     */
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof PlayerCharacter)){
            return false;
        }
        PlayerCharacter other=(PlayerCharacter) obj;
        return this.name.equals(other.getName()) && this.treasurePoints==other.getTreasurePoints();
    }
    /**
     * Returns the name of the player.
     */
    public String getName(){
        return name;
    }
    /**
     * Adds an item to the player's inventory.
     *
     * @param item the item to add
     * @return true if added successfully, false if null
     */
    public boolean addToInventory(GameItem item){
        if (item==null){
            return false;
        }
        this.inventory.addItem(item);
        return true;
    }
    /**
     * Uses a regular health potion from the inventory if available.
     *
     * @return true if a potion was used, false otherwise
     */
    public boolean usePotion(){
        for (GameItem item:inventory.getItems()){
            if (item instanceof Potion && !(item instanceof PowerPotion) && !item.isUsed()){
                item.interact(this);
                inventory.removeItem(item);
                return true;
            }
        }
        return false;
    }
    /**
     * Uses a power potion from the inventory if available.
     *
     * @return true if a power potion was used, false otherwise
     */
    public boolean usePowerPotion(){
        for (GameItem item:inventory.getItems()){
            if (item instanceof PowerPotion){
                if (!item.isUsed()){
                    item.interact(this);
                    inventory.removeItem(item);
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Returns the current number of treasure points.
     */
    public int getTreasurePoints() {
        return treasurePoints;
    }
    /**
     * Adds a specified amount to the player's treasure points.
     */
    public void addTreasurePoint(int amount){
        this.treasurePoints+=amount;
    }
    /**
     * Returns the player's magic element (if applicable).
     */
    public abstract MagicElement getElement();
    /**
     * Returns a string representation of the player character, including inventory.
     */
    public String toString() {
        return String.format(
                "🧑‍💼 %s | Name: %s | Treasure: %d\n%s",
                super.toString(),
                getName(),
                getTreasurePoints(),
                inventory.toString()
        );
    }
    /**
     * Returns the player's inventory.
     */
    public Inventory getInventory(){
        return inventory;
    }

    private final String name;
    private Inventory inventory;
    private int treasurePoints;
}
