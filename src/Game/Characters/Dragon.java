package Game.Characters;

import Game.Combat.*;
import Game.Map.Position;

public class Dragon extends Enemy implements PhysicalAttacker,MeleeFighter, MagicAttacker, RangedFighter{
    public Dragon(int r,int c){
        super(r,c);
        this.element=MagicElement.getRandomElement();
    }

    public Dragon(int r, int c, MagicElement element) {
        super(r, c);
        this.element = element;
    }

    public void calculateMagicDamage(Combatant target){
        double damage= (this.getPower()*1.5);
        MagicElement otherelement=target.getElement();
        if (otherelement!=null){
            if (this.element.isStrongerThan(otherelement)){
                damage*=1.2;
            }
            else if (otherelement.isStrongerThan(this.element)){
                damage*=0.8;
            }
        }
        target.receiveDamage((int) damage,this);
    }

    public void castSpell(Combatant target) {
        calculateMagicDamage(target);
    }
    void func(){
        System.out.println("asdas");
    }
    public MagicElement getElement() {
        return this.element;
    }

    public boolean isElementStrongerThan(MagicAttacker other) {
        if (other.getElement()!=null){
            return this.element.isStrongerThan(other.getElement());
        }
        else{
            return false;
        }
    }

    public double getAccuracy() {
        return 0;
    }

    public void fightClose(Combatant target) {
        int damage = getPower();
        if (isCriticalHit()) {
            damage *= 2;
        }
        target.receiveDamage(damage, this);
    }

    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target) == 1;
    }

    public void fightRanged(Combatant target) {
        if (isInRange(this.getPosition(),target.getPosition())) {
            castSpell(target);
        }
    }

    public int getRange() {
        return 2;
    }

    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target) <= getRange();
    }

    public void attack(Combatant target){
        if (isInMeleeRange(getPosition(), target.getPosition())) {
            fightClose(target);
        }
        else {
            fightRanged(target);
        }
    }

    public String getDisplaySymbol() {
        return "dragon";
    }

    public String toString() {
        return String.format("🐉 Dragon | Element: %s | %s",
                element.toString(),
                super.toString());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Dragon other))
            return false;
        return super.equals(obj) && this.element == other.getElement();
    }

    @Override
    public String getType() {
        return "ranged";
    }

    private MagicElement element;

    public Dragon clone() {
        Dragon copy = new Dragon(getPosition().getRow(), getPosition().getCol(), this.element);
        copyEnemyFieldsTo(copy);
        return copy;
    }
}