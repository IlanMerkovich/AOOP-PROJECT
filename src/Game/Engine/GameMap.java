package Game.Engine;

import Game.Characters.Dragon;
import Game.Characters.Goblin;
import Game.Characters.Orc;
import Game.Characters.PlayerCharacter;
import Game.Core.GameEntity;
import Game.Items.Potion;
import Game.Items.PowerPotion;
import Game.Items.Wall;
import Game.Map.Position;

import java.util.*;

public class GameMap{
    private Map<Position,List<GameEntity>>grid;
    public GameMap(int rows,int cols,String name){
        this.grid=new HashMap<>();
        Random random=new Random();
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                Position newPos=new Position(i,j);
                GameEntity entity=generateEntity(i,j,random);
                List<GameEntity>entityCell=new ArrayList<>();
                if (entity!=null){
                    entity.setPosition(newPos);
                    entityCell.add(entity);
                }
                grid.put(newPos,entityCell);
            }
            while (true){
                int r=random.nextInt(rows);
                int c=random.nextInt(cols);
                Position playerPosition=new Position(r,c);
                List<GameEntity>Cell=grid.get(playerPosition);
                if (Cell.isEmpty()){

                }
            }
        }
    }
    private PlayerCharacter create
    private GameEntity generateEntity(int row,int col,Random random){
        int probability=random.nextInt(100);
        if (probability<40){
            return null;
        }
        else if (probability<70){
            int type=random.nextInt(3);
            if (type==0){
                return new Goblin(row,col);
            }
            else if (type==1){
                return new Orc(row,col);
            }
            else{
                return new Dragon(row,col);
            }
        }
        else if (probability<80){
            return new Wall(row,col);
        }
        else{
            int typepotion=random.nextInt(100);
            if (typepotion<75){
                return new Potion(row,col);
            }
            else{
                return new PowerPotion(row,col);
            }
        }
    }
}
