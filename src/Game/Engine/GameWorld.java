package Game.Engine;

import Game.Characters.*;
import Game.Combat.CombatSystem;
import Game.Combat.Combatant;
import Game.Combat.RangedFighter;
import Game.Core.GameEntity;
import Game.Items.*;
import Game.Map.Position;

import java.util.*;
/**
 * GameWorld is the central controller of the turn-based RPG game.
 * It initializes the game map, generates all entities (player, enemies, and items),
 * handles user input, movement, combat, item interaction, and the main game loop.
 *
 * This class manages:
 * - Initial setup of the world and user input
 * - Populating the grid with enemies and items
 * - Processing turns and actions from the player
 * - Combat resolution using CombatSystem
 * - Visibility logic for entities based on player's position
 */
public class GameWorld {
    /**
     * Constructs the game world by:
     * - Collecting input from the user for map size and character choice
     * - Initializing the game map
     * - Placing the player at a random empty position
     * - Randomly populating the map with enemies, walls, and potions
     */
    public GameWorld(){
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.combatSystem = new CombatSystem();
        Scanner sc=new Scanner(System.in);
        Random random = new Random();
        int rows = 0, cols = 0;
        while (rows < 10) {
            System.out.print("Enter number of rows (minimum 10): ");
            try {
                rows = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter an integer.");
                sc.nextLine();
            }
        }

        while (cols < 10) {
            System.out.print("Enter number of columns (minimum 10): ");
            try {
                cols = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter an integer.");
                sc.nextLine();
            }
        }

        sc.nextLine();
        System.out.print("Enter your character name: ");
        String name = sc.nextLine().trim();

        int type = 0;
        while (type < 1 || type > 3) {
            System.out.print("Choose your class (1 = Warrior, 2 = Mage, 3 = Archer): ");
            try {
                type = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter 1, 2, or 3.");
                sc.nextLine();
            }
        }
        this.gameMap = new GameMap(rows,cols);

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
                }
                else if (probability < 40) {
                    entity = new Wall(i, j);
                }
                else if (probability < 60) {
                    int chance = random.nextInt(100);
                    if (chance < 25) {
                        entity=new PowerPotion(i,j);
                        items.add((GameItem) entity);
                    }
                    else {
                        entity=new Potion(i,j);
                        items.add((GameItem) entity);
                    }
                }

                if (entity != null) {
                    gameMap.placeEntity(newPos, entity);
                }
            }
        }
    }
    /**
     * Picks a random empty cell from the map for placing entities (like player).
     *
     * @param rows total number of rows on the map
     * @param cols total number of columns on the map
     * @param rand Random instance
     * @return a Position on the map that is currently empty
     */
    private Position getRandomEmptyPosition(int rows, int cols, Random rand) {
        while (true){
            Position pos = new Position(rand.nextInt(rows), rand.nextInt(cols));
            if (gameMap.getGrid().get(pos).isEmpty()) {
                return pos;
            }
        }
    }
    /**
     * Handles a single turn of the player.
     * Presents an action menu to the player.
     * Processes movement, item use (potions), and interaction with enemies/items on the map.
     *
     * @param player the active player character
     */
    private void playTurn(PlayerCharacter player){
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
        System.out.println("7. Fight ranged");
        int choice;

        try {
            choice = scanner.nextInt();
        }
        catch (InputMismatchException e) {
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
                    System.out.println("You have used a health potion!");
                }
                else {
                    System.out.println("No health potion found!");
                }
            }
            case 6 -> {
                if (player.usePowerPotion()) {
                    System.out.println("You have used a power potion!");
                } else {
                    System.out.println("No power potion found!");
                }
                return;
            }
            case 7->{
                if (!(player instanceof RangedFighter)){
                    System.out.println("You cant fight ranged!");
                    return;
                }
                Iterator<Enemy> enemyIterator = enemies.iterator();
                while (enemyIterator.hasNext()) {
                    Enemy enemy = enemyIterator.next();
                    if (enemy.getPosition().distanceTo(playerPosition) == 2) {
                        ManageFight(player, enemy);
                        if (enemy.isDead()){
                            enemyIterator.remove();
                        }
                    }
                }

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
                ManageFight(player,e);
            }
            if (entity instanceof Pickupable pickupable){
                pickupable.pickup(player);
                it.remove();
                return;
            }
            if (entity instanceof Interactable interactable){
                interactable.interact(player);
                it.remove();
            }
        }
    }

    /**
     * Handles the combat loop between a player character and an enemy.
     * The method continues to call the combat system until one of the combatants dies.
     * If the enemy is defeated, it is removed from the map and replaced with a treasure.
     *
     * @param player the player character participating in the fight
     * @param enemy  the enemy the player is fighting against
     */
    private void ManageFight(PlayerCharacter player, Enemy enemy) {
        while (!player.isDead() && !enemy.isDead()){
            combatSystem.resolveCombat(player,enemy);
        }
        if (enemy.isDead()){
            Treasure treasure=enemy.Defeat();
            this.addItem(treasure);
            gameMap.placeEntity(enemy.getPosition(),treasure);
            gameMap.removeEntity(enemy.getPosition(),enemy);
        }
    }

    /**
     * Main game loop.
     * Repeats until the player dies or all enemies are defeated.
     * On each turn:
     * - Checks victory/defeat conditions
     * - Updates visibility around player
     * - Displays the map and player info
     * - Lets the player perform an action (movement, item usage, combat)
     */
    public void gameLoop(){
        System.out.println("Welcome to OOP Turn-Based Game!");
        PlayerCharacter player=this.getPlayer();
        while (true){
            if (player.isDead()){
                System.out.println("GAME OVER!");
                System.out.println("Your treasure points are: "+player.getTreasurePoints());
                break;
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
                System.out.println("Your treasure points are: "+player.getTreasurePoints());
                break;
            }
            this.updateVisibility(player.getPosition());
            gameMap.displayMap(player.getPosition());
            System.out.println(player);
            this.playTurn(player);
        }
    }
    /**
     * Returns the first (and currently only) player in the world.
     *
     * @return the main player character
     */
    private PlayerCharacter getPlayer(){
        return players.get(0);
    }
    /**
     * Adds a game item to the world's item list.
     *
     * @param entity the GameEntity to add (must be GameItem)
     * @return true if the item was added successfully, false otherwise
     */
    private boolean addItem(GameEntity entity){
        if (entity instanceof GameItem e){
            items.add(e);
            return true;
        }
        return false;
    }
    /**
     * Updates visibility of entities on the map.
     * Entities within a Manhattan distance of 2 from the player are visible.
     *
     * @param playerPos the current position of the player
     */
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
