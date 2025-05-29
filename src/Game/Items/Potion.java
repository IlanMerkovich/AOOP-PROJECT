package Game.Items;
import Game.Characters.PlayerCharacter;
import java.util.Random;
/**
 * Represents a health potion that can be added to the player's inventory.
 * This potion does not apply effects directly upon interaction — only adds itself to inventory if unused.
 */
public class Potion extends GameItem implements Interactable,Pickupable{
    private int increaseAmount;
    private boolean isUsed;

    /**
     * Constructs a new Potion at the specified row and column.
     *
     * @param r the row index
     * @param c the column index
     */
    public Potion(int r,int c){
        super(r,c);
        this.increaseAmount=new Random().nextInt(41)+10;
        this.isUsed=false;
        this.setDescription("This is a health Potion.\n" +
                "By using this potion,you can heal yourself between 10 and 50 health points.\n" +
                "limited for 1 use only");
    }
    /**
     * Returns a display symbol for rendering the potion on the map.
     *
     * @return the string "PT"
     */
    public String getDisplaySymbol() {
        return "life_potion";
    }
    /**
     * Interacts with the player by using the potion and adding health to the player
     *
     * @param c the player character
     */
    public void interact(PlayerCharacter c) {
        c.Heal(this.getAmount());
        this.setUsed();
    }
    /**
     * cheacks if the obj compared to is equal to this object
     * @param obj the object to compare with
     * @return true is equals,false if else
     */
    public boolean equals(Object obj){
        if (!(obj instanceof Potion) || !super.equals(obj)){
            return false;
        }
        Potion other=(Potion) obj;
        return this.isUsed==other.isUsed() && this.increaseAmount==other.getAmount();
    }
    /**
     * sets the amount of the potion
     * @param amount the amount the potion adds
     */
    public void setAmount(int amount){
        this.increaseAmount=amount;
    }
    /**
     * returns the current amount
     * @return amount
     */
    public int getAmount(){
        return increaseAmount;
    }
    /**
     * sets that the potion is used
     */
    public void setUsed(){
        this.isUsed=true;
    }
    /**
     * Returns a string representation of the potion.
     *
     * @return formatted string with potion details
     */
    public String toString() {
        return String.format("🧪 Potion | %s | +%d HP | Used: %s",
                super.toString(), increaseAmount, isUsed);
    }
    /**
     * Picks up the potion from the game board and adds him to the player inventory
     * @param c player who is picking the potion
     */
    public void pickup(PlayerCharacter c) {
        c.addToInventory(this);
    }
    /**
     * @return if this item is used or not
     */
    public boolean isUsed(){
        return isUsed;
    }
}
