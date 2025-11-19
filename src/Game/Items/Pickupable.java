package Game.Items;
import Game.Characters.PlayerCharacter;
/**
 * Interface for items that can be picked up by the player.
 * Classes implementing this interface must define how the item
 * is added to the player's inventory.
 */
public interface Pickupable{
    /**
     * Adds the item to the player's inventory.
     * @param playerCharacter the player who picks up the item
     */
    public void pickup(PlayerCharacter playerCharacter);
}
