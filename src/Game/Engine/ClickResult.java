package Game.Engine;
/**This class represents the results returned from handling a click
 * contains the type of action happened and damage to player and enemy if the action
 * was an attack
 **/
public class ClickResult {
    public enum Type { MOVE, ATTACK, PICKUP, NONE };
    private final Type type;
    private final int damageToEnemy;
    private final int damageToPlayer;
    /**
     *
     * @param t represents the action happened - attack,move,none,pickup - enums.
     * @param damageToEnemy enemy damage, 0 if none
     * @param damageToPlayer player damage, 0 if none
     */
    public ClickResult(Type t,int damageToEnemy,int damageToPlayer){
        this.type=t;
        this.damageToEnemy=damageToEnemy;
        this.damageToPlayer=damageToPlayer;
    }
    public Type getType(){
        return type;
    }
    public int getDamageToEnemy(){
        return damageToEnemy;
    }
    public int getDamageToPlayer(){
        return damageToPlayer;
    }
}
