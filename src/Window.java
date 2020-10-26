import processing.core.PApplet;
import processing.core.PImage;
public class Window {
    private PApplet p;
    private int windowWidth = 1024, windowHeight = windowWidth/16*9;  //16:9 aspect ratio
    public PImage background, ammoButton;
    public Window(PApplet parent) {
        p = parent;
    }

    public void drawComponents(PApplet parent) {
        p = parent;
        parent.stroke(0);
        parent.fill(250);
        parent.rect(0,0,1024,23);
        parent.noStroke();
        parent.fill(201, 201, 201);
        parent.stroke(0);
        parent.rect(windowWidth/2+100, 5,140,12); //healthBar
        parent.fill(0);
        parent.stroke(0);
        parent.rect(5, 5,140,12); //ammo bar
    }

    public int getWindowWidth() {
        return windowWidth;
    }
    public int getWindowHeight() {
        return windowHeight;
    }

}
