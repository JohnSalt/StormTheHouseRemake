import processing.core.PApplet;
import java.util.ArrayList;

public class Base {
    PApplet p = new PApplet();
    int x1,x2,x3,x4,y1,y2,health;
    Window window = new Window(p);

    public Base() {
        x1 = 720;
        x2 = 750;
        x3 = 895;
        x4 = 925;
        y1 = 210;
        y2 = 560;
        this.health = 1000;
    }
    public void drawBase(PApplet parent) {
        parent.fill(139,69,19);
        parent.quad(x1,y1,x2,y1,x4,y2,x3,y2);
    }

    public int getSlope() { //y = -2x + 1650 //(y-1650)/-2
        return (y2-y1)/(x3-x1);
    }
    public void setHealth(int damage) {
        this.health -= damage;
    }
    public int getHealth() {
        return health;
    }

}
