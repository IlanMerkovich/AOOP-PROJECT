package Game.Engine;

import Game.Characters.*;
import Game.Core.GameEntity;
import Game.Items.GameItem;
import Game.Items.Potion;
import Game.Items.PowerPotion;
import Game.Items.Wall;
import Game.Map.Position;

import java.util.*;

public class GameMap{
    private int rows,cols;
    public GameMap(int rows, int cols) {
        this.rows=rows;
        this.cols=cols;
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
    public void displayMap(){
        final String RESET = "\u001B[0m";
        final String BLUE = "\u001B[34m";

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                Position position=new Position(i,j);
                List<GameEntity> cell = grid.getOrDefault(position, new ArrayList<>());
                if (cell.isEmpty()){
                    System.out.print("  **  ");
                }
                else{
                    GameEntity entity=cell.get(0);
                    if (entity instanceof PlayerCharacter){
                        System.out.print(" "+BLUE+entity.getDisplaySymbol()+RESET+" ");
                    }
                    else {
                        System.out.print("  "+entity.getDisplaySymbol()+"  ");
                    }

                }
            }
            System.out.println("\n");
        }
    }
    public boolean isBlocked(Position pos) {
        List<GameEntity> entities = grid.get(pos);
        for (GameEntity entity : entities) {
            if (entity instanceof GameItem item && item.isBlocksMovement()) {
                return true;
            }
        }
        return false;
    }
    public boolean removeEntity(Position pos, GameEntity entity) {
        List<GameEntity> cell = grid.get(pos);
        if (cell != null && cell.contains(entity)) {
            return cell.remove(entity);
        }
        return false;
    }
    private Map<Position,List<GameEntity>>grid;

}
