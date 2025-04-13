package Game.Items;

import Game.Characters.PlayerCharacter;

import java.util.Random;

public class Treasure extends GameItem implements Interactable{
    public Treasure(int r, int c, int loot) {
        super(r, c);
        this.value = loot;
        this.collected = false;
        this.setDescription("This is a treasure chest. Using it gives: 1/3 chance for a potion, 1/2 for treasure points (100–300), 1/6 for a power potion. Can be used only once.");
    }
    public Treasure(int r,int c){
        super(r,c);
        this.value=new Random().nextInt(201)+100;
        this.collected = false;
        this.setDescription("This is a treasure chest. Using it gives: 1/3 chance for a potion, 1/2 for treasure points (100–300), 1/6 for a power potion. Can be used only once.");

    }
    public void interact(PlayerCharacter c){
        if (collected){
            return;
        }
        int probability=1+new Random().nextInt(7);
        if (probability==1){
            c.addToInventory(new PowerPotion(c.getPosition().getRow(),c.getPosition().getCol()));
        }
        else if (probability>=2 && probability<=4){
            c.addTreasurePoint(this.value);
        }
        else{
            c.addToInventory(new Potion(c.getPosition().getRow(),c.getPosition().getCol()));
        }
        this.collected=true;
        this.removeBlock();
    }
    public String getDisplaySymbol() {
        return "T";
    }

    private int value;
    private boolean collected;
}
