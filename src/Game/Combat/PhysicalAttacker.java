package Game.Combat;

public interface PhysicalAttacker{
    public void attack(Combatant target);
    public boolean isCriticalHit();//10% for double damage//
}
