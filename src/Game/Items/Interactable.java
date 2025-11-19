package Game.Items;

import Game.Characters.PlayerCharacter;
/**
 * Represents any item or entity in the game that can be interacted with by the player.
 * Implementing classes must define behavior when a player character interacts with them.
 */
public interface Interactable {
    /**
     * Defines how the object interacts with the given player character.
     *
     * @param c the player character performing the interaction
     */
    public void interact(PlayerCharacter c);
}
