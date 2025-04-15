package Game.Characters;

import Game.Combat.*;
import Game.Map.Position;
import java.util.Random;
import static java.lang.Math.min;

public class Goblin extends Enemy implements PhysicalAttacker, MeleeFighter{
    private int agility;
    public Goblin(int r,int c){
        super(r,c);
        this.agility=new Random().nextInt(81);
    }
    public boolean tryEvade(){
        double GoblinEvasion=min(0.8,this.agility/100.0);
        return new Random().nextDouble()<GoblinEvasion;
    }
    public String toString() {
        return String.format("👺 %s | Agility: %d", super.toString(), agility);
    }

    public MagicElement getElement() {
        return null;
    }
    public double getAccuracy() {
        return 0;
    }
    public void fightClose(Combatant target){
        int totaldamage=this.getPower();
        if (isInMeleeRange(this.getPosition(),target.getPosition())){
            if (this.isCriticalHit()){
                totaldamage*=2;
            }
            target.receiveDamage(totaldamage,this);
        }
    }
    public boolean isInMeleeRange(Position self, Position target){
        return self.distanceTo(target)==1;
    }
    public void attack(Combatant target){
        fightClose(target);
    }
    public boolean isCriticalHit(){
        return new Random().nextDouble()<0.1;
    }
    public String getDisplaySymbol() {
        return "GO";
    }
}
