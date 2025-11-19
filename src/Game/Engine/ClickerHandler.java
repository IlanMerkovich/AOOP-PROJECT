package Game.Engine;
import Game.Audio.SoundManager;
import Game.Characters.Enemy;
import Game.Characters.PlayerCharacter;
import Game.Combat.RangedFighter;
import Game.Core.GameEntity;
import Game.Items.GameItem;
import Game.Items.Potion;
import Game.Items.Treasure;
import Game.Map.Position;

import java.awt.*;
import java.util.List;

/**
 * This class handles logic of action listener events such as left click and so
 */
public class ClickerHandler{
    GameController gameController;
    public ClickerHandler(GameController gameController){
        this.gameController=gameController;
    }

    /**
     * this method handles left click event
     * @param pos position clicked
     * @return a click result objects contains all data from the event
     */
    public ClickResult handleLeftClickMap(Position pos){
        PlayerCharacter playerCharacter=gameController.getPlayer();
        List<GameEntity>cell=gameController.getGameMap().getGrid().get(pos);
        int distance=playerCharacter.getPosition().distanceTo(pos);
        if (cell==null || cell.isEmpty()){
            if (distance==1){
                gameController.movePlayerTo(pos);
                return new ClickResult(ClickResult.Type.MOVE,0,0);
            }
            else {
                Toolkit.getDefaultToolkit().beep();
                return new ClickResult(ClickResult.Type.NONE,0,0);
            }
        }
        else{
            GameEntity entity=cell.getFirst();
            if (entity instanceof Enemy enemy){
                if (playerCharacter.getType().equals("ranged")){
                    if (distance<=2){
                        return attackAndGetClickResult(pos, playerCharacter, enemy);
                    }
                    else {
                        Toolkit.getDefaultToolkit().beep();
                        return new ClickResult(ClickResult.Type.NONE,0,0);
                    }
                }
                else {
                    if (distance==1){
                        return attackAndGetClickResult(pos, playerCharacter, enemy);
                    }
                    else{
                        Toolkit.getDefaultToolkit().beep();
                        return new ClickResult(ClickResult.Type.NONE,0,0);
                    }
                }
            }
            else if (entity instanceof GameItem item){
                if (distance==1){
                    if (item instanceof Potion){
                        gameController.pickupItemAt(pos);
                        return new ClickResult(ClickResult.Type.PICKUP,0,0);
                    }
                    else if (item instanceof Treasure){
                        SoundManager.playEffect("point.wav");
                        gameController.interactWithItemAt(pos);
                        return new ClickResult(ClickResult.Type.PICKUP,0,0);
                    }
                }
                else{
                    Toolkit.getDefaultToolkit().beep();
                    return new ClickResult(ClickResult.Type.NONE,0,0);
                }
            }
        }
        return new ClickResult(ClickResult.Type.NONE,0,0);
    }
    private ClickResult attackAndGetClickResult(Position pos, PlayerCharacter playerCharacter, Enemy enemy) {
        int beforeE = enemy.getHealth();
        int beforeP = playerCharacter.getHealth();
        gameController.attackEnemyAt(pos);
        int dmgE = beforeE - enemy.getHealth();
        int dmgP = beforeP - playerCharacter.getHealth();
        return new ClickResult(ClickResult.Type.ATTACK,dmgE,dmgP);
    }
}