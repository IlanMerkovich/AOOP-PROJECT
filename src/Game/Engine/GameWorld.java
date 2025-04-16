package Game.Engine;

import Game.Characters.*;
import Game.Combat.CombatSystem;
import Game.Core.GameEntity;
import Game.Items.*;
import Game.Map.Position;

import java.util.*;

public class GameWorld {
    public GameWorld(int rows, int cols, String name, int type){
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.gameMap = new GameMap(rows,cols);
        this.combatSystem = new CombatSystem();
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
                if (!gameMap.getGrid().get(newPos).isEmpty()) continue;

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
                    entity = random.nextInt(100) < 25 ? new PowerPotion(i, j) : new Potion(i, j);
                    items.add((GameItem) entity);
                }

                if (entity != null) {
                    gameMap.placeEntity(newPos, entity);
                }
            }
        }
    }
    private Position getRandomEmptyPosition(int rows, int cols, Random rand) {
        while (true){
            Position pos = new Position(rand.nextInt(rows), rand.nextInt(cols));
            if (gameMap.getGrid().get(pos).isEmpty()) {
                return pos;
            }
        }
    }
    private void playTurn(PlayerCharacter player) {
        Scanner scanner = new Scanner(System.in);
        Position playerPosition = player.getPosition();
        int curX = playerPosition.getRow();
        int curY = playerPosition.getCol();

        System.out.println("Choose an action:");
        System.out.println("1. Move Up");
        System.out.println("2. Move Down");
        System.out.println("3. Move Left");
        System.out.println("4. Move Right");
        System.out.println("5. Use Potion");
        System.out.println("6. Use Power Potion");
        int choice;

        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter a number between 1 and 6.");
            scanner.nextLine();
            return;
        }

        switch (choice) {
            case 1 -> curX--;
            case 2 -> curX++;
            case 3 -> curY--;
            case 4 -> curY++;
            case 5 -> {
                if (player.usePotion()) {
                    System.out.println("You have used a Potion!");
                } else {
                    System.out.println("No Potion found!");
                }
                return;
            }
            case 6 -> {
                if (player.usePowerPotion()) {
                    System.out.println("You have used a Power Potion");
                } else {
                    System.out.println("No Power Potion found!");
                }
                return;
            }
            default -> {
                System.out.println("Invalid Choice!");
                return;
            }
        }

        Position newPos = new Position(curX, curY);
        if (!gameMap.getGrid().containsKey(newPos)) {
            System.out.println("Out of game bounds! - Invalid move");
            return;
        }

        List<GameEntity> entitiesAtnewPos = gameMap.getGrid().get(newPos);
        if (entitiesAtnewPos == null) {
            System.out.println("Internal error: No data for the target cell.");
            return;
        }

        if (entitiesAtnewPos.isEmpty() && !gameMap.isBlocked(newPos)) {
            gameMap.getGrid().get(playerPosition).remove(player);
            player.setPosition(newPos);
            gameMap.placeEntity(newPos, player);
            System.out.println("Player moved from " + playerPosition + " to " + newPos);
            return;
        }

        Iterator<GameEntity> it = entitiesAtnewPos.iterator();
        while (it.hasNext()) {
            GameEntity entity = it.next();
            if (entity instanceof Enemy e) {
                System.out.println("Enemy found, Starting Combat!");
                while (!player.isDead() && !e.isDead()) {
                    combatSystem.resolveCombat(player,e);
                }
                if (e.isDead()){
                    Treasure treasure=e.Defeat();
                    this.addItem(treasure);
                    gameMap.placeEntity(e.getPosition(),treasure);
                    gameMap.removeEntity(e.getPosition(),e);
                }
                return;
            }
            if (entity instanceof GameItem p) {
                p.interact(player);
                if (!p.isBlocksMovement()){
                    it.remove();
                }
            }

        }
    }
    public void gameLoop(){
        System.out.println("Welcome to AOOP Turn-Based Game!");
        PlayerCharacter player=this.getPlayer();
        while (true){
            if (player.isDead()){
                System.out.println("GAME OVER!");
                return;
            }
            boolean allEnemiesDead = true;
            for (Enemy e : enemies) {
                if (!e.isDead()) {
                    allEnemiesDead = false;
                    break;
                }
            }
            if (allEnemiesDead) {
                System.out.println("🏆Victory! All enemies have been defeated.");
                break;
            }
            this.updateVisibility(player.getPosition());
            gameMap.displayMap(player.getPosition());
            System.out.println(player);
            this.playTurn(player);
        }
    }
    private PlayerCharacter getPlayer(){
        return players.get(0);
    }
    private boolean addItem(GameEntity entity){
        if (entity instanceof GameItem e){
            items.add(e);
            return true;
        }
        return false;
    }
    private void updateVisibility(Position playerPos) {
        for (List<GameEntity> cell : gameMap.getGrid().values()) {
            for (GameEntity entity : cell) {
                boolean visible = playerPos.distanceTo(entity.getPosition()) <= 2;
                entity.setVisible(visible);
            }
        }
    }

    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;
    private GameMap gameMap;
    private CombatSystem combatSystem;
}
