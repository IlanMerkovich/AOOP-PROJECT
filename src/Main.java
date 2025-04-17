import Game.Engine.GameWorld;

public class Main {
    public static void main(String[] args){
        GameWorld gameWorld=new GameWorld(10,10,"Ilan",3);
        gameWorld.gameLoop();
    }
}
