package Game.Characters;

import Game.Combat.Combatant;
import Game.Combat.MagicElement;
import Game.Combat.MeleeFighter;
import Game.Combat.PhysicalAttacker;
import Game.Map.Position;

import java.util.Random;

public class Goblin extends Enemy implements PhysicalAttacker, MeleeFighter{
    private int agility;
    public Goblin(int r,int c){
        super(r,c);
        this.agility=new Random().nextInt(81);
    }
    @Override
    public MagicElement getElement() {
        return null;
    }
    @Override
    public double getAccuracy() {
        return -1;
    }
    @Override
    public void fightClose(Combatant target) {

    }

    @Override
    public boolean isInMeleeRange(Position self, Position target) {

    }

    @Override
    public void attack(Combatant target) {

    }

    @Override
    public boolean isCriticalHit(){
        return new Random().nextDouble()<0.1;
    }
}
