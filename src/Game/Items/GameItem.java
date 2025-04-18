package Game.Items;

import Game.Characters.PlayerCharacter;
import Game.Core.GameEntity;
import Game.Map.Position;
/** abstract item class representing an item in the game
 * implements game entity interface
 * has position,description,visibility and if blocks fields*/

public abstract class GameItem implements GameEntity{
    /**
     * constructs a game item object
     * @param r - the row position
     * @param c - thos col position
     */
    public GameItem(int r, int c){
        this.position=new Position(r,c);
        this.blocksMovement=true;
        this.description="This is a game item.";
    }
    /**
     * defines how items interacts with other player
     * @param c - the player character interacting with the item
     */
    public abstract void interact(PlayerCharacter c);
    /**
     * returns the current items position
     * @return the items position
     */
    public Position getPosition() {
        return position;
    }
    /**
     * returns the items description
     * @return the items description
     */
    public String getDescription(){
        return description;
    }
    /**
     * Sets a new description for the item.
     *
     * @param description the description to set
     */
    protected void setDescription(String description){
        this.description=description;
    }
    /**
     * Checks equality based on position and description.
     *
     * @param obj the object to compare with
     * @return true if both items are equal; false otherwise
     */
    public boolean equals(Object obj) {
        if (obj instanceof GameItem){
            return position.equals(((GameItem) obj).getPosition())&&description.equals(((GameItem) obj).getDescription());
        }
        return false;
    }
    /**
     * Updates the item's position.
     *
     * @param newPos the new position to set
     */
    public void setPosition(Position newPos){
        this.position=newPos; //encapsulation is not broken because position is immutable
    }
    /**
     * Disables the item's ability to block movement (e.g. after interaction).
     */
    protected void removeBlock(){
        this.blocksMovement=false;
    }
    /**
     * Returns whether this item blocks movement.
     *
     * @return true if it blocks movement; false otherwise
     */
    public boolean isBlocksMovement(){
        return blocksMovement;
    }
    /**
     * Sets the visibility of the item (used for map display).
     *
     * @param visible true to make it visible; false to hide
     */
    public void setVisible(boolean visible){
        this.visibility=visible;

    }
    /**
     * Returns the current visibility state.
     *
     * @return true if visible; false if hidden
     */
    public boolean getVisibility() {
        return visibility;
    }
    /**
     * Returns a detailed string representation of the item.
     *
     * @return formatted string of item properties
     */
    public String toString() {
        return String.format("📦 GameItem | Desc: %s | Blocks: %s",
                description, blocksMovement);
    }


    private Position position;
    private boolean blocksMovement;
    private String description;
    private boolean visibility;
}
