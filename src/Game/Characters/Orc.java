package Game.Characters;

import Game.Combat.*;
import Game.Logs.LogManager;
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
        return 0;
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

    @Override
    public String getType() {
        return "melee";
    }

    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target)<=1;
    }

    public void attack(Combatant target){
        fightClose(target);
    }

    public void receiveDamage(int amount, Combatant source) {
        int damageReceived=amount;
        if (tryEvade(source)){
            LogManager.addLog("Entity evaded attack at: "+source.getPosition());
            return;
        }
        if (source.getElement()!=null){
            damageReceived*=(1-this.resistance);
        }
        this.getDamage(damageReceived);
    }

    public String toString() {
        return String.format("👹 %s | Resistance: %.2f",
                super.toString(),
                resistance);
    }

    public String getDisplaySymbol() {
        return "orc";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Orc other)) return false;
        return super.equals(obj) && Double.compare(this.resistance, other.resistance) == 0;
    }

    @Override
    public Orc clone() {
        Orc copy = new Orc(getPosition().getRow(), getPosition().getCol());
        copyEnemyFieldsTo(copy);
        copy.resistance = this.resistance;
        return copy;
    }

    private double resistance;
}