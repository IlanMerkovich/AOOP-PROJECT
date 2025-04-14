package Game.Combat;

import Game.Map.Position;

public interface Combatant{
    public int getHealth();
    public boolean setHealth(int health);
    public void receiveDamage(int amount,Combatant source);
    public boolean isDead();
    public int getPower();
    public boolean tryEvade(Combatant attacker);
    public Position getPosition();
    public MagicElement getElement();
    public double getAccuracy();
    void attack(Combatant target);

}
