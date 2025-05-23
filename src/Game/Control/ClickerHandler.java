package Game.Control;
import Game.Audio.SoundManager;
import Game.Characters.Enemy;
import Game.Characters.PlayerCharacter;
import Game.Combat.RangedFighter;
import Game.Core.GameEntity;
import Game.Engine.GameWorld;
import Game.Items.GameItem;
import Game.Items.Potion;
import Game.Items.Treasure;
import Game.Map.Position;

import java.awt.*;
import java.util.List;

public class ClickerHandler{
    GameWorld gameWorld;
    public ClickerHandler(GameWorld gameWorld){
        this.gameWorld=gameWorld;
    }
    public void handleLeftClick(Position pos){
        PlayerCharacter playerCharacter=gameWorld.getPlayer();
        Position playerPos=playerCharacter.getPosition();
        List<GameEntity>cell=gameWorld.getGameMap().getGrid().get(pos);
        int distance=playerCharacter.getPosition().distanceTo(pos);
        if (cell==null || cell.isEmpty()){
            if (distance==1){
                gameWorld.movePlayerTo(pos);
            }
            else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        else{
            GameEntity entity=cell.getFirst();
            if (entity instanceof Enemy enemy){
                if (playerCharacter instanceof RangedFighter){
                    if (distance<=2){
                        gameWorld.attackEnemyAt(pos);
                    }
                    else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
                else {
                    if (distance==1){
                        gameWorld.attackEnemyAt(pos);
                    }
                    else{
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
            else if (entity instanceof GameItem item){
                if (distance==1){
                    if (item instanceof Potion){
                        gameWorld.pickupItemAt(pos);
                    }
                    else if (item instanceof Treasure){
                        SoundManager.playEffect("point.wav");
                        gameWorld.interactWithItemAt(pos);
                    }
                }
                else{
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
    }
}
