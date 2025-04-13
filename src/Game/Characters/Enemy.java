package Game.Characters;

import Game.Combat.Combatant;
import Game.Combat.MagicElement;
import Game.Items.Treasure;
import java.util.Random;

public abstract class Enemy extends AbstractCharacter{
    private int loot;
    public Enemy(int r,int c){
        super(r,c);
        this.setHealth(50);
        this.loot= new Random().nextInt(201) + 100;
    }
    public boolean setHealth(int health) {
        if (health < 0) {
            return false;
        }
        if (health > 50) {
            return super.setHealth(0);
        }
        return super.setHealth(health);
    }
    public void Heal(int amount){
        if (this.getHealth()+amount>50){
            this.setHealth(50);
        }
        else{
            this.setHealth(this.getHealth()+amount);
        }
    }
    public void Defeat(){
        Treasure treasure=new Treasure(this.getPosition().getRow(),this.getPosition().getCol(),loot);
    }
}
