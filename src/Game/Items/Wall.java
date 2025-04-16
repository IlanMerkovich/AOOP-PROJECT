package Game.Items;

import Game.Characters.PlayerCharacter;

public class Wall extends GameItem{
    public Wall(int r,int c){
        super(r,c);
        this.setDescription("This is a wall,you cant go threw here! You must bypass this wall.");
    }
    public String toString() {
        return String.format("Wall | %s", super.toString());
    }
    public void interact(PlayerCharacter c){
        System.out.println("You hit a Wall at: "+this.getPosition());

    }
    public String getDisplaySymbol() {
        return "WL";
    }
}
