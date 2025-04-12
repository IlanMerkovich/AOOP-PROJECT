package Game.Combat;

public interface MagicAttacker{
    public void calculateMagicDamage(Combatant target);
    public void castSpell(Combatant target);
    public MagicElement getElement();
    public boolean isElementStrongerThan(MagicAttacker other);
}
