package Game.Factory;

import Game.Characters.*;

import java.util.*;
import java.util.function.Supplier;

public class EnemyFactory implements Factory{
    private Map<CharacterTypes, Supplier<EnemyBuilder>> enemySuppliers;
    private final Random random = new Random();

    public EnemyFactory(){
        enemySuppliers=new HashMap<>();
        enemySuppliers.put(CharacterTypes.GOBLIN, GoblinBuilder::new);
        enemySuppliers.put(CharacterTypes.ORC, OrcBuilder::new);
        enemySuppliers.put(CharacterTypes.DRAGON, DragonBuilder::new);
    }
    public Enemy createCharacter(List<Enemy>enemies, int rows, int cols){
        List<CharacterTypes>existingTypes=getCurrentTypes(enemies);
        List<CharacterTypes>missingTypes=new ArrayList<>();
        for (CharacterTypes type:enemySuppliers.keySet()){
            if (!existingTypes.contains(type)){
                missingTypes.add(type);
            }
        }
        if (missingTypes.isEmpty()){
            return enemySuppliers.get(getRandomEnemy()).get().build(rows,cols);
        }
        else{
            return enemySuppliers.get(missingTypes.get(random.nextInt(missingTypes.size()))).get().build(rows,cols);
        }
    }
    private CharacterTypes getRandomEnemy(){
        List<CharacterTypes> options = new ArrayList<>(enemySuppliers.keySet());
        return options.get(random.nextInt(enemySuppliers.size()));
    }
    private List<CharacterTypes> getCurrentTypes(List<Enemy>enemies){
        List<CharacterTypes> existingTypes = new ArrayList<>();
        for (Enemy enemy:enemies){
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
