package Game.Map;

import java.util.Objects;

import static java.lang.Math.abs;

public class Position {
    public Position(int r,int c){
        this.row=r;
        this.col=c;
    }

    public Position(Position position){
        this.row=position.getRow();
        this.col=position.getCol();
    }

    public String toString() {
        return "X:"+row+",Y:"+col;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.getRow() == other.getRow() && this.getCol() == other.getCol();
    }
    public int distanceTo(Position other){
        return abs(this.row-other.getRow())+abs(this.col-other.getCol());
    }
    public int hashCode() {
        return Objects.hash(getRow(), getCol());
    }
    private int row,col;
}
