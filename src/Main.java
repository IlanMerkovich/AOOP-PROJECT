import Game.Characters.Archer;
import Game.Characters.Goblin;
import Game.Combat.CombatSystem;
import Game.Engine.GameMap;
import Game.Engine.GameWorld;
import Game.Map.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        GameWorld gameWorld=new GameWorld(10,10,"Ilan",1);
        gameWorld.gameLoop();
    }
}
