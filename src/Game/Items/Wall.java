package Game.Items;

import Game.Characters.PlayerCharacter;

/**
 * Represents a wall object in the game.
 * A wall blocks movement.
 */
public class Wall extends GameItem{
    /**
     * Constructs a Wall object at the specified row and column.
     *
     * @param r the row index of the wall's position
     * @param c the column index of the wall's position
     */
    public Wall(int r,int c){
        super(r,c);
        this.setDescription("This is a wall,you cant go threw here! You must bypass this wall.");
    }
    /**
     * Returns a string representation of the wall.
     *
     * @return a string representing the wall's type and base details
     */
    public String toString() {

        return String.format("Wall | %s", super.toString());
    }
    /**
     * Called when a player interacts with the wall.
     * Walls do not allow movement and only display a message.
     *
     * @param c the player character interacting with the wall
     */
    public void interact(PlayerCharacter c){
        System.out.println("You hit a Wall at: "+this.getPosition());
    }
    /**
     * Returns the display symbol for this wall.
     *
     * @return the string "WL" to represent a wall on the map
     */
    public String getDisplaySymbol() {
        return "wall";
    }
    /**
     * Checks if this wall is equal to another object.
     * Two walls are considered equal if they share the same position
     * @param obj the object to compare with
     * @return true if the objects are equal walls, false otherwise
     */
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Wall))
            return false;
        return super.equals(obj);
    }
    public Wall clone() {
        Wall copy = new Wall(getPosition().getRow(), getPosition().getCol());
        copyFieldsTo(copy);
        return copy;
    }
}
