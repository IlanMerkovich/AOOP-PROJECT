package Game.GUI;

import javax.swing.*;
import java.awt.*;

public class HealthBar extends JComponent {
    private final int currentHp;
    private final int max;
    public HealthBar(int current, int max) {
        this.currentHp =current;
        this.max=max;
        setPreferredSize(new Dimension(64,6));
    }
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        g.setColor(Color.GRAY);
        g.fillRect(0,0,width,height);
        float hpPercent;
        if (max > 0) {
            hpPercent = (float) currentHp / max;
        } else {
            hpPercent = 0;
        }
        if (hpPercent < 0) {
            hpPercent = 0;
        } else if (hpPercent > 1) {
            hpPercent = 1;
        }
        int filled = (int)(width * hpPercent);
        Color col;
        if(hpPercent > 0.7)
            col = Color.GREEN;
        else if(hpPercent > 0.3)
            col = Color.ORANGE;
        else
            col = Color.RED;

        g.setColor(col);
        g.fillRect(0,0,filled,height);
    }
}
