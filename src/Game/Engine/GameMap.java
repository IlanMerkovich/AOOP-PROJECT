package Game.Engine;

import Game.Characters.*;
import Game.Core.GameEntity;
import Game.Items.Potion;
import Game.Items.PowerPotion;
import Game.Items.Wall;
import Game.Map.Position;

import java.util.*;

public class GameMap{
    private static GameMap instance=null;
    public static GameMap getInstance(int rows,int cols){
        if (instance==null){
            return new GameMap(rows,cols);
        }
        return instance;
    }

    private Map<Position,List<GameEntity>>grid;
    public GameMap(int rows, int cols) {
        this.grid = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                grid.put(new Position(i,j),new ArrayList<>());
            }
        }
    }
    public void placeEntity(Position position,GameEntity entity){
        if (!grid.containsKey(position)){
            grid.put(position,new ArrayList<>());
        }
        entity.setPosition(position);
        grid.get(position).add(entity);
    }
    public Map<Position,List<GameEntity>>getGrid(){
        return grid;
    }
}
