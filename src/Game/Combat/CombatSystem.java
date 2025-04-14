package Game.Combat;

import Game.Characters.Enemy;
import Game.Core.GameEntity;
import Game.Map.Position;

public class CombatSystem{
    private static CombatSystem instance = null;
    public static CombatSystem getInstance(){
        if (instance==null){
            return new CombatSystem();
        }
        return instance;
    }
    public void resolveCombat(Combatant attacker, Combatant defender) {
        int counter=0;
        while (true) {
            attacker.attack(defender);
            if (defender.isDead()) {
                if (defender instanceof Enemy e) {
                    GameEntity newTreasure = e.Defeat();
                    break;
                }
            }
        }
    }


}
