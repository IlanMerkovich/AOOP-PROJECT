package Game.Engine;

import java.util.Stack;

public class GameWorldCareTaker {
    private final Stack<GameWorldMemento> stack=new Stack<>();
    public void saveMemento(GameWorldMemento memento){
        stack.push(memento);
    }
    public GameWorldMemento loadMemento(){
        if (!stack.isEmpty()){
            return stack.pop();
        }
        else{
            return null;
        }
    }
    public boolean previousLoads(){
        return stack.isEmpty();
    }
}
