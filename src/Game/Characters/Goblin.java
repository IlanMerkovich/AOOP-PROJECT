package Game.Characters;
import Game.Combat.*;
import Game.Map.Position;
import java.util.Random;

public class Goblin extends Enemy implements PhysicalAttacker, MeleeFighter{
    private int agility;

    public Goblin(int r,int c){
        super(r,c);
        this.agility=new Random().nextInt(81);
    }

    public boolean tryEvade(Combatant attacker) {
        double baseEvasion = Math.min(0.8, agility / 100.0);
        double accuracyFactor;
        accuracyFactor = 1.0 - attacker.getAccuracy();
        double finalEvasion = baseEvasion * accuracyFactor;
        double roll = new Random().nextDouble();
        return roll < finalEvasion;
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

    public String getDisplaySymbol() {
        return "goblin";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Goblin) || !super.equals(obj)) {
            return false;
        }
        Goblin other = (Goblin) obj;
        return this.agility == other.agility;
    }

    @Override
    public String getType() {
        return "melee";
    }

    public Goblin clone() {
        Goblin copy = new Goblin(getPosition().getRow(), getPosition().getCol());
        copyEnemyFieldsTo(copy);
        copy.agility = this.agility;
        return copy;
    }
}