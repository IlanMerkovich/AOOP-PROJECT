package Game.Engine;
import Game.Characters.*;
import Game.Core.GameEntity;
import Game.Items.GameItem;
import Game.Items.Potion;
import Game.Items.PowerPotion;
import Game.Items.Wall;
import Game.Map.Position;
import java.util.*;

public class GameWorld {
    public GameWorld(int rows, int cols, String name, int type) {
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.gameMap = GameMap.getInstance(rows,cols);
        Random random = new Random();

        Position playerPosition = getRandomEmptyPosition(rows, cols, random);
        PlayerCharacter playerCharacter = switch (type) {
            case 1 -> new Warrior(playerPosition.getRow(), playerPosition.getCol(), name);
            case 2 -> new Mage(playerPosition.getRow(), playerPosition.getCol(), name);
            case 3 -> new Archer(playerPosition.getRow(), playerPosition.getCol(), name);
            default -> throw new IllegalArgumentException("Invalid type!");
        };
        players.add(playerCharacter);
        gameMap.placeEntity(playerPosition, playerCharacter);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Position newPos = new Position(i, j);
                if (!(gameMap.getGrid().get(newPos).isEmpty())) {
                    continue;
                }
                int probability = random.nextInt(100);
                GameEntity entity = null;

                if (probability < 30) {
                    int enemyType = random.nextInt(3);
                    entity = switch (enemyType) {
                        case 0 -> new Goblin(i, j);
                        case 1 -> new Orc(i, j);
                        default -> new Dragon(i, j);
                    };
                    enemies.add((Enemy) entity);
                } else if (probability < 40) {
                    entity = new Wall(i, j);
                } else if (probability < 60) {
                    int potionType = random.nextInt(100);
                    if (potionType < 25) {
                        entity = new PowerPotion(i, j);
                    } else {
                        entity = new Potion(i, j);
                    }
                    items.add((GameItem) entity);
                }
                if (entity != null) {
                    gameMap.placeEntity(newPos, entity);
                }
            }
        }
    }
    private Position getRandomEmptyPosition(int rows, int cols, Random rand) {
        while (true) {
            Position pos = new Position(rand.nextInt(rows), rand.nextInt(cols));
            if (gameMap.getGrid().get(pos).isEmpty()) {
                return pos;
            }
        }
    }

    public void playTurn(){
        Scanner scanner=new Scanner(System.in);
        PlayerCharacter player=players.get(0);

        System.out.println("Your turn, " + player.getName());
        System.out.println("1. Move (w/a/s/d)");
        System.out.println("2. Attack enemy near you");
        System.out.println("3. Use health potion");

        System.out.print("Choose action (1-5): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch(choice){
            case 1->{
                System.out.println("Direction W/S/A/D");
                String direction=scanner.nextLine();
            }
        }
    }

    private void movePlayer(Position from,Position to){
        PlayerCharacter player=players.get(0);
        gameMap.getGrid().get(from).remove(player);
        player.setPosition(to);
        gameMap.getGrid().get(to).add(player);
    }
    private void moveDirection(String direction){
        Position current=players.get(0).getPosition();
        int newRow=current.getRow();
        int newCol=current.getCol();

        switch (direction.toLowerCase()){
            case "w"->newRow++;
            case "s"->newRow--;
            case "a"->newCol--;
            case "d"->newCol++;
            default -> {
                System.out.println("Invalid Direction");
                return;
            }
        }
        Position newPos=new Position(newRow,newCol);
        if (!(gameMap.getGrid().containsKey(newPos))){
            System.out.println("Out of border ERROR");
            return;
        }
        List<GameEntity>cell=gameMap.getGrid().get(newPos);
        if (cell.isEmpty()){
            movePlayer(current,newPos);
        }
        if (cell.getFirst() instanceof Enemy e){

        }


    }









    public List<GameItem> getItems() {
        return items;
    }
    public GameMap getGameMap() {
        return gameMap;
    }
    public List<Enemy> getEnemies() {
        return enemies;
    }
    public List<PlayerCharacter> getPlayers() {
        return players;
    }






    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;
    private GameMap gameMap;

}
