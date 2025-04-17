package Game.Core;

import Game.Items.GameItem;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents an inventory for holding game items.
 * Supports adding, removing, listing, and retrieving items.
 */
public class Inventory{
    /**
     * Constructs an empty inventory.
     */
    public Inventory(){
        Items=new ArrayList<GameItem>();
    }
    /**
     * Returns a string representation of the inventory's contents.
     * If empty, returns a message indicating it's empty.
     * @return inventory contents as a string
     */
    public String toString() {
        if (Items.isEmpty()) {
            return "Inventory is empty.";
        }
        String result = "Inventory Contents:\n";
        int index = 1;
        for (GameItem item : Items) {
            result += index++ + ". " + item.toString() + "\n";
        }
        return result;
    }
    /**
     * Adds an item to the inventory.
     * @param Item the item to add
     */
    public void addItem(GameItem Item){
        Items.add(Item);
    }
    /**
     * Removes an item from the inventory.
     * @param Item the item to remove
     */
    public void removeItem(GameItem Item){
        Items.remove(Item);
    }
    /**
     * Returns the list of all items currently in the inventory.
     * @return list of game items
     */
    public List<GameItem> getItems() {
        return Items;
    }
    /**
     * Checks whether this inventory is equal to another object.
     * Two inventories are considered equal if they contain the same items in the same order.
     * @param obj the object to compare with
     * @return true if equal, false otherwise
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Inventory))
            return false;
        Inventory other = (Inventory) obj;
        return this.Items.equals(other.getItems());
    }

    private List<GameItem> Items;
}
