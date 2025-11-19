package Game.Engine;

import Game.Characters.Enemy;
import Game.Characters.PlayerCharacter;

public interface GameListener {
    void changeDetected();
    void onMapChange();
    void playerAttack(PlayerCharacter playerCharacter, Enemy enemy);
}