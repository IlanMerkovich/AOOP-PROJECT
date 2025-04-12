package Game.Characters;

import Game.Combat.Combatant;
import Game.Combat.MagicAttacker;
import Game.Combat.MagicElement;
import Game.Combat.RangedFighter;
import Game.Map.Position;
import java.util.Random;

public class Mage extends PlayerCharacter implements MagicAttacker, RangedFighter{
    private MagicElement element;

    public Mage(int r,int c,String name){
        super(r,c,name);
        this.element=MagicElement.getRandomElement();
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Mage) || !super.equals(obj))
            return false;
        Mage other = (Mage) obj;
        return this.element == other.getElement();
    }
    @Override
    public void calculateMagicDamage(Combatant target){
        int totaldamage= (int) (this.getPower()*1.5);
        if (target.getElement()!=null){
            if (this.element.isStrongerThan(target.getElement())){
                totaldamage*=1.2;
            }
            else{
                totaldamage*=0.8;
            }
        }
        target.receiveDamage(totaldamage,this);
    }
    @Override
    public void castSpell(Combatant target){
        calculateMagicDamage(target);
    }
    @Override
    public MagicElement getElement(){
        return element;
    }
    @Override
    public double getAccuracy() {
        return -1;
    }
    @Override
    public boolean isElementStrongerThan(MagicAttacker other){
        return element.isStrongerThan(other.getElement());
    }
    @Override
    public void fightRanged(Combatant target){
        if (this.isInRange(this.getPosition(),target.getPosition())){
            calculateMagicDamage(target);
        }
    }
    @Override
    public int getRange(){
        return 2;
    }
    @Override
    public boolean isInRange(Position self, Position target){
        return self.distanceTo(target)<=getRange();
    }
}
