package Game.Characters;

public class OrcBuilder implements EnemyBuilder{
    @Override
    public Enemy build(int row, int col) {
        return new Orc(row,col);
    }
}
