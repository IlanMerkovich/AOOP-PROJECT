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
    @Override
    public MagicElement getElement() {
        return null;
    }
    @Override
    public double getAccuracy() {
        return accuracy;
    }

    @Override
    public void attack(Combatant target){
        fightRanged(target);
    }
    @Override
    public boolean isCriticalHit(){
        return new Random().nextDouble()<0.1;
    }
    @Override
    public void fightRanged(Combatant target){
        if (this.isInRange(this.getPosition(),target.getPosition())){
            int damage=this.getPower();
            if (isCriticalHit()){
                damage*=2;
            }
            target.receiveDamage(damage,this);
        }
    }
    @Override
    public int getRange() {
        return 2;
    }
    @Override
    public boolean isInRange(Position self, Position target) {
        if (self.distanceTo(target)<=getRange()){
            return true;
        }
        return false;
    }

    private double accuracy;

}
