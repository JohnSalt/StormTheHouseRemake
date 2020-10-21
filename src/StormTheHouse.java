import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class StormTheHouse extends PApplet {

    Random r = new Random();
    int day = 0, textSizeModifier = 100, power = 1;
    Window window = new Window(this);
    ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Integer> speeds;
    private boolean isMouseEnabled = true;
    Base base = new Base();


    public static void main(String[] args) {
        PApplet.main("StormTheHouse");
    }

    public void setup() {
        window.drawBackground(this);
        frameRate(10);

    }

    public void settings() {
        size(window.getWindowWidth(), window.getWindowHeight());
    }

    public void draw() {
        window.drawBackground(this);
        base.drawBase(this);
        textSize(30);
        text("Day: " + day, window.getWindowWidth()-textSizeModifier, 35);

        if (enemies.size() == 0) {
            System.out.println("Enemies list empty");
            day++;
            if (day%power == 0) {
                textSizeModifier += 15;
                power*=10;
            }
            createEnemies();
        }
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).drawEnemy(this,enemies.get(i));
        }
        moveAll();
        checkWallCollision();
        damageWall();
        text("Wall: " + base.getHealth() + "/100",window.getWindowWidth()-230,75);
        if (base.getHealth() < 0) {
            text("Wall: " + 0 + "/100",window.getWindowWidth()-230,75);
        }
        updateHealthBar();
        checkForLoss();
    }
    public void mousePressed() {
        if (isMouseEnabled) {
            for (int i = 0; i < enemies.size(); i++) {
                if (mouseX >= enemies.get(i).getCurrentX() - 15 && mouseX <= enemies.get(i).getCurrentX() + 25 && mouseY >= enemies.get(i).getStartingY() && mouseY <= enemies.get(i).getStartingY() + 50) {
                    enemies.get(i).setHealth(1);
                    if (enemies.get(i).getHealth() == 0) {
                        enemies.remove(i);
                    }
                }
            }
        }
    }
    public void checkForLoss() {
        for (int i = 0; i < enemies.size(); i++) {
            if (base.getHealth() <= 0) {
                noLoop();
                isMouseEnabled = false;
                textSize(30);
                text("GAME OVER", window.getWindowWidth() / 2 - 105, window.getWindowHeight() / 2 + 5);
            }
        }
    }
    public void keyPressed() {
        if (key == 'p') {
            loop();
            isMouseEnabled = true;
        }
        if (key == 'o') {
            noLoop();
            isMouseEnabled = false;
        }
        if (key == ESC) {
            exit();
        }
        if (key == 'k') {
            for (int i = 0; i < enemies.size(); i++) {
                enemies.remove(i); //removes enemies DEBUG
            }
        }
    }

    public void createEnemies() {
        for (int i = 0; i < day*2; i++) {
            enemies.add(new Enemy());
            enemies.get(i).drawEnemy(this, enemies.get(i));
        }
    }
    public void moveAll() {
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).move();
        }
    }
    public void checkWallCollision() {  //makes the wall a barrier, changes speed to 0
        int wallX;
        for (int i = 0; i < enemies.size(); i++) {
            wallX = (enemies.get(i).getStartingY()+1200)/base.getSlope();

            if (enemies.get(i).getCurrentX() >= wallX) {
                enemies.get(i).setSpeed(0);
            }
        }
    }
    public void damageWall() {
        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).getSpeed() == 0) {
                if (frameCount%10 == 0) {
                    base.setHealth(1);
                }
            }
        }
    }
    public void updateHealthBar() {
        float healthBarLength = (float)base.getHealth()/100*140;
        stroke(0);
        fill(207, 209, 88);
        if (healthBarLength >= 0) {
            rect(window.getWindowWidth() / 2 - 70, 10, healthBarLength, 20);
        }
    }
}
