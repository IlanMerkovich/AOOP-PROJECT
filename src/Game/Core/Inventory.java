package Game.Core;

import Game.Items.GameItem;

import java.util.ArrayList;
import java.util.List;

public class Inventory{
    public Inventory(){
        Items=new ArrayList<GameItem>();
    }
    public void addItem(GameItem Item){
        Items.add(Item);
    }
    public void removeItem(GameItem Item){
        Items.remove(Item);
    }
    public List<GameItem> getItems() {
        return Items;
    }
    private List<GameItem> Items;
}
