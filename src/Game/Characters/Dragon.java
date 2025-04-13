package Game.Characters;

import Game.Combat.*;
import Game.Map.Position;

import java.util.Random;

public class Dragon extends Enemy implements PhysicalAttacker,MeleeFighter, MagicAttacker, RangedFighter{
    private final MagicElement element;
    public Dragon(int r,int c){
        super(r,c);
        this.element=MagicElement.getRandomElement();
    }
    public void calculateMagicDamage(Combatant target){
        int damage= (int) (this.getPower()*1.5);
        MagicElement otherelement=target.getElement();
        if (otherelement!=null){
            if (this.element.isStrongerThan(otherelement)){
                damage*=1.2;
            }
            else if (otherelement.isStrongerThan(this.element)){
                damage*=0.8;
            }
        }
        target.receiveDamage(damage,this);
    }
    public void castSpell(Combatant target) {
        calculateMagicDamage(target);
    }
    public MagicElement getElement() {
        return this.element;
    }
    public boolean isElementStrongerThan(MagicAttacker other) {
        if (other.getElement()!=null){
            return this.element.isStrongerThan(other.getElement());
        }
        else{
            return false;
        }
    }
    public double getAccuracy() {
        return 0;
    }
    public void fightClose(Combatant target) {
        int damage = getPower();
        if (isCriticalHit()) {
            damage *= 2;
        }
        target.receiveDamage(damage, this);
    }
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target) == 1;
    }
    public void fightRanged(Combatant target) {
        if (isInRange(getPosition(),target.getPosition())) {
            castSpell(target);
        }
    }
    public int getRange() {
        return 2;
    }
    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target) <= getRange();
    }
    public void attack(Combatant target){
        if (isInMeleeRange(getPosition(), target.getPosition())) {
            fightClose(target);
        }
        else {
            fightRanged(target);
        }
    }
    public boolean isCriticalHit() {
        return new Random().nextDouble() < 0.1;
    }
    public String getDisplaySymbol() {
        return "D";
    }
}
