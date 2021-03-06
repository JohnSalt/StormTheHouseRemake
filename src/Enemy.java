import processing.core.PImage;
import java.util.ArrayList;
import java.util.Random;
public class Enemy {
    private int speed, startingY, currentX, health, speedModifier = 2;
    Random r = new Random();
    public ArrayList<PImage> images;

    public Enemy() {
        this.speed = r.nextInt(3)+speedModifier;
        this.startingY = r.nextInt(326)+192;
        this.currentX = r.nextInt((100)+20)*-1;
        this.health = 2;
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
    public int getHealth() {
        return health;
    }
    public int getSpeedModifier() {
        return speedModifier;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void decreaseHealth(int damage) {
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
//add internal timer to each and check each enemy if speed equals zero,start timer and based on intervals of that do animation and every 1000 millis attack
