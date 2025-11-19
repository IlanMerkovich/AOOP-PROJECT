package Game.Decorators;

import Game.Characters.PlayerCharacter;
import Game.Combat.*;
import Game.Core.Inventory;
import Game.Items.GameItem;
import Game.Map.Position;

public abstract class PlayerCharacterDecorator extends PlayerCharacter {
    private PlayerCharacter basePlayer;

    public PlayerCharacterDecorator(PlayerCharacter basePlayer) {
        this.basePlayer = basePlayer;
        super(basePlayer.getPosition().getRow(), basePlayer.getPosition().getCol(), basePlayer.getName());
    }
    protected PlayerCharacter getBasePlayer() {
        return basePlayer;
    }
    public int getHealth() { return basePlayer.getHealth(); }
    public boolean setHealth(int health) { return basePlayer.setHealth(health); }
    public int getPower() { return basePlayer.getPower(); }
    public boolean isDead() { return basePlayer.isDead(); }
    public Position getPosition() { return basePlayer.getPosition(); }
    public MagicElement getElement() { return basePlayer.getElement(); }
    public double getAccuracy() { return basePlayer.getAccuracy(); }
    public void setPosition(Position position) { basePlayer.setPosition(position); }
    public boolean tryEvade(Combatant attacker) { return basePlayer.tryEvade(attacker); }
    public int getTreasurePoints() { return basePlayer.getTreasurePoints(); }
    public void addTreasurePoint(int amount) { basePlayer.addTreasurePoint(amount); }
    public Inventory getInventory() { return basePlayer.getInventory(); }
    public boolean addToInventory(GameItem item) { return basePlayer.addToInventory(item); }
    public boolean usePotion() { return basePlayer.usePotion(); }
    public boolean usePowerPotion() { return basePlayer.usePowerPotion(); }
    public String getName() { return basePlayer.getName(); }
    public boolean getVisibility() { return basePlayer.getVisibility(); }
    @Override
    public String getDisplaySymbol() {
        return basePlayer.getDisplaySymbol();
    }
    @Override
    public String getType() {
        return basePlayer.getType();
    }
    public void attack(Combatant target){
        basePlayer.attack(target);
    }
    public void receiveDamage(int amount, Combatant source) {
        basePlayer.receiveDamage(amount,source);
    }
    public PlayerCharacter clone(){
       return basePlayer.clone();
    }
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof PlayerCharacterDecorator other) {
            return this.unwrap().equals(other.unwrap());
        }
        return unwrap().equals(obj);
    }
    public int hashCode() {
        return unwrap().hashCode();
    }
    public PlayerCharacter unwrap() {
        if (basePlayer instanceof PlayerCharacterDecorator decorator) {
            return decorator.unwrap();
        }
        return basePlayer;
    }
}
