package Game.Characters;
import Game.Combat.MagicElement;
import Game.Core.GameEntity;
import Game.Core.Inventory;
import Game.Items.GameItem;
import Game.Items.Potion;
import Game.Items.PowerPotion;

import java.util.Iterator;

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
        Iterator<GameItem> it = inventory.getItems().iterator();
        while (it.hasNext()) {
            GameItem item = it.next();
            if (item instanceof Potion potion && !(potion instanceof PowerPotion)) {
                if (!potion.CheckisUsed()) {
                    this.Heal(potion.getAmount());
                    potion.setUsed();
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }
    public boolean usePowerPotion(){
        Iterator<GameItem> it = inventory.getItems().iterator();
        while (it.hasNext()){
            GameItem item = it.next();
            if (item instanceof PowerPotion potion){
                if (!potion.CheckisUsed()){
                    this.addPower(potion.getAmount());
                    potion.setUsed();
                    it.remove();
                    return true;
                }
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
    public void addTreasurePoint(int amount){
        this.treasurePoints+=amount;
    }
    public String getDisplaySymbol() {
        return "";
    }
    public abstract MagicElement getElement();
    public String toString() {
        return String.format("🧑‍💼 %s | Name: %s | Treasure: %d",
                super.toString(),
                getName(),
                getTreasurePoints());
    }
    private String name;
    private Inventory inventory;
    private int treasurePoints;
}
