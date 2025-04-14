import Game.Characters.*;
import Game.Combat.CombatSystem;
import Game.Core.GameEntity;
import Game.Engine.GameMap;
import Game.Engine.GameWorld;
import Game.Items.*;
import Game.Map.Position;

public class Main {
    public static void main(String[] args){
        Archer archer = new Archer(1, 1, "Legolas");
        Dragon dragon  = new Dragon(2, 2);
        CombatSystem.getInstance().resolveCombat(archer, dragon);
    }
}

