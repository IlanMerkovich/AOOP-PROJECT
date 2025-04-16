    package Game.Combat;
    import Game.Characters.Enemy;
    import Game.Characters.PlayerCharacter;
    import Game.Core.GameEntity;
    import Game.Engine.GameMap;
    import Game.Engine.GameWorld;
    import Game.Items.Potion;
    import Game.Items.Treasure;
    import Game.Map.Position;

    public class CombatSystem {
        public CombatSystem() {
        }
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
