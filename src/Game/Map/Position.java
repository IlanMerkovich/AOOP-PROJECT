package Game.Map;

import static java.lang.Math.abs;

public class Position {
    public Position(int r,int c){
        this.row=r;
        this.col=c;
    }
    @Override
    public String toString() {
        return "X:"+row+",Y:"+col;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position){
            return this.row==((Position) obj).getRow()&&this.col==((Position) obj).getCol();
        }
        return false;
    }
    public int distanceTo(Position other){
        return abs(this.row-other.getRow())+abs(this.col-other.getCol());
    }
    private int row,col;
}
