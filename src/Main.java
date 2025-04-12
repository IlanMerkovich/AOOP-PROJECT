import Game.Characters.AbstractCharacter;
import Game.Characters.Dragon;
import Game.Characters.PlayerCharacter;
import Game.Characters.Warrior;
import Game.Items.Treasure;

public class Main {
    public static void main(String[] args) {
        Treasure t=new Treasure(1,1,450);
        PlayerCharacter player=new Warrior(1,2,"Ilan");
        t.interact(player);
        System.out.println(player.getTreasurePoints());

    }}