package Game.Factory;

import Game.Builders.EnemyBuildDirector;
import Game.Builders.EnemyBuilder;
import Game.Characters.*;

import java.util.*;
import java.util.function.Supplier;

public class EnemyFactory{
    private Map<CharacterTypes, Supplier<EnemyBuilder>> enemySuppliers;
    private final Random random = new Random();
    private final EnemyBuildDirector director = new EnemyBuildDirector();

    public EnemyFactory() {
        enemySuppliers = new HashMap<>();
        enemySuppliers.put(CharacterTypes.GOBLIN, () -> new EnemyBuilder("Goblin"));
        enemySuppliers.put(CharacterTypes.ORC, () -> new EnemyBuilder("Orc"));
        enemySuppliers.put(CharacterTypes.DRAGON, () -> new EnemyBuilder("Dragon"));
    }

    public Enemy createCharacter(List<Enemy> enemies, int rows, int cols) {
        List<CharacterTypes> existingTypes = getCurrentTypes(enemies);
        List<CharacterTypes> missingTypes = new ArrayList<>();
        for (CharacterTypes type : enemySuppliers.keySet()) {
            if (!existingTypes.contains(type)) {
                missingTypes.add(type);
            }
        }

        CharacterTypes selectedType;
        if (missingTypes.isEmpty()) {
            selectedType = getRandomEnemy();
        }
        else {
            selectedType = missingTypes.get(random.nextInt(missingTypes.size()));
        }
        EnemyBuilder builder = enemySuppliers.get(selectedType).get();
        director.constructRandomEnemy(builder);

        return builder.getProduct();
    }

    private CharacterTypes getRandomEnemy() {
        List<CharacterTypes> options = new ArrayList<>(enemySuppliers.keySet());
        return options.get(random.nextInt(options.size()));
    }

    private List<CharacterTypes> getCurrentTypes(List<Enemy> enemies) {
        List<CharacterTypes> existingTypes = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy instanceof Orc && !existingTypes.contains(CharacterTypes.ORC))
                existingTypes.add(CharacterTypes.ORC);
            if (enemy instanceof Goblin && !existingTypes.contains(CharacterTypes.GOBLIN))
                existingTypes.add(CharacterTypes.GOBLIN);
            if (enemy instanceof Dragon && !existingTypes.contains(CharacterTypes.DRAGON))
                existingTypes.add(CharacterTypes.DRAGON);
        }
        return existingTypes;
    }
}