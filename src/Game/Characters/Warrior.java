package Game.Characters;

import Game.Combat.*;
import Game.Map.Position;
import java.util.Random;
import static java.lang.Math.min;

public class Warrior extends PlayerCharacter implements MeleeFighter, PhysicalAttacker{
    public Warrior(int r,int c,String name){
        super(r,c,name);
        defence=new Random().nextInt(121);
    }
    public boolean equals(Object obj){
        if (!(obj instanceof Warrior) || !(super.equals(obj))){
            return false;
        }
        Warrior other=(Warrior) obj;
        return this.getName().equals(other.getName()) && this.defence==other.getDefence();
    }
    public void fightClose(Combatant target){
        int damage=this.getPower();
        if (isCriticalHit()){
            damage*=2;
        }
        target.receiveDamage(damage,this);
    }
    public void receiveDamage(int amount, Combatant source){
        int totaldamage=0;
        if (tryEvade(source)){
            System.out.println("You have evaded the strike");
        }
        else{
            totaldamage=(int)(amount*(1-min(0.6,defence/200.0)));
            this.getDamage(totaldamage);
        }
    }
    public MagicElement getElement() {
        return null;
    }
    public double getAccuracy() {
        return 0;
    }
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target)==1;
    }
    public void attack(Combatant target){
        if (isInMeleeRange(this.getPosition(),target.getPosition())){
            fightClose(target);
        }
        else {
            System.out.println("Target out of range");
        }
    }
    public boolean isCriticalHit() {
        return new Random().nextDouble()<0.1;
    }
    public int getDefence() {
        return defence;
    }
    public String getDisplaySymbol() {
        return "WR";
    }

    private int defence;
}
