package Game.Items;

import Game.Characters.PlayerCharacter;

import java.util.Random;

public class PowerPotion extends Potion{
    public PowerPotion(int r, int c, String despcription) {
        super(r, c, despcription);
        this.setAmount(new Random().nextInt(4)+1);
    }
    public void interact(PlayerCharacter c) {
        if (!this.CheckisUsed()){
            c.addPower(this.getAmount());
            this.setUsed();
        }
    }
    public String getDisplaySymbol() {
        return "PP";
    }
}
