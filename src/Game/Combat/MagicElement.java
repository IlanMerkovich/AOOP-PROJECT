package Game.Combat;

import java.util.Random;

public enum MagicElement {
    FIRE, ICE, LIGHTNING, ACID;

    public boolean isStrongerThan(MagicElement other) {
        if (this == FIRE) {
            return other == ICE;
        } else if (this == ICE) {
            return other == LIGHTNING;
        } else if (this == LIGHTNING) {
            return other == ACID;
        } else if (this == ACID) {
            return other == FIRE;
        } else {
            return false;
        }
    }
    public static MagicElement getRandomElement(){
        return values()[new Random().nextInt(values().length)];
    }
}
