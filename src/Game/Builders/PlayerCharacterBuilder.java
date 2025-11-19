package Game.Builders;

import Game.Characters.*;

public class PlayerCharacterBuilder {
    private int health;
    private int power;
    private String name;
    private int defaultHealth;
    private int defaultPower;

    public PlayerCharacterBuilder setDefaults(int health, int power) {
        this.defaultHealth = health;
        this.defaultPower = power;
        this.health = health;
        this.power = power;
        return this;
    }

    public PlayerCharacterBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PlayerCharacterBuilder setHealth(int health) {
        this.health = health;
        return this;
    }

    public PlayerCharacterBuilder setPower(int power) {
        this.power = power;
        return this;
    }

    public int getDefaultHealth() {
        return defaultHealth;
    }

    public int getDefaultPower() {
        return defaultPower;
    }

    public boolean isValid() {
        int deltaHealth = health - defaultHealth;
        int deltaPower = power - defaultPower;

        boolean healthInRange = deltaHealth >= -2 && deltaHealth <= 2;
        boolean powerInRange  = deltaPower  >= -3 && deltaPower  <= 3;

        return (deltaHealth + deltaPower == 0) && healthInRange && powerInRange;
    }

    public PlayerCharacter buildWarrior() {
        if (!isValid())
            throw new IllegalStateException("Invalid attribute distribution");
        Warrior w = new Warrior(0, 0, name); // position will be set later
        w.setHealthExtra(health);
        w.addPower(power - w.getPower());
        return w;
    }

    public PlayerCharacter buildArcher() {
        if (!isValid())
            throw new IllegalStateException("Invalid attribute distribution");
        Archer a = new Archer(0, 0, name);
        a.setHealthExtra(health);
        a.addPower(power - a.getPower());
        return a;
    }

    public PlayerCharacter buildMage() {
        if (!isValid())
            throw new IllegalStateException("Invalid attribute distribution");
        Mage m = new Mage(0, 0, name);
        m.setHealthExtra(health);
        m.addPower(power - m.getPower());
        return m;
    }
}
