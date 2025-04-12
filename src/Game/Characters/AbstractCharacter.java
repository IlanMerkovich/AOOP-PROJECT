package Game.Characters;

import Game.Combat.Combatant;
import Game.Core.GameEntity;
import Game.Map.Position;
import java.util.Random;

public abstract class AbstractCharacter implements Combatant, GameEntity{
    public AbstractCharacter(int r,int c){
        position=new Position(r,c);
        Health=100;
        Power=new Random().nextInt(11) + 4;
        evasionChance=0.25;
    }
    public boolean equals(Object obj){
        if (!(obj instanceof AbstractCharacter)){
            return false;
        }
        AbstractCharacter other=(AbstractCharacter) obj;
        return this.getPosition().equals(other.getPosition()) && this.getHealth()==other.getHealth() && this.Power==other.getPower();
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
        if (this.Power+amount>14){
            this.Power=14;
        }
        else{
            this.Power+=amount;
        }
    }
    public boolean setHealth(int health) {
        if (health>100 || health<0){
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
        if (Health<=0){
            return true;
        }
        return false;
    }
    public Position getPosition(){
        return position;
    }
    public void setPosition(Position position) {
        this.position = position; //position is immutable
    }
    public boolean tryEvade(){
        return (new Random().nextDouble()<=evasionChance);
    }
    public void receiveDamage(int amount,Combatant source){
        if (tryEvade()){
            System.out.println("You have evaded the strike");
        }
        else{
            this.setHealth(this.getHealth()-amount);
        }
    }
    private Position position;
    private int Health,Power;
    private double evasionChance;
}
