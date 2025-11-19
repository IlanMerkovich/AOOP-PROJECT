package Game.Characters;
import Game.Combat.MagicElement;
import Game.Core.Inventory;
import Game.Items.GameItem;
import Game.Items.Potion;
import Game.Items.PowerPotion;
import Game.Logs.LogManager;

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
            if (item instanceof Potion && !(item instanceof PowerPotion) && !item.isUsed()){
                item.interact(this);
                inventory.removeItem(item);
                LogManager.addLog("Player used a health potion");
                return true;
            }
        }
        return false;
    }

    public boolean usePowerPotion(){
        for (GameItem item:inventory.getItems()){
            if (item instanceof PowerPotion){
                if (!item.isUsed()){
                    item.interact(this);
                    inventory.removeItem(item);
                    LogManager.addLog("Player used a power potion");
                    return true;
                }
            }
        }
        return false;
    }

    public int getTreasurePoints() {
        return treasurePoints;
    }

    public void addTreasurePoint(int amount){
        this.treasurePoints+=amount;
    }

    public abstract MagicElement getElement();

    public String toString() {
        return String.format(
                "🧑‍💼 %s | Name: %s | Treasure: %d\n%s",
                super.toString(),
                getName(),
                getTreasurePoints(),
                inventory.toString()
        );
    }

    public Inventory getInventory(){
        return inventory;
    }

    protected PlayerCharacter(PlayerCharacter source) {
        super(source.getPosition().getRow(), source.getPosition().getCol());
        this.name = source.name;
        this.inventory = source.inventory.clone();
        this.treasurePoints = source.treasurePoints;
        this.copyFieldsTo(this);
    }
    public abstract String getType();


    public abstract PlayerCharacter clone();
    private String name;
    private Inventory inventory;
    private int treasurePoints;
}