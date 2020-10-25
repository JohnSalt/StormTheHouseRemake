import processing.core.PApplet;


public class Base {

    int x1,x2,x3,x4,y1,y2,health,maxHealth;


    public Base() {
        x1 = 720;
        x2 = 750;
        x3 = 895;
        x4 = 925;
        y1 = 210;
        y2 = 560;
        this.health = 100;
        this.maxHealth = 100;
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
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
    public int getHealth() {
        return health;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public void increaseMaxHealth(int amount) {
        maxHealth += amount;
    }
    public void increaseHealth(int amount) {
        health += amount;
    }
}
