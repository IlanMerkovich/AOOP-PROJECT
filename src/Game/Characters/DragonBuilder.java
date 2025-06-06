package Game.Characters;

public class DragonBuilder implements EnemyBuilder{
    @Override
    public Enemy build(int row, int col) {
        return new Dragon(row,col);
    }
}
