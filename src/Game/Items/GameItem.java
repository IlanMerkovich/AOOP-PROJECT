package Game.Items;

import Game.Characters.PlayerCharacter;
import Game.Core.GameEntity;
import Game.Map.Position;

public abstract class GameItem implements GameEntity{
    public GameItem(int r, int c){
        this.position=new Position(r,c);
        this.blocksMovement=true;
        this.description="This is a game item.";
    }
    public abstract void interact(PlayerCharacter c);
    public Position getPosition() {
        return position;
    }
    public String getDescription(){
        return description;
    }
    protected void setDescription(String description){
        this.description=description;
    }
    public boolean equals(Object obj) {
        if (obj instanceof GameItem){
            return position.equals(((GameItem) obj).getPosition())&&description.equals(((GameItem) obj).getDescription());
        }
        return false;
    }
    public void setPosition(Position newPos){
        this.position=newPos; //encapsulation is not broken because position is immutable
    }
    protected void removeBlock(){
        this.blocksMovement=false;
    }
    private Position position;
    private boolean blocksMovement;
    private String description;
}
