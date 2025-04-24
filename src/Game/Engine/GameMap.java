package Game.Engine;

import Game.Characters.*;
import Game.Core.GameEntity;
import Game.Items.GameItem;
import Game.Map.Position;

import java.util.*;
/**
 * GameMap represents the 2D game board that holds all entities in the world,
 * including players, enemies, and items.
 * Each cell in the grid can contain multiple entities.
 * The map supports operations such as placing entities, displaying the map,
 * and checking for movement blockage.
 */
public class GameMap{
    private int rows,cols;
    /**
     * Constructs a new GameMap with the given number of rows and columns.
     * Initializes each cell in the grid as an empty list.
     *
     * @param rows number of rows (height of the map)
     * @param cols number of columns (width of the map)
     */
    public GameMap(int rows, int cols) {
        this.rows=rows;
        this.cols=cols;
        this.grid = new HashMap<>();
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                grid.put(new Position(i,j),new ArrayList<>());
            }
        }
    }
    /**
     * Places a game entity on the map at a given position.
     * If the position does not yet exist in the grid, it is initialized.
     *
     * @param position the position to place the entity
     * @param entity   the game entity to place
     */
    public void placeEntity(Position position,GameEntity entity){
        if (position==null || entity==null){
            return;
        }
        if (!grid.containsKey(position)){
            grid.put(position,new ArrayList<>());
        }
        entity.setPosition(position);
        grid.get(position).add(entity);
    }
    /**
     * Returns the full map grid containing all entities on the board.
     *
     * @return a map from Position to a list of GameEntities
     */
    public Map<Position,List<GameEntity>>getGrid(){
        return grid;
    }
    /**
     * Displays the current map view in the console.
     * Only entities within a visibility range of 2 cells from the player
     * are shown. Others are hidden with "??".
     * Player characters are displayed in blue.
     *
     * @param playerPos the current position of the player
     */
    public void displayMap(Position playerPos) {
        final String RESET = "\u001B[0m";
        final String BLUE = "\u001B[34m";

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Position position = new Position(i, j);
                List<GameEntity> cell = grid.getOrDefault(position, new ArrayList<>());
                int distance = playerPos.distanceTo(position);

                if (cell.isEmpty()) {
                    if (distance <= 2) {
                        System.out.print(" ** ");
                    }
                    else {
                        System.out.print(" ?? ");
                    }
                } else {
                    GameEntity entity = cell.get(0);
                    if (entity instanceof PlayerCharacter){
                        System.out.print(" " + BLUE + entity.getDisplaySymbol() + RESET + " ");
                    }
                    else {
                        if (entity.getVisibility()){
                            System.out.print(" " + entity.getDisplaySymbol() + " ");
                        }
                        else{
                            System.out.print(" ?? ");
                        }
                    }
                }
            }
            System.out.println("\n");
        }
    }
    /**
     * Checks whether the specified position contains any entity
     * that blocks movement (such as walls or uncollected items).
     *
     * @param pos the position to check
     * @return true if movement is blocked, false otherwise
     */
    public boolean isBlocked(Position pos) {
        List<GameEntity> entities = grid.get(pos);
        for (GameEntity entity : entities) {
            if (entity instanceof GameItem item && item.isBlocksMovement()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Removes the specified entity from the given position on the map.
     *
     * @param pos    the position from which to remove the entity
     * @param entity the entity to remove
     * @return true if the entity was found and removed, false otherwise
     */
    public boolean removeEntity(Position pos, GameEntity entity) {
        List<GameEntity> cell = grid.get(pos);
        if (cell != null && cell.contains(entity)) {
            return cell.remove(entity);
        }
        return false;
    }

    private Map<Position,List<GameEntity>>grid;

}
