package Game.Core;

import Game.Map.Position;
/**
 * Represents any entity that exists on the game map and has a position and visibility.
 * This interface is implemented by characters, enemies, items, etc.
 */
public interface GameEntity extends Cloneable{
    /**
     * Returns the current position of the entity on the map.
     * @return the entity's position
     */
    public Position getPosition();
    /**
     * Updates the entity's position on the map.
     * @param newPos the new position to set
     */
    public void setPosition(Position newPos);
    /**
     * Returns a string (usually a short symbol) representing the entity on the map.
     * Used for display purposes.
     * @return the display symbol of the entity
     */
    public String getDisplaySymbol();
    /**
     * Sets whether the entity should be visible on the map.
     * @param visible true to make visible, false to hide
     */
    public void setVisible(boolean visible);
    /**
     * Returns whether the entity is currently visible on the map.
     * @return true if visible, false otherwise
     */
    public boolean getVisibility();

    public GameEntity clone();
}
