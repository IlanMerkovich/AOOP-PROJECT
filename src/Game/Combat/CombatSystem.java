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
