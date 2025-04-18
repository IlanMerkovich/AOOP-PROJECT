import Game.Engine.GameWorld;

public class Main {
    public static void main(String[] args){
        GameWorld gameWorld=new GameWorld(15,15,"Ilan",1);
        gameWorld.gameLoop();
    }
}
