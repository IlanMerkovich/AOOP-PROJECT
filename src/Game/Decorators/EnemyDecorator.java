package Game.Decorators;
import Game.Characters.Enemy;
import Game.Combat.Combatant;
import Game.Combat.MagicElement;
import Game.Engine.GameController;
import Game.Items.Treasure;
import Game.Map.Position;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public abstract class EnemyDecorator extends Enemy {
    private Enemy decoratedEnemy;
    private GameController gameController;
    private AtomicBoolean isRunning;
    private final ReentrantLock lock = new ReentrantLock(true);

    public EnemyDecorator(Enemy enemy){
        decoratedEnemy = enemy;
        super(0, 0);
        enemy.markAsDecorated();
    }
    @Override
    public void init(GameController gameController, AtomicBoolean isRunning) {
        this.gameController = gameController;
        this.isRunning = isRunning;
    }
    @Override
    protected GameController getGameController() {
        return gameController;
    }
    @Override
    public void run() {
        if (!isRunning.get() || this.isDead()) {
            return;
        }
        if (!lock.tryLock()) {
            return;
        }
        try {
            if (gameController == null) {
                return;
            }

            Random random = new Random();
            if (random.nextInt(100) < 30) {
                double distanceToPlayer = gameController.getPlayer().getPosition().distanceTo(this.getPosition());
                if (distanceToPlayer > 1) {
                    gameController.attemptToMove(this);
                }
                else if (distanceToPlayer == 1) {
                    gameController.attackPlayer(gameController.getPlayer(), this);
                    try {
                        Thread.sleep(1500);
                    }
                    catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            }
            else {
                try {
                    Thread.sleep(500 + random.nextInt(1001));
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        finally {
            lock.unlock();
        }
    }
    public Enemy getDecoratedEnemy(){
        return decoratedEnemy;
    }
    @Override
    public Position getPosition() {
        return decoratedEnemy.getPosition();
    }
    @Override
    public void setPosition(Position position) {
        decoratedEnemy.setPosition(position);
    }
    @Override
    public int getHealth() {
        return decoratedEnemy.getHealth();
    }
    @Override
    public boolean setHealth(int health) {
        return decoratedEnemy.setHealth(health);
    }
    @Override
    public int getPower() {
        return decoratedEnemy.getPower();
    }
    @Override
    public boolean isDead() {
        return decoratedEnemy.isDead();
    }
    @Override
    public void setVisible(boolean visible) {
        decoratedEnemy.setVisible(visible);
    }
    @Override
    public int getLoot() {
        return decoratedEnemy.getLoot();
    }
    @Override
    public void setLoot(int loot) {
        decoratedEnemy.setLoot(loot);
    }
    @Override
    public MagicElement getElement() {
        return decoratedEnemy.getElement();
    }
    @Override
    public double getAccuracy() {
        return decoratedEnemy.getAccuracy();
    }
    @Override
    public boolean tryEvade(Combatant attacker) {
        return decoratedEnemy.tryEvade(attacker);
    }
    @Override
    public boolean getVisibility() {
        return decoratedEnemy.getVisibility();
    }
    @Override
    public String getDisplaySymbol() {
        return decoratedEnemy.getDisplaySymbol();
    }
    @Override
    public String getType() {
        return decoratedEnemy.getType();
    }
    @Override
    public void attack(Combatant target){
        decoratedEnemy.attack(target);
    }
    @Override
    public void receiveDamage(int amount, Combatant source) {
        decoratedEnemy.receiveDamage(amount, source);
    }
    @Override
    public Enemy clone() {
        Enemy clonedBase = decoratedEnemy.clone();
        if (this instanceof ExplodingEnemyDecorator) {
            return new ExplodingEnemyDecorator(clonedBase);
        } else if (this instanceof VampireEnemyDecorator) {
            return new VampireEnemyDecorator(clonedBase);
        } else if (this instanceof TeleportingEnemyDecorator) {
            return new TeleportingEnemyDecorator(clonedBase);
        }

        return clonedBase;
    }
    @Override
    public Treasure Defeat() {
        return decoratedEnemy.Defeat();
    }
    @Override
    public String toString(){
        return decoratedEnemy.toString();
    }
    @Override
    public boolean isDecorated() {
        return true;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof EnemyDecorator other) {
            return this.unwrap().equals(other.unwrap());
        }
        return unwrap().equals(obj);
    }
    @Override
    public int hashCode() {
        return unwrap().hashCode();
    }
    public Enemy unwrap() {
        if (decoratedEnemy instanceof EnemyDecorator decorator) {
            return decorator.unwrap();
        }
        return decoratedEnemy;
    }
}