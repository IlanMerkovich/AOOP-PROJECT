package Game.Characters;

import Game.Combat.Combatant;
import Game.Core.GameEntity;
import Game.Logs.LogManager;
import Game.Map.Position;
import java.util.Random;

public abstract class AbstractCharacter implements Combatant, GameEntity{
    public AbstractCharacter(int r,int c){
        position=new Position(r,c);
        Health=100;
        Power=new Random().nextInt(4,14);
        evasionChance=0.25;
        visibility=false;
    }

    public boolean equals(Object obj){
        if (!(obj instanceof AbstractCharacter)){
            return false;
        }
        AbstractCharacter other=(AbstractCharacter) obj;
        return this.getPosition().equals(other.getPosition()) && this.getHealth()==other.getHealth() && this.Power==other.getPower();
    }

    public void setVisible(boolean visible) {
        this.visibility=visible;
    }

    public void Heal(int amount){
        if (this.Health+amount>100){
            this.Health=100;
        }
        else{
            this.Health+=amount;
        }
    }

    public void addPower(int amount){
        this.Power+=amount;
    }

    public boolean setHealth(int health) {
        if (health>100 || health<0){
            return false;
        }
        this.Health=health;
        return true;
    }

    public boolean setHealthExtra(int health){
        if (health>103 || health <0){
            return false;
        }
        this.Health=health;
        return true;
    }

    public int getHealth(){
        return this.Health;
    }

    public int getPower() {
        return Power;
    }

    public boolean isDead() {
        return Health <= 0;
    }

    public Position getPosition(){
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean tryEvade(Combatant attacker){
        double accuracyFactor = 1.0;
        accuracyFactor = 1 - attacker.getAccuracy();
        double evadeChance = this.evasionChance * accuracyFactor;
        double roll = new Random().nextDouble();
        return roll < evadeChance;
    }

    public void receiveDamage(int amount, Combatant source){
        if (tryEvade(source)){
            LogManager.addLog("Entity evaded attack at: "+source.getPosition());
        }
        else {
            this.getDamage(amount);
        }
    }

    protected double getEvasionChance(){
        return evasionChance;
    }

    public String toString() {
        return String.format("%s | HP: %d | Power: %d | Evasion: %.2f | Position: (%d,%d)",
                getClass().getSimpleName(),
                getHealth(),
                getPower(),
                evasionChance,
                getPosition().getRow(),
                getPosition().getCol());
    }

    public boolean getVisibility() {
        return visibility;
    }

    protected void getDamage(int amount){
        this.Health-=amount;
    }

    protected void copyFieldsTo(AbstractCharacter target) {
        target.position = new Position(this.position);
        target.Health = this.Health;
        target.Power = this.Power;
        target.evasionChance = this.evasionChance;
        target.visibility = this.visibility;
    }
    public void setPower(int amount){
        this.Power=amount;
    }

    public abstract GameEntity clone();

    private Position position;
    private int Health,Power;
    private double evasionChance;
    private boolean visibility;
}