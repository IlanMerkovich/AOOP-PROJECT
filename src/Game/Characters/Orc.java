package Game.Characters;

import Game.Combat.Combatant;
import Game.Combat.MagicElement;
import Game.Combat.MeleeFighter;
import Game.Combat.PhysicalAttacker;
import Game.Map.Position;

import java.util.Random;

public class Orc extends Enemy implements PhysicalAttacker, MeleeFighter{
    public Orc(int r,int c){
        super(r,c);
        this.resistance=new Random().nextDouble(0.5);
    }
    public MagicElement getElement() {
        return null;
    }
    public double getAccuracy() {
        return -1;
    }
    public void fightClose(Combatant target){
        int totaldamage=getPower();
        if (this.isInMeleeRange(this.getPosition(),target.getPosition())){
            if (isCriticalHit()){
                totaldamage*=2;
            }
            target.receiveDamage(totaldamage,this);
        }
    }
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target)<=1;
    }
    public void attack(Combatant target){
        fightClose(target);
    }
    public boolean isCriticalHit() {
        return new Random().nextDouble()<0.1;
    }
    public void receiveDamage(int amount, Combatant source) {
        int damageReceived=amount;
        if (source.getElement()!=null){
            damageReceived*=(1-this.resistance);
        }
        this.setHealth(this.getHealth()-damageReceived);
    }


    public String getDisplaySymbol() {
        return "O";
    }
    private double resistance;
}
