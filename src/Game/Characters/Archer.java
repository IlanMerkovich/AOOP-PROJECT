package Game.Characters;

import Game.Combat.Combatant;
import Game.Combat.MagicElement;
import Game.Combat.PhysicalAttacker;
import Game.Combat.RangedFighter;
import Game.Map.Position;

import java.util.Random;

public class Archer extends PlayerCharacter implements RangedFighter, PhysicalAttacker{

    public Archer(int r,int c,String name){
        super(r,c,name);
        accuracy=new Random().nextDouble(0.8);
    }
    public boolean equals(Object obj) {
        if (!(obj instanceof Archer) || !super.equals(obj)) return false;
        Archer other = (Archer) obj;
        return this.accuracy==other.getAccuracy();
    }
    public MagicElement getElement() {
        return null;
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
    }
    public int getRange() {
        return 2;
    }
    public String getDisplaySymbol() {
        return "A";
    }
    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target)<=getRange();
    }
    private double accuracy;

}
