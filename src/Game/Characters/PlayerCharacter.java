package Game.Characters;
import Game.Combat.MagicElement;
import Game.Core.Inventory;
import Game.Items.GameItem;
import Game.Items.Potion;
import Game.Items.PowerPotion;

public abstract class PlayerCharacter extends AbstractCharacter{
    public PlayerCharacter(int r,int c,String name){
        super(r,c);
        this.name=name;
        this.treasurePoints=0;
        inventory=new Inventory();
    }
    public boolean equals(Object obj) {
        if (!super.equals(obj) || !(obj instanceof PlayerCharacter)){
            return false;
        }
        PlayerCharacter other=(PlayerCharacter) obj;
        return this.name.equals(other.getName()) && this.treasurePoints==other.getTreasurePoints();
    }
    public String getName(){
        return name;
    }
    public boolean addToInventory(GameItem item){
        if (item==null){
            return false;
        }
        this.inventory.addItem(item);
        return true;
    }
    public boolean usePotion(){
        for (GameItem item:inventory.getItems()){
            if (item instanceof Potion && !(item instanceof PowerPotion)){
                item.interact(this);
                inventory.removeItem(item);
                return true;
            }
        }
        return false;
    }
    public boolean userPowerPotion(){
        for (GameItem item:inventory.getItems()){
            if (item instanceof PowerPotion){
                item.interact(this);
                inventory.removeItem(item);
                return true;
            }
        }
        return false;
    }
    public boolean updateTreasurePoints(int amount){
        if (amount<0){
            return false;
        }
        this.treasurePoints+=amount;
        return true;
    }
    public int getTreasurePoints() {
        return treasurePoints;
    }
    public String getDisplaySymbol() {
        return "";
    }
    public void setVisible(boolean visible){
    }
    public abstract MagicElement getElement();
    private String name;
    private Inventory inventory;
    private int treasurePoints;


}
