package Game.Items;

import Game.Characters.PlayerCharacter;

public class Wall extends GameItem{
    public Wall(int r,int c){
        super(r,c);
    }
    public void interact(PlayerCharacter c) {

    }
    public String getDisplaySymbol() {
        return "W";
    }
    public void setVisible(boolean visible){
    }
}
