package Game.Characters;

import Game.Combat.Combatant;
import Game.Combat.MagicElement;

import java.util.Random;

public abstract class Enemy extends AbstractCharacter{
    private int loot;
    public Enemy(int r,int c){
        super(r,c);
        this.setHealth(50);
        this.loot= new Random().nextInt(201) + 100;
    }
    @Override
    public boolean setHealth(int health) {
        if (health>100 || health<0){
            return false;
        }
        this.setHealth(health);
        return true;
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
        Treasure treasure=new Treasure(getPosition(),loot);
    }
    @Override
    public String getDisplaySymbol() {
        return "";
    }
    @Override
    public void setVisible(boolean visible) {
    }
}
