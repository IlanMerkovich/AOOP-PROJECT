    package Game.Combat;
    import Game.Characters.Enemy;
    import Game.Map.Position;
    /**
     * Handles turn-based combat between two combatants in the game.
     * Supports checking range and applying attack logic.
     */
    public class CombatSystem {
        /**
         * Constructs a new CombatSystem instance.
         */
        public CombatSystem() {
        }
        /**
         * Executes a full combat round between two combatants.
         * The attacker attacks first. If the defender survives and is in range,
         * they counterattack.
         *
         * @param attacker the combatant initiating the attack
         * @param defender the combatant being attacked
         */
        public void resolveCombat(Combatant attacker, Combatant defender){
            if (isInRange(attacker, defender)) {
                attacker.attack(defender);
                if (defender.isDead()){
                    if (defender instanceof Enemy e) {
                        System.out.println("Enemy was defeated!");
                    }
                    else {
                        System.out.println("You are dead. You lost!");
                    }
                    return;
                }
            }
            else {
                System.out.println("Defender is out of range!");
                return;
            }
            if (isInRange(defender, attacker)) {
                defender.attack(attacker);
                if (attacker.isDead()) {
                    System.out.println("You are dead. You lost!");
                }
            }
        }
        /**
         * Checks if the target is within the attack range of the source.
         * Supports both melee and ranged fighters.
         *
         * @param source the attacking combatant
         * @param target the target combatant
         * @return true if in range, false otherwise
         */
        private boolean isInRange(Combatant source, Combatant target){
            Position srcPos=source.getPosition();
            Position trgPos=target.getPosition();
            if (source instanceof MeleeFighter mf){
                return mf.isInMeleeRange(srcPos,trgPos);
            }
            else if (source instanceof RangedFighter rf){
                return rf.isInRange(srcPos,trgPos);
            }
            return false;
        }
    }
