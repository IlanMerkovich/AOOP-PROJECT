package Game.Engine;

import Game.Characters.Enemy;
import Game.Characters.PlayerCharacter;
import Game.Core.GameEntity;
import Game.Items.GameItem;
import Game.Map.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameWorldMemento{
    private List<PlayerCharacter> playersCopy;
    private List<Enemy>enemiesCopy;
    private List<GameItem>itemsCopy;
    private Map<Position, List<GameEntity>> gridCopy;

    public GameWorldMemento(List<PlayerCharacter> players,List<Enemy>enemies,List<GameItem>items,Map<Position, List<GameEntity>> grid){
        playersCopy=new ArrayList<>();
        enemiesCopy=new ArrayList<>();
        itemsCopy=new ArrayList<>();
        for (PlayerCharacter playerCharacter:players){
            playersCopy.add(playerCharacter.clone());}
        for (Enemy enemy:enemies){
            enemiesCopy.add(enemy.clone());}
        for (GameItem item:items){
            itemsCopy.add((GameItem) item.clone());}
        this.gridCopy=deepCopyGrid(grid);
    }
    public Map<Position, List<GameEntity>> deepCopyGrid(Map<Position, List<GameEntity>> original){
        Map<Position, List<GameEntity>> copy = new HashMap<>();
        for (Map.Entry<Position, List<GameEntity>> entry : original.entrySet()) {
            List<GameEntity> clonedEntities = new ArrayList<>();
            for (GameEntity entity : entry.getValue()) {
                clonedEntities.add(entity.clone());
            }
            copy.put(new Position(entry.getKey()), clonedEntities);
        }
        return copy;
    }

    public List<PlayerCharacter> getPlayers() {
        return new ArrayList<>(playersCopy);
    }
    public List<Enemy> getEnemies() {
        return new ArrayList<>(enemiesCopy);
    }
    public List<GameItem> getItems() {
        return new ArrayList<>(itemsCopy);
    }
    public Map<Position, List<GameEntity>> getGrid() {
        return deepCopyGrid(gridCopy);
    }
}


