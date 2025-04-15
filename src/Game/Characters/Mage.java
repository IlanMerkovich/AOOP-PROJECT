package Game.Characters;

import Game.Combat.*;
import Game.Map.Position;
import java.util.Random;

public class Mage extends PlayerCharacter implements MagicAttacker, RangedFighter{
    private final MagicElement element;
    public Mage(int r,int c,String name){
        super(r,c,name);
        this.element=MagicElement.getRandomElement();
    }
    public boolean equals(Object obj) {
        if (!(obj instanceof Mage) || !super.equals(obj))
            return false;
        Mage other = (Mage) obj;
        return this.element == other.getElement();
    }
    public void calculateMagicDamage(Combatant target) {
        double totaldamage = this.getPower() * 1.5;
        MagicElement targetElement = target.getElement();
        if (targetElement != null) {
            if (this.element.isStrongerThan(targetElement)) {
                totaldamage *= 1.2;
            } else if (targetElement.isStrongerThan(this.element)) {
                totaldamage *= 0.8;
            }
        }
        target.receiveDamage((int) totaldamage, this);
    }
    public void castSpell(Combatant target) {
        calculateMagicDamage(target);
    }
    public MagicElement getElement(){
        return element;
    }
    public double getAccuracy() {
        return 0;
    }
    public boolean isElementStrongerThan(MagicAttacker other){
        return element.isStrongerThan(other.getElement());
    }
    public void fightRanged(Combatant target){
        if (this.isInRange(this.getPosition(),target.getPosition())){
            calculateMagicDamage(target);
        }
    }
    public int getRange(){
        return 2;
    }
    public boolean isInRange(Position self, Position target){
        return self.distanceTo(target)<=getRange();
    }
    public String getDisplaySymbol() {
        return "MA";
    }
    public void attack(Combatant target) {
        castSpell(target);
    }
}
