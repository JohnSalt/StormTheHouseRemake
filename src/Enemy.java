import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;
public class Enemy {
    private PApplet p;
    private int speed, startingY, currentX, health, randomChance, speedModifier = 4;
    private Random r = new Random();
    public ArrayList<PImage> images;

    public Enemy() {
        this.speed = r.nextInt(4)+speedModifier/2;
        this.startingY = r.nextInt(326)+192;
        randomChance = r.nextInt(100)+1;
        if (randomChance>50) {
            this.currentX = r.nextInt(300) * -1;
        } else {
            this.currentX = (r.nextInt(800)+300)*-1;
        }
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

    /*public void drawEnemy(PApplet parent, Enemy e) {   //use to see basic hitbox;
        //parent.fill(255,0,0);
        //parent.rectMode(parent.CORNER);
        //parent.rect(currentX,startingY,20,50);
    }*/


    public int getCurrentX() {
        return currentX;
    }
    public int getStartingY() {
        return startingY;
    }
    public int getSpeed() {
        return speed;
    }
    public int getHealth() {
        return health;
    }
    public int getSpeedModifier() {
        return speedModifier;
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

    public void setSpeedModifier(int value) {
        this.speedModifier = value;
    }

    public String toString() {
        return "X: " + currentX + " Y: " + startingY + " Speed: " + speed;
    }

    public void move() {                    //changes their position based on speed
        currentX += speed;
    }
    public void setImages(PImage img) {     //used to change image each frame
        images.add(img);
    }


}
