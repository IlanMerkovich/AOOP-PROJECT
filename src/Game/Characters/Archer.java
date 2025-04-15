package Game.Characters;

import Game.Combat.*;
import Game.Map.Position;

import java.util.Random;

public class Archer extends PlayerCharacter implements RangedFighter, PhysicalAttacker{

    public Archer(int r,int c,String name){
        super(r,c,name);
        accuracy=new Random().nextDouble()*0.8;
    }
    public boolean equals(Object obj) {
        if (!(obj instanceof Archer) || !super.equals(obj)) return false;
        Archer other = (Archer) obj;
        return this.accuracy==other.getAccuracy();
    }
    public double getAccuracy() {
        return accuracy;
    }
    public void attack(Combatant target){
        fightRanged(target);
    }
    public boolean isCriticalHit(){
        return new Random().nextDouble()<0.1;
    }
    public void fightRanged(Combatant target){
        if (this.isInRange(this.getPosition(),target.getPosition())){
            int damage=this.getPower();
            if (isCriticalHit()){
                damage*=2;
            }
            target.receiveDamage(damage,this);
        }
        else{
            System.out.println("Target out of range");
        }
    }
    public int getRange() {
        return 2;
    }
    public String getDisplaySymbol() {
        return "AR";
    }
    public MagicElement getElement() {
        return null;
    }
    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target)<=getRange();
    }
    public String toString() {
        return String.format("🏹 %s | Accuracy: %.2f",
                super.toString(),
                getAccuracy());
    }

    private double accuracy;

}
