package Game.Items;

import Game.Characters.PlayerCharacter;

import java.util.Random;

public class Potion extends GameItem implements Interactable{
    private int increaseAmount;
    private boolean isUsed;
    public Potion(int r,int c,String despcription){
        super(r,c,despcription);
        this.increaseAmount=new Random().nextInt(41)+10;
        this.isUsed=false;
    }
    public String getDisplaySymbol() {
        return "P";
    }
    public void setVisible(boolean visible){

    }
    public void interact(PlayerCharacter c){
        if (!isUsed) {
            c.Heal(increaseAmount);
            isUsed = true;
        }
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Potion) || !super.equals(obj)){
            return false;
        }
        Potion other=(Potion) obj;
        return this.isUsed==other.CheckisUsed() && this.increaseAmount==other.getAmount();
    }
    protected void setAmount(int amount){
        this.increaseAmount=amount;
    }
    protected int getAmount(){
        return increaseAmount;
    }
    protected boolean CheckisUsed(){
        return isUsed;
    }
    protected void setUsed(){
        this.isUsed=true;
    }

}
