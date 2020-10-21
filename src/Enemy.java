import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;
public class Enemy {
    private PApplet p;
    private int speed, startingY, currentX, health;
    private Random r = new Random();


    public Enemy() {
        this.speed = r.nextInt(10)+4;
        this.startingY = r.nextInt(326)+192;
        this.currentX = r.nextInt(300)*-1;
        this.health = 2;
    }
    public Enemy(PApplet parent) {
        p = parent;
    }
    public Enemy(int speed, int currentX) {
        this.speed = speed;
        this.startingY = r.nextInt(376)+192;
        this.currentX = currentX;

    }

    public void drawEnemy(PApplet parent, Enemy e) {
        parent.fill(255,0,0);
        parent.rectMode(parent.CORNER);
        parent.rect(currentX,startingY,20,50);
    }


    public int getCurrentX() {
        return currentX;
    }
    public int getStartingY() {
        return startingY;
    }
    public int getSpeed() {
        return speed;
    }
    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }
    public void setStartingY(int startingY) {
        this.startingY = startingY;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void setHealth(int damage) {
        this.health -= damage;
    }
    public int getHealth() {
        return health;
    }
    public String toString() {
        return "X: " + currentX + " Y: " + startingY + " Speed: " + speed;
    }

    public void move() {
        currentX += speed;
    }

}
