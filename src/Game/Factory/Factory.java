package Game.Factory;

import Game.Characters.AbstractCharacter;
import Game.Characters.Enemy;
import Game.Characters.PlayerCharacter;

import java.util.List;

public interface Factory {
    Enemy createCharacter(List<Enemy> enemies, int rows, int cols);
}
