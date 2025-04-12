package Game.Items;

import Game.Characters.PlayerCharacter;

import java.util.Random;

public class PowerPotion extends Potion{
    public PowerPotion(int r, int c) {
        super(r,c);
        this.setAmount(new Random().nextInt(4)+1);
        this.setDescription("This is a power Potion.By using this potion,you can add to yourself between 1 and 5 power points.limited for 1 use only");;
    }
    public void interact(PlayerCharacter c) {
        if (!this.CheckisUsed()){
            c.addPower(this.getAmount());
            this.setUsed();
            this.removeBlock();
        }
    }
    public String getDisplaySymbol() {
        return "PP";
    }
}
