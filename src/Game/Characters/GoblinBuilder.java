package Game.Characters;

public class GoblinBuilder implements EnemyBuilder {
    @Override
    public Enemy build(int row, int col) {
        return new Goblin(row,col);
    }
}
