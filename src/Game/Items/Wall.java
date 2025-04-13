package Game.Items;

import Game.Characters.PlayerCharacter;

public class Wall extends GameItem{
    public Wall(int r,int c){
        super(r,c);
        this.setDescription("This is a wall,you cant go threw here! You must bypass this wall.");
    }
    public void interact(PlayerCharacter c) {

    }
    public String getDisplaySymbol() {
        return "W";
    }
}
