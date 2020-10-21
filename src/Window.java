import processing.core.PApplet;

public class Window {
    private PApplet p;
    private int windowWidth = 1024, windowHeight = windowWidth/16*9;  //16:9 aspect ratio

    public Window(PApplet parent) {
        p = parent;
    }

    public void drawBackground(PApplet parent) {
        p = parent;
        parent.noStroke();
        parent.background(0,191,255);
        parent.fill(184,134,11);
        parent.rect(0, windowHeight/3,windowWidth,windowHeight/3*2);
    }

    public int getWindowWidth() {
        return windowWidth;
    }
    public int getWindowHeight() {
        return windowHeight;
    }
}
