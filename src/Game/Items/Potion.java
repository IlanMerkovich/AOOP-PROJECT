package Game.Items;
import Game.Characters.PlayerCharacter;
import java.util.Random;

public class Potion extends GameItem implements Interactable{
    private int increaseAmount;
    private boolean isUsed;

    public Potion(int r,int c){
        super(r,c);
        this.increaseAmount=new Random().nextInt(41)+10;
        this.isUsed=false;
        this.setDescription("This is a health Potion.By using this potion,you can heal yourself between 10 and 50 health points.limited for 1 use only");
    }
    public String getDisplaySymbol() {
        return "PT";
    }
    public void interact(PlayerCharacter c){
        if (!this.CheckisUsed()){
            c.addToInventory(this);
            this.removeBlock();
        }
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Potion) || !super.equals(obj)){
            return false;
        }
        Potion other=(Potion) obj;
        return this.isUsed==other.CheckisUsed() && this.increaseAmount==other.getAmount();
    }
    public void setAmount(int amount){
        this.increaseAmount=amount;
    }
    public int getAmount(){
        return increaseAmount;
    }
    public boolean CheckisUsed(){
        return isUsed;
    }
    public void setUsed(){
        this.isUsed=true;
    }
    public String toString() {
        return String.format("🧪 Potion | %s | +%d HP | Used: %s",
                super.toString(), increaseAmount, isUsed);
    }


}
