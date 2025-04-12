package Game.Engine;

import Game.Core.GameEntity;
import Game.Map.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap{
    private Map<Position,List<GameEntity>>grid;
    public GameMap(){
        this.grid = new HashMap<Position,List<GameEntity>>();
    }
}
