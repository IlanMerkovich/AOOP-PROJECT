package Game.Map;

import java.util.Objects;

import static java.lang.Math.abs;
/**
 * Represents a position on a 2D grid with a row and column.
 * Supports distance calculation, comparison, and printing.
 */
public class Position {
    /**
     * Creates a new position with given row and column.
     * @param r the row
     * @param c the column
     */
    public Position(int r,int c){
        this.row=r;
        this.col=c;
    }
    /**
     * Copy constructor.
     * Creates a new position with the same row and column as another.
     * @param position the position to copy
     */
    public Position(Position position){
        this.row=position.getRow();
        this.col=position.getCol();
    }
    /**
     * Returns a string representation of the position in "X:row,Y:col" format.
     * @return string of the position
     */
    public String toString() {
        return "X:"+row+",Y:"+col;
    }
    /**
     * Gets the row value of the position.
     * @return the row
     */
    public int getRow() {
        return row;
    }
    /**
     * Gets the column value of the position.
     * @return the column
     */
    public int getCol() {
        return col;
    }
    /**
     * Compares this position to another object.
     * Positions are equal if they have the same row and column.
     * @param obj the object to compare with
     * @return true if equal, false otherwise
     */
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof Position))
            return false;
        Position other = (Position) obj;
        return this.getRow() == other.getRow() && this.getCol() == other.getCol();
    }
    /**
     * Calculates the Manhattan distance to another position.
     * @param other the target position
     * @return the distance between this and the other position
     */
    public int distanceTo(Position other){
        return abs(this.row-other.getRow())+abs(this.col-other.getCol());
    }
    /**
     * Generates a hash code based on row and column values.
     * @return hash code for the position
     */
    public int hashCode() {
        return Objects.hash(getRow(), getCol());
    }

    private int row,col;
}
