package Game.Items;

import Game.Core.GameEntity;
import Game.Map.Position;

public abstract class GameItem implements GameEntity{
    public GameItem(int r, int c, String description){
        this.position=new Position(r,c);
        this.blocksMovement=true;
        this.description=description;
    }
    @Override
    public Position getPosition() {
        return position;
    }
    public String getDescription(){
        return description;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameItem){
            return position.equals(((GameItem) obj).getPosition())&&description.equals(((GameItem) obj).getDescription());
        }
        return false;
    }
    public void setPosition(Position newPos){
        this.position=newPos; //encapsulation is not broken because position is immutable
    }

    private Position position;
    private boolean blocksMovement;
    private String description;
}
