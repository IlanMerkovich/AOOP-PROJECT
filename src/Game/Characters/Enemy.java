package Game.Characters;


import Game.Engine.GameController;
import Game.Items.Treasure;
import Game.Logs.LogManager;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Enemy extends AbstractCharacter implements Runnable{
    private int loot;
    private GameController gameController;
    private AtomicBoolean isRunning;
    private final ReentrantLock lock = new ReentrantLock(true);
    private boolean isDecorated;

    public Enemy(int r,int c){
        super(r,c);
        this.setHealth(50);
        this.loot= new Random().nextInt(201) + 100;
        isDecorated=false;
    }

    public boolean setHealth(int health) {
        if (health < 0) {
            return super.setHealth(0);
        }
        if (health > 50) {
            return super.setHealth(50);
        }
        return super.setHealth(health);
    }

    public void Heal(int amount){
        if (this.getHealth()+amount>50){
            this.setHealth(50);
        }
        else{
            this.setHealth(this.getHealth()+amount);
        }
    }

    public String toString() {
        return String.format("👾 %s | Loot: %d",
                super.toString(),
                loot);
    }

    public Treasure Defeat(){
        lock.lock();
        try {
            LogManager.addLog("Enemy was killed at "+getPosition());
            return new Treasure(this.getPosition().getRow(), this.getPosition().getCol(), loot);
        }
        finally {
            lock.unlock();
        }
    }
    public boolean equals(Object obj) {
        if (!(obj instanceof Enemy)) {
            return false;
        }
        Enemy other = (Enemy) obj;
        return this.getPosition().equals(other.getPosition()) &&
                this.getHealth() == other.getHealth() &&
                this.getPower() == other.getPower() &&
                this.loot == other.loot;
    }

    public void init(GameController gameController,AtomicBoolean isRunning) {
        this.gameController = gameController;
        this.isRunning=isRunning;
    }
    protected GameController getGameController() {
        return gameController;
    }
    public boolean isDecorated() {
        return isDecorated;
    }

    @Override
    public void run() {
        if (!isRunning.get() || this.isDead() || this.isDecorated()) {
            return;
        }
        if (!lock.tryLock()){
            return;
        }
        try{
            Random random = new Random();
            if (this.isDecorated()) {
                return;
            }

            else if (random.nextInt(100) < 5) {
                gameController.requestToDecorate(this);
                return;
            }
            if (random.nextInt(100) < 30) {
                if (gameController.getPlayer().getPosition().distanceTo(this.getPosition())>1){
                    gameController.attemptToMove(this);
                }
                else if (gameController.getPlayer().getPosition().distanceTo(this.getPosition())==1){
                    gameController.attackPlayer(gameController.getPlayer(),this);
                    try {
                        Thread.sleep(1500);
                    }
                    catch (InterruptedException e){
                        System.out.println(e);
                    }
                }
            }
            else{
                try {
                    Thread.sleep(500+random.nextInt(1001));
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
    protected void copyEnemyFieldsTo(Enemy target) {
        this.copyFieldsTo(target);
        target.loot = this.loot;
    }
    public void markAsDecorated() {
        this.isDecorated = true;
    }
    public void setLoot(int loot) {
        this.loot = loot;
    }
    public abstract String getType();

    @Override
    public abstract Enemy clone();

    public int getLoot(){
        return loot;
    }
    protected AtomicBoolean getIsRunning() {
        return isRunning;
    }

    protected ReentrantLock getLock() {
        return lock;
    }
}