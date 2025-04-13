package Game.Combat;

public class CombatSystem{
    void resolveCombat(Combatant attacker, Combatant defender){
        if (defender.tryEvade()){
            return;
        }
    }
}
