package Game.Engine;
import Game.Characters.Enemy;
import Game.Characters.PlayerCharacter;
import Game.Core.GameEntity;
import Game.Items.GameItem;
import java.util.ArrayList;
import java.util.List;

public class GameWorld{
    private List<PlayerCharacter>players;
    private List<Enemy>enemies;
    private List<GameItem>items;
    private GameMap map;
    public GameWorld(int rows,int cols,String name,int type){
        this.players=new ArrayList<PlayerCharacter>();
        this.enemies=new ArrayList<Enemy>();
        this.items=new ArrayList<GameItem>();
        this.map=new GameMap(rows,cols,name,type);
    }
}
